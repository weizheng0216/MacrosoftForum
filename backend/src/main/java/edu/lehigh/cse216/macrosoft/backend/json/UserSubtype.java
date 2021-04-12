package edu.lehigh.cse216.macrosoft.backend.json;

public class UserSubtype {
    public int mUserID;
    public String mEmail;
    public String mFirstName;
    public String mLastName;

    public UserSubtype(int mUserID, String mEmail,
                       String mFirstName, String mLastName) {
        this.mUserID = mUserID;
        this.mEmail = mEmail;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
    }
}
