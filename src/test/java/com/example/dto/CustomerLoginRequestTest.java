package com.example.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerLoginRequestTest {

    private CustomerLoginRequest customerLoginRequest;

    @BeforeEach
    public void setUp() {
        customerLoginRequest = new CustomerLoginRequest(1234567890L, "password123");
    }

    @Test
    public void testGetters() {
        assertEquals(1234567890L, customerLoginRequest.getCustomerId());
        assertEquals("password123", customerLoginRequest.getPassword());
    }

    @Test
    public void testSetters() {
        customerLoginRequest.setCustomerId(9876543210L);
        customerLoginRequest.setPassword("newpassword");

        assertEquals(9876543210L, customerLoginRequest.getCustomerId());
        assertEquals("newpassword", customerLoginRequest.getPassword());
    }

    @Test
    public void testDefaultConstructor() {
        CustomerLoginRequest defaultCustomerLoginRequest = new CustomerLoginRequest();
        assertNotNull(defaultCustomerLoginRequest);
    }

    @Test
    public void testParameterizedConstructor() {
        CustomerLoginRequest paramCustomerLoginRequest = new CustomerLoginRequest(1122334455L, "mypassword");

        assertEquals(1122334455L, paramCustomerLoginRequest.getCustomerId());
        assertEquals("mypassword", paramCustomerLoginRequest.getPassword());
    }
}
