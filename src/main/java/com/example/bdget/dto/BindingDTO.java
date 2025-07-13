package com.example.bdget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindingDTO {

	private String nombreCola;
	private String nombreExchange;
	private String routingKey;

    // Getters y setters
    public String getNombreCola() {
        return nombreCola;
    }

    public void setNombreCola(String nombreCola) {
        this.nombreCola = nombreCola;
    }

    public String getNombreExchange() {
        return nombreExchange;
    }

    public void setNombreExchange(String nombreExchange) {
        this.nombreExchange = nombreExchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
