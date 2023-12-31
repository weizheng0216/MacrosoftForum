package edu.lehigh.cse216.macrosoft.backend;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * The BUZZ CacheHelper helps with backend cache management. It's a wrapper
 * around the XMemCache service, which makes the backend server stateless and
 * improves file access time.
 */
class CacheHelper {

    private final MemcachedClient client;

    private static final int FILE_EXPIRE = 18000; //ms

    private static final int SESSION_EXPIRE = 3600; //ms

    /**
     * Initialize with configuration variables.
     */
    CacheHelper(String MEMCACHED_SERVERS,
                String MEMCACHED_USERNAME,
                String MEMCACHED_PASSWORD) throws IOException {
        List<InetSocketAddress> servers = AddrUtil.getAddresses(MEMCACHED_SERVERS.replace(",", " "));
        AuthInfo authInfo =
                AuthInfo.plain(MEMCACHED_USERNAME, MEMCACHED_PASSWORD);
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);
        // Configure SASL auth for each server
        for(InetSocketAddress server : servers) {
            builder.addAuthInfo(server, authInfo);
        }
        // Use binary protocol
        builder.setCommandFactory(new BinaryCommandFactory());
        // Connection timeout in milliseconds (default: 1000)
        builder.setConnectTimeout(1000);
        // Reconnect to servers (default: true)
        builder.setEnableHealSession(true);
        // Delay until reconnect attempt in milliseconds (default: 2000)
        builder.setHealSessionInterval(2000);

        client = builder.build();
    }

    /**
     * Save a file that's base64 encoded into the cache. If the cache is
     * full, depending on the size of the file, other files in the cache
     * may be overridden.
     * @param fullpath Unique identifier of the file.
     * @param str64 File data that's encoded into ASCII.
     */
    void saveFile(String fullpath, String str64) {
        try {
            client.set("f" + fullpath, FILE_EXPIRE, str64);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Get a file from the cache. Return {@code null} if it's not found.
     * @param fullpath Unique identifier of the file.
     * @return Base64 encoded file data, or {@code null} if file does not exist.
     */
    String getFile(String fullpath) {
        try {
            return client.getAndTouch("f" + fullpath, FILE_EXPIRE);
        } catch (Exception exp) {
            return null;
        }
    }

    /**
     * Remove a file from the cache. Do nothing if it does not exist.
     * @param fullpath Unique identifier of the file.
     */
    void removeFile(String fullpath) {
        try {
            client.delete("f" + fullpath);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Saves a user's session, which is a key-value pair of sessionKey and userId.
     * @param sessionKey The sessionKey of target session.
     * @param userId The authorized user of the session.
     */
    void saveSession(String sessionKey, String userId) {
        try {
            client.set("k" + sessionKey, SESSION_EXPIRE, userId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Recover a user's session with the sessionKey.
     * @param sessionKey Credential.
     * @return The userId of user, or {@code null} if key is invalid.
     */
    String getSession(String sessionKey) {
        try {
            return client.getAndTouch("k" + sessionKey, SESSION_EXPIRE);
        } catch (Exception exp) {
            return null;
        }
    }
}
