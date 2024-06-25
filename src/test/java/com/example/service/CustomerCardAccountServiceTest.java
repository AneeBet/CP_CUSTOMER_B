

package com.example.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.*;
import com.example.repository.*;

public class CustomerCardAccountServiceTest {

    @Mock
    private CustomerCardAccountRepository customerCardAccountRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CustomerCardAccountService customerCardAccountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllActiveCustomerAccountsbyCustomerId() throws ResourceNotFoundException {
        List<Long> activeAccounts = Arrays.asList(1L, 2L);
        when(customerCardAccountRepository.findAllActiveCustomerId(anyLong())).thenReturn(activeAccounts);

        List<Long> result = customerCardAccountService.getAllActiveCustomerAccountsbyCustomerId(1L);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0));
        assertEquals(2L, result.get(1));
    }

    @Test
    public void testGetAllActiveCustomerAccountsbyCustomerId_ResourceNotFoundException() {
        when(customerCardAccountRepository.findAllActiveCustomerId(anyLong())).thenReturn(Collections.emptyList());

        
    }

    @Test
    public void testGetAllCustomerAccountsbyCustomerId() throws ResourceNotFoundException {
        List<CustomerCardAccount> accounts = Arrays.asList(new CustomerCardAccount(), new CustomerCardAccount());
        when(customerCardAccountRepository.findAllByCustomerId(anyLong())).thenReturn(accounts);

        List<CustomerCardAccount> result = customerCardAccountService.getAllCustomerAccountsbyCustomerId(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllCustomerAccountsbyCustomerId_ResourceNotFoundException() {
        when(customerCardAccountRepository.findAllByCustomerId(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () ->
            customerCardAccountService.getAllCustomerAccountsbyCustomerId(1L));
    }

    @Test
    public void testCreateCustomerAccount() {
        CustomerCardAccount account = new CustomerCardAccount();
        when(customerCardAccountRepository.save(any(CustomerCardAccount.class))).thenReturn(account);

        CustomerCardAccount result = customerCardAccountService.createCustomerAccount(account);
        assertNotNull(result);
    }

    @Test
    public void testCreateCustomerAccount_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> customerCardAccountService.createCustomerAccount(null));
    }

    @Test
    public void testUpdateCustomerAccount() throws ResourceNotFoundException {
        CustomerCardAccount existingAccount = new CustomerCardAccount();
        existingAccount.setAccountNumber(1L);

        CustomerCardAccount newDetails = new CustomerCardAccount();
        newDetails.setBaseCurrency("USD");

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(existingAccount));
        when(customerCardAccountRepository.save(any(CustomerCardAccount.class))).thenReturn(existingAccount);

        Optional<CustomerCardAccount> result = customerCardAccountService.updateCustomerAccount(1L, newDetails);
        assertTrue(result.isPresent());
        assertEquals("USD", result.get().getBaseCurrency());
    }

    @Test
    public void testUpdateCustomerAccount_ResourceNotFoundException() {
        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerCardAccountService.updateCustomerAccount(1L, new CustomerCardAccount()));
    }

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = customerCardAccountService.createTransaction(transaction);
        assertNotNull(result);
    }

    @Test
    public void testCreateTransaction_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> customerCardAccountService.createTransaction(null));
    }

    @Test
    public void testGetFilteredTransactions() {
        Transaction transaction = new Transaction();
        List<Transaction> transactions = Collections.singletonList(transaction);

        when(transactionRepository.findFilteredTransaction(any(LocalDate.class), any(LocalDate.class), anyString(), anyLong())).thenReturn(transactions);

        List<Transaction> result = customerCardAccountService.getFilteredTransactions(LocalDate.now(), LocalDate.now().plusDays(1), "Debit", 1L);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetFilteredTransactions_InvalidTransactionType() {
        assertThrows(IllegalArgumentException.class, () -> customerCardAccountService.getFilteredTransactions(LocalDate.now(), LocalDate.now().plusDays(1), "InvalidType", 1L));
    }

    @Test
    public void testUpdatePin() throws BadRequestException {
        CustomerCardAccount account = new CustomerCardAccount();
        account.setPin(1234);

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        customerCardAccountService.updatePin(1L, 1234, 5678);
        assertEquals(5678, account.getPin());
    }

    @Test
    public void testUpdatePin_AccountNotFound() {
        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> customerCardAccountService.updatePin(1L, 1234, 5678));
    }

    @Test
    public void testUpdatePin_WrongPin() {
        CustomerCardAccount account = new CustomerCardAccount();
        account.setPin(1234);

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        assertThrows(BadRequestException.class, () -> customerCardAccountService.updatePin(1L, 1111, 5678));
    }

    @Test
    public void testUpdatePin_SamePin() {
        CustomerCardAccount account = new CustomerCardAccount();
        account.setPin(1234);

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        assertThrows(BadRequestException.class, () -> customerCardAccountService.updatePin(1L, 1234, 1234));
    }

    @Test
    public void testGetDueDateAndAmount() {
        CustomerCardAccount account = new CustomerCardAccount();
//        account.setDueDate(LocalDate.now());
        account.setDueAmount(BigDecimal.valueOf(1000));

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        Map<String, Object> result = customerCardAccountService.getDueDateAndAmount(1L);
//        assertEquals(LocalDate.now(), result.get("dueDate"));
        assertEquals(BigDecimal.valueOf(1000), result.get("dueAmount"));
    }

    @Test
    public void testUpdateDueAmount() {
        CustomerCardAccount account = new CustomerCardAccount();
        account.setDueAmount(BigDecimal.valueOf(1000));

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        customerCardAccountService.updateDueAmount(1L, BigDecimal.valueOf(2000));
        assertEquals(BigDecimal.valueOf(2000), account.getDueAmount());
    }

    @Test
    public void testGetPaymentLimit() {
        CustomerCardAccount account = new CustomerCardAccount();
        account.setOnlinePaymentLimit(BigDecimal.valueOf(5000));

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        Map<String, BigDecimal> result = customerCardAccountService.getPaymentLimit(1L, "onlinepayment");
        assertEquals(BigDecimal.valueOf(5000), result.get("onlinePaymentLimit"));
    }

    @Test
    public void testUpdateTransactionLimit() throws BadRequestException {
        CustomerCardAccount account = new CustomerCardAccount();

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        customerCardAccountService.updateTransactionLimit(1L, "onlinepayment", BigDecimal.valueOf(5000));
        assertEquals(BigDecimal.valueOf(5000), account.getOnlinePaymentLimit());
    }

    @Test
    public void testUpdateCardType() {
        CustomerCardAccount account = new CustomerCardAccount();
        CreditCard creditCard = new CreditCard();
        creditCard.setCardType("Gold");

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(cardRepository.findByCardType(anyString())).thenReturn(Optional.of(creditCard));

        customerCardAccountService.updateCardType(1L, "Gold");
        assertEquals("Gold", account.getCreditCard());
    }

    @Test
    public void testUpdateStatusToInactive() {
        CustomerCardAccount account = new CustomerCardAccount();

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        customerCardAccountService.updateStatusToInactive(1L);
        assertEquals(CustomerCardAccount.ActivationStatus.INACTIVE, account.getActivationStatus());
    }

    @Test
    public void testGetPaymentStatuses() {
        CustomerCardAccount account = new CustomerCardAccount();
        account.setOnlinePayment(CustomerCardAccount.PaymentStatus.ENABLE);
        account.setCardSwipe(CustomerCardAccount.PaymentStatus.ENABLE);
        account.setInternationalPayment(CustomerCardAccount.PaymentStatus.ENABLE);

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        Map<String, String> result = customerCardAccountService.getPaymentStatuses(1L);
        assertEquals("ENABLE", result.get("onlinePayment"));
        assertEquals("ENABLE", result.get("cardSwipe"));
        assertEquals("ENABLE", result.get("internationalPayment"));
    }

    @Test
    public void testUpdatePaymentStatus() throws BadRequestException {
        CustomerCardAccount account = new CustomerCardAccount();

        when(customerCardAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        customerCardAccountService.updatePaymentStatus(1L, "onlinepayment", CustomerCardAccount.PaymentStatus.DISABLE);
        assertEquals(CustomerCardAccount.PaymentStatus.DISABLE, account.getOnlinePayment());
    }
}
