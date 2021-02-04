package com.home.automation.util;

import java.util.Random;

public final class CommonUtils {

    public static String createRandomSixDigitsId() {
        // TODO: fa o metod generala care primeste un int si genereaza un id pe baza nr de cifre indicate de int-ul ala
        // in cazul asta ar fi 6
        final Random rnd = new Random();
        return String.valueOf(rnd.nextInt(999999));
    }
}
