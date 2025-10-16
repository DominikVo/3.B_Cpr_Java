package org.example.accounts;

import org.example.accounts.BaseBankAccount;
import org.example.persons.BankAccountOwner;

public class BankAccountWithPaymentCards extends BaseBankAccount {

    public BankAccountWithPaymentCards(String uuid, String accountNumber, BankAccountOwner owner, double balance) {
        super(uuid, accountNumber, owner, balance);
    }
}