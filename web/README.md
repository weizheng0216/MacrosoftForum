# Web
## Functions

## **HTTP GET/POST/PUT for backend-end**
 ---
```ts
    refresh(option:string) {
        if (option === "Date Posted") {
            $.ajax({
                type: "GET",
                url: backendUrl + "/messages/sortbydate",
                dataType: "json",
                success: mainList.update
            });
        } else if (option === "Vote Counts"){
            $.ajax({
                type: "GET",
                url: backendUrl + "/messages/sortbyvotes",
                dataType: "json",
                success: mainList.update
            });
        }
    }
```
This function will get the data from backend according to different `option` paramter. `Date Posted` will sort the page so that latest post goes on the top, and `Vote Count` will sort the page so that most upvotes goes on the top

---
```ts
    upvote() {
        let id = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: backendUrl + "/messages/upvote/" + id,
            dataType: "json",
            // TODO: we should really have a function that looks at the return
            //       value and possibly prints an error message.
            success: mainList.doUp,
            error : function(){
                window.alert("/messages/upvote/" + id);
              }
        })
    }
```
This function will change the `upvote count` data in backend to a specific button by adding one to it for each click.

---
```ts
    private downvote() {
        let id = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: backendUrl + "/messages/downvote/" + id,
            dataType: "json",
            success: mainList.doDown,
            error : function(){
                window.alert("/messages/downvote/" + id);
              }
        })
    }
```
Similar to upvote function, except it change the number count on the downvote button.
---
```ts
submitForm() {
        let title = "" + $("#newTitle").val();
        let content = "" + $("#newMessage").val();
        let username = "" + $("#newUser").val();
        let date = new Date();
        if (title === "" || content === "" || username === "") {
            window.alert("Error: title, messafe, or username is not valid");
            return;
        }
        $.ajax({
            type: "POST",
            url: backendUrl + "/messages",
            dataType: "json",
            data: JSON.stringify({ mTitle: title, mContent: content, mUserName: username, mDate: date}),
            success: newPost.onSubmitResponse
        });
    }
```
This function is used that so user is able to add new data into the database. This function first validate the input, then sends the corresponding data to the backend using AJAX POSt.
 
## **Handlebars**

 ---
```html
    <div class="container " id= "list">
    <div class="card-columns justify-content-sm-center" id="ElementList">
        {{#each mData}}
        {{#if this.mPinned}}
            <div class="card text-white bg-warning mb-3" style="max-width: 18rem;">   
                <div class="card-header">
                    <i class="fas fa-thumbtack"></i> {{this.mDate}}
                </div>         
                <div class="card-body">
                    <h5 class="card-title">
                        {{this.mTitle}}
                    </h5>
                    <p class="card-text">{{this.mContent}}</p>
                    <p class="card-text"><small class="text-white">By {{this.mUserName}}</small></p>
                    <div class="container">
                        <div class = "row justify-content-sm-center">
                            <div class=".col-sm-3">
                                <button class="upbtn" data-value="{{this.mID}}">
                                    <i class="far fa-heart"></i>  {{this.mUpVote}} 
                                </button>
                            </div>
                            <div class = "offset-md-2 .col-sm-3">
                                <button class="downbtn" data-value="{{this.mID}}">
                                    <i class="far fa-thumbs-down"></i> {{this.mDownVote}} 
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        {{else}}
            <div class="card bg-light mb-3" style="max-width: 18rem;">   
                <div class="card-header">
                    {{this.mDate}}
                </div>         
                <div class="card-body">
                    <h5 class="card-title">
                        {{this.mTitle}}
                    </h5>
                    <p class="card-text">{{this.mContent}}</p>
                    <p class="card-text"><small class="text-muted">By {{this.mUserName}}</small></p>
                    <div class="container">
                        <div class = "row justify-content-sm-center">
                            <div class=".col-sm-3">
                                <button class="upbtn" data-value="{{this.mID}}">
                                    <i class="far fa-heart"></i>  {{this.mUpVote}} 
                                </button>
                            </div>
                            <div class = "offset-md-2 .col-sm-3">
                                <button class="downbtn" data-value="{{this.mID}}">
                                    <i class="far fa-thumbs-down"></i> {{this.mDownVote}} 
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        {{/if}}
        {{/each}}
    </div>
</div>
```
This handlebar is use to populated the body using the data from the backend. It used the card-column from the bootstrap for better looking. It first check if the data is pinned, if it is the case, it will use the yellow post box for that specific data, else, it will just use the regular box. It also contains upvote and downvote button that change whenever users click them.

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

## **Tests**
Unit tests are written in *cse216_macrosoft/web/apptest.ts*. The tests are mainly for the UI, especially the event triggered by clicking. Jasmine is used for the test, [click here](https://cse216-macrosoft.herokuapp.com/spec_runner.html) for more details.
