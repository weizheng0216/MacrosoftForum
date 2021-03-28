package edu.lehigh.cse216.macrosoft.backend;

import java.sql.DataTruncation;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ResponseTest extends TestCase{

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ResponseTest(String testName){
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ResponseTest.class);
    }

    public void testMyself() {
        assertTrue(true);
    }

    /**
     * Ensure that the constructor populates every field of the object it
     * creates
     */

    // UserResponse(int uId, String firstName, String lastName, String email, ArrayList<PostResponse> posts, ArrayList<CommentResponse> comments ){
    // public void testUserConstructor() {
       
        // posts.add(new PostResponse(int id, String title, String content, String date, int userId, String firstName, String lastName, String email, int upVote, int downVote, boolean pinned, ArrayList<CommentResponse> comments){

    //     // (int id, String title, String content, String userName, String date, int upVote, int downVote, boolean pinned){
    //     PostRow d = new PostRow(id,title,content,userId,date,upVote,downVote,pinned);
    //     assertTrue(d.title.equals(title));
    //     assertTrue(d.content.equals(content));
    //     assertTrue(d.date.equals(date));
    //     assertTrue(d.postId == id);
    //     assertTrue(d.upVote == upVote);
    //     assertTrue(d.downVote == downVote);
    //     assertTrue(d.pinned == pinned);
    // }

    public void testConstructor() {

        int id = 3;
        String title = "title";
        String pContent = "Test Post";
        String cContent = "Test Comment";
        String firstName = "First";
        String lastName = "Last";
        String date = "03/27/2021";
        int up = 100;
        int down = 9;
        boolean pinned = false;
        boolean userUp = true;
        boolean userDown = false;
        String email = "wez223@lehigh.edu";

        CommentResponse comment = new CommentResponse(id, id, cContent, id, firstName, lastName, date);
        
        assertTrue(comment.mCommentID == id);
        assertTrue(comment.mPostID == id);
        assertTrue(comment.mContent.equals(cContent));
        assertTrue(comment.mUserID == id);
        assertTrue(comment.mFirstName.equals(firstName));
        assertTrue(comment.mLastName.equals(lastName));
        assertTrue(comment.mDate.equals(date));
        ArrayList<CommentResponse> comments = new ArrayList<CommentResponse>();
        comments.add(comment);

        PostResponse post = new PostResponse(id, title, pContent, date, id, firstName, lastName, email, up, down, pinned, userUp, userDown, comments);

        assertTrue(post.mPostID == id);
        assertTrue(post.mTitle.equals(title));
        assertTrue(post.mContent.equals(pContent));
        assertTrue(post.mUserID == id);
        assertTrue(post.mUserFirstName.equals(firstName));
        assertTrue(post.mUserLastName.equals(lastName));
        assertTrue(post.mUserEmail.equals(email));
        assertTrue(post.mUpVote == up);
        assertTrue(post.mDownVote == down);
        assertTrue(post.mUserUpVote == userUp);
        assertTrue(post.mUserDownVote == userDown);
        assertTrue(post.mPinned == pinned);

        ArrayList<PostResponse> posts = new ArrayList<PostResponse>();
        posts.add(post);

        UserResponse user = new UserResponse(id, firstName, lastName, email, posts, comments);

        assertTrue(user.mUserID == id);
        assertTrue(user.mLastName.equals(lastName));
        assertTrue(user.mFirstName.equals(firstName));
        assertTrue(user.mEmail == email);
        assertTrue(user.mPosts == posts);
        assertTrue(user.mComments == comments);

    }
    
}