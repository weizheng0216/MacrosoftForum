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

    /**
     * Test for constructor. The constructor should create the built-in
     * commands.
     */
    public void testConstructor() {
        CLI cli = new CLI();
        assertTrue(cli.containsCommand("exit"));
        assertTrue(cli.containsCommand("promptstr"));
        assertTrue(cli.containsCommand("help"));

    }

    /**
     * Test for command registration. Custom commands should be registered.
     */
    public void testRegistration() {
        String testSyntax = "test";
        String testDescription = "Description: Hello!";
        CLI cli = new CLI();  // Create a new CLI instance
        cli.registerCommand("test", (args, util) -> {
            System.out.println("Test command.");
            return 100;
        });
        cli.addSyntaxHint("test", testSyntax);
        cli.addDescription("test", testDescription);
        assertTrue(cli.containsCommand("test"));
        assertEquals(testSyntax, cli.getSyntaxHint("test"));
        assertEquals(testDescription, cli.getDescription("test"));

        // override
        testSyntax = "woof";
        testDescription = "$%!9813 P";
        cli.registerCommand("test", (args, util) -> {
            System.out.println("Test command.");
            return 100;
        });
        cli.addSyntaxHint("test", testSyntax);
        cli.addDescription("test", testDescription);
        assertTrue(cli.containsCommand("test"));
        assertEquals(testSyntax, cli.getSyntaxHint("test"));
        assertEquals(testDescription, cli.getDescription("test"));

        // multiple commands
        testSyntax = "woof";
        testDescription = "$%!9813 P";
        cli.registerCommand("test2", (args, util) -> {
            System.out.println("Test command.");
            return 100;
        });
        cli.addSyntaxHint("test2", testSyntax);
        cli.addDescription("test2", testDescription);
        assertTrue(cli.containsCommand("test2"));
        assertEquals(testSyntax, cli.getSyntaxHint("test2"));
        assertEquals(testDescription, cli.getDescription("test2"));
    }

    /**
     * Test for command registration with null content. There should not be errors.
     */
    public void testRegistrationNull() {
        String testSyntax = null;
        String testDescription = null;
        CLI cli = new CLI();  // Create a new CLI instance
        cli.registerCommand(null, (args, util) -> {
            System.out.println("Test command.");
            return 100;
        });
        cli.addSyntaxHint(null, testSyntax);
        cli.addDescription(null, testDescription);
        assertFalse(cli.containsCommand(null));
        // All codes should run without error, but nothing should
        // be registered
    }

}
