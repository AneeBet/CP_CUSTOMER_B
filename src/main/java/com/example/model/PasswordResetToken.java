package com.example.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class PasswordResetToken {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email")
	@NotNull(message = "Email cannot be null")
	@Email(message = "Email should be valid")
	private String email;

	@Column(name = "otp")
	@NotNull(message = "OTP cannot be null")
	@Size(min = 6, max = 6, message = "OTP must be 6 characters long")
	private String otp;

	@Column(name = "expiryTime")
	@NotNull(message = "Expiry time cannot be null")
	private LocalDateTime expiryTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

}
