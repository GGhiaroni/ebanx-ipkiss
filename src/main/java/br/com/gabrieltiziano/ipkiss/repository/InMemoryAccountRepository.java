package br.com.gabrieltiziano.ipkiss.repository;

import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

@Repository
public class InMemoryAccountRepository implements AccountRepository{
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findById(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public Account save(Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public Account update(String id, BiFunction<String, Account, Account> remapper) {
        return accounts.compute(id, remapper);
    }
}
