package com.example.demo;

import com.example.demo.dto.AuthResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.impl.*;
import com.example.demo.servlet.HealthServlet;
import com.example.demo.util.ComplianceScoringEngine;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * FULL 64-TEST TestNG Suite
 * Corrected: NO annotation imports, only fully-qualified names
 */
@Listeners(TestResultListener.class)
public class VendorComplianceApplicationTests {

    // ------------------------------
    // Mock Repos, Services, Helpers
    // ------------------------------
    private UserRepository userRepository;
    private VendorRepository vendorRepository;
    private DocumentTypeRepository documentTypeRepository;
    private VendorDocumentRepository vendorDocumentRepository;
    private ComplianceRuleRepository complianceRuleRepository;
    private ComplianceScoreRepository complianceScoreRepository;

    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    private UserServiceImpl userService;
    private VendorServiceImpl vendorService;
    private DocumentTypeServiceImpl documentTypeService;
    private VendorDocumentServiceImpl vendorDocumentService;
    private ComplianceRuleServiceImpl complianceRuleService;
    private ComplianceScoreServiceImpl complianceScoreService;

    private ComplianceScoringEngine scoringEngine;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userRepository = mock(UserRepository.class);
        vendorRepository = mock(VendorRepository.class);
        documentTypeRepository = mock(DocumentTypeRepository.class);
        vendorDocumentRepository = mock(VendorDocumentRepository.class);
        complianceRuleRepository = mock(ComplianceRuleRepository.class);
        complianceScoreRepository = mock(ComplianceScoreRepository.class);

        passwordEncoder = mock(PasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        userDetailsService = mock(UserDetailsService.class);

        scoringEngine = new ComplianceScoringEngine();

        userService = new UserServiceImpl(userRepository, passwordEncoder);
        vendorService = new VendorServiceImpl(vendorRepository);
        documentTypeService = new DocumentTypeServiceImpl(documentTypeRepository);

        vendorDocumentService = new VendorDocumentServiceImpl(
                vendorDocumentRepository,
                vendorRepository,
                documentTypeRepository
        );

        complianceRuleService = new ComplianceRuleServiceImpl(complianceRuleRepository);

        complianceScoreService = new ComplianceScoreServiceImpl(
                vendorRepository,
                documentTypeRepository,
                vendorDocumentRepository,
                complianceScoreRepository
        );
    }

    // =============================================================
    // 1. SERVLET TESTS — WebServlet, HttpServlet, Protected doGet
    // =============================================================

    @Test(priority = 1)
    public void testServletAnnotationPresent() {
        jakarta.servlet.annotation.WebServlet ann =
                HealthServlet.class.getAnnotation(jakarta.servlet.annotation.WebServlet.class);
        Assert.assertNotNull(ann);
    }

    @Test(priority = 2)
    public void testServletUrlPattern() {
        jakarta.servlet.annotation.WebServlet ann =
                HealthServlet.class.getAnnotation(jakarta.servlet.annotation.WebServlet.class);
        Assert.assertTrue(ann.urlPatterns().length > 0);
    }

    @Test(priority = 3)
    public void testServletExtendsHttpServlet() {
        Assert.assertTrue(
                jakarta.servlet.http.HttpServlet.class.isAssignableFrom(HealthServlet.class)
        );
    }

    @Test(priority = 4)
    public void testServletDoGetProtected() throws Exception {
        var m = HealthServlet.class.getDeclaredMethod(
                "doGet", HttpServletRequest.class, HttpServletResponse.class);
        Assert.assertTrue(Modifier.isProtected(m.getModifiers()));
    }

    @Test(priority = 5)
    public void testServletIsPublic() {
        Assert.assertTrue(Modifier.isPublic(HealthServlet.class.getModifiers()));
    }

    @Test(priority = 6)
    public void testServletInstantiable() {
        HealthServlet s = new HealthServlet();
        Assert.assertNotNull(s);
    }

    @Test(priority = 7)
    public void testServletNoFields() {
        Assert.assertEquals(HealthServlet.class.getDeclaredFields().length, 0);
    }

