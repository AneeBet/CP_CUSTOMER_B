package com.example.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customerProfile")
public class CustomerProfile {
	@Id
	@Column(name = "customerId")
	 @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    @TableGenerator(name = "id_generator", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_value", pkColumnValue = "task_gen", initialValue = 1000000000, allocationSize = 1)
	@NotNull(message = "Customer ID cannot be null")
	private Long customerId;

	@Column(name = "name", length = 100)
	@Size(max = 100, message = "Name must be less than or equal to 100 characters")
	private String name;

	@Column(name = "aadhaarNumber")
	@Pattern(regexp = "\\d{12}", message = "Aadhaar number must be a 12-digit number")
	private Long aadhaarNumber;

	@Column(name = "email")
	@Email(message = "Email should be valid")
	private String email;

	@Column(name = "password")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	@Column(name = "address", length = 100)
	@Size(max = 100, message = "Address must be less than or equal to 100 characters")
	private String address;

	@Column(name = "panId")
	@Pattern(regexp = "[A-Z]{5}\\d{4}[A-Z]", message = "PAN ID must be a valid format")
	private String panId;

	@Column(name = "mobileNumber", length = 10)
	@Pattern(regexp = "\\d{10}", message = "Mobile number must be a 10-digit number")
	private String mobileNumber;

	@Column(name = "dob")
	@Temporal(TemporalType.DATE)
	@Past(message = "Date of birth must be in the past")
	private Date dob;

	@Column(name = "firstLogin", columnDefinition = "boolean default true")
	private Boolean firstLogin = true;

	@Column(name = "employmentYears")
	private Integer employmentYears;


	@Column(name = "companyName", length = 100)
	@Size(max = 100, message = "Company name must be less than or equal to 100 characters")
	private String companyName;

	@Column(name = "annualIncome")
	@DecimalMin(value = "0.0", inclusive = true, message = "Annual income must be greater than or equal to 0")
	private BigDecimal annualIncome;

	@Column(name = "incomeProofFilePath", length = 255)
	@Size(max = 255, message = "Income proof file path must be less than or equal to 255 characters")
	private String incomeProofFilePath;

	@OneToMany(mappedBy = "customerProfile")
	private List<CustomerCardAccount> customerCardAccounts;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(Long aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPanId() {
		return panId;
	}

	public void setPanId(String panId) {
		this.panId = panId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Boolean getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Integer getEmploymentYears() {
		return employmentYears;
	}

	public void setEmploymentYears(Integer employmentYears) {
		this.employmentYears = employmentYears;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public BigDecimal getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(BigDecimal annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getIncomeProofFilePath() {
		return incomeProofFilePath;
	}

	public void setIncomeProofFilePath(String incomeProofFilePath) {
		this.incomeProofFilePath = incomeProofFilePath;
	}

	public List<CustomerCardAccount> getCustomerCardAccounts() {
		return customerCardAccounts;
	}

	public void setCustomerCardAccounts(List<CustomerCardAccount> customerCardAccounts) {
		this.customerCardAccounts = customerCardAccounts;
	}

	public CustomerProfile(@NotNull(message = "Customer ID cannot be null") Long customerId,
			@Size(max = 100, message = "Name must be less than or equal to 100 characters") String name,
			@Pattern(regexp = "\\d{12}", message = "Aadhaar number must be a 12-digit number") Long aadhaarNumber,
			@Email(message = "Email should be valid") String email,
			@Size(min = 6, message = "Password must be at least 6 characters") String password,
			@Size(max = 100, message = "Address must be less than or equal to 100 characters") String address,
			@Pattern(regexp = "[A-Z]{5}\\d{4}[A-Z]", message = "PAN ID must be a valid format") String panId,
			@Pattern(regexp = "\\d{10}", message = "Mobile number must be a 10-digit number") String mobileNumber,
			@Past(message = "Date of birth must be in the past") Date dob, Boolean firstLogin, Integer employmentYears,
			@Size(max = 100, message = "Company name must be less than or equal to 100 characters") String companyName,
			@DecimalMin(value = "0.0", inclusive = true, message = "Annual income must be greater than or equal to 0") BigDecimal annualIncome,
			@Size(max = 255, message = "Income proof file path must be less than or equal to 255 characters") String incomeProofFilePath,
			List<CustomerCardAccount> customerCardAccounts) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.aadhaarNumber = aadhaarNumber;
		this.email = email;
		this.password = password;
		this.address = address;
		this.panId = panId;
		this.mobileNumber = mobileNumber;
		this.dob = dob;
		this.firstLogin = firstLogin;
		this.employmentYears = employmentYears;
		this.companyName = companyName;
		this.annualIncome = annualIncome;
		this.incomeProofFilePath = incomeProofFilePath;
		this.customerCardAccounts = customerCardAccounts;
	}

	public CustomerProfile() {
		super();
	}

	

}
