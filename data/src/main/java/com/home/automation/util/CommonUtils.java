package com.home.automation.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public final class CommonUtils {

    private static final String DEFAULT_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";

    public static String createRandomSixDigitsId() {
        // TODO: fa o metod generala care primeste un int si genereaza un id pe baza nr de cifre indicate de int-ul ala
        // in cazul asta ar fi 6
        final Random rnd = new Random();
        return String.valueOf(rnd.nextInt(9999999));
    }

    public static String getCurrentDate() {
        final LocalDateTime ldt = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);
        return ldt.format(fmt);
    }
}
