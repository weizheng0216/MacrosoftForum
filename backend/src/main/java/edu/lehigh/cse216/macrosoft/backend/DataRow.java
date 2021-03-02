package edu.lehigh.cse216.macrosoft.backend;

/**
 * DataRow holds a row of information.  A row of information consists of
 * an identifier and other attributes.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public.  That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */
public class DataRow{
    public final int mID;

    public String mTitle;

    public String mContent;

    public String mUserName;

    //Note: using String instead of Data to store date
    public String mDate;

    public int mUpVote;

    public int mDownVote;

    public boolean mPinned;

    DataRow(int id, String title, String content, String userName, String date, int upVote, int downVote, boolean pinned){
        mID=id;
        mTitle=title;
        mContent=content;
        mUserName=userName;
        mDate=date;
        mUpVote=upVote;
        mDownVote=downVote;
        mPinned = pinned;
    }

    DataRow(DataRow data){
        mID=data.mID;
        mTitle=data.mTitle;
        mContent=data.mContent;
        mUserName=data.mUserName;
        mDate=data.mDate;
        mUpVote=data.mUpVote;
        mDownVote=data.mDownVote;
        mPinned=data.mPinned;
    }
}