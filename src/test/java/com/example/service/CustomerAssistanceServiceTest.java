package com.example.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.dto.GetEmailDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerFeedback;
import com.example.model.CustomerGrievance;
import com.example.model.CustomerProfile;
import com.example.model.CustomerScheduleCall;
import com.example.repository.CustomerFeedbackRepository;
import com.example.repository.CustomerGrievanceRepository;
import com.example.repository.CustomerProfileRepository;
import com.example.repository.CustomerScheduleCallRepository;

public class CustomerAssistanceServiceTest {

    @Mock
    private CustomerFeedbackRepository customerFeedbackRepository;

    @Mock
    private CustomerProfileRepository customerProfileRepository;

    @Mock
    private CustomerGrievanceRepository customerGrievanceRepository;

    @Mock
    private CustomerScheduleCallRepository customerScheduleCallRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private CustomerAssistanceService customerAssistanceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFeedback_Success() {
        CustomerFeedback feedback = new CustomerFeedback();
        when(customerFeedbackRepository.save(any(CustomerFeedback.class))).thenReturn(feedback);

        CustomerFeedback result = customerAssistanceService.createFeedback(feedback);

        verify(customerFeedbackRepository).save(feedback);
        assertTrue(result != null);
    }

    @Test
    public void testCreateFeedback_NullFeedback() { 
        assertThrows(IllegalArgumentException.class, () -> {
            customerAssistanceService.createFeedback(null);
        });
    }

    @Test
    public void testGetAllGrievances_Success() throws ResourceNotFoundException {
        CustomerGrievance grievance = new CustomerGrievance();
        List<CustomerGrievance> grievances = Collections.singletonList(grievance);
        when(customerGrievanceRepository.findAllByAccountNumber(anyLong())).thenReturn(grievances);

        List<CustomerGrievance> result = customerAssistanceService.getAllGrievances(12345L);

        verify(customerGrievanceRepository).findAllByAccountNumber(12345L);
        assertTrue(!result.isEmpty());
    }

    @Test
    public void testGetAllGrievances_NoGrievancesFound() {
        when(customerGrievanceRepository.findAllByAccountNumber(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerAssistanceService.getAllGrievances(12345L);
        });
    }

//    @Test
//    public void testCreateGrievance_Success() {
//        CustomerGrievance grievance = new CustomerGrievance();
//        GetEmailDTO emailDTO = new GetEmailDTO("test@example.com");
//
//        when(customerProfileRepository.findEmail(anyLong())).thenReturn(emailDTO);
//        when(customerGrievanceRepository.save(any(CustomerGrievance.class))).thenReturn(grievance);
//
//        CustomerGrievance result = customerAssistanceService.createGrievance(grievance);
//
//        verify(customerGrievanceRepository).save(grievance);
//        verify(mailSender).send(any(SimpleMailMessage.class));
//        assertTrue(result != null);
//    }
//
//    @Test
//    public void testCreateGrievance_NullGrievance() {
//        assertThrows(IllegalArgumentException.class, () -> {
//            customerAssistanceService.createGrievance(null);
//        });
//    }
//
//    @Test
//    public void testAddScheduleCall_Success() {
//        CustomerScheduleCall scheduleCall = new CustomerScheduleCall();
//        GetEmailDTO emailDTO = new GetEmailDTO("test@example.com");
//
//        when(customerProfileRepository.findEmail(anyLong())).thenReturn(emailDTO);
//        when(customerScheduleCallRepository.save(any(CustomerScheduleCall.class))).thenReturn(scheduleCall);
//
//        CustomerScheduleCall result = customerAssistanceService.addScheduleCall(scheduleCall);
//
//        verify(customerScheduleCallRepository).save(scheduleCall);
//        verify(mailSender).send(any(SimpleMailMessage.class));
//        assertTrue(result != null);
//    }

    @Test
    public void testAddScheduleCall_NullScheduleCall() {
        assertThrows(IllegalArgumentException.class, () -> {
            customerAssistanceService.addScheduleCall(null);
        });
    }
}
