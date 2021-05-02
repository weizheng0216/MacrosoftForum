package edu.lehigh.cse216.macrosoft.backend.json;

import java.util.ArrayList;

public class IntegratedPostSubtype {
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
    public String mVideo_link;
    public ArrayList<CommentSubtype> mComments;
    public boolean mUserUpVote;
    public boolean mUserDownVote;

    public IntegratedPostSubtype(int mPostID, String mTitle,
                                 String mContent, UserSubtype mAuthor,
                                 String mDate, int mUpVoteCount,
                                 int mDownVoteCount, boolean mFlagged,
                                 FileInfoSubtype mFileInfo,
                                 ArrayList<String> mLinks,
                                 String mVideo_link,
                                 ArrayList<CommentSubtype> mComments,
                                 boolean mUserUpVote, boolean mUserDownVote) {
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
        this.mVideo_link = mVideo_link;
        this.mComments = mComments;
        this.mUserUpVote = mUserUpVote;
        this.mUserDownVote = mUserDownVote;
    }

    public static IntegratedPostSubtype fromBasicPost(
            PostSubtype base, boolean mUserUpVote, boolean mUserDownVote) {
        return new IntegratedPostSubtype(
                base.mPostID,
                base.mTitle,
                base.mContent,
                base.mAuthor,
                base.mDate,
                base.mUpVoteCount,
                base.mDownVoteCount,
                base.mFlagged,
                base.mFileInfo,
                base.mLinks,
                base.mVideo_link,
                base.mComments,
                mUserUpVote,
                mUserDownVote
        );
    }
}
