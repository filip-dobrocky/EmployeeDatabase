package cz.vutbr.feec.xdobro22.employeedatabase.tests;

import static org.junit.Assert.*;
import cz.vutbr.feec.xdobro22.employeedatabase.Actions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ActionsTest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream systemOut = System.out;

    @org.junit.Before
    public void setUpStreams() {
        System.setOut(new PrintStream(out));
    }

    @org.junit.After
    public void restoreStreams() {
        System.setOut(systemOut);
    }

    @org.junit.Test
    public void testVowelCount() {
        assertEquals(Actions.getVowelCount("Ahoj"), 2);
    }

    @org.junit.Test
    public void testReversed() {
        Actions.printReversed("Ahoj");
        assertEquals(out.toString(), "johA");
    }
}