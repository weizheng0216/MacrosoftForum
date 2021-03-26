# Web

## Introduction 

User can login using google account in login.html. After login, the user will be redirected to index.html.

In general, the front end's UI have two parts: left part and right part. Left part include 1) new post button 2) user information button 3) a list of brief posts. The right part will show corresponding interface when different operation is done on the left part. For example, when the user click the new post button, new post interface will be showed on the right. 

## Dependencies

Bootstrap, Handlebars and JQuery are used to ensure the web is functionally work. Specifically, icons in Boostrap are used. Handlebars process the responses from the backend and add the element in html, JQuery enable us to find a element efficiently.  

## Functions

To avoid mess up, all functions are writtern in 6 ts files. 5 of them are in the ts fold and one is app.ts. When deploying functions, `node_modules/typescript/bin/tsc app.ts --strict --outFile $TARGETFOLDER/$WEBFOLDERNAME/app.js` in deploy.sh will combine all ts file into one app.js file. Hence in index.html, only app.js is loaded. 

All the function in ts files are static. To call a function, use `ClassName.functionName()`

For the same reason, we seperate CSS file into 6 files under fold css. `cat css/BasicStructure.css css/postBrief.css css/PostCommentBlock.css css/NewPostBlock.css css/MyprofileBlock.css css/OtherProfileBlock.css> $TARGETFOLDER/$WEBFOLDERNAME/app.css` will merge all css file into app.css. index.html only load app.css.

## Communicate with Server
`$ajax` is used to send request and process response in the web.
```javascript
 $.ajax({
    type: "GET",
    url: backendUrl + "/api/auth",
    dataType: "json",
    data: JSON.stringify({ "idToken": id_token }),
    success: function (result:any) {
        alert("login successfully.");
        window.location.replace(backendUrl+"/index.html");
        sessionStorage.setItem('sessionKey', result.sessionKey);
    }
});
```
This function is called in login.html, which is send the id_token to backend. If the id token is valid, the server will return a session key, and then we will jump to index.html. To store the session key, session storage is used. 
```javascript
BasicStructure.sessionKey = sessionStorage.getItem("sessionKey");
```
In the index.html, session is stored in BasicStructure.sessionKey. Each time the front end send request to server, seesion key will be included. 

**The following functions are in BriefPostsList.ts**
```javascript
$.ajax({
    type: "GET",
    url: backendUrl + "/api/posts",
    dataType: "json",
    data: JSON.stringify({"sessionKey": BasicStructure.sessionKey}),
    success: function(result:any){
        BriefPostsList.update(result);
        PostCommentBlock.update(result);
    }
});

...

class BriefPostsList {
    private static update(data:any){

        ...

        $("#left-part").append(Handlebars.templates['BriefPostsList.hb'](data));

        ...

    }
}

...

class PostCommentBlock {
    public static update(data:any){

        ...

        $("#right-part").append(Handlebars.templates['PostCommentBlock.hb'](data));

        ...

    }
}
```
This function will get all posts, all comments, and whether the user like/dislike each posts. In and ONLY in this function, the value of sessionKey will be checked. If the session key is "", which means the user may visit the index.html directly without login, the application will redirect the user to login.html. If the user has login, two update functions are called. In the update function, handlebars will help us to load the information to screen. 

**The following functions are from MyProfileBlock.ts**
```javascript
 $.ajax({
    type: "GET",
    url: backendUrl + "/api/users/my",
    dataType: "json",
    data: JSON.stringify({ "sessionKey": BasicStructure.sessionKey }),
    success: function (result: any) {
        console.log(result);
        MyProfileBlock.update(result);
    }

    ...
    private static update(data: any){
    
        ...

        $("#right-part").append(Handlebars.templates['MyProfileBlock.hb'](data));

        ...

    }
});
```
This function will get "my" own information including my first name, last name, email, all posts and comment I sent. If success, the response will be processed by handlebars and posted to screen. 

```javascript
private static updatePost() {
        
    ...

    $.ajax({
        type: "PUT",
        url: backendUrl + "/api/posts/" + mID,
        dataType: "json",
        data: JSON.stringify({
            "sessionKey": BasicStructure.sessionKey,
            "title": newTitle, "content": newContent
        }),
        success: function (result: any) {
            console.log(result);
            // refresh all posts and comments
            MyProfileBlock.refresh();
        }
    });
}

private static deletePost(){
    
    ...

    $.ajax({
        type: "DELETE",
        url: backendUrl + "/api/posts/" + mID,
        dataType: "json",
        data: JSON.stringify({ "sessionKey": BasicStructure.sessionKey }),
        success: function (result: any) {
            console.log(result);
            MyProfileBlock.refresh();
        }
    });
}

private static updateComment() {

    ...

    $.ajax({
        type: "PUT",
        url: backendUrl + "/api/posts/" + mPID + "/comments/" + mCID,
        dataType: "json",
        data: JSON.stringify({
            "sessionKey": BasicStructure.sessionKey,
            "content": newContent
        }),
        success: function (result: any) {
            console.log(result);
            MyProfileBlock.refresh();
        }
    });
}

private static deleteComment() {
        
        ...

        $.ajax({
            type: "DELETE",
            url: backendUrl + "/api/posts/" + mPID + "/comments/" + mCID,
            dataType: "json",
            data: JSON.stringify({ "sessionKey": BasicStructure.sessionKey }),
            success: function (result: any) {
                console.log(result);
                MyProfileBlock.refresh();
            }
        });
    }
```
Those functions are 1) edit a post 2) delete a post 3) edit a comment 4) delete a comment. If success, `MyProfileBlock.refresh();` is called to ensure all posts and comments are up to date.

**The following functions are from NewPostBlock.ts**
```javascript
private static sendPost(){
        
    ...
    
    $.ajax({
        type: "POST",
        url: backendUrl + "/api/posts",
        dataType: "json",
        data: JSON.stringify({"sessionKey": BasicStructure.sessionKey, 
            "title": newTitle, "content": newContent}),
        success: function(result:any){
            console.log(result);
            BriefPostsList.refresh();

            ...

        }
    });
}
```
This function will send a new post to backend. If success, `MyProfileBlock.refresh();` is called to ensure all posts and comments are up to date.

**The following functions are from PostCommentBlock.ts**
```javascript
$.ajax({
    type: "PUT",
    url: backendUrl + "/api/posts/" + postID + "/vote",
    dataType: "json",
    data: JSON.stringify({
        "sessionKey": BasicStructure.sessionKey,
        "upvote": 0, "downovte": 0
    }),
    success: function (result: any) {
        
        ...

    }
});
```
This function will send user's attitude to a post: up vote/ down vote/ neutral.

**Note that state check is performed in front end**
|               | user click | like    | dislike |
|---------------|------------|---------|---------|
| current state |            |         |         |
| neutral       |            | like    | dislike |
| like          |            | neutral | dislike |
| dislike       |            | like    | neutral |

```javascript
private static getOtherProfile() {
 
    ...

    $.ajax({
        type: "GET",
        url: backendUrl + "/api/users/" + userID,
        dataType: "json",
        data: JSON.stringify({ "sessionKey": BasicStructure.sessionKey }),
        success: function (result: any) {
           
            PostCommentBlock.showOtherProfile(result);

        }
    });
    
}

private static showOtherProfile(data: any) {

    ...

    $("#right-part").append(Handlebars.templates['OtherProfileBlock.hb'](data));

    ...
}
```
This function will request other user's information including first name, last name, email, all posts, and all comments. 

**more documentations are in ts files**

