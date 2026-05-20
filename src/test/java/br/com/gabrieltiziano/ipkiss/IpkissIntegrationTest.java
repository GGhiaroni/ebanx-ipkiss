package br.com.gabrieltiziano.ipkiss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IpkissIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void resetState() throws Exception {
        mockMvc.perform(post("/reset"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldExecuteFullIpkissOfficialFlow() throws Exception {
        mockMvc.perform(get("/balance").param("account_id", "1234"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"deposit","destination":"100","amount":10}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.destination.id").value("100"))
                .andExpect(jsonPath("$.destination.balance").value(10));

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"deposit","destination":"100","amount":10}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.destination.balance").value(20));

        mockMvc.perform(get("/balance").param("account_id", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"withdraw","origin":"200","amount":10}
                    """))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"withdraw","origin":"100","amount":5}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.origin.id").value("100"))
                .andExpect(jsonPath("$.origin.balance").value(15));

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"transfer","origin":"100","amount":15,"destination":"300"}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.origin.id").value("100"))
                .andExpect(jsonPath("$.origin.balance").value(0))
                .andExpect(jsonPath("$.destination.id").value("300"))
                .andExpect(jsonPath("$.destination.balance").value(15));

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"transfer","origin":"200","amount":15,"destination":"300"}
                    """))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));

        mockMvc.perform(get("/balance").param("account_id", "300"))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));
    }

    @Test
    void shouldReturn422WhenWithdrawingWithInsufficientBalance() throws Exception {
        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"deposit","destination":"100","amount":10}
                    """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"withdraw","origin":"100","amount":50}
                    """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.id").value("100"))
                .andExpect(jsonPath("$.balance").value(10));

        mockMvc.perform(get("/balance").param("account_id", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }
}