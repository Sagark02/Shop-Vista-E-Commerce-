package com.asm.shopvista.dto;

import lombok.Data;

@Data
public class CardDto {
	private String number;
	private String expDate;
	private String cvc;
	private String zip;
}
