package com.location.game.restapi.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.location.game.restapi.configuration.ManageDatabase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelperControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ManageDatabase manageDatabase;

	private static final String indexUrl = "/";
	private static final String initialiseDBUrl = "/initialiseDB/{name}";
	private static final String getAllLocationPointUrl = "/allLocationPoint";

	@After
	public void clearUp() {
		manageDatabase.deleteLocationPoints();
	}

	/*
	 * Test URL '/'
	 */
	@Test
	public void givenEmptyDB_whenIndex_thenAssertResponse() throws Exception {
		this.mockMvc.perform(get(indexUrl)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Conquer London Home page. Visit /swagger-ui.html to play!")));
	}

	/*
	 * Test URL '/initialiseDB/{userId}'
	 */
	@Test
	public void givenEmptyDBAndWrongUser_whenInitialiseDB_thenAssertNotAuthorised() throws Exception {
		this.mockMvc.perform(get(initialiseDBUrl, "polert_user")).andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Not authorised!")));
		manageDatabase.deleteLocationPoints();
	}

	@Test
	public void givenDBAndUser_whenInitialiseDB_thenAssertAlreadyInitialised() throws Exception {
		manageDatabase.initialiseDB();
		this.mockMvc.perform(get(initialiseDBUrl, "admin")).andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("DB already initialised")));
		manageDatabase.deleteLocationPoints();
	}

	@Test
	public void givenEmptyDBAndUser_whenInitialiseDB_thenAssertInitialiseDB() throws Exception {
		this.mockMvc.perform(get(initialiseDBUrl, "admin")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Initialise DB")));
		manageDatabase.deleteLocationPoints();
	}

	/*
	 * Test URL '/initialiseDB/{userId}'
	 */
	@Test
	public void givenEmptyDB_whenAllLocationPoint_thenAssertEmpty() throws Exception {
		this.mockMvc.perform(get(getAllLocationPointUrl)).andDo(print()).andExpect(content().string("[]"));
	}

	@Test
	public void givenDB_whenAllLocationPoint_thenAssertLocationPoints() throws Exception {
		manageDatabase.initialiseDB();
		this.mockMvc.perform(get(getAllLocationPointUrl)).andDo(print()).andExpect(content()
				.string(containsString("\"locationPointNumber\":\"108042\",\"type\":\"Feature\",\"geometry\":{\"id\":")));
		manageDatabase.deleteLocationPoints();
	}

}
