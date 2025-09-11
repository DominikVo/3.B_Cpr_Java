package org.example;

import org.example.accounts.BaseBankAccount;
import org.example.accounts.SaveAccount;
import org.example.accounts.StudentAccount;
import org.example.persons.customers.Customer;

public class Main {

    public static void main(String[] args) {

        Customer customer = new Customer("c-123", "Rostislav", "Stavkovy");

        StudentAccount account = new StudentAccount("s-123", "123456789", customer, 1, "Delta");

        System.out.println("Account holder: " + account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        System.out.println("Account balance: " + account.getBalance());
        System.out.println("Student school: " + account.getSchoolName());
        account.deposit(500);
        System.out.println("Account balance after deposit: " + account.getBalance());
    }
}