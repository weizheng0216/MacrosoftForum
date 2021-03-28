package edu.lehigh.cse216.macrosoft.backend;

/**
 * UserRequest provides a format for clients to present data
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class UserRequest {
    public int mUserId;
    public String mFirstName;
    public String mLastName;
    public String sessionKey;
    public String mAccessToken;
    public String mEmail;
    public String mDate;

}