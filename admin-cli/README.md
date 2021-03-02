# CSE216 Macrosoft Buzz - Admin App

## Admin Role
- Phase 1: Haocheng Gao

## User Guide

The Admin App is a stand-alone tool from the front-end and the backend of Buzz.
It is run locally and allows the administrator of Buzz to communicate with the
remote database and perform maintenance works through a command-line interface.

User could provide the URL of the database for the Admin App to connect to as
the first command-line argument. If no command-line argument is provided, the
App will use the default(current) URL of the database. The default URL may not
be valid.

Once the Admin App successfully make the connection to the database, a command-
line interface will be displayed. You can enter `help` to see a full list of
commands and their syntax, or enter `help <command-name>` to see the detailed
description of the command.

Here is an example of how to drop the current table, recreate a new table, and
insert some messages into it.
```
```

## Developer Guide

### The CLI API
The purpose of having the CLI, command-line interface, API is to make it easier
for command management. You can refer to the Java documentation for more details.
Generally, it allows you to create commands with a few function calls. It also
provides some utility functions that may be useful for creating new commands.

Here is an example of creating a `sum` command that takes the sum of its arguments.
```java
CLI cli = new CLI();  // Create a new CLI instance
cli.registerCommand("sum", (args, util) -> {
    int sum = 0;
    int rc = util.OK;  // Return code of the command
    for (int i = 1; i < args.length; i++) {
        try {
            sum += Integer.parseInt(args[i]);
        } catch(NumberFormatException exp) {
            System.err.println(args[i] + " is not an integer.");
            rc = util.INVALID_ARG;  // Mark that at least one of the arguments is invalid
        }
    }
    System.out.println(sum);
    return rc;
});
cli.addSyntaxHint("sum", "sum [INTEGER]...");  // Add a syntax hint to the command "sum"
cli.addDescription("sum", "Print the sum of its argument list");  // Add description
cli.run();  // Run the CLI instance.
            // Now the command-line interface is displayed and it has "sum" command in it
```

### The Database API
The Database API is a tool to allow the commands to perform database communication
and operations. Please refer to the Java documentation in `Database.java` or the
tutorial for more detailed explanation.

Our admin database API is very similar to the backend database API. It is a
good idea to merge the two into a single one because now it's really not
inconvenient to add new functionalities to the database - we have two files
to edit.


## Test Reviews

### Front End
The unit tests primarily done are the UI tests.
- Open Test: Form will appear after pushing the `open` button
- Hide Test: Form will be invisible after pushing the `hide` button
- Clear Test: Form content will be cleared after pushing the `clear` button
- Add Test: Form will disappear after pushing the `add` button

All tests are passed. The tests are self-contained and robust. They ensure
the basic UI operations does not break when future features are added


### Back End
The unit tests primarily done are the constructor tests.
- StructuredResponse Test: `StructuredResponse` are constructed correctly
using test status and `null` status.
- DataRow Test: `DataRow` are constructed correctly from raw data values
and from another `DataRow` instance.

The integration tests that includes the front-end HTTP requests are done
manually. The font-end tests scripts and the database queries are contained
in the `txt` files under backend folder.

All tests are passed, and the basic functionalities are tested. A future
recommendation is to make automated integration tests if possible.


### Admin App
The unit tests primarily done are the command registration tests.
- CLI Constructor Test: `CLI` instance is created with the default commands.
- Normal Command Registration Test: The command `Test` should be registered
correctly, and could be queried through the getters.
- Null Command Registration Test: The test attempts to register `null` as a
command. `CLI` should not generate error, nor register `null` value.
  
All tests are passed. The basic functionality of command-line interface is
guaranteed. Integration tests that include database queries could be added
in the future.