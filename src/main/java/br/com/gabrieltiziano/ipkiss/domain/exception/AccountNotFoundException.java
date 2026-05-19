package br.com.gabrieltiziano.ipkiss.domain.exception;

public class AccountNotFoundException extends RuntimeException {
    private final String accountId;

    public AccountNotFoundException(String accountId) {
        super("Account with id " + accountId + " not found.");
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
