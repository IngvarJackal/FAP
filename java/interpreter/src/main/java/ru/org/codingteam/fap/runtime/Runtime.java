package ru.org.codingteam.fap.runtime;

import javafx.util.Pair;
import ru.org.codingteam.fap.infrastructure.Condition;
import ru.org.codingteam.fap.infrastructure.Node;
import ru.org.codingteam.fap.infrastructure.Program;
import ru.org.codingteam.fap.infrastructure.context.Context;
import ru.org.codingteam.fap.infrastructure.exceptions.FapException;
import ru.org.codingteam.fap.infrastructure.exceptions.StateException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class Runtime {
    private final ExecutorService executor;

    public Runtime(ExecutorService executor) {
        this.executor = executor;
    }

    public List<Pair<Node, Context>> start(Program program, String inStack) {
        Node start = program.getStart();
        Context state = new Context();
        state.makeStack("in", inStack);
        return evaluate(start, state);
    }

    private List<Pair<Node, Context>> evaluate(Node node, Context state) {
        try {
            return CompletableFuture.supplyAsync(() -> evaluateNode(node, state), executor)
                    .thenApplyAsync(nodes -> {
                        ArrayList<Pair<Node, Context>> doneNodes = new ArrayList<>();
                        for (Pair<Node, Context> curNode : nodes) {
                            if (curNode.getKey().getType().equals(Node.CommandType.END)) {
                                doneNodes.add(curNode);
                            } else {
                                doneNodes.addAll(evaluate(curNode.getKey(), curNode.getValue()));
                            }
                        }
                        return doneNodes;
                    }, executor).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FapException(e);
        }
    }

    private List<Pair<Node, Context>> evaluateNode(Node node, Context state) {
        /*
            STACK OPERATIONS
        */
        switch (node.getType()) {
            case CREATE: {
                if (node.getArgs().size() == 1) {
                    state.makeStack(processArg(node.getArgs().get(0), state));
                } else if (node.getArgs().size() == 2) {
                    if (node.getArgs().get(1).startsWith("#")) {
                        state.makeStack(processArg(node.getArgs().get(0), state), processArg(node.getArgs().get(1), state));
                    } else {
                        state.makeStack(processArg(node.getArgs().get(0), state), new StringBuilder(node.getArgs().get(1)).reverse().toString());
                    }
                }
                break;
            }
            case DESTROY: {
                state.removeStack(processArg(node.getArgs().get(0), state));
                break;
            }
            case CLONE: {
                state.cloneStack(processArg(node.getArgs().get(0), state), processArg(node.getArgs().get(1), state));
                break;
            }
            case POP: {
                String ch = state.getStack(processArg(node.getArgs().get(0), state)).pop();
                try {
                    state.getStack("in").put(ch);
                } catch (StateException e) { // if user deleted "in" stack for some reason, recreate it
                    state.makeStack("in");
                    state.getStack("in").put(ch);
                }
                break;
            }
            case PUSH: {
                String ch;
                try {
                    ch = state.getStack("in").pop();
                } catch (StateException e) {
                    throw new StateException("'in' stack is deleted!");
                }
                if (ch.equals("\\e")) {
                    // don't insert end of line
                } else {
                    state.getStack(processArg(node.getArgs().get(0), state)).put(ch);
                }
                break;
            }
        }

        /*
            LIBRARY OPERATIONS
        */
        switch (node.getType()) {
            case INSERT: {
                throw new RuntimeException("Not Implemented Yet");
            }
            case CALL: {
                throw new RuntimeException("Not Implemented Yet");
            }
        }

        /*
            NEXT STEP OPERATIONS
        */
        ArrayList<Pair<Node, Context>> nextNodes = new ArrayList<>();
        for (Condition condition : node.getConditions()) {
            if (condition.matches(state.getStack("in").peek())) {
                Context newState = state.copy();
                if (!condition.isEmpty()) {
                    newState.getStack("in").pop();
                }
                nextNodes.add(new Pair<>(condition.getTo(), newState));
            }
        }

        return nextNodes;
    }

    public String processArg(String arg, Context state) {
        if (arg.startsWith("#")) {
            return state.getStack(arg.substring(1)).makeReversedString();
        } else {
            return arg;
        }
    }
}
