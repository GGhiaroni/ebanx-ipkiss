package br.com.gabrieltiziano.ipkiss.domain.service;

import br.com.gabrieltiziano.ipkiss.domain.exception.AccountNotFoundException;
import br.com.gabrieltiziano.ipkiss.domain.model.Account;
import br.com.gabrieltiziano.ipkiss.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class TransferService {
    private final AccountRepository accountRepository;
    private final Object transferLock = new Object();

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public TransferResult transfer(String originId, int amount, String destinationId){
        synchronized (transferLock){
            Account origin = accountRepository.findById(originId)
                    .orElseThrow(() -> new AccountNotFoundException(originId));

            origin.withdraw(amount);

            Account destination = accountRepository.findById(destinationId)
                    .orElseGet(() -> new Account(destinationId, 0));

            destination.deposit(amount);

            accountRepository.save(origin);
            accountRepository.save(destination);

            return new TransferResult(origin, destination);
        }
    }
}
