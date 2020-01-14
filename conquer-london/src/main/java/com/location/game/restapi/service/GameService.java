package com.location.game.restapi.service;

import org.springframework.http.ResponseEntity;

import com.location.game.restapi.model.Coordinates;

public interface GameService {

	/**
	 * 
	 * @return
	 */
	public ResponseEntity<String> locationPointByPostcode(String postcode);

	/**
	 * 
	 * @param userId
	 * @param coordinates
	 * @param locationPointNumber
	 * @return
	 */
	public ResponseEntity<String> conquerLocationPoint(String userId, Coordinates coordinates,
			String locationPointNumber);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public ResponseEntity<String> showScore(String userId);

}
