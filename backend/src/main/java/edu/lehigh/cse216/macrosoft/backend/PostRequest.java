package edu.lehigh.cse216.macrosoft.backend;

/**
 * PostRequest provides a format for clients to present title and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class PostRequest {
    public int mUserId;
    public String mTitle;
    public String mContent;
    public boolean mPinned;
    public int mUpVote;
    public int mDownVote;
    public int mPostId;
    public String mDate;

}