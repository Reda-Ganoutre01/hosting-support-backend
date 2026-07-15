package hosting_support_backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationRequestDTO {
@NotBlank
    private String title;

    @NotBlank
    private String message;

    @NotNull
    private Long userId;

    private Long hostingAccountId;
}
