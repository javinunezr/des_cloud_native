package com.example.bdget.service;

import com.example.bdget.entity.Producto;
import com.example.bdget.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImp implements ProductoService {

	private final ProductoRepository productoRepository;

	@Autowired
	public ProductoServiceImp(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

	@Override
	public List<Producto> getProductos(){
		return productoRepository.findAll();
	}


	@Override
	public Producto addProducto(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public boolean removeProducto(Long idProducto) {
		Optional<Producto> producto = productoRepository.findById(idProducto);
		if(producto.isPresent()){
			productoRepository.deleteById(idProducto);
			return true;
		}
		return false;
	}
}