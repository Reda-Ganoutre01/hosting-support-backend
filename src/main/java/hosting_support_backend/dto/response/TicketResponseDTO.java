package hosting_support_backend.dto.response;

import java.time.LocalDateTime;

import hosting_support_backend.entity.enums.Priority;
import hosting_support_backend.entity.enums.TicketStatus;

public class TicketResponseDTO {
private Long id;
    private String subject;
    private String description;
    private TicketStatus status;
    private Priority priority;
    private LocalDateTime createdAt;
    private Long userId;


}
