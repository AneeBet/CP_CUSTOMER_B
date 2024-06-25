package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ApplicationStatusTest {

    @Test
    public void testApplicationStatusValues() {
        ApplicationStatus[] statuses = ApplicationStatus.values();
        assertEquals(3, statuses.length);
        assertEquals(ApplicationStatus.APPROVED, statuses[0]);
        assertEquals(ApplicationStatus.REJECTED, statuses[1]);
        assertEquals(ApplicationStatus.PENDING, statuses[2]);
    }

    @Test
    public void testApplicationStatusValueOf() {
        assertEquals(ApplicationStatus.APPROVED, ApplicationStatus.valueOf("APPROVED"));
        assertEquals(ApplicationStatus.REJECTED, ApplicationStatus.valueOf("REJECTED"));
        assertEquals(ApplicationStatus.PENDING, ApplicationStatus.valueOf("PENDING"));
    }
}
