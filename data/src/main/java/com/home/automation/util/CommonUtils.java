package com.home.automation.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public final class CommonUtils {

    private static final String DEFAULT_TIME_FORMAT = "dd-LLLL-yyyy HH:mm:s";

    public static String createRandomSixDigitsId() {
        // TODO: fa o metod generala care primeste un int si genereaza un id pe baza nr de cifre indicate de int-ul ala
        // in cazul asta ar fi 6
        final Random rnd = new Random();
        return String.valueOf(rnd.nextInt(9999999));
    }

    public static String getCurrentDate() {
        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);
        return DATE_TIME_FORMATTER.format(LocalDate.now());
    }
}
