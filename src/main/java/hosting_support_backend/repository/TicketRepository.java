package hosting_support_backend.repository;

import hosting_support_backend.entity.Ticket;
import hosting_support_backend.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByStatus (TicketStatus status);
    List<Ticket> findByUserId (Long userId);
    List<Ticket> findBySubjectContainingIgnoreCase(String subject);
}
