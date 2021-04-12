package edu.lehigh.cse216.macrosoft.backend;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.gson.Gson;
import spark.Spark;

import edu.lehigh.cse216.macrosoft.backend.json.*;

import java.util.ArrayList;

import static edu.lehigh.cse216.macrosoft.backend.DriveHelper.toFullPath;

/**
 * This class only contains static members. It uses the <i>Java Spark Framework</i>
 * to implement the backend server of BUZZ. All required configurations are
 * listed as parameters of the {@code run} method, which will kick off the server.
 */
class BuzzServer {
    private BuzzServer() {}

    private static Gson gson;

    private static DatabaseHelper db;

    private static CacheHelper cache;

    private static AuthHelper auth;

    private static DriveHelper drive;

    /**
     * Run the BUZZ server!
     * @param PORT The port to which the server will bind.
     * @param STATIC_LOCATION The resource directory to be served.
     * @param DATABASE_URL The URL to the database, with credentials.
     * @param CLIENT_ID The Google Developer Console client ID.
     * @param MEMCACHED_SERVERS
     * @param MEMCACHED_USERNAME
     * @param MEMCACHED_PASSWORD
     * @throws Exception Should terminate the program.
     */
    static void run(int PORT,
                    String STATIC_LOCATION,
                    String DATABASE_URL,
                    String CLIENT_ID,
                    String MEMCACHED_SERVERS,
                    String MEMCACHED_USERNAME,
                    String MEMCACHED_PASSWORD) throws Exception {
        Spark.port(PORT);
        Spark.staticFileLocation(STATIC_LOCATION);
        gson = new Gson();
        db = new DatabaseHelper(DATABASE_URL);
        cache = new CacheHelper(MEMCACHED_SERVERS, MEMCACHED_USERNAME, MEMCACHED_PASSWORD);
        auth = new AuthHelper(CLIENT_ID, cache);
        drive = new DriveHelper();

        defineGETs();
        definePOSTs();
        definePUTs();
        defineDELETEs();
    }

    private static void defineGETs() {
        // *****************************************************************
        // *                       GET /api/posts
        // *****************************************************************
        Spark.get("/api/posts", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // database query
            Object payload = db.queryGetAllPosts(loginUserId);
            res.status(200);
            return StructuredResponse.OK(payload);
        }, gson::toJson);

