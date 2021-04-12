package edu.lehigh.cse216.macrosoft.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;


/**
 * Encapsulate a fully configured database.
 */
public class Database {

    /**
     * Database connection.
     */
    private Connection mConnection;

    /**
     * This class follows singleton design.
     */
    private Database() {}

    /**
     * There should only be one {@code Database} gets instantiated during
     * the lifetime of the application.
     */
    private static Database database;

    /**
     * Create a instance of database with url.
     * @param url the url that includes all credentials to connect to the
     *            postgres db.
     * @return the database
     */
    static Database getInstance(String url)
            throws SQLException, ClassNotFoundException, URISyntaxException {
        if (database == null) {
            Database db = new Database();
            db.getConnection(url);
            try {
                db.initPreparedStatements();
            } catch (SQLException exp) {
                db.disconnect();
                throw exp;
            }
            database = db;
        }
        return database;
    }

    /**
     * Make the database connection based on the url provided.  It sets the
     * {@code mConnection} directly upon success, and leaves it as {@code null}
     * upon errors.
     */
    private void getConnection(String url)
            throws SQLException, ClassNotFoundException, URISyntaxException {
        Class.forName("org.postgresql.Driver");
        URI dbUri = new URI(url);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = String.format(
                "jdbc:postgresql://%s:%d%s?sslmode=require",
                dbUri.getHost(), dbUri.getPort(), dbUri.getPath()
        );
        Connection conn = DriverManager.getConnection(dbUrl, username, password);
        if (conn == null) {
            throw new SQLException("DriverManager.getConnection() returned a null object");
        }
        mConnection = conn;
    }

    /**
     * Disconnect from the database.
     */
    void disconnect() throws SQLException {
        if (mConnection == null) return;
        try {
            mConnection.close();
        } finally {
            mConnection = null;
        }
    }

    /*
     * Prepared statements of this Database.  Generated by the script.
     */
    private PreparedStatement mCreateUserTable;
    private PreparedStatement mDropUserTable;
    private PreparedStatement mInsertUser;
    private PreparedStatement mSelectUserById;
    private PreparedStatement mSelectUserByEmail;
    private PreparedStatement mDeleteUserById;
    //private PreparedStatement mUpdateUserById;

    private PreparedStatement mCreatePostTable;
    private PreparedStatement mDropPostTable;
    private PreparedStatement mInsertPost;
    private PreparedStatement mSelectAllPosts;
    private PreparedStatement mSelectAllPostsJoinUsers;
    private PreparedStatement mSelectPostById;
    private PreparedStatement mUpdatePostById;
    private PreparedStatement mDeletePostById;

    private PreparedStatement mSelectPostWithFile;

    private PreparedStatement mSelectAll;

    private PreparedStatement mCreateCommentTable;
    private PreparedStatement mDropCommentTable;
    private PreparedStatement mInsertComment;
    private PreparedStatement mSelectAllCommentsJoinUsers;
    private PreparedStatement mSelectCommentsByUserId;
    private PreparedStatement mSelectCommentsByPostId;
    private PreparedStatement mUpdateCommentById;
    private PreparedStatement mDeleteCommentById;

    private PreparedStatement mCreateVoteTable;
    private PreparedStatement mDropVoteTable;
    private PreparedStatement mInsertVote;
    private PreparedStatement mSelectVotesByUserId;
    private PreparedStatement mSelectVoteByIds;
    private PreparedStatement mUpdateVoteByIds;
    private PreparedStatement mDeleteVoteByIds;
    private PreparedStatement mSelectCommentWithFile;
    private PreparedStatement mUpdatePostVotesUpIncrease;
    private PreparedStatement mUpdatePostVotesUpDecrease;
    private PreparedStatement mUpdatePostVotesDownIncrease;
    private PreparedStatement mUpdatePostVotesDownDecrease;

    private PreparedStatement mGetNewestPost;


    private PreparedStatement mSelectFileByPostID;
    private PreparedStatement mSelectFileByCommentID;

    private PreparedStatement mDeleteFileByPostID;
    private PreparedStatement mDeleteFileByCommentID;

