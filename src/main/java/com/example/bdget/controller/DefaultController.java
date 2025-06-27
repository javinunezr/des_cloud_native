package com.example.bdget.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class DefaultController {

	@PostMapping("/mensaje")
	public String mensaje() {

		System.out.println("Backend llamado");
		return "{\"mensaje\": \"Integración OK al backend\"}";
	}
}
