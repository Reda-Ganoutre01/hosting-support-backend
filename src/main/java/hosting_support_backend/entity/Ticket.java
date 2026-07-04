package hosting_support_backend.entity;

import hosting_support_backend.entity.enums.Priority;
import hosting_support_backend.entity.enums.TicketStatus;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Ticket {
    Long id;
    String subject;
    String description;
    TicketStatus status;
    Priority priority;
    LocalDateTime createdAt;
}
