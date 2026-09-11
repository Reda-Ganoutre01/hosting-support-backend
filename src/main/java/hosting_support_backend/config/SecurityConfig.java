package hosting_support_backend.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import hosting_support_backend.security.JwtAuthenticationFilter;
import hosting_support_backend.security.MaintenanceModeFilter;

// SecurityConfig.java - Spring Security configuration
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final MaintenanceModeFilter maintenanceModeFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, MaintenanceModeFilter maintenanceModeFilter) {

        this.jwtFilter = jwtFilter;
        this.maintenanceModeFilter = maintenanceModeFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Disable CSRF since we use stateless JWT tokens
                .csrf(csrf -> csrf.disable())
                // Set session management to stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                // Configure endpoint authorization
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        // Public read of maintenance status (used to show the banner on login/register)
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/settings/maintenance").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/hostingAccounts/**").permitAll()
                        .requestMatchers("/api/hostingPlans/**").permitAll()
                        .requestMatchers("/api/tickets/**").permitAll()
                        .requestMatchers("/api/messages/**").permitAll()
                        .requestMatchers("/api/faqs/**").permitAll()
                        .requestMatchers("/api/aiResponses/**").permitAll()
                        .requestMatchers("/api/notifications/**").permitAll()
                        .requestMatchers("/api/workflowLogs/**").permitAll()
                        .requestMatchers("/actuator/health/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // Add JWT filter before the standard auth filter, then gate /api traffic
                // behind the maintenance filter (which must run after authentication).
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(maintenanceModeFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowedOriginPatterns(java.util.List.of("*"));
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(java.util.List.of("*"));
        config.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // Password encoder for hashing passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Prevent Spring Boot from registering the filter outside Spring Security
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration =
                new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    // Prevent Spring Boot from auto-registering the maintenance filter as a
    // plain servlet filter so it only runs inside the security chain,
    // after the JWT filter has populated the security context.
    @Bean
    public FilterRegistrationBean<MaintenanceModeFilter> maintenanceModeFilterRegistration(
            MaintenanceModeFilter filter) {
        FilterRegistrationBean<MaintenanceModeFilter> registration =
                new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    // Authentication manager for login endpoint
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
