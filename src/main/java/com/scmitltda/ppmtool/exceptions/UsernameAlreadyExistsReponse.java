package com.scmitltda.ppmtool.exceptions;

public class UsernameAlreadyExistsReponse {
	private String username;

	public UsernameAlreadyExistsReponse(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
