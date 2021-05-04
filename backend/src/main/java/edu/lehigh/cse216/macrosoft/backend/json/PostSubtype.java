package edu.lehigh.cse216.macrosoft.backend.json;

import java.util.ArrayList;

public class PostSubtype {
    public int mPostID;
    public String mTitle;
    public String mContent;
    public UserSubtype mAuthor;
    public String mDate;
    public int mUpVoteCount;
    public int mDownVoteCount;
    public boolean mFlagged;
    public FileInfoSubtype mFileInfo;
    public ArrayList<String> mLinks;
    public String mVideoLink;
    public ArrayList<CommentSubtype> mComments;

    public PostSubtype(int mPostID, String mTitle, String mContent,
                       UserSubtype mAuthor, String mDate, int mUpVoteCount,
                       int mDownVoteCount, boolean mFlagged,
                       FileInfoSubtype mFileInfo, ArrayList<String> mLinks,
                       String mVideoLink,
                       ArrayList<CommentSubtype> mComments) {
        this.mPostID = mPostID;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mAuthor = mAuthor;
        this.mDate = mDate;
        this.mUpVoteCount = mUpVoteCount;
        this.mDownVoteCount = mDownVoteCount;
        this.mFlagged = mFlagged;
        this.mFileInfo = mFileInfo;
        this.mLinks = mLinks;
        this.mVideoLink = mVideoLink;
        this.mComments = mComments;
    }
}
