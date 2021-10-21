package com.finances.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finances.dto.UserDTO;
import com.finances.exception.AuthenticationException;
import com.finances.exception.BusinessRuleException;
import com.finances.model.User;
import com.finances.service.EntryService;
import com.finances.service.UserService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

	static final String API = "/users";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc;
	
	@MockBean 
	UserService service;
	
	@MockBean 
	EntryService entryService;
	
	@DisplayName("Deve autenticar um usuario")
	@Test
	public void mustAuthenticateAUser() throws Exception {
		
		//scenary
		String email = "user@hotmail.com";
		String password = "123456";
		
		UserDTO userDTO = UserDTO.builder().email(email).password(password).build();
		User user = User.builder().email(email).password(password).build();
		
		Mockito.when(service.authenticate(email, password)).thenReturn(user);
		
		//Action e Verification
		String json = new ObjectMapper().writeValueAsString(userDTO);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API.concat("/authenticate"))
												.accept(JSON)
												.contentType(JSON)
												.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()));
		
	}
	
	@DisplayName("Deve retornar bad request ao obter erro de autenticação")
	@Test
	public void shouldReturnBadRequestOnAutheticationError() throws Exception {
		
		//scenary
		String email = "user@hotmail.com";
		String password = "123456";
		
		UserDTO userDTO = UserDTO.builder().email(email).password(password).build();
		Mockito.when(service.authenticate(email, password)).thenThrow(AuthenticationException.class);

		//Action e Verification
		String json = new ObjectMapper().writeValueAsString(userDTO);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API.concat("/authenticate"))
												.accept(JSON)
												.contentType(JSON)
												.content(json);
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
				
	}
	
	@DisplayName("Deve salvar um usuario")
	@Test
	public void mustSaveAUser() throws Exception {
		
		//scenary
		String email = "user@hotmail.com";
		String password = "123456";
		
		User user = User.builder().email(email).password(password).build();
	
		Mockito.when(service.saveUser(Mockito.any(User.class))).thenReturn(user);
		
		//Action e Verification
		String json = new ObjectMapper().writeValueAsString(user);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API)
												.accept(JSON)
												.contentType(JSON)
												.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated());
				
	}
	
	@DisplayName("Deve retornar Bad Request ao tentar salvar um usuario invalido")
	@Test
	public void shouldReturnBadRequestWhenTryingToSaveAnInvalidUser() throws Exception {
		
		//scenary
		String email = "user@hotmail.com";
		String password = "123456";
		
		User user = User.builder().email(email).password(password).build();
		Mockito.when(service.saveUser(Mockito.any(User.class))).thenThrow(BusinessRuleException.class);
	
		//Action e Verification
		String json = new ObjectMapper().writeValueAsString(user);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API)
												.accept(JSON)
												.contentType(JSON)
												.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
				
	}
		
	
}
