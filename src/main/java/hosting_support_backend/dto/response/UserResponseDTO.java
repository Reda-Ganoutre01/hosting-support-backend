package hosting_support_backend.dto.response;


import hosting_support_backend.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private long id;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private Boolean enabled;
}
