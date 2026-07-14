package hosting_support_backend.service;

import hosting_support_backend.dto.requests.MessageRequestDTO;
import hosting_support_backend.entity.Message;

import java.util.List;

public interface MessageService {
    Message create(MessageRequestDTO dto);
    Message update(Long id, MessageRequestDTO dto);
    void delete(Long id);
    Message getById(Long id);
    List<Message> getAll();
    List<Message> getByTicketId(Long ticketId);
}
