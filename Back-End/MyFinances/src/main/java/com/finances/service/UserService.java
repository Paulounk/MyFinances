package com.finances.service;

import java.util.Optional;

import com.finances.model.User;

public interface UserService {

	User authenticate(String email, String passowrd);
	
	User saveUser(User user);
	
	void validateEmail(String email);
	
	Optional<User> getUserById(Long id);
		
}
