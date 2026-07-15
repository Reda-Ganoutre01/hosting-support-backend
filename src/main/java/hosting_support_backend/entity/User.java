package hosting_support_backend.entity;

import hosting_support_backend.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;


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
    private long id;

    private String userName;

    private String fullName;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private String phone;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'USER'")

    private Role role;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<HostingAccount> hostingAccounts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Notification> notifications;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.enabled == null) {
            this.enabled = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    
    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities(){
    //     return List.of(new SimpleGrantedAuthority("ROLE_" +role.name()));
    // }

    // @Override
    // public boolean isEnabled(){
    //     return enabled!=null && enabled;
    // }

}
