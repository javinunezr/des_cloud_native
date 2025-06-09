package com.example.bdget.service;

import com.example.bdget.entity.Producto;

import java.util.List;

public interface ProductoService{
	List<Producto> getProductos();
	Producto addProducto(Producto producto);
	boolean removeProducto(Long idProducto);


}
