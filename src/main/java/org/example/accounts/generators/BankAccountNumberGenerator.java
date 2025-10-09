package org.example.accounts.generators;

import java.util.Random;

public class BankAccountNumberGenerator {

    public String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        // Generate 10 random digits (0–9)
        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10);
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }
}
