package com.example.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerFeedback;
import com.example.model.CustomerGrievance;
import com.example.model.CustomerScheduleCall;
import com.example.service.CustomerAssistanceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerAssistanceControllerTest {

    @Mock
    private CustomerAssistanceService customerAssistanceService;

    @InjectMocks
    private CustomerAssistanceController customerAssistanceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateFeedback_Success() throws BadRequestException {
        CustomerFeedback feedback = new CustomerFeedback();
        CustomerFeedback createdFeedback = new CustomerFeedback();
        
        when(customerAssistanceService.createFeedback(feedback)).thenReturn(createdFeedback);
        
        ResponseEntity<CustomerFeedback> response = customerAssistanceController.createFeedback(feedback);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdFeedback, response.getBody());
    }

    @Test
    public void testCreateFeedback_NullFeedback() {
        assertThrows(BadRequestException.class, () -> {
            customerAssistanceController.createFeedback(null);
        });
    }

    @Test
    public void testGetAllGrievances_Success() throws BadRequestException, ResourceNotFoundException {
        Map<String, Object> data = new HashMap<>();
        data.put("accountNumber", "12345");
        List<CustomerGrievance> grievances = new ArrayList<>();
        grievances.add(new CustomerGrievance());

        when(customerAssistanceService.getAllGrievances(12345L)).thenReturn(grievances);
        
        ResponseEntity<List<CustomerGrievance>> response = customerAssistanceController.getAllGrievances(data);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grievances, response.getBody());
    }

    @Test
    public void testGetAllGrievances_MissingAccountNumber() {
        Map<String, Object> data = new HashMap<>();

        assertThrows(BadRequestException.class, () -> {
            customerAssistanceController.getAllGrievances(data);
        });
    }

    @Test
    public void testGetAllGrievances_InvalidAccountNumberFormat() {
        Map<String, Object> data = new HashMap<>();
        data.put("accountNumber", "invalid");

        assertThrows(BadRequestException.class, () -> {
            customerAssistanceController.getAllGrievances(data);
        });
    }

    @Test
    public void testGetAllGrievances_NoGrievancesFound() throws BadRequestException, ResourceNotFoundException {
        Map<String, Object> data = new HashMap<>();
        data.put("accountNumber", "12345");

        when(customerAssistanceService.getAllGrievances(12345L)).thenReturn(new ArrayList<>());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            customerAssistanceController.getAllGrievances(data);
        });
    }

    @Test
    public void testCreateGrievance_Success() throws BadRequestException {
        CustomerGrievance grievance = new CustomerGrievance();
        CustomerGrievance createdGrievance = new CustomerGrievance();
        
        when(customerAssistanceService.createGrievance(grievance)).thenReturn(createdGrievance);
        
        ResponseEntity<CustomerGrievance> response = customerAssistanceController.createGrievance(grievance);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdGrievance, response.getBody());
    }

    @Test
    public void testCreateGrievance_NullGrievance() {
        assertThrows(BadRequestException.class, () -> {
            customerAssistanceController.createGrievance(null);
        });
    }

    @Test
    public void testAddScheduleCall_Success() throws BadRequestException {
        CustomerScheduleCall scheduleCall = new CustomerScheduleCall();
        CustomerScheduleCall createdScheduleCall = new CustomerScheduleCall();
        
        when(customerAssistanceService.addScheduleCall(scheduleCall)).thenReturn(createdScheduleCall);
        
        ResponseEntity<CustomerScheduleCall> response = customerAssistanceController.addScheduleCall(scheduleCall);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdScheduleCall, response.getBody());
    }

    @Test
    public void testAddScheduleCall_NullScheduleCall() {
        assertThrows(BadRequestException.class, () -> {
            customerAssistanceController.addScheduleCall(null);
        });
    }
}
