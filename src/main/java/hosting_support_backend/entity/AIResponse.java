package hosting_support_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  AIResponse {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column (length= 4000)
  private String prompt;

  @Column (length= 4000)
  private String response;

  private String provider;
  private Double confidenceScore;
  private LocalDateTime generatedAt;

  @OneToOne
  @JoinColumn(name = "ticket_id", nullable = false, unique = true)
  @JsonIgnoreProperties({"aiResponse", "messages", "user"})
  private Ticket ticket;

  @OneToOne
  @JoinColumn(name = "workflow_log_id")
  private WorkflowLog workflowLog;

  @OneToOne
  @JoinColumn(name = "faq_id")
  private FAQ faq;

  @PrePersist
  public void prePersist() {
    if (this.generatedAt == null) this.generatedAt = LocalDateTime.now();
  }
}
