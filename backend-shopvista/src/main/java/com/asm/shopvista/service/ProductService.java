package com.asm.shopvista.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.asm.shopvista.entity.Product;
import com.asm.shopvista.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Optional<Product> getProductById(Long productId) {
		return productRepository.findById(productId);
	}

	public Product add(Product product) {
		return productRepository.save(product);
	}
}
