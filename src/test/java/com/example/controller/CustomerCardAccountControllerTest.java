package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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

import com.example.dto.CustomerDashboardDTO;
import com.example.dto.TransactionQueryRequest;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerCardAccount;
import com.example.model.Transaction;
import com.example.service.CustomerCardAccountService;

public class CustomerCardAccountControllerTest {

    @Mock
    private CustomerCardAccountService customerCardAccountService;

    @InjectMocks
    private CustomerCardAccountController customerCardAccountController;

    private CustomerCardAccount customerCardAccount;
    private Transaction transaction;
    private TransactionQueryRequest transactionQueryRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerCardAccount = new CustomerCardAccount();
        customerCardAccount.setAccountNumber(1L);

        transaction = new Transaction();
        transaction.setCustomerCardAccount(customerCardAccount);

        transactionQueryRequest = new TransactionQueryRequest();
    }

    @Test
    void testGetAllCustomerAccounts_Success() throws ResourceNotFoundException {
        List<CustomerCardAccount> accounts = List.of(customerCardAccount);
        when(customerCardAccountService.getAllCustomerAccountsbyCustomerId(1L)).thenReturn(accounts);

        List<CustomerCardAccount> response = customerCardAccountController.getAllCustomerAccounts(1L);

        assertEquals(accounts, response);
        verify(customerCardAccountService, times(1)).getAllCustomerAccountsbyCustomerId(1L);
    }

    @Test
    void testGetAllCustomerAccounts_NotFound() throws ResourceNotFoundException {
        when(customerCardAccountService.getAllCustomerAccountsbyCustomerId(1L)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> customerCardAccountController.getAllCustomerAccounts(1L));
    }

    @Test
    void testGetAllTransactions_Success() throws BadRequestException {
        List<Transaction> transactions = List.of(transaction);
        when(customerCardAccountService.getFilteredTransactions(any(), any(), any(), anyLong())).thenReturn(transactions);

        transactionQueryRequest.setAccountNumber(1L);
        List<Transaction> response = customerCardAccountController.getAllTransactions(transactionQueryRequest);

        assertEquals(transactions, response);
        verify(customerCardAccountService, times(1)).getFilteredTransactions(any(), any(), any(), anyLong());
    }

    @Test
    void testGetAllTransactions_MissingAccountNumber() {
        assertThrows(BadRequestException.class, () -> customerCardAccountController.getAllTransactions(transactionQueryRequest));
    }

    @Test
    void testDashboard_Success() {
        CustomerDashboardDTO dashboardDTO = new CustomerDashboardDTO();
        when(customerCardAccountService.dashboard(1L)).thenReturn(dashboardDTO);

        ResponseEntity<CustomerDashboardDTO> response = customerCardAccountController.dashboard(Map.of("accountNumber", "1"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dashboardDTO, response.getBody());
        verify(customerCardAccountService, times(1)).dashboard(1L);
    }

    @Test
    void testGetAllActiveCustomerAccounts_Success() throws ResourceNotFoundException {
        List<Long> activeAccounts = List.of(1L);
        when(customerCardAccountService.getAllActiveCustomerAccountsbyCustomerId(1L)).thenReturn(activeAccounts);

        ResponseEntity<List<Long>> response = customerCardAccountController.getAllActiveCustomerAccounts(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeAccounts, response.getBody());
        verify(customerCardAccountService, times(1)).getAllActiveCustomerAccountsbyCustomerId(1L);
    }

    @Test
    void testGetAllActiveCustomerAccounts_NotFound() throws ResourceNotFoundException {
        when(customerCardAccountService.getAllActiveCustomerAccountsbyCustomerId(1L)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> customerCardAccountController.getAllActiveCustomerAccounts(1L));
    }

    @Test
    void testUpdateStatusToInactive_Success() {
        doNothing().when(customerCardAccountService).updateStatusToInactive(1L);

        ResponseEntity<String> response = customerCardAccountController.updateStatusToInactive(Map.of("accountNumber", "1"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated to INACTIVE successfully", response.getBody());
        verify(customerCardAccountService, times(1)).updateStatusToInactive(1L);
    }

    @Test
    void testGetPaymentStatuses_Success() {
        Map<String, String> paymentStatuses = Map.of("onlinePayment", "ENABLED");
        when(customerCardAccountService.getPaymentStatuses(1L)).thenReturn(paymentStatuses);

        ResponseEntity<Map<String, String>> response = customerCardAccountController.getPaymentStatuses(Map.of("accountNumber", "1"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentStatuses, response.getBody());
        verify(customerCardAccountService, times(1)).getPaymentStatuses(1L);
    }

    @Test
    void testGetDueDateAndAmount_Success() {
        Map<String, Object> dueInfo = Map.of("dueDate", "2024-12-31", "dueAmount", new BigDecimal("1000"));
        when(customerCardAccountService.getDueDateAndAmount(1L)).thenReturn(dueInfo);

        ResponseEntity<Map<String, Object>> response = customerCardAccountController.getDueDateAndAmount(Map.of("accountNumber", "1"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dueInfo, response.getBody());
        verify(customerCardAccountService, times(1)).getDueDateAndAmount(1L);
    }

    @Test
    void testUpdateDueAmount_Success() {
        doNothing().when(customerCardAccountService).updateDueAmount(1L, new BigDecimal("1000"));

        ResponseEntity<String> response = customerCardAccountController.updateDueAmount(Map.of(
                "accountNumber", "1",
                "newDueAmount", "1000"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Due amount updated successfully", response.getBody());
        verify(customerCardAccountService, times(1)).updateDueAmount(1L, new BigDecimal("1000"));
    }

    @Test
    void testGetPaymentLimit_Success() {
        Map<String, BigDecimal> paymentLimit = Map.of("ONLINE", new BigDecimal("5000"));
        when(customerCardAccountService.getPaymentLimit(1L, "ONLINE")).thenReturn(paymentLimit);

        ResponseEntity<Map<String, BigDecimal>> response = customerCardAccountController.getPaymentLimit(Map.of(
                "accountNumber", "1",
                "paymentType", "ONLINE"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentLimit, response.getBody());
        verify(customerCardAccountService, times(1)).getPaymentLimit(1L, "ONLINE");
    }

    @Test
    void testUpdateTransactionLimit_Success() throws BadRequestException {
        doNothing().when(customerCardAccountService).updateTransactionLimit(1L, "ONLINE", new BigDecimal("10000"));

        ResponseEntity<String> response = customerCardAccountController.updateTransactionLimit(Map.of(
                "accountNumber", "1",
                "paymentType", "ONLINE",
                "newLimit", "10000"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction limit updated successfully", response.getBody());
        verify(customerCardAccountService, times(1)).updateTransactionLimit(1L, "ONLINE", new BigDecimal("10000"));
    }

    @Test
    void testUpdateCardType_Success() {
        doNothing().when(customerCardAccountService).updateCardType(1L, "VISA");

        ResponseEntity<String> response = customerCardAccountController.updateCardType(Map.of(
                "accountNumber", "1",
                "cardType", "VISA"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Card type updated successfully", response.getBody());
        verify(customerCardAccountService, times(1)).updateCardType(1L, "VISA");
    }

    @Test
    void testUpdatePin_Success() throws BadRequestException {
        doNothing().when(customerCardAccountService).updatePin(1L, 1234, 5678);

        ResponseEntity<String> response = customerCardAccountController.updatePin(Map.of(
                "accountNumber", "1",
                "oldPin", "1234",
                "newPin", "5678"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PIN updated successfully", response.getBody());
        verify(customerCardAccountService, times(1)).updatePin(1L, 1234, 5678);
    }

    @Test
    void testCreateTransaction_Success() throws BadRequestException {
        when(customerCardAccountService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        ResponseEntity<Transaction> response = customerCardAccountController.createTransaction(transaction);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(customerCardAccountService, times(1)).createTransaction(transaction);
    }

    @Test
    void testCreateTransaction_NullTransaction() {
        assertThrows(BadRequestException.class, () -> customerCardAccountController.createTransaction(null));
    }

    @Test
    void testCreateTransaction_MissingAccountNumber() {
        transaction.setCustomerCardAccount(new CustomerCardAccount());
        assertThrows(BadRequestException.class, () -> customerCardAccountController.createTransaction(transaction));
    }

    @Test
    void testGetCreditLimit_Success() {
        BigDecimal creditLimit = new BigDecimal("50000");
        when(customerCardAccountService.getCreditLimit(1L)).thenReturn(creditLimit);

        ResponseEntity<BigDecimal> response = customerCardAccountController.getCreditLimit(Map.of("accountNumber", "1"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(creditLimit, response.getBody());
        verify(customerCardAccountService, times(1)).getCreditLimit(1L);
    }

    @Test
    void testPayDue_Success() {
        doNothing().when(customerCardAccountService).payDue(1L);

        ResponseEntity<String> response = customerCardAccountController.payDue(Map.of("accountNumber", "1"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Payment Successful", response.getBody());
        verify(customerCardAccountService, times(1)).payDue(1L);
    }
}
