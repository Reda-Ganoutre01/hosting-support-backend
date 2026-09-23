package hosting_support_backend.service;

import hosting_support_backend.entity.User;
import hosting_support_backend.entity.enums.Role;
import hosting_support_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User.UserBuilder sampleUser() {
        return User.builder()
                .userName("reda")
                .fullName("Reda")
                .email("reda@example.com")
                .password("secret123");
    }

    @Test
    void create_encodesPlainPasswordAndAppliesDefaults() {
        when(passwordEncoder.encode("secret123")).thenReturn("$2a$encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User created = userService.create(sampleUser().build());

        assertEquals("$2a$encoded", created.getPassword());
        assertEquals(Role.USER, created.getRole());
        assertEquals(Boolean.TRUE, created.getEnabled());
        verify(userRepository).save(created);
    }

    @Test
    void create_usesDefaultPasswordWhenBlank() {
        when(passwordEncoder.encode("12345678")).thenReturn("$2a$default");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User created = userService.create(sampleUser().password("   ").build());

        assertEquals("$2a$default", created.getPassword());
    }

    @Test
    void create_doesNotReencodeAlreadyHashedPassword() {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User created = userService.create(sampleUser().password("$2a$already-hashed").build());

        assertEquals("$2a$already-hashed", created.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void create_keepsExplicitRoleAndEnabledState() {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User created = userService.create(sampleUser().role(Role.ADMIN).enabled(false).build());

        assertEquals(Role.ADMIN, created.getRole());
        assertEquals(Boolean.FALSE, created.getEnabled());
    }

    @Test
    void update_mergesFieldsAndEncodesPlainPassword() {
        User existing = sampleUser().id(1L).password("$2a$old").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newpass")).thenReturn("$2a$new");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = userService.update(1L, sampleUser()
                .fullName("Reda Deux")
                .password("newpass")
                .role(Role.ADMIN)
                .enabled(false)
                .build());

        assertEquals("Reda Deux", updated.getFullName());
        assertEquals("$2a$new", updated.getPassword());
        assertEquals(Role.ADMIN, updated.getRole());
        assertEquals(Boolean.FALSE, updated.getEnabled());
    }

    @Test
    void update_throwsWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.update(99L, sampleUser().build()));
    }

    @Test
    void delete_throwsWhenUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.delete(99L));
        verify(userRepository, never()).deleteById(99L);
    }

    @Test
    void delete_removesExistingUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void getById_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser().id(1L).build()));

        User user = userService.getById(1L);

        assertEquals("reda", user.getUserName());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getById(1L));
    }

    @Test
    void getAll_delegatesToRepository() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser().build()));

        assertEquals(1, userService.getAll().size());
    }

    @Test
    void getPaginated_usesSortOrderDesc() {
        Page<User> page = new PageImpl<>(List.of(sampleUser().id(1L).build()));
        when(userRepository.findByFilters("reda", Role.USER, true, PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "id")))).thenReturn(page);

        Page<User> result = userService.getPaginated(0, 5, "reda", "user", true);

        assertEquals(1, result.getTotalElements());
        verify(userRepository).findByFilters("reda", Role.USER, true, PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "id")));
    }

    @Test
    void getPaginated_ignoresInvalidRoleValue() {
        Page<User> page = new PageImpl<>(List.of());
        when(userRepository.findByFilters("reda", null, null, PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "id")))).thenReturn(page);

        Page<User> result = userService.getPaginated(0, 5, "reda", "NOT_A_ROLE", null);

        assertEquals(0, result.getTotalElements());
        verify(userRepository).findByFilters("reda", null, null, PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "id")));
    }

    @Test
    void getByEmail_delegatesToRepository() {
        when(userRepository.findByEmail("reda@example.com"))
                .thenReturn(Optional.of(sampleUser().build()));

        assertTrue(userService.getByEmail("reda@example.com").isPresent());
    }

    @Test
    void getByUserName_delegatesToRepository() {
        when(userRepository.findByUserName("reda")).thenReturn(Optional.of(sampleUser().build()));

        assertTrue(userService.getByUserName("reda").isPresent());
    }
}