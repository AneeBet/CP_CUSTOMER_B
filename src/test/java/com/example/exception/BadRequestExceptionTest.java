package com.example.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class BadRequestExceptionTest {

    @Test
    public void testBadRequestException() {
        String errorMessage = "This is a bad request";
        
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }
}
