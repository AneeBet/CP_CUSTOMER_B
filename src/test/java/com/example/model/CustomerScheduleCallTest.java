package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerScheduleCallTest {

    private CustomerScheduleCall customerScheduleCall;
    private CustomerCardAccount customerCardAccount;

    @BeforeEach
    public void setUp() {
        customerCardAccount = new CustomerCardAccount();

        customerScheduleCall = new CustomerScheduleCall(
            1L,
            customerCardAccount,
            "Test Subject",
            "Test Description",
            "10:00 AM - 11:00 AM",
            ResolutionStatus.PENDING
        );
    }

    @Test
    public void testGetters() {
        assertEquals(1L, customerScheduleCall.getScheduleCallId());
        assertEquals(customerCardAccount, customerScheduleCall.getCustomerCardAccount());
        assertEquals("Test Subject", customerScheduleCall.getSubject());
        assertEquals("Test Description", customerScheduleCall.getDescription());
        assertEquals("10:00 AM - 11:00 AM", customerScheduleCall.getTimeSlot());
        assertEquals(ResolutionStatus.PENDING, customerScheduleCall.getStatus());
    }

    @Test
    public void testSetters() {
        CustomerCardAccount newCustomerCardAccount = new CustomerCardAccount();

        customerScheduleCall.setScheduleCallId(2L);
        customerScheduleCall.setCustomerCardAccount(newCustomerCardAccount);
        customerScheduleCall.setSubject("New Subject");
        customerScheduleCall.setDescription("New Description");
        customerScheduleCall.setTimeSlot("11:00 AM - 12:00 PM");
        customerScheduleCall.setStatus(ResolutionStatus.RESOLVED);

        assertEquals(2L, customerScheduleCall.getScheduleCallId());
        assertEquals(newCustomerCardAccount, customerScheduleCall.getCustomerCardAccount());
        assertEquals("New Subject", customerScheduleCall.getSubject());
        assertEquals("New Description", customerScheduleCall.getDescription());
        assertEquals("11:00 AM - 12:00 PM", customerScheduleCall.getTimeSlot());
        assertEquals(ResolutionStatus.RESOLVED, customerScheduleCall.getStatus());
    }

    @Test
    public void testDefaultConstructor() {
        CustomerScheduleCall defaultCustomerScheduleCall = new CustomerScheduleCall();
        assertNotNull(defaultCustomerScheduleCall);
    }
}