    private PreparedStatement mSelectFileUnderPostByEmail;
    private PreparedStatement mSelectFileUnderCommentByEmail;
    /**
     * Initialize the tables of this Database.  The primary job is to create
     * the {@code PreparedStatement}s for Database operations.  This function
     * is to be generated by the script.
     */
    private void initPreparedStatements() throws SQLException {

        // User table prepared statement
        mDeleteFileByPostID = mConnection.prepareStatement("update posts set filetype='', filedate='',filepath='',links='' where post_id=?;");
        mDeleteFileByCommentID = mConnection.prepareStatement("update comments set filetype='', filedate='',filepath='',links='' where comment_id=?;");
        mSelectFileByPostID= mConnection.prepareStatement("select  from posts where post_id=? and filepath!='';");
        mSelectFileByCommentID = mConnection.prepareStatement("select filepath from comments where comment_id=? and filepath!='';");
        mSelectFileUnderPostByEmail = mConnection.prepareStatement("select post_id, filepath, title, users.user_id, email from posts, users where users.email=? and filepath != '' and users.user_id=posts.user_id;");
        
        mSelectFileUnderCommentByEmail = mConnection.prepareStatement(" select comment_id, filepath, content, users.user_id, email from comments, users where users.email=? and filepath != '' and users.user_id=comments.user_id;");
        mCreateUserTable = mConnection.prepareStatement("CREATE TABLE users ( user_id SERIAL PRIMARY KEY, email VARCHAR(50), first_name VARCHAR(50), last_name VARCHAR(50) )");
        mDropUserTable = mConnection.prepareStatement("DROP TABLE users");
        mInsertUser = mConnection.prepareStatement("INSERT INTO users VALUES (default, ?, ?, ?)");
        mSelectUserById = mConnection.prepareStatement("SELECT * FROM users WHERE user_id=?");
        mSelectUserByEmail = mConnection.prepareStatement("SELECT * FROM users WHERE email=?");
        mDeleteUserById = mConnection.prepareStatement("DELETE  FROM users WHERE user_id=?");
        
        // Post Table Prepared Statement
        mSelectAll = mConnection.prepareStatement("SELECT * from posts");

        // phase3: change the statement 
        mCreatePostTable = mConnection.prepareStatement("CREATE TABLE posts ( post_id SERIAL PRIMARY KEY, title VARCHAR(100), content VARCHAR(500), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, vote_up INTEGER, vote_down INTEGER, user_id INTEGER REFERENCES users (user_id), pinned BOOLEAN, filetype varchar(10), filedate varchar(19), filepath varchar(100), links varchar(500) )");


        mDropPostTable = mConnection.prepareStatement("Drop TABLE posts");
        mInsertPost = mConnection.prepareStatement("INSERT INTO posts VALUES (default, ?, ?, default, ?, ?, ?, ? ,'','' ,'','')");
        mSelectAllPostsJoinUsers = mConnection.prepareStatement("SELECT post_id, title, content, date, vote_up, vote_down, pinned, users.last_name, users.first_name FROM posts JOIN users ON posts.user_id=users.user_id");
        mSelectAllPosts = mConnection.prepareStatement("SELECT posts.post_id, posts.title, posts.content, posts.date, posts.vote_up, posts.vote_down, posts.pinned, users.last_name, users.first_name, users.email FROM posts LEFT JOIN users ON posts.user_id=users.user_id");
        mSelectPostById = mConnection.prepareStatement("SELECT * FROM posts WHERE post_id=?");
        mUpdatePostById = mConnection.prepareStatement("UPDATE posts SET title=?, content=?, vote_up=?, vote_down=?, pinned=? WHERE post_id=?");
        mDeletePostById = mConnection.prepareStatement("DELETE FROM posts WHERE post_id=?");
        mSelectPostWithFile = mConnection.prepareStatement("select post_id, filepath, last_name, first_name from posts, users where posts.user_id= users.user_id and filepath != '';");
        // Comments Table Prepared Statement

        // phase3: change the statement
        mCreateCommentTable = mConnection.prepareStatement("CREATE TABLE comments ( comment_id SERIAL PRIMARY KEY, content VARCHAR(500), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, user_id INTEGER REFERENCES users (user_id), post_id INTEGER REFERENCES posts (post_id), filetype varchar(10), filedate varchar(19), filepath varchar(100), links varchar(500) )");
        mDropCommentTable = mConnection.prepareStatement("DROP TABLE comments");
        mInsertComment = mConnection.prepareStatement("INSERT INTO comments VALUES (default, ?, default, ?, ?, '','' ,'','')");
        mSelectAllCommentsJoinUsers = mConnection.prepareStatement("SELECT comment_id, content, date, users.last_name, users.first_name FROM comments JOIN users ON comments.user_id=users.user_id");
        mSelectCommentsByUserId = mConnection.prepareStatement("SELECT * FROM comments WHERE user_id=?");
        mSelectCommentsByPostId = mConnection.prepareStatement("SELECT * FROM comments WHERE post_id=?");
        mUpdateCommentById = mConnection.prepareStatement("UPDATE comments SET content=? WHERE comment_id=?");
        mDeleteCommentById = mConnection.prepareStatement("DELETE FROM comments WHERE comment_id=?");
        mSelectCommentWithFile = mConnection.prepareStatement("select post_id,comment_id, filepath, last_name, first_name from comments, users where comments.user_id= users.user_id and filepath != '';");
        // Votes Table Prepared Statement
        mCreateVoteTable = mConnection.prepareStatement("CREATE TABLE votes ( user_id INTEGER, post_id INTEGER, vote_up BOOLEAN, vote_down BOOLEAN )");
        mDropVoteTable = mConnection.prepareStatement("DROP TABLE votes");
        mInsertVote = mConnection.prepareStatement("INSERT INTO votes VALUES (?, ?, ?, ?)");
        mSelectVotesByUserId = mConnection.prepareStatement("SELECT * FROM votes WHERE user_id=?");
        mSelectVoteByIds = mConnection.prepareStatement("SELECT * FROM votes WHERE user_id=? AND post_id=?");
        mUpdateVoteByIds = mConnection.prepareStatement("UPDATE votes SET vote_up=?, vote_down=? WHERE user_id=? AND post_id=?");
        mDeleteVoteByIds = mConnection.prepareStatement("DELETE FROM votes WHERE user_id=? AND post_id=?");
        mUpdatePostVotesUpIncrease = mConnection.prepareStatement("UPDATE posts SET vote_up=vote_up+1 WHERE post_id=?");
        mUpdatePostVotesUpDecrease = mConnection.prepareStatement("UPDATE posts SET vote_up=vote_up-1 WHERE post_id=?");
        mUpdatePostVotesDownIncrease = mConnection.prepareStatement("UPDATE posts SET vote_down=vote_down+1 WHERE post_id=?");
        mUpdatePostVotesDownDecrease = mConnection.prepareStatement("UPDATE posts SET vote_down=vote_down-1 WHERE post_id=?");

        mGetNewestPost = mConnection.prepareStatement("select max(post_id) from posts");
    }

