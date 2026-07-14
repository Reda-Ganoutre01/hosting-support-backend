package hosting_support_backend.service;

import hosting_support_backend.dto.requests.TicketRequestDTO;
import hosting_support_backend.entity.Ticket;

import java.util.List;

public interface TicketService {
    Ticket create(TicketRequestDTO dto);
    Ticket update(Long id, TicketRequestDTO dto);
    void delete(Long id);
    Ticket getById(Long id);
    List<Ticket> getAll();
    List<Ticket> getByUserId(Long userId);
}
