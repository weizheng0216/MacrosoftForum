# CSE216 Macrosoft Buzz - Admin App

## Admin Role
- Phase 1: Haocheng Gao
- Phase 2: Coco Chen
## User Guide

The Admin App is a stand-alone tool from the front-end and the backend of Buzz.
It is run locally and allows the administrator of Buzz to communicate with the
remote database and perform maintenance works through a command-line interface.

User could provide the URL of the database for the Admin App to connect to as
the first command-line argument. If no command-line argument is provided, the
App will use the default(current) URL of the database. The default URL may not
be valid.

Before start testing, make sure to `docker pull postgres` everytime because some changes may added before testing. 

## Developer Guide

### Admin App

Once the Admin App successfully make the connection to the database, the main menu
interface will be displayed. It will show what tables can be changed.

As the admin, you will be asked to enter the action you want to make targeting different tables.

There are four tables in total: 
  User Table    
  Post Table
  Comment Table
  Votes Table
The admin can make changes to these four tables. 
Right now, it only provides the basic functions, 
  i.e. create, drop, select, insert, delete.
Due to the functional dependency, the user table can not be dropped if the rest of the tables are dropped. 


### The CLI API
In this phase, CLI is not used for Database.

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
Since front end will always connect to backend, to avoid this action in front end test, testing flag is set. When this flag is on, all resquest to backend will be blocked. Additionally, debug flag is set that is whenever backend send a response to front end, the response information will be print in console. Normally, both testing and debug flag is false.
There are seven tests in total that targets three parts.
Each part uses `describe()` to separate.

  • basic structure that shows the user interface
      • `shows post detail` 
          When the `brief post` button is clicked,
          check if the post will show up and if the new block will be hidden.
      • `show new post block`
          When the `new post` button is clicked,
          check if the new post block will show up and if the new block will be hidden.
      • `show user profile interface`
          When the `profile` button is clicked
          check if the user interface shows up and the new post block is hidden
	• examine the likes and dislikes  
	    • `test like`
	        When the user click `like` button, 
	        check if the counts of likes will be incremented
	    • `test dislike`
	        When the user click `dislike` button, 
	        check if the counts of dislikes will be incremented
	• switch from different user interfaces
      • `test show other user interface`
          When the user click user button, check if the user's info shows up
      • `test show other user interface`
          When the user click the close button, check if the user interface is closed.
      

All tests are passed. The tests are self-contained and robust. They ensure
the basic UI operations does not break when future features are added


### Back End
The unit tests primarily done are the constructor tests.

- Response Test: `ResponseTest` are for checking if raw data values are constructed correctly
  - Inside `Response Test`, the testConstructor contains three parts:
    • CommentResponse: check if the existing comment is correctly constructed
    • PostResponse: check if the existing post is correctly constructed based on comment
    • UserResponse: check if the existing user is correctly constructed based on post

The integration tests that includes the front-end HTTP requests are done
manually. The font-end tests scripts and the database queries are contained
in the `txt` files under backend folder.

All tests are passed, and the basic functions are tested. A future
recommendation is to make automated integration tests if possible.


### Admin App
The unit tests primarily done are the command registration tests.

- CLI Constructor Test: `CLI` instance is created with the default commands.
- Normal Command Registration Test: The command `Test` should be registered
correctly, and could be queried through the getters.
- Database Test:
  Database instance is created and connected for the tests.
  The tests are divided in couple parts.

  • Check table attributes
    Using select___ById to retrieve an entity by `ResultSet` 
    and then use `ResultSetMetaData.getColumnName()` to check if each table is created correctly with correct column names.
    
  • Check if entity is wholesome 
    Initiate some variables and use them to compare with an existing entity to see if the constructor is correct.

  • Check if entity is inserted successfully and deleted successfully   
    
  
All tests are passed. The basic functionality of command-line interface is
guaranteed. Integration tests that include database queries could be added
in the future.