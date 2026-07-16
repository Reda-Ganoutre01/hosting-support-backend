package hosting_support_backend.dto.response;

import java.time.LocalDate;
import hosting_support_backend.entity.enums.HostingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostingAccountResponseDTO {
    private Long id;
    private String domainName;
    private HostingStatus status;
    private LocalDate startDate;
    private LocalDate expirationDate;
    private Long userId;
    private Long hostingPlanId;
}
