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
@Table(name = "tickets")
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String subject;

  @Column (length=200)
  private String description;

  @Enumerated(EnumType.STRING)
  private TicketStatus status;

  @Enumerated(EnumType.STRING)
  private Priority priority;
  private LocalDateTime createdAt;
}
