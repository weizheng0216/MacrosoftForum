package edu.lehigh.cse216.macrosoft.backend;

/**
 * SessionKeyRequest provides a format for clients to present sessionKey
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class SessionKeyRequest {
    public String sessionKey;

}