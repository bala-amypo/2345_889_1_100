package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VendorComplianceApplicationTests {

    // Mock all services
    @MockBean
    private UserService userService;

    @MockBean
    private VendorService vendorService;

    @MockBean
    private DocumentTypeService documentTypeService;

    @MockBean
    private VendorDocumentService vendorDocumentService;

    @MockBean
    private ComplianceRuleService complianceRuleService;

    @MockBean
    private ComplianceScoreService complianceScoreService;

    @Autowired
    private JwtUtil jwtUtil;

    private User testUser;
    private Vendor testVendor;

    @BeforeEach
    public void setup() {
        // Initialize test objects using public setters
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("user@example.com");

        testVendor = new Vendor();
        testVendor.setId(1L);
        testVendor.setVendorName("Test Vendor");
    }

    @Test
    public void testJwtUtil_generateAndValidateToken() {
        // Mock a UserDetails object
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(testUser.getUsername());

        // Generate JWT token
        String token = jwtUtil.generateToken(
                () -> testUser.getUsername(), // Authentication mock
                testUser.getId(),
                testUser.getEmail(),
                "USER"
        );

        assertNotNull(token);

        // Validate token
        boolean isValid = jwtUtil.validateToken(token, userDetails);
        assertTrue(isValid);

        // Extract user ID and role from token
        Long userId = jwtUtil.getUserIdFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);

        assertEquals(testUser.getId(), userId);
        assertEquals("USER", role);
    }

    @Test
    public void testVendorServiceMock() {
        when(vendorService.getVendor(1L)).thenReturn(testVendor);

        Vendor vendor = vendorService.getVendor(1L);
        assertEquals("Test Vendor", vendor.getVendorName());
    }

    @Test
    public void testUserServiceMock() {
        when(userService.getUser(1L)).thenReturn(testUser);

        User user = userService.getUser(1L);
        assertEquals("testuser", user.getUsername());
    }
}
