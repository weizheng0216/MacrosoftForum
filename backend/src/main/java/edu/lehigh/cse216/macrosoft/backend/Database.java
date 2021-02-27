package edu.lehigh.cse216.macrosoft.backend;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;



public class Database{


    private Connection mConnection;

    private PreparedStatement mSelectAllSortByVotes;

    private PreparedStatement mSelectAllSortByDate;

    private PreparedStatement mSelectAMessage;

    private PreparedStatement mDeleteAMessage;

    private PreparedStatement mInsertAMessage;

    private PreparedStatement mUpdateAMessage;

    private PreparedStatement mUpdateMessageUpVoteByOne;

    private PreparedStatement mUpdateMessageDownVoteByOne;

    private PreparedStatement mPinAMessage;

    private PreparedStatement mUnpinAMessage;

    private PreparedStatement mCreateTable;

    private PreparedStatement mDropTable;

    private PreparedStatement mSelectUpVoteOfAMessage;

    private PreparedStatement mSelectDownVoteOfAMessage;

    private PreparedStatement mUpdateTitleOfAMessage;

    private PreparedStatement mUpdateContentOfAMessage;

    private PreparedStatement mUpdateUsernameOfAMessage;

    /**
     * Class constructor.
     */
    private Database() {}

    /**
     * configure the database.
     * <p> 
     * this function will first parse the url to get the necessary information to connect the database.
     * If an error take place in connection and parsing url, exception will be thrown and catched in the function.
     * Lastly, preparedStatements are constructed.
     * <p>
     * 
     * @param db_url    this contains the information of database including user infotmation, host, port, and path.
     * @return a database that is ready to use.
     */
    public static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        Database db = new Database();
        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }

         // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            db.mCreateTable = db.mConnection.prepareStatement(
                   "create table message (id serial primary key, "+
                   "title varchar(100), content varchar(500), "+ 
                   "date timestamp default current_timestamp, "+
                   "vote_up integer, vote_down integer, "+
                   "pinned boolean, username varchar(50));");
            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE message");

            // Standard CRUD operations
            db.mSelectAllSortByVotes = db.mConnection.prepareStatement("select * from message order by pinned DESC, vote_up DESC;");
            db.mSelectAllSortByDate = db.mConnection.prepareStatement("select * from message order by pinned DESC, date DESC;");
            db.mSelectAMessage = db.mConnection.prepareStatement("select * from message where id=?;");
            db.mDeleteAMessage = db.mConnection.prepareStatement("delete from message where id=?;");
            db.mInsertAMessage = db.mConnection.prepareStatement("insert into message values (default, ?, ?, default, 0, 0, FALSE, ?);");
            db.mUpdateMessageUpVoteByOne = db.mConnection.prepareStatement("update message set vote_up=vote_up+1 where id = ?;");
            db.mUpdateMessageDownVoteByOne = db.mConnection.prepareStatement("update message set vote_down=vote_down+1 where id = ?;");
            db.mPinAMessage = db.mConnection.prepareStatement("update message set pinned=true where id = ?;");
            db.mUnpinAMessage = db.mConnection.prepareStatement("update message set pinned=false where id = ?;");
            db.mSelectUpVoteOfAMessage = db.mConnection.prepareStatement("select vote_up from message where id = ?;");
            db.mSelectDownVoteOfAMessage = db.mConnection.prepareStatement("select vote_down from message where id = ?;");
            db.mUpdateAMessage = db.mConnection.prepareStatement("update message set title=?, content=?, username=? where id = ?;");
            db.mUpdateTitleOfAMessage = db.mConnection.prepareStatement("update message set title=? where id = ?;");
            db.mUpdateContentOfAMessage = db.mConnection.prepareStatement("update message set content=? where id = ?;");
            db.mUpdateUsernameOfAMessage = db.mConnection.prepareStatement("update message set username=? where id = ?;");
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * this function will disconnect the database
     * <p>
     * if an error took place in disconnection, a SQLException will be thrown and catched in 
     * the function. To handle the exception, mConnection will be set as null. Hence in later
     * part, this function will always return false.
     * <p>
     * @return true if successfully disconnect, false otherwise.
     */
    public synchronized boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * this function will return all messages sorted by date
     * <p>
     * This function wil return an array list of DataRow. This array list contain 
     * all message in table message sortin by date. Each message contains attributes 
     * title, content, username, date, vote_up(that indicates number of up vote 
     * of this message), vote_down(that indicates number of down vote of this message), and 
     * pinned(that indicates whether this message is pinned).
     * NOTE: messages that are pinned are in front of the array list. 
     * <p>
     * @return an array list of DataRow containing all messages sorted by date
     */
    public synchronized ArrayList<DataRow> selectAllSortByDate() {
        ArrayList<DataRow> res = new ArrayList<DataRow>();
        try {
            ResultSet rs = mSelectAllSortByDate.executeQuery();
            while (rs.next()) {
                res.add(new DataRow(rs.getInt("id"), 
                    rs.getString("title"), rs.getString("content"), 
                    rs.getString("username"), rs.getString("date"), 
                    rs.getInt("vote_up"), rs.getInt("vote_down"), 
                    rs.getBoolean("pinned")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * this function will return all messages sorted by votes
     * <p>
     * This function wil return an array list of DataRow. This array list contain 
     * all message in table message sortin by up votes. Each message contains attributes 
     * title, content, username, date, vote_up(that indicates number of up vote 
     * of this message), vote_down(that indicates number of down vote of this message), and 
     * pinned(that indicates whether this message is pinned).
     * NOTE: messages that are pinned are in front of the array list. 
     * NOTE: messages are sort only by up votes, not up votes minus down votes.
     * <p>
     * @return an array list of DataRow contains all messages sorted by votes
     */
    public synchronized ArrayList<DataRow> selectAllSortByVotes() {
        ArrayList<DataRow> res = new ArrayList<DataRow>();
        try {
            ResultSet rs = mSelectAllSortByVotes.executeQuery();
            while (rs.next()) {
                res.add(new DataRow(rs.getInt("id"), 
                    rs.getString("title"), rs.getString("content"), 
                    rs.getString("username"), rs.getString("date"), 
                    rs.getInt("vote_up"), rs.getInt("vote_down"), 
                    rs.getBoolean("pinned")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * this function will return a message has the input id.
     * <p>
     * This function wil return a seached message in form of DataRow. 
     * This message contains attributes: title, content, username, date(when the 
     * message is added), vote_up(that indicates number of up vote of this message), 
     * vote_down(that indicates number of down vote of this message), and pinned(that 
     * indicates whether this message is pinned).
     * <p>
     * @param id    the id of the message user want to search
     * @return a DataRow that has this id. null if this id is not found.
     */
    public synchronized DataRow SelectOne(int id){
        try {
            mSelectAMessage.setInt(1, id);
            ResultSet rs = mSelectAMessage.executeQuery();
            DataRow mdata= null;
            while (rs.next()) {
                mdata = new DataRow(rs.getInt("id"), 
                    rs.getString("title"), rs.getString("content"), 
                    rs.getString("username"), rs.getString("date"), 
                    rs.getInt("vote_up"), rs.getInt("vote_down"), 
                    rs.getBoolean("pinned"));
            }
            rs.close();
            return mdata;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * this function will add vote up by one of the target message.
     * 
     * @param id    the id of the message need to vote up one
     * @return  new vote_up of this message, -1 if input id is not found
     */
    public synchronized int voteUpOne(int id) {
        ResultSet rs; int upVoteValue = -1;

        try {
            mUpdateMessageUpVoteByOne.setInt(1, id);
            mUpdateMessageUpVoteByOne.executeUpdate();
            mSelectUpVoteOfAMessage.setInt(1, id);
            rs = mSelectUpVoteOfAMessage.executeQuery();
            while (rs.next()) {
                upVoteValue = rs.getInt("vote_up");     
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upVoteValue;
    }

    /**
     * this function will add vote up by one of the target message.
     * 
     * @param id    the id of the message need to vote up one
     * @return  new vote_up of this message, -1 if input id is not found
     */
    public synchronized int voteDownOne(int id){
        ResultSet rs; int downVoteValue = -1;
        try {
            mUpdateMessageDownVoteByOne.setInt(1, id);
            mUpdateMessageDownVoteByOne.executeUpdate();
            mSelectDownVoteOfAMessage.setInt(1, id);
            rs = mSelectDownVoteOfAMessage.executeQuery();
            while (rs.next()) {
                downVoteValue = rs.getInt("vote_down");     
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return downVoteValue;
    }

    /**
     * this function will update the title of target id.
     * <p>
     * this function is not designed for front-end. Only admin can access to this function
     * <p>
     * @param id
     * @param title     new title
     * @return      return true if new title is updated successfully; false if the input title is null 
     *      or the target id is not found.
     */
    public synchronized boolean updateOneTitle(int id, String title){
        // Do not update if we don't have valid data
        if (title == null)
            return false;
        // Only update if the current entry is valid (not null)
        try {
            mUpdateTitleOfAMessage.setString(1, title);
            mUpdateTitleOfAMessage.setInt(2, id);
            mUpdateTitleOfAMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * this function will update the content of target id.
     * <p>
     * this function is not designed for front-end. Only admin can access to this function
     * <p>
     * @param id
     * @param content     new content
     * @return      return true if new content is updated successfully; false if the input title is null 
     *      or the target id is not found.
     */
    public synchronized boolean updateOneContent(int id, String content){
        // Do not update if we don't have valid data
        if (content == null)
            return false;
        // Only update if the current entry is valid (not null)
        try {
            mUpdateContentOfAMessage.setString(1, content);
            mUpdateContentOfAMessage.setInt(2, id);
            mUpdateContentOfAMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * this function will update the username of target id.
     * <p>
     * this function is not designed for front-end. Only admin can access to this function
     * <p>
     * @param id
     * @param username     new username
     * @return      return true if new username is updated successfully; false if the input title is null 
     *      or the target id is not found.
     */
    public synchronized boolean updateOneUsername(int id, String username){
        // Do not update if we don't have valid data
        if (username == null)
            return false;
        // Only update if the current entry is valid (not null)
        try {
            mUpdateUsernameOfAMessage.setString(1, username);
            mUpdateUsernameOfAMessage.setInt(2, id);
            mUpdateUsernameOfAMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * this function will update the title, content, and username of target id.
     * <p>
     * this function is not designed for front-end. Only admin can access to this function
     * <p>
     * @param id
     * @param title         new title
     * @param content       new content
     * @param username      new username
     * @return      return true if new title, content, and username are updated successfully; 
     *      false if the inputs are null or the target id is not found.
     */
    public synchronized boolean updateOne(int id, String title, String content, String username){
        // Do not update if we don't have valid data
        if (title == null || content == null || username == null)
            return false;
        // Only update if the current entry is valid (not null)
        try {
            mUpdateAMessage.setString(1, title);
            mUpdateAMessage.setString(2, content);
            mUpdateAMessage.setString(3, username);
            mUpdateAMessage.setInt(4, id);
            mUpdateAMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * this function will delete the message of target id.
     *
     * @param id
     * @return      return true if this message is deleted successfully; false if the id is not found.
     */
    public synchronized boolean deleteOne(int id){
        try{
            mDeleteAMessage.setInt(1, id);
            mDeleteAMessage.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * this function will insert a message
     * <p>
     * The date of this message is automatically set as system time. Both up votes and down votes
     * are 0 as default. Message is not pinned as default. 
     * <p>
     * @param title
     * @param content
     * @param userName
     * @return the row count if table or -1 is any input String is null or database error.
     */
    public synchronized int insertOne(String title, String content, String userName){
        if(title == null || content == null || userName == null){
            return -1;
        }
        int res = -1;
        try{
            mInsertAMessage.setString(1, title);
            mInsertAMessage.setString(2, content);
            mInsertAMessage.setString(3, userName);
            res = mInsertAMessage.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    /**
     * this function will pin a message
     * <p>
     * this function is not designed for front-end. Only admin can access to this function
     * <p>
     * @param id
     * @return true if the message is pinned successfully, false otherwise
     */
    public synchronized boolean pinOne(int id){
        try{
            mPinAMessage.setInt(1, id);
            mPinAMessage.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * this function will unpin a message
     * <p>
     * this function is not designed for front-end. Only admin can access to this function
     * <p>
     * @param id
     * @return true if the message is unpinned successfully, false otherwise
     */
    public synchronized boolean unpinOne(int id){
        try{
            mUnpinAMessage.setInt(1, id);
            mUnpinAMessage.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * this function will create the table message.
     */
    public synchronized void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * this function will drop table message.
     */
    public synchronized void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}