package edu.lehigh.cse216.macrosoft.backend;

/**
 * CommentRequest provides a format for clients to present data to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class CommentRequest {
    public int mCommentId;
    public String mDate;
    public int mUserId;
    public int mPostId;
    public String mContent;
}