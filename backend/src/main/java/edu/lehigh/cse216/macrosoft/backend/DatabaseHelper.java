package edu.lehigh.cse216.macrosoft.backend;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import edu.lehigh.cse216.macrosoft.backend.json.*;

import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static edu.lehigh.cse216.macrosoft.backend.DriveHelper.fromFullPath;
import static edu.lehigh.cse216.macrosoft.backend.DriveHelper.toFullPath;

class DatabaseHelper {
    Database db;

    DatabaseHelper(String DATABASE_URL)
            throws ClassNotFoundException, URISyntaxException, SQLException {
        db = Database.getInstance(DATABASE_URL);
    }

    /**
     * Query the author of the post from the database.
     * @param postId identifier of the post.
     * @return author's user id, or {@code null} if post does not exist.
     */
    String queryPostAuthor(String postId) throws SQLException {
        int postIdInt = Integer.parseInt(postId);
        ResultSet rs = db.selectPostById(postIdInt);
        String authorId = rs.next() ?
                Integer.toString(rs.getInt("user_id")) : null;
        rs.close();
        return authorId;
    }

    /**
     * Query the author of the comment from the database.
     * @param postId identifier of the comment.
     * @return author's user id, or {@code null} if comment does not exist.
     */
    String queryCommentAuthor(String postId,
                              String commentId) throws SQLException {
        int postIdInt = Integer.parseInt(postId);
        int commentIdInt = Integer.parseInt(commentId);
        ResultSet rs = db.selectCommentByIds(postIdInt, commentIdInt);
        String authorId = rs.next() ?
                Integer.toString(rs.getInt("user_id")) : null;
        rs.close();
        return authorId;
    }

    /**
     * The largest query to the database. Attempt to get all information about
     * all posts and the vote status of the requesting user.
     * @param loginUserId ID of the requesting user.
     * @return Response payload.
     */
    AllPostsPayload queryGetAllPosts(String loginUserId) throws SQLException {
        AllPostsPayload resPayload = new AllPostsPayload();
        ResultSet allPostsRs = db.selectAllPosts();
        while (allPostsRs.next()) {
            PostSubtype post = rs2Post(allPostsRs);
            int loginUserIdInt = Integer.parseInt(loginUserId);
            boolean userUpVote = false;
            boolean userDownVote = false;
            ResultSet userVoteRs = db.selectVoteByIds(loginUserIdInt, post.mPostID);
            if (userVoteRs.next()) {
                userUpVote = userVoteRs.getBoolean("vote_up");
                userDownVote = userVoteRs.getBoolean("vote_down");
            }
            userVoteRs.close();
            resPayload.add(IntegratedPostSubtype
                    .fromBasicPost(post, userUpVote, userDownVote));
        }
        allPostsRs.close();
        return resPayload;
    }

    /**
     * Get a user's profile information.
     * @param userId ID of the target user.
     * @return Response payload, or {@code null} if not exist.
     */
    UserInfoPayload queryUserInfo(String userId) throws SQLException {
        int userIdInt = Integer.parseInt(userId);
        // user's info
        ResultSet userRs = db.selectUserById(userIdInt);
        if (!userRs.next()) return null;
        UserSubtype user = rs2User(userRs);
        userRs.close();
        // user's posts
        ArrayList<PostSubtype> posts = new ArrayList<>();
        ResultSet postsRs = db.selectPostsByUserId(userIdInt);
        while (postsRs.next())
            posts.add(rs2Post(postsRs));
        postsRs.close();
        // user's comments
        ArrayList<CommentSubtype> comments = new ArrayList<>();
        ResultSet commentsRs = db.selectCommentsByUserId(userIdInt);
        while (commentsRs.next())
            comments.add(rs2Comment(commentsRs));
        commentsRs.close();

        return new UserInfoPayload(user, posts, comments);
    }

    /**
     * Get the filepath to the file attached to the post.
     * @param postId Identifier of the target post.
     * @return filepath to the file, or {@code null} if there is no file.
     */
    String queryPostFilePath(String postId) throws SQLException {
        int postIdInt = Integer.parseInt(postId);
        ResultSet postRs = db.selectPostById(postIdInt);
        if (postRs.next()) {
            String filepath = postRs.getString("filepath");
            postRs.close();
            if (filepath != null && filepath.length() != 0)
                return filepath;
        }
        postRs.close();
        return null;
    }

