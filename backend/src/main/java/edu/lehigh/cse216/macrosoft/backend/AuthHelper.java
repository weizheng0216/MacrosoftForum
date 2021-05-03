package edu.lehigh.cse216.macrosoft.backend;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;
import java.util.Random;

/**
 * The BUZZ AuthHelper helps with authentication issues like Google's OAuth
 * verification, <i>sessionKey</i> generation, login verification and so on.
 */
class AuthHelper {
    private final String CLIENT_ID;

    private final Random random;
    private static DatabaseHelper db;

    private final CacheHelper cache;

    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();

    private static final int SESSION_KEY_LEN = 32;

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
        if (sessionKey == null || sessionKey.length() != SESSION_KEY_LEN)
            return null;
        if (sessionKey.equals("buzztester66-qwertyuioplkjhgfdsa"))
            return "66";  // the test user

        // check if the user is blocked
    //     try{
    //     if(db.checkBlocked(cache.getSession(sessionKey)))
    //     {
    //         //res.status(406);
    //         return null;
    //     }    
    // }catch (Exception exp) {
    //     System.out.println(exp.getMessage());
    // }
        return cache.getSession(sessionKey);
    }

    //isblocked
    // input parameter user id
    // return db.checkedblock
    //
    /**
     * Login a user by refreshing her <i>sessionKey</i> entry in the backend cache
     * or by creating a new entry with a newly generated <i>sessionKey</i>. The
     * generated <i>sessionKey</i> is returned.
     * @param userId The user to be logged in.
     * @return Generated <i>sessionKey</i>
     */
    String login(String userId) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(SESSION_KEY_LEN);
        for (int i = 0; i < SESSION_KEY_LEN; i++) {
            int index = (int)(AlphaNumericString.length() * random.nextDouble());
            sb.append(AlphaNumericString.charAt(index));
        }
        String sk = sb.toString();
        cache.saveSession(sk, userId);
        return sk;
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
     * @param idTokenString The credential granted by front-end user.
     * @return The payload that holds the user information, or null if verification
     *         failed. That's most likely to be caused by invalid <i>idToken</i>.
     */
    GoogleIdToken.Payload accessResource(String idTokenString) {
        if (idTokenString == null || idTokenString.length() == 0) {
            return null;
        }
        try {
            GoogleIdToken idToken;
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    // Specify the CLIENT_ID of the app that accesses the backend:
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    // Or, if multiple clients access the backend:
                    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                    .build();
            idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            }
            return null;
        } catch (Exception exp) {
            return null;
        }
    }
}
