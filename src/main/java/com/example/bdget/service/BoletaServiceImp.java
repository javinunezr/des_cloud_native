package com.example.bdget.service;

import com.example.bdget.entity.Boleta;
import com.example.bdget.entity.Producto;
import com.example.bdget.repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
				// *** ¡Aquí está el cambio clave! ***
				// NO LLAMES a boleta.addProducto(producto) aquí.
				// Los productos ya están en la lista `boleta.getProductos()` porque Spring/Jackson los deserializó del JSON.
				// Lo único que necesitas hacer aquí es establecer la referencia inversa en el producto (el lado ManyToOne).
				producto.setBoleta(boleta); // ¡Esto es CRÍTICO para que Hibernate persista la relación!

				subtotal += producto.getPrecio();
			}
		}

		boleta.setSubtotal(subtotal);
		boleta.setTotal(subtotal * 1.19);

		return boletaRepository.save(boleta); // Hibernate guardará la boleta y sus productos relacionados
	}



}