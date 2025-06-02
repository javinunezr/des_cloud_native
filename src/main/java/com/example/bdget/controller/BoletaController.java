package com.example.bdget.controller;

import com.example.bdget.entity.Boleta;
import com.example.bdget.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("/boleta")


public class BoletaController {

	private final BoletaService boletaService;

	@Autowired
	public BoletaController(BoletaService boletaService) {
		this.boletaService = boletaService;
	}

	@PostMapping("/procesar")
	public ResponseEntity<Boleta> crearBoleta(@RequestBody Boleta boleta) {
		// La entidad Boleta viene directamente en el cuerpo de la petición
		Boleta boletaGuardada = boletaService.procesarBoleta(boleta);
		return new ResponseEntity<>(boletaGuardada, HttpStatus.CREATED);
	}
}