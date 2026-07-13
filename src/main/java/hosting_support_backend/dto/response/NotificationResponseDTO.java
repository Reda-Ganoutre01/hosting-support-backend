package hosting_support_backend.dto.response;

import java.time.LocalDateTime;

public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String message;
    private Boolean read;
    private LocalDateTime createdAt;
    private Long userId;
    private Long hostingAccountId;
}
