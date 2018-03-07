package ru.org.codingteam.fap.interpreter;

import org.junit.Test;
import ru.org.codingteam.fap.infrastructure.Debug;

public class TestGreeter {
    @Test
    public void testAll() throws Exception {
        Main.main(new String[] {"src/test/resources/greeter/greeter.tgf", "Ingvar"});
        Main.main(new String[] {"src/test/resources/greeter/greeter.tgf", "Fornever"});
    }
}
