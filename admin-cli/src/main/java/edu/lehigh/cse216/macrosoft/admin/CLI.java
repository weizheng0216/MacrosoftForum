package edu.lehigh.cse216.macrosoft.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * The {@code CLI} represents a simple general purpose commandline interface.
 * Once run, a classic repl interface will be displayed to standard out.  Users
 * can enter commands followed by various numbers of arguments, each separated
 * by some whitespaces.
 * <p>
 * The {@code CLI} instance keeps an internal command map, which maps command
 * names to {@code CLI.Command} instances.  Each {@code CLI.Command} instance
 * contains a name, a syntax hint, a description and an {@code CLI.Executable},
 * which represent the action to perform when the command gets called.  You can
 * add new commands or override existing commands anytime you want.
 * <p>
 * Here is an example:
 * <blockquote><pre>
 *     CLI cli = new CLI();
 *     cli.registerCommand("foo", (args, util) -> {
 *         for (String arg : args) {
 *             System.out.println(arg);
 *         }
 *         return util.OK;
 *     })
 *     cli.addSyntaxHint("foo", "foo [OPTION]...");
 *     cli.addDescription("foo",
 *             "Print out each argument passed and exit with OK.");
 *     cli.run();
 * </pre></blockquote><p>
 * The program above runs the CLI, with the only custom command being <em>foo</em>.
 * The action <em>foo</em> performs is defined by the lambda expression with
 * two arguments.  The {@code args} is the argument list the user entered.
 * The first * element of the argument list is always the command itself.
 * In this case, {@code args[0]} is {@code "foo"}.  The object {@code util}
 * contains the utility functions you could call in the command executable.
 * Note that if you want to read user input during the execution of the command,
 * you must use the {@code readString} method provided by this util object.
 * All other features, including the recommended return codes, are optional.
 * <p>
 * Note that {@code addSyntaxHint} and {@code addDescription} on a command
 * string should only be called after you have registered that command string
 * with {@code registerCommand}.  Calling {@code registerCommand} on the same
 * command string multiple times will override the previous registrations.
 * <p>
 * There some built-in commands, like "help", which are defined in the class
 * constructor.
 *
 * @author Haocheng Gao
 */
public class CLI {

    /**
     * The return code of the last command executed.
     */
    private int lastStatus;

    /**
     * Repl prompt string.  Could be changed by the "promptstr" command.
     */
    private String promptStr = "[$?] admin> ";

    /**
     * Reader of the stdin.  Commands should always use this reader to obtain
     * user inputs.
     */
    private BufferedReader reader;

    /**
     * Create a {@code CLI} instance.  Since it uses the standard out for
     * output, running multiple CLI instances is never a good idea.
     */
    CLI() {
        /* Built-In Command "help" */
        String helpHelp = "If no arguments are provided, a list containing the " +
                "syntax of all available commands will be printed. If one or "   +
                "more arguments are provided, a list of a more detailed list "   +
                "with syntax and description of each specified command will "    +
                "be printed.";
        registerCommand("help", (args, util) -> {
            if (args.length == 1) {
                // Print all commands in alphabetical order
                Command[] allCommands = new Command[commandSet.size()];
                commandSet.values().toArray(allCommands);
                Arrays.sort(allCommands, Comparator.comparing(Command::getName));
                for (Command c : allCommands) {
                    System.out.printf(
                            "\t%-30s\t%s\n",
                            util.ansiEscape(c.getName(), "1"),
                            c.syntax);
                }
                return util.OK;
            }
            // Only print the required helps with description
            int r = util.OK;  // Return code
            for (int i = 1; i < args.length; i++) {
                Command cmd = commandSet.get(args[i]);
                if (cmd == null) {
                    r = util.INVALID_ARG;
                    System.out.printf("Command \"%s\" is unknown.\n\n", args[i]);
                    continue;
                }
                System.out.printf(
                        "\t%-30s\t%s\n%s\n\n",
                        util.ansiEscape(args[i], "1"),
                        util.ansiEscape(cmd.syntax, "34;4"),
                        cmd.description);
            }
            return r;
        });
        addSyntaxHint("help", "help [CMD]...");
        addDescription("help", helpHelp);

        /* Built-In Command "promptstr" */
        String promptStrHelp = "Change the prompt string. Default: \"[$?] admin> \".";
        registerCommand("promptstr", (args, util) -> {
            if (args.length != 1) {
                promptStr = args[1];
            }
            return util.OK;
        });
        addSyntaxHint("promptstr", "promptstr <STR>");
        addDescription("promptstr", promptStrHelp);

        /* Built-In Command "exit" */
        registerCommand("exit", (args, util) -> util.OK);
        addSyntaxHint("exit", "exit");
        addDescription("exit", "Exit the application.");

    }

    /**
     * The instance of {@code Executable} represents the function to be run
     * when a command is called.  This function, the {@code run} method,
     * takes the entire argument list passed by the CLI user as input, does
     * its job, print feedbacks, and finally outputs a return code following
     * the convention of zero being OK.
     */
    interface Executable {
        /**
         * Perform the appropriate actions of the command with given parameters.
         * @param arguments the entire argument list, with the first element
         *                  is always being the command itself.
         * @param util      a collection of utility methods that could be
         *                  useful for writing commands executables.
         * @return the return code of the command.  There are some recommended
         *         return codes to indicate the common exceptions that could
         *         happen defined in the util object.  However you can also
         *         make your own return codes.
         */
        int run(String[] arguments, Util util);
    }

    /**
     * Just a simple type that binds some related data together to form
     * a {@code Command}.
     */
    private static class Command {
        private final String name;
        private String syntax = "";
        private String description = "";
        private final Executable executable;

        private Command(String name, Executable exe) {
            this.name = name;
            executable = exe;
        }

