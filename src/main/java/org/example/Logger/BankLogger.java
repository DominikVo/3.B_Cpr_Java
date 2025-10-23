// java
package org.example.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;
import java.io.File;
import java.io.IOException;

public class BankLogger {
    private static final DateTimeFormatter FILE_NAME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final BankLogger logger = new BankLogger();

    private Logger delegate;
    private String currentTimestamp;

    private BankLogger(){
        currentTimestamp = LocalDateTime.now().format(FILE_NAME_FMT);
        delegate = Logger.getLogger("org.example");
        delegate.setUseParentHandlers(false);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        ch.setFormatter(new SimpleFormatter());
        delegate.addHandler(ch);

        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        String fileName = "logs" + File.separator + "bank-" + currentTimestamp + ".txt";
        try {
            FileHandler fh = new FileHandler(fileName, true);
            fh.setLevel(Level.ALL);
            fh.setFormatter(new SimpleFormatter());
            try {
                fh.setEncoding("UTF-8");
            } catch (Exception ignored) { }
            delegate.addHandler(fh);
        } catch (IOException | SecurityException e) {
            delegate.severe("Failed to initialize file handler for logger: " + e.getMessage());
        }

        delegate.setLevel(Level.ALL);
    }

    public static BankLogger getInstance(){
        return logger;
    }

    public void info(String msg){
        delegate.info(msg);
    }

    public void warning(String msg){
        delegate.warning(msg);
    }

    public void severe(String msg){
        delegate.severe(msg);
    }

    public void fine(String msg){
        delegate.fine(msg);
    }

    public void logTransaction(String accountNumber, String accountNumber1, double amount) {
        String logMessage = String.format("Transaction: from account %s to account %s amount %.2f", accountNumber, accountNumber1, amount);
        delegate.info(logMessage);
    }
}
