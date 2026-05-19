package br.com.gabrieltiziano.ipkiss.domain.model;

import br.com.gabrieltiziano.ipkiss.domain.exception.InsufficientBalanceException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    @Test
    void shouldCreateAccountWithIdAndInitialBalance() {
        Account account = new Account("100", 50);

        assertThat(account.getId()).isEqualTo("100");
        assertThat(account.getBalance()).isEqualTo(50);
    }

    @Test
    void shouldIncreaseBalanceOnDeposit() {
        Account account = new Account("100", 10);

        account.deposit(15);

        assertThat(account.getBalance()).isEqualTo(25);
    }

    @Test
    void shouldDecreaseBalanceOnWithdraw() {
        Account account = new Account("100", 50);

        account.withdraw(20);

        assertThat(account.getBalance()).isEqualTo(30);
    }

    @Test
    void shouldAllowWithdrawingExactBalance() {
        Account account = new Account("100", 30);

        account.withdraw(30);

        assertThat(account.getBalance()).isZero();
    }

    @Test
    void shouldThrowWhenWithdrawingMoreThanBalance() {
        Account account = new Account("100", 10);

        assertThatThrownBy(() -> account.withdraw(50))
                .isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    void shouldNotChangeBalanceWhenWithdrawFails() {
        Account account = new Account("100", 10);

        try {
            account.withdraw(50);
        } catch (InsufficientBalanceException ignored) {
            // expected
        }

        assertThat(account.getBalance()).isEqualTo(10);
    }

    @Test
    void exceptionShouldCarryAccountIdAndCurrentBalance() {
        Account account = new Account("100", 10);

        assertThatThrownBy(() -> account.withdraw(50))
                .isInstanceOfSatisfying(InsufficientBalanceException.class, e -> {
                    assertThat(e.getAccountId()).isEqualTo("100");
                    assertThat(e.getCurrentBalance()).isEqualTo(10);
                });
    }

    @Test
    void twoAccountsWithSameIdAreEqual() {
        Account a = new Account("100", 50);
        Account b = new Account("100", 999);

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
    }

    @Test
    void accountsWithDifferentIdsAreNotEqual() {
        Account a = new Account("100", 50);
        Account b = new Account("200", 50);

        assertThat(a).isNotEqualTo(b);
    }
}