package edu.lehigh.cse216.macrosoft.backend;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * The BUZZ DriveHelper is a wrapper around the Google Drive service. The
 * Google drive provide the ultimate storage solution for the backend to
 * hold files uploaded by users. If the drive is full, backend may not
 * delete the files arbitrarily.
 */
class DriveHelper {
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";
    private static final String APPLICATION_NAME = "BUZZ BACKEND STORAGE";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final Drive service;
    /**
     * Initialize with configuration variables.
     */
    DriveHelper() throws Exception {
        NetHttpTransport HTTP_TRANSPORT;
        HttpRequestInitializer requestInitializer;

        // Build a new authorized API client service.
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        // InputStream in = DriveHelper.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        InputStream in = new ByteArrayInputStream(C.getBytes());
        requestInitializer = new HttpCredentialsAdapter(
                ServiceAccountCredentials.fromStream(in).createScoped(SCOPES));

        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                           .setApplicationName(APPLICATION_NAME)
                           .build();
    }

    private static final String C = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"cse216-macrosoft-buzz-307714\",\n" +
            "  \"private_key_id\": \"9a50d8b23e2137e9f7ca6e2050aeb5a9aced8651\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC2aH4GeurC9hyI\\n3DDRsU8eus9jA41gkwgp96+zo3dK5a+savNsfG8E/0OvQWQcOVH08KOxzM/DSLPe\\nWorgl8Nmht88xHjun9kdQd+zFnW3bQ4zQXIgB8p2ECDMr6bp83rLOqg1L7r8CU8u\\nzjFmTacQJ5wSURd8XqIlKJ3yfrZoE738JRLmTkGtLEm9+N4CqzEe/t97eIEAYJN2\\n2RxY/Fbt7PC/kk/CR490R9cFgBijq84qBprMogpT89L3RY5gINcVFUmrETuEUZMr\\nQl3CSJ/5R7Y3cyQSZsxCeRj3lOk4fQRnNrK7PCicPR0bc1HDV+kv/WJaV+xp93NP\\nQSgauCddAgMBAAECggEAJWNLmZZuMiHx/Vdspj56oijWQ0xCGS5p+PgsylxSIkbr\\n+CI/3NewhcywmeaEV4ikNNlyJAp/914aiBP07U4OHiC89x6qTvbysJ2E9Zc/76Xp\\nyDW857XRGNv7bRj/1Go0nB1cxvIOmIRr94MJ+xcljBkiU1rNvo9DGuy1WzP/vVsd\\nfJvQTXP13C4jLfROvoNsqOkWByhEMMoC++Gve9ymd6ee7QKd99aqCMMRbP/AsgEi\\nJFEnUgDK18VZOFfm0PNocNv48oFgphMoAGOfo9mDvSvYGOXeeYoUpCkmau+bSKtL\\nLxcWcVoFGDnwskSkLajNoIycgqattzvNgQCbrjL9YQKBgQDwIdRwtmrlKWUmFo23\\n1Z94CcqHo35VBLtc8+XBrm/1qlaEJCQJ+X9CSnuYqJ4WQRzvkhA6L+fMWv/BnmaL\\n9fEpIp5Ebuh+dtfkvxZUWl9sGnRvy6c9yu6AlskQ3xUQwE9WbEFWQocRKhDPpojL\\ngR26bnBtbK5EyiprIizlyFxgpQKBgQDCdi5QBmAzjrKqJC3T446C+NoH1eATTWrc\\n8W75Nux2OLQ8/UfpAOUUqyiS5pu7HUmELRAhjD2W8QCz/XgE+Kf7d9c+5N6QRyDL\\npXw2Fjo0fHotCpWnl1RZgHgWMldJdNUVeXnvNMvp/TJ6ivpysy6DkxCRKDkTKuk9\\n+rBYrgz2WQKBgEGq3q+cEwtsEI8j58l4D84Bz3LcUPXq/1niENiwXUmvNU2ZnIll\\nw1lbiQUsR18Km2EgbnObGiaWVA6cxKpNrY90Ohe4xLegxFV3tCGJPUMQ0PqplKif\\nUC22DnIgr2APVyLpI8z1EH6vJD3E6u/L9VdAUFp8OAPzzTnS9Lg53JlJAoGAZPuc\\nJ0B4Wq3CyCoLhlpIWmlCSAlf2IY+dUgHOfG0r9vYdUeOPUiSrb4ITujkfDHESJhi\\nJRm/rg+vLsj0t5gtG6BrLaUKkt/s/vQL4cZ/KVxu0c8KUkJ6shPNyLERtPqv4Pv5\\nPygqsHMk8JAVaNT11avcUZUStop1HxN82kBgSNkCgYBb+n7Yobfisjol7Bq1zuvR\\nHk9yjUjF2qzixkNYDZSUeoH5P9V1Z5sDPCgSZTJDuXxziBIb52QESmcVwkIlpIQV\\n/YK3mEs7oR5Lhi1bTvBY+g67nSQpC8TYF4aU2PBnwEGDWMtklyTRJEkKDkQrctOY\\nqOy930ek9VMn1bIsYMuKPg==\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"test2-158@cse216-macrosoft-buzz-307714.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"102252142475224423287\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/test2-158%40cse216-macrosoft-buzz-307714.iam.gserviceaccount.com\"\n" +
            "}\n";

