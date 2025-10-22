// java
package org.example;

import org.example.Logger.BankLogger;
import org.example.accounts.BaseBankAccount;
import org.example.accounts.SaveAccount;
import org.example.accounts.StudentAccount;
import org.example.accounts.card.PaymentCard;
import org.example.accounts.card.PaymentCardManipulation;
import org.example.accounts.card.PaymentCardsManager;
import org.example.accounts.factories.BankAccountFactory;
import org.example.accounts.generators.BankAccountNumberGenerator;
import org.example.accounts.serialization.BankAccountOwnerJsonSerializationService;
import org.example.accounts.serialization.BankAccountOwnerXmlSerialization;
import org.example.accounts.serialization.Serialization;
import org.example.accounts.services.BankAccountBalanceService;
import org.example.persons.BankAccountOwner;
import org.example.persons.BankAccountOwnerFactory;
import org.example.accounts.BankAccountWithPaymentCards;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class App {

    public void run() {
        this.testBankAccounts();
    }

    private void testBankAccounts() {

        BankLogger logger = BankLogger.getInstance();
        logger.info("Starting bank accounts test...");

        try {
            BankAccountFactory bankAccountFactory = new BankAccountFactory();
            BankAccountOwnerFactory bankAccountOwnerFactory = new BankAccountOwnerFactory();
            BankAccountBalanceService bankAccountBalanceService = new BankAccountBalanceService();

            BankAccountOwner owner = bankAccountOwnerFactory.createBankAccountOwner("O-123", "Tomas", "Pesek");
            logger.info(owner.getUuid() + ": " + owner.getFullName());


            Serialization bankAccountOwnerJsonSerializationService = new BankAccountOwnerJsonSerializationService();
            bankAccountOwnerJsonSerializationService.serialization(owner);


            /*
            Serialization bankAccountOwnerXmlSerializationService = new BankAccountOwnerXmlSerialization();
            String serializedOwner = bankAccountOwnerXmlSerializationService.serialization(owner);
            logger.info(serializedOwner);

            BankAccountOwner deserializedOwner = (BankAccountOwner) bankAccountOwnerXmlSerializationService.deserialization(serializedOwner);
            logger.info(deserializedOwner.getUuid() + ": " + deserializedOwner.getFullName());
            */

            BaseBankAccount account1 = bankAccountFactory.createBaseBankAccount("base-bank-account-123", owner, 500);
            BaseBankAccount account2 = bankAccountFactory.createSavingBankAccount("saving-bank-account-123", owner, 500);
            BaseBankAccount account3 = bankAccountFactory.createStudentBankAccount("student-bank-account-123", owner, 500, "DELTA");

            logger.info(account1.getUuid() + " (" + account1.getAccountNumber() + ") : " + account1.getBalance());
            logger.info(account2.getUuid() + " (" + account2.getAccountNumber() + ") : " + ": " + account1.getBalance());
            logger.info(account3.getUuid() + " (" + account3.getAccountNumber() + ") : " + ": " + account1.getBalance());

            bankAccountBalanceService.deposit(account1, 300);
            logger.info(account1.getUuid() + ": " + account1.getBalance());

            bankAccountBalanceService.deposit(account1, 200);
            logger.info(account1.getUuid() + ": " + account1.getBalance());

            bankAccountBalanceService.deposit(account1, 100);
            logger.info(account1.getUuid() + ": " + account1.getBalance());

            bankAccountBalanceService.withdraw(account1, 500);
            logger.info(account1.getUuid() + ": " + account1.getBalance());

            //Cards Manager
            PaymentCardsManager cardsManager = new PaymentCardsManager();

            // Create a card
            cardsManager.Create("4111111111111111", "John Doe", "12", "2030", "123");
            ArrayList<PaymentCard> cards = (ArrayList<PaymentCard>) cardsManager.GetAll();
            logger.info("Cards count: " + cards.size());

            BankAccountWithPaymentCards accountWithCards;
            if (account1 instanceof BankAccountWithPaymentCards) {
                accountWithCards = (BankAccountWithPaymentCards) account1;
            } else {
                // wrap existing BaseBankAccount data into a BankAccountWithPaymentCards instance
                accountWithCards = new BankAccountWithPaymentCards(
                        account1.getUuid(),
                        account1.getAccountNumber(),
                        account1.getOwner(),
                        account1.getBalance()
                );
                // optionally replace account1 reference if you want to continue using it as the wrapped account:
                account1 = accountWithCards;
            }

            // Cards Manager
            PaymentCardsManager cardsManager2 = new PaymentCardsManager();

            // Create a standalone card
            cardsManager.Create("4111111111111111", "John Doe", "12", "2030", "123");
            ArrayList<PaymentCard> cards2 = (ArrayList<PaymentCard>) cardsManager.GetAll();
            logger.info("Cards count: " + cards.size());

            // Create a card linked to accountWithCards (safe, no ClassCastException)
            cardsManager.Create("4222222222222222", "Jane Smith", "11", "2025", "456", accountWithCards);
            cards = (ArrayList<PaymentCard>) cardsManager.GetAll();
            logger.info("Cards count: " + cards.size());
            PaymentCard card = cardsManager.findByNumber("4222222222222222");
            if (card != null) {
                logger.info("Card " + card.getCardNumber() + " is linked to account with balance: " + card.getLinkedAccountBalance());
            } else {
                logger.info("Card not found");
            }

            PaymentCard card3 = cardsManager.findByNumber("4222222222222222");
            if (card == null) {
                logger.info("Card not found");
            } else if (card.getLinkedAccount() == null) {
                logger.info("Card is not linked to an account");
            } else {
                PaymentCardManipulation manip = new PaymentCardManipulation();
                double amount = 100.0;
                boolean success = manip.charge(card, amount);
                if (success) {
                    logger.info("Withdrawn " + amount + " via card. New balance: " + card.getLinkedAccount().getBalance());
                } else {
                    logger.info("Withdrawal failed (insufficient funds or business rule).");
                }
            }

            // Test top-up
            if (card3 != null && card3.getLinkedAccount() != null) {
                PaymentCardManipulation manip = new PaymentCardManipulation();
                double topUpAmount = 200.0;
                boolean topUpSuccess = manip.topUp(card3, topUpAmount);
                if (topUpSuccess) {
                    logger.info("Deposited " + topUpAmount + " via card. New balance: " + card3.getLinkedAccount().getBalance());
                } else {
                    logger.info("Top-up failed.");
                }
            }

            // Test transfer
            PaymentCard cardFrom = cardsManager.findByNumber("4222222222222222");
            PaymentCard cardTo = cardsManager.findByNumber("4111111111111111");
            if (cardFrom != null && cardTo != null && cardFrom.getLinkedAccount()
                    != null && cardTo.getLinkedAccount() != null) {
                PaymentCardManipulation manip = new PaymentCardManipulation();
                double transferAmount = 50.0;
                boolean transferSuccess = manip.transfer(cardFrom, cardTo, transferAmount);
                if (transferSuccess) {
                    logger.info("Transferred " + transferAmount + " from card " + cardFrom.getCardNumber()
                            + " to card " + cardTo.getCardNumber());
                    logger.info("New balance - From Card: " + cardFrom.getLinkedAccount().getBalance()
                            + ", To Card: " + cardTo.getLinkedAccount().getBalance());
                } else {
                    logger.info("Transfer failed.");
                }
            }

            // Clean up - delete cards
            for (PaymentCard c : new ArrayList<>(cardsManager.GetAll())) {
                cardsManager.Delete(c);
            }
            logger.info("Cards count after deletion: " + cardsManager.GetAll().size());


        } catch (Exception e) {
            BankLogger.getInstance().severe("Error: " + e.getMessage());
        }
    }

}
