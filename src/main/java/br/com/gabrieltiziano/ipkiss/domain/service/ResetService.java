package br.com.gabrieltiziano.ipkiss.domain.service;

import br.com.gabrieltiziano.ipkiss.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class ResetService {
    private final AccountRepository accountRepository;

    public ResetService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void reset(){
        accountRepository.clear();
    }
}
