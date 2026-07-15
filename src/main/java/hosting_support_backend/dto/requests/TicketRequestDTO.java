package hosting_support_backend.dto.requests;

import hosting_support_backend.entity.enums.Priority;
import hosting_support_backend.entity.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TicketRequestDTO {

    @NotBlank
    private String subject;

    @NotBlank
    private String description;

    private TicketStatus status;
    private Priority priority;

    @NotNull
    private Long userId;

}