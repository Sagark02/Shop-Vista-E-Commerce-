package com.asm.shopvista.dto;

import com.asm.shopvista.entity.User;

import lombok.Getter;

@Getter
public class UserLoginDto {

	private Long id;
	private String token;
	private User user;

	public UserLoginDto() {
		super();
	}

	public UserLoginDto(Long id, String token, User user) {
		this.id = id;
		this.token = token;
		this.user = user;
	}

	public void setId(Long id) {
		this.id = id;
	}
}