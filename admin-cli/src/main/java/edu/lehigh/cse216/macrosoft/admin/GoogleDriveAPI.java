package edu.lehigh.cse216.macrosoft.admin;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;

import com.google.api.client.auth.oauth2.Credential;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import com.google.auth.http.*;
import com.google.auth.oauth2.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

public class GoogleDriveAPI 
{
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */

    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/cse216-macrosoft-buzz-307714-9a50d8b23e21.json";

    private static NetHttpTransport HTTP_TRANSPORT = null;
    private static HttpRequestInitializer requestInitializer = null;
    private static Drive service = null;
    private static File fileMetadata = null;
    private static File file = null;
    private static Boolean init = false;

    private static void init(){

        try {
            // Build a new authorized API client service.
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (java.security.GeneralSecurityException eSecurity) {
            System.out.println("HTTP_TRANSPORT Security Execption" + eSecurity);
        } catch (java.io.IOException eIO) {
            System.out.println("HTTP_TRANSPORT IO Execption" + eIO);
        }

        try {
            InputStream in = GoogleDriveAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            
            requestInitializer = new HttpCredentialsAdapter(ServiceAccountCredentials.fromStream(in)
                .createScoped(SCOPES));

        } catch (java.io.IOException eIO) {
            System.out.println("requestInitializer exception" + eIO);
        }
        //System.out.println("requestInitializer OK");

        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                    .setApplicationName(APPLICATION_NAME)
                    .build();  
        System.out.println("initializing connection ...");
        init = true;
    }

    public static Double printAbout(){
        if(!init)
            init();
        try{
            com.google.api.services.drive.model.About result = service.about().get().setFields("user, storageQuota").execute();
            String s = String.valueOf(result);
            String[] parseResult = s.split("\"", 0);
            Double total = Double.valueOf(parseResult[5]);
            Double use = Double.valueOf(parseResult[9]);
            Double precentage = use/total;
           
            System.out.printf("\n\tusage rate:%.2f\n", precentage);
            return precentage;
        }catch (java.io.IOException eIO) {
            System.out.println("Google Drive File Error" + eIO);
            return -1.0;
        }
    }
    public static void findFile(String fileName){
        if(!init)
            init();
        System.out.println("Searching "+ fileName+" from drive");
        try {
            FileList result = service.files().list()
                    .setQ("name='"+fileName+"'")
                    .setPageSize(20)    
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> files = result.getFiles();
            if (files == null || files.isEmpty()) {
                System.out.println("\tNo files found.");
            } else {
                System.out.println("Files:");
                for (File eachFile : files) {
                    System.out.printf("\t%s (%s)\n", eachFile.getName(), eachFile.getId());
                }
            }
        } catch (java.io.IOException eIO) {
            System.out.println("Google Drive File Error" + eIO);
        }
    }

    public static void removeFile(String fileName){
        if(!init)
            init();
        //File target;
        try {
            System.out.println("Deleting "+ fileName+" from drive");
            FileList result = service.files().list()
                    .setQ("name='"+fileName+"'")
                    .setPageSize(20)    
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> files = result.getFiles();
            // if (files == null || files.isEmpty()) {
            //     // System.out.println("No files found.");
            // } else {
            //     System.out.println("Files:");
            //     for (File eachFile : files) {
            //         System.out.printf("%s (%s)\n", eachFile.getName(), eachFile.getId());
            //     }
            // }

            try {
                    
                for (File eachFileDelete : files) {
                   service.files().delete(eachFileDelete.getId()).execute();
                }
                    
            } catch(IllegalStateException e) {
                System.out.println("System.in was closed; exiting");
            }
        } catch (java.io.IOException eIO) {
            System.out.println("Google Drive File Error" + eIO);
        }
    }
        
}