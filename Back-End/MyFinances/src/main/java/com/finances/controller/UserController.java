package com.finances.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finances.dto.UserDTO;
import com.finances.exception.AuthenticationException;
import com.finances.exception.BusinessRuleException;
import com.finances.model.Entry;
import com.finances.model.User;
import com.finances.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	@PostMapping("/authenticate")
	public ResponseEntity authenticate(@RequestBody UserDTO dto) {
		
		try {
			User autheticadUser = service.authenticate(dto.getEmail(), dto.getPassword());
			return ResponseEntity.ok(autheticadUser);
		}
		catch(AuthenticationException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity saveUser(@RequestBody UserDTO dto) {
		
		User user = User.builder()
				.name(dto.getName())
				.email(dto.getEmail())
				.password(dto.getPassword()).build();
		
		
		try {
			User userSave = service.saveUser(user);
			return new ResponseEntity(userSave, HttpStatus.CREATED);
		}
		catch(BusinessRuleException e) {
			return  ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}
