package hosting_support_backend.service;

import hosting_support_backend.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);
    User update(Long id, User user);
    void delete(Long id);
    User getById(Long id);
    List<User> getAll();
    Optional<User> getByEmail(String email);
}
