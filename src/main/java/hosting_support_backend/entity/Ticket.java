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

  // @ManyToOne(fetch = FetchType.LAZY)
  //   @JoinColumn(name = "user_id", nullable = false)
  //   private User user;

  //   @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
  //   private List<Message> messages;

  //   @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
  //   private AIResponse aiResponse;

  //   @PrePersist
  //   public void prePersist() {
  //       this.createdAt = LocalDateTime.now();
  //       if (this.status == null) this.status = TicketStatus.OPEN;
  //       if (this.priority == null) this.priority = Priority.MEDIUM;
  //   }
}
