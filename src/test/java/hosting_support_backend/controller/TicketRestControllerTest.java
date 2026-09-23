package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.TicketRequestDTO;
import hosting_support_backend.dto.response.TicketResponseDTO;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.entity.enums.Priority;
import hosting_support_backend.entity.enums.TicketStatus;
import hosting_support_backend.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketRestControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketRestController ticketRestController;

    private Ticket sampleTicket() {
        return Ticket.builder()
                .id(1L)
                .subject("Site down")
                .description("Cannot reach the site")
                .status(TicketStatus.OPEN)
                .priority(Priority.HIGH)
                .build();
    }

    private TicketRequestDTO request() {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSubject("Site down");
        dto.setDescription("Cannot reach the site");
        dto.setStatus(TicketStatus.OPEN);
        dto.setPriority(Priority.HIGH);
        dto.setUserId(1L);
        return dto;
    }

    @Test
    void getAll_returnsDtos() {
        when(ticketService.getAll()).thenReturn(List.of(sampleTicket()));

        ResponseEntity<List<TicketResponseDTO>> response = ticketRestController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Site down", response.getBody().get(0).getSubject());
    }

    @Test
    void getByUserId_returnsDtos() {
        when(ticketService.getByUserId(1L)).thenReturn(List.of(sampleTicket()));

        ResponseEntity<List<TicketResponseDTO>> response = ticketRestController.getByUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getById_returnsDto() {
        when(ticketService.getById(1L)).thenReturn(sampleTicket());

        ResponseEntity<TicketResponseDTO> response = ticketRestController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TicketStatus.OPEN, response.getBody().getStatus());
        assertEquals(Priority.HIGH, response.getBody().getPriority());
    }

    @Test
    void create_returnsDto() {
        when(ticketService.create(any(TicketRequestDTO.class))).thenReturn(sampleTicket());

        ResponseEntity<TicketResponseDTO> response = ticketRestController.create(request());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Site down", response.getBody().getSubject());
    }

    @Test
    void update_returnsDto() {
        when(ticketService.update(eq(1L), any(TicketRequestDTO.class))).thenReturn(sampleTicket());

        ResponseEntity<TicketResponseDTO> response = ticketRestController.update(1L, request());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Site down", response.getBody().getSubject());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(ticketService).delete(1L);

        ResponseEntity<Void> response = ticketRestController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ticketService).delete(1L);
    }
}