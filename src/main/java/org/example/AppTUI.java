// java
package org.example;

import org.example.Logger.BankLogger;
import org.example.accounts.BaseBankAccount;
import org.example.accounts.BankAccountWithPaymentCards;
import org.example.accounts.card.PaymentCard;
import org.example.accounts.card.PaymentCardFactory;
import org.example.accounts.card.PaymentCardManipulation;
import org.example.accounts.card.PaymentCardsManager;
import org.example.accounts.factories.BankAccountFactory;
import org.example.accounts.services.BankAccountBalanceService;
import org.example.persons.BankAccountOwner;
import org.example.persons.BankAccountOwnerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class AppTUI {

    private final BankLogger logger = BankLogger.getInstance();
    private final BankAccountFactory accountFactory = new BankAccountFactory();
    private final BankAccountOwnerFactory ownerFactory = new BankAccountOwnerFactory();
    private final BankAccountBalanceService balanceService = new BankAccountBalanceService();
    private final PaymentCardsManager cardsManager = new PaymentCardsManager();
    private final PaymentCardFactory cardFactory = new PaymentCardFactory();
    private final PaymentCardManipulation manip = new PaymentCardManipulation();

    // simple in-memory lists for this TUI session
    private final List<BaseBankAccount> accounts = new ArrayList<>();

    public void run() {
        Scanner sc = new Scanner(System.in);
        logger.info("Starting Text UI bank demo");
        boolean running = true;
        while (running) {
            showMenu();
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> createAccount(sc);
                    case "2" -> listAccounts();
                    case "3" -> createCard(sc);
                    case "4" -> listCards();
                    case "5" -> depositToAccount(sc);
                    case "6" -> withdrawFromAccount(sc);
                    case "7" -> chargeCard(sc);
                    case "8" -> topUpCard(sc);
                    case "9" -> transferBetweenCards(sc);
                    case "0" -> running = false;
                    default -> logger.info("Unknown option");
                }
            } catch (Exception e) {
                logger.severe("Error: " + e.getMessage());
            }
        }
        logger.info("Exiting Text UI bank demo");
        sc.close();
    }

    private void showMenu() {
        System.out.println();
        System.out.println("=== Simple Bank TUI ===");
        System.out.println("1) Create account");
        System.out.println("2) List accounts");
        System.out.println("3) Create card (optionally link to account)");
        System.out.println("4) List cards");
        System.out.println("5) Deposit to account");
        System.out.println("6) Withdraw from account");
        System.out.println("7) Charge card (withdraw via card)");
        System.out.println("8) Top-up card (deposit via card)");
        System.out.println("9) Transfer between cards");
        System.out.println("0) Exit");
        System.out.print("Select: ");
        flushConsole();
    }

    private void createAccount(Scanner sc) {
        System.out.print("Owner first name: ");
        String first = sc.nextLine().trim();
        System.out.print("Owner last name: ");
        String last = sc.nextLine().trim();
        String ownerId = "OWN-" + UUID.randomUUID();
        BankAccountOwner owner = ownerFactory.createBankAccountOwner(ownerId, first, last);

        System.out.print("Initial balance (numeric): ");
        double balance = Double.parseDouble(sc.nextLine().trim());

        System.out.print("Account type (1=base, 2=saving, 3=student): ");
        String t = sc.nextLine().trim();

        String acctNumber = "ACC-" + System.currentTimeMillis();
        BaseBankAccount account;
        switch (t) {
            case "2" -> account = accountFactory.createSavingBankAccount(acctNumber, owner, balance);
            case "3" -> account = accountFactory.createStudentBankAccount(acctNumber, owner, balance, "STU");
            default -> account = accountFactory.createBaseBankAccount(acctNumber, owner, balance);
        }

        accounts.add(account);
        logger.info("Created account " + account.getAccountNumber() + " for " + owner.getFullName() + " balance=" + account.getBalance());
        flushConsole();
    }

    private void listAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts created.");
            flushConsole();
            return;
        }
        System.out.println("Accounts:");
        for (BaseBankAccount a : accounts) {
            System.out.println("- " + a.getAccountNumber() + " | uuid=" + a.getUuid() + " | balance=" + a.getBalance()
                    + " | type=" + a.getClass().getSimpleName()
                    + " | owner=" + a.getOwner().getFullName());
        }
        flushConsole();
    }

    private void createCard(Scanner sc) {
        System.out.print("Link to account? (y/n): ");
        String link = sc.nextLine().trim().toLowerCase();
        BankAccountWithPaymentCards linkAccount = null;
        if (link.equals("y")) {
            System.out.print("Enter account number to link: ");
            String accNum = sc.nextLine().trim();
            BaseBankAccount found = findAccountByNumber(accNum);
            if (found == null) {
                System.out.println("Account not found");
                flushConsole();
                return;
            }
            // wrap if needed
            if (found instanceof BankAccountWithPaymentCards) {
                linkAccount = (BankAccountWithPaymentCards) found;
            } else {
                BankAccountWithPaymentCards wrapped = new BankAccountWithPaymentCards(
                        found.getUuid(),
                        found.getAccountNumber(),
                        found.getOwner(),
                        found.getBalance()
                );
                // replace in list so further ops use wrapped instance
                accounts.remove(found);
                accounts.add(wrapped);
                linkAccount = wrapped;
            }
        }

        // generate card using PaymentCardFactory then add to manager (keeps details consistent)
        String ownerName = (linkAccount != null) ? linkAccount.getOwner().getFullName() : "Standalone";
        PaymentCard gen = cardFactory.create(ownerName);

        if (linkAccount != null) {
            cardsManager.Create(gen.getCardNumber(), gen.getCvv(), gen.getExpireMonth(), gen.getExpireYear(), gen.getOwner(), linkAccount);
        } else {
            cardsManager.Create(gen.getCardNumber(), gen.getCvv(), gen.getExpireMonth(), gen.getExpireYear(), gen.getOwner());
        }
        System.out.println("Created card " + gen.getCardNumber() + (linkAccount != null ? " linked to " + linkAccount.getAccountNumber() : ""));
        flushConsole();
    }

    private void listCards() {
        List<PaymentCard> list = cardsManager.GetAll();
        if (list.isEmpty()) {
            System.out.println("No cards created.");
            flushConsole();
            return;
        }
        System.out.println("Cards:");
        for (PaymentCard c : list) {
            System.out.println("- " + c.getCardNumber() + " | owner=" + c.getOwner()
                    + " | linkedAccount=" + (c.getLinkedAccount() != null ? c.getLinkedAccount().getAccountNumber() : "none")
                    + " | balance=" + (c.getLinkedAccount() != null ? c.getLinkedAccount().getBalance() : "n/a"));
        }
        flushConsole();
    }

    private void depositToAccount(Scanner sc) {
        System.out.print("Account number: ");
        String acc = sc.nextLine().trim();
        BaseBankAccount a = findAccountByNumber(acc);
        if (a == null) { System.out.println("Account not found"); flushConsole(); return; }
        System.out.print("Amount: ");
        double amount = Double.parseDouble(sc.nextLine().trim());
        balanceService.deposit(a, amount);
        logger.info("Deposited " + amount + " to " + a.getAccountNumber() + ", newBalance=" + a.getBalance());
        flushConsole();
    }

    private void withdrawFromAccount(Scanner sc) {
        System.out.print("Account number: ");
        String acc = sc.nextLine().trim();
        BaseBankAccount a = findAccountByNumber(acc);
        if (a == null) { System.out.println("Account not found"); flushConsole(); return; }
        System.out.print("Amount: ");
        double amount = Double.parseDouble(sc.nextLine().trim());
        balanceService.withdraw(a, amount);
        logger.info("Withdrew " + amount + " from " + a.getAccountNumber() + ", newBalance=" + a.getBalance());
        flushConsole();
    }

    private void chargeCard(Scanner sc) {
        System.out.print("Card number: ");
        String cn = sc.nextLine().trim();
        PaymentCard card = cardsManager.findByNumber(cn);
        if (card == null) { System.out.println("Card not found"); flushConsole(); return; }
        System.out.print("Amount: ");
        double amt = Double.parseDouble(sc.nextLine().trim());
        boolean ok = manip.charge(card, amt);
        System.out.println("charge result: " + ok + (card.getLinkedAccount() != null ? ", balance=" + card.getLinkedAccount().getBalance() : ""));
        flushConsole();
    }

    private void topUpCard(Scanner sc) {
        System.out.print("Card number: ");
        String cn = sc.nextLine().trim();
        PaymentCard card = cardsManager.findByNumber(cn);
        if (card == null) { System.out.println("Card not found"); flushConsole(); return; }
        System.out.print("Amount: ");
        double amt = Double.parseDouble(sc.nextLine().trim());
        boolean ok = manip.topUp(card, amt);
        System.out.println("topUp result: " + ok + (card.getLinkedAccount() != null ? ", balance=" + card.getLinkedAccount().getBalance() : ""));
        flushConsole();
    }

    private void transferBetweenCards(Scanner sc) {
        System.out.print("From card number: ");
        String from = sc.nextLine().trim();
        System.out.print("To card number: ");
        String to = sc.nextLine().trim();
        PaymentCard cFrom = cardsManager.findByNumber(from);
        PaymentCard cTo = cardsManager.findByNumber(to);
        if (cFrom == null || cTo == null) { System.out.println("One or both cards not found"); flushConsole(); return; }
        System.out.print("Amount: ");
        double amt = Double.parseDouble(sc.nextLine().trim());
        boolean ok = manip.transfer(cFrom, cTo, amt);
        System.out.println("transfer result: " + ok
                + ", fromBalance=" + (cFrom.getLinkedAccount() != null ? cFrom.getLinkedAccount().getBalance() : "n/a")
                + ", toBalance=" + (cTo.getLinkedAccount() != null ? cTo.getLinkedAccount().getBalance() : "n/a"));
        flushConsole();
    }

    private BaseBankAccount findAccountByNumber(String accountNumber) {
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst().orElse(null);
    }

    // helper: ensure console output is flushed immediately
    private void flushConsole() {
        try {
            System.out.flush();
        } catch (Exception ignored) {
        }
    }
}
