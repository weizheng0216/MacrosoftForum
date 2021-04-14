package edu.lehigh.cse216.macrosoft.backend.json;

public class LoginRequest implements ValidateFormat {
    public String idToken;

    public boolean validate() {
        return idToken != null;
    }
}
