package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CustomerCardApplicationRequest;
import com.example.dto.CustomerLoginRequest;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerCardApplication;
import com.example.model.CustomerProfile;
import com.example.service.CustomerAuthService;
import com.example.service.CustomerProfileService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer")
public class CustomerProfileController {

	private CustomerProfileService customerProfileService;

	private CustomerAuthService customerAuthService;
	
	private static final String EMAIL = "email";

	public CustomerProfileController(CustomerProfileService customerProfileService,
			CustomerAuthService customerAuthService) {
		this.customerProfileService = customerProfileService;
		this.customerAuthService = customerAuthService;
	}

	
	@PostMapping("/create-customer-card-application")
	public ResponseEntity<String> createCustomerCardApplication(@RequestBody CustomerCardApplicationRequest request) {
		
		customerProfileService.createCustomerCardApplication(request);
		
		
		return ResponseEntity.ok("Application Successful");
	}

//	  get all customer card applications--------------------------------------------------------------------------------------	  

	@PostMapping("/getallcardapplications")
	public ResponseEntity<List<CustomerCardApplication>> getApplicationsByCustomerId(
			@RequestBody Map<String, Long> request) {
		Long customerId = request.get("customerId");
		List<CustomerCardApplication> applications = customerProfileService.getApplicationsByCustomerId(customerId);
		return ResponseEntity.ok(applications);
	}
//--------------------------------------------------------------------------------------------------------------	

	
	
	@GetMapping("/readone/{id}")
	public ResponseEntity<CustomerProfile> getCustomerById(@PathVariable("id") Long id) throws ResourceNotFoundException {
		CustomerProfile customer = customerProfileService.getCustomerById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
		return ResponseEntity.ok(customer);
	}

	@PostMapping("/add")
	public ResponseEntity<CustomerProfile> createCustomer(@RequestBody CustomerProfile customerProfile)
			throws BadRequestException {
		if (customerProfile == null) {
			return ResponseEntity.badRequest().body(null);
		}
		CustomerProfile createdCustomer = customerProfileService.createCustomer(customerProfile);
		return ResponseEntity.ok(createdCustomer);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<CustomerProfile> updateCustomer(@PathVariable("id") Long id,
			@RequestBody CustomerProfile customerDetails) throws ResourceNotFoundException {
		CustomerProfile updatedCustomer = customerProfileService.updateCustomer(id, customerDetails);
		return ResponseEntity.ok(updatedCustomer);
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginCustomer(@RequestBody CustomerLoginRequest loginRequest) throws BadRequestException, ResourceNotFoundException {
		if(loginRequest == null) {
			throw new BadRequestException("Login credentials cannot be empty");
		}
		return customerProfileService.loginCustomer(loginRequest);
	}

	@PostMapping("/update-password")
	public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> loginRequest) throws  BadRequestException, ResourceNotFoundException {
		if(loginRequest == null) {
			throw new BadRequestException("CustomerID and Password cannot be empty");
		}
		Long customerId = Long.parseLong(loginRequest.get("customerId")); // This should now correctly be a Long
		String newPassword = loginRequest.get("password");
		return customerProfileService.updatePassword(customerId, newPassword);
	}


	@PostMapping("/forgot-password")
	public boolean forgotPassword(@RequestBody Map<String, String> emailRequest) {
		if(customerAuthService.sendOtp(emailRequest.get(EMAIL)))
			return true;
		else
			return false;
	
		
	}

	@PostMapping("/verify-password-reset-otp")
	public boolean verifyOtp(@RequestBody Map<String, String> otpRequest) {
		return customerAuthService.verifyOtp(otpRequest.get(EMAIL), otpRequest.get("otp"));
	}

	@PostMapping("/reset-password")
	public void resetPassword(@RequestBody Map<String, String> resetPasswordRequest) throws ResourceNotFoundException {
		customerAuthService.resetPassword(resetPasswordRequest.get(EMAIL), resetPasswordRequest.get("otp"),
				resetPasswordRequest.get("password"));
	}

}
