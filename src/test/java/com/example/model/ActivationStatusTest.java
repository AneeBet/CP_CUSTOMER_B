package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ActivationStatusTest {

    @Test
    public void testActivationStatusValues() {
        ActivationStatus[] statuses = ActivationStatus.values();
        assertEquals(2, statuses.length);
        assertEquals(ActivationStatus.ACTIVE, statuses[0]);
        assertEquals(ActivationStatus.INACTIVE, statuses[1]);
    }

    @Test
    public void testActivationStatusValueOf() {
        assertEquals(ActivationStatus.ACTIVE, ActivationStatus.valueOf("ACTIVE"));
        assertEquals(ActivationStatus.INACTIVE, ActivationStatus.valueOf("INACTIVE"));
    }
}
