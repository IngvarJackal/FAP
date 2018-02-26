package ru.org.codingteam.fap.infrastructure;

import java.util.Collections;
import java.util.LinkedList;

public class Stack {
    private final LinkedList<String> list = new LinkedList<>();

    public String peek() {
        if (list.isEmpty()) {
            return "\\e";
        } else {
            return list.peekLast();
        }
    }

    public String pop() {
        if (list.isEmpty()) {
            return "\\e";
        } else {
            return list.removeLast();
        }
    }

    public void put(String value) {
        list.add(value);
    }

    public Stack copy() {
        Stack copy = new Stack();
        copy.list.addAll(list);
        return copy;
    }

    public String makeReversedString() {
        LinkedList<String> reversedList = new LinkedList<>(this.list);
        Collections.reverse(reversedList);
        return String.join("", reversedList);
    }

    @Override
    public String toString() {
        return String.join("|", list);
    }
}
