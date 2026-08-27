package hosting_support_backend.service;

import hosting_support_backend.dto.requests.MessageRequestDTO;
import hosting_support_backend.entity.Message;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.entity.User;
import hosting_support_backend.repository.MessageRepository;
import hosting_support_backend.repository.TicketRepository;
import hosting_support_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public Message create(MessageRequestDTO dto) {
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + dto.getTicketId()));

        User user = null;
        if (dto.getUserId() != null && dto.getUserId() > 0) {
            user = userRepository.findById(dto.getUserId()).orElse(null);
        }
        if (user == null) {
            user = ticket.getUser();
        }
        if (user == null) {
            user = userRepository.findAll().stream().findFirst().orElse(null);
        }

        hosting_support_backend.entity.enums.SenderType sender = dto.getSender() != null ? dto.getSender() : hosting_support_backend.entity.enums.SenderType.USER;

        Message message = Message.builder()
                .content(dto.getContent())
                .sender(sender)
                .ticket(ticket)
                .user(user)
                .build();

        return messageRepository.save(message);
    }

    @Override
    public Message update(Long id, MessageRequestDTO dto) {
        Message existing = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));

        existing.setContent(dto.getContent());

        return messageRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
    }

    @Override
    public Message getById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getByTicketId(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));
        return messageRepository.findAll().stream()
                .filter(m -> m.getTicket().getId().equals(ticketId))
                .toList();
    }
}
