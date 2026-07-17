package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.FAQRequestDTO;
import hosting_support_backend.dto.response.FAQResponseDTO;
import hosting_support_backend.entity.FAQ;
import hosting_support_backend.service.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
public class FAQRestController {

    private final FAQService faqService;

    @GetMapping
    public ResponseEntity<List<FAQResponseDTO>> getAll() {
        List<FAQResponseDTO> dtos = faqService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FAQResponseDTO> getById(@PathVariable long id) {
        FAQ faq = faqService.getById(id);
        return ResponseEntity.ok(toResponseDTO(faq));
    }

    @PostMapping
    public ResponseEntity<FAQResponseDTO> create(@RequestBody FAQRequestDTO dto) {
        FAQ faq = faqService.create(dto);
        return ResponseEntity.ok(toResponseDTO(faq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FAQResponseDTO> update(@PathVariable long id, @RequestBody FAQRequestDTO dto) {
        FAQ faq = faqService.update(id, dto);
        return ResponseEntity.ok(toResponseDTO(faq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        faqService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private FAQResponseDTO toResponseDTO(FAQ faq) {
        return FAQResponseDTO.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .category(faq.getCategory())
                .build();
    }
}
