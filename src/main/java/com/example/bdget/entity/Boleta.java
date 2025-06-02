package com.example.bdget.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "boleta")
@Entity
public class Boleta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boletaId;
	private int subtotal;
	private double total;

	@OneToMany(mappedBy = "boleta") // 'boleta' es el nombre del campo en la entidad Producto
	private List<Producto> productos = new ArrayList<>(); // Ahora guardamos una lista de objetos Producto, no solo sus IDs

	public Boleta(List<Producto> productos, double total, int subtotal, Long boletaId) {
		this.productos = productos;
		this.total = total;
		this.subtotal = subtotal;
		this.boletaId = boletaId;
	}

	public Boleta() {}

	public Long getBoletaId() {
		return this.boletaId;
	}

	public void setBoletaId(Long boletaId) {
		this.boletaId = boletaId;
	}

	public int getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(int subtotal) {
		this.subtotal = subtotal;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	//Este método es crucial para establecer la relación bidireccional
	public void addProducto(Producto producto) {
		productos.add(producto);
		producto.setBoleta(this); // Establece la referencia de Boleta en el Producto
	}

	public void removeProducto(Producto producto) {
		productos.remove(producto);
		producto.setBoleta(null);
	}
}