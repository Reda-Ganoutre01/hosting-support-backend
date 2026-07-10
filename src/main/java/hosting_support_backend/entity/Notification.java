package hosting_support_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@Table(name = "notifications")
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;

  @Column(length = 4000,nullable = false)


  private String message;
  @Column(name = "is_read")
  private Boolean read;
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnoreProperties({"tickets", "hostingAccounts", "notifications"})
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hosting_account_id")
  @JsonIgnoreProperties({"notifications", "user"})
  private HostingAccount hostingAccount;

  @PrePersist
  public void prePersist() {
      this.createdAt = LocalDateTime.now();
      if (this.read == null) {
          this.read = false;
      }
  }
}
