package com.example.model;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customerScheduleCall")
public class CustomerScheduleCall {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
	@TableGenerator(name = "id_generator", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_value", pkColumnValue = "task_gen", initialValue = 1000000000, allocationSize = 1)
	@Column(name = "scheduleCallId")
	@NotNull(message = "Schedule Call ID cannot be null")
	private Long scheduleCallId;

	@ManyToOne
	@JoinColumn(name = "accountNumber", referencedColumnName = "accountNumber")
	private CustomerCardAccount customerCardAccount;

	@Column(name = "subject", length = 100, nullable = false)
	@Size(max = 100, message = "Subject must be less than or equal to 100 characters")
	private String subject;

	@Column(name = "description", length = 1000, nullable = false)	
	@Size(max = 1000, message = "Description must be less than or equal to 1000 characters")
	private String description;

	@Column(name = "timeSlot")
	private String timeSlot;

	@Column(name = "resolutionStatus")
	@Enumerated(EnumType.STRING)
	private ResolutionStatus status;

	public Long getScheduleCallId() {
		return scheduleCallId;
	}

	public void setScheduleCallId(Long scheduleCallId) {
		this.scheduleCallId = scheduleCallId;
	}

	public CustomerCardAccount getCustomerCardAccount() {
		return customerCardAccount;
	}

	public void setCustomerCardAccount(CustomerCardAccount customerCardAccount) {
		this.customerCardAccount = customerCardAccount;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public ResolutionStatus getStatus() {
		return status;
	}

	public void setStatus(ResolutionStatus status) {
		this.status = status;
	}

	public CustomerScheduleCall(Long scheduleCallId, CustomerCardAccount customerCardAccount, String subject,
			String description, String timeSlot, ResolutionStatus status) {
		super();
		this.scheduleCallId = scheduleCallId;
		this.customerCardAccount = customerCardAccount;
		this.subject = subject;
		this.description = description;
		this.timeSlot = timeSlot;
		this.status = status;
	}

	public CustomerScheduleCall() {
		super();
	}

}