package edu.lehigh.cse216.macrosoft.backend;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import edu.lehigh.cse216.macrosoft.backend.json.StructuredResponse;
import spark.Spark;

// Import Google's JSON library
import com.google.gson.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.*;
import java.sql.SQLException;
import java.net.URISyntaxException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import java.sql.ResultSet;


public class OldApp {
//
//    // // private static final HttpTransport transport = new NetHttpTransport();
//    private static final JsonFactory jsonFactory = new JacksonFactory();
//
//    // function to generate a random string of length n
//    static String getSessionKey() {
//
//        // chose a Character random from this String
//        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//                                    + "0123456789"
//                                    + "abcdefghijklmnopqrstuvxyz";
//
//        // create StringBuffer size of AlphaNumericString
//        StringBuilder sb = new StringBuilder(32);
//
//        for (int i = 0; i < 32; i++) {
//
//            // generate a random number with 32 in length
//            int index = (int)(AlphaNumericString.length() * Math.random());
//
//            // add Character one by one in end of sb
//            sb.append(AlphaNumericString.charAt(index));
//        }
//
//        return sb.toString();
//    }
//
//    // function to get the key of given value in the hashmap
//    static String getKeyFromValue(HashMap<String, String> map, String value) {
//        String[] ret = {""};
//        // go through the hashmap, check if the key matches the value
//        map.forEach((k,v) -> {
//            if (v.equals(value)){
//                ret[0] = k;
//            }
//        });
//        return ret[0];
//    }
//
//    // function to get userId from sessionKey
//    static int getUserIdFromSessionKey(HashMap<String, String> hashmap, Database db , String sessionKey) throws SQLException{
//
//        // session key - email - userID
//        // check if the key exist in the map
//        if(!hashmap.containsValue(sessionKey)){
//            return -1;
//        }
//
//        // find the email corresponding to the hashmap
//        String email = getKeyFromValue(hashmap, sessionKey);
//        int userId = -1;
//
//        // using database to find the user using the email
//        ResultSet userRs = db.selectUserByEmail(email);
//        while (userRs.next()) {
//            userId = userRs.getInt("user_id");
//        }
//
//        return userId;
//    }
//
//
//    static int getIntFromEnv(String envar, int defaultVal) {
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        if (processBuilder.environment().get(envar) != null) {
//            return Integer.parseInt(processBuilder.environment().get(envar));
//        }
//        return defaultVal;
//    }
//
//    public static void minor( String[] args ){
//        HashMap <String,String> hashmap = new HashMap<String,String>();
//        Spark.port(getIntFromEnv("PORT", 4567));
//        Map<String, String> env = System.getenv();
//        String db_url = env.get("DATABASE_URL");
//        String CLIENT_ID = "41111326106-hcpq125i4f8c658t16g6euk7f0gi6gkr.apps.googleusercontent.com";
//        Database db;
//
//        final Gson gson = new Gson();
//        try {
//            // create table
//            db = Database.getInstance(db_url);
//            if (db == null)
//                return;
//        } catch (SQLException e) {
//            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
//            e.printStackTrace();
//            return;
//        } catch (ClassNotFoundException cnfe) {
//            System.out.println("Unable to find postgresql driver");
//            return;
//        } catch (URISyntaxException s) {
//            System.out.println("URI Syntax Error");
//            return;
//        }
//
//        String static_location_override = System.getenv("STATIC_LOCATION");
//        if (static_location_override == null) {
//            Spark.staticFileLocation("/web");
//        } else {
//            Spark.staticFiles.externalLocation(static_location_override);
//        }
//
//        /**
//         * this function will send the index.html to front-end when a user connects.
//         */
//        Spark.get("/", (req, res) -> {
//            res.redirect("/index.html");
//            return "";
//        });
//
//
//        /**
//         * GET request: using session key provided by fronted to display all posts
//         */
//        Spark.get("api/posts/:sessionKey", (request, response) -> {
//
//            // SessionKeyRequest req = gson.fromJson(request.body(), SessionKeyRequest.class);
//
//
//            String sessionKey = request.params("sessionKey");
//            // use the given SK to find the email associate with that SK
//            String email = getKeyFromValue(hashmap, sessionKey);
//
//
//            // System.out.println("!!!!!!!!!! Session Key is "+sessionKey);
//            // String email = req.mEmail;
//
//            // if the SK is invalid, return 401 (unauthorized)
//            if(!hashmap.containsValue(sessionKey)){
//                response.status(401);
//                response.type("application/json");
//                return gson.toJson(new StructuredResponse("Error", "Authentication Failed", null));
//            }
//
//            // create a PR arraylist
//            ArrayList<PostResponse> posts = new ArrayList<PostResponse>();
//            // select all the posts and the author info
//            ResultSet postRs = db.selectAllPosts();
//
//            while (postRs.next()) {
//                // create CR arraylist
//                ArrayList<CommentResponse> comments = new ArrayList<CommentResponse>();
//                // select all comments associate with that post using postid
//                ResultSet commentRs = db.selectCommentsByPostId(postRs.getInt("post_id"));
//                while (commentRs.next()) {
//                    ResultSet commentUserRs = db.selectUserById(commentRs.getInt("user_id"));
//                    String commentUserFirstName =""; String commentUserLastName ="";
//                    while(commentUserRs.next()){
//                        commentUserFirstName = commentUserRs.getString("first_name");
//                        commentUserLastName = commentUserRs.getString("last_name");
//                    }
//                    // add info into the comment array
//                    comments.add(new CommentResponse(commentRs.getInt("comment_id"),
//                        commentRs.getInt("post_id"), commentRs.getString("content"), commentRs.getInt("user_id"), commentUserFirstName, commentUserLastName,
//                        commentRs.getString("date")));
//                }
//                commentRs.close();
//
//                int userId = 0;
//                boolean userUp = false;
//                boolean userDown = false;
//
//                // get the user vote status using their ids
//                ResultSet userRs = db.selectUserByEmail(email);
//                while(userRs.next()){
//                    userId = userRs.getInt("user_id");
//                    ResultSet voteRs = db.selectVoteByIds(userId, postRs.getInt("post_id"));
//                    // store info about if they up or downvote the post
//                    while(voteRs.next()){
//                        userUp = voteRs.getBoolean("vote_up");
//                        userDown = voteRs.getBoolean("vote_down");
//                    }
//                    voteRs.close();
//                }
//                userRs.close();
//
//                // System.out.println("!!!!!!!!!!!!!"+ postRs.getString("first_name"));
//                // System.out.println("!!!!!!!!!!!!!"+ postRs.getString("last_name"));
//                // System.out.println("!!!!!!!!!!!!!"+ postRs.getString("email"));
//
//                // add all info into posts using the PR constructor
//                posts.add(new PostResponse(postRs.getInt("post_id"),
//                    postRs.getString("title"), postRs.getString("content"),
//                    postRs.getString("date"), postRs.getInt("user_id"), postRs.getString("first_name"), postRs.getString("last_name"),
//                    postRs.getString("email"), postRs.getInt("vote_up"), postRs.getInt("vote_down"), postRs.getBoolean("pinned"),
//                    userUp, userDown, comments));
//
//            }
//            postRs.close();
//
//            response.status(200);
//            response.type("application/json");
//            return gson.toJson(new StructuredResponse("ok", null, posts));
//
//        });
//
//        /**
//         * GET request: usinf sessionkey to get info of the current user (my profile)
//         */
//        // api/users/my -> getting session key and find the user and its info
//        Spark.get("api/users/my/:sessionKey", (request, response) -> {
//
//            String sessionKey = request.params("sessionKey");
//            // System.out.println("from user/my: Session Key: "+sessionKey);
//
//            if(!hashmap.containsValue(sessionKey)){
//                response.status(401);
//                response.type("application/json");
//                return gson.toJson(new StructuredResponse("Error", "Authentication Failed", null));
//            }
//
//            // first need get the email associate with the SK
//            String email = getKeyFromValue(hashmap, sessionKey);
//            // init vars for later usage
//            int userId = -1;
//            String userFirstName = "";
//            String userLastName = "";
//            String userEmail = "";
//
//            // using db function to select current user info using its email
//            ResultSet userRs = db.selectUserByEmail(email);
//            while (userRs.next()) {
//                System.out.println("from user/my, user found");
//                userId = userRs.getInt("user_id");
//                userFirstName = userRs.getString("first_name");
//                userLastName = userRs.getString("last_name");
//                userEmail = userRs.getString("email");
//            }
//
//
//            ResultSet postRs = db.selectPostByUserId(userId);
//            ResultSet commentRs = db.selectCommentsByUserId(userId);
//
//            // two Arraylist use to store the post and the comment the user posted
//            ArrayList<PostResponse> posts = new ArrayList<PostResponse>();
//            ArrayList<CommentResponse> comments = new ArrayList<CommentResponse>();
//
//            //get all posts
//            while (postRs.next()) {
//                System.out.println("from user/my, user have post");
//                posts.add(new PostResponse(postRs.getInt("post_id"), postRs.getString("title"), postRs.getString("content"), postRs.getInt("user_id"),
//                postRs.getString("date"),postRs.getInt("vote_up"),postRs.getInt("vote_down"), postRs.getBoolean("pinned")));
//            }
//
//            //get all comments
//            while (commentRs.next()) {
//                System.out.println("from user/my, user have comment");
//                comments.add(new CommentResponse(commentRs.getInt("comment_id"),
//                    commentRs.getInt("post_id"), commentRs.getString("content"), commentRs.getInt("user_id"),
//                    commentRs.getString("date")));
//            }
//
//            // return user profile using UserResponse class
//            UserResponse data = new UserResponse(userId, userFirstName, userLastName, userEmail, posts, comments);
//
//            if (data == null) {
//                return gson.toJson(new StructuredResponse("error", " not found", null));
//            } else {
//                return gson.toJson(new StructuredResponse("ok", null, data));
//            }
//
//        });
//
//        /**
//         * Get other user's information, similar to get my profile
//         *
//         * FINISH
//         */
//        Spark.get("api/users/:user_id/:sessionKey", (request, response) -> {
//
//            int userId = Integer.parseInt(request.params("user_id"));
//            String sessionKey = request.params("sessionKey");
//            System.out.println("from user/my: Session Key: "+sessionKey);
//            // String email = req.mEmail;
//
//            if(!hashmap.containsValue(sessionKey)){
//                response.status(401);
//                response.type("application/json");
//                return gson.toJson(new StructuredResponse("Error", "Authentication Failed", null));
//            }
//
//            String userFirstName = "";
//            String userLastName = "";
//            String userEmail = "";
//
//            ResultSet userRs = db.selectUserById(userId);
//            while (userRs.next()) {
//                System.out.println("from user/my, user found");
//                userId = userRs.getInt("user_id");
//                userFirstName = userRs.getString("first_name");
//                userLastName = userRs.getString("last_name");
//                userEmail = userRs.getString("email");
//            }
//
//            ResultSet postRs = db.selectPostByUserId(userId);
//            ResultSet commentRs = db.selectCommentsByUserId(userId);
//
//            ArrayList<PostResponse> posts = new ArrayList<PostResponse>();
//            ArrayList<CommentResponse> comments = new ArrayList<CommentResponse>();
//
//            //get all posts
//            while (postRs.next()) {
//                System.out.println("from user/my, user have post");
//                posts.add(new PostResponse(postRs.getInt("post_id"), postRs.getString("title"), postRs.getString("content"), postRs.getInt("user_id"),
//                postRs.getString("date"),postRs.getInt("vote_up"),postRs.getInt("vote_down"), postRs.getBoolean("pinned")));
//            }
//
//            //get all comments
//            while (commentRs.next()) {
//                System.out.println("from user/my, user have copmment");
//                comments.add(new CommentResponse(commentRs.getInt("comment_id"),
//                    commentRs.getInt("post_id"), commentRs.getString("content"), commentRs.getInt("user_id"),
//                    commentRs.getString("date")));
//            }
//
//            UserResponse data = new UserResponse(userId, userFirstName, userLastName, userEmail, posts, comments);
//
//            if (data == null) {
//                return gson.toJson(new StructuredResponse("error", " not found", null));
//            } else {
//                return gson.toJson(new StructuredResponse("ok", null, data));
//            }
//
//        });
//
//
//        /**
//         * this function will add a new post to backend
//         *
//         * FINISH!!!
//         */
//        Spark.post("api/posts/:sessionKey", (request, response) -> {
//
//            // get session key, use session key to find email
//            String sessionKey = request.params("sessionKey");
//
//            int userId = getUserIdFromSessionKey(hashmap, db, sessionKey);
//
//            int postId = -1;
//            // System.out.println("from api/posts/:sessionKey: Session Key: "+sessionKey);
//            // System.out.println("from api/posts/:sessionKey: user ID: "+userId);
//
//            // if user is not found, return error
//            if(userId == -1){
//                response.status(401);
//                response.type("application/json");
//                return gson.toJson(new StructuredResponse("Error", "user id not find, invalid post", null));
//            }
//
//
//            // UserRequest userReq = gson.fromJson(request.body(), UserRequest.class);
//            // String sessionKey = userReq.mSessionKey;
//            // String email = userReq.mEmail;
//
//            PostOrUpdatePostRequest req = gson.fromJson(request.body(), PostOrUpdatePostRequest.class);
//            // System.out.println("\tfrom api/posts/:sessionKey:user new post title "+req.title);
//            // System.out.println("\tfrom api/posts/:sessionKey:user new post content "+req.content);
//            response.type("application/json");
//
//            ResultSet nest;
//            try{
//                // insert the post in to the post table using data in request
//                db.insertPost(req.title, req.content, 0, 0, userId, false);
//                nest = db.GetNewestPost();
//
//                while(nest.next()){
//                    postId = nest.getInt("max");
//                }
//                // there will be netural vote at first
//                db.insertVote(userId, postId,false, false);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            response.status(200);
//
//            return gson.toJson(new StructuredResponse("ok", null, ""));
//
//        });
//
//
//        /**
//         * POST: this will post a new comment to exisitng post
//         */
//        Spark.post("api/posts/:post_id/comments/:sessionKey", (request, response) -> {
//
//            String sessionKey = request.params("sessionKey");
//
//            int userId = getUserIdFromSessionKey(hashmap, db, sessionKey);
//
//            // System.out.println("\t this useriD: "+userId);
//
//            // check if user exist
//            if(userId == -1){
//                response.status(401);
//                response.type("application/json");
//                return gson.toJson(new StructuredResponse("Error", "user id not find, invalid comment", null));
//            }
//
//            // store request's info
//            PostOrUpdateCommentRequest req = gson.fromJson(request.body(), PostOrUpdateCommentRequest.class);
//
//            response.type("application/json");
//            // NB: createEntry checks for null title and message
//
//            try{
//                // insert comment to the existing post in the database
//                db.insertComment(req.content, userId, req.postID);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            response.status(200);
//
//            return gson.toJson(new StructuredResponse("ok", null, ""));
//        });
//
//
//        /**
//         * PUT: this function is called when user update a post
//         */
//        Spark.put("api/posts/:post_id/:sessionKey", (request, response) -> {
//
//            // get session key, use session key to find email
//            String sessionKey = request.params("sessionKey");
//            int postID = Integer.parseInt(request.params("post_id"));
//
//            PostOrUpdatePostRequest req = gson.fromJson(request.body(), PostOrUpdatePostRequest.class);
//            // System.out.println("\tfrom api/posts/:sessionKey:user new post title "+req.title);
//            // System.out.println("\tfrom api/posts/:sessionKey:user new post content "+req.content);
//            response.type("application/json");
//
//            try{
//                // only the original post author can edit the post
//                db.UpdatePostByIdShort(req.title, req.content, postID);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            response.status(200);
//
//            return gson.toJson(new StructuredResponse("ok", null, ""));
//
//
//        });
//
//        /**
//         * PUT: this is used to update a comments
//         */
//        Spark.put("api/posts/:post_id/comments/:comment_id/:sessionKey", (request, response) -> {
//
//            // get the comment id for later referencing
//            int comment_id = Integer.parseInt(request.params("comment_id"));
//
//            PostOrUpdateCommentRequest req = gson.fromJson(request.body(), PostOrUpdateCommentRequest.class);
//
//            response.type("application/json");
//            // NB: createEntry checks for null title and message
//
//            System.out.println("\tfrom api/posts/:sessionKey:user new post content "+req.content);
//            try{
//                // only the comment author can edit the comment
//                db.updateCommentById(req.content, comment_id);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            response.status(200);
//
//            return gson.toJson(new StructuredResponse("ok", null, ""));
//
//        });
//
//        /*
//        * DELETE: this is used to deleted the comment of current log-in user
//        */
//        Spark.delete("api/posts/:post_id/comments/:comment_id/:sessionKey", (request, response) -> {
//
//            int comment_id = Integer.parseInt(request.params("comment_id"));
//
//            response.type("application/json");
//            // NB: createEntry checks for null title and message
//
//            try{
//                // only the original author can delete the comment
//                db.deleteCommentById(comment_id);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            response.status(200);
//
//            return gson.toJson(new StructuredResponse("ok", null, ""));
//
//        });
//
//        /*
//        * DELETE: this is used to deleted the post of current log-in user
//        */
//        Spark.delete("api/posts/:post_id/:sessionKey", (request, response) -> {
//
//            int postId = Integer.parseInt(request.params("post_id"));
//
//            response.type("application/json");
//            // NB: createEntry checks for null title and message
//            System.out.println("!!!!!!!!!"+ postId);
//            try{
//                // need to delete all the comments relating to the post first, then delete the post itself
//                db.deleteCommentsByPostId(postId);
//                db.deletePostById(postId);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            response.status(200);
//
//            return gson.toJson(new StructuredResponse("ok", null, ""));
//
//        });
//
//        /*
//        * PUT: used for updating the vote of current user
//        */
//        Spark.put("api/posts/:post_id/vote/:sessionKey", (request, response) -> {
//
//            // get session key, use session key to find email
//            String sessionKey = request.params("sessionKey");
//
//            int postID = Integer.parseInt(request.params("post_id"));
//
//            int userId = getUserIdFromSessionKey(hashmap, db, sessionKey);
//
//            int dc = 0; int uc = 0;
//            if(userId == -1){
//                response.status(401);
//                response.type("application/json");
//                return gson.toJson(new StructuredResponse("Error", "user id not find, invalid post", null));
//            }
//
//            // delete the vote using the userid and postid
//            db.deleteVoteByIds(userId, postID);
//
//            ResultSet downst; ResultSet upst;
//            VoteRequest req = gson.fromJson(request.body(), VoteRequest.class);
//            try{
//                // getting the status of the vote
//                boolean upvote = (req.upvote == 1);
//                boolean downvote = (req.downvote == 1);
//                // insert the vote into the database
//                db.insertVote( userId, postID, upvote, downvote);
//                // count the total up and down votes of the post
//                downst = db.SelectDownVoteCountOfPost(postID);
//                upst = db.SelectUpVoteCountOfPost(postID);
//                if(downst.next()){
//                    dc = downst.getInt("count");
//                }
//                if(upst.next()){
//                    uc = upst.getInt("count");
//                }
//
//                db.UpdatePostVoteById(postID, uc, dc);
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            // ensure status 200 OK, with a MIME type of JSON
//
//            response.type("application/json");
//            response.status(200);
//            return gson.toJson(new StructuredResponse("ok", null, ""));
//
//        });
//
//        Spark.post("api/auth", (request, response) -> {
//        //     // NB: if gson.Json fails, Spark will reply with status 500 Internal
//        //     // Server Error
//            String sessionKey = getSessionKey();
//            String idTokenString = gson.fromJson(request.body(), IdTokenRequest.class).idToken;
//
//            GoogleIdToken idToken = null;
//
//            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
//                // Specify the CLIENT_ID of the app that accesses the backend:
//                .setAudience(Collections.singletonList(CLIENT_ID))
//                // Or, if multiple clients access the backend:
//                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
//                .build();
//            try {
//                idToken = verifier.verify(idTokenString);
//            } catch (java.security.GeneralSecurityException eSecurity) {
//                response.status(401);
//                return gson.toJson(new StructuredResponse("error", "Token Verification Security Execption" + eSecurity, null));
//
//            } catch (java.io.IOException eIO) {
//                response.status(401);
//                return gson.toJson(new StructuredResponse("error", "Token Verification IO Execption" + eIO, null));
//            }
//            if (idToken != null) {
//                Payload payload = idToken.getPayload();
//
//                // check if the user is in the lehigh domain
//                if(payload.getHostedDomain().contains("lehigh.edu")){
//                    String email = payload.getEmail();
//                    boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//                    String name = (String) payload.get("name");
//                    String pictureUrl = (String) payload.get("picture");
//                    String locale = (String) payload.get("locale");
//                    String familyName = (String) payload.get("family_name");
//                    String givenName = (String) payload.get("given_name");
//
//                    // insert the new user into the database
//                    if (!db.selectUserByEmail(email).next()){
//                        db.insertUser(email, givenName, familyName);
//                    }
//                    // put the session key into the hashmap so that is associated with its email
//                    hashmap.put(email, sessionKey);
//                }
//            } else {
//                response.status(401);
//                return gson.toJson(new StructuredResponse("error", "id token is invalid", null));
//            }
//            response.status(200);
//            return gson.toJson(new StructuredResponse("ok", null, sessionKey));
//        });
//    }
}
