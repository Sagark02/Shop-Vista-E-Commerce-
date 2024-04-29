package com.asm.shopvista.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.asm.shopvista.dto.CartDto;
import com.asm.shopvista.dto.CartItemDto;
import com.asm.shopvista.dto.ProductDto;
import com.asm.shopvista.entity.Cart;
import com.asm.shopvista.entity.CartItem;
import com.asm.shopvista.entity.Product;
import com.asm.shopvista.exception.ShopVistaException;
import com.asm.shopvista.mapper.CartMapper;
import com.asm.shopvista.repository.CartItemRepository;
import com.asm.shopvista.repository.CartRepository;
import com.asm.shopvista.repository.ProductRepository;
import com.asm.shopvista.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	public CartDto getCartByUserId(Long userId) {
		Optional<Cart> userCart = cartRepository.findByUserId(userId);
		if (userCart.isPresent()) {
			Cart cart = userCart.get();

			CartDto cartDto = new CartDto();
			cartDto.setId(cart.getId());
			cartDto.setUserId(cart.getUserId());

			List<CartItemDto> cartItemDtos = getCartItemDto(cart);

			Map<Long, CartItemDto> cartItemMap = new HashMap<>();

			for (CartItemDto cartItemDto : cartItemDtos) {
				Long productId = cartItemDto.getProductId();
				if (cartItemMap.containsKey(productId)) {
					CartItemDto existingItem = cartItemMap.get(productId);
					existingItem.setQuantity(existingItem.getQuantity() + cartItemDto.getQuantity());
					existingItem.setSubTotal(existingItem.getSubTotal().add(cartItemDto.getSubTotal()));
				} else {
					cartItemMap.put(productId, cartItemDto);
				}
			}

			List<CartItemDto> consolidatedCartItems = new ArrayList<>(cartItemMap.values());
			cartDto.setCartItems(consolidatedCartItems);

			for (CartItemDto consolidatedCartItem : consolidatedCartItems) {
				Long productId = consolidatedCartItem.getProductId();
				Product product = productRepository.findById(productId)
						.orElseThrow(() -> new ShopVistaException("Product not found", HttpStatus.NOT_FOUND));

				ProductDto productDto = new ProductDto();
				productDto.setImgUrl(product.getImgUrl());
				consolidatedCartItem.setProduct(productDto);
			}

			BigDecimal totalPrice = consolidatedCartItems.stream().map(CartItemDto::getSubTotal).reduce(BigDecimal.ZERO,
					BigDecimal::add);
			
			cartDto.setTotalPrice(totalPrice);

			return cartDto;
		} else {
			return null;
		}
	}

	public int getNumberOfItemsInCart(Long userId) {
		Optional<Cart> userCart = cartRepository.findByUserId(userId);
		if (userCart.isPresent()) {
			Cart cart = userCart.get();
			return cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum();
		} else {
			return 0;
		}
	}

	private static List<CartItemDto> getCartItemDto(Cart cart) {
		List<CartItemDto> cartItemDtos = new ArrayList<>();
		for (CartItem cartItem : cart.getCartItems()) {
			CartItemDto cartItemDto = new CartItemDto();
			cartItemDto.setProductId(cartItem.getProductId());
			cartItemDto.setProductName(cartItem.getProductName());
			cartItemDto.setQuantity(cartItem.getQuantity());
			cartItemDto.setPrice(cartItem.getPrice());

			cartItemDto.setSubTotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
			cartItemDtos.add(cartItemDto);
		}
		return cartItemDtos;
	}

	public CartDto addItemToCart(Long userId, Long productId, int quantity) {
		userRepository.findById(userId)
				.orElseThrow(() -> new ShopVistaException("User not found", HttpStatus.NOT_FOUND));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ShopVistaException("Product not found", HttpStatus.NOT_FOUND));

		BigDecimal itemPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

		Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
		Cart userCart = optionalCart.orElse(new Cart());
		if (optionalCart.isEmpty()) {
			userCart.setUserId(userId);
			cartRepository.save(userCart);
		}

		CartItem newItem = new CartItem();
		newItem.setProductId(product.getId());
		newItem.setProductName(product.getName());
		newItem.setQuantity(quantity);
		newItem.setPrice(itemPrice);
		newItem.setCart(userCart);
		newItem.setSubTotal(itemPrice);

		cartItemRepository.save(newItem);

		userCart.getCartItems().add(newItem);

		BigDecimal totalPrice = userCart.getCartItems().stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		userCart.setTotalPrice(totalPrice);
		cartRepository.save(userCart);

		List<CartItemDto> cartItemDtos = userCart.getCartItems().stream().map(item -> {
			CartItemDto itemDto = new CartItemDto();
			itemDto.setProductId(item.getProductId());
			itemDto.setProductName(item.getProductName());
			itemDto.setQuantity(item.getQuantity());
			itemDto.setPrice(item.getPrice());
			itemDto.setSubTotal(
					item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())).setScale(2, RoundingMode.HALF_UP));
			return itemDto;
		}).collect(Collectors.toList());

		return CartMapper.INSTANCE.cartToCartDto(userCart, totalPrice, cartItemDtos);
	}

	public CartDto removeItemFromCart(Long userId, Long productId) {
		userRepository.findById(userId)
				.orElseThrow(() -> new ShopVistaException("User not found", HttpStatus.NOT_FOUND));

		productRepository.findById(productId)
				.orElseThrow(() -> new ShopVistaException("Product not found", HttpStatus.NOT_FOUND));

		Cart userCart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ShopVistaException("Cart not found", HttpStatus.NOT_FOUND));

		CartItem cartItemToRemove = userCart.getCartItems().stream()
				.filter(item -> item.getProductId().equals(productId)).findFirst()
				.orElseThrow(() -> new ShopVistaException("Cart item not found", HttpStatus.NOT_FOUND));

		userCart.getCartItems().remove(cartItemToRemove);
		cartItemRepository.delete(cartItemToRemove);

		BigDecimal totalPrice = userCart.getCartItems().stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		userCart.setTotalPrice(totalPrice);
		cartRepository.save(userCart);

		List<CartItemDto> cartItemDtos = getCartItemDto(userCart);
		return CartMapper.INSTANCE.cartToCartDto(userCart, totalPrice, cartItemDtos);
	}

	public Cart getCartEntityByUserId(Long userId) {
		return cartRepository.findByUserId(userId).orElseThrow(
				() -> new ShopVistaException("Cart not found for user id: " + userId, HttpStatus.NOT_FOUND));
	}

	public void clearCart(Long userId) {
		Cart cart = getCartEntityByUserId(userId);
		if (cart != null) {
			cart.getCartItems().clear();
			cart.setTotalPrice(BigDecimal.ZERO);
			cartRepository.save(cart);
		}
	}
}
