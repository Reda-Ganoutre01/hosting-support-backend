package hosting_support_backend.repository;


import hosting_support_backend.entity.Message;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message ,Long> {
    List<Message> findByUser(User user);
    List<Message> findByTicketId(long TicketId);
}
