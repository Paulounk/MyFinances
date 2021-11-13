package com.finances.service;

import com.finances.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {

	String generateToken(User user); 
	
	Claims getClaims(String token) throws ExpiredJwtException;
	
	boolean isValidToken(String token);
	
	String getLoginUser(String token);
	
}
