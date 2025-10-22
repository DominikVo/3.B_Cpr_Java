// java
package org.example.accounts.services;

import org.example.accounts.BaseBankAccount;
import org.example.Logger.BankLogger;

public class BankAccountBalanceService {

    private static final BankLogger logger = BankLogger.getInstance();

    public void deposit(BaseBankAccount account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        logger.info(account.getUuid() + " (" + account.getAccountNumber() + ") : " + ": + " + amount);

        if (amount > 10000) {
            logger.warning("Deposit amount greater than 10000.");
        }

        // TODO check Anti Money Laundering – opatření proti praní špinavých peněz

        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
    }

    public void withdraw(BaseBankAccount account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }

        if (account.getBalance() < amount) {
            throw new IllegalStateException("Insufficient funds.");
        }

        // TODO check ...

        logger.info(account.getUuid() + " (" + account.getAccountNumber() + ") : " + ": - " + amount);

        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
    }

}
