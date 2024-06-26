package com.example.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.CustomerProfile;
import com.example.model.PasswordResetToken;
import com.example.repository.CustomerProfileRepository;
import com.example.repository.PasswordResetTokenRepository;
import com.example.util.Hasher;

import jakarta.transaction.Transactional;

@Service
public class CustomerAuthService {

	private static final SecureRandom random = new SecureRandom();
	private static final Logger logger = LoggerFactory.getLogger(CustomerAuthService.class);

	private final CustomerProfileRepository customerProfileRepository;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final JavaMailSender mailSender;

	public CustomerAuthService(CustomerProfileRepository customerProfileRepository,
			PasswordResetTokenRepository passwordResetTokenRepository, JavaMailSender mailSender) {
		this.customerProfileRepository = customerProfileRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.mailSender = mailSender;
	}

	public boolean verifyPasswordResetOtp(String email, String otp) {
		logger.info("Verifying OTP for email: {} with OTP: {}", email, otp);

		try {
			Optional<PasswordResetToken> token = passwordResetTokenRepository.findByEmailAndOtp(email, otp);

			logger.debug("Token present: {}", token.isPresent());

			if (token.isPresent()) {
				PasswordResetToken retrievedToken = token.get();
				logger.debug("Token found: {}", retrievedToken);
				boolean isTokenValid = retrievedToken.getExpiryTime().isAfter(LocalDateTime.now());
				logger.debug("Token valid: {}", isTokenValid);
				return isTokenValid;
			} else {
				logger.warn("No token found for email: {} with OTP: {}", email, otp);
				return false;
			}
		} catch (Exception e) {
			logger.error("An error occurred while verifying OTP for email: {} with OTP: {}", email, otp, e);
			return false;
		}
	}

	public boolean sendOtp(String email) {
		Optional<CustomerProfile> customerProfile = customerProfileRepository.findByEmail(email);
		if (customerProfile.isPresent()) {
			String otp = generateOtp();
			PasswordResetToken token = new PasswordResetToken();
			token.setEmail(email);
			token.setOtp(otp);
			token.setExpiryTime(LocalDateTime.now().plusMinutes(10));
			passwordResetTokenRepository.save(token);
			sendEmail(email, otp);
			logger.info("OTP sent to email: {}", email);
			return true;
		} else {
			logger.warn("Customer profile not found for email: {}", email);
			return false;
		}
	}

	@Transactional
	public void resetPassword(String email, String otp, String newPassword) throws ResourceNotFoundException, BadRequestException {
		if (verifyPasswordResetOtp(email, otp)) {
			Optional<CustomerProfile> optionalCustomerProfile = customerProfileRepository.findByEmail(email);
			if (optionalCustomerProfile.isPresent()) {
				CustomerProfile customerProfile = optionalCustomerProfile.get();
				customerProfile.setPassword(Hasher.hashPassword(newPassword));
				customerProfileRepository.save(customerProfile);
				passwordResetTokenRepository.deleteByEmail(customerProfile.getEmail());
				logger.info("Password reset successfully for email: {}", email);
			} else {
				logger.warn("Customer profile not found for email: {}", email);
				throw new ResourceNotFoundException("Customer profile not found for email: " + email);
			}
		} else {
			logger.warn("Invalid OTP for email: {}", email);
			throw new BadRequestException("Invalid OTP");
		}
	}

	private void sendEmail(String to, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Your OTP Code");
		message.setText("Your OTP code is: " + otp);
		mailSender.send(message);
		logger.info("Email sent to: {}", to);
	}

	String generateOtp() {
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}
}
