package hosting_support_backend.entity;

import hosting_support_backend.entity.enums.Role;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class User {
    long id;
    String firstName;
    String lastName;
    String email;
    String password;
    String phone;
    Role role;
    Boolean enabled;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
