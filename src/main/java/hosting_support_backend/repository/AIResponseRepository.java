package hosting_support_backend.repository;

import hosting_support_backend.entity.AIResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AIResponseRepository extends JpaRepository<AIResponse , Long> {
    Optional <AIResponse> findByTicketId(Long ticketId);
}
