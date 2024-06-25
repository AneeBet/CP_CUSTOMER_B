package com.example.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest request;

    @BeforeEach
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        request = mock(WebRequest.class);
    }

    @Test
    public void testResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        when(request.getDescription(false)).thenReturn("Test description");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.resourceNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals("Resource not found", errorDetails.getMessage());
        assertEquals("Test description", errorDetails.getDetails());
    }

    @Test
    public void testBadRequestException() {
        BadRequestException ex = new BadRequestException("Bad request");
        when(request.getDescription(false)).thenReturn("Test description");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.badRequestException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals("Bad request", errorDetails.getMessage());
        assertEquals("Test description", errorDetails.getDetails());
    }

    @Test
    public void testUnauthorizedAccessException() {
        UnauthorizedAccessException ex = new UnauthorizedAccessException("Unauthorized access");
        when(request.getDescription(false)).thenReturn("Test description");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.unauthorizedAccessException(ex, request);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals("Unauthorized access", errorDetails.getMessage());
        assertEquals("Test description", errorDetails.getDetails());
    }
}
