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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void login_withAdminCredentials_returnsJwtToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"reda@example.com","password":"12345678"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(body.get("token").asText()).isNotBlank();
    }

    @Test
    void login_withWrongPassword_isRejected() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"reda@example.com","password":"wrong-password"}
                                """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void register_createsNewUser() throws Exception {
        long nonce = System.currentTimeMillis();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fullName":"Test User","userName":"tuser%d",
                                 "email":"tuser%d@example.com","password":"secret123",
                                 "phone":"0600000000","role":"USER"}
                                """.formatted(nonce, nonce)))
                .andExpect(status().isCreated());
    }

    @Test
    void register_withExistingEmail_returnsConflict() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fullName":"Reda","userName":"someone-else",
                                 "email":"reda@example.com","password":"secret123",
                                 "phone":"0600000000","role":"USER"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void getAllUsers_returnsSeededUsers() throws Exception {
        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()")
                        .value(org.hamcrest.Matchers.greaterThanOrEqualTo(12)))
                .andExpect(jsonPath("$[0].email").isNotEmpty());
    }

    @Test
    void getPaginatedUsers_returnsContent() throws Exception {
        mockMvc.perform(get("/api/users").param("page", "0").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements")
                        .value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    void getPublicMaintenanceStatus_returnsOk() throws Exception {
        mockMvc.perform(get("/api/settings/maintenance"))
                .andExpect(status().isOk());
    }
}