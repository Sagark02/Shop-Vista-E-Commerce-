package com.asm.shopvista.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.asm.shopvista.dto.CartDto;
import com.asm.shopvista.dto.OrderDto;
import com.asm.shopvista.dto.PaymentDto;
import com.asm.shopvista.entity.Order;
import com.asm.shopvista.service.CartService;
import com.asm.shopvista.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
	private final OrderService orderService;
	private final CartService cartService;

	@GetMapping("/{userId}")
	public List<OrderDto> getOrdersByUserId(@PathVariable Long userId, Authentication authentication) {
		return orderService.getOrdersByUserId(userId, authentication);
	}

	@PostMapping("/{userId}/checkout")
	public ResponseEntity<PaymentDto> checkout(@PathVariable Long userId, Authentication authentication) {
		CartDto cart = cartService.getCartByUserId(userId);
		BigDecimal totalPrice = cart.getTotalPrice();

		Order createdOrder = orderService.createOrderFromCart(cart, userId, authentication);

		cartService.clearCart(userId);

		PaymentDto paymentDto = new PaymentDto(totalPrice, "rs", createdOrder.getId());

		return ResponseEntity.ok().body(paymentDto);
	}
}
