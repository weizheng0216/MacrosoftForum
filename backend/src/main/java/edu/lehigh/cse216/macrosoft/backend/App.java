package edu.lehigh.cse216.macrosoft.backend;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;

// Import Google's JSON library
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;

public class App {

    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    public static void main( String[] args ){
        Spark.port(getIntFromEnv("PORT", 4567));
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        final Gson gson = new Gson();
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        //db.createTable(); // we will not create the table in backend
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        /**
         * this function will send the index.html to front-end when a user connects.
         */
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        /**
         * send all data sort by votes(up votes only), pinned message is at the beginning with attribute 
         * mPinned = true. 
         * 
         * return type: structResponse 
         * access data through "mData"
         * mData is a arrayList of DataRow
         * example: mData[i].mContent
         * 
         * {
         *  type: "GET",
         *  url: "/messages/sortbyvotes",
         *  dataType: "json",
         *  ...
         * }
         */
        Spark.get("/messages/sortbyvotes", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllSortByVotes()));
        });

        /**
         * send all data sort by date, pinned message is at the beginning with attribute 
         * mPinned = true. 
         * {
         *   type: "GET",
         *   url: "/messages/sortbydate",
         *   dataType: "json",
         *   ...
         * }
         * 
         */
        Spark.get("/messages/sortbydate", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllSortByDate()));
        });

        /**
         * send the information of a data of target id 
         * format of getting one data（maybe use after update votes）
         *
         * return type: structResponse 
         * access data through "mData"
         * mData is a class of DataRow
         * example: mData.mContent
         * {
         *  type: "GET",
         *  // id should be mID of this message
         *  url: "/messages/"+id,
         *  dataType: "json",
         *  ...
         * }
         */
        Spark.get("/messages/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            DataRow data = db.selectOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        /**
         * update up vote by one of the target id
         * {
         *  type: "PUT",
         *  // id should be mID of this message
         *  url: "/messages/upvote/" + id,
         *  dataType: "json",
         *  ...
         * }
         * 
         */
        Spark.put("/messages/upvote/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int newUpVote = db.voteUpOne(idx);
            if (newUpVote == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, newUpVote));
            }
        });

        /**
         * down up vote by one of the target id
         * 
         * {
         *  type: "PUT",
         *  // id should be mID of this message
         *  url: "/messages/downvote/" + id,
         *  dataType: "json",
         *  ...
         * }
         * 
         */
        Spark.put("/messages/downvote/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int newDownVote = db.voteDownOne(idx);
            if (newDownVote == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, newDownVote));
            }
        });

        /**
         * insert a new message to the database
         * 
         * {
         *   type: "POST",
         *   url: "/messages",
         *   dataType: "json",
         *   // upvote and downvote are 0 when message is created
         *   data: JSON.stringify({ mTitle: title, mContent: content, mUserName: username}),
         *   ...
         * }
         * 
         */
        Spark.post("/messages", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = db.insertOne(req.mTitle, req.mContent, req.mUserName);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

    }
}
