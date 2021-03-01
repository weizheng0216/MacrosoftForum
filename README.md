# The Buzz
---


## Introduction
---

    This app is for the company internal staffs to illustrate, up-vote, 
    and down-vote their ideas by giving them a space to discuss.
    


## User Guide
---    


        The users can use the following link to access to our platform.
        
        [heroku link](https://cse216-macrosoft.herokuapp.com/)
        
        After loading the website, the users will see the interface.
        - From there, we can see that there is a button "Make a Post." 
        When users want to share some ideas, you can click that.
        
        - Then a new window will be opened. From there, we can see three text boxes, 
        "Title," "Username/Anonymous," and "Message." 
        So users can write down the message title, their userid or choose to stay anonymous, 
        and then type the message.
        Username/Anonymous is for identification but users can stay anonymous. Type Messages is for users to 
        drop down their ideas. All these three boxes are required to fill out. If not, an error message will show up. 
        Except the three text boxes, there are three buttons, Hide, Clear, and Send. 
    
        - Users can hide the "Make a Post" window if they choose halt their thoughts for a second 
        and view other people’s thoughts. 
        Clicking “Clear” button helps to delete what users have written down if they decide to start over. 
        "Send" is for users to post their ideas. After clicking "Send,”" the users can see their post on the platform. 
	    On the platform, we can see that there are many posts. Each post frame has a couple attributes. 
        On the top of the post frame, we have the time stamp colored by gray indicating when the poster sent out the post.
        Following that are the message title, message, and the username. 
    
        - The last two attributes are for the function, up-vote and down-vote. 
        The users can choose to click these two buttons based on their opinions.
	    In addition, we also have a function for pinning the ideas. 
        For the pinned messages, the entire post frame is colored by yellow. 
        Before the time stamp, there is a pin icon indicating it is a pinned message.
	    On the top left of the entire platform next to our platform name "Macrosoft Buzz," we have the sorting functions. 
        The users can choose to sort the post wither by the date posted or the vote counts based on their preferences.

## Functions - Backend

**App.java(server) provides several APIs for front-end**
 ---
```java
    Spark.get("/", (req, res) -> {
        ...
    });
```
This function will send the index.html to front-end when a user connects.

```java
    Spark.get("/messages/sortbyvotes", (request, response) -> {
        ...
    });
```
This function will send all data sort by **up votes**, pinned message is at the beginning with attribute (with mPinned = true in mData[i]). The return type is structResponse front-end can access data through **mData** in structResponse, and mData is a arrayList of DataRow. 

**example:** front-end can access the content of the i-th message by: mData[i].mContent

---
```java
    Spark.get("/messages/sortbydate", (request, response) -> {
        ...
    });
```
This function will send all data sort by add **date**, pinned message is at the beginning with attribute (with mPinned = true in mData[i]). The return type is structResponse front-end can access data through **mData** in structResponse, and mData is a arrayList of DataRow. 

 ---
```java
    Spark.get("/messages/:id", (request, response) -> {
        ...
    });
```
This function will send the information of a data of target id format of getting one data（maybe use after update votes）The return type is structResponse. Front-end can access data through **mData** in structResponse, and mData is a class of DataRow. 

**example:** front-end can access the content of message by mData.mContent

 ---
```java
    Spark.put("/messages/upvote/:id", (request, response) -> {
        ...
    });
```
This function will update up vote by one of a target id. The return type is structResponse. Front-end can access data through **mData** in structResponse, and mData is a class of int which is the new up vote count if update successfully; null otherwise.

 ---
```java
    Spark.put("/messages/downvote/:id", (request, response) -> {
        ...
    });
```
This function will update down vote by one of a target id. The return type is structResponse. Front-end can access data through **mData** in structResponse, and mData is a class of int which is the new down vote count if update successfully; null otherwise.

 ---
```java
    Spark.post("/messages", (request, response) -> {
        ...
    });
```
This function will insert a new message to the database. POST should be in form of "data: JSON.stringify({ mTitle: title, mContent: content, mUserName: username})". The return type is structResponse. Front the can access the id of this message in mMessage.

---
## **Database.java provides several APIs for server.**

 ---
```java
    public static Database getDatabase(String db_url)
```
This fucntion will configure the database. It will first parse the url to get the necessary information to connect the database.If an error take place in connection and parsing url, exception will be thrown and catched in the function. Lastly, preparedStatements are constructed. *db_url* should contain the information of database including user infotmation, host, port, and path. This fucntion will return
a database connection that is ready to use.

 ---
```java
    public synchronized void createTable()
```
This function will create the table message.The attributes of the table are: **title, content, username, vote_up, vote_down, date, pinned.**

**Note:** createTable() is not called in getDatabase(); make sure the table is created before calling following method, or there must be an error

 ---
```java
    public synchronized void dropTable()
```
This function will drop the table **message**

 ---
```java
    public synchronized Arraylist<DataRow> selectAllSortByVotes() 
```
this function will return all messages sorted by votes

This function wil return an array list of DataRow. The array list contains 
all messages in table message **sorted by up votes**. Each message contains attributes 
title, content, username, date, vote_up(that indicates number of up vote 
of this message), vote_down(that indicates number of down vote of this message), and pinned(that indicates whether this message is pinned).

**NOTE:** messages that are pinned are in front of the array list. 

**NOTE:** messages are sort only by up votes, not up votes minus down votes.
 
 ---
```java
    public synchronized Arraylist<DataRow> selectAllSortByDate() 
```
This function wil return an array list of DataRow. This array list contain 
all message in table message sortin by date. Each message contains attributes 
title, content, username, date, vote_up(that indicates number of up vote 
of this message), vote_down(that indicates number of down vote of this message), and pinned(that indicates whether this message is pinned).

**NOTE:** messages that are pinned are in front of the array list. 

 ---
```java
    public synchronized DataRow selectOne(int id) 
```
This function wil return a seached message in form of DataRow. This message contains attributes: title, content, username, date(when the 
message is added), vote_up(that indicates number of up vote of this message), 
vote_down(that indicates number of down vote of this message), and pinned(that 
indicates whether this message is pinned).

 ---
```java
    public synchronized int voteUpOne(int id) 
```
This function will add vote up by one for the target message. It will return new vote_up of this message, and -1 if input id is not found.

 ---
```java
    public synchronized int voteDownOne(int id)
```
This function will add vote down by one for the target message. It will return new vote_down of this message, and -1 if input id is not found.

 ---
```java
    public synchronized boolean updateOneTitle(int id, String title)
```
this function will update the title of target id. Return true if new title is updated successfully; false if the input title is null or the target id is not found.

 ---
```java
    public synchronized boolean updateOneContent(int id, String content)
```
this function will update the content of target id. Return true if new content is updated successfully; false if the input content is null or the target id is not found.

 ---
```java
    public synchronized boolean updateOneUsername(int id, String username)
```
this function will update the username of target id. Return true if new username is updated successfully; false if the input username is null or the target id is not found.

 ---
```java
    public synchronized boolean updateOne(int id, String title, String content, String username)
```
This function will update the title, content, and username of target id. Return true if new title, content, and username are updated successfully; false if the any input are null or the target id is not found.

 ---
```java
    public synchronized boolean deleteOne(int id)
```
This function will delete the message of target id. Return true if this message is deleted successfully; false if the id is not found.

 ---
```java
    public synchronized int insertOne(String title, String content, String userName)
```
This function will insert a message. The date of this message is automatically set as system time. Both up votes and down votes are 0 as default. Message is not pinned as default. Return the row count if table or -1 is any input String is null or database error.

 ---
```java
    public synchronized boolean pinOne(int id)
```
This function will pin a message. Return true if the message is pinned successfully, false otherwise.

 ---
```java
    public synchronized boolean unpinOne(int id)
```
This function will unpin a message. Return true if the message is unpinned successfully, false otherwise.


    
