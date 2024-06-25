package com.example.model;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "customerCardApplication")
public class CustomerCardApplication {

	@Id
	 @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    @TableGenerator(name = "id_generator", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_value", pkColumnValue = "task_gen", initialValue = 1000000000, allocationSize = 1)
	@Column(name = "applicationId")
	private Long applicationId;

	@ManyToOne
	@JoinColumn(name = "customerId", referencedColumnName = "customerId")
	@NotNull(message = "Customer profile cannot be null")
	private CustomerProfile customerProfile;

	@Column(name = "aadhaarNumber")
	@NotNull(message = "Aadhaar number cannot be null")
	@Pattern(regexp = "\\d{12}", message = "Aadhaar number must be a 12-digit number")
	private Long aadhaarNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "applicationStatus")
	@NotNull(message = "Application status cannot be null")
	private ApplicationStatus status;

	
	@NotNull(message = "Credit card cannot be null")
	private String creditCard;
	
	
	@Column(name = "accountNumber")
	private Long accountNumber;

	@Column(name = "isUpgrade", columnDefinition = "boolean default false")
	private Boolean isUpgrade = false;

	@ManyToOne
	@JoinColumn(name = "assignedAdmin", referencedColumnName = "username")
	private Admin admin;

	public Boolean getIsUpgrade() {
		return isUpgrade;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public void setIsUpgrade(Boolean isUpgrade) {
		this.isUpgrade = isUpgrade;
	}

	public enum ApplicationStatus {
		APPROVED, REJECTED, PENDING
	}

	
	public CustomerCardApplication(Long applicationId,
			@NotNull(message = "Customer profile cannot be null") CustomerProfile customerProfile,
			@NotNull(message = "Aadhaar number cannot be null") @Pattern(regexp = "\\d{12}", message = "Aadhaar number must be a 12-digit number") Long aadhaarNumber,
			@NotNull(message = "Application status cannot be null") ApplicationStatus status,
			@NotNull(message = "Credit card cannot be null") @NotNull(message = "Credit card cannot be null") String creditCard, Long accountNumber,
			Boolean isUpgrade, Admin admin) {
		super();
		this.applicationId = applicationId;
		this.customerProfile = customerProfile;
		this.aadhaarNumber = aadhaarNumber;
		this.status = status;
		this.creditCard = creditCard;
		this.accountNumber = accountNumber;
		this.isUpgrade = isUpgrade;
		this.admin = admin;
	}
	
	

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public CustomerCardApplication() {
		super();
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public CustomerProfile getCustomerProfile() {
		return customerProfile;
	}

	public void setCustomerProfile(CustomerProfile customerProfile) {
		this.customerProfile = customerProfile;
	}

	public Long getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(Long aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}
}
