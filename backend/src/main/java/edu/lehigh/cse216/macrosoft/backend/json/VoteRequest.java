package edu.lehigh.cse216.macrosoft.backend.json;

public class VoteRequest implements ValidateFormat {
    // use int to prevent json error
    public boolean upVote;
    public boolean downVote;

    public boolean validate() {
        return !(upVote && downVote);
    }
}
