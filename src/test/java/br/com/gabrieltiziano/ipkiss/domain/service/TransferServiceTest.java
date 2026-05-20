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

class TransferServiceTest {

    private AccountRepository repository;
    private TransferService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new TransferService(repository);
    }

    @Test
    void shouldTransferBetweenExistingAccounts() {
        repository.save(new Account("100", 50));
        repository.save(new Account("200", 10));

        TransferResult result = service.transfer("100", 30, "200");

        assertThat(result.origin().getBalance()).isEqualTo(20);
        assertThat(result.destination().getBalance()).isEqualTo(40);
    }

    @Test
    void shouldCreateDestinationAccountWhenItDoesNotExist() {
        repository.save(new Account("100", 50));

        TransferResult result = service.transfer("100", 30, "300");

        assertThat(result.origin().getBalance()).isEqualTo(20);
        assertThat(result.destination().getId()).isEqualTo("300");
        assertThat(result.destination().getBalance()).isEqualTo(30);
    }

    @Test
    void shouldThrowAccountNotFoundWhenOriginDoesNotExist() {
        assertThatThrownBy(() -> service.transfer("999", 10, "100"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void shouldThrowInsufficientBalanceWhenOriginHasNotEnough() {
        repository.save(new Account("100", 10));

        assertThatThrownBy(() -> service.transfer("100", 50, "200"))
                .isInstanceOfSatisfying(InsufficientBalanceException.class, e -> {
                    assertThat(e.getAccountId()).isEqualTo("100");
                    assertThat(e.getCurrentBalance()).isEqualTo(10);
                });
    }

    @Test
    void shouldNotChangeAnyAccountWhenOriginHasInsufficientBalance() {
        repository.save(new Account("100", 10));
        repository.save(new Account("200", 25));

        try {
            service.transfer("100", 50, "200");
        } catch (InsufficientBalanceException ignored) {
        }

        assertThat(repository.findById("100"))
                .map(Account::getBalance).contains(10);
        assertThat(repository.findById("200"))
                .map(Account::getBalance).contains(25);
    }

    @Test
    void shouldNotCreateDestinationWhenOriginDoesNotExist() {
        assertThatThrownBy(() -> service.transfer("999", 10, "300"))
                .isInstanceOf(AccountNotFoundException.class);

        assertThat(repository.findById("300")).isEmpty();
    }

    @Test
    void shouldPersistBothAccountsAfterTransfer() {
        repository.save(new Account("100", 50));
        repository.save(new Account("200", 10));

        service.transfer("100", 30, "200");

        assertThat(repository.findById("100"))
                .map(Account::getBalance).contains(20);
        assertThat(repository.findById("200"))
                .map(Account::getBalance).contains(40);
    }
}