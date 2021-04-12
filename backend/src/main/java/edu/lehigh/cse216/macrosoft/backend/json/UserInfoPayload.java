package edu.lehigh.cse216.macrosoft.backend.json;

import java.util.ArrayList;

public class UserInfoPayload {
    public UserSubtype mUser;
    public ArrayList<PostSubtype> mPosts;
    public ArrayList<CommentSubtype> mComments;

    public UserInfoPayload(UserSubtype mUser,
                           ArrayList<PostSubtype> mPosts,
                           ArrayList<CommentSubtype> mComments) {
        this.mUser = mUser;
        this.mPosts = mPosts;
        this.mComments = mComments;
    }
}
