package hosting_support_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQResponseDTO {
    private Long id;
    private String question;
    private String answer;
    private String category;

}
