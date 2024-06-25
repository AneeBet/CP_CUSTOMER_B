package com.example.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testResourceNotFoundException() {
        String errorMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        // Check if the message is correctly set
        assertEquals(errorMessage, exception.getMessage());

        // Check if the exception is of the correct type
        assertTrue(exception instanceof ResourceNotFoundException);
    }
}
