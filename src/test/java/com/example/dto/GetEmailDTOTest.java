package com.example.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class GetEmailDTOTest {

    @Test
    public void testNoArgsConstructor() {
        GetEmailDTO getEmailDTO = new GetEmailDTO();
        assertNotNull(getEmailDTO);
    }

    @Test
    public void testAllArgsConstructor() {
        String email = "test@example.com";
        GetEmailDTO getEmailDTO = new GetEmailDTO(email);
        assertEquals(email, getEmailDTO.getEmail());
    }

    @Test
    public void testGettersAndSetters() {
        GetEmailDTO getEmailDTO = new GetEmailDTO();
        String email = "test@example.com";

        getEmailDTO.setEmail(email);
        assertEquals(email, getEmailDTO.getEmail());
    }
}
