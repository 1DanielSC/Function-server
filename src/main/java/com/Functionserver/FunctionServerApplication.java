package com.Functionserver;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.Functionserver.model.ProductDTO;

import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableDiscoveryClient
public class FunctionServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunctionServerApplication.class, args);
	}

	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	@Bean
	public Function<Flux<ProductDTO>, Flux<String>> checkProduct(){
		return flux -> flux.map(product -> {

			if(product.getPrice() < 0.0)
				return "Invalid price: the price is negative.";

			if(product.getPrice() > 10000.0)
				return "Invalid price: the price is too high.";
			
			if(product.getQuantity() < 0)
				return "Invalid quantity: the quantity is negative.";
			
			if(product.getQuantity() >= Long.MAX_VALUE-1)
				return "Invalid quantity: the quantity is too high.";

			if(product.getName().length() < 3)
				return "Invalid name: the name must have at least 3 characters.";
			return "OK";
		});
	}

}
