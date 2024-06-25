package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerFeedback;
import com.example.model.CustomerGrievance;
import com.example.model.CustomerScheduleCall;
import com.example.service.CustomerAssistanceService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer")
public class CustomerAssistanceController {

	private CustomerAssistanceService customerAssistanceService;

	public CustomerAssistanceController(CustomerAssistanceService customerAssistanceService) {
		this.customerAssistanceService = customerAssistanceService;
	}

	@PostMapping("/feedback/add")
	public ResponseEntity<CustomerFeedback> createFeedback(@RequestBody CustomerFeedback feedback)
			throws BadRequestException {
		if (feedback == null) {
			throw new BadRequestException("Feedback data must not be null");
		}
		CustomerFeedback createdFeedback = customerAssistanceService.createFeedback(feedback);
		return ResponseEntity.ok(createdFeedback);
	}

	@PostMapping("/grievance/readall")
	public ResponseEntity<List<CustomerGrievance>> getAllGrievances(@RequestBody Map<String, Object> data)
			throws BadRequestException, ResourceNotFoundException {
		if (!data.containsKey("accountNumber")) {
			throw new BadRequestException("Account number is required");
		}
		Long accountNumber;
		try {
			accountNumber = Long.parseLong(data.get("accountNumber").toString());
		} catch (NumberFormatException e) {
			throw new BadRequestException("Invalid account number format");
		}
		List<CustomerGrievance> grievances = customerAssistanceService.getAllGrievances(accountNumber);
		if (grievances.isEmpty()) {
			throw new ResourceNotFoundException("No grievances found for account number: " + accountNumber);
		}
		return ResponseEntity.ok(grievances);
	}

	@PostMapping("/grievance/add")
	public ResponseEntity<CustomerGrievance> createGrievance(@RequestBody CustomerGrievance grievance)
			throws BadRequestException {
		if (grievance == null) {
			throw new BadRequestException("Grievance data must not be null");
		}
		CustomerGrievance createdGrievance = customerAssistanceService.createGrievance(grievance);
		return ResponseEntity.ok(createdGrievance);
	}

	@PostMapping("/schedulecall/add")
	public ResponseEntity<CustomerScheduleCall> addScheduleCall(@RequestBody CustomerScheduleCall scheduleCall)
			throws BadRequestException {
		if (scheduleCall == null) {
			throw new BadRequestException("Schedule call data must not be null");
		}
		CustomerScheduleCall createdScheduleCall = customerAssistanceService.addScheduleCall(scheduleCall);
		return ResponseEntity.ok(createdScheduleCall);
	}

}
