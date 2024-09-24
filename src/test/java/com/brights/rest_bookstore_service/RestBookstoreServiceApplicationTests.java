package com.brights.rest_bookstore_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestBookstoreServiceApplicationTests {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	@Test
	void contextLoads() {
	}

	@Test
	void testGetBooks() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/book"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.content().string(containsString("Spring Boot 2 Recipes")))
			.andExpect(MockMvcResultMatchers.content().string(containsString("Cloud-Native Applications in Java")))
			.andExpect(MockMvcResultMatchers.content().string(containsString("Spring Boot in Action")));
	}

	@Test
	void testBookById() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/book/1"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.content().string(containsString("Spring Boot in Action")));
	}

	@Test
	void testPostBook() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/book"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().string(not(containsString("New Book"))));

		mvc.perform(
			MockMvcRequestBuilders.post("/book")
				.content(mapper.writeValueAsString(new Book("New Book", "New Author", 10)))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
		)
		.andExpect(status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.content().string(containsString("New Book")));

		mvc.perform(
			MockMvcRequestBuilders.get("/book")
		)
		.andExpect(status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.content().string(containsString("New Book")));
	}

	@Test
		void testUpdateBook() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("/book/8"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(MockMvcResultMatchers.content().string(containsString("Cloud-Native Applications in Java")));

			String updatedBookJson = "{ \"title\": \"Updated Spring Boot Title\", \"author\": \"Craig Walls\", \"price\": 40 }";

		mvc.perform(MockMvcRequestBuilders.put("/book/8")
						.content(updatedBookJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());

		mvc.perform(MockMvcRequestBuilders.get("/book/8"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().string(containsString("Updated Spring Boot Title")));

	}

	@Test
	void testDeleteBook() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/book/8"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().string(containsString("Updated Spring Boot Title")));

		mvc.perform(MockMvcRequestBuilders.delete("/book/8"))
				.andExpect(status().is2xxSuccessful());

		mvc.perform(MockMvcRequestBuilders.get("/book"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().string(not(containsString("Updated Spring Boot Title"))));
	}
}
