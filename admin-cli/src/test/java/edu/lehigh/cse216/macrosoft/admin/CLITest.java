package edu.lehigh.cse216.macrosoft.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CLITest extends TestCase {

    public CLITest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(CLITest.class);
    }

    public void testConstructor() {
        CLI cli = new CLI();
    }

}
