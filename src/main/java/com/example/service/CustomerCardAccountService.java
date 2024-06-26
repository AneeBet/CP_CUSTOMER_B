package com.example.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.CustomerDashboardDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CreditCard;
import com.example.model.CustomerCardAccount;
import com.example.model.Transaction;
import com.example.repository.CardRepository;
import com.example.repository.CustomerCardAccountRepository;
import com.example.repository.CustomerProfileRepository;
import com.example.repository.TransactionRepository;

@Service
public class CustomerCardAccountService {

    private static final String ACCOUNT_NOT_FOUND = "Account not found";
    private static final String CARD_NOT_FOUND = "Card not found";
    private static final String ONLINE_PAYMENT = "onlinepayment";
    private static final String CARD_SWIPE = "cardswipe";
    private static final String INTERNATIONAL_PAYMENT = "internationalpayment";
    private static final String INVALID_PAYMENT_TYPE = "Invalid payment type";

    private static final Logger logger = LoggerFactory.getLogger(CustomerCardAccountService.class);

    private final CustomerCardAccountRepository customerCardAccountRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public CustomerCardAccountService(CustomerCardAccountRepository customerCardAccountRepository,
                                      CardRepository cardRepository, TransactionRepository transactionRepository,
                                      CustomerProfileRepository customerProfileRepository) {
        this.customerCardAccountRepository = customerCardAccountRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.customerProfileRepository = customerProfileRepository;
    }

    public List<Long> getAllActiveCustomerAccountsbyCustomerId(Long id) throws ResourceNotFoundException {
        logger.info("Fetching all active customer accounts by customer ID: {}", id);
        List<Long> accounts = customerCardAccountRepository.findAllActiveCustomerId(id);
        if (accounts.isEmpty()) {
            logger.warn("No accounts found for customer ID: {}", id);
            throw new ResourceNotFoundException("No accounts found for customer id: " + id);
        }
        return accounts;
    }

    public List<CustomerCardAccount> getAllCustomerAccountsbyCustomerId(Long id) throws ResourceNotFoundException {
        logger.info("Fetching all customer accounts by customer ID: {}", id);
        List<CustomerCardAccount> accounts = customerCardAccountRepository.findAllByCustomerId(id);
        if (accounts.isEmpty()) {
            logger.warn("No accounts found for customer ID: {}", id);
            throw new ResourceNotFoundException("No accounts found for customer id: " + id);
        }
        return accounts;
    }

    public CustomerCardAccount createCustomerAccount(CustomerCardAccount customerCardAccount) {
        if (customerCardAccount == null) {
            logger.error("Customer card account data cannot be null");
            throw new IllegalArgumentException("Customer card account data cannot be null");
        }
        logger.info("Creating new customer card account");
        return customerCardAccountRepository.save(customerCardAccount);
    }

    public List<Transaction> getFilteredTransactions(LocalDate startDate, LocalDate endDate, String transactionType,
                                                     Long accountNumber) {
        logger.info("Fetching filtered transactions for account number: {}, from: {}, to: {}, type: {}",
                accountNumber, startDate, endDate, transactionType);
        if (transactionType == null || (!transactionType.equals("Debit") && !transactionType.equals("Credit")
                && !transactionType.equals("All"))) {
            logger.error("Invalid transaction type provided: {}", transactionType);
            throw new IllegalArgumentException("Invalid transaction type provided: " + transactionType);
        } else if (transactionType.contains("Debit") || transactionType.contains("Credit"))
            return transactionRepository.findFilteredTransaction(startDate, endDate, transactionType, accountNumber);
        else
            return transactionRepository.findTransaction(startDate, endDate, accountNumber);
    }

