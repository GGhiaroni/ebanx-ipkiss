package br.com.gabrieltiziano.ipkiss.domain.exception;

public class InsufficientBalanceException extends RuntimeException {
    private final String accountId;
    private final int currentBalance;

    public InsufficientBalanceException(String accountId, int currentBalance) {
        super("Insufficient balance on account " + accountId);
        this.accountId = accountId;
        this.currentBalance = currentBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }
}
