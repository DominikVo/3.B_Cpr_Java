package org.example.accounts.card;

import java.security.SecureRandom;

public class PaymentCardCvvGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final byte LENGTH = 3;

    public String generate() {
        int max = (int) Math.pow(10, LENGTH);
        int value = RANDOM.nextInt(max); // 0 .. max-1
        return String.format("%0" + LENGTH + "d", value);
    }

}