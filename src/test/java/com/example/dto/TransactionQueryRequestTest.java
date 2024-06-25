package com.example.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionQueryRequestTest {

    private TransactionQueryRequest transactionQueryRequest;

    @BeforeEach
    void setUp() {
        transactionQueryRequest = new TransactionQueryRequest();
    }

    @Test
    void testGettersAndSetters() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String transactionType = "DEBIT";
        Long accountNumber = 12345L;

        transactionQueryRequest.setStartDate(startDate);
        transactionQueryRequest.setEndDate(endDate);
        transactionQueryRequest.setTransactionType(transactionType);
        transactionQueryRequest.setAccountNumber(accountNumber);

        assertEquals(startDate, transactionQueryRequest.getStartDate());
        assertEquals(endDate, transactionQueryRequest.getEndDate());
        assertEquals(transactionType, transactionQueryRequest.getTransactionType());
        assertEquals(accountNumber, transactionQueryRequest.getAccountNumber());
    }

    @Test
    void testNoArgsConstructor() {
        TransactionQueryRequest transactionQueryRequest = new TransactionQueryRequest();

        // Ensure default constructor works and fields are null as expected
        assertNull(transactionQueryRequest.getStartDate());
        assertNull(transactionQueryRequest.getEndDate());
        assertNull(transactionQueryRequest.getTransactionType());
        assertNull(transactionQueryRequest.getAccountNumber());
    }
}
