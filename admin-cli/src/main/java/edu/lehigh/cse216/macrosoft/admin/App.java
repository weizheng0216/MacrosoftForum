

package edu.lehigh.cse216.macrosoft.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.Date;
import java.sql.ResultSetMetaData;

/**
 * App is our basic admin app.  For now, it is a demonstration of the six key 
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
 class App {

    /**
     * Print the menu for our program
     */
    public static void main_menu()
    {
        System.out.println("*********************************************************");
        System.out.println("---------------------------------------------------------");
        System.out.println("    Here is the Main Menu");
        System.out.println("    You can edit the following tables");
        System.out.println("    By entering the correspond number,");  
        System.out.println("    you can check what changes can be made to each table.");
        System.out.println("    [1] User Table");
        System.out.println("    [2] Post Table");
        System.out.println("    [3] Comment Table");
        System.out.println("    [4] Votes Table");
        System.out.println("    [5] Quit the session.");
        System.out.println("---------------------------------------------------------");
        System.out.println("*********************************************************");
    }

    public static void UserMenu() {

        System.out.println("*******************************");
        System.out.println("    User Table Menu");
        System.out.println("    [1] Create User Table");
        System.out.println("    [2] Drop User Table");
        System.out.println("    [3] Insert a new User");
        System.out.println("    [4] Select user by User ID");
        System.out.println("    [5] Select user by Email");
        System.out.println("    [6] Delete an User");
        System.out.println("    [7] Quit Table Session");
        System.out.println("*******************************");
    }

    public static void PostMenu() {
        System.out.println("*******************************");
        System.out.println("    Post Table Menu");
        System.out.println("    [1] Create Post Table");
        System.out.println("    [2] Drop Post Table");
        System.out.println("    [3] Insert a new Post");
        System.out.println("    [4] View all posts");
        System.out.println("    [5] Select a Post by Post Id");
        System.out.println("    [6] Update a Post by Post Id");        
        System.out.println("    [7] Delete a Post");
        System.out.println("    [8] Quit Table Session");
        System.out.println("*******************************");
    }

    public static void CommentMenu() {
        System.out.println("*******************************");
        System.out.println("    Comment Table Menu");
        System.out.println("    [1] Create Comment Table");
        System.out.println("    [2] Drop Comment Table");
        System.out.println("    [3] Insert a new Comment");
        System.out.println("    [4] Select all comments");
        System.out.println("    [5] View Comment(s) in a Post");
        System.out.println("    [6] View Comment(s) made by an User");
        System.out.println("    [7] Update a Comment");        
        System.out.println("    [8] Delete a Comment");
        System.out.println("    [9] Quit Table Session");
        System.out.println("*******************************");
    }
    
    public static void VotesMenu() {
        System.out.println("*******************************");
        System.out.println("    Votes Table Menu");
        System.out.println("    [1] Create Votes Table");
        System.out.println("    [2] Drop Votes Table");
        System.out.println("    [3] Insert a new Vote");
        System.out.println("    [4] Select votes by User ID");
        System.out.println("    [5] Select a vote by Post and User IDs");
        System.out.println("    [6] Update a Vote");        
        System.out.println("    [7] Delete a Vote");
        System.out.println("    [8] Quit Table Session");
        System.out.println("*******************************");
    }
    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */

    

    /**
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */

    /**
     * Ask the user to enter an integer
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */

 
    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.
     */
    
    private static Database database;
    
    public static void main(String[] argv) {
        String dbURL = "postgres://vdtksuqjtzvetb:b7ccb5e707b07d8c8bfdf7" +
        "badbae2048282884d6b2e8ad336a71ff5833b2abc3@ec2-52-22-16" +
        "1-59.compute-1.amazonaws.com:5432/d9m8d6ulhh2bbk";
    
        // Get URL from command line
       if (argv.length >= 1) {
            dbURL = argv[0];
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


        ResultSet rs;
        //      User table attributes
        int user_id;
        String email;
        String first_name;
        String last_name;
        

        //      Post table attributes
        int post_id;
        String title;
        String content;
        Date date;
        int vote_up;
        int vote_down;
        boolean pinned;

        //      Comment Table Attributes
        int comment_id;
        String body;
        String content_comment;

        //      Votes Table Attributes
        boolean up_Vote;
        boolean down_Vote;



        // Start our basic command-line interpreter:
        //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Scanner keyboard = new Scanner(System.in);
        int operation = 0;
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call
            System.out.println();
            main_menu();  
            operation = keyboard.nextInt();
            if(keyboard.hasNextLine()){
                keyboard.nextLine();
            }
            if(operation == 5)
            {   
                System.out.println("Thanks for using the database");
                break; 
            }
            else if(operation == 1) // user table
            {
                
                int u_choice = 0;
                //BufferedReader u = new BufferedReader(new InputStreamReader(System.in));
                //BufferedReader uin = new BufferedReader(new InputStreamReader(System.in));
                //Scanner u = new Scanner(System.in);
                //Scanner uin = new Scanner(System.in);
                do {
                    System.out.println();
                    UserMenu();
                    System.out.println();
                    u_choice = keyboard.nextInt();
                    if(keyboard.hasNextLine()){
                        keyboard.nextLine();
                    }
                    //System.out.println(u_choice);
                    switch(u_choice)
                    {   
                        case 1:
                            try
                            {   System.out.println();
                                database.createUserTable();
                                System.out.println("User Table is created");
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            try{
                                System.out.println();
                                database.dropUserTable();
                                System.out.println("User Table is DROPPED");
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            System.out.println();
                            System.out.println("Enter the user email");
                            email = keyboard.nextLine();
                            System.out.println("Enter the user first name");
                            first_name = keyboard.nextLine();
                            System.out.println("Enter the user last name");
                            last_name = keyboard.nextLine();
                            System.out.println();
                            try{
                                database.insertUser(email, first_name, last_name);
                                System.out.println("User has been added to the data base");
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4: //selectUserById
                        System.out.println();
                            try{
                                System.out.println("Enter the user id: ");
                                user_id = keyboard.nextInt();
                                if(keyboard.hasNextLine()){
                                    keyboard.nextLine();
                                }
                            rs = database.selectUserById(user_id);
                            if(!rs.next())
                            {
                                System.out.println();
                                System.out.println("Sorry, this user record is not found.");
                                
                                break;
                            }
                            do
                            {
                                user_id = rs.getInt("user_id");
                                email = rs.getString("email");
                                first_name = rs.getString("first_name");
                                last_name = rs.getString("last_name");
                                System.out.println(user_id + ", " + email + ", " + first_name +
                           ", " + last_name);
                            }while(rs.next());
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            System.out.println();
                            break;
                        case 5: // select user by email
                            System.out.println();
                            try{
                                System.out.println("Enter the user email");
                                email = keyboard.nextLine();
                                rs = database.selectUserByEmail(email); 
                                System.out.println();
                                if(!rs.next())
                                {
                                    System.out.println("Sorry, this user record is not found.");
                                    
                                    break;
                                }
                                do
                                {
                                
                                    user_id = rs.getInt("user_id");
                                    email = rs.getString("email");
                                    first_name = rs.getString("first_name");
                                    last_name = rs.getString("last_name");
                                    System.out.println(user_id + " " + email + " " + first_name +
                           " " + last_name);
                            }while(rs.next());
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }break;
                        case 6: //deleteUserById()
                            System.out.println();
                            System.out.println("Enter the user id you want to delete:" );
                                user_id = keyboard.nextInt();
                                if(keyboard.hasNextLine()){
                                    keyboard.nextLine();
                                }
                                try{
                                    rs = database.selectUserById(user_id);
                                    System.out.println();
                                if(!rs.next())
                                {
                                    System.out.println("Sorry, this user record is not found.");
                                
                                    break;
                                }
                                else{   
                                    database.deleteUserById(user_id);
                                    System.out.println("\nUser has been deleted.");
                                }
                                }
                                catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            break;
                        case 7:
                                System.out.println("Quit Table User. Back to Main Menu ...");
                            break;
                        } // end of switch 

                }   while(u_choice != 7); // end of user table
            }
            else if (operation == 2){ // switch to post table 
                int p_choice = 0;
               
                do {
                    PostMenu();
                    p_choice = keyboard.nextInt();
                    if(keyboard.hasNextLine()){
                        keyboard.nextLine();
                    }
                    switch(p_choice)
                    {   
                        case 1:
                            try{
                                database.createPostTable();
                                System.out.println("Post Table is created");

                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }break;
                        case 2:
                          
                            try{
                                database.dropPostTable();
                                System.out.println("Post Table is dropped");
                         
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                        System.out.println();
                        System.out.println("Enter the post title");
                        title = keyboard.nextLine();
                        System.out.println("Enter the post content");
                        content = keyboard.nextLine();
                        System.out.println("Enter the number of upvotes");
                        vote_up = keyboard.nextInt();
                        if(keyboard.hasNextLine()){
                            keyboard.nextLine();
                        }
                        System.out.println("Enter the number of downvotes");
                        vote_down = keyboard.nextInt();
                        if(keyboard.hasNextLine()){
                            keyboard.nextLine();
                        }
                        System.out.println("Enter the user id");
                        user_id = keyboard.nextInt();
                        if(keyboard.hasNextLine()){
                            keyboard.nextLine();
                        }
                        System.out.println("Is it pinned");
                        pinned = keyboard.nextBoolean();
                        if(keyboard.hasNextLine()){
                            keyboard.nextLine();
                        }
                        try{
                            database.insertPost(title, content, vote_up, vote_down, user_id, pinned);
                            System.out.println("\nNew post has been added to the data base");
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                            break;
                        case 4:
                            try{
                                rs = database.selectAllPostsJoinUsers();
                                while(rs.next())
                                {
                                post_id = rs.getInt("post_id");
                                title = rs.getString("title");
                                content = rs.getString("content");
                                date = rs.getDate("date");
                                vote_up = rs.getInt("vote_up");
                                vote_down = rs.getInt("vote_down");
                                last_name = rs.getString("last_name");
                                first_name = rs.getString("first_name");
                                pinned = rs.getBoolean("pinned");
                                System.out.println("+++++++++++++++");
                                System.out.println("+++ Post ID +++");
                                System.out.println("    " + post_id);
                                System.out.println("+++  Title ++++");
                                System.out.println("    " + title);
                                System.out.println("+++ Content +++ ");
                                System.out.println("    " + content);
                                System.out.println("+++ Date +++ ");
                                System.out.println("    " + date);
                                System.out.println("++ Upvote counts ++");
                                System.out.println("    " + vote_up);
                                System.out.println("+ Downvote counts +");
                                System.out.println("    " + vote_down);
                                System.out.println("++ Last Name ++");
                                System.out.println("    " + last_name);
                                System.out.println("++ First Name ++");
                                System.out.println("    " + first_name);
                                System.out.println("+++ Pinned +++");
                                System.out.println("    " + pinned);
                                System.out.println("++++++++++++++");
                                System.out.println();
                                //System.out.printf ("%15d%15s%15s%15s%15d%15d%15s%15s%15b\n",post_id, title, content, date, vote_up, vote_down, first_name, pinned);
                                }
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 5:
                            System.out.println("Enter the post id you want to check");
                            post_id = keyboard.nextInt();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            try{
                                rs = database.selectPostById(post_id);
                                if(!rs.next())
                                {
                                    System.out.println("Sorry, this post record is not found.");
                                    
                                    break;
                                }
                                do
                                {  
                             
                                post_id = rs.getInt("post_id");
                                title = rs.getString("title");
                                content = rs.getString("content");
                                date = rs.getDate("date");
                                vote_up = rs.getInt("vote_up");
                                vote_down = rs.getInt("vote_down");
                                user_id = rs.getInt("user_id");
                                pinned = rs.getBoolean("pinned");
                                
                                System.out.println();
                                System.out.println("+++++++++++++++++++++++++++");
                                System.out.println("+++++++++ Post ID +++++++++");
                                System.out.println("    " + post_id);
                                System.out.println("+++++++++  Title ++++++++++");
                                System.out.println("    " + title);
                                System.out.println("+++++++++ Content +++++++++");
                                System.out.println("    " + content);
                                System.out.println("+++++++++ Date ++++++++++++");
                                System.out.println("    " + date);
                                System.out.println("++++++ Upvote counts ++++++");
                                System.out.println("    " + vote_up);
                                System.out.println("+++++ Downvote counts +++++");
                                System.out.println("    " + vote_down);
                                System.out.println("+++++++++ User Id +++++++++");
                                System.out.println("    " + user_id);
                                System.out.println("+++++++++ Pinned ++++++++++");
                                System.out.println("    " + pinned);
                                System.out.println("++++++++++++++++++++++++++++");
                                System.out.println();

                                //System.out.println(post_id + ", " + title + ", " + content + " ," + date +
                           //", " + vote_up + " ," + vote_down + " ," + user_id + " ," + pinned);
                            }while(rs.next());
                        }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 6: // update post
                            System.out.println();
                            System.out.println("Enter the post id you want to update: ");
                                post_id = keyboard.nextInt();
                                if(keyboard.hasNextLine()){
                                    keyboard.nextLine();
                                }
                                try{
                                    rs = database.selectPostById(post_id);
                                    System.out.println();
                              
                                    if(!rs.next())
                                    {
                                    System.out.println("This post Doesn't Exit");
                                    }
                                    else{
                                        System.out.println("Enter the new post title");
                                        title = keyboard.nextLine();
                                        System.out.println("Enter the post content");
                                        content = keyboard.nextLine();
                                        System.out.println("Enter the number of upvotes");
                                        vote_up = keyboard.nextInt();
                                        if(keyboard.hasNextLine()){
                                            keyboard.nextLine();
                                        }
                                        System.out.println("Enter the number of downvotes");
                                        vote_down = keyboard.nextInt();
                                        if(keyboard.hasNextLine()){
                                            keyboard.nextLine();
                                        }
                                        System.out.println("Is it pinned: true/false ");
                                        pinned = keyboard.nextBoolean();
                                        if(keyboard.hasNextLine()){
                                            keyboard.nextLine();
                                        } 
                                        System.out.println();
                                        try{
                                        
                                        database.updatePostById(title, content, vote_up, vote_down, pinned, post_id);
                                        System.out.println("The post has been reset:) ");
                                        //System.out.println(post_id + ", " + title + ", " + content + " ," +
                                            //", " + vote_up + " ," + vote_down + " ," + user_id + " ," + pinned);
                                        }catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    }}
                                    catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println();
                            break;
                        case 7:  // delete a post
                            System.out.println();
                            System.out.println("Enter the post id you want to delete:" );
                                post_id = keyboard.nextInt();
                                if(keyboard.hasNextLine()){
                                    keyboard.nextLine();
                                }
                                try{
                                    rs = database.selectPostById(post_id);
                                    if(!rs.next())
                                    {
                                        System.out.println("Sorry, we don't have this post.");
                                        
                                        break;
                                    }
                                    else{
                                        //check if it has comments
                                        rs = database.selectCommentsByPostId(post_id);
                                        if(rs.next())
                                        {
                                        System.out.println("\nSorry, there are comments in this post.");
                                        System.out.println("You have to delete the comments so that the post can be deleted.");
                                        break;
                                        }
                                       
                                        database.deletePostById(post_id);
                                        System.out.println("\nPost "+ post_id + " has been deleted.");
                                    }
                                }
                                catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                System.out.println();
                            break;
                        case 8:    
                            System.out.println("Quit Table Post. Back to Main Menu ...");
                            break;
                    }
                } while(p_choice!=8);
            }   // end of the post table

            else if(operation == 3) // user table
            {
                
                int c_choice = 0;
                do {
                    System.out.println();
                    CommentMenu();
                    c_choice = keyboard.nextInt();
                    if(keyboard.hasNextLine()){
                        keyboard.nextLine();
                    }
                    switch(c_choice)
                    {   
                        case 1:
                            System.out.println();
                            try{
                                database.createCommentTable();
                                System.out.println("Comment Table is created");
                            }
                                catch (SQLException e) {
                                    e.printStackTrace();
                            }
                            System.out.println();
                            break;
                        case 2:
                            System.out.println();
                            try{
                                database.dropCommentTable();
                                System.out.println("Comment Table is dropped");
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            System.out.println();
                            break;

                        case 3: // insert comment
                            System.out.println();
                            System.out.println("Enter the content");
                            content_comment = keyboard.nextLine();
                            System.out.println("Enter the user id");
                            user_id = keyboard.nextInt();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            try{
                                rs = database.selectUserById(user_id);
                            
                            System.out.println("Enter the post id");
                            post_id = keyboard.nextInt();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            ResultSet rs1 = database.selectPostById(post_id);
                            if(!rs.next() || !rs1.next())
                            {
                                System.out.println("Record doesn't exist");
                                break;
                            }
                            
                            System.out.println();
                        
                            database.insertComment(content_comment, user_id, post_id);
                            System.out.println("Comment has been added to the data base");
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.println();
                        break;

                        case 4: // inner join
                        System.out.println();
                        try{
                            rs = database.selectAllCommentsJoinUsers();
                            while(rs.next())
                            {
                            comment_id = rs.getInt("comment_id");
                            content_comment = rs.getString("content");
                            date = rs.getDate("date");
                            last_name = rs.getString("last_name");
                            first_name = rs.getString("first_name");
                            System.out.println("++++++++++++++++");
                            System.out.println("++ Comment ID ++");
                            System.out.println("    " + comment_id);
                            System.out.println(" ++ Content ++ ");
                            System.out.println("    " + content_comment);
                            System.out.println(" ++ Date ++ ");
                            System.out.println(" " + date);
                            System.out.println("++ Last Name ++");
                            System.out.println("    " + last_name);
                            System.out.println("++ First Name ++");
                            System.out.println("    " + first_name);
                            System.out.println("++++++++++++++++");
                     
                            //System.out.printf ("%15d%15s%15s%15s%15d%15d%15s%15s%15b\n",post_id, title, content, date, vote_up, vote_down, first_name, pinned);
                            }
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.println();
                        break;

                        case 5: // select comments by post id
                        System.out.println();
                            try{
                            System.out.println("Enter the post id you want to check:" );
                            post_id = keyboard.nextInt(); 
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            rs = database.selectCommentsByPostId(post_id);
                            System.out.println();
                            if(!rs.next())
                            {
                                System.out.println("\nSorry, no one has made any comments under this post.");
                                break;
                            }
                            do
                            {
                                comment_id = rs.getInt("comment_id");
                                content = rs.getString("content");
                                date = rs.getDate("date");
                                user_id = rs.getInt("user_id");
                                post_id = rs.getInt("post_id");
                                System.out.println("++++++++++++++++");
                                System.out.println("++ Comment ID ++");
                                System.out.println("    " + comment_id);
                                System.out.println(" ++ Content ++ ");
                                System.out.println("    " + content);
                                System.out.println(" ++ Date ++ ");
                                System.out.println(" " + date);
                                System.out.println("+++ User ID +++");
                                System.out.println("     " + user_id);
                                System.out.println("++ Post ID ++");
                                System.out.println("     " + post_id);
                                System.out.println("++++++++++++++++");
                                System.out.println();

                                //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                            }while(rs.next());
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            System.out.println();
                            break;

                    case 6: // select comments by user id
                        System.out.println();
                        try{
                            System.out.println("Enter the user id you want to check:" );
                            user_id = keyboard.nextInt(); 
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                        }
                            rs = database.selectCommentsByUserId(user_id);
                            System.out.println();
                            if(!rs.next())
                            {
                                System.out.println("\nSorry, this user hasn't made any comments.");
                                break;
                            }
                            do
                            {
                                comment_id = rs.getInt("comment_id");
                                content = rs.getString("content");
                                date = rs.getDate("date");
                                user_id = rs.getInt("user_id");
                                post_id = rs.getInt("post_id");
                                System.out.println("++++++++++++++++");
                                System.out.println("++ Comment ID ++");
                                System.out.println("    " + comment_id);
                                System.out.println(" ++ Content ++ ");
                                System.out.println("    " + content);
                                System.out.println(" ++ Date ++ ");
                                System.out.println(" " + date);
                                System.out.println("+++ User ID +++");
                                System.out.println(" " + user_id);
                                System.out.println("++ Post ID ++");
                                System.out.println("  " + post_id);
                                System.out.println("++++++++++++++++");
                                System.out.println();

                                //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                            }while(rs.next());
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            System.out.println();
                            break;


                            
                        case 7: // update
                            try{
                            System.out.println("Enter the comment id you want to change:" );
                            comment_id = keyboard.nextInt(); 
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            
                            System.out.println("Enter the new content you want to update:" );
                            content_comment = keyboard.nextLine(); 
                            
                                database.updateCommentById(content_comment, comment_id);
                                System.out.println("Comment has been updated.");
                            }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.println();
                            break;
                        case 8: // delete
                        System.out.println();
                            System.out.println("Enter the comment id you want to delete:" );
                            comment_id = keyboard.nextInt(); 
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            try{
                                database.deleteCommentById(comment_id);
                                System.out.println("Comment "+ comment_id + " has been deleted.");
                            }
                            
                        catch (SQLException e) {
                            e.printStackTrace();
                        }System.out.println();
                        break;
                        case 9:    
                            System.out.println("Quit Table Comment. Back to Main Menu ...");
                            break;
                    } // end of switch 
                    System.out.println();
                } while(c_choice != 8);
            }   
            else if(operation == 4) // user table
            {
             
                int v_choice = 0;
                // Scanner v = new Scanner(System.in);
                // Scanner vin = new Scanner(System.in);
               
                do {
                    System.out.println();
                    VotesMenu();
                    v_choice = keyboard.nextInt();
                    if(keyboard.hasNextLine()){
                        keyboard.nextLine();
                    }
                    switch(v_choice)
                    {   
                        case 1:
                            
                            try{
                                database.createVoteTable();
                                System.out.println("Table Vote is created");
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }System.out.println();
                            break;
                        case 2:
                            
                            try{
                                database.dropVoteTable();
                                System.out.println("Table Vote is dropped");
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }System.out.println();
                            break;
                        case 3:
                            System.out.println();
                            System.out.println("Enter the user id:" );
                            user_id = keyboard.nextInt();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            try{
                            rs = database.selectUserById(user_id);
                            if(!rs.next())
                            {
                                System.out.println("User doesn't exist");
                                break;
                            }
                            System.out.println("Enter the post id:" );
                            post_id = keyboard.nextInt();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            
                            rs = database.selectPostById(post_id);
                            if(!rs.next())
                            {
                                System.out.println("Post doesn't exist");
                                break;
                            }
                            System.out.println("Enter if it is upvote: true/false" );
                            up_Vote = keyboard.nextBoolean();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            System.out.println("Enter if it is downvote: true/false" );
                            down_Vote = keyboard.nextBoolean();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                                database.insertVote(user_id, post_id, up_Vote, down_Vote);
                                System.out.println("The vote has been inserted.");
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4: // by user id
                        System.out.println();
                            try{
                                System.out.println("Enter the user id: ");
                                user_id = keyboard.nextInt();
                                if(keyboard.hasNextLine()){
                                    keyboard.nextLine();
                                }
                                rs = database.selectVotesByUserId(user_id);
                                System.out.println();
                                if(!rs.next())
                                {
                                    System.out.println("Sorry, we don't have this record.");
                                        
                                    break;
                                }
                                do
                                {
                                    System.out.printf("%15s%15s%15s%15s\n","user_id", "post_id", "upVote", "down_Vote");
                                    user_id = rs.getInt("user_id");
                                    post_id = rs.getInt("post_id");
                                    up_Vote = rs.getBoolean("vote_up");
                                    down_Vote = rs.getBoolean("vote_down");
                                    System.out.printf("%15d%15d%15b%15b\n",user_id, post_id, up_Vote, down_Vote);
                                    //System.out.println(user_id + " ," + post_id + " ," + up_Vote + " ," + down_Vote);
                                }while(rs.next());
                            }
                                catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            break;
                        case 5:   // by ids
                            System.out.println();
                            try{
                                System.out.println("Enter the user id: ");
                                user_id = keyboard.nextInt();
                                if(keyboard.hasNextLine()){
                                    keyboard.nextLine();
                                }
                                System.out.println("Enter the post id: ");
                                post_id = keyboard.nextInt();
                                if(keyboard.hasNextLine()){
                                    keyboard.nextLine();
                                }
                                    rs = database.selectVoteByIds(user_id, post_id);
                                    if(!rs.next())
                                    {
                                        System.out.println("This user hasn't voted this post");
                                    }
                                    else{
                                //
                                do
                                {   System.out.printf("%15s%15s%15s%15s\n","user_id", "post_id", "upVote", "down_Vote");
                                    user_id = rs.getInt("user_id");
                                    post_id = rs.getInt("post_id");
                                    up_Vote = rs.getBoolean("vote_up");
                                    down_Vote = rs.getBoolean("vote_down");
                                    System.out.printf("%15d%15d%15b%15b\n",user_id, post_id, up_Vote, down_Vote);
                                }while(rs.next());
                            }}
                                catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            break;
                        case 6: // update
                            System.out.println();
                            System.out.println("Enter the user id");
                            user_id = keyboard.nextInt();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            
                            System.out.println("Enter the post id");
                            post_id = keyboard.nextInt();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                     
                            try{
                                rs = database.selectVoteByIds(user_id, post_id);
                                if(!rs.next())
                                {
                                    System.out.println("\nThis user hasn't voted this post");
                                }
                                else{   
                                    System.out.println("Enter the upVote Status: true/false");
                                    up_Vote = keyboard.nextBoolean();
                                    if(keyboard.hasNextLine()){
                                    keyboard.nextLine();
                                }
                                    System.out.println("Enter the downVote Status: true/false");
                                    down_Vote = keyboard.nextBoolean();
                                    if(keyboard.hasNextLine()){
                                        keyboard.nextLine();
                                    }
                                    database.updateVoteByIds(up_Vote, down_Vote, user_id, post_id);
                                    System.out.println("\nThe vote has been reset: ");
                                    
                                }

                                }catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                            break;
                        case 7:
                            System.out.println();
                            System.out.println("To delete the vote, enter the user id: " );
                            user_id = keyboard.nextInt();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            System.out.println("To delete the vote, enter the post id: " );
                            post_id = keyboard.nextInt();
                            if(keyboard.hasNextLine()){
                                keyboard.nextLine();
                            }
                            try{
                                rs = database.selectVoteByIds(user_id, post_id);
                                if(!rs.next())
                                {
                                    System.out.println("This user hasn't voted this post.");
                                }
                                else{
                                database.deleteVoteByIds(user_id, post_id);

                                System.out.println("The vote has been deleted.");
                                }
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }System.out.println();
                    break;
                        case 8:   
                            System.out.println("Quit Table Votes. Back to Main Menu ...");
                            
                            break;
                    }   
                
                } while(v_choice != 8); // end of do loop
            }  // end of the last option
           
        }
        try {
                database.disconnect();
            } catch (SQLException exp) {
                System.out.println(exp.getMessage());
            }
        keyboard.close();
        
}
}
  
       
 
  
       
 