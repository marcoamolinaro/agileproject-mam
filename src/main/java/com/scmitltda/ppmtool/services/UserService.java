package com.scmitltda.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scmitltda.ppmtool.domain.User;
import com.scmitltda.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.scmitltda.ppmtool.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userReository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			
			newUser.setUsername(newUser.getUsername());
			
			// Make sure the password and confirm password matches
			// we don't need to persist or show the confirm password
			
			return userReository.save(newUser);
		} catch (Exception e) {
			throw new UsernameAlreadyExistsException(
					"Username '" + newUser.getUsername() + 
					"' already exists");
		}
	}
}
