package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.LoginRequest;
import hosting_support_backend.dto.requests.UserRequestDTO;
import hosting_support_backend.dto.response.AuthResponse;
import hosting_support_backend.entity.User;
import hosting_support_backend.entity.enums.Role;
import hosting_support_backend.security.JwtTokenProvider;
import hosting_support_backend.service.MaintenanceModeService;
import hosting_support_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MaintenanceModeService maintenanceModeService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest(String email) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword("12345678");
        return request;
    }

    private UserDetails adminDetails() {
        return org.springframework.security.core.userdetails.User
                .withUsername("reda@example.com")
                .password("$2a$secret")
                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                .build();
    }

    private Authentication authenticated(UserDetails details) {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(details);
        return authentication;
    }

    private UserRequestDTO registerRequest() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setFullName("New User");
        dto.setUserName("newuser");
        dto.setEmail("new@example.com");
        dto.setPassword("secret123");
        dto.setPhone("0600000000");
        dto.setRole(Role.USER);
        return dto;
    }

    @Test
    void login_withValidCredentials_returnsJwt() {
        User admin = User.builder().id(1L).email("reda@example.com").role(Role.ADMIN).build();
        UserDetails details = adminDetails();
        Authentication authentication = authenticated(details);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.getByEmail("reda@example.com")).thenReturn(Optional.of(admin));
        when(maintenanceModeService.isMaintenanceEnabled()).thenReturn(false);
        when(tokenProvider.generateToken(details, 1L)).thenReturn("jwt-token");

        ResponseEntity<?> response = authController.login(loginRequest("reda@example.com"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse body = (AuthResponse) response.getBody();
        assertNotNull(body);
        assertEquals("jwt-token", body.getToken());
    }

    @Test
    void login_isBlockedDuringMaintenanceForNonAdmin() {
        User client = User.builder().id(2L).email("client@example.com").role(Role.USER).build();
        Authentication authentication = authenticated(adminDetails());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.getByEmail("reda@example.com")).thenReturn(Optional.of(client));
        when(maintenanceModeService.isMaintenanceEnabled()).thenReturn(true);

        ResponseEntity<?> response = authController.login(loginRequest("reda@example.com"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(true, body.get("maintenance"));
    }

    @Test
    void login_allowedDuringMaintenanceForAdmin() {
        User admin = User.builder().id(1L).email("reda@example.com").role(Role.ADMIN).build();
        UserDetails details = adminDetails();
        Authentication authentication = authenticated(details);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.getByEmail("reda@example.com")).thenReturn(Optional.of(admin));
        when(maintenanceModeService.isMaintenanceEnabled()).thenReturn(true);
        when(tokenProvider.generateToken(details, 1L)).thenReturn("jwt-token");

        ResponseEntity<?> response = authController.login(loginRequest("reda@example.com"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void register_isBlockedDuringMaintenance() {
        when(maintenanceModeService.isMaintenanceEnabled()).thenReturn(true);

        ResponseEntity<?> response = authController.register(registerRequest());

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    void register_conflictWhenEmailAlreadyRegistered() {
        when(maintenanceModeService.isMaintenanceEnabled()).thenReturn(false);
        when(userService.getByEmail("new@example.com")).thenReturn(Optional.of(User.builder().build()));

        ResponseEntity<?> response = authController.register(registerRequest());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void register_conflictWhenUsernameAlreadyRegistered() {
        when(maintenanceModeService.isMaintenanceEnabled()).thenReturn(false);
        when(userService.getByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userService.getByUserName("newuser")).thenReturn(Optional.of(User.builder().build()));

        ResponseEntity<?> response = authController.register(registerRequest());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void register_createsNewUser() {
        when(maintenanceModeService.isMaintenanceEnabled()).thenReturn(false);
        when(userService.getByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userService.getByUserName("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("$2a$encoded");

        User saved = User.builder().id(10L).fullName("New User").build();
        when(userService.create(any(User.class))).thenReturn(saved);

        ResponseEntity<?> response = authController.register(registerRequest());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }

    @Test
    void register_hasNoRolesClaimWhenAuthoritiesEmpty() {
        User admin = User.builder().id(1L).email("reda@example.com").role(Role.ADMIN).build();
        UserDetails details = org.springframework.security.core.userdetails.User
                .withUsername("reda@example.com")
                .password("$2a$secret")
                .authorities(Collections.emptyList())
                .build();
        Authentication authentication = authenticated(details);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.getByEmail("reda@example.com")).thenReturn(Optional.of(admin));
        when(maintenanceModeService.isMaintenanceEnabled()).thenReturn(false);
        when(tokenProvider.generateToken(details, 1L)).thenReturn("jwt-token");

        ResponseEntity<?> response = authController.login(loginRequest("reda@example.com"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}