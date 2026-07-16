package hosting_support_backend.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowLogResponseDTO {
      private Long id;
    private String workflowName;
    private String executionStatus;
    private LocalDateTime executionDate;
}