        // *****************************************************************
        // *                 GET /api/posts/:post_id/file
        // *****************************************************************
        Spark.get("/api/posts/:post_id/file", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // get file from storage
            String postId = req.params("post_id");
            String fullpath = db.queryPostFilePath(postId);
            String str64 = null;
            if (fullpath != null) {
                str64 = cache.getFile(fullpath);
                if (str64 == null) {
                    str64 = drive.getFile(fullpath);
                    if (str64 != null)
                        cache.saveFile(fullpath, str64);
                }
            }

            // respond with file in payload.
            Object payload = new FilePayload(str64);
            res.status(200);
            return StructuredResponse.OK(payload);
        }, gson::toJson);

        // *****************************************************************
        // *      GET /api/posts/:post_id/comments/:comment_id/file
        // *****************************************************************
        Spark.get("/api/posts/:post_id/comments/:comment_id/file", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // get file from storage
            String postId = req.params("post_id");
            String commentId = req.params("comment_id");
            String fullpath = db.queryCommentFilePath(postId, commentId);
            String str64 = null;
            if (fullpath != null) {
                str64 = cache.getFile(fullpath);
                if (str64 == null) {
                    str64 = drive.getFile(fullpath);
                    if (str64 != null)
                        cache.saveFile(fullpath, str64);
                }
            }

            // respond with file in payload
            Object payload = new FilePayload(str64);
            res.status(200);
            return StructuredResponse.OK(payload);
        }, gson::toJson);

        // *****************************************************************
        // *                       GET /api/users/my
        // *****************************************************************
        Spark.get("/api/users/my", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // get requesting user's bio
            Object payload = db.queryUserInfo(loginUserId);
            if (payload == null) {
                res.status(404);
                return StructuredResponse.ERR("User info does not exist.");
            }

            res.status(200);
            return StructuredResponse.OK(payload);
        }, gson::toJson);

        // *****************************************************************
        // *                    GET /api/users/:user_id
        // *****************************************************************
        Spark.get("/api/users/:user_id", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // get specific user's bio
            String userId = req.params("user_id");
            Object payload = db.queryUserInfo(userId);
            if (payload == null) {
                res.status(404);
                return StructuredResponse.ERR("User info does not exist.");
            }

            res.status(200);
            return StructuredResponse.OK(payload);
        }, gson::toJson);
    }

    private static void definePOSTs() {
        // *****************************************************************
        // *                        POST /api/posts
        // *****************************************************************
        Spark.post("/api/posts", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // read post request & store the post into db
            PostRequest request = gson.fromJson(req.body(), PostRequest.class);
            String postId = db.addPost(loginUserId, request);

            // try to save file into backend storage
            if (request.fileData != null) {
                String fullpath = toFullPath(request.fileName, postId);
                boolean success = drive.saveFile(fullpath, request.fileData);
                if (!success) {
                    res.status(507);
                    return StructuredResponse.ERR(
                            "Server has run out of quota. File is not saved.");
                }
            }

            res.status(200);
            return StructuredResponse.OK(null);
        }, gson::toJson);

        // *****************************************************************
        // *               POST /api/posts/:post_id/comments
        // *****************************************************************
        Spark.post("/api/posts/:post_id/comments", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // read comment request & save comment into db
            CommentRequest request = gson.fromJson(req.body(), CommentRequest.class);
            String postId = req.params("post_id");
            String commentId = db.addComment(loginUserId, postId, request);

            if (commentId == null) {
                res.status(404);
                return StructuredResponse.ERR("Post does not exist.");
            }

            // try to save file into backend storage
            if (request.fileData != null) {
                String fullpath = toFullPath(request.fileName, postId, commentId);
                boolean success = drive.saveFile(fullpath, request.fileData);
                if (!success) {
                    res.status(507);
                    return StructuredResponse.ERR(
                            "Server has run out of quota. File is not saved.");
                }
            }

            res.status(200);
            return StructuredResponse.OK(null);
        }, gson::toJson);

        // *****************************************************************
        // *                        POST /api/auth
        // *****************************************************************
        Spark.post("/api/auth", (req, res) -> {
            res.type("application/json");

            // read auth request & try access resource
            LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
            GoogleIdToken.Payload payload = auth.accessResource(request.idToken);
            if (payload == null) {
                res.status(401);
                return StructuredResponse.ERR("Login fails.");
            }

            // verify user's domain
            if (!payload.getEmailVerified()) {
                res.status(401);
                return StructuredResponse.ERR("User's domain is unauthorized.");
            }

            // user is valid, login and add to db
            String userId = db.addUser(payload);  // will not add duplicate user
            String sessionKey = auth.login(userId);

            res.status(200);
            return StructuredResponse.OK(sessionKey);
        }, gson::toJson);
    }

    private static void definePUTs() {
        // *****************************************************************
        // *                    PUT /api/posts/:post_id
        // *****************************************************************
        Spark.put("/api/posts/:post_id", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // read request & get author of the post
            PostRequest request = gson.fromJson(req.body(), PostRequest.class);
            String postId = req.params("post_id");
            String authorId = db.queryPostAuthor(postId);

            if (authorId == null) {
                res.status(404);
                return StructuredResponse.ERR("Post does not exist.");
            }

            if (!loginUserId.equals(authorId)) {
                res.status(403);
                return StructuredResponse.ERR("Cannot edit other user's post.");
            }

            // execute update in db
            db.updatePost(postId, request);

            res.status(200);
            return StructuredResponse.OK(null);
        }, gson::toJson);

        // *****************************************************************
        // *          PUT /api/posts/:post_id/comments/:comment_id
        // *****************************************************************
        Spark.put("/api/posts/:post_id/comments/:comment_id", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // read request & get author of the comment
            CommentRequest request = gson.fromJson(req.body(), CommentRequest.class);
            String postId = req.params("post_id");
            String commentId = req.params("comment_id");
            String authorId = db.queryCommentAuthor(postId, commentId);

            if (authorId == null) {
                res.status(404);
                return StructuredResponse.ERR("Post/Comment does not exist.");
            }

            if (!loginUserId.equals(authorId)) {
                res.status(403);
                return StructuredResponse.ERR("Cannot edit other user's comment.");
            }

            // execute update in db
            db.updateComment(commentId, request);

            res.status(200);
            return StructuredResponse.OK(null);
        }, gson::toJson);

        // *****************************************************************
        // *               PUT /api/posts/:post_id/vote
        // *****************************************************************
        Spark.put("/api/posts/:post_id/vote", (req, res) -> {
            res.type("application/json");

            // verify login
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // read request & check request validity
            VoteRequest request = gson.fromJson(req.body(), VoteRequest.class);
            if (request.upVote && request.downVote) {
                res.status(400);
                return StructuredResponse.ERR("Can only have 1 upvote at most.");
            }

            // check if the target post exists
            String postId = req.params("post_id");
            String authorId = db.queryPostAuthor(postId);
            if (authorId == null) {
                res.status(404);
                return StructuredResponse.ERR("Post does not exist.");
            }

            // execute update in db
            db.updateVote(loginUserId, postId, request);  // will also update vote count

            res.status(200);
            return StructuredResponse.OK(null);
        }, gson::toJson);
    }

    private static void defineDELETEs() {
        // *****************************************************************
        // *         DELETE /api/posts/:post_id/comments/:comment_id
        // *****************************************************************
        Spark.delete("/api/posts/:post_id/comments/:comment_id", (req, res) -> {
            res.type("application/json");

            // verify delete
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // get author of the comment
            String postId = req.params("post_id");
            String commentId = req.params("comment_id");
            String authorId = db.queryCommentAuthor(postId, commentId);

            if (authorId == null) {
                res.status(404);
                return StructuredResponse.ERR("Post/Comment does not exist.");
            }

            if (!loginUserId.equals(authorId)) {
                res.status(403);
                return StructuredResponse.ERR("Cannot delete other user's comment.");
            }

            // remove file from storage
            String fullpath = db.queryCommentFilePath(postId, commentId);
            cache.removeFile(fullpath);
            drive.removeFile(fullpath);

            // execute update in db
            db.deleteComment(postId, commentId);

            res.status(200);
            return StructuredResponse.OK(null);
        }, gson::toJson);

        // *****************************************************************
        // *                 DELETE /api/posts/:post_id
        // *****************************************************************
        Spark.delete("/api/posts/:post_id", (req, res) -> {
            res.type("application/json");

            // verify delete
            String sessionKey = req.queryParams("session");
            String loginUserId = auth.verifyLogin(sessionKey);
            if (loginUserId == null) {
                res.status(401);
                return StructuredResponse.LOGIN_ERR;
            }

            // get author of the post
            String postId = req.params("post_id");
            String authorId = db.queryPostAuthor(postId);

            if (authorId == null) {
                res.status(404);
                return StructuredResponse.ERR("Post does not exist.");
            }

            if (!loginUserId.equals(authorId)) {
                res.status(403);
                return StructuredResponse.ERR("Cannot delete other user's post.");
            }

            // remove files from storage
            // file under the post
            String fullpath = db.queryPostFilePath(postId);
            cache.removeFile(fullpath);
            drive.removeFile(fullpath);
            // files under all comments
            ArrayList<String> fullpaths =
                    db.queryAllCommentFilePaths(postId);
            for (String path : fullpaths) {
                cache.removeFile(path);
                drive.removeFile(path);
            }

            // execute update in db
            db.deletePost(postId);  // will also delete the comments under the post

            res.status(200);
            return StructuredResponse.OK(null);
        }, gson::toJson);
    }
}
