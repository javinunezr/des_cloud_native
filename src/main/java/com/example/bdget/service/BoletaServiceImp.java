package com.example.bdget.service;

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

		return boletaRepository.save(boleta);
	}


}