package hosting_support_backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResponseRequestDTO {
    @NotBlank(message = "Prompt cannot be blank")
    private String prompt;

    private String response;

    private String provider;

    private Double confidenceScore;

    private Long ticketId;

    private Long workflowLogId;
    private Long faqId;
}
