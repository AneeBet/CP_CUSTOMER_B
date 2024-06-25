package com.example.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.exception.ResourceNotFoundException;

import java.nio.charset.StandardCharsets;

public class Hasher {

    public static String hashPassword(String password) throws ResourceNotFoundException {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Apply the digest to the input string in bytes
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // Convert the byte array into a hexadecimal string
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new ResourceNotFoundException("No resource");
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

  
}
