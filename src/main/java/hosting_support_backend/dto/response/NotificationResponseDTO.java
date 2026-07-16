package hosting_support_backend.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String message;
    private Boolean read;
    private LocalDateTime createdAt;
    private Long userId;
    private Long hostingAccountId;
}
