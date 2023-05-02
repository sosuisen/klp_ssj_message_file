package com.example.model;

import lombok.Data;

@Data
public class MessageFileDTO extends MessageDTO {
	private String message;
	private String name;
	private String fileName;
}