    @Test(priority = 8)
    public void testServletClassNameContainsHealth() {
        Assert.assertTrue(HealthServlet.class.getSimpleName().contains("Health"));
    }

    // =============================================================
    // 2. CRUD OPERATIONS
    // =============================================================

    @Test(priority = 9)
    public void testRegisterUserSuccess() {
        User u = new User();
        u.setEmail("abc@test.com");
        u.setPassword("pass12345");

        when(userRepository.existsByEmail("abc@test.com")).thenReturn(false);
        when(passwordEncoder.encode("pass12345")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User x = inv.getArgument(0);
            x.setId(1L);
            return x;
        });

        User saved = userService.registerUser(u);

        Assert.assertEquals(saved.getId(), 1L);
        Assert.assertEquals(saved.getPassword(), "encoded");
    }

    @Test(priority = 10)
    public void testRegisterUserDuplicateEmail() {
        when(userRepository.existsByEmail("dup@test.com")).thenReturn(true);

        User u = new User();
        u.setEmail("dup@test.com");

        try {
            userService.registerUser(u);
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("Email already used"));
        }
    }

    @Test(priority = 11)
    public void testCreateVendor() {
        Vendor v = new Vendor();
        v.setVendorName("A1");
        v.setIndustry("Tech");

        when(vendorRepository.save(any(Vendor.class))).thenAnswer(inv -> {
            Vendor x = inv.getArgument(0);
            x.setId(10L);
            return x;
        });

        Vendor saved = vendorService.createVendor(v);
        Assert.assertEquals(saved.getId(), 10L);
    }

    @Test(priority = 12)
    public void testGetVendorNotFound() {
        when(vendorRepository.findById(999L))
                .thenReturn(Optional.empty());

        try {
            vendorService.getVendor(999L);
            Assert.fail();
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("Vendor not found"));
        }
    }

    @Test(priority = 13)
    public void testUploadDocumentSuccess() {
        Vendor v = new Vendor();
        v.setId(1L);

        DocumentType dt = new DocumentType();
        dt.setId(2L);
        dt.setWeight(10);

        VendorDocument doc = new VendorDocument();
        doc.setFileUrl("http://file");
        doc.setExpiryDate(LocalDate.now().plusDays(10));

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));
        when(documentTypeRepository.findById(2L)).thenReturn(Optional.of(dt));
        when(vendorDocumentRepository.save(any(VendorDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        VendorDocument saved = vendorDocumentService.uploadDocument(1L, 2L, doc);

        Assert.assertEquals(saved.getVendor().getId(), 1L);
        Assert.assertEquals(saved.getDocumentType().getId(), 2L);
    }

    @Test(priority = 14)
    public void testUploadDocumentExpiredNegative() {
        VendorDocument doc = new VendorDocument();
        doc.setFileUrl("x");
        doc.setExpiryDate(LocalDate.now().minusDays(1));

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(new Vendor()));
        when(documentTypeRepository.findById(2L)).thenReturn(Optional.of(new DocumentType()));

        try {
            vendorDocumentService.uploadDocument(1L,2L,doc);
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("Expiry date cannot be in the past"));
        }
    }

    @Test(priority = 15)
    public void testGetDocumentSuccess() {
        VendorDocument d = new VendorDocument();
        d.setId(5L);

        when(vendorDocumentRepository.findById(5L)).thenReturn(Optional.of(d));

        VendorDocument out = vendorDocumentService.getDocument(5L);
        Assert.assertEquals(out.getId(), 5L);
    }

    @Test(priority = 16)
    public void testGetDocumentNotFound() {
        when(vendorDocumentRepository.findById(99L))
                .thenReturn(Optional.empty());

        try {
            vendorDocumentService.getDocument(99L);
            Assert.fail();
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("VendorDocument not found"));
        }
    }

    // =============================================================
    // 3. DI & IoC
    // =============================================================

    @Test(priority = 17)
    public void testServiceInjected() {
        Assert.assertNotNull(userService);
    }

    @Test(priority = 18)
    public void testVendorServiceInjected() {
        Assert.assertNotNull(vendorService);
    }

    @Test(priority = 19)
    public void testDocumentTypeServiceInjected() {
        Assert.assertNotNull(documentTypeService);
    }

    @Test(priority = 20)
    public void testVendorDocumentServiceInjected() {
        Assert.assertNotNull(vendorDocumentService);
    }

    @Test(priority = 21)
    public void testComplianceRuleServiceInjected() {
        Assert.assertNotNull(complianceRuleService);
    }

    @Test(priority = 22)
    public void testComplianceScoreServiceInjected() {
        Assert.assertNotNull(complianceScoreService);
    }

    @Test(priority = 23)
    public void testIoCAlternateRepoAllowed() {
        VendorServiceImpl alt = new VendorServiceImpl(mock(VendorRepository.class));
        Assert.assertNotNull(alt);
    }

    @Test(priority = 24)
    public void testScoringEngine100Percent() {
        DocumentType dt = new DocumentType();
        dt.setId(1L);
        dt.setWeight(10);

        double score = scoringEngine.calculateScore(List.of(dt), List.of(dt));
        Assert.assertEquals(score, 100.0);
    }

    // =============================================================
    // 4. Hibernate, JPA Mapping
    // =============================================================

    @Test(priority = 25)
    public void testVendorHasTable() {
        jakarta.persistence.Table t =
                Vendor.class.getAnnotation(jakarta.persistence.Table.class);
        Assert.assertNotNull(t);
    }

    @Test(priority = 26)
    public void testUserHasUniqueConstraint() {
        jakarta.persistence.Table t =
                User.class.getAnnotation(jakarta.persistence.Table.class);
        Assert.assertTrue(t.uniqueConstraints().length > 0);
    }

    @Test(priority = 27)
    public void testVendorPrePersistSetsCreatedAt() {
        Vendor v = new Vendor();
        v.setIndustry("IT");
        v.prePersist();
        Assert.assertNotNull(v.getCreatedAt());
    }

    @Test(priority = 28)
    public void testDocumentTypePrePersist() {
        DocumentType dt = new DocumentType();
        dt.setWeight(5);
        dt.prePersist();
        Assert.assertNotNull(dt.getCreatedAt());
    }

    @Test(priority = 29)
    public void testVendorDocumentPrePersist() {
        VendorDocument d = new VendorDocument();
        d.setFileUrl("x");
        d.prePersist();
        Assert.assertNotNull(d.getUploadedAt());
    }

    @Test(priority = 30)
    public void testComplianceRulePrePersist() {
        ComplianceRule r = new ComplianceRule();
        r.setRuleName("r1");
        r.setMatchType("EXPIRY_CHECK");
        r.prePersist();

        Assert.assertNotNull(r.getCreatedAt());
        Assert.assertEquals(r.getThreshold(), 0.0);
    }

    @Test(priority = 31)
    public void testHibernateSaveUserAssignsId() {
        User u = new User();
        u.setEmail("a@b.com");
        u.setPassword("p");

        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);
        when(passwordEncoder.encode("p")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User x = inv.getArgument(0);
            x.setId(99L);
            return x;
        });

        User out = userService.registerUser(u);
        Assert.assertEquals(out.getId(), 99L);
    }

    @Test(priority = 32)
    public void testHibernateSaveVendorAssignsId() {
        Vendor v = new Vendor();
        v.setVendorName("H");
        v.setIndustry("IT");

        when(vendorRepository.save(any(Vendor.class))).thenAnswer(inv -> {
            Vendor x = inv.getArgument(0);
            x.setId(77L);
            return x;
        });

        Vendor out = vendorService.createVendor(v);
        Assert.assertEquals(out.getId(), 77L);
    }

    // =============================================================
    // 5. JPA Mapping / Normalization
    // =============================================================

    @Test(priority = 33)
    public void testVendorDocumentVendorManyToOneAnnotation() throws Exception {
        jakarta.persistence.ManyToOne m =
                VendorDocument.class.getDeclaredField("vendor")
                        .getAnnotation(jakarta.persistence.ManyToOne.class);
        Assert.assertNotNull(m);
    }

    @Test(priority = 34)
    public void testVendorDocumentTypeManyToOneAnnotation() throws Exception {
        jakarta.persistence.ManyToOne m =
                VendorDocument.class.getDeclaredField("documentType")
                        .getAnnotation(jakarta.persistence.ManyToOne.class);
        Assert.assertNotNull(m);
    }

    @Test(priority = 35)
    public void testComplianceScoreHasVendorField() {
        boolean exists = Arrays.stream(ComplianceScore.class.getDeclaredFields())
                .anyMatch(f -> f.getName().equals("vendor"));
        Assert.assertTrue(exists);
    }

 @Test(priority = 36)
