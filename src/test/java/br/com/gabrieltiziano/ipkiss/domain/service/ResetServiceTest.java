package br.com.gabrieltiziano.ipkiss.domain.service;

import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.repository.AccountRepository;
import br.com.gabrieltiziano.ipkiss.repository.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResetServiceTest {

    private AccountRepository repository;
    private ResetService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new ResetService(repository);
    }

    @Test
    void shouldRemoveAllAccountsWhenReset() {
        repository.save(new Account("100", 50));
        repository.save(new Account("200", 75));

        service.reset();

        assertThat(repository.findById("100")).isEmpty();
        assertThat(repository.findById("200")).isEmpty();
    }

    @Test
    void shouldDoNothingWhenRepositoryIsAlreadyEmpty() {
        service.reset();

        assertThat(repository.findById("100")).isEmpty();
    }
}