package edu.lehigh.cse216.macrosoft.backend;
import java.util.ArrayList;

/**
 * UserResponse holds a row of information.  A row of information consists of
 * an identifier and other attributes.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public.  That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */

public class UserResponse{
    int mUserID;
    String mFirstName;
    String mLastName;
    String mEmail;
    ArrayList<PostResponse> mPosts;
    ArrayList<CommentResponse> mComments;


    UserResponse(int uId, String firstName, String lastName, String email, ArrayList<PostResponse> posts, ArrayList<CommentResponse> comments ){
        mUserID = uId;
        mFirstName = firstName;
        mLastName = lastName;
        mPosts = posts;
        mComments = comments;
        mEmail = email;
    }

    // UserRow(UserRow data){
    //     mUserID = data.mUserID;
    //     mFirstName = data.mFirstName;
    //     mLastName = data.mLastName;
    //     mSessionKey = data.mSessionKey;
    //     mEmail = data.mEmail;
    // }
}