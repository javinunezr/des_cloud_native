package com.example.bdget.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bdget.entity.Cliente;
import com.example.bdget.repository.ClienteRepository;

@Service
public class ClienteServiceImp implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImp(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> obtenerPorId(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }
}
