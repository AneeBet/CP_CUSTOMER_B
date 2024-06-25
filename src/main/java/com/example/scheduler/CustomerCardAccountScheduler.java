package com.example.scheduler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.CustomerCardAccount;
import com.example.repository.CustomerCardAccountRepository;

@Component
public class CustomerCardAccountScheduler {

    @Autowired
    private CustomerCardAccountRepository customerCardAccountRepository;

    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the 1st of every month
    @Transactional
    public void updateDueAmounts() {
        List<CustomerCardAccount> accounts = customerCardAccountRepository.findAll();

        LocalDate currentDate = LocalDate.now();
        LocalDate dueDate = currentDate.plusDays(10);

        for (CustomerCardAccount account : accounts) {
            account.setDueAmount(account.getCardBalance());
            account.setDueDate(Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            customerCardAccountRepository.save(account);
        }
    }

    @Scheduled(cron = "0 0 0 10 * ?") // Runs at midnight on the 10th of every month
    @Transactional
    public void deactivateOverdueAccounts() {
        List<CustomerCardAccount> accounts = customerCardAccountRepository.findAll();

        for (CustomerCardAccount account : accounts) {
            if (account.getDueAmount().compareTo(BigDecimal.ZERO) > 0) {
                account.setActivationStatus(CustomerCardAccount.ActivationStatus.INACTIVE);
                customerCardAccountRepository.save(account);
            }
        }
    }
}

