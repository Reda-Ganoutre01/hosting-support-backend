package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.ContactRequestDTO;
import hosting_support_backend.dto.response.ContactResponseDTO;
import hosting_support_backend.entity.Contact;
import hosting_support_backend.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactRestController {

    private final ContactService contactService;

    // Public: submit a contact message from the client-facing form.
    @PostMapping
    public ResponseEntity<ContactResponseDTO> create(@Valid @RequestBody ContactRequestDTO dto) {
        Contact contact = contactService.create(dto);
        return ResponseEntity.ok(toResponseDTO(contact));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactResponseDTO>> getAll() {
        List<ContactResponseDTO> dtos = contactService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactResponseDTO> getById(@PathVariable long id) {
        return ResponseEntity.ok(toResponseDTO(contactService.getById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ContactResponseDTO toResponseDTO(Contact contact) {
        return ContactResponseDTO.builder()
                .id(contact.getId())
                .name(contact.getName())
                .email(contact.getEmail())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .userId(contact.getUserId())
                .createdAt(contact.getCreatedAt())
                .build();
    }
}