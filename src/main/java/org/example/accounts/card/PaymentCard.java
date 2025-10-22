// java
package org.example.accounts.card;

import org.example.accounts.BankAccountWithPaymentCards;

public class PaymentCard {

    private final String cardNumber;
    private final String cvv;
    private final String expireMonth;
    private final String expireYear;
    private final String owner;

    // optional link to a bank account
    private final BankAccountWithPaymentCards linkedAccount;

    // existing constructor (no linked account)
    public PaymentCard(String cardNumber, String cvv, String expireMonth, String expireYear, String owner) {
        this(cardNumber, cvv, expireMonth, expireYear, owner, null);
    }

    // new constructor with linked account
    public PaymentCard(String cardNumber, String cvv, String expireMonth, String expireYear, String owner, BankAccountWithPaymentCards linkedAccount) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.owner = owner;
        this.linkedAccount = linkedAccount;
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

    public BankAccountWithPaymentCards getLinkedAccount() {
        return linkedAccount;
    }

    // returns null if card is not linked to an account
    public Double getLinkedAccountBalance() {
        return linkedAccount != null ? linkedAccount.getBalance() : null;
    }

}
