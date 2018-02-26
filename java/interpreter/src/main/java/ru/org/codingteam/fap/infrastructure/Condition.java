package ru.org.codingteam.fap.infrastructure;

import java.util.function.Function;

public class Condition {
    private final String text;
    private final Node next;

    private Function<String, Boolean> matcher;

    public Condition(String text, Node next) {
        this.text = text;
        this.next = next;
        if (text.equalsIgnoreCase("\\?")) {
            matcher = s -> !s.equals("\\e");
        } else {
            matcher = s -> s.equals(text);
        }
    }

    public Boolean matches(String s) {
        return matcher.apply(s);
    }

    public Boolean isEmpty() {
        return text.isEmpty();
    }

    public Node getNext() {
        return next;
    }

    @Override
    public String toString() {
        return text + " -> " + next;
    }
}
