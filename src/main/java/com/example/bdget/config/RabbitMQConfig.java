package com.example.bdget.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String COLA1 = "cola1";
	public static final String COLA2 = "cola2";
	public static final String MAIN_EXCHANGE = "exchange-principal";
	public static final String DLX_EXCHANGE = "exchange-errores";
	public static final String DLX_ROUTING_KEY = "errores";
    public static final String MAIN_QUEUE = null;

	@Bean
	Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost("52.205.114.38");
		factory.setPort(5672);
		factory.setUsername("guest");
		factory.setPassword("guest");
		return factory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter());
		return template;
	}

	@Bean
	Queue cola1() {
		return new Queue(COLA1, true, false, false,
				Map.of("x-dead-letter-exchange", DLX_EXCHANGE,
					   "x-dead-letter-routing-key", DLX_ROUTING_KEY));
	}

	@Bean
	Queue cola2() {
		return new Queue(COLA2);
	}

	@Bean
	DirectExchange mainExchange() {
		return new DirectExchange(MAIN_EXCHANGE);
	}

	@Bean
	DirectExchange dlxExchange() {
		return new DirectExchange(DLX_EXCHANGE);
	}

	@Bean
	Binding bindingCola1() {
		return BindingBuilder.bind(cola1()).to(mainExchange()).with("routingKeyCola1");
	}

	@Bean
	Binding bindingCola2() {
		return BindingBuilder.bind(cola2()).to(dlxExchange()).with(DLX_ROUTING_KEY);
	}
}
