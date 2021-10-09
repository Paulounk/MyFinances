package com.finances.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.finances.exception.BusinessRuleException;
import com.finances.model.User;
import com.finances.repository.UserRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	@Autowired
	UserService service;
	
	@Autowired
	UserRepository repository;
	
	@Test
	public void mustValidateEmail() {
		
		Assertions.assertDoesNotThrow(() -> {
			//Scenery
			repository.deleteAll();
				
			//Action 
			service.validateEmail("pedro@hotmail.com");
		});
	}
	
	@Test
	public void shouldShowErrorWhenValidatingEmailWhenItAlreadExists() {
		
		Assertions.assertThrows(BusinessRuleException.class, () -> {
			//Scenery
			User user = User.builder().name("pedro").email("pedro@hotmail.com").password("123456").build();
			repository.save(user);
		
			//Action 
			service.validateEmail("pedro@hotmail.com");
		});
	}

}
