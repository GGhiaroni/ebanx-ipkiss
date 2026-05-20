package br.com.gabrieltiziano.ipkiss.domain.service;

import br.com.gabrieltiziano.ipkiss.domain.exception.AccountNotFoundException;
import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class WithdrawService {
    private final AccountRepository accountRepository;

    public WithdrawService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account withdraw(String originId, int amount){
        return accountRepository.update(originId, (id, existing) -> {
            if (existing == null) {
                throw new AccountNotFoundException(id);
            }
            existing.withdraw(amount);
            return  existing;
        });
    }
}
