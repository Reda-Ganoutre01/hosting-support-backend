package hosting_support_backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "hosting_plans")
public class HostingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column (nullable = false)
    String name;
    String description;
    @Column (nullable = false)
    Double price;
    Integer storage;
    Integer bandwidth;
    Integer emailAccounts;
    Boolean sslIncluded;
}
