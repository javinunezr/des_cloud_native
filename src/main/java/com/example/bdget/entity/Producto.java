package com.example.bdget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Table(name = "producto")
@Entity
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProducto;
	@Basic
	private String nombre;
	private String marca;
	private String descripcion;
	private int precio;
	private String categoria;

	// Lado "muchos" de la relación
	@ManyToOne // Muchos productos pueden pertenecer a una boleta
	@JoinColumn(name = "boletaId") // Esta es la columna de la clave foránea en la tabla 'producto'
	@JsonIgnore
	private Boleta boleta; // Referencia a la boleta a la que pertenece este producto


	public Producto(Long idProducto, String nombre, String marca, String descripcion, int precio, String categoria, Boleta boleta) {
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.marca = marca;
		this.descripcion = descripcion;
		this.precio = precio;
		this.categoria = categoria;
		this.boleta = boleta;
	}

	public Producto() {}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Boleta getBoleta() {
		return boleta;
	}

	public void setBoleta(Boleta boleta) {
		this.boleta = boleta;
	}
}