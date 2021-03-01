package edu.lehigh.cse216.macrosoft.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Encapsulate a fully configured database.
 *
 * @author Haocheng Gao
 */
public class Database {

    /**
     * Database connection.
     */
    private Connection mConnection;

    private Database() {}

    /**
     * Create a instance of database with url.
     * @param url the url that includes all credentials to connect to the
     *            postgres db.
     * @return the database
     */
    static Database getInstance(String url)
            throws SQLException, ClassNotFoundException, URISyntaxException {
        Database db = new Database();
        db.getConnection(url);
        try {
            db.initPreparedStmt();
        } catch (SQLException exp) {
            db.disconnect();
            throw exp;
        }
        return db;
    }

    /**
     * Illustrative function that make the database connection based on the
     * url provided. It sets the {@code mConnection} directly upon success,
     * and leaves it as {@code null} upon errors.
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
     * Illustrative function that initialize the prepared statements. The
     * prepared statements will be used to execute SQL instructions.
     */
    private void initPreparedStmt() throws SQLException {
        // SQL Statements
        String createTable = "CREATE TABLE message (" +
                "id         SERIAL PRIMARY KEY, " +
                "title      VARCHAR(100), " +
                "content    VARCHAR(500), " +
                "date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "vote_up    INTEGER, " +
                "vote_down  INTEGER, " +
                "pinned     BOOLEAN," +
                "username   VARCHAR(50))";
        String insertMessage = "INSERT INTO message VALUES (" +
                "default, " + // id
                "?, "       + // title      1
                "?, "       + // content    2
                "default, " + // date
                "?, "       + // vote_up    3
                "?, "       + // vote_down  4
                "?, "       + // pinned     5
                "?)";         // username   6
        String updateMessageById = "UPDATE message SET " +
                "title=?, "     + // 1
                "content=?, "   + // 2
                "vote_up=?, "   + // 3
                "vote_down=?, " + // 4
                "pinned=?, "    + // 5
                "username=? "   + // 6
                "WHERE id=?";     // 7
        String selectAllMessages = "SELECT * FROM message ORDER BY pinned DESC, date DESC";
        String selectMessageById = "SELECT * FROM message WHERE id=?";
        String deleteMessageById = "DELETE FROM message WHERE id=?";
        String dropTable  = "DROP TABLE message";

        // Create prepared statements
        mCreateTable = mConnection.prepareStatement(createTable);
        mInsertMessage = mConnection.prepareStatement(insertMessage);
        mUpdateMessageById = mConnection.prepareStatement(updateMessageById);
        mSelectAllMessages = mConnection.prepareStatement(selectAllMessages);
        mSelectMessageById = mConnection.prepareStatement(selectMessageById);
        mDeleteById = mConnection.prepareStatement(deleteMessageById);
        mDropTable = mConnection.prepareStatement(dropTable);
    }

    // The prepared statements
    private PreparedStatement mCreateTable;
    private PreparedStatement mInsertMessage;
    private PreparedStatement mUpdateMessageById;
    private PreparedStatement mSelectAllMessages;
    private PreparedStatement mSelectMessageById;
    private PreparedStatement mDeleteById;
    private PreparedStatement mDropTable;

    /**
     * Create the message table.
     */
    void createTable() throws SQLException {
        mCreateTable.execute();
    }

    /**
     * Insert a new message to the database.
     */
    void insertMessage(String title, String content,
                       int upVotes, int downVotes,
                       boolean pinned, String username)
            throws SQLException {
        mInsertMessage.setString(1, title);
        mInsertMessage.setString(2, content);
        mInsertMessage.setInt(3, upVotes);
        mInsertMessage.setInt(4, downVotes);
        mInsertMessage.setBoolean(5, pinned);
        mInsertMessage.setString(6, username);
        mInsertMessage.executeUpdate();
    }

    /**
     * Get all messages. The messages will be in descending order based on
     * the timestamp.
     * @return the list of messages.
     */
    ArrayList<Message> selectAllMessages() throws SQLException {
        ArrayList<Message> ls = new ArrayList<>();
        ResultSet rs = mSelectAllMessages.executeQuery();
        while (rs.next()) {
            ls.add(new Message(rs));
        }
        rs.close();
        return ls;
    }

    /**
     * Get a single message by its id.
     * @param id ID of the message
     * @return message with given ID
     */
    Message selectMessageById(int id) throws SQLException {
        mSelectMessageById.setInt(1, id);
        ResultSet rs = mSelectMessageById.executeQuery();
        return rs.next() ? new Message(rs) : null;
    }

    /**
     * Update an existing message with a {@code Message} instance. You can
     * only get such instances though methods like {@code selectMessageById}.
     * @param msg the Message that will override the existing one.
     */
    void updateMessage(Message msg) throws SQLException {
        mUpdateMessageById.setString(1, msg.mTitle);
        mUpdateMessageById.setString(2, msg.mContent);
        mUpdateMessageById.setInt(3, msg.mUpVotes);
        mUpdateMessageById.setInt(4, msg.mDownVotes);
        mUpdateMessageById.setBoolean(5, msg.mPinned);
        mUpdateMessageById.setString(6, msg.mUsername);
        mUpdateMessageById.setInt(7, msg.mID);
        mUpdateMessageById.executeUpdate();
    }

    /**
     * Delete the message with given ID from the database.
     */
    void deleteMessageById(int id) throws SQLException {
        mDeleteById.setInt(1, id);
        mDeleteById.executeUpdate();
    }

    /**
     * Drop the message table. All data will be gone.
     */
    void dropTable() throws SQLException {
        mDropTable.execute();
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

    /**
     * A {@code Message} represents a row in the message table. It include
     * all fields of the row, with the ones that shouldn't be modified by
     * anyone being final. The only way to obtain a {@code Message} instance
     * is by calling {@code Database} methods and pull a message from the
     * database.
     */
    static class Message {
        final int mID;
        String mTitle;
        String mContent;
        final String mDate;
        int    mUpVotes;
        int    mDownVotes;
        String mUsername;
        boolean mPinned;

        /**
         * Private constructor, convert the SQL query result into Message.
         */
        private Message(ResultSet rs) throws SQLException {
            mID = rs.getInt("id");
            mTitle = rs.getString("title");
            mContent = rs.getString("content");
            mDate = rs.getString("date");
            mUpVotes = rs.getInt("vote_up");
            mDownVotes = rs.getInt("vote_down");
            mPinned = rs.getBoolean("pinned");
            mUsername = rs.getString("username");
        }
    }









    // ==============================================================================
    // ==============================================================================
    // ==============================================================================
    // ==============================================================================
    // ==============================================================================
    public static Database getLocalDatabase(String ip, String port,
                                            String user, String pass)
            throws SQLException
    {
        Database db = new Database();
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/", user, pass);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }

        try {
            db.initPreparedStmt();
        } catch (SQLException exp) {
            db.disconnect();
            throw exp;
        }
        return db;
    }

}
