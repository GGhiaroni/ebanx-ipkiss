package br.com.gabrieltiziano.ipkiss.web;

import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.domain.service.DepositService;
import br.com.gabrieltiziano.ipkiss.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@Import(GlobalExceptionHandler.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepositService depositService;

    @Test
    void shouldReturn201WithDepositResponseWhenDepositSucceeds() throws Exception {
        when(depositService.deposit("100", 10))
                .thenReturn(new Account("100", 10));

        String body = """
            {"type":"deposit","destination":"100","amount":10}
            """;

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.destination.id").value("100"))
                .andExpect(jsonPath("$.destination.balance").value(10));
    }

    @Test
    void shouldReturnIncrementedBalanceOnSecondDeposit() throws Exception {
        when(depositService.deposit("100", 10))
                .thenReturn(new Account("100", 20));

        String body = """
            {"type":"deposit","destination":"100","amount":10}
            """;

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.destination.balance").value(20));
    }
}