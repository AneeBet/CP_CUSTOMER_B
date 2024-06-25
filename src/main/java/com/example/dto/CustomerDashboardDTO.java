package com.example.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.example.model.CreditCard;
import com.example.model.Transaction;

public class CustomerDashboardDTO {

	private String name;
	  private Date expiryDate;
	  private String creditCard;
	  private BigDecimal maxlimit;
	  public BigDecimal getMaxlimit() {
		return maxlimit;
	}
	public void setMaxlimit(BigDecimal maxlimit) {
		this.maxlimit = maxlimit;
	}
	private Long cardNumber;
	  private BigDecimal cardBalance;
	  private BigDecimal dueAmount;
	  private Date dueDate;
	  private List<Transaction> transactionList;
	  
	  
	  public CustomerDashboardDTO() {
		super();
	}
	public String getName() {
		return name;
	}
	public CustomerDashboardDTO(String name, Date expiryDate, String creditCard, Long cardNumber,
			BigDecimal cardBalance, BigDecimal dueAmount, Date dueDate, List<Transaction> transactionList) {
		super();
		this.name = name;
		this.expiryDate = expiryDate;
		this.creditCard = creditCard;
		this.cardNumber = cardNumber;
		this.cardBalance = cardBalance;
		this.dueAmount = dueAmount;
		this.dueDate = dueDate;
		this.transactionList = transactionList;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(String cardType) {
		this.creditCard = cardType;
	}
	public Long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public BigDecimal getCardBalance() {
		return cardBalance;
	}
	public void setCardBalance(BigDecimal cardBalance) {
		this.cardBalance = cardBalance;
	}
	public BigDecimal getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public List<Transaction> getTransactionList() {
		return transactionList;
	}
	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}
	
}
