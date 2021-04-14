package edu.lehigh.cse216.macrosoft.admin;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import java.io.IOException;
public class Storage {
    private static void printAbout(Drive service) {
        // try {
        // About about = service.about().get().execute();
        // System.out.println("Current user name: " + about.getName());
        // System.out.println("Root folder ID: " + about.getRootFolderId());
        // System.out.println("Total quota (bytes): " + about.getQuotaBytesTotal());
        // System.out.println("Used quota (bytes): " + about.getQuotaBytesUsed());
        // } catch (IOException e) {
        //     System.out.println("An error occurred: " + e);
        // }
    }
}