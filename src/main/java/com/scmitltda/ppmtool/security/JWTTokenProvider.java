package com.scmitltda.ppmtool.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.scmitltda.ppmtool.domain.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTTokenProvider {
	//Generate the Token
	public String generateToke(Authentication auth) {
		User user = (User)auth.getPrincipal();
		Date now = new Date(System.currentTimeMillis());
		
		Date expiryDate = new Date(now.getTime()+SecurityConstants.EXPIRATION_TIME);
		
		String userId = Long.toString(user.getId());
		
		Map<String, Object> mapClaims = new HashMap<String, Object>();
		mapClaims.put("id", Long.toString(user.getId()));
		mapClaims.put("username", user.getUsername());
		mapClaims.put("fullname", user.getFullName());
		
		
		return Jwts.builder()
				.setSubject(userId)
				.setClaims(mapClaims)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
				.compact();
	}

	//Validate the Token
	
	//Get User Id from the Token
	
	
}