    /**
     * Get the filepath to the file attached to the comment under a post.
     * @param postId Identifier of the target post.
     * @param commentId Identifier of the comment under target post.
     * @return filepath to the file, or {@code null} if there is no file.
     */
    String queryCommentFilePath(String postId,
                                String commentId) throws SQLException {
        int postIdInt = Integer.parseInt(postId);
        int commentIdInt = Integer.parseInt(commentId);
        ResultSet commentRs = db.selectCommentByIds(postIdInt, commentIdInt);
        if (commentRs.next()) {
            String filepath = commentRs.getString("filepath");
            commentRs.close();
            if (filepath != null && filepath.length() != 0)
                return filepath;
        }
        commentRs.close();
        return null;
    }

    /**
     * Get a list of fullpath for all comments under a particular post.
     * Note that this function does not check for postId validity.
     * @param postId Identifier of the target post.
     * @return the list containing all fullpath.
     */
    ArrayList<String> queryAllCommentFilePaths(String postId)
            throws SQLException {
        int postIdInt = Integer.parseInt(postId);
        ArrayList<String> list = new ArrayList<>();
        ResultSet commentsRs = db.selectCommentsByPostId(postIdInt);
        while (commentsRs.next())
            list.add(commentsRs.getString("filepath"));
        commentsRs.close();
        return list;
    }

    /**
     * Update a post.
     * @param postId Identify the post to update
     * @param req Front-end request
     */
    void updatePost(String postId,
                    PostRequest req) throws SQLException {
        int postIdInt = Integer.parseInt(postId);
        db.updatePostById(req.title, req.content, postIdInt);
    }

    /**
     * Update a comment.
     * @param commentId Identify the comment to update
     * @param req Front-end request
     */
    void updateComment(String commentId,
                       CommentRequest req) throws SQLException {
        int commentIdInt = Integer.parseInt(commentId);
        db.updateCommentById(req.content, commentIdInt);
    }

    /**
     * Update the vote status of the user on a particular post. If there is no
     * existing vote record, a new one will be inserted.
     * <p>
     * The vote counts in the <i>posts</i> table will also be updated. The table
     * should be re-designed in the future to avoid this kind of operation.
     */
    void updateVote(String userId, String postId,
                    VoteRequest req) throws SQLException {
        int userIdInt = Integer.parseInt(userId);
        int postIdInt = Integer.parseInt(postId);
        ResultSet voteRs = db.selectVoteByIds(userIdInt, postIdInt);
        if (voteRs.next()) {  // there is an existing vote, update
            db.updateVoteByIds(req.upVote, req.downVote, userIdInt, postIdInt);
        } else {  // user hasn't made any vote, create new
            db.insertVote(userIdInt, postIdInt, req.upVote, req.downVote);
        }
        voteRs.close();
        // update vote count under table *posts*
        int upCount, downCount;
        ResultSet upCountRs, downCountRs;
        upCountRs = db.selectUpVoteCountByPostId(postIdInt);
        downCountRs = db.selectDownVoteCountByPostId(postIdInt);
        upCountRs.next();
        downCountRs.next();
        upCount = upCountRs.getInt(1);
        downCount = downCountRs.getInt(1);
        db.updatePostVoteCountById(upCount, downCount, postIdInt);
        upCountRs.close();
        downCountRs.close();
    }

    /**
     * Delete a post, along with all comments under it.
     * @param postId Identify the post to be deleted.
     */
    void deletePost(String postId) throws SQLException {
        int postIdInt = Integer.parseInt(postId);
        db.deleteCommentsByPostId(postIdInt);
        db.deletePostById(postIdInt);
        db.deleteVotesByPostId(postIdInt);
    }

    /**
     * Delete a comment.
     * @param postId This is actually redundant since <i>comment_id</i> is
     *               the primary key of <i>comments</i> table.
     * @param commentId Identify the comment to be deleted.
     */
    void deleteComment(String postId, String commentId) throws SQLException {
        int postIdInt = Integer.parseInt(postId);
        int commentIdInt = Integer.parseInt(commentId);
        db.deleteCommentByIds(postIdInt, commentIdInt);
    }

    /**
     * Add a new post with specified author.
     * @param userId Author of the post.
     * @param req Front-end POST request.
     * @return <i>post_id</i> of the added post.
     */
    String addPost(String userId, PostRequest req) throws SQLException {
        int userIdInt = Integer.parseInt(userId);
        StringBuilder linksSB = new StringBuilder();
        for (String link : req.links)
            linksSB.append(link).append(" ");
        db.insertPost(req.title, req.content, userIdInt, req.fileType,
                "", linksSB.toString());
        // get the id of newly added post
        ResultSet rs = db.selectLatestPostId();
        rs.next();
        int newIdInt = rs.getInt(1);
        String newId = Integer.toString(newIdInt);
        rs.close();
        // set the filepath now
        if (req.fileName != null && req.fileName.length() != 0)
            db.updatePostFilePathById(toFullPath(
                    req.fileName, newId), newIdInt);
        return newId;
    }

