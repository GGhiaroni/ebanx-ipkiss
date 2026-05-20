package br.com.gabrieltiziano.ipkiss.web;

import br.com.gabrieltiziano.ipkiss.domain.exception.AccountNotFoundException;
import br.com.gabrieltiziano.ipkiss.domain.service.BalanceService;
import br.com.gabrieltiziano.ipkiss.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BalanceController.class)
@Import(GlobalExceptionHandler.class)
class BalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BalanceService balanceService;

    @Test
    void shouldReturn200AndBalanceWhenAccountExists() throws Exception {
        when(balanceService.getBalance("100")).thenReturn(20);

        mockMvc.perform(get("/balance").param("account_id", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));
    }

    @Test
    void shouldReturn404AndBodyZeroWhenAccountDoesNotExist() throws Exception {
        when(balanceService.getBalance("999"))
                .thenThrow(new AccountNotFoundException("999"));

        mockMvc.perform(get("/balance").param("account_id", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }
}