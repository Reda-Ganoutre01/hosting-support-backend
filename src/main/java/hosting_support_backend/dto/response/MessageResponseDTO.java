package hosting_support_backend.dto.response;

import java.time.LocalDateTime;

import hosting_support_backend.entity.enums.SenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {

    private Long id;
    private String content;
    private SenderType sender;
    private LocalDateTime sentAt;
    private Long ticketId;
}
