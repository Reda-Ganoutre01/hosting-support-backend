package hosting_support_backend.controller;

import hosting_support_backend.dto.response.UserResponseDTO;
import hosting_support_backend.entity.User;
import hosting_support_backend.entity.enums.Role;
import hosting_support_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;

    private User sampleUser() {
        return User.builder()
                .id(1L)
                .userName("reda")
                .fullName("Reda")
                .email("reda@example.com")
                .phone("+1-202-555-0173")
                .role(Role.ADMIN)
                .enabled(true)
                .build();
    }

    @Test
    void getAll_returnsDtos() {
        when(userService.getAll()).thenReturn(List.of(sampleUser()));

        ResponseEntity<List<UserResponseDTO>> response = userRestController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("reda@example.com", response.getBody().get(0).getEmail());
    }

    @Test
    void getById_returnsDto() {
        when(userService.getById(1L)).thenReturn(sampleUser());

        ResponseEntity<UserResponseDTO> response = userRestController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("reda", response.getBody().getUserName());
    }

    @Test
    void getByEmail_returnsDtoWhenFound() {
        when(userService.getByEmail("reda@example.com")).thenReturn(Optional.of(sampleUser()));

        ResponseEntity<UserResponseDTO> response = userRestController.getByEmail("reda@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("reda", response.getBody().getUserName());
    }

    @Test
    void getByEmail_returnsNotFoundWhenMissing() {
        when(userService.getByEmail("missing@example.com")).thenReturn(Optional.empty());

        ResponseEntity<UserResponseDTO> response = userRestController.getByEmail("missing@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void create_conflictWhenEmailAlreadyRegistered() {
        when(userService.getByEmail("reda@example.com")).thenReturn(Optional.of(sampleUser()));

        ResponseEntity<?> response = userRestController.create(sampleUser());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(userService, never()).create(any());
    }

    @Test
    void create_conflictWhenUsernameAlreadyRegistered() {
        when(userService.getByEmail(any())).thenReturn(Optional.empty());
        when(userService.getByUserName("reda")).thenReturn(Optional.of(sampleUser()));

        ResponseEntity<?> response = userRestController.create(sampleUser());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(userService, never()).create(any());
    }

    @Test
    void create_createsUser() {
        when(userService.getByEmail(any())).thenReturn(Optional.empty());
        when(userService.getByUserName(any())).thenReturn(Optional.empty());
        when(userService.create(any(User.class))).thenReturn(sampleUser());

        ResponseEntity<?> response = userRestController.create(sampleUser());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        UserResponseDTO dto = (UserResponseDTO) response.getBody();
        assertNotNull(dto);
        assertEquals("reda", dto.getUserName());
    }

    @Test
    void update_updatesExistingUser() {
        User existing = sampleUser();
        User input = User.builder()
                .id(1L)
                .userName("reda2")
                .fullName("Reda Deux")
                .email("reda@example.com")
                .build();
        User saved = User.builder()
                .id(1L)
                .userName("reda2")
                .fullName("Reda Deux")
                .email("reda@example.com")
                .role(Role.USER)
                .enabled(true)
                .build();

        when(userService.getById(1L)).thenReturn(existing);
        when(userService.getByUserName("reda2")).thenReturn(Optional.empty());
        when(userService.update(1L, input)).thenReturn(saved);

        ResponseEntity<?> response = userRestController.update(1L, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserResponseDTO dto = (UserResponseDTO) response.getBody();
        assertNotNull(dto);
        assertEquals("reda2", dto.getUserName());
    }

    @Test
    void update_returnsNotFoundWhenUserMissing() {
        when(userService.getById(99L)).thenReturn(null);

        ResponseEntity<?> response = userRestController.update(99L, sampleUser());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(userService).delete(1L);

        ResponseEntity<Void> response = userRestController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).delete(1L);
    }

    @Test
    void getPaginated_returnsPage() {
        when(userService.getPaginated(0, 10, null, null, null))
                .thenReturn(new PageImpl<>(List.of(sampleUser())));

        ResponseEntity<Page<UserResponseDTO>> response = userRestController.getPaginated(0, 10, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("reda", response.getBody().getContent().get(0).getUserName());
    }
}