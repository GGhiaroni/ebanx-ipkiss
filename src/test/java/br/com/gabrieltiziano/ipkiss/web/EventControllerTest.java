package br.com.gabrieltiziano.ipkiss.web;

import br.com.gabrieltiziano.ipkiss.domain.exception.AccountNotFoundException;
import br.com.gabrieltiziano.ipkiss.domain.exception.InsufficientBalanceException;
import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.domain.service.DepositService;
import br.com.gabrieltiziano.ipkiss.domain.service.TransferResult;
import br.com.gabrieltiziano.ipkiss.domain.service.TransferService;
import br.com.gabrieltiziano.ipkiss.domain.service.WithdrawService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@Import(GlobalExceptionHandler.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepositService depositService;

    @MockitoBean
    private WithdrawService withdrawService;

    @MockitoBean
    private TransferService transferService;

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

    @Test
    void shouldReturn201WithWithdrawResponseWhenWithdrawSucceeds() throws Exception {
        when(withdrawService.withdraw("100", 5))
                .thenReturn(new Account("100", 15));

        String body = """
            {"type":"withdraw","origin":"100","amount":5}
            """;

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.origin.id").value("100"))
                .andExpect(jsonPath("$.origin.balance").value(15));
    }

    @Test
    void shouldReturn404AndBodyZeroWhenWithdrawingFromMissingAccount() throws Exception {
        when(withdrawService.withdraw("200", 10))
                .thenThrow(new AccountNotFoundException("200"));

        String body = """
            {"type":"withdraw","origin":"200","amount":10}
            """;

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }

    @Test
    void shouldReturn422WhenWithdrawingWithInsufficientBalance() throws Exception {
        when(withdrawService.withdraw("100", 50))
                .thenThrow(new InsufficientBalanceException("100", 10));

        String body = """
            {"type":"withdraw","origin":"100","amount":50}
            """;

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.id").value("100"))
                .andExpect(jsonPath("$.balance").value(10));
    }

    @Test
    void shouldReturn201WithTransferResponseWhenTransferSucceeds() throws Exception {
        when(transferService.transfer("100", 15, "300"))
                .thenReturn(new TransferResult(new Account("100", 0), new Account("300", 15)));

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
    }

    @Test
    void shouldReturn404AndBodyZeroWhenTransferOriginDoesNotExist() throws Exception {
        when(transferService.transfer("200", 15, "300"))
                .thenThrow(new AccountNotFoundException("200"));

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"transfer","origin":"200","amount":15,"destination":"300"}
                    """))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }

    @Test
    void shouldReturn422WhenTransferringWithInsufficientBalance() throws Exception {
        when(transferService.transfer("100", 9999, "300"))
                .thenThrow(new InsufficientBalanceException("100", 10));

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"type":"transfer","origin":"100","amount":9999,"destination":"300"}
                    """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.id").value("100"))
                .andExpect(jsonPath("$.balance").value(10));
    }
}