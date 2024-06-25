package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PaymentStatusTest {

    @Test
    public void testPaymentStatusValues() {
        PaymentStatus[] statuses = PaymentStatus.values();
        assertEquals(2, statuses.length);
        assertEquals(PaymentStatus.ENABLE, statuses[0]);
        assertEquals(PaymentStatus.DISABLE, statuses[1]);
    }

    @Test
    public void testPaymentStatusValueOf() {
        assertEquals(PaymentStatus.ENABLE, PaymentStatus.valueOf("ENABLE"));
        assertEquals(PaymentStatus.DISABLE, PaymentStatus.valueOf("DISABLE"));
    }
}
