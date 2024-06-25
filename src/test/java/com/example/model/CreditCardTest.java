package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreditCardTest {

    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        creditCard = new CreditCard();
    }

    @Test
    void testGettersAndSetters() {
        creditCard.setCardType("Gold");
        creditCard.setMaxLimit(new BigDecimal("5000.0000"));
        creditCard.setInterest(new BigDecimal("13.5"));
        creditCard.setAnnualFee(new BigDecimal("100.0000"));

        assertEquals("Gold", creditCard.getCardType());
        assertEquals(new BigDecimal("5000.0000"), creditCard.getMaxLimit());
        assertEquals(new BigDecimal("13.5"), creditCard.getInterest());
        assertEquals(new BigDecimal("100.0000"), creditCard.getAnnualFee());
    }

    @Test
    void testConstructorWithParameters() {
        CreditCard creditCard = new CreditCard("Platinum", new BigDecimal("10000.0000"), new BigDecimal("12.5"), new BigDecimal("200.0000"));

        assertEquals("Platinum", creditCard.getCardType());
        assertEquals(new BigDecimal("10000.0000"), creditCard.getMaxLimit());
        assertEquals(new BigDecimal("12.5"), creditCard.getInterest());
        assertEquals(new BigDecimal("200.0000"), creditCard.getAnnualFee());
    }

    @Test
    void testNoArgsConstructor() {
        CreditCard creditCard = new CreditCard();

        // Ensure default constructor works and fields are null or zero as expected
        assertEquals(null, creditCard.getCardType());
        assertEquals(null, creditCard.getMaxLimit());
        assertEquals(null, creditCard.getInterest());
        assertEquals(null, creditCard.getAnnualFee());
    }
}
