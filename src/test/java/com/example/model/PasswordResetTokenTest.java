package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordResetTokenTest {

    private PasswordResetToken passwordResetToken;

    @BeforeEach
    void setUp() {
        passwordResetToken = new PasswordResetToken();
    }

    @Test
    void testGettersAndSetters() {
        Long id = 1L;
        String email = "john.doe@example.com";
        String otp = "123456";
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);

        passwordResetToken.setId(id);
        passwordResetToken.setEmail(email);
        passwordResetToken.setOtp(otp);
        passwordResetToken.setExpiryTime(expiryTime);

        assertEquals(id, passwordResetToken.getId());
        assertEquals(email, passwordResetToken.getEmail());
        assertEquals(otp, passwordResetToken.getOtp());
        assertEquals(expiryTime, passwordResetToken.getExpiryTime());
    }

    @Test
    void testNoArgsConstructor() {
        PasswordResetToken passwordResetToken = new PasswordResetToken();

        // Ensure default constructor works and fields are null as expected
        assertNull(passwordResetToken.getId());
        assertNull(passwordResetToken.getEmail());
        assertNull(passwordResetToken.getOtp());
        assertNull(passwordResetToken.getExpiryTime());
    }
}
