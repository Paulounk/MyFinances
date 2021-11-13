package com.finances.service.impl;


import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finances.exception.AuthenticationException;
import com.finances.exception.BusinessRuleException;
import com.finances.model.User;
import com.finances.repository.UserRepository;
import com.finances.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private UserRepository repository;
	private PasswordEncoder encoder;
	
	public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
		super();
		this.repository = repository;
		this.encoder = encoder;
	}

	@Override
	public User authenticate(String email, String passowrd) {
	
		Optional<User> user = repository.findByEmailContainingIgnoreCase(email);
		
		if(!user.isPresent()) {
			throw new AuthenticationException("User not found with the email entered!");
		}
		
		boolean passwordsMatch = encoder.matches(passowrd, user.get().getPassword());
		
		if(!passwordsMatch) {
			throw new AuthenticationException("Invalid Password!");
		}
		
		return user.get();
	}

	@Override
	@Transactional
	public User saveUser(User user) {

		validateEmail(user.getEmail());
		encryptPassword(user);
		return repository.save(user);
	}
	
	public void encryptPassword(User user) {
		String password = user.getPassword();
		String encryptPassword = encoder.encode(password);
		user.setPassword(encryptPassword);
	}

	@Override
	public void validateEmail(String email) {
		
		boolean exist = repository.existsByEmail(email);
		if(exist) {
			throw new BusinessRuleException("Already exists a user with this e-mail!");
		}
	}

	@Override
	public Optional<User> getUserById(Long id) {
		
		return repository.findById(id);
	}
	
	

}
