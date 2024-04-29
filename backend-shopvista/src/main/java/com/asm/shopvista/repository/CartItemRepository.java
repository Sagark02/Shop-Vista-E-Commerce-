package com.asm.shopvista.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asm.shopvista.entity.CartItem;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
