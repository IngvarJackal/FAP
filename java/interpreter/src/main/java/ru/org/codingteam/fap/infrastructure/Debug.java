package ru.org.codingteam.fap.infrastructure;

public class Debug {
    public static String DEBUG_PROPERTY = "fap.interpreter.debug";

    public static void debug(Object debug) {
        if ("true".equalsIgnoreCase(System.getProperty(DEBUG_PROPERTY))) {
            System.out.println(debug);
        }
    }
}
