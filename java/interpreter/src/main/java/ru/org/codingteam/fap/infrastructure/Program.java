package ru.org.codingteam.fap.infrastructure;

public class Program {
    private String name;
    private Node start;

    public Program(String name, Node start) {
        this.name = name;
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public Node getStart() {
        return start;
    }
}
