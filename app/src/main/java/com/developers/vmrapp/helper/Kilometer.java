package com.developers.vmrapp.helper;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Kilometer {

    @NonNull
    public static String getKilometer(Integer value) {

        return String.format(Locale.ENGLISH,
                "%,d", value) + " KM";

    }

}
