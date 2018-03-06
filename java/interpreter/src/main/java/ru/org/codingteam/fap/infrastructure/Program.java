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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Program{");
        sb.append("name='").append(name).append('\'');
        sb.append(", start=").append(start);
        sb.append('}');
        return sb.toString();
    }
}
