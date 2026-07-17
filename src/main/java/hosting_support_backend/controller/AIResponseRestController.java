package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.AIResponseRequestDTO;
import hosting_support_backend.dto.response.AIResponseResponseDTO;
import hosting_support_backend.entity.AIResponse;
import hosting_support_backend.service.AIResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/aiResponses")
@RequiredArgsConstructor
public class AIResponseRestController {

    private final AIResponseService aiResponseService;

    @GetMapping
    public ResponseEntity<List<AIResponseResponseDTO>> getAll() {
        List<AIResponseResponseDTO> dtos = aiResponseService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AIResponseResponseDTO> getById(@PathVariable long id) {
        AIResponse aiResponse = aiResponseService.getById(id);
        return ResponseEntity.ok(toResponseDTO(aiResponse));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<AIResponseResponseDTO> getByTicketId(@PathVariable long ticketId) {
        return aiResponseService.getByTicketId(ticketId)
                .map(this::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AIResponseResponseDTO> create(@RequestBody AIResponseRequestDTO dto) {
        AIResponse aiResponse = aiResponseService.create(dto);
        return ResponseEntity.ok(toResponseDTO(aiResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AIResponseResponseDTO> update(@PathVariable long id, @RequestBody AIResponseRequestDTO dto) {
        AIResponse aiResponse = aiResponseService.update(id, dto);
        return ResponseEntity.ok(toResponseDTO(aiResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        aiResponseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private AIResponseResponseDTO toResponseDTO(AIResponse aiResponse) {
        return AIResponseResponseDTO.builder()
                .id(aiResponse.getId())
                .prompt(aiResponse.getPrompt())
                .response(aiResponse.getResponse())
                .provider(aiResponse.getProvider())
                .confidenceScore(aiResponse.getConfidenceScore())
                .generatedAt(aiResponse.getGeneratedAt())
                .ticketId(aiResponse.getTicket() != null ? aiResponse.getTicket().getId() : null)
                .workflowLogId(aiResponse.getWorkflowLog() != null ? aiResponse.getWorkflowLog().getId() : null)
                .faqId(aiResponse.getFaq() != null ? aiResponse.getFaq().getId() : null)
                .build();
    }
}
