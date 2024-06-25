package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerFeedback;
import com.example.model.CustomerGrievance;
import com.example.model.CustomerScheduleCall;
import com.example.repository.CustomerFeedbackRepository;
import com.example.repository.CustomerGrievanceRepository;
import com.example.repository.CustomerProfileRepository;
import com.example.repository.CustomerScheduleCallRepository;

@Service
public class CustomerAssistanceService {

	private final CustomerFeedbackRepository customerFeedbackRepository;
	private final CustomerProfileRepository customerProfileRepository;
	private final CustomerGrievanceRepository customerGrievanceRepository;

	private final CustomerScheduleCallRepository customerScheduleCallRepository;

	public CustomerAssistanceService(CustomerFeedbackRepository customerFeedbackRepository,
			CustomerGrievanceRepository customerGrievanceRepository,
			CustomerScheduleCallRepository customerScheduleCallRepository,
			CustomerProfileRepository customerProfileRepository) {
		this.customerFeedbackRepository = customerFeedbackRepository;
		this.customerGrievanceRepository = customerGrievanceRepository;
		this.customerScheduleCallRepository = customerScheduleCallRepository;
		this.customerProfileRepository = customerProfileRepository;
	}

	public CustomerFeedback createFeedback(CustomerFeedback feedback) {
		if (feedback == null) {
			throw new IllegalArgumentException("Feedback cannot be null");
		}
		return customerFeedbackRepository.save(feedback);
	}

	public List<CustomerGrievance> getAllGrievances(Long accountNumber) throws ResourceNotFoundException {
		List<CustomerGrievance> grievances = customerGrievanceRepository.findAllByAccountNumber(accountNumber);
		if (grievances.isEmpty()) {
			throw new ResourceNotFoundException("No grievances found for account number: " + accountNumber);
		}
		return grievances;
	}

	public CustomerGrievance createGrievance(CustomerGrievance grievance) {
		if (grievance == null) {
			throw new IllegalArgumentException("Grievance cannot be null");
		}

		String mail = customerProfileRepository.findEmail(grievance.getCustomerCardAccount().getAccountNumber())
				.getEmail();
		sendEmail(mail, "Grievance Submitted Successfully",
				"Hi, your grievance is Submitted with Id: " + grievance.getGrievanceId());
		return customerGrievanceRepository.save(grievance);
	}

	public CustomerScheduleCall addScheduleCall(CustomerScheduleCall scheduleCall) {
		if (scheduleCall == null) {
			throw new IllegalArgumentException("Schedule call data cannot be null");
		}

		String mail = customerProfileRepository.findEmail(scheduleCall.getCustomerCardAccount().getAccountNumber())
				.getEmail();
		sendEmail(mail, "ScheduleCall Submitted Successfully",
				"Hi, your ScheduleCall is Submitted with Id: " + scheduleCall.getScheduleCallId());
		return customerScheduleCallRepository.save(scheduleCall);
	}

	@Autowired
	private JavaMailSender mailSender;

	private void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

}
