package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.TicketRequestDTO;
import hosting_support_backend.dto.response.TicketResponseDTO;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketRestController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAll() {
        List<TicketResponseDTO> dtos = ticketService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getById(@PathVariable long id) {
        Ticket ticket = ticketService.getById(id);
        return ResponseEntity.ok(toResponseDTO(ticket));
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> create(@RequestBody TicketRequestDTO dto) {
        Ticket ticket = ticketService.create(dto);
        return ResponseEntity.ok(toResponseDTO(ticket));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> update(@PathVariable long id, @RequestBody TicketRequestDTO dto) {
        Ticket ticket = ticketService.update(id, dto);
        return ResponseEntity.ok(toResponseDTO(ticket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TicketResponseDTO toResponseDTO(Ticket ticket) {
        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .subject(ticket.getSubject())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .createdAt(ticket.getCreatedAt())
                .userId(ticket.getUser() != null ? ticket.getUser().getId() : null)
                .build();
    }
}
