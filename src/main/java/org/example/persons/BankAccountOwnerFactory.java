package org.example.persons;

public class BankAccountOwnerFactory {
    public BankAccountOwner createBankAccountOwner(String uuid, String firstName, String lastName) {
        return new BankAccountOwner(uuid, firstName, lastName);
    }
}