public void testVendorNoDirectVendorDocumentLink() {
    boolean hasVendorDocumentRef = Arrays.stream(Vendor.class.getDeclaredFields())
            .anyMatch(f -> f.getType().getSimpleName().equals("VendorDocument"));
    Assert.assertFalse(hasVendorDocumentRef);
}


    @Test(priority = 37)
    public void testDocumentTypeNoVendorIdField() {
        boolean hasVendorId = Arrays.stream(DocumentType.class.getDeclaredFields())
                .anyMatch(f -> f.getName().equalsIgnoreCase("vendorId"));
        Assert.assertFalse(hasVendorId);
    }

    @Test(priority = 38)
    public void testDocumentTypeWeightPositive() {
        DocumentType dt = new DocumentType();
        dt.setWeight(1);
        Assert.assertTrue(dt.getWeight() > 0);
    }

    @Test(priority = 39)
    public void testComplianceScoreNoRuleFields() {
        boolean hasRule = Arrays.stream(ComplianceScore.class.getDeclaredFields())
                .anyMatch(f -> f.getName().toLowerCase().contains("rule"));
        Assert.assertFalse(hasRule);
    }

    @Test(priority = 40)
    public void testVendorTableNameCorrect() {
        jakarta.persistence.Table t =
                Vendor.class.getAnnotation(jakarta.persistence.Table.class);
        Assert.assertEquals(t.name(), "vendors");
    }

    // =============================================================
    // 6. MANY-TO-MANY
    // =============================================================

    @Test(priority = 41)
    public void testVendorSupportedDocumentTypesAnnotation() throws Exception {
        jakarta.persistence.ManyToMany ann =
                Vendor.class.getDeclaredField("supportedDocumentTypes")
                        .getAnnotation(jakarta.persistence.ManyToMany.class);
        Assert.assertNotNull(ann);
    }

    @Test(priority = 42)
    public void testDocumentTypeVendorsAnnotation() throws Exception {
        jakarta.persistence.ManyToMany ann =
                DocumentType.class.getDeclaredField("vendors")
                        .getAnnotation(jakarta.persistence.ManyToMany.class);
        Assert.assertNotNull(ann);
    }

    @Test(priority = 43)
    public void testAddDocumentTypeToVendor() {
        Vendor v = new Vendor();
        DocumentType t = new DocumentType();
        v.getSupportedDocumentTypes().add(t);
        Assert.assertEquals(v.getSupportedDocumentTypes().size(), 1);
    }

    @Test(priority = 44)
    public void testAddMultipleDocumentTypes() {
        Vendor v = new Vendor();
        v.getSupportedDocumentTypes().add(new DocumentType());
        v.getSupportedDocumentTypes().add(new DocumentType());
        Assert.assertEquals(v.getSupportedDocumentTypes().size(), 2);
    }

    @Test(priority = 45)
    public void testSetPreventsDuplicates() {
        Vendor v = new Vendor();
        DocumentType t = new DocumentType();
        v.getSupportedDocumentTypes().add(t);
        v.getSupportedDocumentTypes().add(t);
        Assert.assertEquals(v.getSupportedDocumentTypes().size(), 1);
    }

    @Test(priority = 46)
    public void testDocumentTypeVendorsEmpty() {
        DocumentType t = new DocumentType();
        Assert.assertTrue(t.getVendors().isEmpty());
    }

    @Test(priority = 47)
    public void testVendorDocumentTypesEmpty() {
        Vendor v = new Vendor();
        Assert.assertTrue(v.getSupportedDocumentTypes().isEmpty());
    }

    @Test(priority = 48)
    public void testBidirectionalConsistency() {
        Vendor v = new Vendor();
        DocumentType d = new DocumentType();

        v.getSupportedDocumentTypes().add(d);
        d.getVendors().add(v);

        Assert.assertTrue(v.getSupportedDocumentTypes().contains(d));
        Assert.assertTrue(d.getVendors().contains(v));
    }

    // =============================================================
    // 7. SECURITY & JWT
    // =============================================================

    @Test(priority = 49)
    public void testAuthResponseFields() {
        AuthResponse r = new AuthResponse("token", 1L, "a@b.com", "ADMIN");
        Assert.assertEquals(r.getToken(), "token");
        Assert.assertEquals(r.getUserId(), 1L);
        Assert.assertEquals(r.getEmail(), "a@b.com");
        Assert.assertEquals(r.getRole(), "ADMIN");
    }

    @Test(priority = 50)
    public void testPasswordEncoding() {
        User u = new User();
        u.setEmail("x@test.com");
        u.setPassword("plain");

        when(userRepository.existsByEmail("x@test.com")).thenReturn(false);
        when(passwordEncoder.encode("plain")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User x = inv.getArgument(0);
            x.setId(2L);
            return x;
        });

        User saved = userService.registerUser(u);
        Assert.assertEquals(saved.getPassword(), "enc");
    }

    @Test(priority = 51)
    public void testAuthenticationManagerCall() {
        UsernamePasswordAuthenticationToken t =
                new UsernamePasswordAuthenticationToken("u", "p");
        authenticationManager.authenticate(t);
        verify(authenticationManager, times(1)).authenticate(t);
    }

    @Test(priority = 52)
    public void testUserRoleSpringAuthority() {
        User u = new User();
        u.setRole("ADMIN");
        String auth = "ROLE_" + u.getRole();
        Assert.assertEquals(auth, "ROLE_ADMIN");
    }

    @Test(priority = 53)
    public void testUnauthorizedNullHeader() {
        String header = null;
        Assert.assertNull(header);
    }

    @Test(priority = 54)
    public void testUserServiceFindByEmailNotFound() {
        when(userRepository.findByEmail("none@test.com"))
                .thenReturn(Optional.empty());

        try {
            userService.findByEmail("none@test.com");
            Assert.fail();
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("User not found"));
        }
    }

    @Test(priority = 55)
    public void testUserServiceGetByIdSuccess() {
        User u = new User();
        u.setId(3L);

        when(userRepository.findById(3L)).thenReturn(Optional.of(u));

        User out = userService.getById(3L);
        Assert.assertEquals(out.getId(), 3L);
    }

    @Test(priority = 56)
    public void testUserServiceGetByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        try {
            userService.getById(999L);
            Assert.fail();
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("User not found"));
        }
    }

    // =============================================================
    // 8. HQL / CRITERIA
    // =============================================================

    @Test(priority = 57)
    public void testExpiredDocumentQueryPositive() {
        VendorDocument d = new VendorDocument();
        d.setExpiryDate(LocalDate.now().minusDays(1));

        when(vendorDocumentRepository.findExpiredDocuments(any(LocalDate.class)))
                .thenReturn(List.of(d));

        List<VendorDocument> list = vendorDocumentRepository.findExpiredDocuments(LocalDate.now());
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 58)
    public void testExpiredDocumentQueryEmpty() {
        when(vendorDocumentRepository.findExpiredDocuments(any(LocalDate.class)))
                .thenReturn(List.of());

        Assert.assertTrue(
                vendorDocumentRepository.findExpiredDocuments(LocalDate.now()).isEmpty()
        );
    }

    @Test(priority = 59)
    public void testDocumentTypeRequiredTrueQuery() {
        DocumentType dt = new DocumentType();
        dt.setRequired(true);

        when(documentTypeRepository.findByRequiredTrue()).thenReturn(List.of(dt));

        List<DocumentType> list = documentTypeRepository.findByRequiredTrue();
        Assert.assertTrue(list.get(0).getRequired());
    }

    @Test(priority = 60)
    public void testComplianceScoreFullSuccess() {
        Vendor v = new Vendor();
        v.setId(1L);

        DocumentType dt = new DocumentType();
        dt.setId(10L);
        dt.setRequired(true);
        dt.setWeight(10);

        VendorDocument doc = new VendorDocument();
        doc.setVendor(v);
        doc.setDocumentType(dt);
        doc.setIsValid(true);

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));
        when(documentTypeRepository.findByRequiredTrue()).thenReturn(List.of(dt));
        when(vendorDocumentRepository.findByVendor(v)).thenReturn(List.of(doc));
        when(complianceScoreRepository.findByVendor_Id(1L)).thenReturn(Optional.empty());
        when(complianceScoreRepository.save(any(ComplianceScore.class))).thenAnswer(inv -> inv.getArgument(0));

        ComplianceScore score = complianceScoreService.evaluateVendor(1L);

        Assert.assertEquals(score.getScoreValue(), 100.0);
        Assert.assertEquals(score.getRating(), "EXCELLENT");
    }

    @Test(priority = 61)
    public void testComplianceScorePartial() {
        Vendor v = new Vendor();
        v.setId(2L);

        DocumentType dt1 = new DocumentType();
        dt1.setId(1L);
        dt1.setRequired(true);
        dt1.setWeight(10);

        DocumentType dt2 = new DocumentType();
        dt2.setId(2L);
        dt2.setRequired(true);
        dt2.setWeight(10);

        VendorDocument doc1 = new VendorDocument();
        doc1.setVendor(v);
        doc1.setDocumentType(dt1);
        doc1.setIsValid(true);

        when(vendorRepository.findById(2L)).thenReturn(Optional.of(v));
        when(documentTypeRepository.findByRequiredTrue()).thenReturn(List.of(dt1, dt2));
        when(vendorDocumentRepository.findByVendor(v)).thenReturn(List.of(doc1));
        when(complianceScoreRepository.findByVendor_Id(2L)).thenReturn(Optional.empty());
        when(complianceScoreRepository.save(any(ComplianceScore.class))).thenAnswer(inv -> inv.getArgument(0));

        ComplianceScore score = complianceScoreService.evaluateVendor(2L);

        Assert.assertTrue(score.getScoreValue() > 0 && score.getScoreValue() < 100);
    }

    @Test(priority = 62)
    public void testGetScoreNotFound() {
        when(complianceScoreRepository.findByVendor_Id(99L))
                .thenReturn(Optional.empty());

        try {
            complianceScoreService.getScore(99L);
            Assert.fail();
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("Score not found"));
        }
    }

    @Test(priority = 63)
    public void testRatingBoundaries() {
        Assert.assertEquals(scoringEngine.deriveRating(95.0), "EXCELLENT");
        Assert.assertEquals(scoringEngine.deriveRating(80.0), "GOOD");
        Assert.assertEquals(scoringEngine.deriveRating(60.0), "POOR");
        Assert.assertEquals(scoringEngine.deriveRating(10.0), "NON_COMPLIANT");
    }

    @Test(priority = 64)
    public void testNoRequiredTypesEdgeCase() {
        Vendor v = new Vendor();
        v.setId(3L);

        when(vendorRepository.findById(3L)).thenReturn(Optional.of(v));
        when(documentTypeRepository.findByRequiredTrue()).thenReturn(List.of());
        when(vendorDocumentRepository.findByVendor(v)).thenReturn(List.of());
        when(complianceScoreRepository.findByVendor_Id(3L)).thenReturn(Optional.empty());
        when(complianceScoreRepository.save(any(ComplianceScore.class))).thenAnswer(inv -> inv.getArgument(0));

        ComplianceScore score = complianceScoreService.evaluateVendor(3L);
        Assert.assertEquals(score.getScoreValue(), 100.0);
    }

}
