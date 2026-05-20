package br.com.gabrieltiziano.ipkiss.domain.service;

import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class DepositService {
    private final AccountRepository accountRepository;

    public DepositService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account deposit(String destinationId, int amount) {
        return accountRepository.update(destinationId, (id, existing) -> {
                    Account account = (existing != null) ? existing : new Account(id, 0);
                    account.deposit(amount);
                    return account;
                }
        );
    }
}
