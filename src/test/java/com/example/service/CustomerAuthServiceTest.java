package com.example.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerProfile;
import com.example.model.PasswordResetToken;
import com.example.repository.CustomerProfileRepository;
import com.example.repository.PasswordResetTokenRepository;

public class CustomerAuthServiceTest {

    @Mock
    private CustomerProfileRepository customerProfileRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private CustomerAuthService customerAuthService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVerifyOtp_Success() {
        PasswordResetToken token = new PasswordResetToken();
        token.setEmail("test@example.com");
        token.setOtp("123456");
        token.setExpiryTime(LocalDateTime.now().plusMinutes(10));

        when(passwordResetTokenRepository.findByEmailAndOtp("test@example.com", "123456"))
                .thenReturn(Optional.of(token));

        assertTrue(customerAuthService.verifyOtp("test@example.com", "123456"));
    }

    @Test
    public void testVerifyOtp_Failure() {
        when(passwordResetTokenRepository.findByEmailAndOtp(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertFalse(customerAuthService.verifyOtp("test@example.com", "123456"));
    }

    @Test
    public void testSendOtp_Success() {
        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setEmail("test@example.com");

        when(customerProfileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(customerProfile));

        boolean result = customerAuthService.sendOtp("test@example.com");

        assertTrue(result);
        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendOtp_Failure() {
        when(customerProfileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        boolean result = customerAuthService.sendOtp("test@example.com");

        assertFalse(result);
    }

    @Test
    public void testResetPassword_Success() throws ResourceNotFoundException {
        PasswordResetToken token = new PasswordResetToken();
        token.setEmail("test@example.com");
        token.setOtp("123456");
        token.setExpiryTime(LocalDateTime.now().plusMinutes(10));

        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setEmail("test@example.com");
        customerProfile.setPassword("oldPassword");

        when(passwordResetTokenRepository.findByEmailAndOtp("test@example.com", "123456"))
                .thenReturn(Optional.of(token));
        when(customerProfileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(customerProfile));

        customerAuthService.resetPassword("test@example.com", "123456", "newPassword");

        verify(customerProfileRepository).save(customerProfile);
        verify(passwordResetTokenRepository).deleteByEmail("test@example.com");
    }

//    @Test
//    public void testResetPassword_Failure() {
//        when(passwordResetTokenRepository.findByEmailAndOtp("test@example.com", "123456"))
//                .thenReturn(Optional.empty());
//
//        customerAuthService.resetPassword("test@example.com", "123456", "newPassword");
//
//        verify(customerProfileRepository).findByEmail("test@example.com");
//        verify(passwordResetTokenRepository).findByEmailAndOtp("test@example.com", "123456");
//    }
}