    // **********************************************************************
    // *                database operations (generated)
    // **********************************************************************

    ResultSet GetNewestPost() throws SQLException {
        return mGetNewestPost.executeQuery();
    }
    
    void createUserTable() throws SQLException {
        mCreateUserTable.execute();
    }

    void dropUserTable() throws SQLException {
        mDropUserTable.execute();
    }

    void insertUser(String email, String first, String last) throws SQLException {
        mInsertUser.setString(1, email);
        mInsertUser.setString(2, first);
        mInsertUser.setString(3, last);
        mInsertUser.executeUpdate();
    }

    ResultSet selectUserById(int userId) throws SQLException {
        mSelectUserById.setInt(1, userId);
        return mSelectUserById.executeQuery();
    }

    ResultSet SelectFileByPostID(int userId) throws SQLException {
        mSelectFileByPostID.setInt(1, userId);
        return mSelectFileByPostID.executeQuery();
    }

    ResultSet SelectFileByCommentID(int userId) throws SQLException {
        mSelectFileByCommentID.setInt(1, userId);
        return mSelectFileByCommentID.executeQuery();
    }
    
    ResultSet SelectFileUnderPostByEmail(String email) throws SQLException {
        mSelectFileUnderPostByEmail.setString(1, email);
        return mSelectFileUnderPostByEmail.executeQuery();
    }
    ResultSet SelectFileUnderCommentByEmail(String email) throws SQLException {
        mSelectFileUnderCommentByEmail.setString(1, email);
        return mSelectFileUnderCommentByEmail.executeQuery();
    }
    ResultSet selectUserByEmail(String email) throws SQLException {
        mSelectUserByEmail.setString(1, email);
        return mSelectUserByEmail.executeQuery();
    }

    void deleteUserById(int userId) throws SQLException {
        mDeleteUserById.setInt(1, userId);
        mDeleteUserById.executeUpdate();
    }



    // ***************
    // Post Table Part
    // ***************
    void createPostTable() throws SQLException {
        mCreatePostTable.execute();
    }

    void dropPostTable() throws SQLException {
        mDropPostTable.execute();
    }

    ResultSet selectPostAll() throws SQLException{
        return mSelectAll.executeQuery();

    }

    ResultSet SelectPostWithFile() throws SQLException{
        return mSelectPostWithFile.executeQuery();
    }

    ResultSet SelectCommentWithFile() throws SQLException{
        return mSelectCommentWithFile.executeQuery();
    }

    void insertPost(String title, String content, int upVote, int downVote, int userId, boolean pinned) throws SQLException {
        mInsertPost.setString(1, title);
        mInsertPost.setString(2, content);
        mInsertPost.setInt(3, upVote);
        mInsertPost.setInt(4, downVote);
        mInsertPost.setInt(5, userId);
        mInsertPost.setBoolean(6, pinned);
        mInsertPost.executeUpdate();
    }

    ResultSet selectAllPostsJoinUsers() throws SQLException {
        return mSelectAllPostsJoinUsers.executeQuery();
    }

    ResultSet selectPostById(int postId) throws SQLException {
        mSelectPostById.setInt(1, postId);
        return mSelectPostById.executeQuery();
    }

