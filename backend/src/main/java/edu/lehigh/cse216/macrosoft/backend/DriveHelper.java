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
        return false;
    }

    /**
     * Get a file from Google Drive.
     * @param fullpath Unique identifier of the file.
     * @return Base64 encoded file data, or {@code null} if it does not exist.
     */
    String getFile(String fullpath) {
        return null;
    }

    /**
     * Remove a directory from the cache. Do nothing if it does not exist.
     * @param dir Unique identifier of the directory.
     */
    void removeDir(String dir) {

    }
}
