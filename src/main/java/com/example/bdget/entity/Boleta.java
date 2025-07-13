package com.example.bdget.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "boleta")
@Entity
public class Boleta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boletaId;
	private int subtotal;
	private double total;


	@ManyToOne
	@JoinColumn(name = "cliente_id")
	@JsonIgnore
	private Cliente cliente;

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@OneToMany(mappedBy = "boleta", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Producto> productos = new ArrayList<>();

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

	public void addProducto(Producto producto) {
		productos.add(producto);
		producto.setBoleta(this); 
	}

	public void removeProducto(Producto producto) {
		productos.remove(producto);
		producto.setBoleta(null);
	}
}