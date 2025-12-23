package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.service.VendorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class VendorComplianceApplicationTests {

    @MockBean
    private UserService userService;

    @MockBean
    private VendorService vendorService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("user@example.com");
        testUser.setPassword("password");
    }

    @Test
    void contextLoads() {
        // This test only checks whether Spring context loads successfully
        assertTrue(true);
    }

    @Test
    void testFindUserById() {
        when(userService.findById(1L)).thenReturn(testUser);

        User user = userService.findById(1L);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("user@example.com", user.getEmail());
    }

    @Test
    void testFindUserByEmail() {
        when(userService.findByEmail("user@example.com")).thenReturn(testUser);

        User user = userService.findByEmail("user@example.com");

        assertNotNull(user);
        assertEquals("user@example.com", user.getEmail());
    }

    @Test
    void testRegisterUser() {
        when(userService.registerUser(any(User.class))).thenReturn(testUser);

        User savedUser = userService.registerUser(testUser);

        assertNotNull(savedUser);
        assertEquals("user@example.com", savedUser.getEmail());
    }
}