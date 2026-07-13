package hosting_support_backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AIResponseRequestDTO {
   @NotBlank
    private String prompt;

    @NotBlank
    private String response;

    @NotBlank
    private String provider;

    private Double confidenceScore;

    @NotNull
    private Long ticketId;

    private Long workflowLogId;
    private Long faqId;
}
