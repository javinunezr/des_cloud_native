package com.example.bdget.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bdget.dto.BoletaRequestDto;
import com.example.bdget.entity.Boleta;
import com.example.bdget.entity.Cliente;
import com.example.bdget.entity.Producto;
import com.example.bdget.repository.BoletaRepository;
import com.example.bdget.repository.ClienteRepository;
import com.example.bdget.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BoletaServiceImp implements BoletaService {

    private final BoletaRepository boletaRepository;

    @Autowired
    public BoletaServiceImp(BoletaRepository boletaRepository) {
        this.boletaRepository = boletaRepository;
    }

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private S3UploaderService s3UploaderService;

    @Override
    public Boleta procesarBoleta(Boleta boleta) {
        int subtotal = 0;
        if (boleta.getProductos() != null && !boleta.getProductos().isEmpty()) {
            for (Producto producto : boleta.getProductos()) {
                producto.setBoleta(boleta);
                subtotal += producto.getPrecio();
            }
        }
        boleta.setSubtotal(subtotal);
        boleta.setTotal(subtotal * 1.19);
        return boletaRepository.save(boleta);
    }

    @Override
    public Boleta actualizarBoleta(Boleta boleta) {
        int subtotal = 0;
        if (boleta.getProductos() != null && !boleta.getProductos().isEmpty()) {
            for (Producto producto : boleta.getProductos()) {
                producto.setBoleta(boleta);
                subtotal += producto.getPrecio();
            }
        }
        boleta.setSubtotal(subtotal);
        boleta.setTotal(subtotal * 1.19);
        return boletaRepository.save(boleta);
    }

    @Override
    public List<Boleta> obtenerBoletasPorCliente(Long clienteId) {
        return boletaRepository.findByCliente_ClienteId(clienteId);
    }

    @Override
    public Boleta procesarBoletaDesdeDto(BoletaRequestDto boletaDto) {
        Cliente cliente = clienteRepository.findById(boletaDto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Boleta boleta = new Boleta();
        boleta.setCliente(cliente);

		List<Producto> productos = boletaDto.getProductos();

        int subtotal = 0;
        for (Producto producto : productos) {
            producto.setBoleta(boleta);
            subtotal += producto.getPrecio();
        }

		boleta.setProductos(productos);
        boleta.setSubtotal(subtotal);
        boleta.setTotal(subtotal * 1.19);

        // Guardar boleta
        Boleta boletaGuardada = boletaRepository.save(boleta);

		// Cargar los productos a la boleta en PDF
		boletaGuardada = boletaRepository.findByIdWithProductos(boletaGuardada.getBoletaId())
    		.orElseThrow(() -> new RuntimeException("No se pudo recargar la boleta"));

        // Generar y subir PDF

        try {
			byte[] pdfBytes = pdfService.generarBoletaPDF(boletaGuardada);

            String clienteId = cliente.getClienteId().toString();
            String anioMes = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            String key = String.format("cliente%s/%s/boleta%d.pdf", clienteId, anioMes, boletaGuardada.getBoletaId());

            s3UploaderService.subirBoletaPDF(pdfBytes, key);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar o subir el PDF: " + e.getMessage(), e);
        }

        // Enviar boletaDto a la cola1
        try {
            rabbitTemplate.convertAndSend("cola1", boletaDto);
        } catch (Exception e) {
            try {
                rabbitTemplate.convertAndSend("cola2", boletaDto);
            } catch (Exception ex) {
                throw new RuntimeException("Error al enviar a RabbitMQ (cola1 y cola2): " + ex.getMessage(), ex);
            }
        }

        return boletaGuardada;
    }

	@Override
	public Boleta obtenerBoletaConProductosPorId(Long boletaId) {
		return boletaRepository.findByIdWithProductos(boletaId)
				.orElseThrow(() -> new RuntimeException("Boleta no encontrada con productos"));
	}

    @Autowired
    private RabbitTemplate rabbitTemplate;

}
