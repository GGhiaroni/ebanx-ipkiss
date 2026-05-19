package br.com.gabrieltiziano.ipkiss.domain.model;

import br.com.gabrieltiziano.ipkiss.domain.exception.InsufficientBalanceException;

import java.util.Objects;

public class Account {
    private final String id;
    private int balance;

    public Account(String id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public void deposit(int amount){
        this.balance += amount;
    }

    public void withdraw(int amount){
        if (balance < amount) {
            throw new InsufficientBalanceException(id, balance);
        }
        this.balance -= amount;
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Account{id='" + id + "', balance=" + balance + "}";
    }
}
