package ru.org.codingteam.fap.infrastructure;

import ru.org.codingteam.fap.infrastructure.exceptions.ValidationException;

import java.util.Objects;
import java.util.function.BiFunction;

public class Condition {
    private final String text;
    private final Node to;
    private final Node from;

    private BiFunction<String, Node, Boolean> matcher;

    public Condition(String text, Node from, Node to) {
        this.text = text;
        this.from = from;
        this.to = to;
        if (text == null || text.equalsIgnoreCase("\\?")) { // empty arrow
            matcher = (s, node) -> {
                for (Condition condition : node.getConditions()) {
                    if (!condition.equals(this)) {
                        if (condition.matches(s)) {
                            return false;
                        }
                    }
                }
                return !s.equals("\\e");
            };
        } else if (text.equalsIgnoreCase("\\e")) { // end of string
            matcher = (s, node) -> {
                return s.equals(text);
            };
        } else {
            if (text.isEmpty()) {
                throw new ValidationException("illegal transition identifier " + text);
            } else {
                matcher = (s, node) -> {
                    return s.equals(text);
                };
            }
        }
    }

    public Boolean matches(String s) {
        return matcher.apply(s, from);
    }

    public Boolean isEmpty() {
        return text == null;
    }

    public Node getTo() {
        return to;
    }

    @Override
    public String toString() {
        return from + " --(" + (text != null ? text : " ") + ")-> " + to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return Objects.equals(text, condition.text) &&
                Objects.equals(from, condition.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, from);
    }
}
