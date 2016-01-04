package de.grundid.drinker.utils;


public class Utils {
    public static Double getPricePerVolume(int price, Integer volume) {
        if (volume != null) {
           return ((double) price / 100) / ((double) volume / 100);
        } else {
            return Double.NaN;
        }

    }
}
