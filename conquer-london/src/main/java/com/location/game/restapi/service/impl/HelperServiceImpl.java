package com.location.game.restapi.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.location.game.restapi.exception.ConsumeRestapiException;
import com.location.game.restapi.model.Coordinates;
import com.location.game.restapi.model.Geometry;
import com.location.game.restapi.model.LocationPoint;
import com.location.game.restapi.model.PropertiesInfo;
import com.location.game.restapi.model.User;
import com.location.game.restapi.repository.LocationPointRepository;
import com.location.game.restapi.repository.UserRepository;
import com.location.game.restapi.service.HelperService;

@Component
public class HelperServiceImpl implements HelperService {

	private static final Logger LOGGER = Logger.getLogger(HelperServiceImpl.class.getName());

	@Autowired
	private LocationPointRepository locationPointRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public ResponseEntity<String> indexPage() {
		return ResponseEntity.status(HttpStatus.OK).body("Conquer London Home page. Visit /swagger-ui.html to play!");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResponseEntity<String> importDataJsonIntoDB(String userId) {
		if (!"admin".equals(userId)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorised!");
		} else if (!locationPointRepository.findAll().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB already initialised");
		}

		try (FileReader reader = new FileReader("data.json")) {
			// Read JSON file
			Object dataJson = new JSONParser().parse(reader);
			JSONObject dataJsonObject = (JSONObject) dataJson;
			JSONObject featuresJsonObject = (JSONObject) dataJsonObject.get("features");
			Set featuresSet = featuresJsonObject.entrySet();

			Iterator value = featuresSet.iterator();
			while (value.hasNext()) {
				String locationPointTemp = value.next().toString();
				String locationPointString = locationPointTemp.substring(locationPointTemp.lastIndexOf('=') + 1);
				locationPointRepository.save(this.createLocationPointFromImported(locationPointString));
			}
		} catch (IOException | ParseException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		return ResponseEntity.status(HttpStatus.OK).body("Initialise DB");
	}

	@Override
	public List<LocationPoint> getAllLocationPoint() {
		return locationPointRepository.findAll();
	}

	@Override
	public void createUserIfNotExists(String userId) {
		if (null == userRepository.findByUserId(userId)) {
			userRepository.save(User.builder().userId(userId).build());
		}
	}

	@Override
	public void increaseConqueredPoints(String userId) {
		User user = userRepository.findByUserId(userId);
		user.setConqueredPoints(user.getConqueredPoints() + 1);
		userRepository.save(user);
	}

	@Override
	public Coordinates findPointByPostCode(String postcode) {
		try {
			URL url = new URL("http://api.postcodes.io/postcodes/" + postcode);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new ConsumeRestapiException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			StringBuilder addressDetailsBuilder = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				addressDetailsBuilder.append(output);
			}

			JsonObject coordinatesObject = new Gson().fromJson(addressDetailsBuilder.toString(), JsonObject.class);
			String longitude = coordinatesObject.get("result").getAsJsonObject().get("longitude").getAsString();
			String latitude = coordinatesObject.get("result").getAsJsonObject().get("latitude").getAsString();

			Coordinates coordinates = Coordinates.builder().lng(longitude).lat(latitude).build();

			conn.disconnect();
			return coordinates;
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		return null;
	}

	@Override
	public double distance(String longitude1, String latitude1, String longitude2, String latitude2, String unit) {
		double lon1 = Double.parseDouble(longitude1);
		double lat1 = Double.parseDouble(latitude1);
		double lon2 = Double.parseDouble(longitude2);
		double lat2 = Double.parseDouble(latitude2);

		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		} else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
					+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515; // miles
			if ("KM".equals(unit)) { // kilometres
				dist = dist * 1.609344;
			} else if ("M".equals(unit)) { // metres
				dist = dist * 1.609344 * 1000;
			}
			return (dist);
		}
	}

	/**
	 * Reformat imported JSON object.
	 * 
	 * @param locationPointString
	 * @return
	 * @throws ParseException
	 */
	private LocationPoint createLocationPointFromImported(String locationPointString) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONObject locationPointJson = (JSONObject) jsonParser.parse(locationPointString);

		// Create Coordinates and Geometry
		String geometryString = locationPointJson.get("geometry").toString();
		JSONObject geometryJson = (JSONObject) jsonParser.parse(geometryString);
		String geometryType = geometryJson.get("type").toString();
		JSONArray coordinatesArray = (JSONArray) geometryJson.get("coordinates");
		String longitude = coordinatesArray.get(0).toString();
		String latitude = coordinatesArray.get(1).toString();
		Coordinates coordinates = Coordinates.builder().lng(longitude).lat(latitude).build();
		Geometry geometry = Geometry.builder().type(geometryType).coordinates(coordinates).build();

		// Create PropertiesInfo
		String propertiesString = locationPointJson.get("properties").toString();
		JSONObject propertiesJson = (JSONObject) jsonParser.parse(propertiesString);
		String marked = propertiesJson.get("marked").toString();
		PropertiesInfo propertiesInfo = PropertiesInfo.builder().marked(Boolean.valueOf(marked)).build();

		// Create and return LocationPoint
		String locationPointType = locationPointJson.get("type").toString();
		String locationPointNumber = propertiesJson.get("id").toString();
		return LocationPoint.builder().locationPointNumber(locationPointNumber).type(locationPointType).geometry(geometry)
				.propertiesInfo(propertiesInfo).build();
	}

}
