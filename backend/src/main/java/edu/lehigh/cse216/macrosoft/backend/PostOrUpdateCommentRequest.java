package edu.lehigh.cse216.macrosoft.backend;
/**
 * PostOrUpdateRequest provides a format for clients to present data to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
class PostOrUpdateCommentRequest{
    int postID;
    String content;
}