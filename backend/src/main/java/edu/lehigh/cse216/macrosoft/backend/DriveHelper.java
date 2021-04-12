package edu.lehigh.cse216.macrosoft.backend;

/**
 * The BUZZ DriveHelper is a wrapper around the Google Drive service. The
 * Google drive provide the ultimate storage solution for the backend to
 * hold files uploaded by users. If the drive is full, backend may not
 * delete the files arbitrarily.
 */
class DriveHelper {
    /**
     * Initialize with configuration variables.
     */
    DriveHelper() {

    }

    /**
     * Save a file to Google Drive.
     * @param fullpath Unique identifier of the file.
     * @param str64 Base64 encoded file data.
     * @return Whether the file is saved successfully.
     */
    boolean saveFile(String fullpath, String str64) {
        return true;
    }

    /**
     * Get a file from Google Drive.
     * @param fullpath Unique identifier of the file.
     * @return Base64 encoded file data, or {@code null} if it does not exist.
     */
    String getFile(String fullpath) {
        return "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
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
