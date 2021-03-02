package edu.lehigh.cse216.macrosoft.admin;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Admin App of Buzz, which allows administrator of Buzz to create table,
 * drop table, and perform other maintenance jobs though command-line interface.
 *
 * This is a stand-alone application that is completely detached with the
 * front-end and the back-end of Buzz.  The only facility it interact with
 * is the Postgres Database.  User would need to provide a URL of the database
 * as a command-line argument of this application.
 *
 * @author Haocheng Gao
 */
public class App {

    /**
     * The instance that will handle all database operations.
     */
    private static Database database;

    public static void main(String[] args) {
        String dbURL = "postgres://vdtksuqjtzvetb:b7ccb5e707b07d8c8bfdf7" +
                "badbae2048282884d6b2e8ad336a71ff5833b2abc3@ec2-52-22-16" +
                "1-59.compute-1.amazonaws.com:5432/d9m8d6ulhh2bbk";

        // Get URL from command line
        if (args.length >= 1) {
            dbURL = args[0];
        }

        // Create & connect to the database
        try {
            database = Database.getInstance(dbURL);
        } catch (SQLException |
                 ClassNotFoundException |
                 URISyntaxException exp) {
            System.out.println(exp.getMessage());
            System.exit(-1);
        }

        // Start the CLI
        CLI cli = new CLI();
        registerCommands(cli);
        cli.run();

        // Always disconnect from the database
        try {
            database.disconnect();
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
    }

    /**
     * Register commands to the admin-cli. The purpose of the method is to
     * preventing the {@code main} method from being too long. If the project
     * grows very large at some point, it may also be a good idea to move
     * the command registrations into separate files.
     * @param cli provided the {@code CLI} instance to register on.
     */
    private static void registerCommands(CLI cli) {
        // *******************************************************************************
        // *                                createtable
        // *******************************************************************************
        String createTableHelp = "Create a new table.";
        cli.registerCommand("createtable", (args, util) -> {
            try {
                database.createTable();
                System.out.println("Table is created.");
                return util.OK;
            } catch (SQLException exp) {
                System.out.println(exp.getMessage());
                return util.RUNTIME_ERR;
            }
        });
        cli.addSyntaxHint("createtable", "createtable");
        cli.addDescription("createtable", createTableHelp);


        // *******************************************************************************
        // *                                 droptable
        // *******************************************************************************
        String dropTableHelp = "Drop the table.";
        cli.registerCommand("droptable", (args, util) -> {
            String confirm = "Yes, I am sure";
            try {
                // Must enter confirm message first
                System.out.println("You are going to delete everything, please type:\n" +
                        util. ansiEscape(confirm, "33"));
                if (util.readString().trim().equals(confirm)) {
                    // Confirmed, drop table
                    database.dropTable();
                    System.out.println("Table is dropped.");
                    return util.OK;
                }
                // Didn't confirm, abort
                System.out.println("Input didn't match, operation is canceled.");
            } catch (SQLException exp) {
                System.out.println(exp.getMessage());
            }
            return util.RUNTIME_ERR;
        });
        cli.addSyntaxHint("droptable", "droptable");
        cli.addDescription("droptable", dropTableHelp);


        // *******************************************************************************
        // *                                     list
        // *******************************************************************************
        String listHelp = "List messages. Print the entire message table if " +
                "no arguments are passed. Otherwise only the required fields " +
                "of the required messages(identified by ID) will be printed.";
        cli.registerCommand("list", (args, util) -> {
            List<Integer> requiredIDs = new ArrayList<>();
            List<String> requiredFields = new ArrayList<>();

            int r = util.OK;  // return code
            // Parse args
            int i = 1;
            for (; i < args.length; i++) {
                try {
                    int id = Integer.parseInt(args[i]);
                    requiredIDs.add(id);
                } catch (NumberFormatException exp) {
                    break;
                }
            }
            for (; i < args.length; i++) {
                String ptn = "id|title|content|date|likes|" +
                        "dislikes|pinned|username";
                if (args[i].toLowerCase().matches(ptn)) {
                    requiredFields.add(args[i].toLowerCase());
                } else {
                    System.out.printf("Invalid argument: %s;\n", args[i]);
                    r = util.INVALID_ARG;
                }
            }
            // Get messages table
            ArrayList<Database.Message> messages;
            try {
                messages = database.selectAllMessages();
            } catch (SQLException exp) {
                System.out.printf("Error, %s\n", exp.getMessage());
                return util.RUNTIME_ERR;
            }
            // ===== Print result =====
            // Prep
            List<String> allFields = Arrays.asList("title", "date", "likes",
                    "dislikes", "pinned", "username", "content");
            if (requiredFields.isEmpty()) {
                requiredFields = allFields;
            }
            // Print content
            for (Database.Message msg : messages) {
                if (requiredIDs.isEmpty() || requiredIDs.contains(msg.mID)) {
                    System.out.printf("ID: %s\n", msg.mID);  // always print ID
                    if (requiredFields.contains("title"))
                        System.out.printf("Title: %s\n", msg.mTitle);
                    if (requiredFields.contains("date"))
                        System.out.printf("Date: %s\n", msg.mDate);
                    if (requiredFields.contains("likes"))
                        System.out.printf("Likes: %d\n", msg.mUpVotes);
                    if (requiredFields.contains("dislikes"))
                        System.out.printf("Dislikes: %d\n", msg.mDownVotes);
                    if (requiredFields.contains("pinned"))
                        System.out.printf("Pinned: %s\n", msg.mPinned);
                    if (requiredFields.contains("username"))
                        System.out.printf("Username: %s\n", msg.mUsername);
                    if (requiredFields.contains("content"))
                        System.out.printf("Content: %s\n", msg.mContent);
                    System.out.println();
                }
            }
            return r;
        });
        cli.addSyntaxHint("list", "list [ID]... [FIELDS]...");
        cli.addDescription("list", listHelp);


        // *******************************************************************************
        // *                                   insert
        // *******************************************************************************
        String insertHelp = "Create a new message in the database. You can " +
                "also set the initial values of each field by passing the " +
                "optional arguments. If you set any value to \"?\", you will " +
                "be prompted to enter the value later.";
        cli.registerCommand("insert", (args, util) -> {
            // Default values of the new message
            String title = "Just another message";
            String content = "Content is missing!";
            int upVotes = 0;
            int downVotes = 0;
            boolean pinned = false;
            String username = "";

            int r = util.OK;  // return code

            try {
                // Modify initial values
                for (int i = 1; i < args.length; i++) {
                    String ptn = "(title|content|likes|" +
                            "dislikes|pinned|username)=(.+)";
                    Matcher m = Pattern.compile(ptn).matcher(args[i]);
                    if (m.matches()) {
                        String field = m.group(1);
                        String value = m.group(2);
                        if (value.equals("?")) {
                            System.out.printf("Enter %s: ", field);
                            value = util.readString();
                        }
                        switch (field) {
                            case "title":
                                title = value;
                                break;
                            case "content":
                                content = value;
                                break;
                            case "likes":
                                upVotes = Integer.parseInt(value);
                                break;
                            case "dislikes":
                                downVotes = Integer.parseInt(value);
                                break;
                            case "pinned":
                                pinned = Boolean.parseBoolean(value);
                                break;
                            case "username":
                                username = value;
                        }
                    } else {
                        System.out.printf("Invalid argument: %s;\n", args[i]);
                        r = util.INVALID_ARG;
                    }
                }
                // Insert message to the database
                database.insertMessage(title, content, upVotes,
                        downVotes, pinned, username);
                System.out.println("Message inserted");
            } catch (SQLException | NumberFormatException exp) {
                r = util.RUNTIME_ERR;
                System.out.printf("Error, %s\n", exp.getMessage());
            }
            return r;
        });
        cli.addSyntaxHint("insert", "insert [<FIELD>=<VALUE>]...");
        cli.addDescription("insert", insertHelp);


        // *******************************************************************************
        // *                                   delete
        // *******************************************************************************
        String deleteHelp = "Delete the messages identified with given IDs.";
        cli.registerCommand("delete", (args, util) -> {
            // Parse args
            if (args.length < 2) {
                util.exec("help", "delete");
                return util.INVALID_ARG;
            }
            int r = util.OK;  // return code
            // Delete each message
            for (int i = 1; i < args.length; i++) {
                try {
                    System.out.printf("%s: ", args[i]);
                    database.deleteMessageById(Integer.parseInt(args[i]));
                    System.out.println("deleted;");
                } catch (SQLException exp) {
                    r |= util.RUNTIME_ERR;
                    System.out.printf("Error, %s\n", exp.getMessage());
                } catch (NumberFormatException exp) {
                    r |= util.INVALID_ARG;
                    System.out.println("Error, ID must be integer");
                }
            }
            return r;
        });
        cli.addSyntaxHint("delete", "delete <ID>...");
        cli.addDescription("delete", deleteHelp);


        // *******************************************************************************
        // *                                   update
        // *******************************************************************************
        String updateHelp = "Update a message in the database by ID. If you " +
                "set any value to \"?\", you will be prompted to enter the " +
                "value later.";
        cli.registerCommand("update", (args, util) -> {
            // Parse args
            if (args.length < 2) {
                util.exec("help", "update");
                return util.INVALID_ARG;
            }
            int r = util.OK;  // return code
            try {
                // 1. Get message
                int id = Integer.parseInt(args[1]);
                Database.Message msg = database.selectMessageById(id);
                // 2. Modify message
                for (int i = 2; i < args.length; i++) {
                    String ptn = "(title|content|likes|" +
                            "dislikes|pinned|username)=(.+)";
                    Matcher m = Pattern.compile(ptn).matcher(args[i]);
                    if (m.matches()) {
                        String field = m.group(1);
                        String value = m.group(2);
                        if (value.equals("?")) {
                            System.out.printf("Enter %s: ", field);
                            value = util.readString();
                        }
                        switch (field) {
                            case "title":
                                msg.mTitle = value;
                                break;
                            case "content":
                                msg.mContent = value;
                                break;
                            case "likes":
                                msg.mUpVotes = Integer.parseInt(value);
                                break;
                            case "dislikes":
                                msg.mDownVotes = Integer.parseInt(value);
                                break;
                            case "pinned":
                                msg.mPinned = Boolean.parseBoolean(value);
                                break;
                            case "username":
                                msg.mUsername = value;
                        }
                    } else {
                        System.out.printf("Invalid argument: %s;\n", args[i]);
                        r = util.INVALID_ARG;
                    }
                }
                // 3. Post new message to the database
                database.updateMessage(msg);
                System.out.println("Message updated");
            } catch (SQLException | NumberFormatException exp) {
                r = util.RUNTIME_ERR;
                System.out.printf("Error, %s\n", exp.getMessage());
            }
            return r;
        });
        cli.addSyntaxHint("update", "update <ID> [<FIELD>=<VALUE>]...");
        cli.addDescription("update", updateHelp);
    }

}
