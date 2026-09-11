package hosting_support_backend.controller;

import hosting_support_backend.service.MaintenanceModeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingRestController {

    private final MaintenanceModeService maintenanceModeService;

    public SettingRestController(MaintenanceModeService maintenanceModeService) {
        this.maintenanceModeService = maintenanceModeService;
    }

    // Public read (also reachable while maintenance is active) so clients can display a banner.
    @GetMapping("/maintenance")
    public ResponseEntity<Map<String, Object>> getMaintenanceMode() {
        return ResponseEntity.ok(Map.of(
                "enabled", maintenanceModeService.isMaintenanceEnabled()
        ));
    }

    @PutMapping("/maintenance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> setMaintenanceMode(@RequestBody Map<String, Boolean> body) {
        boolean enabled = Boolean.TRUE.equals(body.get("enabled"));
        maintenanceModeService.setMaintenanceEnabled(enabled);
        return ResponseEntity.ok(Map.of(
                "enabled", maintenanceModeService.isMaintenanceEnabled()
        ));
    }
}