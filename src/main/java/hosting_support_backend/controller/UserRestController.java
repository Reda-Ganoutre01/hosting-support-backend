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
    public ResponseEntity<UserResponseDTO> getById(@PathVariable long id){
        User user = userService.getById(id);
        return ResponseEntity.ok(toResponseDTO(user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getByEmail(@PathVariable String email){
        return  userService.getByEmail(email)
                .map(user -> ResponseEntity.ok(toResponseDTO(user)))
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO>  create(@RequestBody User user){
        User createdUser = userService.create(user);
        return ResponseEntity.ok(toResponseDTO(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO>  update(@PathVariable long id,@RequestBody User user){
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(toResponseDTO(updatedUser));
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
