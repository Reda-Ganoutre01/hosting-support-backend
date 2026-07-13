package hosting_support_backend.dto.response;

import java.time.LocalDateTime;

public class AIResponseResponseDTO {
  private Long id;
    private String prompt;
    private String response;
    private String provider;
    private Double confidenceScore;
    private LocalDateTime generatedAt;
    private Long ticketId;
    private Long workflowLogId;
    private Long faqId;
}
