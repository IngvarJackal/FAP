package ru.org.codingteam.fap.interpreter;

import org.junit.Test;
import ru.org.codingteam.fap.infrastructure.Debug;

public class TestAll {
    @Test
    public void testAll() throws Exception {
        System.setProperty(Debug.DEBUG_PROPERTY, "true");
        Main.main(new String[] {"src/test/resources/hello_world.tgf", "ururu?"});
    }
}
