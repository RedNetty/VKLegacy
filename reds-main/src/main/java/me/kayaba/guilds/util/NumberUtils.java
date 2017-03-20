package me.kayaba.guilds.util;

import java.util.*;

public final class NumberUtils {
    private static final Random rand = new Random();

    private NumberUtils() {

    }


    public static boolean isNumeric(String str) {
        return !str.isEmpty() && str.matches("[+-]?\\d*(\\.\\d+)?");
    }


    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }


    public static double roundOffTo2DecPlaces(double val) {
        return Math.round(val * 100D) / 100D;
    }


    public static long systemSeconds() {
        return System.currentTimeMillis() / 1000;
    }
}
