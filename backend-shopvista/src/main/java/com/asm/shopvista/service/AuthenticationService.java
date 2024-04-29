package com.asm.shopvista.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.asm.shopvista.dto.UserLoginDto;
import com.asm.shopvista.entity.Role;
import com.asm.shopvista.entity.User;
import com.asm.shopvista.repository.RoleRepository;
import com.asm.shopvista.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void initRoleAndUser() {
		Role adminRole = new Role();
		adminRole.setRoleName("Admin");
		adminRole.setRoleDescription("Admin Role - With Full Privileges");
		roleRepo.save(adminRole);

		Role userRole = new Role();
		userRole.setRoleName("User");
		userRole.setRoleDescription("User Role - Limited Access - Default role for newly created user");
		roleRepo.save(userRole);

		User adminUser = userRepo.findByEmail("sagar.kurde@asm.com").orElse(null);
		if (adminUser == null) {
			adminUser = new User();
			adminUser.setFirstname("Sagar");
			adminUser.setLastname("Kurde");
			adminUser.setEmail("sagar.kurde@asm.com");
			adminUser.setMobilenumber("8007886118");
			adminUser.setPassword(getEncodedPassword("sagar123"));

			Set<Role> adminRoles = new HashSet<>();
			adminRoles.add(adminRole);
			adminUser.setRole(adminRoles);
			userRepo.save(adminUser);
		}
	}

	public User registerNewUser(User user) {
		User existingUser = userRepo.findByEmail(user.getEmail()).orElse(null);

		if (existingUser != null) {
			return null;
		}

		Role role = roleRepo.findById("User").get();
		Set<Role> userRoles = new HashSet<>();
		userRoles.add(role);
		user.setRole(userRoles);
		user.setPassword(getEncodedPassword(user.getPassword()));

		return userRepo.save(user);
	}

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public UserLoginDto login(String email, String password) {
		User user = userRepo.findByEmail(email).orElse(null);
		if (user != null) {
			return new UserLoginDto(user.getId(), RandomStringUtils.randomAlphanumeric(10), user);
		} else {
			return new UserLoginDto(null, null, null);
		}
	}
}
