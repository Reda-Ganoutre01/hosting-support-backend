package hosting_support_backend.entity;

import hosting_support_backend.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String firstName;

    String lastName;
    @Column(unique = true)
    String email;

    String password;

    String phone;


    @Enumerated(EnumType.STRING)
    Role role;
    Boolean enabled;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
