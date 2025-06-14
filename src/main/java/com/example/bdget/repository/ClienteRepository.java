package com.example.bdget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bdget.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
