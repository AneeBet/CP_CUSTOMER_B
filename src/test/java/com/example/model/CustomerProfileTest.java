package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerProfileTest {

    private CustomerProfile customerProfile;

    @BeforeEach
    public void setUp() {
        List<CustomerCardAccount> customerCardAccounts = new ArrayList<>();
        customerCardAccounts.add(new CustomerCardAccount());

        customerProfile = new CustomerProfile(
            1L,
            "John Doe",
            123456789012L,
            "john.doe@example.com",
            "password123",
            "1234 Main St, Anytown, USA",
            "ABCDE1234F",
            "1234567890",
            new Date(90, 0, 1), // January 1, 1990
            true,
            5,
            "Example Company",
            new BigDecimal("50000.00"),
            "/path/to/income/proof",
            customerCardAccounts
        );
    }

    @Test
    public void testGetters() {
        assertEquals(1L, customerProfile.getCustomerId());
        assertEquals("John Doe", customerProfile.getName());
        assertEquals(123456789012L, customerProfile.getAadhaarNumber());
        assertEquals("john.doe@example.com", customerProfile.getEmail());
        assertEquals("password123", customerProfile.getPassword());
        assertEquals("1234 Main St, Anytown, USA", customerProfile.getAddress());
        assertEquals("ABCDE1234F", customerProfile.getPanId());
        assertEquals("1234567890", customerProfile.getMobileNumber());
        assertNotNull(customerProfile.getDob());
        assertEquals(true, customerProfile.getFirstLogin());
        assertEquals(5, customerProfile.getEmploymentYears());
        assertEquals("Example Company", customerProfile.getCompanyName());
        assertEquals(0, new BigDecimal("50000.00").compareTo(customerProfile.getAnnualIncome()));
        assertEquals("/path/to/income/proof", customerProfile.getIncomeProofFilePath());
        assertNotNull(customerProfile.getCustomerCardAccounts());
    }

    @Test
    public void testSetters() {
        customerProfile.setCustomerId(2L);
        customerProfile.setName("Jane Doe");
        customerProfile.setAadhaarNumber(987654321012L);
        customerProfile.setEmail("jane.doe@example.com");
        customerProfile.setPassword("newpassword123");
        customerProfile.setAddress("5678 Main St, Anytown, USA");
        customerProfile.setPanId("XYZAB1234F");
        customerProfile.setMobileNumber("0987654321");
        Date newDob = new Date(91, 1, 1); // February 1, 1991
        customerProfile.setDob(newDob);
        customerProfile.setFirstLogin(false);
        customerProfile.setEmploymentYears(10);
        customerProfile.setCompanyName("New Company");
        customerProfile.setAnnualIncome(new BigDecimal("100000.00"));
        customerProfile.setIncomeProofFilePath("/new/path/to/income/proof");
        List<CustomerCardAccount> newCustomerCardAccounts = new ArrayList<>();
        newCustomerCardAccounts.add(new CustomerCardAccount());
        customerProfile.setCustomerCardAccounts(newCustomerCardAccounts);

        assertEquals(2L, customerProfile.getCustomerId());
        assertEquals("Jane Doe", customerProfile.getName());
        assertEquals(987654321012L, customerProfile.getAadhaarNumber());
        assertEquals("jane.doe@example.com", customerProfile.getEmail());
        assertEquals("newpassword123", customerProfile.getPassword());
        assertEquals("5678 Main St, Anytown, USA", customerProfile.getAddress());
        assertEquals("XYZAB1234F", customerProfile.getPanId());
        assertEquals("0987654321", customerProfile.getMobileNumber());
        assertEquals(newDob, customerProfile.getDob());
        assertEquals(false, customerProfile.getFirstLogin());
        assertEquals(10, customerProfile.getEmploymentYears());
        assertEquals("New Company", customerProfile.getCompanyName());
        assertEquals(0, new BigDecimal("100000.00").compareTo(customerProfile.getAnnualIncome()));
        assertEquals("/new/path/to/income/proof", customerProfile.getIncomeProofFilePath());
        assertEquals(newCustomerCardAccounts, customerProfile.getCustomerCardAccounts());
    }
}
