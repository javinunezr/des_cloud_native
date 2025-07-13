package com.example.bdget.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.bdget.entity.Boleta;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    List<Boleta> findByCliente_ClienteId(Long clienteId);

    @Query("SELECT b FROM Boleta b LEFT JOIN FETCH b.productos WHERE b.boletaId = :boletaId")
    Optional<Boleta> findByIdWithProductos(Long boletaId);
}
