package hosting_support_backend.dto.requests;

import jakarta.validation.constraints.NotBlank;

public class FAQRequestDTO {
   @NotBlank
    private String question;

    @NotBlank
    private String answer;

    private String category;

}
