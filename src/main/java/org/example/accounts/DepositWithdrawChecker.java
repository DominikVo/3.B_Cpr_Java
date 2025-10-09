package org.example.accounts;

public class DepositWithdrawChecker {
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

    public static boolean isLargeWithdrawal(double amount) {
        if(amount > LIMIT) {
            System.out.println("Large withdrawal detected: " + amount);
            return true;
        } else {
            System.out.println("Withdrawal amount is within the normal range: " + amount);
            return false;
        }
    }
}
