package com.example.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UnauthorizedAccessExceptionTest {

    @Test
    public void testUnauthorizedAccessException() {
        String errorMessage = "Unauthorized access";
        UnauthorizedAccessException exception = new UnauthorizedAccessException(errorMessage);

        // Check if the message is correctly set
        assertEquals(errorMessage, exception.getMessage());

        // Check if the exception is of the correct type
        assertTrue(exception instanceof UnauthorizedAccessException);
    }
}
