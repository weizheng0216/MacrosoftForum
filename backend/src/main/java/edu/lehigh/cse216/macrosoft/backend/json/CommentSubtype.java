package edu.lehigh.cse216.macrosoft.backend.json;

import java.util.ArrayList;

public class CommentSubtype {
    public int mCommentID;
    public int mPostID;
    public String mContent;
    public UserSubtype mAuthor;
    public String mDate;
    public FileInfoSubtype mFileInfo;
    public ArrayList<String> mLinks;
    public boolean mFlagged;

    public CommentSubtype(int mCommentID, int mPostID, String mContent,
                          UserSubtype mAuthor, String mDate,
                          FileInfoSubtype mFileInfo, ArrayList<String> mLinks, boolean Flagged) {
        this.mCommentID = mCommentID;
        this.mPostID = mPostID;
        this.mContent = mContent;
        this.mAuthor = mAuthor;
        this.mDate = mDate;
        this.mFileInfo = mFileInfo;
        this.mLinks = mLinks;
        this.mFlagged = mFlagged;
    }
}
