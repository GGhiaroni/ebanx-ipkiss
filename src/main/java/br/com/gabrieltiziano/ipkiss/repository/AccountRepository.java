package br.com.gabrieltiziano.ipkiss.repository;

import br.com.gabrieltiziano.ipkiss.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findById(String id);
    Account save(Account account);
    void clear();
}
