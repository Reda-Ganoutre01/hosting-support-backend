package hosting_support_backend.entity;

import hosting_support_backend.entity.enums.Priority;
import hosting_support_backend.entity.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table ( name = 'tickets')
public class Ticket {

    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String subject;
    String description;
    TicketStatus status;
    Priority priority;
    LocalDateTime createdAt;
}
