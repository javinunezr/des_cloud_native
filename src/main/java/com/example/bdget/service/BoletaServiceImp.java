package com.example.bdget.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bdget.dto.BoletaRequestDto;
import com.example.bdget.entity.Boleta;
import com.example.bdget.entity.Cliente;
import com.example.bdget.entity.Producto;
import com.example.bdget.repository.BoletaRepository;
import com.example.bdget.repository.ClienteRepository;


@Service
public class BoletaServiceImp implements BoletaService{

	private final BoletaRepository boletaRepository;

	@Autowired
	public BoletaServiceImp(BoletaRepository boletaRepository) {
		this.boletaRepository = boletaRepository;
	}

	@Override
	public Boleta procesarBoleta(Boleta boleta){

		int subtotal = 0;

		if (boleta.getProductos() != null && !boleta.getProductos().isEmpty()) {
			for (Producto producto : boleta.getProductos()) {
				producto.setBoleta(boleta); 

				subtotal += producto.getPrecio();
			}
		}

		boleta.setSubtotal(subtotal);
		boleta.setTotal(subtotal * 1.19);

		return boletaRepository.save(boleta); 
	}

	@Override
	public Boleta actualizarBoleta(Boleta boleta) {
		int subtotal = 0;
	
		if (boleta.getProductos() != null && !boleta.getProductos().isEmpty()) {
			for (Producto producto : boleta.getProductos()) {
				producto.setBoleta(boleta);
				subtotal += producto.getPrecio();
			}
		}
	
		boleta.setSubtotal(subtotal);
		boleta.setTotal(subtotal * 1.19);
	
		return boletaRepository.save(boleta);
	}

	@Override
	public List<Boleta> obtenerBoletasPorCliente(Long clienteId) {
		return boletaRepository.findByCliente_ClienteId(clienteId);
	}

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public Boleta procesarBoletaDesdeDto(BoletaRequestDto boletaDto) {
		Cliente cliente = clienteRepository.findById(boletaDto.getClienteId())
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

		Boleta boleta = new Boleta();
		boleta.setCliente(cliente);

		int subtotal = 0;
		for (Producto producto : boletaDto.getProductos()) {
			producto.setBoleta(boleta);
			subtotal += producto.getPrecio();
		}

		boleta.setProductos(boletaDto.getProductos());
		boleta.setSubtotal(subtotal);
		boleta.setTotal(subtotal * 1.19);

		// Guardar en la base de datos
		Boleta boletaGuardada = boletaRepository.save(boleta);

		// Generar PDF
		byte[] pdfBytes = pdfService.generarBoletaPDF(boletaGuardada);

		// Crear ruta en S3: /cliente{id}/{YYYY-MM}/boleta{id}.pdf
		String clienteId = "cliente" + cliente.getClienteId();
		String mesAnio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
		String filename = "boleta" + boletaGuardada.getBoletaId() + ".pdf";
		String s3Key = clienteId + "/" + mesAnio + "/" + filename;

		// Subir a S3
		s3UploaderService.subirBoletaPDF(pdfBytes, cliente.getClienteId().toString(), boletaGuardada.getBoletaId());

		return boletaGuardada;
	}

	@Autowired
	private PDFService pdfService;

	@Autowired
	private S3UploaderService s3UploaderService;


}