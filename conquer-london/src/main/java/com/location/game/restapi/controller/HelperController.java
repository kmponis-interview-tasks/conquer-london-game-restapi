package com.location.game.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.location.game.restapi.model.LocationPoint;
import com.location.game.restapi.service.HelperService;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class HelperController {

	@Autowired
	private HelperService helperService;

	@ApiOperation(value = "Conquer london homepage", response = ResponseEntity.class)
	@GetMapping(value = "/")
	public ResponseEntity<String> index() {
		return helperService.indexPage();
	}

	@ApiOperation(value = "Initialise DB with Location Points", response = ResponseEntity.class)
	@GetMapping(value = "/initialiseDB/{userId}")
	public ResponseEntity<String> initialiseDB(@PathVariable("userId") String userId) {
		return helperService.importDataJsonIntoDB(userId);
	}

	@ApiOperation(value = "Get all Location Points", response = LocationPoint.class)
	@GetMapping(value = "/allLocationPoint")
	public List<LocationPoint> getAllLocationPoint() {
		return helperService.getAllLocationPoint();
	}

}
