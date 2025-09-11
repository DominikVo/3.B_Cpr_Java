package org.example.accounts;

import org.example.persons.customers.Customer;

public class StudentAccount extends BaseBankAccount{

    private String SchoolName;

    public StudentAccount(String uuid, String bankAccountNumber, Customer customer, double balance, String SchoolName) {
        super(uuid, bankAccountNumber, customer, balance);
        this.SchoolName = SchoolName;
    }

    public StudentAccount(String uuid, String bankAccountNumber, org.example.persons.customers.Customer customer) {
        this(uuid, bankAccountNumber, customer, 0, "");
    }

    public String getSchoolName() {
        return SchoolName;
    }
}