    @Transactional
    public CustomerDashboardDTO dashboard(Long accountNumber) {
        logger.info("Generating dashboard for account number: {}", accountNumber);

        CustomerDashboardDTO result = new CustomerDashboardDTO();
        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });

        String name = customerProfileRepository.getName(account.getCustomerProfile().getCustomerId());
        String cardType = account.getCreditCard();
        Optional<CreditCard> card = cardRepository.findByCardType(cardType);
        BigDecimal maxLimit = card.get().getMaxLimit();
        List<Transaction> transactionList = transactionRepository.getFiveTransaction(accountNumber);
        Date expiryDate = account.getExpiryDate();
        Long cardNumber = account.getCardNumber();
        BigDecimal cardBalance = account.getCardBalance();
        BigDecimal dueAmount = account.getDueAmount();
        Date dueDate = account.getDueDate();

        result.setCardBalance(cardBalance);
        result.setCardNumber(cardNumber);
        result.setCreditCard(cardType);
        result.setDueAmount(dueAmount);
        result.setDueDate(dueDate);
        result.setExpiryDate(expiryDate);
        result.setName(name);
        result.setTransactionList(transactionList);
        result.setMaxlimit(maxLimit);

        return result;
    }

    @Transactional
    public void updatePin(Long accountNumber, int oldPin, int newPin) throws BadRequestException {
        logger.info("Updating PIN for account number: {}", accountNumber);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new BadRequestException(ACCOUNT_NOT_FOUND);
                });

        if (account.getPin() != oldPin) {
            logger.warn("Incorrect old PIN provided for account number: {}", accountNumber);
            throw new BadRequestException("PIN is wrong");
        }

        if (oldPin == newPin) {
            logger.warn("New PIN cannot be the same as old PIN for account number: {}", accountNumber);
            throw new BadRequestException("New PIN cannot be equal to the old PIN");
        }

        account.setPin(newPin);
        customerCardAccountRepository.save(account);
        logger.info("PIN updated successfully for account number: {}", accountNumber);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDueDateAndAmount(Long accountNumber) {
        logger.info("Fetching due date and amount for account number: {}", accountNumber);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });

        Map<String, Object> result = new HashMap<>();
        result.put("dueDate", account.getDueDate());
        result.put("dueAmount", account.getDueAmount());

        return result;
    }

    @Transactional
    public void updateDueAmount(Long accountNumber, BigDecimal newDueAmount) {
        logger.info("Updating due amount for account number: {}", accountNumber);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });

        account.setDueAmount(newDueAmount);
        customerCardAccountRepository.save(account);
        logger.info("Due amount updated successfully for account number: {}", accountNumber);
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getPaymentLimit(Long accountNumber, String paymentType) {
        logger.info("Fetching payment limit for account number: {}, payment type: {}", accountNumber, paymentType);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });

        Map<String, BigDecimal> result = new HashMap<>();
        switch (paymentType.toLowerCase()) {
            case ONLINE_PAYMENT:
                result.put("onlinePaymentLimit", account.getOnlinePaymentLimit());
                break;
            case CARD_SWIPE:
                result.put("cardSwipeLimit", account.getCardSwipeLimit());
                break;
            case INTERNATIONAL_PAYMENT:
                result.put("internationalPaymentLimit", account.getInternationalPaymentLimit());
                break;
            default:
                logger.error(INVALID_PAYMENT_TYPE);
                throw new IllegalArgumentException(INVALID_PAYMENT_TYPE);
        }
        return result;
    }

    @Transactional
    public void updateTransactionLimit(Long accountNumber, String paymentType, BigDecimal newLimit)
            throws BadRequestException {
        logger.info("Updating transaction limit for account number: {}, payment type: {}", accountNumber, paymentType);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new BadRequestException(ACCOUNT_NOT_FOUND);
                });

        switch (paymentType.toLowerCase()) {
            case ONLINE_PAYMENT:
                account.setOnlinePaymentLimit(newLimit);
                break;
            case CARD_SWIPE:
                account.setCardSwipeLimit(newLimit);
                break;
            case INTERNATIONAL_PAYMENT:
                account.setInternationalPaymentLimit(newLimit);
                break;
            default:
                logger.error(INVALID_PAYMENT_TYPE);
                throw new IllegalArgumentException(INVALID_PAYMENT_TYPE);
        }

        customerCardAccountRepository.save(account);
        logger.info("Transaction limit updated successfully for account number: {}, payment type: {}",
                accountNumber, paymentType);
    }

    @Transactional
    public void updateCardType(Long accountNumber, String cardType) {
        logger.info("Updating card type for account number: {}", accountNumber);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });

        CreditCard creditCard = cardRepository.findByCardType(cardType)
                .orElseThrow(() -> {
                    logger.error(CARD_NOT_FOUND);
                    return new RuntimeException("Card type not found");
                });

        account.setCreditCard(creditCard.getCardType());
        customerCardAccountRepository.save(account);
        logger.info("Card type updated successfully for account number: {}", accountNumber);
    }

    @Transactional
    public void updateStatusToInactive(Long accountNumber) {
        logger.info("Updating status to inactive for account number: {}", accountNumber);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });

        account.setActivationStatus(CustomerCardAccount.ActivationStatus.INACTIVE);
        customerCardAccountRepository.save(account);
        logger.info("Status updated to inactive for account number: {}", accountNumber);
    }

    @Transactional(readOnly = true)
    public Map<String, String> getPaymentStatuses(Long accountNumber) {
        logger.info("Fetching payment statuses for account number: {}", accountNumber);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });

        Map<String, String> result = new HashMap<>();
        result.put("internationalPayment", account.getInternationalPayment().name());
        result.put("cardSwipe", account.getCardSwipe().name());
        result.put("onlinePayment", account.getOnlinePayment().name());

        return result;
    }

    @Transactional
    public void updatePaymentStatus(Long accountNumber, String paymentType, CustomerCardAccount.PaymentStatus status)
            throws BadRequestException {
        logger.info("Updating payment status for account number: {}, payment type: {}", accountNumber, paymentType);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });

        switch (paymentType.toLowerCase()) {
            case ONLINE_PAYMENT:
                account.setOnlinePayment(status);
                break;
            case CARD_SWIPE:
                account.setCardSwipe(status);
                break;
            case INTERNATIONAL_PAYMENT:
                account.setInternationalPayment(status);
                break;
            default:
                logger.error(INVALID_PAYMENT_TYPE);
                throw new BadRequestException(INVALID_PAYMENT_TYPE);
        }

        customerCardAccountRepository.save(account);
        logger.info("Payment status updated successfully for account number: {}, payment type: {}", accountNumber, paymentType);
    }

    public BigDecimal getCreditLimit(Long accountNumber) {
        logger.info("Fetching credit limit for account number: {}", accountNumber);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });
        CreditCard card = cardRepository.findByCardType(account.getCreditCard())
                .orElseThrow(() -> {
                    logger.error(CARD_NOT_FOUND);
                    return new RuntimeException(CARD_NOT_FOUND);
                });
        return card.getMaxLimit();
    }

    public void payDue(Long accountNumber) {
        logger.info("Paying due amount for account number: {}", accountNumber);

        CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
                .orElseThrow(() -> {
                    logger.error(ACCOUNT_NOT_FOUND);
                    return new RuntimeException(ACCOUNT_NOT_FOUND);
                });
        account.setCardBalance(account.getCardBalance().subtract(account.getDueAmount()));
        account.setDueAmount(BigDecimal.ZERO);
        customerCardAccountRepository.save(account);
        logger.info("Due amount paid successfully for account number: {}", accountNumber);
    }

    private void updateAccountDetails(CustomerCardAccount existingAccount, CustomerCardAccount details) {
        existingAccount.setCustomerProfile(details.getCustomerProfile());
        existingAccount.setBaseCurrency(details.getBaseCurrency());
        existingAccount.setOpeningDate(details.getOpeningDate());
        existingAccount.setActivationStatus(details.getActivationStatus());
        existingAccount.setCardStatus(details.getCardStatus());
        existingAccount.setInternationalPayment(details.getInternationalPayment());
        existingAccount.setOnlinePayment(details.getOnlinePayment());
        existingAccount.setCardBalance(details.getCardBalance());
        existingAccount.setDueAmount(details.getDueAmount());
        existingAccount.setExpiryDate(details.getExpiryDate());
        existingAccount.setCvv(details.getCvv());
        existingAccount.setDueDate(details.getDueDate());
        existingAccount.setCreditCard(details.getCreditCard());
    }

    public Transaction createTransaction(Transaction transaction) {
        if (transaction == null) {
            logger.error("Transaction data cannot be null");
            throw new IllegalArgumentException("Transaction data cannot be null");
        }
        logger.info("Creating new transaction");
        return transactionRepository.save(transaction);
    }

    public Optional<CustomerCardAccount> updateCustomerAccount(Long id, CustomerCardAccount customerAccountDetails)
            throws ResourceNotFoundException {
        logger.info("Updating customer account for ID: {}", id);

        CustomerCardAccount existingAccount = customerCardAccountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer card account not found for ID: {}", id);
                    return new ResourceNotFoundException("Customer card account not found for id: " + id);
                });

        updateAccountDetails(existingAccount, customerAccountDetails);
        CustomerCardAccount updatedAccount = customerCardAccountRepository.save(existingAccount);
        logger.info("Customer account updated successfully for ID: {}", id);
        return Optional.of(updatedAccount);
    }
}
