package com.example.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.example.model.CustomerCardApplication.ApplicationStatus;

public class CustomerCardApplicationRequestTest {

    @Test
    public void testNoArgsConstructor() {
        CustomerCardApplicationRequest request = new CustomerCardApplicationRequest();
        
        assertNull(request.getCustomerId());
        assertNull(request.getAccountNumber());
        assertNull(request.getCardType());
        assertNull(request.getIsUpgrade());
        assertNull(request.getStatus());
    }

    @Test
    public void testAllArgsConstructor() {
        Long customerId = 1L;
        Long accountNumber = 12345L;
        String cardType = "Visa";
        Boolean isUpgrade = true;
        ApplicationStatus status = ApplicationStatus.APPROVED;
        
        CustomerCardApplicationRequest request = new CustomerCardApplicationRequest(customerId, cardType, isUpgrade, status);
        
        request.setAccountNumber(accountNumber);
        
        assertEquals(customerId, request.getCustomerId());
        assertEquals(accountNumber, request.getAccountNumber());
        assertEquals(cardType, request.getCardType());
        assertEquals(isUpgrade, request.getIsUpgrade());
        assertEquals(status, request.getStatus());
    }

    @Test
    public void testSettersAndGetters() {
        CustomerCardApplicationRequest request = new CustomerCardApplicationRequest();
        
        Long customerId = 1L;
        Long accountNumber = 12345L;
        String cardType = "Visa";
        Boolean isUpgrade = true;
        ApplicationStatus status = ApplicationStatus.APPROVED;
        
        request.setCustomerId(customerId);
        request.setAccountNumber(accountNumber);
        request.setCardType(cardType);
        request.setIsUpgrade(isUpgrade);
        request.setStatus(status);
        
        assertEquals(customerId, request.getCustomerId());
        assertEquals(accountNumber, request.getAccountNumber());
        assertEquals(cardType, request.getCardType());
        assertEquals(isUpgrade, request.getIsUpgrade());
        assertEquals(status, request.getStatus());
    }
}
