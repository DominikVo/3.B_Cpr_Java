// java
package org.example.accounts.card;

import org.example.accounts.BankAccountWithPaymentCards;

import java.util.ArrayList;
import java.util.List;

public class PaymentCardsManager {

    // Initialize the list to avoid NullPointerExceptions
    private List<PaymentCard> paymentCards = new ArrayList<>();

    // Create a standalone card (parameters ordered to match PaymentCard constructor)
    public void Create(String cardNumber, String cvv, String expirationMonth, String expirationYear, String cardHolderName) {
        PaymentCard paymentCard = new PaymentCard(cardNumber, cvv, expirationMonth, expirationYear, cardHolderName);
        paymentCards.add(paymentCard);
    }

    // Create a card and link it to a bank account
    public void Create(String cardNumber, String cvv, String expirationMonth, String expirationYear, String cardHolderName, BankAccountWithPaymentCards account) {
        PaymentCard paymentCard = new PaymentCard(cardNumber, cvv, expirationMonth, expirationYear, cardHolderName, account);
        paymentCards.add(paymentCard);
        if (account != null) {
            account.attachCard(paymentCard);
        }
    }

    public void Delete(PaymentCard paymentCard) {
        if(paymentCards.contains(paymentCard)){
            paymentCards.remove(paymentCard);
        }
        else {
            throw new RuntimeException("Card not found");
        }
    }

    public List<PaymentCard> GetAll() {
        return paymentCards;
    }

    // helper to find card by number
    public PaymentCard findByNumber(String cardNumber) {
        return paymentCards.stream()
                .filter(c -> c.getCardNumber().equals(cardNumber))
                .findFirst()
                .orElse(null);
    }
}
