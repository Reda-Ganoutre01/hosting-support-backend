package hosting_support_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "website_orders")
public class WebsiteOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String domainName;

    @Column(nullable = false)
    private String siteType; // e.g. "Création Site E-commerce PrestaShop", "Formule Mojoud 1 An"

    private Double price;

    private String period; // "One Time", "Annual"

    @Column(nullable = false)
    private String status; // "ACTIVE", "PENDING", "COMPLETED"

    private String productCount;
    private String productVolume;
    private String productRanges;
    private String productSpecs;

    private Boolean hasVideoCompany;
    private Boolean hasVideoPromo;
    private Boolean hasVideo360;

    private LocalDate orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnoreProperties({"tickets", "hostingAccounts", "notifications", "password"})
    private User user;
}
