package org.example.accounts.card;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PaymentCardExpireCalculator {

    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("MM");
    private static final DateTimeFormatter YEAR_FMT = DateTimeFormatter.ofPattern("yy");

    private static final byte DEFAULT_EXPIRE_YEARS = 5;

    public String calculateMonthExpire() {
        LocalDate today = LocalDate.now();
        return today.format(MONTH_FMT);
    }

    public String calculateYearExpire() {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusYears(DEFAULT_EXPIRE_YEARS);
        return future.format(YEAR_FMT);
    }

}