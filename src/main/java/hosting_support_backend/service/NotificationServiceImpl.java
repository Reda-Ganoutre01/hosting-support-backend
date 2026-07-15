package hosting_support_backend.service;

import hosting_support_backend.dto.requests.NotificationRequestDTO;
import hosting_support_backend.entity.HostingAccount;
import hosting_support_backend.entity.Notification;
import hosting_support_backend.entity.User;
import hosting_support_backend.repository.HostingAccountRepository;
import hosting_support_backend.repository.NotificationRepository;
import hosting_support_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final HostingAccountRepository hostingAccountRepository;

    @Override
    public Notification create(NotificationRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        HostingAccount hostingAccount = hostingAccountRepository.findById(dto.getHostingAccountId())
                .orElseThrow(() -> new RuntimeException("HostingAccount not found with id: " + dto.getHostingAccountId()));

        Notification notification = Notification.builder()
                .message(dto.getMessage())
                .title(dto.getTitle())
                .read(dto.getRead())
                .user(user)
                .hostingAccount(hostingAccount)
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Long id, NotificationRequestDTO dto) {
        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        existing.setMessage(dto.getMessage());
        existing.setTitle(dto.getTitle());
        existing.setRead(dto.getRead());

        return notificationRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    public Notification getById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

   @Override
public List<Notification> getByUserId(Long userId) {
    if (!userRepository.existsById(userId)) {
        throw new RuntimeException("User not found with id: " + userId);
    }
    return notificationRepository.findByUserId(userId);
}
}
