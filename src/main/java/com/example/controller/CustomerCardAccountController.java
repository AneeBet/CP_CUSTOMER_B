package com.example.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CustomerDashboardDTO;
import com.example.dto.TransactionQueryRequest;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerCardAccount;
import com.example.model.Transaction;
import com.example.service.CustomerCardAccountService;
import com.fasterxml.jackson.annotation.JsonBackReference;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer")
public class CustomerCardAccountController {

	private static final String ACCOUNT_NUMBER = "accountNumber";
	private static final String PAYMENT_TYPE = "paymentType";

	private CustomerCardAccountService customerCardAccountService;

	public CustomerCardAccountController(CustomerCardAccountService customerCardAccountService) {
		this.customerCardAccountService = customerCardAccountService;
	} 

//-----------	
	@GetMapping("/readall/{id}")
	public List<CustomerCardAccount> getAllCustomerAccounts(@PathVariable("id") Long id)
			throws ResourceNotFoundException {
		List<CustomerCardAccount> accounts = customerCardAccountService.getAllCustomerAccountsbyCustomerId(id);
		if (accounts.isEmpty()) {
			throw new ResourceNotFoundException("No customer accounts found for this customer id: " + id);
		}
		return accounts;
	}

	@PostMapping("/transaction/readall")
	public List<Transaction> getAllTransactions(@RequestBody TransactionQueryRequest queryRequest)
			throws BadRequestException {
		if (queryRequest.getAccountNumber() == null) {
			throw new BadRequestException("Account number must be provided to fetch transactions");
		}
		return customerCardAccountService.getFilteredTransactions(queryRequest.getStartDate(),
				queryRequest.getEndDate(), queryRequest.getTransactionType(), queryRequest.getAccountNumber());
	}

	// Dashboard Function
	@PostMapping("/dashboard")
	public ResponseEntity<CustomerDashboardDTO> dashboard(@RequestBody Map<String, String> requestBody) {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());

