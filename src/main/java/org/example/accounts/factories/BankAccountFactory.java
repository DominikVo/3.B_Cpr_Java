package org.example.accounts.factories;

import org.example.accounts.BaseBankAccount;
import org.example.accounts.SaveAccount;
import org.example.accounts.StudentAccount;
import org.example.accounts.generators.BankAccountNumberGenerator;
import org.example.persons.BankAccountOwner;

public class BankAccountFactory {

    private final BankAccountNumberGenerator bankAccountNumberGenerator = new BankAccountNumberGenerator();

    public BaseBankAccount createBaseBankAccount(String uuid, BankAccountOwner owner, double balance) {
        String accountNumber = bankAccountNumberGenerator.generateRandomAccountNumber();

        return new BaseBankAccount(uuid, accountNumber, owner, balance);
    }

    public SaveAccount createSavingBankAccount(String uuid, BankAccountOwner owner, double balance) {
        String accountNumber = bankAccountNumberGenerator.generateRandomAccountNumber();

        return new SaveAccount(uuid, accountNumber, owner, balance);
    }

    public StudentAccount createStudentBankAccount(String uuid, BankAccountOwner owner, double balance, String schoolName) {
        String accountNumber = bankAccountNumberGenerator.generateRandomAccountNumber();

        return new StudentAccount(uuid, accountNumber, owner, balance, schoolName);
    }

}
