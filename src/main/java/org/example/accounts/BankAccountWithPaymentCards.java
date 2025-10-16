// java
package org.example.accounts;

import org.example.accounts.card.PaymentCard;

import org.example.persons.BankAccountOwner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccountWithPaymentCards extends BaseBankAccount {

    private final List<PaymentCard> paymentCards = new ArrayList<>();

    public BankAccountWithPaymentCards(String uuid, String accountNumber, BankAccountOwner owner, double balance) {
        super(uuid, accountNumber, owner, balance);
    }

    // attach an existing card to this account
    public void attachCard(PaymentCard card) {
        if (card != null && !paymentCards.contains(card)) {
            paymentCards.add(card);
        }
    }

    // detach a card
    public void detachCard(PaymentCard card) {
        paymentCards.remove(card);
    }

    // issue a card via external factory/manager and attach it (manager should call attach as well)
    public List<PaymentCard> getPaymentCards() {
        return paymentCards;
    }
}
