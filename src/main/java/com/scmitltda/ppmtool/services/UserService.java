package com.scmitltda.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scmitltda.ppmtool.domain.User;
import com.scmitltda.ppmtool.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userReository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
		
		// Username has to be unique (thow an exception)
		
		// Make sure the password and confirm password matches
		// we don't need to persist or show the confirm password
		return userReository.save(newUser);
	}
}
