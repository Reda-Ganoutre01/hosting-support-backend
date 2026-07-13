package hosting_support_backend.dto.response;

import java.time.LocalDateTime;

import hosting_support_backend.entity.enums.SenderType;

public class MessageResponseDTO {

    private Long id;
    private String content;
    private SenderType sender;
    private LocalDateTime sentAt;
    private Long ticketId;

    // getters and setters
}
