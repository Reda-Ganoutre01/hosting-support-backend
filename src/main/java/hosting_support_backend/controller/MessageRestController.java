package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.MessageRequestDTO;
import hosting_support_backend.dto.response.MessageResponseDTO;
import hosting_support_backend.entity.Message;
import hosting_support_backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageRestController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageResponseDTO>> getAll() {
        List<MessageResponseDTO> dtos = messageService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> getById(@PathVariable long id) {
        Message message = messageService.getById(id);
        return ResponseEntity.ok(toResponseDTO(message));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<MessageResponseDTO>> getByTicketId(@PathVariable long ticketId) {
        List<MessageResponseDTO> dtos = messageService.getByTicketId(ticketId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> create(@RequestBody MessageRequestDTO dto) {
        Message message = messageService.create(dto);
        return ResponseEntity.ok(toResponseDTO(message));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> update(@PathVariable long id, @RequestBody MessageRequestDTO dto) {
        Message message = messageService.update(id, dto);
        return ResponseEntity.ok(toResponseDTO(message));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MessageResponseDTO toResponseDTO(Message message) {
        return MessageResponseDTO.builder()
                .id(message.getId())
                .content(message.getContent())
                .sender(message.getSender())
                .sentAt(message.getSendAt())
                .ticketId(message.getTicket() != null ? message.getTicket().getId() : null)
                .build();
    }
}
