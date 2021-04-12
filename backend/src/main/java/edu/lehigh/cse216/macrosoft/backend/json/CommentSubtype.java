package edu.lehigh.cse216.macrosoft.backend.json;

import java.util.ArrayList;

public class CommentSubtype {
    public int mCommentID;
    public int mPostID;
    public String mContent;
    public String mDate;
    public FileInfoSubtype mFileInfo;
    public ArrayList<String> mLinks;

    public CommentSubtype(int mCommentID, int mPostID, String mContent,
                          String mDate, FileInfoSubtype mFileInfo,
                          ArrayList<String> mLinks) {
        this.mCommentID = mCommentID;
        this.mPostID = mPostID;
        this.mContent = mContent;
        this.mDate = mDate;
        this.mFileInfo = mFileInfo;
        this.mLinks = mLinks;
    }
}
