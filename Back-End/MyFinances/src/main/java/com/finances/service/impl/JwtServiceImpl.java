package com.finances.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.finances.model.User;
import com.finances.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtServiceImpl implements JwtService{

	@Value("${jwt.expiration}")
	private String expiration;
	
	@Value("${jwt.signature-key}")
	private String signatureKey;
	
	@Override
	public String generateToken(User user) {
		long exp = Long.valueOf(expiration);
		LocalDateTime dateHourExpiration = LocalDateTime.now().plusMinutes(exp);
		Instant instant = dateHourExpiration.atZone(ZoneId.systemDefault()).toInstant();
		Date date = Date.from(instant);
		
		String tokenExpirationTime = dateHourExpiration.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		
		String token = Jwts
							.builder()
							.setExpiration(date)
							.setSubject(user.getEmail())
							.claim("userId", user.getId())
							.claim("name", user.getName())
							.claim("expirationHour", tokenExpirationTime)
							.signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, signatureKey)
							.compact();
		
		return token;
	}

	@Override
	public Claims getClaims(String token) throws ExpiredJwtException {

		return Jwts.parser()
					.setSigningKey(signatureKey)
					.parseClaimsJws(token)
					.getBody();
	}

	@Override
	public boolean isValidToken(String token) {

		try {
			Claims claims = getClaims(token);
			Date dateExp = claims.getExpiration();
			
			LocalDateTime dateExpiration = dateExp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			
			return !LocalDateTime.now().isAfter(dateExpiration);
		}catch(ExpiredJwtException e){
			return false;
		}
		
	}

	@Override
	public String getLoginUser(String token) {
		Claims claims = getClaims(token);
		return claims.getSubject();
	}

}
