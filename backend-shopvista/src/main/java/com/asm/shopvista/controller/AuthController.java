package com.asm.shopvista.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asm.shopvista.dto.UserLoginDto;
import com.asm.shopvista.entity.User;
import com.asm.shopvista.exception.ShopVistaException;
import com.asm.shopvista.service.AuthenticationService;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

	Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationService authService;

	@PostMapping({ "/register" })
	public User registerNewUser(@RequestBody User user) {
		User registeredUser = authService.registerNewUser(user);
		if (registeredUser == null) {
			throw new ShopVistaException("Failed to register user", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return registeredUser;
	}

	@PostMapping("/login")
	public UserLoginDto login(@RequestBody User userdetails) {
		logger.info("Loging request received for user[{}]", userdetails.getEmail());

		UserLoginDto userLoginDto = authService.login(userdetails.getEmail(), userdetails.getPassword());
		if (userLoginDto == null) {
			throw new ShopVistaException("Invalid credentials", HttpStatus.NOT_FOUND);
		}

		logger.info("User [{} {}] with ID [{}] successfully loggedin", userLoginDto.getUser().getFirstname(),
				userLoginDto.getUser().getLastname());

		return userLoginDto;
	}

}
