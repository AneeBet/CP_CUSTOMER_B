package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.service.CustomerAuthService;
import com.example.service.CustomerProfileService;

public class CustomerProfileControllerTest {

    @Mock
    private CustomerProfileService customerProfileService;

    @Mock
    private CustomerAuthService customerAuthService;

    @InjectMocks
    private CustomerProfileController customerProfileController;

    private CustomerProfile customerProfile;
    private CustomerCardApplicationRequest customerCardApplicationRequest;
    private CustomerLoginRequest customerLoginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerProfile = new CustomerProfile();
        customerProfile.setCustomerId(1L);
        customerProfile.setEmail("customer@example.com");

        customerCardApplicationRequest = new CustomerCardApplicationRequest();
        customerLoginRequest = new CustomerLoginRequest();
    }

    @Test
    void testCreateCustomerCardApplication() {

        ResponseEntity<String> response = customerProfileController.createCustomerCardApplication(customerCardApplicationRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Application Successful", response.getBody());
        verify(customerProfileService, times(1)).createCustomerCardApplication(customerCardApplicationRequest);
    }

    @Test
    void testGetApplicationsByCustomerId() {
        List<CustomerCardApplication> applications = List.of(new CustomerCardApplication());
        when(customerProfileService.getApplicationsByCustomerId(1L)).thenReturn(applications);

        ResponseEntity<List<CustomerCardApplication>> response = customerProfileController.getApplicationsByCustomerId(Map.of("customerId", 1L));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(applications, response.getBody());
        verify(customerProfileService, times(1)).getApplicationsByCustomerId(1L);
    }

    @Test
    void testGetCustomerById_Success() throws ResourceNotFoundException {
        when(customerProfileService.getCustomerById(1L)).thenReturn(java.util.Optional.of(customerProfile));

        ResponseEntity<CustomerProfile> response = customerProfileController.getCustomerById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerProfile, response.getBody());
    }

    @Test
    void testGetCustomerById_NotFound() throws ResourceNotFoundException {
        when(customerProfileService.getCustomerById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerProfileController.getCustomerById(1L));
    }

    @Test
    void testCreateCustomer_Success() throws BadRequestException {
        when(customerProfileService.createCustomer(any(CustomerProfile.class))).thenReturn(customerProfile);

        ResponseEntity<CustomerProfile> response = customerProfileController.createCustomer(customerProfile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerProfile, response.getBody());
    }

    @Test
    void testCreateCustomer_NullCustomer() throws BadRequestException {
        ResponseEntity<CustomerProfile> response = customerProfileController.createCustomer(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateCustomer() throws ResourceNotFoundException {
        when(customerProfileService.updateCustomer(anyLong(), any(CustomerProfile.class))).thenReturn(customerProfile);

        ResponseEntity<CustomerProfile> response = customerProfileController.updateCustomer(1L, customerProfile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerProfile, response.getBody());
    }

    @Test
    void testLoginCustomer_Success() throws BadRequestException, ResourceNotFoundException {
        when(customerProfileService.loginCustomer(any(CustomerLoginRequest.class))).thenReturn(ResponseEntity.ok("Login Successful"));

        ResponseEntity<String> response = customerProfileController.loginCustomer(customerLoginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login Successful", response.getBody());
    }

    @Test
    void testLoginCustomer_NullRequest() {
        assertThrows(BadRequestException.class, () -> customerProfileController.loginCustomer(null));
    }

    @Test
    void testUpdatePassword_Success() throws BadRequestException, ResourceNotFoundException {
        when(customerProfileService.updatePassword(anyLong(), anyString())).thenReturn(ResponseEntity.ok("Password Updated"));

        ResponseEntity<String> response = customerProfileController.updatePassword(Map.of("customerId", "1", "password", "newpassword"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password Updated", response.getBody());
    }

    @Test
    void testUpdatePassword_NullRequest() {
        assertThrows(BadRequestException.class, () -> customerProfileController.updatePassword(null));
    }

    @Test
    void testForgotPassword_Success() {
        when(customerAuthService.sendOtp("customer@example.com")).thenReturn(true);

        boolean result = customerProfileController.forgotPassword(Map.of("email", "customer@example.com"));

        assertTrue(result);
        verify(customerAuthService, times(1)).sendOtp("customer@example.com");
    }

    @Test
    void testForgotPassword_Failure() {
        when(customerAuthService.sendOtp("customer@example.com")).thenReturn(false);

        boolean result = customerProfileController.forgotPassword(Map.of("email", "customer@example.com"));

        assertFalse(result);
        verify(customerAuthService, times(1)).sendOtp("customer@example.com");
    }

    @Test
    void testVerifyOtp() {
        when(customerAuthService.verifyPasswordResetOtp("customer@example.com", "123456")).thenReturn(true);

        boolean result = customerProfileController.verifyOtp(Map.of("email", "customer@example.com", "otp", "123456"));

        assertTrue(result);
        verify(customerAuthService, times(1)).verifyPasswordResetOtp("customer@example.com", "123456");
    }

    @Test
    void testResetPassword() throws ResourceNotFoundException, BadRequestException {
        doNothing().when(customerAuthService).resetPassword(anyString(), anyString(), anyString());

        customerProfileController.resetPassword(Map.of("email", "customer@example.com", "otp", "123456", "password", "newpassword"));

        verify(customerAuthService, times(1)).resetPassword("customer@example.com", "123456", "newpassword");
    }
}
