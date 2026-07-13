package hosting_support_backend.dto.requests;

import hosting_support_backend.entity.enums.SenderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageRequestDTO {
  @NotBlank
    private String content;

    @NotNull
    private SenderType sender;

    @NotNull
    private Long ticketId;

    // getters and setters
}
