package net.javaguide.spring.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OAuth2 integration.
 * This class is prepared for future OAuth2 implementation.
 *
 * To enable OAuth2 in the future:
 * 1. Add spring-boot-starter-oauth2-client dependency
 * 2. Uncomment and configure the methods below
 * 3. Update WebSecurityConfig to include OAuth2 login
 */
@Configuration
public class OAuthConfig {

    // Uncomment when ready to implement OAuth2
    /*
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("your-google-client-id")
                .clientSecret("your-google-client-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v3/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }
    */
}
