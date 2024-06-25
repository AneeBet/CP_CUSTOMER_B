package com.example.dto;
 
 
 
import com.example.model.CreditCard;
import com.example.model.CustomerCardApplication.ApplicationStatus;
 
 
 
public class CustomerCardApplicationRequest {
    private Long customerId;
    private Long accountNumber;
    private String cardType;
	private Boolean isUpgrade;
    private ApplicationStatus status;
    
    public Long getAccountNumber() {
		return accountNumber;
	}
 
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
 
	public Long getCustomerId() {
		return customerId;
	}
 
	public String getCardType() {
		return cardType;
	}
 
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
 
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
 
	public Boolean getIsUpgrade() {
		return isUpgrade;
	}
	public void setIsUpgrade(Boolean isUpgrade) {
		this.isUpgrade = isUpgrade;
	}
	public ApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	public CustomerCardApplicationRequest(Long customerId, String cardType, Boolean isUpgrade,
			ApplicationStatus status) {
		super();
		this.customerId = customerId;
		this.cardType = cardType;
		this.isUpgrade = isUpgrade;
		this.status = status;
	}
	public CustomerCardApplicationRequest() {
		super();
	}
    
    
}