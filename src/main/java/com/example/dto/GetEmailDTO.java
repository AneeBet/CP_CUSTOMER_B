package com.example.dto;

public class GetEmailDTO {
 
	private String Email;
 
	public String getEmail() {
		return Email;
	}
 
	public void setEmail(String email) {
		Email = email;
	}
 
	public GetEmailDTO(String email) {
		super();
		Email = email;
	}
 
	public GetEmailDTO() {
		super();
	}
	
	
}
 