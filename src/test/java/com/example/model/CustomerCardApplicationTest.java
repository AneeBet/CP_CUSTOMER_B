package com.example.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerCardApplicationTest {

    private CustomerCardApplication customerCardApplication;
    private CustomerProfile customerProfile;
    private Admin admin;

    @BeforeEach
    public void setUp() {
        customerProfile = new CustomerProfile();
        customerProfile.setCustomerId(1L);
        customerProfile.setEmail("customer@example.com");

        admin = new Admin();
        admin.setUsername("admin");

        customerCardApplication = new CustomerCardApplication();
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(customerCardApplication.getApplicationId());
        assertNull(customerCardApplication.getCustomerProfile());
        assertNull(customerCardApplication.getAadhaarNumber());
        assertNull(customerCardApplication.getStatus());
        assertNull(customerCardApplication.getCreditCard());
        assertNull(customerCardApplication.getAccountNumber());
        assertFalse(customerCardApplication.getIsUpgrade());
        assertNull(customerCardApplication.getAdmin());
    }

    @Test
    public void testConstructorAndGetters() {
        CustomerCardApplication customerCardApplication = new CustomerCardApplication(
                123L, customerProfile, 123456789012L, CustomerCardApplication.ApplicationStatus.PENDING,
                "VISA", 456L, true, admin);

        assertEquals(123L, customerCardApplication.getApplicationId());
        assertEquals(customerProfile, customerCardApplication.getCustomerProfile());
        assertEquals(123456789012L, customerCardApplication.getAadhaarNumber());
        assertEquals(CustomerCardApplication.ApplicationStatus.PENDING, customerCardApplication.getStatus());
        assertEquals("VISA", customerCardApplication.getCreditCard());
        assertEquals(456L, customerCardApplication.getAccountNumber());
        assertTrue(customerCardApplication.getIsUpgrade());
        assertEquals(admin, customerCardApplication.getAdmin());
    }

    @Test
    public void testSetAndGetApplicationId() {
        customerCardApplication.setApplicationId(123L);
        assertEquals(123L, customerCardApplication.getApplicationId());
    }

    @Test
    public void testSetAndGetCustomerProfile() {
        customerCardApplication.setCustomerProfile(customerProfile);
        assertEquals(customerProfile, customerCardApplication.getCustomerProfile());
    }

    @Test
    public void testSetAndGetAadhaarNumber() {
        customerCardApplication.setAadhaarNumber(123456789012L);
        assertEquals(123456789012L, customerCardApplication.getAadhaarNumber());
    }

    @Test
    public void testSetAndGetStatus() {
        customerCardApplication.setStatus(CustomerCardApplication.ApplicationStatus.APPROVED);
        assertEquals(CustomerCardApplication.ApplicationStatus.APPROVED, customerCardApplication.getStatus());
    }

    @Test
    public void testSetAndGetCreditCard() {
        customerCardApplication.setCreditCard("MASTER");
        assertEquals("MASTER", customerCardApplication.getCreditCard());
    }

    @Test
    public void testSetAndGetAccountNumber() {
        customerCardApplication.setAccountNumber(456L);
        assertEquals(456L, customerCardApplication.getAccountNumber());
    }

    @Test
    public void testSetAndGetIsUpgrade() {
        customerCardApplication.setIsUpgrade(true);
        assertTrue(customerCardApplication.getIsUpgrade());
    }

    @Test
    public void testSetAndGetAdmin() {
        customerCardApplication.setAdmin(admin);
        assertEquals(admin, customerCardApplication.getAdmin());
    }
}
