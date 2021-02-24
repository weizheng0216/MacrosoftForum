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

    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
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
    private Database() {}

    static Database getDatabase(String db_url) {
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

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    boolean disconnect() {
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

    public synchronized ArrayList<DataRow> SelectAllSortByDate() {
        ArrayList<DataRow> res = new ArrayList<DataRow>();
        try {
            ResultSet rs = mSelectAllSortByDate.executeQuery();
            while (rs.next()) {
                res.add(new DataRow(rs.getInt("id"), 
                    rs.getString("title"), rs.getString("content"), 
                    rs.getString("username"), rs.getString("date"), 
                    rs.getInt("vote_up"), rs.getInt("downVote"), 
                    rs.getBoolean("pinned")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized ArrayList<DataRow> SelectAllSortByVotes() {
        ArrayList<DataRow> res = new ArrayList<DataRow>();
        try {
            ResultSet rs = mSelectAllSortByVotes.executeQuery();
            while (rs.next()) {
                res.add(new DataRow(rs.getInt("id"), 
                    rs.getString("title"), rs.getString("content"), 
                    rs.getString("username"), rs.getString("date"), 
                    rs.getInt("vote_up"), rs.getInt("downVote"), 
                    rs.getBoolean("pinned")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized DataRow SelectOne(int id){
        try {
            mSelectAMessage.setInt(1, id);
            ResultSet rs = mSelectUpVoteOfAMessage.executeQuery();
            DataRow mdata= null;
            while (rs.next()) {
                mdata = new DataRow(rs.getInt("id"), 
                    rs.getString("title"), rs.getString("content"), 
                    rs.getString("username"), rs.getString("date"), 
                    rs.getInt("vote_up"), rs.getInt("downVote"), 
                    rs.getBoolean("pinned"));
            }
            rs.close();
            return mdata;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public synchronized int voteUpOne(int id) {
        int res = -1;
        try {
            mUpdateMessageUpVoteByOne.setInt(1, id);
            mUpdateMessageUpVoteByOne.executeUpdate();
            res = mSelectUpVoteOfAMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public synchronized int voteDownOne(int id){
        int res = -1;
        try {
            mUpdateMessageDownVoteByOne.setInt(1, id);
            mUpdateMessageDownVoteByOne.executeUpdate();
            res = mSelectDownVoteOfAMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public synchronized int updateOne(int id, String title, String content, String username){
        // Do not update if we don't have valid data
        if (title == null || content == null || username == null)
            return -1;
        // Only update if the current entry is valid (not null)
        try {
            mUpdateAMessage.setString(1, title);
            mUpdateAMessage.setString(2, content);
            mUpdateAMessage.setString(3, username);
            mUpdateAMessage.setInt(4, id);
            mUpdateAMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

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

    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}