    /**
     * Save a file to Google Drive.
     * @param fullpath Unique identifier of the file.
     * @param str64 Base64 encoded file data.
     * @return Whether the file is saved successfully.
     */
    boolean saveFile(String fullpath, String str64) {
        try {
            // decode the file
            byte[] bytes = Base64.getDecoder().decode(str64);
            FileOutputStream out = new FileOutputStream("decode");
            out.write(bytes);
            out.close();
            // upload to drive
            File fileMetadata = new File();
            fileMetadata.setName(fullpath);
            java.io.File filePath = new java.io.File("decode");
            FileContent mediaContent = new FileContent("image/png", filePath);
            service.files().create(fileMetadata, mediaContent).setFields("id").execute();
            return true;
        } catch (IOException exp) {
            return false;
        }
    }

    /**
     * Get a file from Google Drive.
     * @param fullpath Unique identifier of the file.
     * @return Base64 encoded file data, or {@code null} if it does not exist.
     */
    String getFile(String fullpath) {
        try {
            FileList result = service.files().list()
                    .setPageSize(20)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> files = result.getFiles();
            if (files == null || files.isEmpty()) {
                return null;
            } else {
                for (File file : files) {
                    String name = file.getName();
                    String id = file.getId();
                    System.out.printf("%s (%s)\n", name, id);
                    if (name.equals(fullpath)) {
                        FileOutputStream out = new FileOutputStream("down");
                        service.files().get(id).executeMediaAndDownloadTo(out);
                        out.close();
                        byte[] bytes = Files.readAllBytes(Paths.get("down"));
                        return new String(Base64.getEncoder().encode(bytes));
                    }
                }
            }
        } catch (IOException exp) {
            return null;
        }
        return null;
    }

    /**
     * Remove a file from the cache. Do nothing if it does not exist.
     * @param fullpath Unique identifier of the file.
     */
    void removeFile(String fullpath) {

    }

    /**
     * Get the fullpath of a file under a post when it's stored in Google drive.
     */
    static String toFullPath(String fileName, String postId) {
        if (fileName == null || fileName.length() == 0)
            return "";
        return String.format("%s/%s", postId, fileName);
    }

    /**
     * Get the fullpath of a file under a comment when it's stored in Google drive.
     */
    static String toFullPath(String fileName,
                             String postId, String commentId) {
        if (fileName == null || fileName.length() == 0)
            return "";
        return String.format("%s/%s/%s", postId, commentId, fileName);
    }

    /**
     * Get the filename from the fullpath.
     */
    static String fromFullPath(String fullpath) {
        if (fullpath == null || fullpath.length() == 0)
            return "";
        int last = fullpath.lastIndexOf("/");
        return fullpath.substring(last + 1);
    }
}
