package hosting_support_backend.service;

import hosting_support_backend.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface UserService {
    User create(User user);
    User update(Long id, User user);
    void delete(Long id);
    User getById(Long id);
    List<User> getAll();
    Page<User> getPaginated(int page, int size, String search, String role, Boolean enabled);
    Optional<User> getByEmail(String email);
    Optional<User> getByUserName(String userName);
}
