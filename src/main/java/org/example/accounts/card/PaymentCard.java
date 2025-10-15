package org.example.accounts.card;

public class PaymentCard {

    private final String cardNumber;

    private final String cvv;

    private final String expireMonth;

    private final String expireYear;

    private final String owner;

    public PaymentCard(String cardNumber, String cvv, String expireMonth, String expireYear, String owner) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.owner = owner;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public String getOwner() {
        return owner;
    }
}