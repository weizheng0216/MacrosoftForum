package edu.lehigh.cse216.macrosoft.backend.json;

public class VoteRequest implements ValidateFormat {
    // use int to prevent json error
    public int upVote;
    public int downVote;

    public boolean upVote() {
        return this.upVote == 1;
    }

    public boolean downVote() {
        return this.downVote == 1;
    }

    public boolean validate() {
        return !(upVote() && downVote());
    }
}
