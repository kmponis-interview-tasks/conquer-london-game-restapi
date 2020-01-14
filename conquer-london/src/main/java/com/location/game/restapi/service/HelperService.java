package com.location.game.restapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.location.game.restapi.model.Coordinates;
import com.location.game.restapi.model.LocationPoint;

public interface HelperService {

	/**
	 * The index page content
	 * 
	 * @return
	 */
	public ResponseEntity<String> indexPage();

	/**
	 * Import data from given json file into DB
	 * 
	 * @param userId
	 * @return
	 */
	public ResponseEntity<String> importDataJsonIntoDB(String userId);

	/**
	 * Find all Location Point
	 * 
	 * @return
	 */
	public List<LocationPoint> getAllLocationPoint();

	/**
	 * Create user if not exists. Used in all methods of GameService.
	 * 
	 * @param userId
	 */
	public void createUserIfNotExists(String userId);

	/**
	 * Increase user conquered points by 1
	 * 
	 * @param userId
	 */
	public void increaseConqueredPoints(String userId);

	/**
	 * Find Point By PostCode
	 * 
	 * @param postcode
	 * @return
	 */
	public Coordinates findPointByPostCode(String postcode);

	/**
	 * Calculate distance between two points.
	 * 
	 * @param longitude1
	 * @param latitude1
	 * @param longitude2
	 * @param latitude2
	 * @param unit       - default(miles), KM(kilometres), M(metres)
	 * @return
	 */
	public double distance(String longitude1, String latitude1, String longitude2, String latitude2, String unit);

}
