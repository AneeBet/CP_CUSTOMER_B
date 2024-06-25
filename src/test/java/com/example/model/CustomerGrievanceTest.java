package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerGrievanceTest {

    private CustomerGrievance customerGrievance;
    private CustomerCardAccount customerCardAccount;

    @BeforeEach
    public void setUp() {
        customerCardAccount = new CustomerCardAccount();

        customerGrievance = new CustomerGrievance(
            1L,
            customerCardAccount,
            "Test Subject",
            "Test Description",
            new Date(),
            ResolutionStatus.PENDING
        );
    }

    @Test
    public void testGetters() {
        assertEquals(1L, customerGrievance.getGrievanceId());
        assertEquals(customerCardAccount, customerGrievance.getCustomerCardAccount());
        assertEquals("Test Subject", customerGrievance.getSubject());
        assertEquals("Test Description", customerGrievance.getDescription());
        assertNotNull(customerGrievance.getTimestamp());
        assertEquals(ResolutionStatus.PENDING, customerGrievance.getStatus());
    }

    @Test
    public void testSetters() {
        CustomerCardAccount newCustomerCardAccount = new CustomerCardAccount();
        Date newTimestamp = new Date();

        customerGrievance.setGrievanceId(2L);
        customerGrievance.setCustomerCardAccount(newCustomerCardAccount);
        customerGrievance.setSubject("New Subject");
        customerGrievance.setDescription("New Description");
        customerGrievance.setTimestamp(newTimestamp);
        customerGrievance.setStatus(ResolutionStatus.RESOLVED);

        assertEquals(2L, customerGrievance.getGrievanceId());
        assertEquals(newCustomerCardAccount, customerGrievance.getCustomerCardAccount());
        assertEquals("New Subject", customerGrievance.getSubject());
        assertEquals("New Description", customerGrievance.getDescription());
        assertEquals(newTimestamp, customerGrievance.getTimestamp());
        assertEquals(ResolutionStatus.RESOLVED, customerGrievance.getStatus());
    }

    @Test
    public void testDefaultConstructor() {
        CustomerGrievance defaultCustomerGrievance = new CustomerGrievance();
        assertNotNull(defaultCustomerGrievance);
    }
}
