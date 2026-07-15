package hosting_support_backend.service;

import hosting_support_backend.dto.requests.AIResponseRequestDTO;
import hosting_support_backend.entity.AIResponse;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.repository.AIResponseRepository;
import hosting_support_backend.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AIResponseServiceImpl implements AIResponseService {

    private final AIResponseRepository aiResponseRepository;
    private final TicketRepository ticketRepository;

    @Override
    public AIResponse create(AIResponseRequestDTO dto) {
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + dto.getTicketId()));

        AIResponse aiResponse = AIResponse.builder()
                .prompt(dto.getPrompt())
                .response(dto.getResponse())
                .provider(dto.getProvider())
                .confidenceScore(dto.getConfidenceScore())
                .ticket(ticket)
                .build();

        return aiResponseRepository.save(aiResponse);
    }

    @Override
    public AIResponse update(Long id, AIResponseRequestDTO dto) {
        AIResponse existing = aiResponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AIResponse not found with id: " + id));

        existing.setPrompt(dto.getPrompt());
        existing.setResponse(dto.getResponse());
        existing.setProvider(dto.getProvider());
        existing.setConfidenceScore(dto.getConfidenceScore());

        return aiResponseRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!aiResponseRepository.existsById(id)) {
            throw new RuntimeException("AIResponse not found with id: " + id);
        }
        aiResponseRepository.deleteById(id);
    }

    @Override
    public AIResponse getById(Long id) {
        return aiResponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AIResponse not found with id: " + id));
    }

    @Override
    public List<AIResponse> getAll() {
        return aiResponseRepository.findAll();
    }

    @Override
    public Optional<AIResponse> getByTicketId(Long ticketId) {
        return aiResponseRepository.findAll().stream()
                .filter(ar -> ar.getTicket().getId().equals(ticketId))
                .findFirst();
    }
}
