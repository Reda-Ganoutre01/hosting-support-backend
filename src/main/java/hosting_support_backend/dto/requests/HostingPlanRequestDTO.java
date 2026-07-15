package hosting_support_backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HostingPlanRequestDTO {
@NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Double price;

    @NotNull
    private Integer storage;

    @NotNull
    private Integer bandwidth;

    @NotNull
    private Integer emailAccounts;

    @NotNull
    private Boolean sslIncluded;

    // getters and setters
}
