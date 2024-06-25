package com.example.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.model.CustomerCardAccount;
import com.example.model.CustomerCardAccount.ActivationStatus;
import com.example.repository.CustomerCardAccountRepository;

public class CustomerCardAccountSchedulerTest {

    @Mock
    private CustomerCardAccountRepository customerCardAccountRepository;

    @InjectMocks
    private CustomerCardAccountScheduler customerCardAccountScheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateDueAmounts() {
        CustomerCardAccount account1 = new CustomerCardAccount();
        account1.setCardBalance(new BigDecimal("100.00"));
        
        CustomerCardAccount account2 = new CustomerCardAccount();
        account2.setCardBalance(new BigDecimal("200.00"));

        List<CustomerCardAccount> accounts = Arrays.asList(account1, account2);

        when(customerCardAccountRepository.findAll()).thenReturn(accounts);

        customerCardAccountScheduler.updateDueAmounts();

        LocalDate dueDate = LocalDate.now().plusDays(10);
        Date expectedDueDate = Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        verify(customerCardAccountRepository, times(2)).save(any(CustomerCardAccount.class));
        assertEquals(new BigDecimal("100.00"), account1.getDueAmount());
        assertEquals(expectedDueDate, account1.getDueDate());
        assertEquals(new BigDecimal("200.00"), account2.getDueAmount());
        assertEquals(expectedDueDate, account2.getDueDate());
    }

    @Test
    public void testDeactivateOverdueAccounts() {
        CustomerCardAccount account1 = new CustomerCardAccount();
        account1.setDueAmount(new BigDecimal("100.00"));
        
        CustomerCardAccount account2 = new CustomerCardAccount();
        account2.setDueAmount(BigDecimal.ZERO);

        List<CustomerCardAccount> accounts = Arrays.asList(account1, account2);

        when(customerCardAccountRepository.findAll()).thenReturn(accounts);

        customerCardAccountScheduler.deactivateOverdueAccounts();

        verify(customerCardAccountRepository, times(1)).save(account1);
        verify(customerCardAccountRepository, times(0)).save(account2);
        assertEquals(ActivationStatus.INACTIVE, account1.getActivationStatus());
    }
}
