package org.example.accounts;

import org.example.persons.BankAccountOwner;

public abstract class BankAccount extends  BaseBankAccount {

    public BankAccount(String uuid, String accountNumber, BankAccountOwner owner, double balance) {
        super(uuid, accountNumber, owner, balance);
    }

}