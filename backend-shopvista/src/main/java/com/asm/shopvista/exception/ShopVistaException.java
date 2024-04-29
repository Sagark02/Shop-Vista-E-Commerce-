package com.asm.shopvista.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ShopVistaException extends RuntimeException {

	private static final long serialVersionUID = 1354321165777240617L;

	private final HttpStatus httpStatus;

	public ShopVistaException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}
}
