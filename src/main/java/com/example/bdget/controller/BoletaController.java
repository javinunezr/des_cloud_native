package com.example.bdget.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bdget.dto.BoletaRequestDto;
import com.example.bdget.entity.Boleta;
import com.example.bdget.service.BoletaService;
import com.example.bdget.service.S3UploaderService;

@Controller
@RequestMapping("/boleta")
public class BoletaController {

	private final BoletaService boletaService;
	private final S3UploaderService s3UploaderService;

	@Autowired
	public BoletaController(BoletaService boletaService, S3UploaderService s3UploaderService) {
		this.boletaService = boletaService;
		this.s3UploaderService = s3UploaderService;
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

	@GetMapping("/descargar/{clienteId}/{anioMes}/{boletaId}")
	public ResponseEntity<byte[]> descargarBoletaPDF(
			@PathVariable String clienteId,
			@PathVariable String anioMes,
			@PathVariable Long boletaId) {

		String key = String.format("cliente%s/%s/boleta%d.pdf", clienteId, anioMes, boletaId);
		byte[] pdf = s3UploaderService.descargarBoletaPDF(key);

		if (pdf == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=boleta" + boletaId + ".pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdf);
	}

	@DeleteMapping("/eliminar-pdf/{clienteId}/{anioMes}/{boletaId}")
	public ResponseEntity<Void> eliminarBoletaPDF(
			@PathVariable String clienteId,
			@PathVariable String anioMes,
			@PathVariable Long boletaId) {

		String key = String.format("cliente%s/%s/boleta%d.pdf", clienteId, anioMes, boletaId);
		boolean eliminado = s3UploaderService.eliminarBoletaPDF(key);

		if (eliminado) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
