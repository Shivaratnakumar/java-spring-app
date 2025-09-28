package net.javaguide.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic smoke test to ensure the application context loads successfully.
 * This test verifies that all beans are properly configured and the application starts.
 */
@SpringBootTest
@ActiveProfiles("test")
class SpringJwtAuthApplicationTests {

    @Test
    void contextLoads() {
        // This test passes if the Spring application context loads successfully
        // It catches configuration errors, missing beans, or circular dependencies
    }

    @Test
    void applicationStarts() {
        // If we reach this point, the application has started successfully
        // This includes all auto-configurations, security setup, and database connections
        assert true;
    }
}
