package org.example.accounts;

import org.example.persons.BankAccountOwner;

public class SaveAccount extends BaseBankAccount {

    private final float interestRate;

    public SaveAccount(String uuid, String accountNumber, BankAccountOwner owner, double balance) {
        super(uuid, accountNumber, owner, balance);

        interestRate = 5;
    }

    public float getInterestRate() {
        return interestRate;
    }
}