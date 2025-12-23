package com.example.demo;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.servlet.HealthServlet;
import com.example.demo.util.ComplianceScoringEngine;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Listeners(TestResultListener.class)
public class VendorComplianceApplicationTests {
    
    @Mock
    private UserRepository userRepository;
    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private DocumentTypeRepository documentTypeRepository;
    @Mock
    private VendorDocumentRepository vendorDocumentRepository;
    @Mock
    private ComplianceRuleRepository complianceRuleRepository;
    @Mock
    private ComplianceScoreRepository complianceScoreRepository;
    
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private VendorService vendorService;
    private DocumentTypeService documentTypeService;
    private VendorDocumentService vendorDocumentService;
    private ComplianceRuleService complianceRuleService;
    private ComplianceScoreService complianceScoreService;
    
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        
        // Initialize services with correct constructor parameter order
        userService = new UserServiceImpl(userRepository, passwordEncoder);
        vendorService = new VendorServiceImpl(vendorRepository);
        documentTypeService = new DocumentTypeServiceImpl(documentTypeRepository);
        vendorDocumentService = new VendorDocumentServiceImpl(vendorDocumentRepository, vendorRepository, documentTypeRepository);
        complianceRuleService = new ComplianceRuleServiceImpl(complianceRuleRepository);
        complianceScoreService = new ComplianceScoreServiceImpl(vendorRepository, documentTypeRepository, vendorDocumentRepository, complianceScoreRepository);
    }
    
    // Test 1: UserService - Register User with unique email
    @Test
    public void testRegisterUser_Success() {
        User user = new User("John Doe", "john@example.com", "password123", "USER");
        
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        User result = userService.registerUser(user);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getEmail(), "john@example.com");
        verify(userRepository, times(1)).existsByEmail("john@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    // Test 2: UserService - Duplicate email throws ValidationException
    @Test(expectedExceptions = ValidationException.class)
    public void testRegisterUser_DuplicateEmail() {
        User user = new User("John Doe", "john@example.com", "password123", "USER");
        
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
        
        userService.registerUser(user);
    }
    
    // Test 3: UserService - Password is hashed
    @Test
    public void testRegisterUser_PasswordHashed() {
        User user = new User("John Doe", "john@example.com", "password123", "USER");
        
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        User result = userService.registerUser(user);
        
        Assert.assertNotNull(result.getPassword());
        Assert.assertNotEquals(result.getPassword(), "password123");
        Assert.assertTrue(passwordEncoder.matches("password123", result.getPassword()));
    }
    
    // Test 4: UserService - Default role is USER
    @Test
    public void testRegisterUser_DefaultRole() {
        User user = new User("John Doe", "john@example.com", "password123", null);
        
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        User result = userService.registerUser(user);
        
        Assert.assertEquals(result.getRole(), "USER");
    }
    
    // Test 5: UserService - Find by email throws ResourceNotFoundException
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        
        userService.findByEmail("notfound@example.com");
    }
    
    // Test 6: VendorService - Create vendor with unique name
    @Test
    public void testCreateVendor_Success() {
        Vendor vendor = new Vendor("Acme Corp", "acme@example.com", "1234567890", "IT");
        
        when(vendorRepository.existsByVendorName("Acme Corp")).thenReturn(false);
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);
        
        Vendor result = vendorService.createVendor(vendor);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getVendorName(), "Acme Corp");
    }
    
    // Test 7: VendorService - Duplicate vendor name throws ValidationException
    @Test(expectedExceptions = ValidationException.class)
    public void testCreateVendor_DuplicateName() {
        Vendor vendor = new Vendor("Acme Corp", "acme@example.com", "1234567890", "IT");
        
        when(vendorRepository.existsByVendorName("Acme Corp")).thenReturn(true);
        
        vendorService.createVendor(vendor);
    }
    
    // Test 8: VendorService - Get vendor by ID throws ResourceNotFoundException
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testGetVendor_NotFound() {
        when(vendorRepository.findById(999L)).thenReturn(Optional.empty());
        
        vendorService.getVendor(999L);
    }
    
    // Test 9: DocumentTypeService - Create document type with unique name
    @Test
    public void testCreateDocumentType_Success() {
        DocumentType type = new DocumentType("Tax ID", "Tax identification document", true, 10);
        
        when(documentTypeRepository.existsByTypeName("Tax ID")).thenReturn(false);
        when(documentTypeRepository.save(any(DocumentType.class))).thenReturn(type);
        
        DocumentType result = documentTypeService.createDocumentType(type);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getTypeName(), "Tax ID");
    }
    
    // Test 10: DocumentTypeService - Duplicate type name throws ValidationException
    @Test(expectedExceptions = ValidationException.class)
    public void testCreateDocumentType_DuplicateName() {
        DocumentType type = new DocumentType("Tax ID", "Tax identification document", true, 10);
        
        when(documentTypeRepository.existsByTypeName("Tax ID")).thenReturn(true);
        
        documentTypeService.createDocumentType(type);
    }
    
    // Test 11: VendorDocumentService - Upload document with valid expiry date
    @Test
    public void testUploadDocument_Success() {
        Vendor vendor = new Vendor("Acme Corp", "acme@example.com", "1234567890", "IT");
        vendor.setId(1L);
        
        DocumentType type = new DocumentType("Tax ID", "Tax identification document", true, 10);
        type.setId(1L);
        
        LocalDate futureDate = LocalDate.now().plusDays(30);
        VendorDocument document = new VendorDocument(vendor, type, "http://example.com/doc.pdf", futureDate);
        
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(documentTypeRepository.findById(1L)).thenReturn(Optional.of(type));
        when(vendorDocumentRepository.save(any(VendorDocument.class))).thenReturn(document);
        
        VendorDocument result = vendorDocumentService.uploadDocument(1L, 1L, document);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getFileUrl(), "http://example.com/doc.pdf");
    }
    
    // Test 12: VendorDocumentService - Past expiry date throws ValidationException
    @Test(expectedExceptions = ValidationException.class)
    public void testUploadDocument_PastExpiryDate() {
        Vendor vendor = new Vendor("Acme Corp", "acme@example.com", "1234567890", "IT");
        vendor.setId(1L);
        
        DocumentType type = new DocumentType("Tax ID", "Tax identification document", true, 10);
        type.setId(1L);
        
        LocalDate pastDate = LocalDate.now().minusDays(30);
        VendorDocument document = new VendorDocument(vendor, type, "http://example.com/doc.pdf", pastDate);
        
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(documentTypeRepository.findById(1L)).thenReturn(Optional.of(type));
        
        vendorDocumentService.uploadDocument(1L, 1L, document);
    }
    
    // Test 13: VendorDocumentService - Missing vendor throws ResourceNotFoundException
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testUploadDocument_VendorNotFound() {
        DocumentType type = new DocumentType("Tax ID", "Tax identification document", true, 10);
        type.setId(1L);
        
        VendorDocument document = new VendorDocument(null, type, "http://example.com/doc.pdf", LocalDate.now().plusDays(30));
        
        when(vendorRepository.findById(999L)).thenReturn(Optional.empty());
        
        vendorDocumentService.uploadDocument(999L, 1L, document);
    }
    
    // Test 14: VendorDocumentService - Missing document type throws ResourceNotFoundException
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testUploadDocument_DocumentTypeNotFound() {
        Vendor vendor = new Vendor("Acme Corp", "acme@example.com", "1234567890", "IT");
        vendor.setId(1L);
        
        VendorDocument document = new VendorDocument(vendor, null, "http://example.com/doc.pdf", LocalDate.now().plusDays(30));
        
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(documentTypeRepository.findById(999L)).thenReturn(Optional.empty());
        
        vendorDocumentService.uploadDocument(1L, 999L, document);
    }
    
    // Test 15: ComplianceScoreService - Evaluate vendor compliance score
    @Test
    public void testEvaluateVendor_Success() {
        Vendor vendor = new Vendor("Acme Corp", "acme@example.com", "1234567890", "IT");
        vendor.setId(1L);
        
        DocumentType type1 = new DocumentType("Tax ID", "Tax identification document", true, 50);
        type1.setId(1L);
        
        DocumentType type2 = new DocumentType("Business License", "Business license document", true, 50);
        type2.setId(2L);
        
        VendorDocument doc1 = new VendorDocument(vendor, type1, "http://example.com/tax.pdf", LocalDate.now().plusDays(30));
        doc1.setIsValid(true);
        
        List<DocumentType> requiredTypes = List.of(type1, type2);
        List<VendorDocument> vendorDocs = List.of(doc1);
        
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(documentTypeRepository.findByRequiredTrue()).thenReturn(requiredTypes);
        when(vendorDocumentRepository.findByVendor(vendor)).thenReturn(vendorDocs);
        when(complianceScoreRepository.findByVendorId(1L)).thenReturn(Optional.empty());
        when(complianceScoreRepository.save(any(ComplianceScore.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        ComplianceScore result = complianceScoreService.evaluateVendor(1L);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getScoreValue() >= 0 && result.getScoreValue() <= 100);
        Assert.assertNotNull(result.getRating());
    }
    
    // Test 16: ComplianceScoreService - Missing vendor throws ResourceNotFoundException
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testEvaluateVendor_VendorNotFound() {
        when(vendorRepository.findById(999L)).thenReturn(Optional.empty());
        
        complianceScoreService.evaluateVendor(999L);
    }
    
    // Test 17: ComplianceScoringEngine - Calculate score with 100% compliance
    @Test
    public void testCalculateScore_FullCompliance() {
        double score = ComplianceScoringEngine.calculateScore(2, 2, 100, 100);
        Assert.assertEquals(score, 100.0, 0.01);
    }
    
    // Test 18: ComplianceScoringEngine - Calculate score with partial compliance
    @Test
    public void testCalculateScore_PartialCompliance() {
        double score = ComplianceScoringEngine.calculateScore(2, 1, 100, 50);
        Assert.assertTrue(score >= 0 && score <= 100);
    }
    
    // Test 19: ComplianceScoringEngine - Calculate score with no required documents
    @Test
    public void testCalculateScore_NoRequiredDocuments() {
        double score = ComplianceScoringEngine.calculateScore(0, 0, 0, 0);
        Assert.assertEquals(score, 100.0, 0.01);
    }
    
    // Test 20: ComplianceScoringEngine - Derive rating EXCELLENT
    @Test
    public void testDeriveRating_Excellent() {
        String rating = ComplianceScoringEngine.deriveRating(95.0);
        Assert.assertEquals(rating, "EXCELLENT");
    }
    
    // Test 21: ComplianceScoringEngine - Derive rating GOOD
    @Test
    public void testDeriveRating_Good() {
        String rating = ComplianceScoringEngine.deriveRating(75.0);
        Assert.assertEquals(rating, "GOOD");
    }
    
    // Test 22: ComplianceScoringEngine - Derive rating POOR
    @Test
    public void testDeriveRating_Poor() {
        String rating = ComplianceScoringEngine.deriveRating(55.0);
        Assert.assertEquals(rating, "POOR");
    }
    
    // Test 23: ComplianceScoringEngine - Derive rating NONCOMPLIANT
    @Test
    public void testDeriveRating_NonCompliant() {
        String rating = ComplianceScoringEngine.deriveRating(30.0);
        Assert.assertEquals(rating, "NONCOMPLIANT");
    }
    
    // Test 24: JwtUtil - Generate and validate token
    @Test
    public void testJwtUtil_GenerateAndValidate() {
        JwtUtil jwtUtil = new JwtUtil("mySecretKeyForJWTTokenGenerationAndValidationPurposeOnly123456789", 86400000);
        
        Authentication auth = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        String token = jwtUtil.generateToken(auth, 1L, "test@example.com", "USER");
        
        Assert.assertNotNull(token);
        Assert.assertTrue(jwtUtil.validateToken(token));
    }
    
    // Test 25: JwtUtil - Extract userId from token
    @Test
    public void testJwtUtil_ExtractUserId() {
        JwtUtil jwtUtil = new JwtUtil("mySecretKeyForJWTTokenGenerationAndValidationPurposeOnly123456789", 86400000);
        
        Authentication auth = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        String token = jwtUtil.generateToken(auth, 123L, "test@example.com", "ADMIN");
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        Assert.assertEquals(userId, Long.valueOf(123L));
    }
    
    // Test 26: JwtUtil - Extract role from token
    @Test
    public void testJwtUtil_ExtractRole() {
        JwtUtil jwtUtil = new JwtUtil("mySecretKeyForJWTTokenGenerationAndValidationPurposeOnly123456789", 86400000);
        
        Authentication auth = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        String token = jwtUtil.generateToken(auth, 1L, "test@example.com", "ADMIN");
        
        String role = jwtUtil.getRoleFromToken(token);
        Assert.assertEquals(role, "ADMIN");
    }
    
    // Test 27: User entity - PrePersist sets createdAt
    @Test
    public void testUserEntity_PrePersist() {
        User user = new User("John Doe", "john@example.com", "password123", "USER");
        user.onCreate();
        
        Assert.assertNotNull(user.getCreatedAt());
    }
    
    // Test 28: Vendor entity - PrePersist sets createdAt
    @Test
    public void testVendorEntity_PrePersist() {
        Vendor vendor = new Vendor("Acme Corp", "acme@example.com", "1234567890", "IT");
        vendor.onCreate();
        
        Assert.assertNotNull(vendor.getCreatedAt());
    }
    
    // Test 29: DocumentType entity - Weight validation
    @Test
    public void testDocumentTypeEntity_WeightValidation() {
        DocumentType type = new DocumentType("Tax ID", "Tax identification document", true, -10);
        type.onCreate();
        
        Assert.assertTrue(type.getWeight() >= 0);
    }
    
    // Test 30: VendorDocument entity - isValid calculation
    @Test
    public void testVendorDocumentEntity_IsValidCalculation() {
        Vendor vendor = new Vendor("Acme Corp", "acme@example.com", "1234567890", "IT");
        DocumentType type = new DocumentType("Tax ID", "Tax identification document", true, 10);
        
        VendorDocument document = new VendorDocument(vendor, type, "http://example.com/doc.pdf", LocalDate.now().plusDays(30));
        document.onCreate();
        
        Assert.assertNotNull(document.getIsValid());
        Assert.assertTrue(document.getIsValid());
    }
    
    // Test 31: HealthServlet - Constructor is public
    @Test
    public void testHealthServlet_Constructor() {
        HealthServlet servlet = new HealthServlet();
        Assert.assertNotNull(servlet);
    }
    
    // Test 32: ComplianceRuleService - Create rule
    @Test
    public void testCreateRule_Success() {
        ComplianceRule rule = new ComplianceRule("Rule1", "Description", "EXPIRYCHECK", 75.0);
        
        when(complianceRuleRepository.save(any(ComplianceRule.class))).thenReturn(rule);
        
        ComplianceRule result = complianceRuleService.createRule(rule);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getRuleName(), "Rule1");
    }
    
    // Test 33: ComplianceRuleService - Get all rules
    @Test
    public void testGetAllRules_Success() {
        List<ComplianceRule> rules = new ArrayList<>();
        rules.add(new ComplianceRule("Rule1", "Description1", "EXPIRYCHECK", 75.0));
        rules.add(new ComplianceRule("Rule2", "Description2", "WEIGHTEDSCORE", 80.0));
        
        when(complianceRuleRepository.findAll()).thenReturn(rules);
        
        List<ComplianceRule> result = complianceRuleService.getAllRules();
        
        Assert.assertEquals(result.size(), 2);
    }
    
    // Test 34: UserService - Constructor parameter order check
    @Test
    public void testUserServiceImpl_ConstructorOrder() {
        UserServiceImpl service = new UserServiceImpl(userRepository, passwordEncoder);
        Assert.assertNotNull(service);
    }
    
    // Test 35: VendorService - Constructor parameter order check
    @Test
    public void testVendorServiceImpl_ConstructorOrder() {
        VendorServiceImpl service = new VendorServiceImpl(vendorRepository);
        Assert.assertNotNull(service);
    }
    
    // Test 36: DocumentTypeService - Constructor parameter order check
    @Test
    public void testDocumentTypeServiceImpl_ConstructorOrder() {
        DocumentTypeServiceImpl service = new DocumentTypeServiceImpl(documentTypeRepository);
        Assert.assertNotNull(service);
    }
    
    // Test 37: VendorDocumentService - Constructor parameter order check
    @Test
    public void testVendorDocumentServiceImpl_ConstructorOrder() {
        VendorDocumentServiceImpl service = new VendorDocumentServiceImpl(
                vendorDocumentRepository, vendorRepository, documentTypeRepository);
        Assert.assertNotNull(service);
    }
    
    // Test 38: ComplianceRuleService - Constructor parameter order check
    @Test
    public void testComplianceRuleServiceImpl_ConstructorOrder() {
        ComplianceRuleServiceImpl service = new ComplianceRuleServiceImpl(complianceRuleRepository);
        Assert.assertNotNull(service);
    }
    
    // Test 39: ComplianceScoreService - Constructor parameter order check
    @Test
    public void testComplianceScoreServiceImpl_ConstructorOrder() {
        ComplianceScoreServiceImpl service = new ComplianceScoreServiceImpl(
                vendorRepository, documentTypeRepository, vendorDocumentRepository, complianceScoreRepository);
        Assert.assertNotNull(service);
    }
    
    // Test 40: VendorDocumentService - fileUrl validation
    @Test(expectedExceptions = ValidationException.class)
    public void testUploadDocument_MissingFileUrl() {
        Vendor vendor = new Vendor("Acme Corp", "acme@example.com", "1234567890", "IT");
        vendor.setId(1L);
        
        DocumentType type = new DocumentType("Tax ID", "Tax identification document", true, 10);
        type.setId(1L);
        
        VendorDocument document = new VendorDocument(vendor, type, null, LocalDate.now().plusDays(30));
        
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(documentTypeRepository.findById(1L)).thenReturn(Optional.of(type));
        
        vendorDocumentService.uploadDocument(1L, 1L, document);
    }
}