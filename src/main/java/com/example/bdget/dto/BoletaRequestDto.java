package com.example.bdget.dto;

import java.util.List;

import com.example.bdget.entity.Producto;

public class BoletaRequestDto {
    private Long clienteId;
    private List<Producto> productos;

    // Getters y setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
