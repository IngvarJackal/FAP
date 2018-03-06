package ru.org.codingteam.fap.infrastructure;


import java.util.ArrayList;
import java.util.Objects;

public class Node {
    public enum CommandType {
        START, END, CREATE, DESTROY, POP, PUSH, CLONE, CALL, INSERT, COMMENT;
    }

    private final String originalText;

    private final CommandType type;
    private final ArrayList<String> args;
    private final ArrayList<Condition> conditions;

    public Node(String originalText, CommandType type) {
        this.originalText = originalText;
        this.type = type;
        args = new ArrayList<>();
        conditions = new ArrayList<>();
    }

    public CommandType getType() {
        return type;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public boolean addCondition(Condition condition) {
        return conditions.add(condition);
    }

    @Override
    public String toString() {
        return "["+originalText+"]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(originalText, node.originalText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalText);
    }
}
