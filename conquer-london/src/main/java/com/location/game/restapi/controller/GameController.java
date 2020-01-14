package com.location.game.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.location.game.restapi.model.Coordinates;
import com.location.game.restapi.service.GameService;

import io.swagger.annotations.ApiOperation;

@RestController
public class GameController {

	@Autowired
	private GameService gameService;

	@ApiOperation(value = "Get a location point in a certain area", response = ResponseEntity.class)
	@GetMapping(value = "/locationPointByArea")
	public ResponseEntity<String> locationPointByArea(@RequestParam(value = "postcode") String postcode) {
		return gameService.locationPointByPostcode(postcode);
	}

	@ApiOperation(value = "Conquer a location point", response = ResponseEntity.class)
	@PostMapping(value = "/conquerLocationPoint")
	public ResponseEntity<String> conquerLocationPoint(@RequestParam(value = "userId") String userId,
			@RequestBody Coordinates currentCoordinates,
			@RequestParam(value = "conquerLocationPointNumber") String conquerLocationPointNumber) {
		return gameService.conquerLocationPoint(userId, currentCoordinates, conquerLocationPointNumber);
	}

	@ApiOperation(value = "Show score", response = ResponseEntity.class)
	@GetMapping(value = "/showScore")
	public ResponseEntity<String> showScore(@RequestParam(value = "userId") String userId) {
		return gameService.showScore(userId);
	}

}
