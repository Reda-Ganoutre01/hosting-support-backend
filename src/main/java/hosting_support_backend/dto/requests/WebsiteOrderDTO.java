package hosting_support_backend.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteOrderDTO {
    private Long id;
    private String domainName;
    private String siteType;
    private Double price;
    private String period;
    private String status;

    private String productCount;
    private String productVolume;
    private String productRanges;
    private String productSpecs;

    private Boolean hasVideoCompany;
    private Boolean hasVideoPromo;
    private Boolean hasVideo360;

    private LocalDate orderDate;

    private Long userId;
    private String userName;
    private String userEmail;
}
