package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class VendorComplianceApplicationTests {

    @MockBean
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("user@example.com");
        testUser.setPassword("password");
    }

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void testFindUserById() {
        when(userService.findById(1L)).thenReturn(testUser);

        User user = userService.findById(1L);

        assertNotNull(user);
        assertEquals(1L, user.getId());
    }

    @Test
    void testFindUserByEmail() {
        when(userService.findByEmail("user@example.com")).thenReturn(testUser);

        User user = userService.findByEmail("user@example.com");

        assertNotNull(user);
        assertEquals("user@example.com", user.getEmail());
    }
}