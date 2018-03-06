package ru.org.codingteam.fap.interpreter;

import javafx.util.Pair;
import ru.org.codingteam.fap.infrastructure.Node;
import ru.org.codingteam.fap.infrastructure.Program;
import ru.org.codingteam.fap.infrastructure.Stack;
import ru.org.codingteam.fap.infrastructure.context.Context;
import ru.org.codingteam.fap.infrastructure.exceptions.FapException;
import ru.org.codingteam.fap.runtime.Runtime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        String filename = args[0];
        String inStack = args[1];

        StringBuilder sb = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(filename);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
        } catch(IOException e) {
            throw new FapException(e);
        }

        Program program = Parser.parse(sb.toString(), filename);

        ExecutorService executorService = Executors.newWorkStealingPool();
        Runtime runtime = new Runtime(executorService);

        List<Pair<Node, Context>> result = runtime.start(program, inStack);
        for (Pair<Node, Context> pair : result) {
            String stackname = runtime.processArg(pair.getKey().getArgs().get(0), pair.getValue()); // name of stack
            Stack stack = pair.getValue().getStack(stackname);
            System.out.println(pair.getKey() + " -> " + stack);
        }
    }
}
