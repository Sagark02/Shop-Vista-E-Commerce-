package com.asm.shopvista.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userid", unique = true)
	private Long id;

	@NotBlank(message = "Invalid First Name")
	private String firstname;

	@NotBlank(message = "Invalid Last Name")
	private String lastname;

	@Email
	@NotBlank(message = "Invalid email address")
	private String email;

	@Size(min = 10, message = "please entered 10 digit phone number")
	@Size(max = 10, message = "please entered 10 digit phone number")
	private String mobilenumber;

	@NotBlank(message = "Invalid User Password")
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })

	private Set<Role> role;

}
