package org.example;

import org.example.Logger.BankLogger;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BankLogger logger = BankLogger.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Select mode:");
        System.out.println("1) App (automated test flow)");
        System.out.println("2) AppTUI (interactive text UI)");
        System.out.print("Enter choice (1/2): ");

        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1" -> {
                new App().run();
            }
            case "2" -> {
                try {
                    AppTUI appTUI = new AppTUI();
                    appTUI.run();
                } catch (NoClassDefFoundError | Exception e) {
                    logger.severe("AppTUI not available or failed to start: " + e.getMessage());
                    System.out.println("AppTUI is not available. Falling back to App.");
                    new App().run();
                }
            }
            default -> {
                System.out.println("Unknown option. Running App by default.");
                new App().run();
            }
        }

        sc.close();
    }
}
