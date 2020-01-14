package com.location.game.restapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.location.game.restapi.model.Coordinates;
import com.location.game.restapi.model.Geometry;
import com.location.game.restapi.model.LocationPoint;
import com.location.game.restapi.model.PropertiesInfo;
import com.location.game.restapi.repository.LocationPointRepository;

@Service
public class ManageDatabase {

	@Autowired
	private LocationPointRepository locationPointRepository;

	/**
	 * Import Location Points
	 */
	public void initialiseDB() {
		// postcode: WC1E 6JL
		this.saveLocationPointOnDatabase("-0.1355294", "51.5235359", Boolean.TRUE, "108042");
		// postcode: N3 1DH
		this.saveLocationPointOnDatabase("-0.1946078", "51.6008404", Boolean.FALSE, "451152");
	}

	/**
	 * Delete all Location points
	 */
	public void deleteLocationPoints() {
		locationPointRepository.deleteAll();
	}

	/**
	 * Saves location point in DB using locationPointRepository
	 * 
	 * @param lng
	 * @param lat
	 * @param marked
	 * @param locationPointNumber
	 */
	private void saveLocationPointOnDatabase(String lng, String lat, Boolean marked, String locationPointNumber) {
		Coordinates coordinates = Coordinates.builder().lng(lng).lat(lat).build();
		Geometry geometry = Geometry.builder().type("Point").coordinates(coordinates).build();
		PropertiesInfo propertiesInfo = PropertiesInfo.builder().marked(marked).build();
		LocationPoint locationPoint = LocationPoint.builder().locationPointNumber(locationPointNumber).type("Feature")
				.geometry(geometry).propertiesInfo(propertiesInfo).build();
		locationPointRepository.save(locationPoint);
	}

}
