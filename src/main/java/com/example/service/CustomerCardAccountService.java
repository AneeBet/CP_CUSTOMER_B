package com.example.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

//	Get all active accounts
	public List<Long> getAllActiveCustomerAccountsbyCustomerId(Long id) throws ResourceNotFoundException {
		List<Long> accounts = customerCardAccountRepository.findAllActiveCustomerId(id);
		if (accounts.isEmpty()) {
			throw new ResourceNotFoundException("No accounts found for customer id: " + id);
		}
		return accounts;
	}

	public List<CustomerCardAccount> getAllCustomerAccountsbyCustomerId(Long id) throws ResourceNotFoundException {
		List<CustomerCardAccount> accounts = customerCardAccountRepository.findAllByCustomerId(id);
		if (accounts.isEmpty()) {
			throw new ResourceNotFoundException("No accounts found for customer id: " + id);
		}
		return accounts;
	}

	public CustomerCardAccount createCustomerAccount(CustomerCardAccount customerCardAccount) {
		if (customerCardAccount == null) {
			throw new IllegalArgumentException("Customer card account data cannot be null");
		}
		return customerCardAccountRepository.save(customerCardAccount);
	}

	public Optional<CustomerCardAccount> updateCustomerAccount(Long id, CustomerCardAccount customerAccountDetails)
			throws ResourceNotFoundException {
		// Retrieve the existing account
		CustomerCardAccount existingAccount = customerCardAccountRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer card account not found for id: " + id));

		// Update the retrieved account with new details
		updateAccountDetails(existingAccount, customerAccountDetails);

		// Save the updated account
		CustomerCardAccount updatedAccount = customerCardAccountRepository.save(existingAccount);

		// Return the updated account wrapped in an Optional
		return Optional.of(updatedAccount);
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
			throw new IllegalArgumentException("Transaction data cannot be null");
		}
		return transactionRepository.save(transaction);
	}

	public List<Transaction> getFilteredTransactions(LocalDate startDate, LocalDate endDate, String transactionType,
			Long accountNumber) {
		if (transactionType == null || (!transactionType.equals("Debit") && !transactionType.equals("Credit")
				&& !transactionType.equals("All"))) {
			throw new IllegalArgumentException("Invalid transaction type provided: " + transactionType);
		} else if (transactionType.contains("Debit") || transactionType.contains("Credit"))
			return transactionRepository.findFilteredTransaction(startDate, endDate, transactionType, accountNumber);
		else
			return transactionRepository.findTransaction(startDate, endDate, accountNumber);
	}

	// dashboard
	public CustomerDashboardDTO dashboard(Long accountNumber) {

		CustomerDashboardDTO result = new CustomerDashboardDTO();
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));

		String name = customerProfileRepository.getName(account.getCustomerProfile().getCustomerId());
		String cardType = account.getCreditCard();
		System.out.println();
		Optional<CreditCard> card = cardRepository.findByCardType(cardType);
		BigDecimal maxLimit = card.get().getMaxLimit();
		List<Transaction> transactionList = transactionRepository.getFiveTransaction(accountNumber);
		Date expiryDate = account.getExpiryDate();
		Long cardNumber = account.getCardNumber();
		BigDecimal cardBalance = account.getCardBalance();
		BigDecimal dueAmount = account.getDueAmount();
		Date dueDate = account.getDueDate();
		
        System.out.println(expiryDate + " "+cardNumber+" "+cardBalance+" "+dueAmount+" ");
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

	// Mansi's functions

	// Change
	// PIN----------------------------------------------------------------------------------------------------
	@Transactional
	public void updatePin(Long accountNumber, int oldPin, int newPin) throws BadRequestException {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new BadRequestException(ACCOUNT_NOT_FOUND));

		if (account.getPin() != oldPin) {
			throw new BadRequestException("PIN is wrong");
		}

		if (oldPin == newPin) {
			throw new BadRequestException("New PIN cannot be equal to the old PIN");
		}

		account.setPin(newPin);
		customerCardAccountRepository.save(account);
	}
	// Change
	// PIN--------------------------------------------------------------------------------------------------------------

	// due date and due
	// amount-------------------------------------------------------------------
	@Transactional(readOnly = true)
	public Map<String, Object> getDueDateAndAmount(Long accountNumber) {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));

		Map<String, Object> result = new HashMap<>();
		result.put("dueDate", account.getDueDate());
		result.put("dueAmount", account.getDueAmount());

		return result;
	}

	// due date and due amount
	// ---------------------------------------------------------------------------------
	// change due
	// amt-------------------------------------------------------------------------
	@Transactional
	public void updateDueAmount(Long accountNumber, BigDecimal newDueAmount) {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));

		account.setDueAmount(newDueAmount);
		customerCardAccountRepository.save(account);
	}

	// change due
	// amt------------------------------------------------------------------------------------
	// update transaction
	// limit-------------------------------------------------------------------------------
	@Transactional(readOnly = true)
	public Map<String, BigDecimal> getPaymentLimit(Long accountNumber, String paymentType) {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));

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
			throw new IllegalArgumentException(INVALID_PAYMENT_TYPE);
		}
		return result;
	}

	@Transactional
	public void updateTransactionLimit(Long accountNumber, String paymentType, BigDecimal newLimit)
			throws BadRequestException {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new BadRequestException(ACCOUNT_NOT_FOUND));

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
			throw new IllegalArgumentException(INVALID_PAYMENT_TYPE);
		}

		customerCardAccountRepository.save(account);
	}

	// upgrade card
	// type--------------------------------------------------------------------------------------------------------
	@Transactional
	public void updateCardType(Long accountNumber, String cardType) {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));

		CreditCard creditCard = cardRepository.findByCardType(cardType)
				.orElseThrow(() -> new RuntimeException("Card type not found"));

		account.setCreditCard(creditCard.getCardType());
		customerCardAccountRepository.save(account);
	}

	// block
	// account-----------------------------------------------------------------------------------------
	@Transactional
	public void updateStatusToInactive(Long accountNumber) {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));

		account.setActivationStatus(CustomerCardAccount.ActivationStatus.INACTIVE);
		customerCardAccountRepository.save(account);
	}
	// block
	// account-----------------------------------------------------------------------------------------

	// show payment type status
	@Transactional(readOnly = true)
	public Map<String, String> getPaymentStatuses(Long accountNumber) {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));

		Map<String, String> result = new HashMap<>();
		result.put("internationalPayment", account.getInternationalPayment().name());
		result.put("cardSwipe", account.getCardSwipe().name());
		result.put("onlinePayment", account.getOnlinePayment().name());

		return result;
	}

	// ------------------------------------------------------------------------------------------------------------
	// disable
	// payments----------------------------------------------------------------
	@Transactional
	public void updatePaymentStatus(Long accountNumber, String paymentType, CustomerCardAccount.PaymentStatus status)
			throws BadRequestException {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));

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
			throw new BadRequestException(INVALID_PAYMENT_TYPE);
		}

		customerCardAccountRepository.save(account);
	}

	// disable
	// payments----------------------------------------------------------------
	// Credit Limit
	public BigDecimal getCreditLimit(Long accountNumber) {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));
		CreditCard card = cardRepository.findByCardType(account.getCreditCard())
				.orElseThrow(() -> new RuntimeException(CARD_NOT_FOUND));
		BigDecimal limit = card.getMaxLimit();
		return limit;
	}

	// payDue
	public void payDue(Long accountNumber) {
		CustomerCardAccount account = customerCardAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));
		account.setCardBalance(account.getCardBalance().subtract(account.getDueAmount()));
		account.setDueAmount(BigDecimal.ZERO);

		customerCardAccountRepository.save(account);

	}

}