        String getName() {
            return name;
        }
    }

    /**
     * A collection of utility functions that could be useful to CLI and the
     * executables registered to the CLI.  An immutable instance of this class
     * is passed as the second argument of every run of command.
     */
    class Util {
        private Util() {}

        // Recommended return codes
        final int OK = 0;             // OK
        final int INVALID_CMD = 1;    // Can't find the command
        final int INVALID_ARG = 2;    // At least one argument is invalid
        final int RUNTIME_ERR = 4;    // Error occurred during runtime of the command

        /**
         * Wrap a {@code String} with ANSI escape code.
         * @param text content to be wrapped.
         * @param csi ANSI CSI code.
         * @return wrapped content.
         */
        String ansiEscape(String text, String csi) {
            return String.format("\033[%sm%s\033[0m", csi, text);
        }

        /**
         * Read a line of {@code String} from standard in.
         * @return The {@code String} read, or an empty {@code String} upon
         *         error.  {@code null} if the user issues an EOF signal.
         */
        String readString() {
            try {
                return reader.readLine();
            } catch (IOException exp) {
                exp.printStackTrace();
                return "";
            }
        }

        /**
         * Execute a command.
         * @param args The argument list to be passed to the executing command.
         *             The first element of this vararg will identify the name
         *             of the command to be executed.
         * @return The return code of the command that gets executed. If the
         *         argument is invalid, {@code util.INVALID_ARG} is returned.
         *         If the command is not defined, {@code util.INVALID_CMD} is
         *         returned.
         */
        int exec(String... args) {
            if (args == null || args.length == 0) return util.INVALID_ARG;
            Command cmd = commandSet.get(args[0]);
            if (cmd == null) return util.INVALID_CMD;
            return cmd.executable.run(args, util);
        }
    }

    /**
     * This immutable utility instance is passed to every command executable.
     */
    private final Util util = new Util();

    /**
     * All commands, including the built-in ones, are registered in this map.
     * Each time when user issues a command, {@code CLI} queries to this map
     * to obtain the corresponding {@code Command} instance. If it cannot be
     * found, it means the command is invalid.
     */
    private final HashMap<String, Command> commandSet = new HashMap<>();

    /**
     * Register a new command or override an existing command.  The command is
     * identified with its name, {@code cmdStr}, and will execute the {@code cmd}
     * upon user calls.
     * @param cmdStr the identifier/name of the command.
     * @param cmd    the action to perform.
     */
    void registerCommand(String cmdStr, Executable cmd) {
        if (cmdStr == null || cmd == null) return;
        commandSet.put(cmdStr, new Command(cmdStr, cmd));
    }

    /**
     * Provide syntax hint to an existing command.  The purpose of it is to
     * provide a quick reminder.  If the command hasn't been registered when
     * this function is called, nothing will happen. The default value is
     * empty string {@code ""}.
     * @param cmdStr the identifier/name of the target command.
     * @param syntax the syntax string of the target command.
     */
    void addSyntaxHint(String cmdStr, String syntax) {
        if (cmdStr == null) return;
        Command cmd = commandSet.get(cmdStr);
        if (cmd != null) cmd.syntax = syntax;
    }

    /**
     * Provide detailed description to an existing command.  The purpose of
     * it is to provide full documentation of registered commands.  If the
     * command hasn't been registered when this function is called, nothing
     * will happen.  The default value is empty string{@code ""}.
     * @param cmdStr the identifier/name of the target command.
     * @param text   the description string of the target command.
     */
    void addDescription(String cmdStr, String text) {
        if (cmdStr == null) return;
        Command cmd = commandSet.get(cmdStr);
        if (cmd != null) cmd.description = text;
    }

    /**
     * Initiate the CLI and start interacting with user. This method will
     * block the execution of the program and keeps waiting for user inputs,
     * until the user enters "exit".
     */
    void run() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        for (;;) {
            printPromptStr();
            String[] inputs = readLineAsArgs();
            if (inputs == null || inputs[0].equals("exit")) {
                if (inputs == null) System.out.println();
                closeReader();
                return;
            }
            if (inputs[0].equals("")) continue;
            if (commandSet.containsKey(inputs[0])) {
                lastStatus = commandSet.get(inputs[0]).executable.run(inputs, util);
            } else {
                lastStatus = util.INVALID_CMD;
                System.err.printf("Unknown command \"%s\".\n", inputs[0]);
            }
        }
    }

    /**
     * Print the prompt string to let user know that the CLI is waiting for
     * an command.  All occurrences of <em>$?</em> will be replaced with the
     * return code of the last command.  If the return code happens to be
     * one of the standard return codes defined in {@code Util} object,
     * the text of the return code will be printed.
     */
    private void printPromptStr() {
        String status;
        if (lastStatus == util.OK) {
            status = util.ansiEscape("OK", "32");
        } else {
            status = lastStatus == util.INVALID_CMD ? "INVALID_CMD" :
                     lastStatus == util.INVALID_ARG ? "INVALID_ARG" :
                     lastStatus == util.RUNTIME_ERR ? "RUNTIME_ERR" :
                     Integer.toString(lastStatus);
            status = util.ansiEscape(status, "31");
        }
        System.out.print(promptStr.replaceAll("\\$\\?", status));
    }

    /**
     * Read the user input and split it into arguments stored in an {@code String}
     * array.
     * @return The argument array, with the first element always being the
     *         command itself.  Empty string if the user enters an empty line
     *         or if an error occurred.  {@code null} if the user issues an
     *         EOF signal.
     */
    private String[] readLineAsArgs() {
        String input = util.readString();
        if (input == null) return null;
        return input.trim().split("\\s+");
    }

    /**
     * Close the reader.
     */
    private void closeReader() {
        try {
            if (reader != null) reader.close();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

}
