package ru.org.codingteam.fap.infrastructure;


import java.util.ArrayList;

public class Node {
    public enum CommandType {
        START, END, CREATE, DESTROY, POP, PUSH, CLONE, INVOKE, SUBSTITUTE, COMMENT;
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

    @Override
    public String toString() {
        return originalText;
    }
}
