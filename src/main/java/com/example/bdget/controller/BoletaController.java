package com.example.bdget.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bdget.dto.BoletaRequestDto;
import com.example.bdget.entity.Boleta;
import com.example.bdget.service.BoletaService;

@Controller

@RequestMapping("/boleta")


public class BoletaController {

	private final BoletaService boletaService;

	@Autowired
	public BoletaController(BoletaService boletaService) {
		this.boletaService = boletaService;
	}

	@PostMapping("/procesar")
	public ResponseEntity<Boleta> crearBoleta(@RequestBody BoletaRequestDto boletaDto) {
		Boleta boletaGuardada = boletaService.procesarBoletaDesdeDto(boletaDto);
		return new ResponseEntity<>(boletaGuardada, HttpStatus.CREATED);
	}

	@PutMapping("/actualizar/{id}")
	public ResponseEntity<Boleta> actualizarBoleta(@PathVariable Long id, @RequestBody Boleta boletaActualizada) {
		boletaActualizada.setBoletaId(id);
		Boleta boleta = boletaService.actualizarBoleta(boletaActualizada);
		return ResponseEntity.ok(boleta);
	}

	@GetMapping("/historial/{clienteId}")
	public ResponseEntity<List<Boleta>> obtenerHistorialPorCliente(@PathVariable Long clienteId) {
		List<Boleta> historial = boletaService.obtenerBoletasPorCliente(clienteId);
		return ResponseEntity.ok(historial);
	}

}