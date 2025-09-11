package org.example.accounts;

import org.example.persons.customers.Customer;

public class BaseBankAccount {

    private String uuid;

    private String bankAccountNumber;

    private Customer customer;

    private double balance;

    public BaseBankAccount(String uuid, String bankAccountNumber, Customer customer, double balance) {
        this.uuid = uuid;
        this.bankAccountNumber = bankAccountNumber;
        this.customer = customer;
        this.balance = balance;
    }

    public void deposit(double deposit)
    {
        this.balance += deposit;
    }

    public void withdraw(double withdraw)
    {
        if(this.balance >= withdraw)
        {
            this.balance -= withdraw;
        }
        else
        {
            System.out.println("Insufficient funds.");
        }
    }

    public String getUuid() {
        return uuid;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getBalance() {
        return balance;
    }
}
