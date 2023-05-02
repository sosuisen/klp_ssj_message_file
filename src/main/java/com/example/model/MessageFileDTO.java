package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageFileDTO extends MessageDTO {
	private String message;
	private String name;
	private String fileName;
}