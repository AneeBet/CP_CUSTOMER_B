package com.example.dto;

public class CustomerLoginRequest {
    private Long customerId;
    private String password;

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public CustomerLoginRequest(Long customerId, String password) {
		super();
		this.customerId = customerId;
		this.password = password;
	}

	public CustomerLoginRequest() {
		super();
	}

	public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
