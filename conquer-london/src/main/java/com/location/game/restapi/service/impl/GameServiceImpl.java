package com.location.game.restapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.location.game.restapi.model.Coordinates;
import com.location.game.restapi.model.LocationPoint;
import com.location.game.restapi.repository.CoordinatesRepository;
import com.location.game.restapi.repository.LocationPointRepository;
import com.location.game.restapi.repository.UserRepository;
import com.location.game.restapi.service.GameService;
import com.location.game.restapi.service.HelperService;

@Component
public class GameServiceImpl implements GameService {

	@Autowired
	private LocationPointRepository locationPointRepository;

	@Autowired
	private CoordinatesRepository coordinatesRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HelperService helperService;

	@Override
	public ResponseEntity<String> locationPointByPostcode(String postcode) {
		if (coordinatesRepository.findAll().isEmpty()) {
			return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Empty database, please come back later.");
		}

		Coordinates coordinates;
		try {
			coordinates = helperService.findPointByPostCode(postcode);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coordinates not found for postcode: " + postcode);
		}

		List<Object[]> locationPoint = coordinatesRepository.findPointsWithinDistanceByCoordinations(coordinates.getLng(),
				coordinates.getLat(), 100);
		if (null == locationPoint || locationPoint.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location point was not found in the area.");
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body("The location point is " + locationPoint.get(0)[0] + ". Coordinates: { \"lng\": \""
						+ locationPoint.get(0)[1] + "\", \"lat\": \"" + locationPoint.get(0)[2] + "\" }");
	}

	@Override
	public ResponseEntity<String> conquerLocationPoint(String userId, Coordinates currentCoordinates,
			String conquerLocationPointNumber) {
		if (coordinatesRepository.findAll().isEmpty()) {
			return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Empty database, please come back later.");
		}

		helperService.createUserIfNotExists(userId);

		LocationPoint conquerLocationPoint = locationPointRepository.findByLocationPointNumber(conquerLocationPointNumber);
		if (null == conquerLocationPoint) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location point number doesn't exist in DB");
		}

		String longitude1 = conquerLocationPoint.getGeometry().getCoordinates().getLng();
		String latitude1 = conquerLocationPoint.getGeometry().getCoordinates().getLat();
		String longitude2 = currentCoordinates.getLng();
		String latitude2 = currentCoordinates.getLat();

		Double distance;
		try {
			distance = helperService.distance(longitude1, latitude1, longitude2, latitude2, "M");
		} catch (NumberFormatException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coordinates are not properly inserted!");
		}

		if (distance > 15) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + userId + " couldn't conquer location point "
					+ conquerLocationPointNumber + ".\nDistance greater than 15 metre.");
		} else if (conquerLocationPoint.getPropertiesInfo().getMarked()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + userId + " couldn't conquer location point "
					+ conquerLocationPointNumber + ".\nLocation point already conquered.");
		} else {
			conquerLocationPoint.getPropertiesInfo().setMarked(Boolean.TRUE);
			locationPointRepository.save(conquerLocationPoint);
			helperService.increaseConqueredPoints(userId);
			return ResponseEntity.status(HttpStatus.OK)
					.body("User " + userId + " conquered location point " + conquerLocationPointNumber);
		}
	}

	@Override
	public ResponseEntity<String> showScore(String userId) {
		helperService.createUserIfNotExists(userId);

		return ResponseEntity.status(HttpStatus.OK).body("User " + userId + " conquered "
				+ userRepository.findByUserId(userId).getConqueredPoints() + " location points.");
	}

}
