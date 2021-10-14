package com.finances.service;


import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
	
	//Metodo Spy mocka a classe, não utilizando os metodos originais caso seja informado o comportamento
	@SpyBean
	UserServiceImpl service;
	
	//Através da anotation MockBean o mockito cria uma instância fake de uma classe para ser testada
	@MockBean
	UserRepository repository;
	
	@DisplayName("Deve salvar um usuario")
	@Test
	public void mustSaveAUser() {
		//Scenery
		Mockito.doNothing().when(service).validateEmail(Mockito.anyString());
		User user = User.builder()
				.id(1l)
				.name("paulo")
				.email("paulo@hotmail.com")
				.password("123456").build();
		
		Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);
		
		//Action
		User userSave = service.saveUser(new User());
		
		//Verification
		org.assertj.core.api.Assertions.assertThat(userSave).isNotNull();
		org.assertj.core.api.Assertions.assertThat(userSave.getId()).isEqualTo(1l);
		org.assertj.core.api.Assertions.assertThat(userSave.getName()).isEqualTo("paulo");
		org.assertj.core.api.Assertions.assertThat(userSave.getEmail()).isEqualTo("paulo@hotmail.com");
		org.assertj.core.api.Assertions.assertThat(userSave.getPassword()).isEqualTo("123456");
		
	}
	
	@DisplayName("Não deve salvar um usuario com e-mail já cadastrado")
	@Test
	public void shouldNotSaveAUser() {
		
		Assertions.assertThrows(BusinessRuleException.class, () -> {	
			
			//Scenery
			String email = "paulo@hotmail.com";
			User user = User.builder().name("paulo").email(email).password("123456").build();
			Mockito.doThrow(BusinessRuleException.class).when(service).validateEmail(email);
		
			//Action
			service.saveUser(user);
		
			//Verification
			Mockito.verify(repository, Mockito.never()).save(user);
		});
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
