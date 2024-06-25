package com.example.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "creditCard")
public class CreditCard {

	@Id
	@Column(name = "cardType")
	@NotNull(message = "Card type cannot be null")
	@Size(min = 1, max = 25, message = "Card type must be between 1 and 25 characters")
	private String cardType;

	@Column(name = "maxLimit", precision = 10, scale = 4)
	@DecimalMin(value = "0.0", inclusive = false, message = "Max limit must be greater than 0")
	private BigDecimal maxLimit;

	@Column(name = "interest")
	@DecimalMin(value = "0.0", inclusive = false, message = "Interest must be greater than 0")
	private BigDecimal interest;

	@Column(name = "annualFee", precision = 10, scale = 4)
	@DecimalMin(value = "0.0", inclusive = false, message = "Annual fee must be greater than 0")
	private BigDecimal annualFee;

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public CreditCard() {
		super();
	}

	public CreditCard(String cardType, BigDecimal maxLimit, BigDecimal interest, BigDecimal annualFee) {
		super();
		this.cardType = cardType;
		this.maxLimit = maxLimit;
		this.interest = interest;
		this.annualFee = annualFee;
	}

	public BigDecimal getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(BigDecimal maxLimit) {
		this.maxLimit = maxLimit;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getAnnualFee() {
		return annualFee;
	}

	public void setAnnualFee(BigDecimal annualFee) {
		this.annualFee = annualFee;
	}

}
