package com.example.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customerTransaction")
public class Transaction {

	@Id
	@Column(name = "transactionId")
	@NotNull(message = "Transaction ID cannot be null")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
	@TableGenerator(name = "id_generator", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_value", pkColumnValue = "task_gen", initialValue = 10000000, allocationSize = 1)
	private Long transactionId;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "accountNumber", referencedColumnName = "accountNumber")
	private CustomerCardAccount customerCardAccount;

	@Column(name = "amount", precision = 10, scale = 4)
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
	private BigDecimal amount;

	@Column(name = "description", length = 100)
	@Size(max = 100, message = "Description must be less than or equal to 100 characters")
	private String description;

	@Column(name = "transactionType")
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	@Column(name = "timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	public enum TransactionType {
		DEBIT, CREDIT
	}

	public Transaction(Long transactionId, CustomerCardAccount customerCardAccount, BigDecimal amount,
			String description, TransactionType transactionType, Date timestamp) {
		super();
		this.transactionId = transactionId;
		this.customerCardAccount = customerCardAccount;
		this.amount = amount;
		this.description = description;
		this.transactionType = transactionType;
		this.timestamp = timestamp;
	}

	public Transaction() {
		super();
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public CustomerCardAccount getCustomerCardAccount() {
		return customerCardAccount;
	}

	public void setCustomerCardAccount(CustomerCardAccount customerCardAccount) {
		this.customerCardAccount = customerCardAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
