package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.dto.CustomerCardApplicationRequest;
import com.example.dto.CustomerLoginRequest;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerCardApplication;
import com.example.model.CustomerProfile;
import com.example.repository.CardRepository;
import com.example.repository.CustomerCardApplicationRepository;
import com.example.repository.CustomerProfileRepository;
import com.example.util.Hasher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerProfileServiceTest {

    @Mock
    private CustomerProfileRepository customerProfileRepository;

    @Mock
    private CustomerCardApplicationRepository customerCardApplicationRepository;

    @Mock
    private CardRepository customerCardRepository;

    @InjectMocks
    private CustomerProfileService customerProfileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCustomerCardApplication() {
        CustomerCardApplicationRequest request = new CustomerCardApplicationRequest();
        request.setCustomerId(1L);
        request.setCardType("Visa");
        request.setIsUpgrade(true);
        request.setStatus(CustomerCardApplication.ApplicationStatus.APPROVED);
        request.setAccountNumber(12345L);

        CustomerProfile customerProfile = new CustomerProfile();
        when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(customerProfile));

        CustomerCardApplication application = new CustomerCardApplication();
        when(customerCardApplicationRepository.save(any(CustomerCardApplication.class))).thenReturn(application);

        CustomerCardApplication result = customerProfileService.createCustomerCardApplication(request);

        assertEquals(application, result);
    }

    @Test
    public void testGetCustomerProfileById() {
        CustomerProfile customerProfile = new CustomerProfile();
        when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(customerProfile));

        CustomerProfile result = customerProfileService.getCustomerProfileById(1L);

        assertEquals(customerProfile, result);
    }

    @Test
    public void testGetApplicationsByCustomerId() {
        List<CustomerCardApplication> applications = new ArrayList<>();
        when(customerCardApplicationRepository.findByCustomerProfileCustomerId(1L)).thenReturn(applications);

        List<CustomerCardApplication> result = customerProfileService.getApplicationsByCustomerId(1L);

        assertEquals(applications, result);
    }

    @Test
    public void testGetCustomerById_Success() throws ResourceNotFoundException {
        CustomerProfile customerProfile = new CustomerProfile();
        when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(customerProfile));

        Optional<CustomerProfile> result = customerProfileService.getCustomerById(1L);

        assertEquals(Optional.of(customerProfile), result);
    }

    @Test
    public void testGetCustomerById_NotFound() {
        when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerProfileService.getCustomerById(1L);
        });
    }

    @Test
    public void testCreateCustomer_Success() throws BadRequestException {
        CustomerProfile customerProfile = new CustomerProfile();
        when(customerProfileRepository.save(any(CustomerProfile.class))).thenReturn(customerProfile);

        CustomerProfile result = customerProfileService.createCustomer(customerProfile);

        assertEquals(customerProfile, result);
    }

    @Test
    public void testCreateCustomer_NullProfile() {
        assertThrows(BadRequestException.class, () -> {
            customerProfileService.createCustomer(null);
        });
    }

    @Test
    public void testUpdateCustomer_Success() throws ResourceNotFoundException {
        CustomerProfile existingCustomer = new CustomerProfile();
        when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerProfileRepository.save(any(CustomerProfile.class))).thenReturn(existingCustomer);

        CustomerProfile customerDetails = new CustomerProfile();
        customerDetails.setName("New Name");

        CustomerProfile result = customerProfileService.updateCustomer(1L, customerDetails);

        assertEquals(existingCustomer, result);
        assertEquals("New Name", existingCustomer.getName());
    }

    @Test
    public void testUpdateCustomer_NotFound() {
        when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerProfile customerDetails = new CustomerProfile();

        assertThrows(ResourceNotFoundException.class, () -> {
            customerProfileService.updateCustomer(1L, customerDetails);
        });
    }

    @Test
    public void testLoginCustomer_Success_FirstLogin() throws ResourceNotFoundException {
        CustomerLoginRequest loginRequest = new CustomerLoginRequest();
        loginRequest.setCustomerId(1L);
        loginRequest.setPassword("password");

        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setFirstLogin(true);
        when(customerProfileRepository.findByCustomerIdAndPassword(1L, "password"))
                .thenReturn(Optional.of(customerProfile));

        ResponseEntity<String> response = customerProfileService.loginCustomer(loginRequest);

        
       
    }

    @Test
    public void testLoginCustomer_Success_NotFirstLogin() throws ResourceNotFoundException {
        CustomerLoginRequest loginRequest = new CustomerLoginRequest();
        loginRequest.setCustomerId(1L);
        loginRequest.setPassword("password");

        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setFirstLogin(false);
        when(customerProfileRepository.findByCustomerIdAndPassword(1L, "password"))
                .thenReturn(Optional.of(customerProfile));

        ResponseEntity<String> response = customerProfileService.loginCustomer(loginRequest);

       
    }

    @Test
    public void testLoginCustomer_InvalidCredentials() throws ResourceNotFoundException {
        CustomerLoginRequest loginRequest = new CustomerLoginRequest();
        loginRequest.setCustomerId(1L);
        loginRequest.setPassword("password");

        when(customerProfileRepository.findByCustomerIdAndPassword(1L, "password")).thenReturn(Optional.empty());

        ResponseEntity<String> response = customerProfileService.loginCustomer(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid customer ID or password", response.getBody());
    }

    @Test
    public void testUpdatePassword_Success() throws ResourceNotFoundException {
        CustomerProfile customerProfile = new CustomerProfile();
        when(customerProfileRepository.findById(1L)).thenReturn(Optional.of(customerProfile));

        ResponseEntity<String> response = customerProfileService.updatePassword(1L, "newPassword");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated successfully, please login again", response.getBody());
    }

    @Test
    public void testUpdatePassword_CustomerNotFound() throws ResourceNotFoundException {
        when(customerProfileRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = customerProfileService.updatePassword(1L, "newPassword");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Customer not found", response.getBody());
    }
}
