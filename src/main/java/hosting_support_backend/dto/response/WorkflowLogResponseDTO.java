package hosting_support_backend.dto.response;

import java.time.LocalDateTime;

public class WorkflowLogResponseDTO {
      private Long id;
    private String workflowName;
    private String executionStatus;
    private LocalDateTime executionDate;
}
