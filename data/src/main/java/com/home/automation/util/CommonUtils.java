package com.home.automation.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public final class CommonUtils {

    public static final String DAY_MONTH_TIME_PATTERN = "dd-MMM HH:mm:ss";
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

    public static String convertTimeToString(final LocalDateTime dateTime, final String pattern) {
        if (dateTime == null || StringUtils.isBlank(pattern)) {
            return null;
        }
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter); // "1986-04-08 12:30"
    }
}
