package edu.lehigh.cse216.macrosoft.backend;

/**
 * VoteRequest provides a format for clients to present title and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class VoteRequest {
    public int upvote;
    public int downvote;
}