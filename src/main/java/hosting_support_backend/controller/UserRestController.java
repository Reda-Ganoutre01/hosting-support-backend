package hosting_support_backend.controller;


import hosting_support_backend.dto.response.UserResponseDTO;
import hosting_support_backend.entity.User;
import hosting_support_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {


    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        List<UserResponseDTO> dtos = userService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable("id") long id){
        User user = userService.getById(id);
        return ResponseEntity.ok(toResponseDTO(user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getByEmail(@PathVariable("email") String email){
        return  userService.getByEmail(email)
                .map(user -> ResponseEntity.ok(toResponseDTO(user)))
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user){
        try {
            if (user.getEmail() != null && userService.getByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT)
                        .body("Email '" + user.getEmail() + "' is already registered.");
            }
            if (user.getUserName() != null && userService.getByUserName(user.getUserName()).isPresent()) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT)
                        .body("Username '" + user.getUserName() + "' is already registered.");
            }
            User createdUser = userService.create(user);
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(toResponseDTO(createdUser));
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody User user){
        try {
            User existing = userService.getById(id);
            if (existing == null) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                        .body("User not found with id: " + id);
            }

            if (user.getEmail() != null && !user.getEmail().equalsIgnoreCase(existing.getEmail())) {
                var byEmail = userService.getByEmail(user.getEmail());
                if (byEmail.isPresent() && byEmail.get().getId() != id) {
                    return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT)
                            .body("Email '" + user.getEmail() + "' is already used by another account.");
                }
            }

            if (user.getUserName() != null && !user.getUserName().equalsIgnoreCase(existing.getUserName())) {
                var byUsername = userService.getByUserName(user.getUserName());
                if (byUsername.isPresent() && byUsername.get().getId() != id) {
                    return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT)
                            .body("Username '" + user.getUserName() + "' is already used by another account.");
                }
            }

            User updatedUser = userService.update(id, user);
            return ResponseEntity.ok(toResponseDTO(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  delete(@PathVariable("id") long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }
}
