package hosting_support_backend.entity;


import hosting_support_backend.entity.enums.HostingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table ( name = "hosting_accounts")
public class HostingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String domainName;
    HostingStatus status;
    LocalDate startDate;
    LocalDate expirationDate;
}
