package org.example.accounts.card;

import org.example.accounts.BankAccountWithPaymentCards;
import org.example.accounts.services.BankAccountBalanceService;
import org.example.Logger.BankLogger;

public class PaymentCardManipulation {

    private static final BankAccountBalanceService balanceService = new BankAccountBalanceService();
    private static final BankLogger logger = BankLogger.getInstance();

    public boolean charge(PaymentCard card, double amount) {
        logger.info("charge() called - card=" + (card != null ? card.getCardNumber() : "null") + ", amount=" + amount);

        if (card == null) {
            logger.severe("charge() failed: paymentCard is null");
            throw new IllegalArgumentException("paymentCard is null");
        }
        if (amount <= 0) {
            logger.warning("charge() failed: invalid amount <= 0 (" + amount + ")");
            throw new IllegalArgumentException("amount must be > 0");
        }

        BankAccountWithPaymentCards account = card.getLinkedAccount();
        if (account == null) {
            logger.warning("charge() aborted: card " + card.getCardNumber() + " is not linked to an account");
            return false;
        }

        try {
            balanceService.withdraw(account, amount);
            logger.info("Charged " + amount + " from card " + card.getCardNumber() + " (account=" + account.getAccountNumber() + "), newBalance=" + account.getBalance());
            return true;
        } catch (RuntimeException ex) {
            logger.severe("charge() failed for card " + card.getCardNumber() + ": " + ex.getMessage());
            return false;
        }
    }

    public boolean topUp(PaymentCard card, double amount) {
        logger.info("topUp() called - card=" + (card != null ? card.getCardNumber() : "null") + ", amount=" + amount);

        if (card == null) {
            logger.severe("topUp() failed: paymentCard is null");
            throw new IllegalArgumentException("paymentCard is null");
        }
        if (amount <= 0) {
            logger.warning("topUp() failed: invalid amount <= 0 (" + amount + ")");
            throw new IllegalArgumentException("amount must be > 0");
        }

        BankAccountWithPaymentCards account = card.getLinkedAccount();
        if (account == null) {
            logger.severe("topUp() aborted: card " + card.getCardNumber() + " is not linked to an account");
            return false;
        }

        try {
            balanceService.deposit(account, amount);
            logger.info("Deposited " + amount + " to card " + card.getCardNumber() + " (account=" + account.getAccountNumber() + "), newBalance=" + account.getBalance());
            return true;
        } catch (RuntimeException ex) {
            logger.severe("topUp() failed for card " + card.getCardNumber() + ": " + ex.getMessage());
            return false;
        }
    }


    public boolean transfer(PaymentCard fromCard, PaymentCard toCard, double amount) {
        logger.info("transfer() called - fromCard=" + (fromCard != null ? fromCard.getCardNumber() : "null")
                + ", toCard=" + (toCard != null ? toCard.getCardNumber() : "null") + ", amount=" + amount);

        if (fromCard == null || toCard == null) {
            logger.severe("transfer() failed: one or both cards are null");
            throw new IllegalArgumentException("cards must not be null");
        }
        if (amount <= 0) {
            logger.warning("transfer() failed: invalid amount <= 0 (" + amount + ")");
            throw new IllegalArgumentException("amount must be > 0");
        }

        BankAccountWithPaymentCards fromAcc = fromCard.getLinkedAccount();
        BankAccountWithPaymentCards toAcc = toCard.getLinkedAccount();
        if (fromAcc == null || toAcc == null) {
            logger.warning("transfer() aborted: one or both cards are not linked to accounts (fromAcc="
                    + (fromAcc == null ? "null" : fromAcc.getAccountNumber()) + ", toAcc="
                    + (toAcc == null ? "null" : toAcc.getAccountNumber()) + ")");
            return false;
        }

        logger.info("Attempting transfer of " + amount + " from account " + fromAcc.getAccountNumber() + " to account " + toAcc.getAccountNumber());

        boolean withdrew = charge(fromCard, amount);
        if (!withdrew) {
            logger.warning("transfer() failed: withdrawal from fromCard " + fromCard.getCardNumber() + " unsuccessful");
            return false;
        }

        boolean deposited = topUp(toCard, amount);
        if (!deposited) {
            logger.warning("transfer() failed: deposit to toCard " + toCard.getCardNumber() + " unsuccessful, attempting rollback");
            boolean refunded = topUp(fromCard, amount);
            if (refunded) {
                logger.info("Rollback successful: refunded " + amount + " to account " + fromAcc.getAccountNumber());
            } else {
                logger.severe("Rollback failed: could not refund " + amount + " to account " + fromAcc.getAccountNumber());
            }
            return false;
        }

        // record successful transaction
        logger.info("Transfer successful: " + amount + " from account " + fromAcc.getAccountNumber() + " to account " + toAcc.getAccountNumber());
        logger.logTransaction(fromAcc.getAccountNumber(), toAcc.getAccountNumber(), amount);
        return true;
    }
}
