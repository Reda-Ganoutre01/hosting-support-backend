package hosting_support_backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostingPlanResponseDTO {
  
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer storage;
    private Integer bandwidth;
    private Integer emailAccounts;
    private Boolean sslIncluded;

    // getters and setters
}
