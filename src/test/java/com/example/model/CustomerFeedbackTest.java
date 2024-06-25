package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerFeedbackTest {

    private CustomerFeedback customerFeedback;

    @BeforeEach
    void setUp() {
        customerFeedback = new CustomerFeedback();
    }

    @Test
    void testGettersAndSetters() {
        Long feedbackId = 1L;
        Byte rating = 5;
        CustomerProfile customerProfile = new CustomerProfile();
        String description = "Great service!";

        customerFeedback.setFeedbackId(feedbackId);
        customerFeedback.setRating(rating);
        customerFeedback.setCustomerProfile(customerProfile);
        customerFeedback.setDescription(description);

        assertEquals(feedbackId, customerFeedback.getFeedbackId());
        assertEquals(rating, customerFeedback.getRating());
        assertEquals(customerProfile, customerFeedback.getCustomerProfile());
        assertEquals(description, customerFeedback.getDescription());
    }

    @Test
    void testConstructorWithParameters() {
        Long feedbackId = 1L;
        Byte rating = 5;
        CustomerProfile customerProfile = new CustomerProfile();
        String description = "Great service!";

        CustomerFeedback customerFeedback = new CustomerFeedback(feedbackId, rating, customerProfile, description);

        assertEquals(feedbackId, customerFeedback.getFeedbackId());
        assertEquals(rating, customerFeedback.getRating());
        assertEquals(customerProfile, customerFeedback.getCustomerProfile());
        assertEquals(description, customerFeedback.getDescription());
    }

    @Test
    void testNoArgsConstructor() {
        CustomerFeedback customerFeedback = new CustomerFeedback();

        // Ensure default constructor works and fields are null or zero as expected
        assertNull(customerFeedback.getFeedbackId());
        assertNull(customerFeedback.getRating());
        assertNull(customerFeedback.getCustomerProfile());
        assertNull(customerFeedback.getDescription());
    }
}
