package com.location.game.restapi.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.location.game.restapi.configuration.ManageDatabase;
import com.location.game.restapi.model.Coordinates;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ManageDatabase manageDatabase;

	private static final String locationPointByAreaUrl = "/locationPointByArea";;
	private static final String conquerLocationPointUrl = "/conquerLocationPoint";
	private static final String showScoreUrl = "/showScore";

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@After
	public void clearUp() {
		manageDatabase.deleteLocationPoints();
	}

	/*
	 * Test URL '/locationPointByArea'
	 */
	@Test
	public void givenEmptyDBAndPostcode_whenLocationPointByArea_thenAssertDatabaseEmpty() throws Exception {
		this.mockMvc.perform(get(locationPointByAreaUrl).param("postcode", "WC1E 6JL")).andDo(print())
				.andExpect(status().isIAmATeapot())
				.andExpect(content().string(containsString("Empty database, please come back later.")));
	}

	@Test
	public void givenDBAndInvalidPostcode_whenLocationPointByArea_thenAssertCoordinatesNotFound() throws Exception {
		manageDatabase.initialiseDB();
		this.mockMvc.perform(get(locationPointByAreaUrl).param("postcode", "G32 8EETG")).andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Coordinates not found for postcode: ")));
	}

	@Test
	public void givenDBAndPostcodeFarAway_whenLocationPointByArea_thenAssertCoordinatesNotFound() throws Exception {
		manageDatabase.initialiseDB();
		this.mockMvc.perform(get(locationPointByAreaUrl).param("postcode", "G32 8TG")).andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Location point was not found in the area.")));
	}

	@Test
	public void givenDBAndPostcode_whenLocationPointByArea_thenAssertLocationPoint() throws Exception {
		manageDatabase.initialiseDB();
		this.mockMvc.perform(get(locationPointByAreaUrl).param("postcode", "WC1E 6JL")).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString("The location point is")));
	}

	/*
	 * Test URL '/conquerLocationPoint'
	 */
	@Test
	public void givenEmptyDB_whenConquerLocationPoint_thenAssertDatabaseEmpty() throws Exception {
		Gson gson = new Gson();
		String coordinates = gson.toJson(Coordinates.builder().lng("-0.1355294").lat("51.5235359"));
		this.mockMvc
				.perform(post(conquerLocationPointUrl).param("userId", "ela").contentType(APPLICATION_JSON_UTF8)
						.content(coordinates).param("conquerLocationPointNumber", "108042"))
				.andDo(print()).andExpect(status().isIAmATeapot())
				.andExpect(content().string(containsString("Empty database, please come back later.")));
	}

	@Test
	public void givenDBAndMissingLocationPoint_whenConquerLocationPoint_thenAssertPointNotFound() throws Exception {
		manageDatabase.initialiseDB();
		Gson gson = new Gson();
		String coordinates = gson.toJson(Coordinates.builder().lng("-0.1355294").lat("51.5235359"));
		this.mockMvc
				.perform(post(conquerLocationPointUrl).param("userId", "ela").contentType(APPLICATION_JSON_UTF8)
						.content(coordinates).param("conquerLocationPointNumber", "108042SD"))
				.andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Location point number doesn't exist in DB")));
		manageDatabase.deleteLocationPoints();
	}

	@Test
	public void givenDBAndWrongCoordinates_whenConquerLocationPoint_thenAssertWrongCoordinates() throws Exception {
		manageDatabase.initialiseDB();
		Gson gson = new Gson();
		String coordinates = gson.toJson(Coordinates.builder().lng("-0.1355DF29Adf4").lat("51.5235359"));
		this.mockMvc
				.perform(post(conquerLocationPointUrl).param("userId", "ela").contentType(APPLICATION_JSON_UTF8)
						.content(coordinates).param("conquerLocationPointNumber", "108042"))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Coordinates are not properly inserted!")));
		manageDatabase.deleteLocationPoints();
	}

	@Test
	public void givenDBAndCoordinatesAndConqueredPoint_whenConquerLocationPoint_thenAssertDistanceGreaterThanExpected()
			throws Exception {
		manageDatabase.initialiseDB();
		Gson gson = new Gson();
		String coordinates = gson.toJson(Coordinates.builder().lng("-0.1996078").lat("51.6008404"));
		this.mockMvc
				.perform(post(conquerLocationPointUrl).param("userId", "ela").contentType(APPLICATION_JSON_UTF8)
						.content(coordinates).param("conquerLocationPointNumber", "451152"))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Distance greater than 15 metre.")));
		manageDatabase.deleteLocationPoints();
	}

	@Test
	public void givenDBAndCoordinatesAndConqueredPoint_whenConquerLocationPoint_thenAssertAlreadyConquered()
			throws Exception {
		manageDatabase.initialiseDB();
		Gson gson = new Gson();
		String coordinates = gson.toJson(Coordinates.builder().lng("-0.1355294").lat("51.5235359"));
		this.mockMvc
				.perform(post(conquerLocationPointUrl).param("userId", "ela").contentType(APPLICATION_JSON_UTF8)
						.content(coordinates).param("conquerLocationPointNumber", "108042"))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Location point already conquered.")));
		manageDatabase.deleteLocationPoints();
	}

	@Test
	public void givenDBAndCoordinatesAndConqueredPoint_whenConquerLocationPoint_thenAssertPointConquered()
			throws Exception {
		manageDatabase.initialiseDB();
		Gson gson = new Gson();
		String coordinates = gson.toJson(Coordinates.builder().lng("-0.1946078").lat("51.6008404"));
		this.mockMvc
				.perform(post(conquerLocationPointUrl).param("userId", "ela").contentType(APPLICATION_JSON_UTF8)
						.content(coordinates).param("conquerLocationPointNumber", "451152"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(" conquered location point ")));
		manageDatabase.deleteLocationPoints();
	}

	/*
	 * Test URL '/showScore'
	 */
	@Test
	public void givenUserid_whenShowScore_thenAssertResponseMessage() throws Exception {
		manageDatabase.initialiseDB();
		this.mockMvc.perform(get(showScoreUrl).param("userId", "ela")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(" conquered ")));
	}

}
