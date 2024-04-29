package com.asm.shopvista.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.asm.shopvista.dto.CartDto;
import com.asm.shopvista.dto.CartItemDto;
import com.asm.shopvista.entity.Cart;
import com.asm.shopvista.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "id", source = "cart.id")
    @Mapping(target = "cartItems", source = "cartItems")
    CartDto cartToCartDto(Cart cart, BigDecimal totalPrice, List<CartItemDto> cartItems);

    @Mapping(target = "subTotal", expression = "java(cartItem.getSubTotal())")
    CartItemDto cartItemToCartItemDto(CartItem cartItem);
}