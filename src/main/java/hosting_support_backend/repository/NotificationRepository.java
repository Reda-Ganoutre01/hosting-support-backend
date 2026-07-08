package hosting_support_backend.repository;

import hosting_support_backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByReadFalse();
    List<Notification> findByUserIdAndReadFalse(Long userId);
}
