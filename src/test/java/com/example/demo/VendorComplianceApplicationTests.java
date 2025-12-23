package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.service.VendorService;
import com.example.demo.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VendorComplianceApplicationTests {

    @MockBean
    private UserService userService;

    @MockBean
    private VendorService vendorService;

    @Autowired
    private JwtUtil jwtUtil;

    private User testUser;

    @BeforeEach
    public void setup() {
        // Initialize a test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("user@example.com");
        testUser.setPassword("password"); // if needed
    }

    @Test
    public void testJwtTokenGenerationAndClaims() {
        // Mock authentication
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(testUser.getEmail());

        // Generate JWT token
        String token = jwtUtil.generateToken(auth, testUser.getId(), testUser.getEmail(), "USER");
        assertNotNull(token, "Token should not be null");

        // Mock UserDetails for token validation
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(testUser.getEmail());

        // Validate the token
        assertTrue(jwtUtil.validateToken(token, userDetails), "Token should be valid");

        // Extract user ID and role from token
        Long userId = jwtUtil.getUserIdFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);

        assertEquals(testUser.getId(), userId, "User ID from token should match");
        assertEquals("USER", role, "Role from token should match");
    }

    @Test
    public void testUserServiceFindById() {
        // Mock findById
        when(userService.findById(1L)).thenReturn(testUser);

        User user = userService.findById(1L);
        assertNotNull(user, "User should not be null");
        assertEquals("user@example.com", user.getEmail(), "User email should match");
    }

    @Test
    public void testVendorServiceMock() {
        // Example mock for vendor service
        when(vendorService.getAllVendors()).thenReturn(java.util.Collections.emptyList());

        assertTrue(vendorService.getAllVendors().isEmpty(), "Vendor list should be empty");
    }
}
