package com.example.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public class UsersModel {
	/**
	 * auth.jsonの中身は
	 {
  		"user1":"pass1",
  		"user2":"pass2",
  		"user3":"pass3"
	 }
	 */
	private String saveFilePath = "c:\\pleiades-ssj2023\\auth.json";

	private HashMap<String, String> users = new HashMap<>();

	public boolean auth(String name, String password) {
		return password.equals(users.get(name));
	}
	
	@PostConstruct
	public void prepare() {
		Jsonb jsonb = JsonbBuilder.create();
		try {
			String json = Files.readString(Path.of(saveFilePath));
			users.putAll(jsonb.fromJson(json, new HashMap<String, String>(){}.getClass().getGenericSuperclass()));
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
	
}
