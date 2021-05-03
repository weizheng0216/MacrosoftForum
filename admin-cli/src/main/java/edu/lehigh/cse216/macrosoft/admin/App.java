

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
import java.util.regex.*;

/**
 * App is our basic admin app.  For now, it is a demonstration of the six key 
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
 class App {

    final static Boolean DEBUG = true;
    private static int getInputInt(int lower, int upper, String prompt) {
		try {
			System.out.print(prompt);
			String input = keyboard.nextLine();
			int choose = Integer.parseInt(input);
			if (choose < lower || choose > upper) {
				// invalid input
				throw new Exception("");
			}
			System.out.println("");
			return choose;
		} catch (Exception exception) {
			System.out.println("\ninvlid input, please try again");
			return getInputInt(lower, upper, prompt);
		}
	}
    
    private static boolean getInputBoolean(String prompt) {
		try {
			System.out.print(prompt);
            String input = keyboard.nextLine();
            boolean choose = Boolean.parseBoolean(input);
			System.out.println("");
			return choose;
		} catch (Exception exception) {
			System.out.println("\ninvlid input, please try again");
			return getInputBoolean(prompt);
		}
	}

    private static int getInputInt(String prompt) {
		try {
			System.out.print(prompt);
			String input = keyboard.nextLine();
			int choose = Integer.parseInt(input);
			System.out.println("");
			return choose;
		} catch (Exception exception) {
			System.out.println("\ninvlid input, please try again");
			return getInputInt(prompt);
		}
	}
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
        System.out.println(" NEW [1] User Table");
        System.out.println(" NEW [2] Post Table");
        System.out.println(" NEW [3] Comment Table");
        System.out.println("     [4] Votes Table");
        System.out.println("     [5] Manage File.\n");
        System.out.println("     [0] Quit the session.");
        System.out.println("---------------------------------------------------------");
        System.out.println("");
        
        System.out.println(" !!NEW FUNCTIONS from phase 4 under post, comment, and user table!!");
        System.out.println("");
        System.out.println("*********************************************************");
    }

     /**
     * Print the User Menu 
     */
    public static void UserMenu() {
        System.out.println("*******************************");
        System.out.println("    User Table Menu");
        System.out.println("    [1] Create User Table");
        System.out.println("    [2] Drop User Table");
        System.out.println("    [3] Insert a new User");
        System.out.println("    [4] Select user by User ID");
        System.out.println("    [5] Select user by Email");
        System.out.println("    [6] Delete an User\n");
        System.out.println("NEW FUNCTION:");
        System.out.println("    [7] Block User By Email \n");
        System.out.println("    [0] Quit Table Session");
        System.out.println("*******************************");
    }
     /**
     * Print Post Menu
     */
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
        System.out.println("    [8] View all files");
        System.out.println("");
        System.out.println("NEW FUNCTION:");
        System.out.println("    [9] Flag a Post\n");
        System.out.println("    [0] Quit Table Session");
        System.out.println("*******************************");
    }

    /**
     * Print the Comment Menu 
     */
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
        System.out.println("    [9] View all files");
        System.out.println("");
        System.out.println("NEW FUNCTION:");
        System.out.println("    [10] Flag a Comment\n");
        System.out.println("    [0] Quit Table Session");
        System.out.println("*******************************");
    }
    
    /**
     * Print the Votes Menu 
     */
    public static void VotesMenu() {
        System.out.println("*******************************");
        System.out.println("    Votes Table Menu");
        System.out.println("    [1] Create Votes Table");
        System.out.println("    [2] Drop Votes Table");
        System.out.println("    [3] Insert a new Vote");
        System.out.println("    [4] Select votes by User ID");
        System.out.println("    [5] Select a vote by Post and User IDs");
        System.out.println("    [6] Update a Vote");        
        System.out.println("    [7] Delete a Vote\n");
        System.out.println("    [0] Quit Table Session");
        System.out.println("*******************************");
    }

    public static void FileMenu() {
        System.out.println("*******************************");
        System.out.println("    Manage File Menu");
        System.out.println("    [1] Select all files under posts");
        System.out.println("    [2] Select all files under comments"); 
        System.out.println("    [3] Select all files of a user\n");
        System.out.println("    [4] Delete a file under POSTS "); 
        System.out.println("    [5] Delete a file under COMMENTS");
        System.out.println("    [6] Delete Least used file\n");
        System.out.println("    [7] Find a file in Drive\n");
        System.out.println("    [0] Quit Table Session");
        System.out.println("*******************************");
    }
 
    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.
     */
    
    private static Database database;
    
    private static Scanner keyboard = new Scanner(System.in);
    
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
        boolean blocked;
        String filepath;
        //      Post table attributes
        int post_id;
        String title;
        String content;
        String video_link;
        Date date;
        int vote_up;
        int vote_down;
        boolean flagged;

        //      Comment Table Attributes
        int comment_id;
        String body;
        String content_comment;

        //      Votes Table Attributes
        boolean up_Vote = true;
        boolean down_Vote = true;

       
        int operation = 0;
        while (true) {
            // User can enter the operation by number and make editions to the database
            System.out.println();
            main_menu();  
            operation = getInputInt(0,5,"\tplease enter 0-5: ");
          
            if(operation == 0) // quite the program
            {   
                System.out.println("Thanks for using the database");
                break; 
            }
            else if(operation == 5){
                // Scanner v = new Scanner(System.in);
                // Scanner vin = new Scanner(System.in);
                int u_choice = 0;
                do {
                    System.out.println();
                    FileMenu();
                    System.out.println();
                    u_choice = getInputInt("\tplease enter: ");
                    switch(u_choice){
                        case 1: 
                        try{
                            rs = database.SelectPostWithFile();
                            System.out.println();
                            if(!rs.next())
                            {
                                System.out.println("\nThere is no file under posts.");
                                break;
                            }
                            
                            do
                            {
                                post_id = rs.getInt("post_id");
                                first_name = rs.getString("first_name");
                                last_name = rs.getString("last_name");
                                filepath = rs.getString("filepath");
                                String[] parsePath = filepath.split("/",2);
                                System.out.println("=====================");
                                System.out.println(" -- Under Post ----");
                                System.out.println("      ID: " + post_id);
                                System.out.println(" ---- From User ---");
                                System.out.println("        " + first_name+ " "+last_name);
                                System.out.println(" ---- File Name ---");
                                System.out.println("        " + parsePath[parsePath.length-1]);
                                System.out.println("=====================");
                                System.out.println();

                                //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                            }while(rs.next());

                        }catch (SQLException e) {
                            System.out.println("An error happened when selecting the file");

                        }
                            break;
                        case 2:
                        
                        //add functions !!!
                        try{
                            rs = database.SelectCommentWithFile();
                            System.out.println();
                            if(!rs.next())
                            {
                                System.out.println("\nThere is no file under all comments.");
                                break;
                            }
                            
                            do
                            {
                                post_id = rs.getInt("post_id");
                                comment_id = rs.getInt("comment_id");
                                first_name = rs.getString("first_name");
                                last_name = rs.getString("last_name");
                                filepath = rs.getString("filepath");
                                String[] parsePath = filepath.split("/",3);
                                System.out.println("=====================");
                                System.out.println(" -- Under Post ----");
                                System.out.println("      ID: " + post_id);
                                System.out.println(" ---- With Comment ");
                                System.out.println("        ID: " + comment_id);
                                System.out.println(" ---- From User ---");
                                System.out.println("        " + first_name+ " "+last_name);
                                System.out.println(" ---- File Name ---");
                                System.out.println("        " + parsePath[parsePath.length-1]);
                                System.out.println("=====================");
                                System.out.println();

                                //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                            }while(rs.next());

                        }catch (SQLException e) {
                            System.out.println("An error happened when selecting the file");

                        }
                        
                            break;
                        case 3:
                            System.out.println("\tEnter the user email: ");
                            email = keyboard.nextLine();
                            try{
                                rs= database.SelectFileUnderPostByEmail(email);
                                System.out.println();
                                if(!rs.next())
                                {
                                    System.out.println("\nThe user have NO file under post");
                                    
                                }else{
                                    do
                                    {
                                        System.out.println("FROM POSTS");
                                        post_id = rs.getInt("post_id");
                                        content = rs.getString("title");
                                        filepath = rs.getString("filepath");
                                        String[] parsePath = filepath.split("/",2);
                                        System.out.println("=====================");
                                        System.out.println(" -- Under Post ----");
                                        System.out.println("      ID: " + post_id);
                                        System.out.println(" -- With Title ----");
                                        System.out.println("      Title: " + content);
                                        System.out.println(" ---- File Name ---");
                                        System.out.println("        " + parsePath[parsePath.length-1]);
                                        System.out.println("=====================");
                                        System.out.println();
        
                                        //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                                    }while(rs.next());
                                }

                                
    
                                rs = database.SelectFileUnderCommentByEmail(email);
                                System.out.println();
                                if(!rs.next())
                                {
                                    System.out.println("\nThe user have NO file under comment");
                                    
                                }else{
                                    do
                                    {
                                        System.out.println("FROM COMMENTS");
                                        comment_id = rs.getInt("comment_id");
                                        content = rs.getString("content");
                                        filepath = rs.getString("filepath");
                                        String[] parsePath = filepath.split("/",3);
                                        System.out.println("=====================");
                                        System.out.println(" -- Under Comment -");
                                        System.out.println("      ID: " + comment_id);
                                        System.out.println(" -- Wtih Comment --");
                                        System.out.println("      Content: " + content);
                                        System.out.println(" ---- File Name ---");
                                        System.out.println("        " + parsePath[parsePath.length-1]);
                                        System.out.println("=====================");
                                        System.out.println();
        
                                        //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                                    }while(rs.next());
                                }

                                
                            
                            
                            }catch (SQLException e) {
                            System.out.println("An error happened when selecting the file");

                            }
                            break;

                        case 4: // post
                            try{
                                System.out.println();
                                post_id = getInputInt("\tEnter the post id where the file is under: ");
                                rs = database.SelectFileByPostID(post_id); // check if user exists
                                    System.out.println();
                                if(!rs.next())
                                {
                                    System.out.println("Sorry, No file under this post.");
                                    break;
                                }
                                filepath = rs.getString("filepath");
                                GoogleDriveAPI.removeFile(filepath);
                                database.DeleteFileByPostID(post_id);
                                System.out.println("Delete the file successfully!");
                            }catch (SQLException e) {
                                if(DEBUG)
                                    System.out.println(e.getMessage());
                                System.out.println("An error happened when deleting the file");
                            }
                            break;
                        case 5: // comment
                            try{
                                System.out.println();
                                comment_id = getInputInt("\tEnter the comment id where the file is under: ");
                                rs = database.SelectFileByCommentID(comment_id); // check if user exists
                                    System.out.println();
                                if(!rs.next())
                                {
                                    System.out.println("Sorry, No file under this comment.");
                                    break;
                                }
                                filepath = rs.getString("filepath");
                                GoogleDriveAPI.removeFile(filepath);
                                database.DeleteFileByCommentID(comment_id);
                                System.out.println("Delete the file successfully!");
                            }catch (SQLException e) {
                                System.out.println("An error happened when deleting the file");
                            }
                            break;
                        case 6:
                            Double precentage = GoogleDriveAPI.printAbout();
                            if(precentage < 0.9){
                                System.out.println("Do not need to remove anything");
                                System.out.println("Still want to remove? ");
                                System.out.println("Press [y] to remove 4 least used posts and 4 least used comments file");
                                System.out.println("\tPress anything else to return");
                                System.out.print("Enter: ");
                                String userInput = keyboard.nextLine();
                                if(userInput.equals("y")){
                                    try{
                                        rs = database.SelectPostWithFile();
                                        int count = 0;
                                        System.out.println();
                                        if(!rs.next())
                                        {
                                            System.out.println("\nThere is no file under posts.");
                                        }else{
                                            do{
                                                post_id = rs.getInt("post_id");
                                                filepath = rs.getString("filepath");
                                                System.out.printf("Deleting \"%s\" under post %d\n", filepath, post_id);
                                                GoogleDriveAPI.removeFile(filepath);
                                                database.DeleteFileByPostID(post_id);
                                                count++;
                                            }while(rs.next() && count<4);
                                        }

                                        rs = database.SelectCommentWithFile();
                                        count = 0;
                                        System.out.println();
                                        if(!rs.next())
                                        {
                                            System.out.println("\nThere is no file under all comments.");
                                        }else{
                                            do{
                                                post_id = rs.getInt("post_id");
                                                comment_id = rs.getInt("comment_id");
                                                filepath = rs.getString("filepath");
                                                System.out.printf("Deleting \"%s\" under post %d with comment id %d\n", filepath, post_id, comment_id);
                                                GoogleDriveAPI.removeFile(filepath);
                                                database.DeleteFileByCommentID(comment_id);
                                                count++;
                                            }while(rs.next() && count<4);
                                        }
                                    }catch (SQLException e) {
                                        System.out.println("An error happened when deleting the file");

                                    }
         
                                }else{
                                    System.out.println("Return to previous interface");
                                }
                            }
                            //GoogleDriveAPI.printAbout();

                            break;
                        case 7:
                            System.out.println("Enter the file name: ");
                            String fileName = keyboard.nextLine();
                            GoogleDriveAPI.findFile(fileName);
                            break;
                        default:
                            System.out.println("invlid input");
                    }
                } while(u_choice != 0); // end of do loop
            }
            else if(operation == 1) // user table
            {
                
                int u_choice = 0;
                do {
                    System.out.println();
                    UserMenu();
                    System.out.println();
                    u_choice = getInputInt("\tplease enter: ");
                   
                    switch(u_choice)
                    {   
                        // create user table
                        case 1: 
                            try
                            {   System.out.println();
                                database.createUserTable();
                                System.out.println("User Table is created");
                            }
                            catch (SQLException e) {
                                System.out.println("User Table already created. Cannot create again");
                            }
                            break;

                        // drop user table
                        case 2: 
                            try{
                                System.out.println();
                                database.dropUserTable();
                                System.out.println("User Table is DROPPED");
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when dropping table. You may check key constrains. You must drop posts and comments table");
                            }
                            break;

                        // insert user
                        case 3: 
                            System.out.println("\tEnter the user email: ");
                            email = keyboard.nextLine();
                            System.out.println("\tEnter the user first name: ");
                            first_name = keyboard.nextLine();
                            System.out.println("\tEnter the user last name: ");
                            last_name = keyboard.nextLine();
                            System.out.println();
                            try{
                                database.insertUser(email, first_name, last_name);
                                System.out.println("User has been added to the data base");
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when adding user");
                            }
                            break;
                        
                        //selectUserById
                        case 4: 
                            System.out.println();
                            try{
                                user_id = getInputInt("\tEnter the user id: ");
                                
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
                                blocked = rs.getBoolean("blocked");
                                System.out.println(user_id + ", " + email + ", " + first_name +
                           ", " + last_name + " blocked: " + blocked);
                            }while(rs.next());
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when selecting the user");
                            }
                            System.out.println();
                            break;
                        
                        // select user by email
                        case 5: 
                            System.out.println();
                            try{
                                System.out.println("\tEnter the user email: ");
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
                                    blocked = rs.getBoolean("blocked");
                                    System.out.println(user_id + " " + email + " " + first_name +
                           " " + last_name + " blocked: " + blocked);
                            }while(rs.next());
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when selecting the user");
                            }break;

                        //deleteUserById()
                        case 6: 
                            System.out.println();
                            
                                user_id = getInputInt("\tEnter the user id you want to delete: ");
                               
                                try{
                                    rs = database.selectUserById(user_id); // check if user exists
                                    System.out.println();
                                if(!rs.next())
                                {
                                    System.out.println("Sorry, this user record is not found.");
                                
                                    break;
                                }
                                rs = database.selectCommentsByUserId(user_id);
                                if(rs.next())
                                {
                                    System.out.println("Sorry, this user cannot be deleted because this user has made some comments.");
                                    System.out.println("You have to delete those comments first.");
                                    break;
                                }
                                else{   
                                    database.deleteUserById(user_id);
                                    System.out.println("\nUser has been deleted.");
                                }
                                }
                                catch (SQLException e) {
                                    System.out.println("An error happened when deleteing the user");
                                }

                            break;
                        case 7: 
                            System.out.println();
                            
                                
                               
                                try{
                                    System.out.println("\tEnter the user email: ");
                                    email = keyboard.nextLine();
                                    rs = database.selectUserByEmail(email); 
                                    System.out.println();
                              
                                    if(!rs.next())
                                    {
                                    System.out.println("This user Doesn't Exist");
                                    }
                                    else{
                                        
                                        try{
                                        
                                        database.blockUserByEmail(true, email);
                                        System.out.println("The post has been blocked:) ");
                                        }catch (SQLException e) {
                                        System.out.println("An error happened when blocking the user");
                                    }
                                    }}
                                    catch (SQLException e) {
                                        System.out.println("An error happened when updating the post");

                                    }
                                    System.out.println();
                            break;
                        case 0:
                                System.out.println("Quit Table User. Back to Main Menu ...");
                            break;
                        } // end of switch 

                }   while(u_choice != 0); // end of user table
            }
            else if (operation == 2){ // switch to post table 
                int p_choice = 0;
               
                do {
                    PostMenu();
                    p_choice = getInputInt("\tplease enter: ");
                   
                    switch(p_choice)
                    {   
                        case 1:
                            try{
                                database.createPostTable();
                                System.out.println("Post Table is created");

                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when creating the posts table. May be it has already been created");
                            }break;
                        case 2:
                          
                            try{
                                database.dropPostTable();
                                System.out.println("Post Table is dropped");
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when dropping the posts table. You can check the key constrains. You must drop the comments table");
                            }
                            break;

                        // insert post 
                        case 3:
                        System.out.println();
                        System.out.println("\tEnter the post title: ");
                        title = keyboard.nextLine();
                        System.out.println("\tEnter the post content: ");
                        content = keyboard.nextLine();
                        System.out.println("\tEnter the video link: ");

                        String linkPattern = "^(http(s)?://)?((w){3}.)?youtu(be|.be)?(.com)?/.+";
                        video_link = keyboard.nextLine();
                        if(!Pattern.matches(linkPattern, video_link)){
                            System.out.println("\tVideo link not provided, default to empty");
                            video_link = "";
                        } else {
                            video_link = video_link.substring(video_link.lastIndexOf("=") + 1);
                        }
                    
                        user_id = getInputInt("\tEnter the user id: "); 
                        
                        
                        try{
                            database.insertPost(title, content, 0, 0, user_id, video_link);
                            System.out.println("\nNew post has been added to the data base");
                        }
                        catch (SQLException e) {
                            System.out.println("An error happened when inserting a new post");
                        }
                            break;
                        
                        // Select Posts with user names
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
                                flagged = rs.getBoolean("flagged");
                                video_link = rs.getString("video_link");
                                System.out.println("=====================");
                                System.out.println(" -- Post ID ----- ");
                                System.out.println("      " + post_id);
                                System.out.println(" ---- Title ------ ");
                                System.out.println("         " + title);
                                System.out.println(" ---- Content -----");
                                System.out.println("        " + content);
                                System.out.println(" ---- Date -------- ");
                                System.out.println("        " + date);
                                System.out.println(" ---- Upvote counts");
                                System.out.println("        " + vote_up);
                                System.out.println(" ---- Downvote counts");
                                System.out.println("        " + vote_down);
                                System.out.println(" ---- Last Name ---");
                                System.out.println("        " + last_name);
                                System.out.println(" ---- First Name -- ");
                                System.out.println("        " + first_name);
                                System.out.println(" ---- Video Link ------");
                                System.out.println("        " + video_link);
                                System.out.println(" ---- Flagged ------");
                                System.out.println("        " + flagged);
                                System.out.println("=====================");
                                System.out.println();
                                //System.out.printf ("%15d%15s%15s%15s%15d%15d%15s%15s%15b\n",post_id, title, content, date, vote_up, vote_down, first_name, flagged);
                                }
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when selecting all posts");
                            }
                            break;

                        // View the post
                        case 5:
                            
                            post_id = getInputInt("\tEnter the post id you want to check: ");
                           
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
                                flagged = rs.getBoolean("flagged");
                                video_link = rs.getString("video_link");
                                System.out.println();
                                System.out.println("=====================");
                                System.out.println(" -- Post ID -------");
                                System.out.println("      " + post_id);
                                System.out.println(" ---- Title -------");
                                System.out.println("        " + title);
                                System.out.println(" ---- Content ----- ");
                                System.out.println("        " + content);
                                System.out.println(" ---- Date -------- ");
                                System.out.println("        " + date);
                                System.out.println(" ---- Upvote counts  ");
                                System.out.println("        " + vote_up);
                                System.out.println(" ---- Downvote counts ");
                                System.out.println("        " + vote_down);
                                System.out.println(" ---- User Id ----- ");
                                System.out.println("        " + user_id);
                                System.out.println(" ---- Flagged ------ ");
                                System.out.println("        " + flagged);
                                System.out.println(" ---- Video Link ------ ");
                                System.out.println("        " + video_link);
                                System.out.println("=====================");
                                System.out.println();

                                //System.out.println(post_id + ", " + title + ", " + content + " ," + date +
                           //", " + vote_up + " ," + vote_down + " ," + user_id + " ," + flagged);
                            }while(rs.next());
                        }
                            catch (SQLException e) {
                                System.out.println("An error happened when selecting the post");
                            }
                            break;
                            
                        // update post
                        case 6: 
                            System.out.println();
                            
                                post_id = getInputInt("\tEnter the post id you want to update: ");
                               
                                try{
                                    rs = database.selectPostById(post_id);
                                    System.out.println();
                              
                                    if(!rs.next())
                                    {
                                    System.out.println("This post Doesn't Exist");
                                    }
                                    else{
                                        System.out.println("\tEnter the new post title: ");
                                        title = keyboard.nextLine();
                                        System.out.println("\tEnter the post content: ");
                                        content = keyboard.nextLine();
                                       
                                        vote_up = getInputInt("\tEnter the number of upvotes: ");
                                       
                                        vote_down = getInputInt("\tEnter the number of downvotes: ");
                                        
                                        flagged = getInputBoolean("\tIs it flagged: true/false: ");
                                        
                                        System.out.println();
                                        try{
                                        
                                        database.updatePostById(title, content, vote_up, vote_down, flagged, post_id);
                                        System.out.println("The post has been reset:) ");
                                        }catch (SQLException e) {
                                        System.out.println("An error happened when updating the post");
                                    }
                                    }}
                                    catch (SQLException e) {
                                        System.out.println("An error happened when updating the post");

                                    }
                                    System.out.println();
                            break;

                        // delete a post
                        case 7:  
                            System.out.println();
                            
                                post_id = getInputInt("\tEnter the post id you want to delete: ");
                              
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
                                    System.out.println("An error happened when deleting the post");

                                }
                                System.out.println();
                            break;
                        case 8:
                        // add functions !!!
                        try{
                            rs = database.SelectPostWithFile();
                            System.out.println();
                            if(!rs.next())
                            {
                                System.out.println("\nThere is no file under posts.");
                                break;
                            }
                            
                            do
                            {
                                post_id = rs.getInt("post_id");
                                first_name = rs.getString("first_name");
                                last_name = rs.getString("last_name");
                                filepath = rs.getString("filepath");
                                String[] parsePath = filepath.split("/",2);
                                System.out.println("=====================");
                                System.out.println(" -- Under Post ----");
                                System.out.println("      ID: " + post_id);
                                System.out.println(" ---- From User ---");
                                System.out.println("        " + first_name+ " "+last_name);
                                System.out.println(" ---- File Name ---");
                                System.out.println("        " + parsePath[parsePath.length-1]);
                                System.out.println("=====================");
                                System.out.println();

                                //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                            }while(rs.next());

                        }catch (SQLException e) {
                            System.out.println("An error happened when selecting the file");

                        }
                        break;
                        case 9: // flag post
                        System.out.println();
                            
                        post_id = getInputInt("\tEnter the post id you want to flag: ");
                        
                        try{
                            rs = database.selectPostById(post_id);
                            System.out.println();
                        
                            if(!rs.next())
                            {
                            System.out.println("This post Doesn't Exist");
                            }
                            else{
                                try{
                                    database.flagPostById(true, post_id);
                                    System.out.println("The post has been flagged:) ");
                                }catch (SQLException e) {
                                System.out.println("An error happened when flagging the post");
                            }
                            }}
                            catch (SQLException e) {
                                System.out.println("An error happened when flagging the post");

                            }
                            System.out.println();
                            break;
                        case 0:    
                            System.out.println("Quit Table Post. Back to Main Menu ...");
                            break;
                    }
                } while(p_choice!=0);
            }   // end of the post table

            else if(operation == 3) // comment table
            {
                int c_choice = 0;
                do {
                    System.out.println();
                    CommentMenu();
                    c_choice = getInputInt("\tplease enter: ");
               
                    switch(c_choice)
                    {   
                        case 1:
                            System.out.println();
                            try{
                                database.createCommentTable();
                                System.out.println("Comment Table is created");
                            }
                                catch (SQLException e) {
                                System.out.println("An error happened when creating the comments table. maybe it has already be created");
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
                                System.out.println("An error happened when dropping the comments table. Please check the key constrains.");
                            }
                            System.out.println();
                            break;

                        case 3: // insert comment
                            System.out.println();
                            System.out.println("\tEnter the content: ");
                            content_comment = keyboard.nextLine();
                            
                            user_id = getInputInt("\tEnter the user id: ");
                           
                            try{
                                rs = database.selectUserById(user_id);
                            
                            
                            post_id = getInputInt("\tEnter the post id: ");
                           
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
                            System.out.println("An error happened when inserting the comment");
                        }
                        System.out.println();
                        break;

                        case 4: // view all comments
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
                            flagged = rs.getBoolean("flagged");
                            System.out.println("=====================");
                            System.out.println(" -- Comment ID --");
                            System.out.println("      " + comment_id);
                            System.out.println(" ---- Content ----- ");
                            System.out.println("        " + content_comment);
                            System.out.println(" ---- Date -------- ");
                            System.out.println("        " + date);
                            System.out.println(" ---- Last Name ---");
                            System.out.println("        " + last_name);
                            System.out.println(" ---- First Name --");
                            System.out.println("        " + first_name);
                            System.out.println("=====================");
                            System.out.println(" ---- Flagged -----");
                            System.out.println("        " + flagged);
                            }
                        }
                        catch (SQLException e) {
                            System.out.println("An error happened when selecting comments");
                        }
                        System.out.println();
                        break;

                        case 5: // select comments by post id
                        System.out.println();
                            try{
                            post_id = getInputInt("\tEnter the post id you want to check: "); 
                         
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
                                flagged = rs.getBoolean("flagged");
                                System.out.println("=====================");
                                System.out.println(" -- Comment ID --");
                                System.out.println("      " + comment_id);
                                System.out.println(" ---- Content ----- ");
                                System.out.println("        " + content);
                                System.out.println(" ---- Date -------- ");
                                System.out.println("        " + date);
                                System.out.println(" ---- User ID -----");
                                System.out.println("        " + user_id);
                                System.out.println(" ---- Post ID -----");
                                System.out.println("        " + post_id);
                                System.out.println(" ---- Flagged -----");
                                System.out.println("        " + flagged);
                                System.out.println("=====================");
                                System.out.println();

                                //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                            }while(rs.next());
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when selecting the comments");
                            }
                            System.out.println();
                            break;

                    case 6: // select comments by user id
                        System.out.println();
                        try{
                            user_id = getInputInt("\tEnter the user id you want to check: "); 
                          
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
                                System.out.println("=====================");
                                System.out.println(" -- Comment ID --");
                                System.out.println("      " + comment_id);
                                System.out.println(" ---- Content -----");
                                System.out.println("        " + content);
                                System.out.println(" ---- Date --------");
                                System.out.println("        " + date);
                                System.out.println(" ---- User ID -----");
                                System.out.println("        " + user_id);
                                System.out.println(" ---- Post ID -----");
                                System.out.println("        " + post_id);
                                System.out.println("=====================");
                                System.out.println();

                                //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                            }while(rs.next());
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when selecting the comment");
                            }
                            System.out.println();
                            break;


                            
                        case 7: // update
                            try{
                            comment_id = getInputInt("\tEnter the comment id you want to change: "); 
                            
                            
                            System.out.println("\tEnter the new content you want to update: " );
                            content_comment = keyboard.nextLine(); 
                            
                                database.updateCommentById(content_comment, comment_id);
                                System.out.println("Comment has been updated.");
                            }
                        catch (SQLException e) {
                            System.out.println("An error happened when updating the comment");
                        }
                        System.out.println();
                            break;
                        case 8: // delete
                        System.out.println();
                            comment_id = getInputInt("\tEnter the comment id you want to delete: "); 
                           
                            try{
                                database.deleteCommentById(comment_id);
                                System.out.println("Comment "+ comment_id + " has been deleted.");
                            }
                            
                        catch (SQLException e) {
                            System.out.println("An error happened when deleting the comment");
                        }System.out.println();
                        break;
                        case 9:

                        //add functions !!!
                        try{
                            rs = database.SelectCommentWithFile();
                            System.out.println();
                            if(!rs.next())
                            {
                                System.out.println("\nThere is no file under all comments.");
                                break;
                            }
                            
                            do
                            {
                                post_id = rs.getInt("post_id");
                                comment_id = rs.getInt("comment_id");
                                first_name = rs.getString("first_name");
                                last_name = rs.getString("last_name");
                                filepath = rs.getString("filepath");
                                String[] parsePath = filepath.split("/",3);
                                System.out.println("=====================");
                                System.out.println(" -- Under Post ----");
                                System.out.println("      ID: " + post_id);
                                System.out.println(" ---- With Comment ----");
                                System.out.println("        ID: " + comment_id);
                                System.out.println(" ---- From User ---");
                                System.out.println("        " + first_name+ " "+last_name);
                                System.out.println(" ---- File Name ---");
                                System.out.println("        " + parsePath[parsePath.length-1]);
                                System.out.println("=====================");
                                System.out.println();

                                //System.out.println(comment_id + ", " + content + ", " + date + " ," + user_id + " ," + post_id);
                            }while(rs.next());

                        }catch (SQLException e) {
                            System.out.println("An error happened when selecting the file");

                        }
                        break;

                        case 10:
                        System.out.println();
                            
                        comment_id = getInputInt("\tEnter the comment id you want to flag: ");
                        
                        try{
                            rs = database.selectCommentById(comment_id);
                            System.out.println();
                        
                            if(!rs.next())
                            {
                            System.out.println("This comment Doesn't Exist");
                            }
                            else{
                                try{
                                    database.flagCommentById(true, comment_id);
                                    System.out.println("The comment has been flagged:) ");
                                }catch (SQLException e) {
                                System.out.println("An error happened when flagging the comment");
                            }
                            }}
                            catch (SQLException e) {
                                System.out.println("An error happened when flagging the comment");

                            }
                            System.out.println();
                            break;
                        
                        case 0:    
                            System.out.println("Quit Table Comment. Back to Main Menu ...");
                            break;
                    } // end of switch 
                    System.out.println();
                } while(c_choice != 0);
            }   
            else if(operation == 4) // user table
            {
             
                int v_choice = 0;
                // Scanner v = new Scanner(System.in);
                // Scanner vin = new Scanner(System.in);
               
                do {
                    System.out.println();
                    VotesMenu();
                    v_choice = getInputInt("\tplease enter: ");
                    
                    switch(v_choice)
                    {   
                        case 1:
                            
                            try{
                                database.createVoteTable();
                                System.out.println("Table Vote is created");
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when creating the votes table. May be it has already been created");
                            }System.out.println();
                            break;

                        case 2:
                            
                            try{
                                database.dropVoteTable();
                                System.out.println("Table Vote is dropped");
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when dropping the votes table. Please check the key constains");
                            }System.out.println();
                            break;

                        case 3: // insert a vote
                            System.out.println();
                            user_id = getInputInt("\tEnter the user id: ");
                            
                            try{
                            rs = database.selectUserById(user_id);
                            if(!rs.next())
                            {
                                System.out.println("User doesn't exist");
                                break;
                            }
                            post_id = getInputInt("\tEnter the post id: ");
                        
                            
                            rs = database.selectPostById(post_id);
                            if(!rs.next())
                            {
                                System.out.println("Post doesn't exist");
                                break;
                            }

                            up_Vote = getInputBoolean("\tEnter if it is upvote(true/false): ");
                           
                            
                            down_Vote = getInputBoolean("\tEnter if it is downvote(true/false): ");
                            if(up_Vote && down_Vote)
                                throw new SQLException();
                                database.insertVote(user_id, post_id, up_Vote, down_Vote);
                                System.out.println("The vote has been inserted.");
                                rs = database.selectPostById(post_id);
                                if(rs.next()){
                                int vote_up_counts = rs.getInt("vote_up");
                                int vote_down_counts = rs.getInt("vote_down");
                                if(up_Vote == true)
                                {
                                    database.updatePostVotesUpIncrease(post_id);
                                }
                                else{
                                    continue;
                                }
                                if(down_Vote == true)
                                {
                                    database.updatePostVotesDownIncrease(post_id);
                                }
                                else{
                                    continue;
                                }
                            }
                            }
                            catch (SQLException e) {
                                System.out.println("An error happened when inserting the vote. Note that user cannot both like and dislike a post");
                            }
                            break;
                        case 4: //select votes by user id
                        System.out.println();
                            try{
                                user_id = getInputInt("\tEnter the user id: ");
                               
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
                                    System.out.println("An error happened when selecting the vote");
                                }
                            break;
                        case 5:   //select votes by ids
                            System.out.println();
                            try{
                                user_id = getInputInt("\tEnter the user id: ");
                              
                                post_id = getInputInt("\tEnter the post id: ");
                                
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
                                    System.out.println("An error happened when selecting the votes");
                                }
                            break;

                        case 6: // update votes by ids
                            System.out.println();
                            user_id = getInputInt("\tEnter the user id: ");
                         
                            post_id = getInputInt("\tEnter the post id: ");
                      
                     
                            try{
                                boolean original_up_Vote = true;
                                boolean original_down_Vote = true;
                                rs = database.selectVoteByIds(user_id, post_id);
                                if(!rs.next())
                                {
                                    System.out.println("\nThis user hasn't voted this post");
                                }
                                else{   
                                    original_up_Vote = rs.getBoolean("vote_up");
                                    original_down_Vote=rs.getBoolean("vote_down");
                                    up_Vote = getInputBoolean("\tEnter if it is upvote(true/false): ");
                                    
                                    down_Vote = getInputBoolean("\tEnter if it is downvote(true/false): ");
                                    if(up_Vote && down_Vote)
                                        throw new SQLException();
                                    database.updateVoteByIds(up_Vote, down_Vote, user_id, post_id);
                                    System.out.println("\nThe vote has been reset: ");

                                    rs = database.selectVoteByIds(user_id, post_id);

                                    rs = database.selectPostById(post_id);
                                    if(rs.next()){
                                    int vote_up_counts = rs.getInt("vote_up");
                                    int vote_down_counts = rs.getInt("vote_down");
                                    if(up_Vote != original_up_Vote){
                                        if(up_Vote == true)
                                        {
                                            database.updatePostVotesUpIncrease(post_id);
                                        }
                                        else{
                                            database.updatePostVotesUpDecrease(post_id);
                                        }
                                    }
                                    if(down_Vote != original_down_Vote){
                                        if(down_Vote == true)
                                        {
                                            database.updatePostVotesDownIncrease(post_id);
                                        }
                                        else{
                                            database.updatePostVotesDownDecrease(post_id);
                                        }
                                }
                                }

                                }

                                }catch (SQLException e) {
                                    System.out.println("An error happened when updating the vote. Note that user cannot both like and dislike to a post");
                                    }
                            break;
                        case 7:
                            System.out.println();
                            user_id = getInputInt("\tTo delete the vote, enter the user id: ");
                           
                            post_id = getInputInt("\tTo delete the vote, enter the post id: ");
                         
                            try{
                                rs = database.selectVoteByIds(user_id, post_id);
                                if(!rs.next())
                                {
                                    System.out.println("This user hasn't voted this post.");
                                }
                                else{
                                database.deleteVoteByIds(user_id, post_id);

                                System.out.println("The vote has been deleted.");
                                rs= database.selectVoteByIds(user_id, post_id);
                                if(rs.next()){
                                    up_Vote = rs.getBoolean("vote_up");
                                    down_Vote=rs.getBoolean("vote_down");
                                }    
                                rs = database.selectPostById(post_id);
                                    if(rs.next()){
                                    int vote_up_counts = rs.getInt("vote_up");
                                    int vote_down_counts = rs.getInt("vote_down");
                                    if(up_Vote == true) // if true, decrease by 1
                                    {
                                        database.updatePostVotesUpDecrease(post_id);
                                    }
                                    else{
                                        continue;
                                    }
                                    if(down_Vote == true)   // if true, decrease by 1
                                    {
                                        database.updatePostVotesDownDecrease(post_id);
                                    }
                                    else{
                                        continue;
                                    }
                                }
                            }}
                            catch (SQLException e) {
                                System.out.println("An error happened when deleting the vote");
                            }System.out.println();
                    break;
                        case 0:   
                            System.out.println("Quit Table Votes. Back to Main Menu ...");
                            
                            break;
                    }   
                
                } while(v_choice != 0); // end of do loop
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
  
       
 
  
       
 