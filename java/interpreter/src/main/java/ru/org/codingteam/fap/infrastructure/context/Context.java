package ru.org.codingteam.fap.infrastructure.context;

import ru.org.codingteam.fap.infrastructure.Stack;
import ru.org.codingteam.fap.infrastructure.exceptions.StateException;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private HashMap<String, Stack> stacks = new HashMap<>();

    public Context copy() {
        HashMap<String, Stack> stacksCopy = new HashMap<>();

        for (Map.Entry<String, Stack> entry: stacks.entrySet()) {
            stacksCopy.put(entry.getKey(), entry.getValue().copy());
        }

        Context context = new Context();
        context.stacks.putAll(stacksCopy);
        return context;
    }

    public Stack getStack(String stackName) throws StateException {
        Stack stack = stacks.get(stackName);
        if (stack == null) {
            throw new StateException("No stack " + stackName);
        } else {
            return stack;
        }
    }

    public void makeStack(String stackName) throws StateException {
        stacks.put(stackName, new Stack());
    }

    public void makeStack(String stackName, String stackContent) throws StateException {
        Stack stack = new Stack();
        for (String s : stackContent.split("")) {
            stack.put(s);
        }
        stacks.put(stackName, stack);
    }

    public void removeStack(String stackName) throws StateException {
        Stack stack = stacks.remove(stackName);
        if (stack == null) {
            throw new StateException("No stack " + stackName);
        }
    }

    public void cloneStack(String stackName, String newStackName) throws StateException {
        Stack stack = stacks.get(stackName);
        if (stack == null) {
            throw new StateException("No stack " + stackName);
        } else {
            stacks.put(newStackName, stack.copy());
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Context{");
        sb.append("stacks=").append(stacks);
        sb.append('}');
        return sb.toString();
    }
}
