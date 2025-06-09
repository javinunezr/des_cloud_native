package com.example.bdget.service;

import java.util.List;
import java.util.Optional;

import com.example.bdget.entity.Cliente;

public interface ClienteService {
    Cliente guardarCliente(Cliente cliente);
    List<Cliente> obtenerTodos();
    Optional<Cliente> obtenerPorId(Long clienteId);
}
