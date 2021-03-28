package edu.lehigh.cse216.macrosoft.backend;

public class CommentResponse{

    public int mCommentID;
    public int mPostID;
    public String mContent;
    public int mUserID;
    public String mFirstName; 
    public String mLastName;
    public String mDate;

    CommentResponse(int cId, int pId, String content, int userID, String FirstName, String LastName, String date){
        mCommentID=cId;
        mPostID=pId;
        mContent=content;
        mUserID=userID;
        mDate=date;
        mLastName = LastName;
        mFirstName = FirstName;
    }

    CommentResponse(int cId, int pId, String content, int userID, String date){
        mCommentID=cId;
        mPostID=pId;
        mContent=content;
        mUserID=userID;
        mDate=date;
    }

}