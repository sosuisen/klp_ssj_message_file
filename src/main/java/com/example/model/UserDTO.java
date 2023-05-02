package com.example.model;

import jakarta.ws.rs.FormParam;
import lombok.Data;

@Data
public class UserDTO {
	@FormParam("name")
	private String name;
	@FormParam("password")
	private String password;
}

