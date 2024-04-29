package com.asm.shopvista.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
	private String tokenId;
	private BigDecimal amount;
	private String currency;
	private Object metadata;

	public PaymentDto(BigDecimal amount, String currency, Object metadata) {
		this.amount = amount;
		this.currency = currency;
		this.metadata = metadata;
	}
}
