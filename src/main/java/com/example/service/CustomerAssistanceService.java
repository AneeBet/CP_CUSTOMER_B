//package com.example.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import com.example.exception.ResourceNotFoundException;
//import com.example.model.CustomerFeedback;
//import com.example.model.CustomerGrievance;
//import com.example.model.CustomerScheduleCall;
//import com.example.repository.CustomerFeedbackRepository;
//import com.example.repository.CustomerGrievanceRepository;
//import com.example.repository.CustomerProfileRepository;
//import com.example.repository.CustomerScheduleCallRepository;
//
//@Service
//public class CustomerAssistanceService {
//
//	private final CustomerFeedbackRepository customerFeedbackRepository;
//	private final CustomerProfileRepository customerProfileRepository;
//	private final CustomerGrievanceRepository customerGrievanceRepository;
//
//	private final CustomerScheduleCallRepository customerScheduleCallRepository;
//
//	public CustomerAssistanceService(CustomerFeedbackRepository customerFeedbackRepository,
//			CustomerGrievanceRepository customerGrievanceRepository,
//			CustomerScheduleCallRepository customerScheduleCallRepository,
//			CustomerProfileRepository customerProfileRepository) {
//		this.customerFeedbackRepository = customerFeedbackRepository;
//		this.customerGrievanceRepository = customerGrievanceRepository;
//		this.customerScheduleCallRepository = customerScheduleCallRepository;
//		this.customerProfileRepository = customerProfileRepository;
//	}
//
//	public CustomerFeedback createFeedback(CustomerFeedback feedback) {
//		if (feedback == null) {
//			throw new IllegalArgumentException("Feedback cannot be null");
//		}
//		return customerFeedbackRepository.save(feedback);
//	}
//
//	public List<CustomerGrievance> getAllGrievances(Long accountNumber) throws ResourceNotFoundException {
//		List<CustomerGrievance> grievances = customerGrievanceRepository.findAllByAccountNumber(accountNumber);
//		if (grievances.isEmpty()) {
//			throw new ResourceNotFoundException("No grievances found for account number: " + accountNumber);
//		}
//		return grievances;
//	}
//
//	public CustomerGrievance createGrievance(CustomerGrievance grievance) {
//		if (grievance == null) {
//			throw new IllegalArgumentException("Grievance cannot be null");
//		}
// 
//		String mail = customerProfileRepository.findEmail(grievance.getCustomerCardAccount().getAccountNumber())
//				.getEmail();
//		sendEmail(mail, "Grievance Submitted Successfully",
//				"Hi, your grievance is Submitted. Our Customer Support will try to resolve it as soon as possible");
//		return customerGrievanceRepository.save(grievance);
//	}
//
//	public CustomerScheduleCall addScheduleCall(CustomerScheduleCall scheduleCall) {
//		if (scheduleCall == null) {
//			throw new IllegalArgumentException("Schedule call data cannot be null");
//		}
//
//		String mail = customerProfileRepository.findEmail(scheduleCall.getCustomerCardAccount().getAccountNumber())
//				.getEmail();
//		sendEmail(mail, "ScheduleCall Submitted Successfully",
//				"Hi, your ScheduleCall is Submitted.  Our Customer Support will try to resolve it as soon as possible");
//		return customerScheduleCallRepository.save(scheduleCall);
//	}
//
//	@Autowired
//	private JavaMailSender mailSender;
//
//	private void sendEmail(String to, String subject, String text) {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(to);
//		message.setSubject(subject);
//		message.setText(text);
//		mailSender.send(message);
//	}
//
//}


package com.example.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CustomerAssistanceService.class);

    private final CustomerFeedbackRepository customerFeedbackRepository;
    private final CustomerGrievanceRepository customerGrievanceRepository;
    private final CustomerScheduleCallRepository customerScheduleCallRepository;
    private final CustomerProfileRepository customerProfileRepository;

    @Autowired
    private JavaMailSender mailSender;

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
        logger.info("Creating customer feedback");
        if (feedback == null) {
            logger.warn("Attempted to create feedback with null data");
            throw new IllegalArgumentException("Feedback cannot be null");
        }
        return customerFeedbackRepository.save(feedback);
    }

    public List<CustomerGrievance> getAllGrievances(Long accountNumber) throws ResourceNotFoundException {
        logger.info("Fetching all grievances for account number: {}", accountNumber);
        List<CustomerGrievance> grievances = customerGrievanceRepository.findAllByAccountNumber(accountNumber);
        if (grievances.isEmpty()) {
            logger.warn("No grievances found for account number: {}", accountNumber);
            throw new ResourceNotFoundException("No grievances found for account number: " + accountNumber);
        }
        return grievances;
    }

    public CustomerGrievance createGrievance(CustomerGrievance grievance) {
        logger.info("Creating a grievance");
        if (grievance == null) {
            logger.warn("Attempted to create a grievance with null data");
            throw new IllegalArgumentException("Grievance cannot be null");
        }
        String mail = customerProfileRepository.findEmail(grievance.getCustomerCardAccount().getAccountNumber())
                .getEmail();
        sendEmail(mail, "Grievance Submitted Successfully",
                "Hi, your grievance has been submitted. Our Customer Support will try to resolve it as soon as possible.");
        return customerGrievanceRepository.save(grievance);
    }

    public CustomerScheduleCall addScheduleCall(CustomerScheduleCall scheduleCall) {
        logger.info("Adding a schedule call");
        if (scheduleCall == null) {
            logger.warn("Attempted to add a schedule call with null data");
            throw new IllegalArgumentException("Schedule call data cannot be null");
        }
        String mail = customerProfileRepository.findEmail(scheduleCall.getCustomerCardAccount().getAccountNumber())
                .getEmail();
        sendEmail(mail, "Schedule Call Submitted Successfully",
                "Hi, your schedule call has been submitted. Our Customer Support will try to resolve it as soon as possible.");
        return customerScheduleCallRepository.save(scheduleCall);
    }

    private void sendEmail(String to, String subject, String text) {
        logger.info("Sending email to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

