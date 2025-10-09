package org.example.accounts;

public class DepositChecker {
    private static final double LIMIT = 10000;

    public static boolean isLargeDeposit(double amount) {
        if(amount > LIMIT) {
            System.out.println("Large deposit detected: " + amount);
            return true;
        } else {
            System.out.println("Deposit amount is within the normal range: " + amount);
            return false;
        }
    }
}
