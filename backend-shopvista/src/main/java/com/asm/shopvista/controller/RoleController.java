package com.asm.shopvista.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asm.shopvista.entity.Role;
import com.asm.shopvista.service.RoleService;

@RestController
@RequestMapping("api/v1/auth")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping({ "/role" })
	public Role createNewRole(@RequestBody Role role) {
		return roleService.createNewRole(role);
	}
}