		CustomerDashboardDTO result = customerCardAccountService.dashboard(accountNumber);
		return ResponseEntity.ok(result);
	}

	// Mansi's functions

	// get all active accounts
	// ---------------------------------------------------------------
	@GetMapping("/readallactive/{id}")
	public ResponseEntity<List<Long>> getAllActiveCustomerAccounts(@PathVariable("id") Long id)
			throws ResourceNotFoundException {
		List<Long> accounts = customerCardAccountService.getAllActiveCustomerAccountsbyCustomerId(id);
		if (accounts.isEmpty()) {
			throw new ResourceNotFoundException("No customer accounts found for this customer id: " + id);
		}
		return ResponseEntity.ok(accounts);
	}

	// block
	// account-----------------------------------------------------------------------------------------------------
	@PutMapping("/update-status")
	public ResponseEntity<String> updateStatusToInactive(@RequestBody Map<String, Object> requestBody) {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());
		customerCardAccountService.updateStatusToInactive(accountNumber);
		return ResponseEntity.ok("Status updated to INACTIVE successfully");
	}
	// block
	// account-----------------------------------------------------------------------------------------------------

	// show payment status
	// ----------------------------------------------------------------------------
	@PostMapping("/get-payment-statuses")
	public ResponseEntity<Map<String, String>> getPaymentStatuses(@RequestBody Map<String, Object> requestBody) {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());
		Map<String, String> result = customerCardAccountService.getPaymentStatuses(accountNumber);
		return ResponseEntity.ok(result);
	}

	// ----------------------------------------------------------------------------------
	// disable
	// payments---------------------------------------------------------------------------
	@PutMapping("/update-payment-status")
	public ResponseEntity<String> updatePaymentStatus(@RequestBody Map<String, Object> requestBody)
			throws BadRequestException {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());
		String paymentType = requestBody.get(PAYMENT_TYPE).toString();
		CustomerCardAccount.PaymentStatus status = CustomerCardAccount.PaymentStatus
				.valueOf(requestBody.get("status").toString().toUpperCase());

		customerCardAccountService.updatePaymentStatus(accountNumber, paymentType, status);
		return ResponseEntity.ok("Payment status updated successfully");
	}

	// disable
	// payments------------------------------------------------------------------------------------
	// due date and due
	// amt---------------------------------------------------------------------------------------
	@PostMapping("/get-due-info")
	public ResponseEntity<Map<String, Object>> getDueDateAndAmount(@RequestBody Map<String, Object> requestBody) {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());
		Map<String, Object> result = customerCardAccountService.getDueDateAndAmount(accountNumber);
		return ResponseEntity.ok(result);
	}
	// due date and due
	// amt-------------------------------------------------------------------------------------

	// change due
	// amt---------------------------------------------------------------------------------------------------
	@PutMapping("/update-due-amount")
	public ResponseEntity<String> updateDueAmount(@RequestBody Map<String, Object> requestBody) {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());
		BigDecimal newDueAmount = new BigDecimal(requestBody.get("newDueAmount").toString());
		customerCardAccountService.updateDueAmount(accountNumber, newDueAmount);
		return ResponseEntity.ok("Due amount updated successfully");
	}
	// change due
	// amt---------------------------------------------------------------------------------------------------------

	// transaction limit
	@PostMapping("/get-transaction-limit")
	public ResponseEntity<Map<String, BigDecimal>> getPaymentLimit(@RequestBody Map<String, Object> requestBody) {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());
		String paymentType = requestBody.get(PAYMENT_TYPE).toString();
		Map<String, BigDecimal> result = customerCardAccountService.getPaymentLimit(accountNumber, paymentType);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/update-transaction-limit")
	public ResponseEntity<String> updateTransactionLimit(@RequestBody Map<String, Object> requestBody)
			throws BadRequestException {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());
		String paymentType = requestBody.get(PAYMENT_TYPE).toString();
		BigDecimal newLimit = new BigDecimal(requestBody.get("newLimit").toString());

		customerCardAccountService.updateTransactionLimit(accountNumber, paymentType, newLimit);
		return ResponseEntity.ok("Transaction limit updated successfully");
	}

	// update transaction
	// limit--------------------------------------------------------------------------------------------------------
	// update card
	// type-------------------------------------------------------------------------------------
	@PutMapping("/update-card-type")
	public ResponseEntity<String> updateCardType(@RequestBody Map<String, Object> requestBody) {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());
		String cardType = requestBody.get("cardType").toString();
		customerCardAccountService.updateCardType(accountNumber, cardType);
		return ResponseEntity.ok("Card type updated successfully");
	}

	// update card
	// type-----------------------------------------------------------------------------------
	// Change
	// PIN----------------------------------------------------------------------------------------------------------------------
	@PutMapping("/update-pin")
	public ResponseEntity<String> updatePin(@RequestBody Map<String, Object> requestBody) throws BadRequestException {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());
		int oldPin = Integer.parseInt(requestBody.get("oldPin").toString());
		int newPin = Integer.parseInt(requestBody.get("newPin").toString());

		customerCardAccountService.updatePin(accountNumber, oldPin, newPin);
		return ResponseEntity.ok("PIN updated successfully");
	}

	// Change
	// PIN--------------------------------------------------------------------------------------------------------------

	@PostMapping("/transaction/add")
	public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction)
			throws BadRequestException {
		if (transaction == null || transaction.getCustomerCardAccount().getAccountNumber() == null) {
			throw new BadRequestException("Transaction details are incomplete or missing account number");
		}
		Transaction createdTransaction = customerCardAccountService.createTransaction(transaction);
		return ResponseEntity.ok(createdTransaction);
	}

	// Get card-limit
	// transaction limit
	@PostMapping("/get-credit-limit")
	public ResponseEntity<BigDecimal> getCreditLimit(@RequestBody Map<String, String> requestBody) {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());

		BigDecimal result = customerCardAccountService.getCreditLimit(accountNumber);
		return ResponseEntity.ok(result);
	}

	// Due Amount Payment
	@PutMapping("/pay-due")
	public ResponseEntity<String> payDue(@RequestBody Map<String, String> requestBody) {
		Long accountNumber = Long.valueOf(requestBody.get(ACCOUNT_NUMBER).toString());

		customerCardAccountService.payDue(accountNumber);
		return ResponseEntity.ok("Payment Successful");
	}

}
