package hosting_support_backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FAQRequestDTO {
   @NotBlank
    private String question;

    @NotBlank
    private String answer;

    private String category;

}
