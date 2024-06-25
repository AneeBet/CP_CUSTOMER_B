package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.CustomerCardApplicationRequest;
import com.example.dto.CustomerLoginRequest;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerCardApplication;
import com.example.model.CustomerProfile;
import com.example.repository.CustomerCardApplicationRepository;
import com.example.repository.CustomerProfileRepository;
import com.example.util.Hasher;

@Service
public class CustomerProfileService {

	private final CustomerProfileRepository customerProfileRepository;
	private final CustomerCardApplicationRepository customerCardApplicationRepository;
	public CustomerProfileService(CustomerProfileRepository customerProfileRepository,
			CustomerCardApplicationRepository customerCardApplicationRepository ) {
		this.customerProfileRepository = customerProfileRepository;
		this.customerCardApplicationRepository = customerCardApplicationRepository;
	}

	// create customer additional card
	// application------------------------------------------------------------------------------------
	public CustomerCardApplication createCustomerCardApplication(CustomerCardApplicationRequest request) {
		CustomerCardApplication application = new CustomerCardApplication();
		application.setCreditCard(request.getCardType());
		application.setIsUpgrade(request.getIsUpgrade());
		application.setStatus(request.getStatus());
		application.setAccountNumber(request.getAccountNumber());
		CustomerProfile customerProfile = customerProfileRepository.findById(request.getCustomerId()).orElse(null);				
					application.setCustomerProfile(customerProfile);
         			return customerCardApplicationRepository.save(application);
 
 
	}
	public CustomerProfile getCustomerProfileById(Long customerId) {
		return customerProfileRepository.findById(customerId).orElse(null);
	}
	// ------------------------------------------------------------------------------------------------------------------

	// get all card
	// applications--------------------------------------------------------------------------------------------
	public List<CustomerCardApplication> getApplicationsByCustomerId(Long customerId) {
		return customerCardApplicationRepository.findByCustomerProfileCustomerId(customerId);
	}
	// ----------------------------------------------------------------------------------------------------------------

	public Optional<CustomerProfile> getCustomerById(Long id) throws ResourceNotFoundException {
		return Optional.ofNullable(customerProfileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id)));
	}

	public CustomerProfile createCustomer(CustomerProfile customerProfile) throws BadRequestException {
		if (customerProfile == null) {
			throw new BadRequestException("Customer profile cannot be null");
		}
		return customerProfileRepository.save(customerProfile);
	}

	public CustomerProfile updateCustomer(Long id, CustomerProfile customerDetails) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
		updateCustomerDetails(existingCustomer, customerDetails);
		return customerProfileRepository.save(existingCustomer);
	}

	private void updateCustomerDetails(CustomerProfile existingCustomer, CustomerProfile details) {
		existingCustomer.setName(details.getName());
		existingCustomer.setEmail(details.getEmail());
		existingCustomer.setPassword(details.getPassword());
		existingCustomer.setAddress(details.getAddress());
		existingCustomer.setDob(details.getDob());
		existingCustomer.setFirstLogin(details.getFirstLogin());
	}

	public ResponseEntity<String> loginCustomer(CustomerLoginRequest loginRequest) throws ResourceNotFoundException {
		Long customerId = loginRequest.getCustomerId();
		String password = loginRequest.getPassword();
		Optional<CustomerProfile> customerOpt = customerProfileRepository.findByCustomerIdAndPassword(customerId,
				Hasher.hashPassword(password));
		if (customerOpt.isPresent()) {
			CustomerProfile customerProfile = customerOpt.get();
			if (Boolean.TRUE.equals(customerProfile.getFirstLogin())) {
				return ResponseEntity.ok("First login, change password required");
			} else {
				return ResponseEntity.ok("Customer logged in successfully!");
			}
		} else {
			return ResponseEntity.badRequest().body("Invalid customer ID or password");
		}
	}

	public ResponseEntity<String> updatePassword(Long customerId, String newPassword) throws ResourceNotFoundException {
		Optional<CustomerProfile> customerOpt = customerProfileRepository.findById(customerId);
		if (customerOpt.isPresent()) {
			CustomerProfile customerProfile = customerOpt.get();
			customerProfile.setPassword(Hasher.hashPassword(newPassword));
			customerProfile.setFirstLogin(false);
			customerProfileRepository.save(customerProfile);
			return ResponseEntity.ok("Password updated successfully, please login again");
		} else {
			return ResponseEntity.badRequest().body("Customer not found");
		}
	}

}
