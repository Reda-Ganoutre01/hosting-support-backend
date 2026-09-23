package hosting_support_backend.service;

import hosting_support_backend.dto.requests.TicketRequestDTO;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.entity.User;
import hosting_support_backend.entity.enums.Priority;
import hosting_support_backend.entity.enums.TicketStatus;
import hosting_support_backend.repository.TicketRepository;
import hosting_support_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketRequestDTO requestFor(Long userId) {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSubject("Subject");
        dto.setDescription("Description");
        dto.setStatus(null);
        dto.setPriority(null);
        dto.setUserId(userId);
        return dto;
    }

    @Test
    void create_usesExplicitUserAndAppliesDefaults() {
        User user = User.builder().id(7L).email("owner@example.com").build();
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket created = ticketService.create(requestFor(7L));

        assertEquals(user, created.getUser());
        assertEquals("Subject", created.getSubject());
        assertEquals(TicketStatus.OPEN, created.getStatus());
        assertEquals(Priority.MEDIUM, created.getPriority());
    }

    @Test
    void create_fallsBackToFirstUserWhenUserUnknown() {
        User first = User.builder().id(3L).email("first@example.com").build();
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(List.of(first));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket created = ticketService.create(requestFor(99L));

        assertEquals(first, created.getUser());
    }

    @Test
    void create_throwsWhenNoUserIsAvailable() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> ticketService.create(requestFor(99L)));
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void create_keepsExplicitStatusAndPriority() {
        User user = User.builder().id(1L).email("owner@example.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        TicketRequestDTO dto = requestFor(1L);
        dto.setStatus(TicketStatus.IN_PROGRESS);
        dto.setPriority(Priority.HIGH);

        Ticket created = ticketService.create(dto);

        assertEquals(TicketStatus.IN_PROGRESS, created.getStatus());
        assertEquals(Priority.HIGH, created.getPriority());
    }

    @Test
    void update_mergesFields() {
        Ticket existing = Ticket.builder().id(1L).subject("Old").build();
        TicketRequestDTO dto = requestFor(1L);
        dto.setSubject("New Subject");
        dto.setDescription("New description");
        dto.setStatus(TicketStatus.CLOSED);
        dto.setPriority(Priority.LOW);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket updated = ticketService.update(1L, dto);

        assertEquals("New Subject", updated.getSubject());
        assertEquals("New description", updated.getDescription());
        assertEquals(TicketStatus.CLOSED, updated.getStatus());
        assertEquals(Priority.LOW, updated.getPriority());
    }

    @Test
    void update_throwsWhenTicketNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.update(1L, requestFor(1L)));
    }

    @Test
    void delete_throwsWhenTicketNotFound() {
        when(ticketRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ticketService.delete(99L));
        verify(ticketRepository, never()).deleteById(99L);
    }

    @Test
    void delete_removesExistingTicket() {
        when(ticketRepository.existsById(1L)).thenReturn(true);

        ticketService.delete(1L);

        verify(ticketRepository).deleteById(1L);
    }

    @Test
    void getById_returnsTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(Ticket.builder().id(1L).build()));

        assertEquals(1L, ticketService.getById(1L).getId());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.getById(1L));
    }

    @Test
    void getByUserId_throwsWhenUserMissing() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ticketService.getByUserId(99L));
    }

    @Test
    void getByUserId_returnsUserTickets() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(ticketRepository.findByUserId(1L)).thenReturn(List.of(Ticket.builder().id(1L).build()));

        assertEquals(1, ticketService.getByUserId(1L).size());
    }

    @Test
    void getAll_returnsAllTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(Ticket.builder().id(1L).build()));

        assertEquals(1, ticketService.getAll().size());
    }
}