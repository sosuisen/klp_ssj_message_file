package com.example.model;

import jakarta.ws.rs.FormParam;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserDTO {
	@FormParam("name")
	private String name;
	@FormParam("password")
	private String password;
}

