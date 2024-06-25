package com.example.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

	private final CustomerProfileRepository customerProfileRepository;

	private final PasswordResetTokenRepository passwordResetTokenRepository;

	private final JavaMailSender mailSender;

	public CustomerAuthService(CustomerProfileRepository customerProfileRepository,
			PasswordResetTokenRepository passwordResetTokenRepository, JavaMailSender mailSender) {
		this.customerProfileRepository = customerProfileRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.mailSender = mailSender;
	}


	public boolean verifyOtp(String email, String otp) {
		Optional<PasswordResetToken> token = passwordResetTokenRepository.findByEmailAndOtp(email, otp);
		return (token.isPresent() && token.get().getExpiryTime().isAfter(LocalDateTime.now()));
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
	        return true;
	    } else {
	    	return false;
	    }
	}

	@Transactional
	public void resetPassword(String email, String otp, String newPassword) throws ResourceNotFoundException {
	    if (verifyOtp(email, otp)) {
	        Optional<CustomerProfile> optionalCustomerProfile = customerProfileRepository.findByEmail(email);
	        if (optionalCustomerProfile.isPresent()) {
	            CustomerProfile customerProfile = optionalCustomerProfile.get();
	            customerProfile.setPassword(Hasher.hashPassword(newPassword));
	            customerProfileRepository.save(customerProfile);
	            passwordResetTokenRepository.deleteByEmail(customerProfile.getEmail());
	        } 
	    } 
	}

	private void sendEmail(String to, String otp) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Your OTP Code");
		message.setText("Your OTP code is: " + otp);
		mailSender.send(message);

	}

	String generateOtp() {

		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}
}
