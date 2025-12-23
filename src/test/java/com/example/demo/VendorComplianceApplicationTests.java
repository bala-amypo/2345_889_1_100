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
        testUser = new User();
        testUser.setEmail("user@example.com");
        testUser.setId(1L);
    }

    @Test
    public void testJwtToken() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(testUser.getEmail());

        String token = jwtUtil.generateToken(auth, testUser.getId(), testUser.getEmail(), "USER");
        assertNotNull(token);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(testUser.getEmail());

        assertTrue(jwtUtil.validateToken(token, userDetails));

        assertEquals(testUser.getId(), jwtUtil.getUserIdFromToken(token));
        assertEquals("USER", jwtUtil.getRoleFromToken(token));
    }

    @Test
    public void testUserServiceMock() {
        when(userService.findById(1L)).thenReturn(testUser);

        User user = userService.findById(1L);
        assertEquals("user@example.com", user.getEmail());
    }
}
