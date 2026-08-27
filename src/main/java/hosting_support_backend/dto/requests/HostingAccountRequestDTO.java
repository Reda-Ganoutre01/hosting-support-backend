package hosting_support_backend.dto.requests;

import java.time.LocalDate;

import hosting_support_backend.entity.enums.HostingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HostingAccountRequestDTO {

    private String domainName;
    private HostingStatus status;
    private LocalDate startDate;
    private LocalDate expirationDate;
    private Long userId;
    private Long hostingPlanId;
}
