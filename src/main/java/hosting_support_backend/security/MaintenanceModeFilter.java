package hosting_support_backend.security;

import hosting_support_backend.service.MaintenanceModeService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MaintenanceModeFilter extends OncePerRequestFilter {

    public static final String MAINTENANCE_MESSAGE =
            "Le site est en maintenance. L'accès est réservé aux administrateurs.";

    private final MaintenanceModeService maintenanceModeService;

    public MaintenanceModeFilter(MaintenanceModeService maintenanceModeService) {
        this.maintenanceModeService = maintenanceModeService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (!maintenanceModeService.isMaintenanceEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String uri = request.getRequestURI();

        // Always let CORS preflight, auth, and the public status read through.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Auth endpoints are handled by AuthController which enforces the admin-only rule.
        if (uri.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Allow reading the maintenance status so clients can show a banner.
        if (uri.equals("/api/settings/maintenance") && "GET".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Non-API resources (swagger, actuator, static, error mapping) stay available,
        // but every real /api endpoint is locked down to administrators.
        if (!uri.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));

        if (isAdmin) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"message\":\"" + MAINTENANCE_MESSAGE + "\",\"maintenance\":true}"
        );
    }
}