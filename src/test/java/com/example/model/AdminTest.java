package com.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AdminTest {

    @Test
    void testAdminConstructorAndGetters() {
        Admin admin = new Admin("adminUser", "adminPass");

        assertEquals("adminUser", admin.getUsername());
        assertEquals("adminPass", admin.getPassword());
    }

    @Test
    void testAdminSetters() {
        Admin admin = new Admin();
        admin.setUsername("newUser");
        admin.setPassword("newPass");

        assertEquals("newUser", admin.getUsername());
        assertEquals("newPass", admin.getPassword());
    }

    @Test
    void testAdminDefaultConstructor() {
        Admin admin = new Admin();

        assertNull(admin.getUsername());
        assertNull(admin.getPassword());
    }
}
