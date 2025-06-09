package com.example.bdget.service;

import java.util.List;

import com.example.bdget.dto.BoletaRequestDto;
import com.example.bdget.entity.Boleta;


public interface BoletaService {
	Boleta procesarBoleta(Boleta boleta);
	Boleta actualizarBoleta(Boleta boleta);
	List<Boleta> obtenerBoletasPorCliente(Long clienteId);
	Boleta procesarBoletaDesdeDto(BoletaRequestDto boletaDto);


}