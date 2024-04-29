package com.asm.shopvista.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.asm.shopvista.dto.CartDto;
import com.asm.shopvista.dto.CartItemDto;
import com.asm.shopvista.dto.OrderDto;
import com.asm.shopvista.entity.Order;
import com.asm.shopvista.entity.OrderItem;
import com.asm.shopvista.entity.Product;
import com.asm.shopvista.entity.User;
import com.asm.shopvista.exception.ShopVistaException;
import com.asm.shopvista.repository.OrderRepository;
import com.asm.shopvista.repository.ProductRepository;
import com.asm.shopvista.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	public List<OrderDto> getOrdersByUserId(Long userId, Authentication authentication) {
		userRepository.findById(userId)
				.orElseThrow(() -> new ShopVistaException("User not found.", HttpStatus.NOT_FOUND));

		List<Order> orders = orderRepository.findAllByUserId(userId);
		List<OrderDto> orderDtos = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Order order : orders) {
			OrderDto orderDto = new OrderDto();
			orderDto.setId(order.getId());
			orderDto.setTotal(order.getTotal());
			String dateCreatedStr = dateFormat.format(order.getDateCreated());
			orderDto.setDateCreated(dateCreatedStr);
			orderDtos.add(orderDto);
		}
		return orderDtos;
	}

	public Order createOrderFromCart(CartDto cart, Long userId, Authentication authentication) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ShopVistaException("User not found", HttpStatus.NOT_FOUND));

		Order order = new Order();
		order.setUser(user);
		order.setTotal(cart.getTotalPrice());
		order.setDateCreated(new Date());
		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItemDto cartItem : cart.getCartItems()) {
			OrderItem orderItem = new OrderItem();
			Product product = productRepository.findById(cartItem.getProductId())
					.orElseThrow(() -> new ShopVistaException("Product not found", HttpStatus.NOT_FOUND));
			orderItem.setProduct(product);
			orderItem.setQuantity(cartItem.getQuantity());
			orderItems.add(orderItem);
		}
		order.setOrderItems(orderItems);
		return orderRepository.save(order);
	}

}