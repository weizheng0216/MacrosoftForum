package edu.lehigh.cse216.macrosoft.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
 * Unit test for Admin Database.
 */

public class DatabaseTest extends TestCase {
    /**
    */
    private static Database database;
    //ResultSet rs;
    //ResultSetMetaData rsmd;
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


    boolean upVote;
    boolean downVote;

    protected Database getDatabase() {
        String dbURL = "postgres://vdtksuqjtzvetb:b7ccb5e707b07d8c8bfdf7" +
        "badbae2048282884d6b2e8ad336a71ff5833b2abc3@ec2-52-22-16" +
        "1-59.compute-1.amazonaws.com:5432/d9m8d6ulhh2bbk";
        try {
            database = Database.getInstance(dbURL);
        } catch (SQLException |
            ClassNotFoundException |
            URISyntaxException exp) {
        System.out.println(exp.getMessage());
        System.exit(-1);
        }    
        
        return database;
    }


    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DatabaseTest(String testName){
        super(testName);
    }
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DatabaseTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testDatabase() {
        assertTrue(true);
    }

    /**
     * Test for User Table Attributes
     * check if it is correctly constructed
     */
    
    public void testUser(){
        try{
        Database database = getDatabase();
        ResultSet rs = database.selectUserById(15);
        ResultSetMetaData rsmd = rs.getMetaData();
        String name = rsmd.getColumnName(2);
        assertTrue(name.equals("email"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for User Table Attributes
     * check if it is correctly constructed
     */
    public void testUser1(){
        //database.createPostTable();
        //System.out.println("Post Table is created");
        try{
        Database database = getDatabase();
        ResultSet rs = database.selectUserById(16);
        ResultSetMetaData rsmd = rs.getMetaData();
        String name = rsmd.getColumnName(3);
        assertTrue(name.equals("first_name"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for User Table Attributes
     * check if an existing user is correctly constructed
     */
     public void testUserCheck(){
        String user_email = "sic323@lehigh.edu";
        String user_first_name = "Coco";
        String user_last_name = "Chen";
        try{
            ResultSet rs = database.selectUserByEmail(user_email);
            while(rs.next())
            {
                //user_id = rs.getInt("user_id");
                email = rs.getString("email");
                first_name = rs.getString("first_name");
                last_name = rs.getString("last_name");
            }

        assertTrue(user_email.equals(email));
        assertTrue(user_first_name.equals(first_name));
        assertTrue(user_last_name.equals(last_name));
        database.selectUserByEmail(user_email);
    }catch (SQLException e) {
        e.printStackTrace();
    }
    }


    /**
     * Test for User Table Insert and Delete
     * check if it is correctly constructed
     */
    public void testUserInsert(){
        String user_email = "345345345@google.com";
        String user_first_name = "Try";
        String user_last_name = "Mr.";
        try{
            Database database = getDatabase();
            database.insertUser(user_email, user_first_name, user_last_name);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            ResultSet rs = database.selectUserByEmail(user_email);
            while(rs.next())
            {
                //user_id = rs.getInt("user_id");
                email = rs.getString("email");
                first_name = rs.getString("first_name");
                last_name = rs.getString("last_name");
            }

        assertTrue(user_email.equals(email));
        assertTrue(user_first_name.equals(first_name));
        assertTrue(user_last_name.equals(last_name));
     rs = database.selectUserByEmail(user_email);
     if (rs.next() ) 
     {int user_id_ts = rs.getInt("user_id");
    database.deleteUserById(user_id_ts);
        }
    }catch (SQLException e) {
        e.printStackTrace();
    }
    }



    /**
     * Test for Post Table Attributes
     */
    public void testPost(){
        
        try{
        Database database = getDatabase();
        ResultSet rs = database.selectPostAll();
        ResultSetMetaData rsmd = rs.getMetaData();
        String name = rsmd.getColumnName(7);
        assertTrue(name.equals("user_id"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Test for Post Table Attributes
     * check if an existing post if correctly constructed
     */
    public void testPostConstructor() {
       
        String title_ts = "wohoo";
        String content_ts = "happy monday";
        //int user_id_ts = 2;
        // int upVote = 0;
        // int downVote = 0;
        // boolean pinned_ts = false;
        try{
            ResultSet rs = database.selectPostById(44);
        while(rs.next())
        {
            //post_id = rs.getInt("post_id");
            title = rs.getString("title");
            content = rs.getString("content");
        }

        assertTrue(title_ts.equals(title));
        assertTrue(content_ts.equals(content));
    }catch (SQLException e) {
        e.printStackTrace();
    }
}
    

//     //  /**
//     //  * Test for Comment Table Attributes
//     //  */

    public void testComment(){
        try{
            Database database = getDatabase();
            ResultSet rs = database.selectCommentsByPostId(28);
            ResultSetMetaData rsmd = rs.getMetaData();
            String name = rsmd.getColumnName(3);
            assertTrue(name.equals("date"));
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    /**
     * Test for Comment Table 
     * check if it an existing comment is correctly constructed
     */
    public void testCommentCheck(){
        String comment_content = "Got it!";
        int comment_user_id = 12;  
        try{
            ResultSet rs = database.selectCommentsByPostId(30);
        while(rs.next())
        {
            //post_id = rs.getInt("post_id");
            content = rs.getString("content");
            user_id = rs.getInt("user_id");
          
        }
        assertTrue(comment_content.equals(content));
        assertTrue(comment_user_id == user_id);
    }catch (SQLException e) {
        e.printStackTrace();
    }  
    }
    

     // /**
     //  * Test for Votes Table Attributes
     //  */

    public void testVotes(){
        try{
            Database database = getDatabase();
         ResultSet rs = database.selectVotesByUserId(16);
         ResultSetMetaData rsmd = rs.getMetaData();
        String n = rsmd.getColumnName(2);
        assertTrue(n.equals("post_id"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    /**
     * Test for Votes Table Attributes
     * check if an existing vote is correctly constructed
     */
    public void testVoteCheck(){
        int user_id_vote = 16;
        int post_id_vote = 28;

        try{
            ResultSet rs = database.selectVoteByIds(user_id_vote, post_id_vote);
        while(rs.next())
        {
            //post_id = rs.getInt("post_id");
            user_id = rs.getInt("user_id");
            post_id = rs.getInt("post_id");
            //date = rs.getDate("date");
        }

        assertTrue(user_id_vote == user_id);
        assertTrue(post_id_vote == post_id);
    }catch (SQLException e) {
        e.printStackTrace();
    }
}

    /**
     * Test for Votes Table Insertion and Deletion
     * check if votes can be inserted
     */
    public void testVoteInsert(){
        int user_id_vote = 16;
        int post_id_vote = 30;
        boolean up_vote_ts = true;
        boolean down_vote_ts = false;
        try{
            database.insertVote(user_id_vote, post_id_vote, up_vote_ts, down_vote_ts);
            ResultSet rs = database.selectVoteByIds(user_id_vote, post_id_vote);
            while(rs.next())
            {
            upVote = rs.getBoolean("vote_up");
            user_id = rs.getInt("user_id");
            post_id = rs.getInt("post_id");
            downVote = rs.getBoolean("vote_down");
            }
            assertTrue(upVote == up_vote_ts);
            assertTrue(post_id_vote == post_id);
            assertTrue(post_id_vote == post_id);
            assertTrue(downVote == down_vote_ts);
            database.deleteVoteByIds(16, 30);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
}


