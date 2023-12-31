package edu.lehigh.cse216.macrosoft.backend.json;

import java.util.ArrayList;

public class PostRequest implements ValidateFormat {
    public String title;
    public String content;
    public ArrayList<String> links;
    public String fileName;
    public String fileType;
    public String fileData;
    public boolean flagged;
    public String videoLink;

    public boolean validate() {
        if (title == null) title = "";
        if (content == null) content = "";
        if (fileName == null) fileName = "";
        if (fileType == null) fileType = "";
        if (fileData == null) fileData = "";
        if (videoLink == null) videoLink = "";
        if (links == null) links = new ArrayList<>();
        for (int i = links.size()-1; i >= 0; i--)
            if (links.get(i) == null) links.remove(i);
        return true;
    }
}
