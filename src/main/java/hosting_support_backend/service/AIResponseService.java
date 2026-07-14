package hosting_support_backend.service;

import hosting_support_backend.dto.requests.AIResponseRequestDTO;
import hosting_support_backend.entity.AIResponse;

import java.util.List;
import java.util.Optional;

public interface AIResponseService {
    AIResponse create(AIResponseRequestDTO dto);
    AIResponse update(Long id, AIResponseRequestDTO dto);
    void delete(Long id);
    AIResponse getById(Long id);
    List<AIResponse> getAll();
    Optional<AIResponse> getByTicketId(Long ticketId);
}
