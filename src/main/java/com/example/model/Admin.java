package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "admin")
public class Admin {

	@Id
	@NotNull(message = "Username cannot be null")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	public String getUsername() {
		return username;
	}

	public Admin(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public Admin() {
		super();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
