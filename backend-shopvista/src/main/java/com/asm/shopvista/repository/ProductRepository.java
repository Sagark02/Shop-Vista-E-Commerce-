package com.asm.shopvista.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asm.shopvista.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
