package net.javaguide.spring.controller;

import net.javaguide.spring.entity.Role;
import net.javaguide.spring.entity.User;
import net.javaguide.spring.repository.UserRepository;
import net.javaguide.spring.security.JwtUtils;
import net.javaguide.spring.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@AutoConfigureWebMvc
public class TestControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;
    private String userToken;
    private String adminToken;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.deleteAll();
        createTestUsersAndTokens();
    }

    private void createTestUsersAndTokens() {
        // Create regular user
        User regularUser = new User("user", "user@example.com",
                passwordEncoder.encode("password"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(Role.USER);
        regularUser.setRoles(userRoles);
        userRepository.save(regularUser);

        // Create admin user
        User adminUser = new User("admin", "admin@example.com",
                passwordEncoder.encode("password"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(Role.ADMIN);
        adminUser.setRoles(adminRoles);
        userRepository.save(adminUser);

        // Generate tokens
        UserPrincipal userPrincipal = UserPrincipal.create(regularUser);
        Authentication userAuth = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());
        userToken = jwtUtils.generateJwtToken(userAuth);

        UserPrincipal adminPrincipal = UserPrincipal.create(adminUser);
        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
                adminPrincipal, null, adminPrincipal.getAuthorities());
        adminToken = jwtUtils.generateJwtToken(adminAuth);
    }

    @Test
    void testPublicEndpoint() throws Exception {
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("Public Content."));
    }

    @Test
    void testUserEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUserEndpointWithValidToken() throws Exception {
        mockMvc.perform(get("/api/test/user")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().string("User Content."));
    }

    @Test
    void testUserEndpointWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/test/user")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAdminEndpointWithUserToken() throws Exception {
        mockMvc.perform(get("/api/test/admin")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAdminEndpointWithAdminToken() throws Exception {
        mockMvc.perform(get("/api/test/admin")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin Board."));
    }

    @Test
    void testModeratorEndpointWithoutRole() throws Exception {
        mockMvc.perform(get("/api/test/mod")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testMalformedToken() throws Exception {
        mockMvc.perform(get("/api/test/user")
                        .header("Authorization", "Bearer malformed.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testMissingBearerPrefix() throws Exception {
        mockMvc.perform(get("/api/test/user")
                        .header("Authorization", userToken))
                .andExpect(status().isUnauthorized());
    }
}
