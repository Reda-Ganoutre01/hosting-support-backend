package hosting_support_backend.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private long firstUserId() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode users = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(users.isArray()).isTrue();
        assertThat(users.size()).isGreaterThan(0);
        return users.get(0).get("id").asLong();
    }

    @Test
    void createAndFetchTicket_roundTrip() throws Exception {
        long userId = firstUserId();

        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"subject":"Impossible de se connecter","description":"Erreur de connexion",
                                 "status":"OPEN","priority":"HIGH","userId":%d}
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("Impossible de se connecter"))
                .andReturn();

        long ticketId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asLong();

        mockMvc.perform(get("/api/tickets/" + ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void updateTicket_changesFields() throws Exception {
        long userId = firstUserId();

        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"subject":"Site web très lent","description":"Description",
                                 "status":"OPEN","priority":"MEDIUM","userId":%d}
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andReturn();
        long ticketId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asLong();

        mockMvc.perform(put("/api/tickets/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"subject":"Site web très lent","description":"Description mise à jour",
                                 "status":"IN_PROGRESS","priority":"HIGH","userId":%d}
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    void deleteTicket_removesIt() throws Exception {
        long userId = firstUserId();

        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"subject":"Ticket à supprimer","description":"Description",
                                 "status":"OPEN","priority":"LOW","userId":%d}
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andReturn();
        long ticketId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asLong();

        mockMvc.perform(delete("/api/tickets/" + ticketId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == %d)]".formatted(ticketId)).isEmpty());
    }

    @Test
    void getAllTickets_returnsSeededData() throws Exception {
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}