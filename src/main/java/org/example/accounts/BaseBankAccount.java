package org.example.accounts;

import org.example.persons.BankAccountOwner;

public class BaseBankAccount {

    private final String uuid;

    private final String accountNumber;

    private final BankAccountOwner owner;

    double balance;

    public BaseBankAccount(String uuid, String accountNumber, BankAccountOwner owner, double balance) {
        this.uuid = uuid;
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BankAccountOwner getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}