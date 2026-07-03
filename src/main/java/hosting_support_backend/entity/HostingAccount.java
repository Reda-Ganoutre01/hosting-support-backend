package hosting_support_backend.entity;


import hosting_support_backend.entity.enums.HostingStatus;
import jakarta.persistence.Entity;

import java.time.LocalDate;


@Entity
public class HostingAccount {
    Long id;
    String domainName;
    HostingStatus status;
    LocalDate startDate;
    LocalDate expirationDate;
}
