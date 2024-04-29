package com.asm.shopvista.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDto {
	private Long id;
	private String dateCreated;
	private BigDecimal total;
}