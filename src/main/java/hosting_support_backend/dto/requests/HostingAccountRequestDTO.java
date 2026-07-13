package hosting_support_backend.dto.requests;

import java.time.LocalDate;

import hosting_support_backend.entity.enums.HostingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HostingAccountRequestDTO {

    @NotBlank
    private String domainName;

    private HostingStatus status;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private Long userId;

    @NotNull
    private Long hostingPlanId;
}
