package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.NotificationRequestDTO;
import hosting_support_backend.dto.response.NotificationResponseDTO;
import hosting_support_backend.entity.Notification;
import hosting_support_backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationRestController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAll() {
        List<NotificationResponseDTO> dtos = notificationService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getById(@PathVariable long id) {
        Notification notification = notificationService.getById(id);
        return ResponseEntity.ok(toResponseDTO(notification));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getByUserId(@PathVariable long userId) {
        List<NotificationResponseDTO> dtos = notificationService.getByUserId(userId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> create(@RequestBody NotificationRequestDTO dto) {
        Notification notification = notificationService.create(dto);
        return ResponseEntity.ok(toResponseDTO(notification));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> update(@PathVariable long id, @RequestBody NotificationRequestDTO dto) {
        Notification notification = notificationService.update(id, dto);
        return ResponseEntity.ok(toResponseDTO(notification));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private NotificationResponseDTO toResponseDTO(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .userId(notification.getUser() != null ? notification.getUser().getId() : null)
                .hostingAccountId(notification.getHostingAccount() != null ? notification.getHostingAccount().getId() : null)
                .build();
    }
}
