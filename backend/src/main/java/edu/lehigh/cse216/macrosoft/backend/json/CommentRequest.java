package edu.lehigh.cse216.macrosoft.backend.json;

import java.util.ArrayList;

public class CommentRequest implements ValidateFormat {
    public String content;
    public ArrayList<String> links;
    public String fileName;
    public String fileType;
    public String fileData;

    public boolean validate() {
        if (fileName == null) fileName = "";
        if (fileType == null) fileType = "";
        if (fileData == null) fileData = "";
        if (links == null) links = new ArrayList<>();
        for (String s : links) {
            if (s == null) return false;
        }
        return content != null;
    }
}
