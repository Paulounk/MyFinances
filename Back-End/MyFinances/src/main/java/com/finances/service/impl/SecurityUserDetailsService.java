package com.finances.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finances.model.User;
import com.finances.repository.UserRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User userFound = userRepository
							.findByEmailContainingIgnoreCase(email)
							.orElseThrow(() -> new UsernameNotFoundException("E-mail not found."));
		
		return org.springframework.security.core.userdetails.User.builder()
																	.username(userFound.getEmail())
																	.password(userFound.getPassword())
																	.roles("USER")
																	.build();
		
	}

}
