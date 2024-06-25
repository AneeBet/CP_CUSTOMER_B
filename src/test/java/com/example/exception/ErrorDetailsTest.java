package com.example.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ErrorDetailsTest {

    private Date timestamp;
    private String message;
    private String details;
    private ErrorDetails errorDetails;

    @BeforeEach
    public void setUp() {
        timestamp = new Date();
        message = "Error message";
        details = "Error details";
        errorDetails = new ErrorDetails(timestamp, message, details);
    }

    @Test
    public void testGetTimestamp() {
        assertEquals(timestamp, errorDetails.getTimestamp());
    }

    @Test
    public void testGetMessage() {
        assertEquals(message, errorDetails.getMessage());
    }

    @Test
    public void testGetDetails() {
        assertEquals(details, errorDetails.getDetails());
    }
}
