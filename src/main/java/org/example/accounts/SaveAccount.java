package org.example.accounts;

import org.example.persons.customers.Customer;

public class SaveAccount extends BaseBankAccount {

    private float interestRate;

    public SaveAccount(String uuid, String bankAccountNumber, Customer customer, double balance, float interestRate) {
        super(uuid, bankAccountNumber, customer, balance);

        this.interestRate = interestRate;
    }

    public SaveAccount(String uuid, String bankAccountNumber, Customer customer) {
        this(uuid, bankAccountNumber, customer, 0, 0);
    }

    public float getInterestRate() {
        return interestRate;
    }


}
