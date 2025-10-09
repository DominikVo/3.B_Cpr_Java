package org.example.accounts.services;

import org.example.accounts.BaseBankAccount;

public class BankAccountBalanceService {

    public void deposit(BaseBankAccount account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        System.out.println(account.getUuid() + " (" + account.getAccountNumber() + ") : " + ": + " + amount);

        if (amount > 10000) {
            System.out.println("Deposit amount greater than 10000.");
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

        System.out.println(account.getUuid() + " (" + account.getAccountNumber() + ") : " + ": - " + amount);

        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
    }

}
