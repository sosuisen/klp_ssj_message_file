package com.example.model;

import java.io.Serializable;

import jakarta.inject.Named;
import jakarta.mvc.RedirectScoped;
import lombok.Getter;
import lombok.Setter;

@RedirectScoped
@Setter @Getter
@Named
public class ErrorBean implements Serializable {
	private String message;
}

