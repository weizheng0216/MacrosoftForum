package edu.lehigh.cse216.macrosoft.backend;

/**
 * The BUZZ CacheHelper helps with backend cache management. It's a wrapper
 * around the XMemCache service, which makes the backend server stateless and
 * improves file access time.
 */
class CacheHelper {

    /**
     * Initialize with configuration variables.
     * @param MEMCACHED_SERVERS
     * @param MEMCACHED_USERNAME
     * @param MEMCACHED_PASSWORD
     */
    CacheHelper(String MEMCACHED_SERVERS,
                String MEMCACHED_USERNAME,
                String MEMCACHED_PASSWORD) {

    }

    /**
     * Save a file that's base64 encoded into the cache. If the cache is
     * full, depending on the size of the file, other files in the cache
     * may be overridden.
     * @param fullpath Unique identifier of the file.
     * @param str64 File data that's encoded into ASCII.
     */
    void saveFile(String fullpath, String str64) {

    }

    /**
     * Get a file from the cache. Return {@code null} if it's not found.
     * @param fullpath Unique identifier of the file.
     * @return Base64 encoded file data, or {@code null} if file does not exist.
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
