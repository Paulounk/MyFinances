package com.finances.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.finances.model.User;
import com.finances.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") //Especifica que será utilizado o application-test.properties
@DataJpaTest //Realiza o rollback na transação de cada teste realizado
@AutoConfigureTestDatabase(replace = Replace.NONE) //Para não subescrever as config da base de teste
public class UserRepositoryTest {

	@Autowired
	UserRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@DisplayName("Valida e-mail já cadastrado")
	@Test
	public void mustCheckForAnEmail() {
		
		//Scenery
		User user =  createUserTest();
		entityManager.persist(user);
		
		//Action 
		boolean result = repository.existsByEmail(user.getEmail());
		
		//Verification
		Assertions.assertThat(result).isTrue();
	}
	
	@DisplayName("Valida e-mail não cadastrado")
	@Test
	public void mustReturnFalseWhenTheUserIsNotRegistered() {
		
		//Action 
		boolean result = repository.existsByEmail("pedro@hotmail.com");
		
		//Verification
		Assertions.assertThat(result).isFalse();
	}
	
	@DisplayName("Valida cadastro de usuario")
	@Test
	public void mustPersistAUserInTheDataBase() {

		//Scenery
		User user = createUserTest();
		
		//Action
		User userSave = repository.save(user);
		
		//Verification
		Assertions.assertThat(userSave.getId()).isNotNull();
	}
	
	@DisplayName("Valida pesquisa de usuario por e-mail")
	@Test
	public void mustSearchForAUserByEmail() {
		
		//Scenery
		User user = createUserTest();
		entityManager.persist(user);
		
		//Action 
		Optional<User> result = repository.findByEmailContainingIgnoreCase(user.getEmail());
		
		//Verification
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@DisplayName("Valida pesquisa de usuario por e-mail que não existe")
	@Test
	public void mustReturnEmptyWhenUserNotExist() {
	
		//Action 
		Optional<User> result = repository.findByEmailContainingIgnoreCase("pedro@hotmail.com");
		
		//Verification
		Assertions.assertThat(result.isEmpty()).isTrue();
	}
	
	
	//Method for tests that need a test user
	public static User createUserTest() {
		return User 
				.builder()
				.name("pedro")
				.email("pedro@hotmail.com")
				.password("123456")
				.build();
	}
}
