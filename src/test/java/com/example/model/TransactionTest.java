package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class TransactionTest {

    @Test
    public void testTransactionConstructorAndGetters() {
        Long transactionId = 1L;
        CustomerCardAccount customerCardAccount = new CustomerCardAccount();
        BigDecimal amount = new BigDecimal("100.00");
        String description = "Test transaction";
        Transaction.TransactionType transactionType = Transaction.TransactionType.DEBIT;
        Date timestamp = new Date();

        Transaction transaction = new Transaction(transactionId, customerCardAccount, amount, description, transactionType, timestamp);

        assertEquals(transactionId, transaction.getTransactionId());
        assertEquals(customerCardAccount, transaction.getCustomerCardAccount());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(transactionType, transaction.getTransactionType());
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    public void testTransactionSetters() {
        Transaction transaction = new Transaction();
        Long transactionId = 1L;
        CustomerCardAccount customerCardAccount = new CustomerCardAccount();
        BigDecimal amount = new BigDecimal("100.00");
        String description = "Test transaction";
        Transaction.TransactionType transactionType = Transaction.TransactionType.DEBIT;
        Date timestamp = new Date();

        transaction.setTransactionId(transactionId);
        transaction.setCustomerCardAccount(customerCardAccount);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionType(transactionType);
        transaction.setTimestamp(timestamp);

        assertEquals(transactionId, transaction.getTransactionId());
        assertEquals(customerCardAccount, transaction.getCustomerCardAccount());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(transactionType, transaction.getTransactionType());
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    public void testTransactionNoArgsConstructor() {
        Transaction transaction = new Transaction();
        assertNotNull(transaction);
    }
}
