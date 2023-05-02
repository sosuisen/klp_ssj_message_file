package com.example.model;

import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsersModel {
	private Map<String, String> users = Map.of(
			"user1", "pass1",
			"user2", "pass2",
			"user3", "pass3");

	public String getPassword(String name) {
		return (String) users.get(name);
	}
}
