package br.com.gabrieltiziano.ipkiss.domain.service;

import br.com.gabrieltiziano.ipkiss.domain.exception.AccountNotFoundException;
import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class BalanceServiceTest {

    private AccountRepository repository;
    private BalanceService service;

    @BeforeEach
    void setUp() {
        repository = mock(AccountRepository.class);
        service = new BalanceService(repository);
    }

    @Test
    void shouldReturnBalanceWhenAccountExists() {
        when(repository.findById("100"))
                .thenReturn(Optional.of(new Account("100", 50)));

        int balance = service.getBalance("100");

        assertThat(balance).isEqualTo(50);
    }

    @Test
    void shouldThrowAccountNotFoundWhenAccountDoesNotExist() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getBalance("999"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void shouldNeverModifyRepositoryStateOnBalanceQuery() {
        when(repository.findById("100"))
                .thenReturn(Optional.of(new Account("100", 50)));

        service.getBalance("100");

        verify(repository, never()).save(any());
        verify(repository, never()).clear();
    }
}