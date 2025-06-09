package com.example.bdget.controller;

import com.example.bdget.entity.Producto;
import com.example.bdget.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/producto")

public class ProductoController {

	private final ProductoService productoService;

	@Autowired
	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	@RequestMapping("/todos")
	public ResponseEntity<List<Producto>> getProductos(){
		return ResponseEntity.ok(productoService.getProductos());
	}

	@PostMapping("/crear")
	public ResponseEntity<Producto> addProducto(@RequestBody Producto producto){
		return ResponseEntity.ok(productoService.addProducto(producto));
	}

	@DeleteMapping("eliminar/{idProducto}")
	ResponseEntity<String> removeProducto(@PathVariable Long idProducto){
		boolean eliminado = productoService.removeProducto(idProducto);
		if(eliminado){
			return ResponseEntity.ok("Producto eliminado");
		}else{
			return ResponseEntity.badRequest().body("No se pudo eliminar el producto");
		}
	}

}