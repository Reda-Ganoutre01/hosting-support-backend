package hosting_support_backend.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private Long userId;
    private LocalDateTime createdAt;
}