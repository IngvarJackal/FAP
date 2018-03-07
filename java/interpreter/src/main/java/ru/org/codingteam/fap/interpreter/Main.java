package ru.org.codingteam.fap.interpreter;

import javafx.util.Pair;
import ru.org.codingteam.fap.infrastructure.Debug;
import ru.org.codingteam.fap.infrastructure.Node;
import ru.org.codingteam.fap.infrastructure.Program;
import ru.org.codingteam.fap.infrastructure.Stack;
import ru.org.codingteam.fap.infrastructure.context.Context;
import ru.org.codingteam.fap.runtime.Runtime;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("program has two args: name of the program and initial input");
            return;
        }

        String filename = args[0];
        String inStack = args[1];

        Program program = Parser.parse(new String(Files.readAllBytes(Paths.get(filename))), filename);

        Debug.debug("Program parserd " + program);

        ExecutorService executorService = Executors.newWorkStealingPool();
        Runtime runtime = new Runtime(executorService);

        List<Pair<Node, Context>> result = runtime.start(program, inStack);
        for (Pair<Node, Context> pair : result) {
            String stackname = runtime.processArg(pair.getKey().getArgs().get(0), pair.getValue()); // name of stack
            Stack stack = pair.getValue().getStack(stackname);
            System.out.println(pair.getKey() + " -> " + stack.makeReversedString());
        }
    }
}
