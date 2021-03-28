package edu.lehigh.cse216.macrosoft.backend;
/**
 * IdTokenRequest provides a format for clients to present idToken 
 * string to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class IdTokenRequest {
    public String idToken;
}