package org.example.accounts;

import org.example.persons.BankAccountOwner;

public class StudentAccount extends BaseBankAccount {

    private final String schoolName;

    public StudentAccount(String uuid, String accountNumber, BankAccountOwner owner, double balance, String schoolName) {
        super(uuid, accountNumber, owner, balance);

        this.schoolName = schoolName;
    }

    public String getSchoolName() {
        return schoolName;
    }
}