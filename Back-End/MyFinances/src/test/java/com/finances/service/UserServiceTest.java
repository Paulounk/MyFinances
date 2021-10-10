package com.finances.service;


import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.finances.exception.AuthenticationException;
import com.finances.exception.BusinessRuleException;
import com.finances.model.User;
import com.finances.repository.UserRepository;
import com.finances.service.impl.UserServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	@Autowired
	UserService service;
	
	//Através da anotation MockBean o mockito cria uma instância fake de uma classe para ser testada
	@MockBean
	UserRepository repository;
	
	@BeforeEach
	public void setUp() {
		service = new UserServiceImpl(repository);
	}
	
	@DisplayName("Não deve retornar exception se a autenticação estiver com dados corretos")
	@Test
	public void mustAuthenticateAUserWithSuccess() {
		
		//Scenery
		String email = "paulo@hotmail.com";
		String password = "123456";
		
		User user = User.builder().email(email).password(password).id(1l).build();
		Mockito.when(repository.findByEmailContainingIgnoreCase(email)).thenReturn(Optional.of(user));
		
		//Action
		User result = service.authenticate(email,password);
		
		//Verification
		Assertions.assertNotNull(result);
	
	}
	
	@DisplayName("Deve retornar exception quando não for encontrado o e-mail para autenticação")
	@Test
	public void shouldReturnExceptionWhenTheEmailForAuthenticationDoesNotExist() {
		
		//Scenery
		Mockito.when(repository.findByEmailContainingIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		
		//Action
		Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.authenticate("paulo@hotmail.com", "456789"));
		
		//Verification
		org.assertj.core.api.Assertions.assertThat(exception)
			.isInstanceOf(AuthenticationException.class)
			.hasMessage("User not found with the email entered!");	
		
	}
	
	@DisplayName("Deve retornar exception quando senha estiver inválida")
	@Test
	public void shouldReturnErrorWhenPasswordIsInvalid() {
		
		//Scenery
		String senha = "123456";
		User user = User.builder().email("paulo@hotmail.com").password(senha).build();
		Mockito.when(repository.findByEmailContainingIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(user));
		
		//Action
		Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.authenticate("paulo@hotmail.com", "456789"));
		
		//Verification
		org.assertj.core.api.Assertions.assertThat(exception)
		.isInstanceOf(AuthenticationException.class)
		.hasMessage("Invalid Password!");	
		
	}
	
	@DisplayName("Não deve retornar exception se for um novo e-mail para cadastrado")
	@Test
	public void mustValidateEmail() {
		
		Assertions.assertDoesNotThrow(() -> {
			
			//Scenery
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
				
			//Action 
			service.validateEmail("paulo@hotmail.com");
		});
	}
	
	@DisplayName("Deve retornar exception se e-mail já estiver cadastrado")
	@Test
	public void shouldShowErrorWhenValidatingEmailWhenItAlreadExists() {
		
		Assertions.assertThrows(BusinessRuleException.class, () -> {
			//Scenery
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
			//Action 
			service.validateEmail("paulo@hotmail.com");
		});
	}

}