    /**
     * Add a new comment with specified author under a post.
     * @param userId Author of the comment.
     * @param postId The containing post.
     * @param req Front-end POST request.
     * @return <i>comment_id</i> of the added comment, or {@code null} if
     *         the post does not exist.
     */
    String addComment(String userId, String postId,
                      CommentRequest req) throws SQLException {
        int userIdInt = Integer.parseInt(userId);
        int postIdInt = Integer.parseInt(postId);
        ResultSet postRs = db.selectPostById(postIdInt);
        if (!postRs.next()) {
            return null;  // post does not exist
        }
        postRs.close();

        StringBuilder linksSB = new StringBuilder();
        for (String link : req.links)
            linksSB.append(link).append(" ");
        db.insertComment(req.content, userIdInt, postIdInt, req.fileType,
                req.fileName, linksSB.toString());
        // get the id of newly added comment
        ResultSet rs = db.selectLatestPostId();
        rs.next();
        int newIdInt = rs.getInt(1);
        String newId = Integer.toString(newIdInt);
        rs.close();
        // set the filepath now
        if (req.fileName != null && req.fileName.length() != 0)
            db.updateCommentFilePathById(toFullPath(
                    req.fileName, postId, newId), newIdInt);
        return newId;
    }

    /**
     * Add new user to the database if the user described by
     * {@code GoogleIdToken.Payload} does not exist. Uniqueness of users are
     * determined by their <i>email</i>.
     * <p>
     * It is assumed that the domain of the user has already been verified.
     * @param payload Google's resource server payload.
     * @return <i>user_id</i> of the newly added user or the existing user.
     */
    String addUser(GoogleIdToken.Payload payload) throws SQLException {
        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        ResultSet userRs = db.selectUserByEmail(email);
        int newId;  // the id to be returned
        if (userRs.next()) {  // user already exist in db
            newId = userRs.getInt(1);
        } else {  // user does not exist
            db.insertUser(email, firstName, lastName);
            ResultSet idRs = db.selectLatestUserId();
            idRs.next();
            newId = idRs.getInt(1);
            idRs.close();
        }
        userRs.close();
        return Integer.toString(newId);
    }

    // *******************************************
    // Json Query Utilities
    private PostSubtype rs2Post(ResultSet rs) throws SQLException {
        // Author of the post
        ResultSet userRs = db.selectUserById(rs.getInt("user_id"));
        userRs.next();
        UserSubtype user = rs2User(userRs);
        userRs.close();

        // Attached file of the post
        FileInfoSubtype fileInfo = rs2FileInfo(rs);

        // Attached links of the post
        ArrayList<String> links = new ArrayList<>();
        String linksStr = rs.getString("links");
        if (linksStr != null)
            links.addAll(Arrays.asList(linksStr.split("\\s")));

        // Comments under the post
        ArrayList<CommentSubtype> comments = new ArrayList<>();
        ResultSet commentsRs = db.selectCommentsByPostId(
                rs.getInt("post_id"));
        while (commentsRs.next())
            comments.add(rs2Comment(commentsRs));
        commentsRs.close();

        return new PostSubtype(
                rs.getInt("post_id"),
                rs.getString("title"),
                rs.getString("content"),
                user,
                rs.getString("date"),
                rs.getInt("vote_up"),
                rs.getInt("vote_down"),
                rs.getBoolean("pinned"),
                fileInfo,
                links,
                comments
        );
    }

    private CommentSubtype rs2Comment(ResultSet rs) throws SQLException {
        String linksStr = rs.getString("links");
        ArrayList<String> links = new ArrayList<>();
        if (linksStr != null)
            links.addAll(Arrays.asList(linksStr.split("\\s")));
        ResultSet userRs = db.selectUserById(rs.getInt("user_id"));
        userRs.next();
        return new CommentSubtype(
                rs.getInt("comment_id"),
                rs.getInt("post_id"),
                rs.getString("content"),
                rs2User(userRs),
                rs.getString("date"),
                rs2FileInfo(rs),
                links
        );
    }

    private FileInfoSubtype rs2FileInfo(ResultSet rs) throws SQLException {
        return new FileInfoSubtype(
                rs.getString("filetype"),
                rs.getString("filedate"),
                fromFullPath(rs.getString("filepath"))
        );
    }

    private UserSubtype rs2User(ResultSet rs) throws SQLException {
        return new UserSubtype(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("first_name"),
                rs.getString("last_name")
        );
    }
}
