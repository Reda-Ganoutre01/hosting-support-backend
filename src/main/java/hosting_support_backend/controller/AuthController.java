package hosting_support_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hosting_support_backend.dto.requests.LoginRequest;
import hosting_support_backend.dto.requests.UserRequestDTO;
import hosting_support_backend.dto.response.AuthResponse;
import hosting_support_backend.entity.User;
import hosting_support_backend.security.JwtTokenProvider;
import hosting_support_backend.service.UserService;
import jakarta.validation.Valid;

// AuthController.java - Authentication endpoints
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            UserService userService,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // POST /api/auth/login - Authenticate and return JWT
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        // Authenticate with Spring Security using email instead of username
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Set the authentication in context
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        // Generate JWT token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername()).orElse(null);
        Long userId = user != null ? user.getId() : null;
        String token = tokenProvider.generateToken(userDetails, userId);

        return ResponseEntity.ok(new AuthResponse(token));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequestDTO registerRequestDto) {
        if (userService.getByEmail(registerRequestDto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email is already registered");
        }

        if (userService.getByUserName(registerRequestDto.getUserName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username is already registered");
        }

        User user = User.builder()
                .fullName(registerRequestDto.getFullName())
                .userName(registerRequestDto.getUserName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .phone(registerRequestDto.getPhone())
                .role(registerRequestDto.getRole())
                .enabled(true)
                .build();

        User createdUser = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
