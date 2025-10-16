package org.example.accounts.card;

import java.util.List;

public class PaymentCardsManager {

    private List<PaymentCard> paymentCards;

    public void Create(String CardNumber, String CardHolderName, String ExpirationMonth, String ExpirationYear , String CVV)
    {
        PaymentCard paymentCard = new PaymentCard(CardNumber, CardHolderName, ExpirationMonth, ExpirationYear , CVV);
        paymentCards.add(paymentCard);
    }

    public void Delete(PaymentCard paymentCard)
    {
        if(paymentCards.contains(paymentCard)){
            paymentCards.remove(paymentCard);
        }
        else
        {
            throw new RuntimeException("Card not found");
        }
    }

    public List<PaymentCard> GetAll()
    {
        return paymentCards;
    }

}
