package com.finances.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finances.dto.TokenDTO;
import com.finances.dto.UserDTO;
import com.finances.exception.AuthenticationException;
import com.finances.exception.BusinessRuleException;
import com.finances.model.User;
import com.finances.service.EntryService;
import com.finances.service.JwtService;
import com.finances.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService serviceUser;
	
	@Autowired
	private EntryService serviceEntry;
	
	@Autowired
	private JwtService jwtService;
	

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO dto) {
		
		try {
			User autheticadUser = serviceUser.authenticate(dto.getEmail(), dto.getPassword());
			String token = jwtService.generateToken(autheticadUser);
			TokenDTO tokenDTO = new TokenDTO(autheticadUser.getName(), token);
			
			return ResponseEntity.ok(tokenDTO);
		}
		catch(AuthenticationException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity<?> saveUser(@RequestBody UserDTO dto) {
		
		User user = User.builder()
				.name(dto.getName())
				.email(dto.getEmail())
				.password(dto.getPassword()).build();
		
		
		try {
			User userSave = serviceUser.saveUser(user);
			return new ResponseEntity(userSave, HttpStatus.CREATED);
		}
		catch(BusinessRuleException e) {
			return  ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("{id}/balance")
	public ResponseEntity<?> getBalance(@PathVariable Long id){
		
		Optional<User> user = serviceUser.getUserById(id);
		
		if(!user.isPresent()) {
			return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
		}
		
		BigDecimal balance = serviceEntry.getBalanceByUser(id);
		
		return ResponseEntity.ok(balance);
	}
	
}
