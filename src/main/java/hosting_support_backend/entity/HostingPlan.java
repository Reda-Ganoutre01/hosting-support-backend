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
    private long id;

    @Column (nullable = false)
    private String name;

    @Column(length =  2000)
    private String description;
    
    @Column (nullable = false)
    private Double price;
    private Integer storage;
    private Integer bandwidth;
    private Integer emailAccounts;
    private Boolean sslIncluded;
}
