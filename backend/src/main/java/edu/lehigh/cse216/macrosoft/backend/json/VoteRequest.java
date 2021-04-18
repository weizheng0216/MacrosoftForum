package edu.lehigh.cse216.macrosoft.backend.json;

public class VoteRequest implements ValidateFormat {
    public boolean upVote;
    public boolean downVote;

    public boolean validate() {
        return !(upVote && downVote);
    }
}