    void updatePostById(String title, String content, int upVote, int downVote, boolean pinned, int postId) throws SQLException {
        mUpdatePostById.setString(1, title);
        mUpdatePostById.setString(2, content);
        mUpdatePostById.setInt(3, upVote);
        mUpdatePostById.setInt(4, downVote);
        mUpdatePostById.setBoolean(5, pinned);
        mUpdatePostById.setInt(6, postId);
        mUpdatePostById.executeUpdate();
    }     

    void DeleteFileByPostID(int postId) throws SQLException {

        mDeleteFileByPostID.setInt(1, postId);
        mDeleteFileByPostID.executeUpdate();
    }     
    void DeleteFileByCommentID(int commentId) throws SQLException {
        mDeleteFileByCommentID.setInt(1, commentId);
        mDeleteFileByCommentID.executeUpdate();
    }        

    void deletePostById(int postId) throws SQLException {
        mDeletePostById.setInt(1, postId);
        mDeletePostById.executeUpdate();
    }

    void createCommentTable() throws SQLException {
        mCreateCommentTable.execute();
    }

    void dropCommentTable() throws SQLException {
        mDropCommentTable.execute();
    }

    void insertComment(String content, int userId, int postId) throws SQLException {
        mInsertComment.setString(1, content);
        mInsertComment.setInt(2, userId);
        mInsertComment.setInt(3, postId);
        mInsertComment.executeUpdate();
    }

    ResultSet selectAllCommentsJoinUsers() throws SQLException {
        return mSelectAllCommentsJoinUsers.executeQuery();
    }

    ResultSet selectCommentsByUserId(int userId) throws SQLException {
        mSelectCommentsByUserId.setInt(1, userId);
        return mSelectCommentsByUserId.executeQuery();
    }

    ResultSet selectCommentsByPostId(int postId) throws SQLException {
        mSelectCommentsByPostId.setInt(1, postId);
        return mSelectCommentsByPostId.executeQuery();
    }

    void updateCommentById(String content, int commentId) throws SQLException {
        mUpdateCommentById.setString(1, content);
        mUpdateCommentById.setInt(2, commentId);
        mUpdateCommentById.executeUpdate();
    }

    void deleteCommentById(int commentId) throws SQLException {
        mDeleteCommentById.setInt(1, commentId);
        mDeleteCommentById.executeUpdate();
    }



    void createVoteTable() throws SQLException {
        mCreateVoteTable.execute();
    }

    void dropVoteTable() throws SQLException {
        mDropVoteTable.execute();
    }

    void insertVote(int userId, int postId, boolean upVote, boolean downVote) throws SQLException {
        mInsertVote.setInt(1, userId);
        mInsertVote.setInt(2, postId);
        mInsertVote.setBoolean(3, upVote);
        mInsertVote.setBoolean(4, downVote);
        mInsertVote.executeUpdate(); // post vote increment
    }

    ResultSet selectVotesByUserId(int userId) throws SQLException {
        mSelectVotesByUserId.setInt(1, userId);
        return mSelectVotesByUserId.executeQuery();
    }

    ResultSet selectVoteByIds(int userId, int postId) throws SQLException {
        mSelectVoteByIds.setInt(1, userId);
        mSelectVoteByIds.setInt(2, postId);
        return mSelectVoteByIds.executeQuery();
    }

    void updateVoteByIds(boolean upVote, boolean downVote, int userId, int postId) throws SQLException {
        mUpdateVoteByIds.setBoolean(1, upVote);
        mUpdateVoteByIds.setBoolean(2, downVote);
        mUpdateVoteByIds.setInt(3, userId);
        mUpdateVoteByIds.setInt(4, postId);
        mUpdateVoteByIds.executeUpdate();
    }
    
    void deleteVoteByIds(int userId, int postId) throws SQLException {
        mDeleteVoteByIds.setInt(1, userId);
        mDeleteVoteByIds.setInt(2, postId);
        mDeleteVoteByIds.executeUpdate();
    }

    void updatePostVotesUpIncrease(int postId) throws SQLException{
        mUpdatePostVotesUpIncrease.setInt(1, postId);
        mUpdatePostVotesUpIncrease.executeUpdate();
    }

    void updatePostVotesUpDecrease(int postId) throws SQLException{
        mUpdatePostVotesUpDecrease.setInt(1, postId);
        mUpdatePostVotesUpDecrease.executeUpdate();
    }

    void updatePostVotesDownIncrease(int postId) throws SQLException{
        mUpdatePostVotesDownIncrease.setInt(1, postId);
        mUpdatePostVotesDownIncrease.executeUpdate();
    }

    void updatePostVotesDownDecrease(int postId) throws SQLException{
        mUpdatePostVotesDownDecrease.setInt(1, postId);
        mUpdatePostVotesDownDecrease.executeUpdate();
    }

}
