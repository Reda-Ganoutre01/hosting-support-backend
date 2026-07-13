package hosting_support_backend.dto.requests;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WorkflowLogRequestDTO {
  @NotBlank
    private String workflowName;

    @NotBlank
    private String executionStatus;

    @NotNull
    private LocalDateTime executionDate;
}
