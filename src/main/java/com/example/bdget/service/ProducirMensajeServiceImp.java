package com.example.bdget.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bdget.config.RabbitMQConfig;

@Service
public class ProducirMensajeServiceImp implements ProducirMensajeService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProducirMensajeServiceImp(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enviarMensaje(String mensaje) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.MAIN_QUEUE, mensaje);
    }

    @Override
    public void enviarObjeto(Object objeto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.MAIN_QUEUE, objeto);
    }
}

