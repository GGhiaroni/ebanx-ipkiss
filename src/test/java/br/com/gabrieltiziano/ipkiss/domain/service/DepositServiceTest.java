package br.com.gabrieltiziano.ipkiss.domain.service;

import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.repository.AccountRepository;
import br.com.gabrieltiziano.ipkiss.repository.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DepositServiceTest {

    private AccountRepository repository;
    private DepositService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new DepositService(repository);
    }

    @Test
    void shouldCreateAccountWhenItDoesNotExist() {
        Account result = service.deposit("100", 10);

        assertThat(result.getId()).isEqualTo("100");
        assertThat(result.getBalance()).isEqualTo(10);
    }

    @Test
    void shouldIncrementBalanceWhenAccountAlreadyExists() {
        service.deposit("100", 10);

        Account result = service.deposit("100", 15);

        assertThat(result.getBalance()).isEqualTo(25);
    }

    @Test
    void shouldPersistAccountInRepository() {
        service.deposit("100", 50);

        assertThat(repository.findById("100"))
                .isPresent()
                .map(Account::getBalance)
                .contains(50);
    }
}