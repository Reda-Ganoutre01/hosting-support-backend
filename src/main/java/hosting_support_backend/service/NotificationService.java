package hosting_support_backend.service;

import hosting_support_backend.dto.requests.NotificationRequestDTO;
import hosting_support_backend.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification create(NotificationRequestDTO dto);
    Notification update(Long id, NotificationRequestDTO dto);
    void delete(Long id);
    Notification getById(Long id);
    List<Notification> getAll();
    List<Notification> getByUserId(Long userId);
}
