package br.com.gabrieltiziano.ipkiss.domain.service;

import br.com.gabrieltiziano.ipkiss.domain.exception.AccountNotFoundException;
import br.com.gabrieltiziano.ipkiss.domain.exception.InsufficientBalanceException;
import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.repository.AccountRepository;
import br.com.gabrieltiziano.ipkiss.repository.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WithdrawServiceTest {

    private AccountRepository repository;
    private WithdrawService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new WithdrawService(repository);
    }

    @Test
    void shouldDecreaseBalanceWhenWithdrawIsValid() {
        repository.save(new Account("100", 50));

        Account result = service.withdraw("100", 20);

        assertThat(result.getBalance()).isEqualTo(30);
    }

    @Test
    void shouldThrowAccountNotFoundWhenAccountDoesNotExist() {
        assertThatThrownBy(() -> service.withdraw("999", 10))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void shouldThrowInsufficientBalanceWhenBalanceIsTooLow() {
        repository.save(new Account("100", 10));

        assertThatThrownBy(() -> service.withdraw("100", 50))
                .isInstanceOfSatisfying(InsufficientBalanceException.class, e -> {
                    assertThat(e.getAccountId()).isEqualTo("100");
                    assertThat(e.getCurrentBalance()).isEqualTo(10);
                });
    }

    @Test
    void shouldNotChangeBalanceWhenWithdrawFails() {
        repository.save(new Account("100", 10));

        try {
            service.withdraw("100", 50);
        } catch (InsufficientBalanceException ignored) {
        }

        assertThat(repository.findById("100"))
                .map(Account::getBalance)
                .contains(10);
    }
}