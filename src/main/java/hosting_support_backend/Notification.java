package hosting_support_backend;


import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Notification {
    Long id;
    String title;
    String message;
    Boolean read;
    LocalDateTime createdAt;
}
