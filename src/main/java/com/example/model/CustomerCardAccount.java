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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



@Entity
@Table(name = "customerCardAccount")
public class CustomerCardAccount {

    @Id
    @Column(name = "accountNumber")
    @NotNull(message = "Account number cannot be null")
    @Min(value = 1, message = "Account number must be positive")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    @TableGenerator(name = "id_generator", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_value", pkColumnValue = "task_gen", initialValue = 1000000000, allocationSize = 1)
    private Long accountNumber;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "customerId", referencedColumnName = "customerId")
    private CustomerProfile customerProfile;

    @Column(name = "baseCurrency")
    @Size(min = 3, max = 3, message = "Base currency must be a 3-letter currency code")
    private String baseCurrency;

    @Column(name = "openingDate")
    @Temporal(TemporalType.DATE)
    @PastOrPresent(message = "Opening date must be in the past or present")
    private Date openingDate;

    @Column(name = "activationStatus")
    @Enumerated(EnumType.STRING)
    private ActivationStatus activationStatus;

    @Column(name = "cardNumber", unique = true)
    @Min(value = 1, message = "Card number must be positive")
    private Long cardNumber;

    @Column(name = "cardStatus")
    @Enumerated(EnumType.STRING)
    private ActivationStatus cardStatus;

    @Column(name = "internationalPayment")
    @Enumerated(EnumType.STRING)
    private PaymentStatus internationalPayment;

    @Column(name = "onlinePayment")
    @Enumerated(EnumType.STRING)
    private PaymentStatus onlinePayment;

    @Column(name = "pin")
    @Min(value = 1000, message = "PIN must be a 4-digit number")
    @Max(value = 9999, message = "PIN must be a 4-digit number")
    private int pin;

    @Column(name = "cardSwipe")
    @Enumerated(EnumType.STRING)
    private PaymentStatus cardSwipe;

    @Column(name = "onlinePaymentLimit", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", inclusive = true, message = "Online payment limit must be greater than or equal to 0")
    private BigDecimal onlinePaymentLimit;

    @Column(name = "cardSwipeLimit", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", inclusive = true, message = "Card swipe limit must be greater than or equal to 0")
    private BigDecimal cardSwipeLimit;

    @Column(name = "internationalPaymentLimit", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", inclusive = true, message = "International payment limit must be greater than or equal to 0")
    private BigDecimal internationalPaymentLimit;

    

    @Column(name = "cardBalance", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", inclusive = true, message = "Card balance must be greater than or equal to 0")
    private BigDecimal cardBalance;

    @Column(name = "dueAmount", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", inclusive = true, message = "Due amount must be greater than or equal to 0")
    private BigDecimal dueAmount;

    @Column(name = "dueDate")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "expiryDate")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @Column(name = "cvv")
    @Pattern(regexp = "\\d{3}", message = "CVV must be a 3 digit number")
    private String cvv;

    @Column(name = "creditCardLimit", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit card limit must be greater than or equal to 0")
    private BigDecimal creditCardLimit;

  
    @Column(name = "creditCard")
    private String creditCard;

    
    public enum ActivationStatus {
        ACTIVE, INACTIVE
    }

    public enum PaymentStatus {
        ENABLE, DISABLE
    }

    // Default constructor
    public CustomerCardAccount() {
        super();
    }

    
    // Parameterized constructor
    public CustomerCardAccount(Long accountNumber, CustomerProfile customerProfile, String baseCurrency,
            Date openingDate, ActivationStatus activationStatus, Long cardNumber, ActivationStatus cardStatus,
            PaymentStatus internationalPayment, PaymentStatus onlinePayment, int pin, PaymentStatus cardSwipe,
            BigDecimal onlinePaymentLimit, BigDecimal cardSwipeLimit, BigDecimal internationalPaymentLimit,
            BigDecimal cardBalance, BigDecimal dueAmount, BigDecimal creditCardLimit,
            Date dueDate, Date expiryDate, String cvv, String creditCard) {
        super();
        this.accountNumber = accountNumber;
        this.customerProfile = customerProfile;
        this.baseCurrency = baseCurrency;
        this.openingDate = openingDate;
        this.activationStatus = activationStatus;
        this.cardNumber = cardNumber;
        this.cardStatus = cardStatus;
        this.internationalPayment = internationalPayment;
        this.onlinePayment = onlinePayment;
        this.pin = pin;
        this.cardSwipe = cardSwipe;
        this.onlinePaymentLimit = onlinePaymentLimit;
        this.cardSwipeLimit = cardSwipeLimit;
        this.internationalPaymentLimit = internationalPaymentLimit;
       
        this.cardBalance = cardBalance;
        this.dueAmount = dueAmount;
        this.creditCardLimit = creditCardLimit;
        this.dueDate = dueDate;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.creditCard = creditCard;
    }

    // Getters and setters

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public CustomerProfile getCustomerProfile() {
        return customerProfile;
    }

    public void setCustomerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public ActivationStatus getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(ActivationStatus activationStatus) {
        this.activationStatus = activationStatus;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public ActivationStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(ActivationStatus cardStatus) {
        this.cardStatus = cardStatus;
    }

    public PaymentStatus getInternationalPayment() {
        return internationalPayment;
    }

    public void setInternationalPayment(PaymentStatus internationalPayment) {
        this.internationalPayment = internationalPayment;
    }

    public PaymentStatus getOnlinePayment() {
        return onlinePayment;
    }

    public void setOnlinePayment(PaymentStatus onlinePayment) {
        this.onlinePayment = onlinePayment;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public PaymentStatus getCardSwipe() {
        return cardSwipe;
    }

    public void setCardSwipe(PaymentStatus cardSwipe) {
        this.cardSwipe = cardSwipe;
    }

    public BigDecimal getOnlinePaymentLimit() {
        return onlinePaymentLimit;
    }

    public void setOnlinePaymentLimit(BigDecimal onlinePaymentLimit) {
        this.onlinePaymentLimit = onlinePaymentLimit;
    }

    public BigDecimal getCardSwipeLimit() {
        return cardSwipeLimit;
    }

    public void setCardSwipeLimit(BigDecimal cardSwipeLimit) {
        this.cardSwipeLimit = cardSwipeLimit;
    }

    public BigDecimal getInternationalPaymentLimit() {
        return internationalPaymentLimit;
    }

    public void setInternationalPaymentLimit(BigDecimal internationalPaymentLimit) {
        this.internationalPaymentLimit = internationalPaymentLimit;
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

    public BigDecimal getCreditCardLimit() {
        return creditCardLimit;
    }

    public void setCreditCardLimit(BigDecimal creditCardLimit) {
        this.creditCardLimit = creditCardLimit;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }
}
