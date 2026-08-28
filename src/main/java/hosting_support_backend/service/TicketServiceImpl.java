package hosting_support_backend.service;

import hosting_support_backend.dto.requests.TicketRequestDTO;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.entity.User;
import hosting_support_backend.repository.TicketRepository;
import hosting_support_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public Ticket create(TicketRequestDTO dto) {
        Long targetUserId = dto.getUserId();
        User user = null;
        if (targetUserId != null) {
            user = userRepository.findById(targetUserId).orElse(null);
        }
        if (user == null) {
            user = userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No user available to create ticket"));
        }

        Ticket ticket = Ticket.builder()
                .subject(dto.getSubject())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? dto.getStatus() : hosting_support_backend.entity.enums.TicketStatus.OPEN)
                .priority(dto.getPriority() != null ? dto.getPriority() : hosting_support_backend.entity.enums.Priority.MEDIUM)
                .user(user)
                .build();

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket update(Long id, TicketRequestDTO dto) {
        Ticket existing = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        existing.setSubject(dto.getSubject());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setPriority(dto.getPriority());

        return ticketRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public Ticket getById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }

    @Override
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> getByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return ticketRepository.findByUserId(userId);
    }
}
