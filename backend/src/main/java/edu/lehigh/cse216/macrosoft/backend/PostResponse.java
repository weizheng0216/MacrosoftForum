package edu.lehigh.cse216.macrosoft.backend;

import java.util.ArrayList;

/**
 * Post Response holds a row of information.  A row of information consists of
 * an identifier and other attributes.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public.  That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */
public class PostResponse{

    public int mPostID;

    public String mTitle;

    public String mContent;

    public String mUserLastName;

    public String mUserFirstName;

    //Note: using String instead of Data to store date
    public String mDate;

    public int mUpVote;

    public int mDownVote;

    public boolean mPinned;

    public ArrayList<CommentResponse> mComments;

    public boolean mUserUpVote;

    public boolean mUserDownVote;

    public int mUserID;

    public String mUserEmail;



    PostResponse(int id, String title, String content, int userId, String date, int upVote, int downVote, boolean pinned){
        mPostID=id;
        mTitle=title;
        mContent=content;
        mDate=date;
        mUpVote=upVote;
        mDownVote=downVote;
        mPinned = pinned;
    }

    PostResponse(int id, String title, String content, String date, int userId, String firstName, String lastName, String email, 
                int upVote, int downVote, boolean pinned, boolean userUp, boolean userDown, ArrayList<CommentResponse> comments){
        mPostID=id;
        mTitle=title;
        mContent=content;
        mDate=date;
        mUserID = userId;
        mUserLastName = lastName;
        mUserFirstName = firstName;
        mUserEmail = email;
        mUpVote=upVote;
        mDownVote=downVote;
        mPinned = pinned;
        mUserUpVote = userUp;
        mUserDownVote = userDown;
        mComments = (ArrayList<CommentResponse>)comments.clone();
        
    }
}