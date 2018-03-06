package ru.org.codingteam.fap.interpreter;

import ru.org.codingteam.fap.infrastructure.Condition;
import ru.org.codingteam.fap.infrastructure.Node;
import ru.org.codingteam.fap.infrastructure.Program;
import ru.org.codingteam.fap.infrastructure.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    public static Program parse(String program, String programName) {
        Map<String, Node> nodes = new HashMap<>();
        boolean fillingNodes = true;
        Node startingNode = null;
        for (String line : program.split("\n")) {
            if (fillingNodes) {
                if (line.equals("#")) {
                    fillingNodes = false;
                    continue;
                }
                Node node = addNode(line, nodes);
                if (startingNode != null && node != null) {
                    throw new ValidationException("Multiple starting nodes found! " + startingNode + " and " + node);
                }
                if (startingNode == null) {
                    startingNode = node;
                }
            } else {
                addEdge(line, nodes);
            }
        }
        return new Program(programName, startingNode);
    }

    private static Node addNode(String line, Map<String, Node> nodes) {
        String[] strings = line.split(" ");
        String number = strings[0];
        String command = strings[1];
        if (command.startsWith("#")) {
            return null;
        }
        List<String> args = new ArrayList<>(2);
        Node.CommandType type = null;
        switch (command) {
            case "create": {
                args.add(strings[2]); // name
                if (strings.length > 3) { // value
                    StringBuilder value = new StringBuilder();
                    for (int i = 3; i < strings.length; i++) {
                        value.append(strings[i]);
                    }
                    args.add(value.toString());
                }
                type = Node.CommandType.CREATE;
                break;
            }
            case "destroy": {
                args.add(strings[2]);
                type = Node.CommandType.DESTROY;
                break;
            }
            case "pop": {
                args.add(strings[2]);
                type = Node.CommandType.POP;
                break;
            }
            case "push": {
                args.add(strings[2]);
                type = Node.CommandType.PUSH;
                break;
            }
            case "clone": {
                args.add(strings[2]);
                args.add(strings[3]);
                type = Node.CommandType.CLONE;
                break;
            }
            case "call": {
                args.add(strings[2]);
                args.add(strings[3]);
                type = Node.CommandType.CALL;
                break;
            }
            case "insert": {
                args.add(strings[2]);
                type = Node.CommandType.INSERT;
                break;
            }
            case "start": {
                type = Node.CommandType.START;
                break;
            }
            case "end": {
                args.add(strings[2]);
                type = Node.CommandType.END;
                break;
            }
            default: {
                throw new ValidationException("Unknown command " + line.replace("\n", ""));
            }
        }

        Node node = new Node(line.replace("\n", ""), type);
        if (nodes.get(number) != null) {
            throw new ValidationException("Node is defined twice " + number);
        } else {
            nodes.put(number, node);
        }

        if (type.equals(Node.CommandType.START)) {
            return node;
        } else {
            return null;
        }
    }

    private static void addEdge(String line, Map<String, Node> nodes) {
        String[] strings = line.split(" ");
        Node from = nodes.get(strings[0]);
        Node to = nodes.get(strings[1]);

        if (from == null || to == null) {
            throw new ValidationException("Node is not found " + from + " -> " + to);
        }

        Condition condition = new Condition(strings[2], from, to);
        for (Condition nodeCondition: from.getConditions()) {
            if (condition.equals(nodeCondition)) {
                throw new ValidationException("Duplicated condition " + condition);
            }
        }
        from.addCondition(condition);
    }
}
