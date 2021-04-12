package edu.lehigh.cse216.macrosoft.backend;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java.util.Random;

/**
 * The BUZZ AuthHelper helps with authentication issues like Google's OAuth
 * verification, <i>sessionKey</i> generation, login verification and so on.
 */
class AuthHelper {
    private final String CLIENT_ID;

    private final Random random;

    private final CacheHelper cache;

    /**
     * Initialize the authentication helper with Google's CLIENT_ID and
     * a {@code CacheHelper}, which will be used for sessionKey queries.
     * @param CLIENT_ID Google's CLIENT_ID.
     * @param cache Backend cache helper.
     */
    AuthHelper(String CLIENT_ID, CacheHelper cache) {
        this.CLIENT_ID = CLIENT_ID;
        this.random = new Random();
        this.cache = cache;
    }

    /**
     * Search through the cache for <i>sessionKey</i>. If it is not found, the
     * user need to login. Every API endpoint that requires explicit user identity
     * should call this function.
     * @param sessionKey The <i>sessionKey/i> passed from Front-end.
     * @return The user id of the requesting user, or {@code null} if the user
     *         hasn't logged in.
     */
    String verifyLogin(String sessionKey) {
        return null;
    }

    /**
     * Login a user by refreshing her <i>sessionKey</i> entry in the backend cache
     * or by creating a new entry with a newly generated <i>sessionKey</i>. The
     * generated <i>sessionKey</i> is returned.
     * @param userId The user to be logged in.
     * @return Generated <i>sessionKey</i>
     */
    String login(String userId) {
        return null;
    }

    /**
     * Logout a user by clearing the <i>sessionKey</i> entry in the backend cache.
     * @param userId The user to be logged out.
     */
    void logout(String userId) {

    }

    /**
     * Access Google's resource server to retrieve user's personal information
     * using the <i>idToken</i> obtained from the front-end.
     * @param idToken The credential granted by front-end user.
     * @return The payload that holds the user information, or null if verification
     *         failed. That's most likely to be caused by invalid <i>idToken</i>.
     */
    GoogleIdToken.Payload accessResource(String idToken) {
        return null;
    }
}
