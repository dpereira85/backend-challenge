package com.invillia.acme.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invillia.acme.InvilliaApplication;
import com.invillia.acme.model.Store;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = InvilliaApplication.class)
@AutoConfigureMockMvc
public class StoreControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void createStore_givenValidArguments_thenStatus201Created() throws Exception {

		// Given
		Store newStore = new Store("Vitória", "Centro, Vitória/ES");
		String jsonStore = objectMapper.writeValueAsString(newStore);

		// Then assert
		mockMvc.perform(post("/stores/")
			.accept(MediaType.APPLICATION_JSON)
			.content(jsonStore)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void createStore_givenNoArguments_thenStatus400BadRequest() throws Exception {
		// Given
		Store newStore = new Store();
		String jsonStore = objectMapper.writeValueAsString(newStore);

		// Then assert
		mockMvc.perform(post("/stores/")
			.accept(MediaType.APPLICATION_JSON)
			.content(jsonStore)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void createStore_givenOnlyName_thenStatus400BadRequest() throws Exception {
		// Given
		Store newStore = new Store("Vitória", null);
		String jsonStore = objectMapper.writeValueAsString(newStore);

		// Then assert
		mockMvc.perform(post("/stores/")
			.accept(MediaType.APPLICATION_JSON)
			.content(jsonStore)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void createStore_givenOnlyAddress_thenStatus400BadRequest() throws Exception {
		// Given
		Store newStore = new Store(null, "Centro, Vitória/ES");
		String jsonStore = objectMapper.writeValueAsString(newStore);

		// Then assert
		mockMvc.perform(post("/stores/")
			.accept(MediaType.APPLICATION_JSON)
			.content(jsonStore)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}


	@Test
	public void updateStore_givenValidArguments_thenStatus200Ok() throws Exception {
		// Given
		String uuid = "49fc24c8-d7e9-4b82-9bb9-cf476877a081";
		Store newStore = new Store("Vitória", "Centro, Vitória/ES");
		String jsonStore = objectMapper.writeValueAsString(newStore);

		// Then assert
		mockMvc.perform(put("/stores/" + uuid)
			.accept(MediaType.APPLICATION_JSON)
			.content(jsonStore)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void updateStore_givenInvalidUUID_thenStatus400BadRequest() throws Exception {
		
		// Given
		String uuid = "00acc00e";
		Store newStore = new Store("Vitória", "Centro, Vitória/ES");
		String jsonStore = objectMapper.writeValueAsString(newStore);

		// Then assert
		mockMvc.perform(put("/stores/" + uuid)
			.accept(MediaType.APPLICATION_JSON)
			.content(jsonStore)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void updateStore_givenNoStoreArguments_thenStatus400BadRequest() throws Exception {
		
		// Given
		String uuid = "49fc24c8-d7e9-4b82-9bb9-cf476877a081";
		Store newStore = new Store();
		String jsonStore = objectMapper.writeValueAsString(newStore);

		// Then assert
		mockMvc.perform(put("/stores/" + uuid)
			.accept(MediaType.APPLICATION_JSON)
			.content(jsonStore)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void retrieveStore_givenValidUUID_thenReturn200Ok() throws Exception {
		// Given
		String uuid = "00acc00e-2745-4ffe-b2be-2c461278ba3e";

		// Then assert
		mockMvc.perform(get("/stores/" + uuid)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void retrieveStore_givenInvalidUUID_thenReturn400BadRequest() throws Exception {
		
		// Given
		String uuid = "00acc00e";
				
		// Assert
		mockMvc.perform(get("/stores/" + uuid)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void retrieveStore_givenUnknownUUID_thenReturn404NotFound() throws Exception {
		
		// Given
		String uuid = "227a9218-214d-43df-a511-58d7e84d7ed2";
				
		// Assert
		mockMvc.perform(get("/stores/" + uuid)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void retrieveStore_givenValidFullName_thenReturn200Ok() throws Exception {
		
		// Given
		String name = "Aracaju";
		
		// Assert
		mockMvc.perform(get("/stores/" + "?name=" + name)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void retrieveStore_givenValidPartialName_thenReturn200Ok() throws Exception {
		// Given
		String name = "acaj";
				
		// Assert
		mockMvc.perform(get("/stores/" + "?name=" + name)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void retrieveStore_givenValidFullAddress_shouldPass() throws Exception {
		
		// Given
		String address = "Centro, Aracaju/SE";
						
		// Assert
		mockMvc.perform(get("/stores/" + "?address=" + address)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void retrieveStore_givenValidPartialAddress_thenReturn200Ok() throws Exception {
		
		// Given
		String address = "entr";
						
		// Assert
		mockMvc.perform(get("/stores/" + "?address=" + address)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void retrieveStore_givenValidFullNameAndAddress_thenReturn200Ok() throws Exception {
		
		// Given
		String name = "Aracaju";
		String address = "Centro, Aracaju/SE";
						
		// Assert
		mockMvc.perform(get("/stores/" + "?name=" + name + "&address=" + address)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void retrieveStore_givenValidPartialNameAndAddress_thenReturn200Ok() throws Exception {
		
		// Given
		String name = "caju";
		String address = "Araca";
						
		// Assert
		mockMvc.perform(get("/stores/" + "?name=" + name + "&address=" + address)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void retrieveStore_givenUnknownName_thenReturn404NotFound() throws Exception {
		
		// Given
		String name = "zzzzzzx6";
								
		// Assert
		mockMvc.perform(get("/stores/" + "?name=" + name)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void retrieveStore_givenUnknownAddress_thenReturn404NotFound() throws Exception {
		
		// Given
		String address = "zzzzzzx6";
								
		// Assert
		mockMvc.perform(get("/stores/" + "?address=" + address)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void retrieveStore_givenUnknownNameAndAddress_thenReturn404NotFound() throws Exception {
		
		// Given
		String name = "zzzzzzx6";
		String address = "zzzzzzx6";
							
		// Assert
		mockMvc.perform(get("/stores/" + "?name=" + name + "&address=" + address)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
}
