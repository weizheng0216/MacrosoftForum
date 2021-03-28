# Backend

## Backend Role
- Phase 1: Weihang Guo
- Phase 2: Wei Zheng

## Functions

## **App.java(server) provides several APIs for front-end**
 ---
```java
    Spark.get("/", (req, res) -> {
        ...
    });
```
this function will send the index.html to front-end when a user connects.

```java
   Spark.get("api/posts/:sessionKey", (request, response) -> {
        ...
    });
```
This function will send all posts data, including comments and user infos sort by date. The return type is structResponse front-end can access data through **mData** in structResponse, and mData is structure in key-value pair fashion with comment has a value of an array.

---
```java
   Spark.get("api/users/my/:sessionKey", (request, response) -> {
        ...
    });

    Spark.get("api/users/:user_id/:sessionKey", (request, response) -> {
        ...
    });

```
These two functions will send all data regarding user's profile. The first get request for the post and comment of the current users while the second request the info of other people's profile.

 ---
```java
    Spark.post("api/posts/:sessionKey", (request, response) -> {
         ...
    });

    Spark.post("api/posts/:post_id/comments/:sessionKey", (request, response) -> {
         ...
    });


```
These two posts function insert the new post and comment into the database. When inserting a comment, it will need a post id that comment is associate with. Both of the function does not return anything because data is being insert in the database, and frontend will be able to call refresh function every time a insertion has been performed.

 ---
```java
    Spark.put("api/posts/:post_id/:sessionKey", (request, response) -> {
        ...
    });
    Spark.put("api/posts/:post_id/vote/:sessionKey", (request, response) -> {
        ...
    });
    Spark.put("api/posts/:post_id/comments/:comment_id/:sessionKey", (request, response) -> {
        ...
    });
```
These three function are used to edit post, comment, and vote respectively. Updating comment need a post id while updating a voote need both the post id and the comment id. These functions return null since frontend will call refresh when changes are made.
 ---
```java
    Spark.delete("api/posts/:post_id/comments/:comment_id/:sessionKey", (request, response) -> {
        ...
    });
    Spark.delete("api/posts/:post_id/:sessionKey", (request, response) -> {
         ...
    });
```
These two function are used to delete the post and comment the current user sent. Posts and comments can only deleted by the original author. These functions return null, and frontend will call refresh when post or comment is being deleted.

## **Tests**
Unit tests are written in *cse216_macrosoft/backend/src/test/java/edu/lehigh/cse216/macrosoft/backend/test mainly on ensure the constructor works correctly.
