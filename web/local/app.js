"use strict";
var BriefPostsList = /** @class */ (function () {
    function BriefPostsList() {
    }
    /**
     * this function is usually called when user make a DELETE/POST/PUT to backend in order to
     *  ensure all the posts and comments are up to date.
     *
     * In and ONLY in this function, the value of sessionKey will be checked. If the session key
     *  is "", which means the user may visit the index.html directly without login, the application
     *  will redirect the user to login.html.
     *
     * After receive the response, the result will be update to 1) brief post list(which is the
     *  list of all posts on the left) 2) post Comment Block (which is the detail of posts and comments
     *  on the right) NOTE that ALL POST AND COMMMENT in the post comment block will be load at once.
     *
     * NOTE: only connect to backend when we are not testing. spec_runner already have post examples
     */
    BriefPostsList.refresh = function () {
        if (!testing) {
            if (debug) {
                console.log("current section key: " + BasicStructure.sessionKey);
            }
            if (BasicStructure.sessionKey === "") {
                alert("please login");
                window.location.replace(backendUrl + "/login.html");
            }
            else {
                if (debug) {
                    console.log("BriefPostsList.refresh() called");
                }
                $.ajax({
                    type: "GET",
                    url: backendUrl + "/api/posts/" + BasicStructure.sessionKey,
                    dataType: "json",
                    success: function (result) {
                        if (debug) {
                            console.log(result);
                        }
                        BriefPostsList.update(result);
                        PostCommentBlock.update(result);
                    },
                    error: function () {
                        alert("Login timeout, please login again");
                        window.location.replace(backendUrl + "/login.html");
                    }
                });
            }
        }
        else {
            //testing.
            BriefPostsList.update("");
            PostCommentBlock.update("");
        }
    };
    /**
     * this function will load the brief post block using the data passed by refresh(). This function
     *  will ONLY be called by refresh().
     *
     * NOTE: during testing, we do not need Handlebars to append div to html
     */
    BriefPostsList.update = function (data) {
        if (!testing) {
            // it is important to REMOVE all "post-brief-block" when new posts are added, or there 
            // will be more than one same post on the left, which is not what we want
            $(".post-brief-block").remove();
            // using handlebars, data will be processed and added to left part
            $("#left-part").append(Handlebars.templates['BriefPostsList.hb'](data));
        }
        // this line is to add click function that when user click a specific post on the left
        // the corresponding post's detail will be showed on the right
        $(".post-brief-block").click(BriefPostsList.showDetail);
    };
    /**
     * this function will only be called when a button from class "post-brief-block" is clicked.
     *  it will hide ALL div in the right view and then show the corresponding post detail.
     */
    BriefPostsList.showDetail = function () {
        // get the post id to be showed. The post id is stored in data-value attribute of the button
        var postID = $(this).data("value");
        if (debug) {
            console.log("user clicked post " + postID + ", show post detail on the right");
        }
        // no matter new post interface is showed or not, hide it. 
        $("#my-new-post-block").hide();
        // hide all post comment block. 
        $(".post-comment-view").hide();
        $(".my-user-profile-block").hide();
        // get the specific post comment block by post id because each post comment block 
        // has a data-value={{postid}} attribute, which is unique. 
        $(".post-comment-view[data-value='" + postID + "']").show();
    };
    return BriefPostsList;
}());
var PostCommentBlock = /** @class */ (function () {
    function PostCommentBlock() {
    }
    PostCommentBlock.init = function () {
        var postID = $(this).data("value");
        var imageType = $('#imgType').val();
        console.log("imageType", imageType);
        if (imageType == "image/jpg" || imageType == "image/png") {
            $.ajax({
                type: "GET",
                url: backendUrl + "/api/posts/" + postID + "/file/" + BasicStructure.sessionKey,
                dataType: "json",
                success: function (result) {
                    console.log(result);
                    $('#img').attr("src", "data:" + imageType + ";base64," + result);
                }
            });
        }
    };
    PostCommentBlock.readFile = function (input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            $('#insert-file-input').change(function () {
                //$('#title').val(this.value ? this.value.match(/([\w-_]+)(?=\.)/)[0] : '');
                $('#file-title').val(this.files && this.files.length ? this.files[0].name.split('.')[0] : '');
                PostCommentBlock.filetype = this.files && this.files.length ? this.files[0].type.split('.')[0] : '';
            });
            reader.readAsBinaryString(input.files[0]);
            reader.onloadend = function () {
                console.log(reader.result);
                var encodedStr = btoa(reader.result);
                //var img = $('#img');
                //img.src = this.result;
                $('#img-id').attr("src", "data:" + PostCommentBlock.filedata + ";base64," + encodedStr);
                PostCommentBlock.filedata = encodedStr;
                console.log(encodedStr);
            };
            //reader.readAsDataURL(document.getElementById('file').files[0]);
        }
    };
    PostCommentBlock.AddLink = function () {
        var strLink = $('#insert-link').val();
        $('#id-link').attr('href', strLink);
        $('#id-link').text(strLink);
        $('#id-link').attr('target', '_blank');
    };
    PostCommentBlock.update = function (data) {
        if (!testing) {
            $(".post-comment-view").remove();
            $("#right-part").append(Handlebars.templates['PostCommentBlock.hb'](data));
        }
        $(".send-comment").click(PostCommentBlock.addnewComment);
        $(".my-down-vote-button").click(PostCommentBlock.downVotePost);
        $(".my-up-vote-button").click(PostCommentBlock.upVotePost);
        $(".user-button").click(PostCommentBlock.getOtherProfile);
        $(".post-comment-view").hide();
    };
    PostCommentBlock.addnewComment = function () {
        var postID = $(this).data("value");
        var newContent = $("#my-new-comment-content" + postID).val();
        var newFileName = $("#file-title").val();
        if (newFileName.length <= 0) {
            newFileName = null;
        }
        var newFileType = PostCommentBlock.filetype;
        if (newFileType.length <= 0) {
            newFileType = null;
        }
        var newFileData = PostCommentBlock.filedata;
        if (newFileData.length <= 0) {
            newLink = null;
        }
        var newLink = $('#id-link').val();
        if (newLink.length <= 0) {
            newLink = null;
        }
        console.log(newFileName);
        // link and file
        if (newContent === "") {
            alert("invalid input");
        }
        else {
            sessionStorage.setItem("currentPost", postID);
            $.ajax({
                type: "POST",
                url: backendUrl + "/api/posts/" + postID + "/comments/" + BasicStructure.sessionKey,
                dataType: "json",
                data: JSON.stringify({
                    "postID": postID, "content": newContent, "links": "[" + newLink + "]", "fileName": newFileName, "fileType": newFileType, "fileData": newFileData
                }),
                success: function (result) {
                    console.log(result);
                    BriefPostsList.refresh();
                    var postID = sessionStorage.getItem("currentPost");
                    sessionStorage.removeItem("currentPost");
                    if (debug)
                        console.log("this postID: " + postID);
                    if (debug)
                        console.log("this postID2: " + postID);
                    $("#my-new-comment-content" + postID).val("");
                    if (debug)
                        console.log("length: " + $(".post-comment-view[data-value='" + postID + "']").length);
                    $(".post-comment-view[data-value='" + postID + "']").show();
                }
            });
        }
        if (debug)
            console.log("user add new comment under post: " + postID);
        if (debug)
            console.log("user send new comment: " + newContent);
    };
    PostCommentBlock.downVotePost = function () {
        // we need to make a state check 
        var postID = $(this).data("postid");
        var upVoteState = $("#my-up-vote-button" + postID).attr("data-upvotestate");
        var downVoteState = $(this).attr("data-downvotestate");
        if (debug)
            console.log("down vote button is clicked for post: " + postID);
        if (debug)
            console.log("up state: " + upVoteState);
        if (debug)
            console.log("down state: " + downVoteState);
        if (downVoteState == "true") {
            // user want to cancel the dislike 
            $(this).attr("data-downvotestate", "false");
            var currentDownVote = $("#my-down-vote-count" + postID).text();
            $("#my-down-vote-count" + postID).text(--currentDownVote);
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
            // both up vote and down vote are 0
            // ONLY connect backend when not testing
            if (!testing) {
                $.ajax({
                    type: "PUT",
                    url: backendUrl + "/api/posts/" + postID + "/vote/" + BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 0, "downvote": 0
                    }),
                    success: function (result) {
                        if (debug)
                            console.log(result);
                    }
                });
            }
        }
        else {
            // user dislike this post
            $(this).attr("data-downvotestate", "true");
            $(this).removeClass("my-vote-false").addClass("my-vote-true");
            var currentDownVote = $("#my-down-vote-count" + postID).text();
            $("#my-down-vote-count" + postID).text(++currentDownVote);
            if ($("#my-up-vote-button" + postID).attr("data-upvotestate") == "true") {
                var currentUpVote = $("#my-up-vote-count" + postID).text();
                $("#my-up-vote-count" + postID).text(--currentUpVote);
                $("#my-up-vote-button" + postID).attr("data-upvotestate", "false");
                $("#my-up-vote-button" + postID).removeClass("my-vote-true").addClass("my-vote-false");
            }
            // ONLY connect backend when not testing
            if (!testing) {
                $.ajax({
                    type: "PUT",
                    url: backendUrl + "/api/posts/" + postID + "/vote/" + BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 0, "downvote": 1
                    }),
                    success: function (result) {
                        if (debug)
                            console.log(result);
                    }
                });
            }
        }
    };
    PostCommentBlock.upVotePost = function () {
        // we need to make a state check 
        var postID = $(this).data("postid");
        var downVoteState = $("#my-down-vote-button" + postID).attr("data-downvotestate");
        var upVoteState = $(this).attr("data-upvotestate");
        if (debug)
            console.log("up vote button is clicked for post: " + postID);
        if (debug)
            console.log("up state: " + upVoteState);
        if (debug)
            console.log("down state: " + downVoteState);
        if (upVoteState == "true") {
            //user want to cancel the like
            $(this).attr("data-upvotestate", "false");
            var currentUpVote = $("#my-up-vote-count" + postID).text();
            $("#my-up-vote-count" + postID).text(--currentUpVote);
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
            // both up vote and down vote are 0
            // ONLY connect backend when not testing
            if (!testing) {
                $.ajax({
                    type: "PUT",
                    url: backendUrl + "/api/posts/" + postID + "/vote/" + BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 0, "downvote": 0
                    }),
                    success: function (result) {
                        if (debug)
                            console.log(result);
                    }
                });
            }
        }
        else {
            //user like this post
            $(this).attr("data-upvotestate", "true");
            $(this).removeClass("my-vote-false").addClass("my-vote-true");
            var currentUpVote = $("#my-up-vote-count" + postID).text();
            $("#my-up-vote-count" + postID).text(++currentUpVote);
            if (debug)
                console.log("after click: " + $("#my-up-vote-count" + postID).text());
            if ($("#my-down-vote-button" + postID).attr("data-downvotestate") == "true") {
                var currentDownVote = $("#my-down-vote-count" + postID).text();
                $("#my-down-vote-count" + postID).text(--currentDownVote);
                $("#my-down-vote-button" + postID).attr("data-downvotestate", "false");
                $("#my-down-vote-button" + postID).removeClass("my-vote-true").addClass("my-vote-false");
            }
            // ONLY connect backend when not testing
            if (!testing) {
                $.ajax({
                    type: "PUT",
                    url: backendUrl + "/api/posts/" + postID + "/vote/" + BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 1, "downvote": 0
                    }),
                    success: function (result) {
                        if (debug)
                            console.log(result);
                    }
                });
            }
        }
    };
    /**
     *
     * NOTE: during testing, we will not connect to the backend. We do not need any information about "this user"
     */
    PostCommentBlock.getOtherProfile = function () {
        if (!testing) {
            var userID = $(this).data("value");
            if (debug)
                console.log("user click user: " + userID + ", request for detail");
            $.ajax({
                type: "GET",
                url: backendUrl + "/api/users/" + userID + "/" + BasicStructure.sessionKey,
                dataType: "json",
                data: JSON.stringify({ "sessionKey": BasicStructure.sessionKey }),
                success: function (result) {
                    if (debug)
                        console.log(result);
                    PostCommentBlock.showOtherProfile(result);
                }
            });
        }
        else {
            PostCommentBlock.showOtherProfile(" ");
        }
    };
    /**
     *
     * @param data
     * NOTE: during testing, we do not need to create other profile block.
     */
    PostCommentBlock.showOtherProfile = function (data) {
        if (!testing) {
            $(".other-user-profile-block").remove();
            $("#right-part").append(Handlebars.templates['OtherProfileBlock.hb'](data));
            $(".close-button").click(function () {
                $(".other-user-profile-block").remove();
            });
        }
        else {
            $(".other-user-profile-block").show();
            $(".close-button").click(function () {
                $(".other-user-profile-block").hide();
            });
        }
    };
    PostCommentBlock.refresh = function () {
        this.init();
    };
    /**
     *
     * @param data
     * NOTE: during testing, we do not need Handlebars to append div to html
     */
    PostCommentBlock.filedata = null;
    PostCommentBlock.filetype = null;
    return PostCommentBlock;
}());
var BasicStructure = /** @class */ (function () {
    function BasicStructure() {
    }
    /**
     * This function will be called when the html is initialized.
     *
     * After the first time it is called, isInit will be set true so that nothing will happened
     *  if this function is called again.
     *
     * This function initialize all basic structure and block of the index.html page.
     *      1) it will initialize the banner, add functions to button
     *      2) it will refresh the BriefPostList. Note that BriefPostList have not been initalized,
     *              this funciton will send "GET api/posts" request to the backend and get all lists
     *              and load the response to the screen etc.
     *      3) it will initialize the NewPostBlock, which enable user to create a new post.
     */
    BasicStructure.init = function () {
        if (!BasicStructure.isInit) {
            // add the basix structure to the html
            $(".container").append(Handlebars.templates["BasicStructure.hb"]());
            $(".new-post-button").click(BasicStructure.showNewPostBlock);
            $(".my-profile-button").click(BasicStructure.showMyProfileBlock);
            $("#sign-out").click(BasicStructure.signOut);
            BriefPostsList.refresh();
            //PostCommentBlock.refresh();
            // initial new post block
            NewPostBlock.refresh();
            PostCommentBlock.refresh();
            BasicStructure.isInit = true;
        }
    };
    BasicStructure.signOut = function () {
        alert("sign out successfully");
        window.location.replace(backendUrl + "/login.html");
    };
    /**
     * this function is called when "new post" block is clicked
     * during this function:
     *      1) hide all post-comment-view
     *      2) show new post block
     *
     * Since NewPostBlock has aleady initialize in init(), we do not need to initialize it again
     */
    BasicStructure.showNewPostBlock = function () {
        $(".post-comment-view").hide();
        $("#my-new-post-block").show();
        $(".post-comment-view").hide();
        $(".my-user-profile-block").hide();
        // remember to hide other user's profile, if has one
        if (!testing) {
            $(".other-user-profile-block").remove();
        }
        else {
            $(".other-user-profile-block").hide();
        }
    };
    /**
     * This function is called when "my profile" button is clicked
     *  this fucntion will do:
     *      1) hide the post-comment-view
     *      2) hide new post interface
     *      3) remove other user info interface
     *
     * IMPORTANT: since "my profile" may different from previous call (because the user may have new posts
     *  and comments). We will call refresh() each time to ensure "my profile" is up to date.
     */
    BasicStructure.showMyProfileBlock = function () {
        $(".post-comment-view").hide();
        $("#my-new-post-block").hide();
        if (!testing) {
            $(".other-user-profile-block").remove();
        }
        else {
            $(".other-user-profile-block").hide();
        }
        MyProfileBlock.refresh();
    };
    BasicStructure.isInit = false;
    return BasicStructure;
}());
var NewPostBlock = /** @class */ (function () {
    function NewPostBlock() {
    }
    /**
     * this function will initailize the new post block.
     *  including add the html structure and click function
     *
     * NOTE: NewPostBlock will not be initialized during the test since spec_running.html has
     *  already had this structure.
     */
    NewPostBlock.init = function () {
        if (!testing) {
            if (!NewPostBlock.isInit) {
                $("#right-part").append(Handlebars.templates["NewPostBlock.hb"]());
                //$("#file-upload-btn").click(NewPostBlock.readURL);
                $("#my-send-new-post-button").click(NewPostBlock.sendPost);
                $("#my-new-post-block").hide();
                NewPostBlock.isInit = true;
            }
        }
        else {
            if (!NewPostBlock.isInit) {
                $("#my-send-new-post-button").click(NewPostBlock.sendPost);
                $("#my-new-post-block").hide();
                NewPostBlock.isInit = true;
            }
        }
    };
    /**
     * This function will send the new post to the server.
     *
     * NOTE: this function will not be called during test, so no if(testing) statement here
     */
    NewPostBlock.AddLink = function () {
        var strLink = $('#upload-link').val();
        $('#link-id').attr('href', strLink);
        $('#link-id').text(strLink);
        $('#link-id').attr('target', '_blank');
    };
    NewPostBlock.readURL = function (input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            $('#file-upload-input').change(function () {
                //$('#title').val(this.value ? this.value.match(/([\w-_]+)(?=\.)/)[0] : '');
                $('#image-title').val(this.files && this.files.length ? this.files[0].name.split('.')[0] : '');
                NewPostBlock.imagetype = this.files && this.files.length ? this.files[0].type.split('.')[0] : '';
            });
            reader.readAsBinaryString(input.files[0]);
            reader.onloadend = function () {
                console.log(reader.result);
                var encodedStr = btoa(reader.result);
                //var img = $('#img');
                //img.src = this.result;
                $('#img').attr("src", "data:" + NewPostBlock.imagetype + ";base64," + encodedStr);
                NewPostBlock.strbase64 = encodedStr;
                console.log(encodedStr);
            };
            //reader.readAsDataURL(document.getElementById('file').files[0]);
        }
        // else {
        //   NewPostBlock.removeUpload();
        // }
    };
    NewPostBlock.sendPost = function () {
        var newTitle = $("#input-title").val();
        var newContent = $("#input-content").val();
        //var newFile = $("#file-upload-image").val();
        var newFileName = $("#image-title").val();
        if (newFileName.length <= 0) {
            newFileName = null;
        }
        var newFileType = NewPostBlock.imagetype;
        if (newFileType.length <= 0) {
            newFileType = null;
        }
        var newFileData = NewPostBlock.strbase64;
        if (newFileData.length <= 0) {
            newLink = null;
        }
        var newLink = $('#link-id').val();
        if (newLink.length <= 0) {
            newLink = null;
        }
        console.log(newFileName);
        if (newTitle === "" || newContent === "") {
            alert("invalid input");
            return;
        }
        else {
            $.ajax({
                type: "POST",
                url: backendUrl + "/api/posts?session=" + BasicStructure.sessionKey,
                dataType: "json",
                data: JSON.stringify({ "title": newTitle, "content": newContent, "links": "[" + newLink + "]", "fileName": newFileName, "fileType": newFileType, "fileData": newFileData }),
                success: function (result) {
                    if (debug)
                        console.log(result);
                    BriefPostsList.refresh();
                    $("#my-new-post-block").hide();
                    $("#input-title").val("");
                    $("#input-content").val("");
                }
            });
        }
    };
    NewPostBlock.refresh = function () {
        this.init();
    };
    NewPostBlock.isInit = false;
    NewPostBlock.strbase64 = null;
    NewPostBlock.imagetype = null;
    return NewPostBlock;
}());
var MyProfileBlock = /** @class */ (function () {
    function MyProfileBlock() {
    }
    /**
     * This function will send a request to backend to get user's information.
     *  then is function will call update() to load the result.
     *
     * NOTE: we should not connect to the backend when testing.
     */
    MyProfileBlock.refresh = function () {
        if (!testing) {
            if (debug)
                console.log("MyProfileBlock.refresh() called");
            if (debug)
                console.log("\tsession key: " + BasicStructure.sessionKey);
            $.ajax({
                type: "GET",
                url: backendUrl + "/api/users/my/" + BasicStructure.sessionKey,
                dataType: "json",
                // data: JSON.stringify({ "sessionKey": BasicStructure.sessionKey }),
                success: function (result) {
                    if (debug)
                        console.log(result);
                    MyProfileBlock.update(result);
                },
                error: function () {
                    alert("Login timeout, please login again");
                    window.location.replace(backendUrl + "/login.html");
                }
            });
        }
        else {
            MyProfileBlock.update("");
        }
    };
    /**
     * this function is only called by MyProfileBlock.refresh().
     *  it will process data and add click function.
     */
    MyProfileBlock.update = function (data) {
        if (!testing) {
            // we have to delete previous my info interface first, or there will be 
            // more than one my info interface, which is not what we want. 
            $(".my-user-profile-block").remove();
            // the following line will process the data and load the data on the right side. 
            $("#right-part").append(Handlebars.templates['MyProfileBlock.hb'](data));
        }
        else {
            $(".my-user-profile-block").show();
        }
        // the follwing line will add click functions. 
        $(".post-update-button").click(MyProfileBlock.updatePost);
        $(".post-delete-button").click(MyProfileBlock.deletePost);
        $(".comment-update-button").click(MyProfileBlock.updateComment);
        $(".comment-delete-button").click(MyProfileBlock.deleteComment);
    };
    /**
     * this function will update a post to backend database
     */
    MyProfileBlock.updatePost = function () {
        var mID = $(this).data("value");
        if (debug)
            console.log("update post: " + mID);
        var newTitle = $("#post-title" + mID).val();
        var newContent = $("#post-content" + mID).val();
        if (debug)
            console.log("\tnew title: " + newTitle);
        if (debug)
            console.log("\tnew content: " + newContent);
        // if either content or title is "", we cannot update. Just alert to 
        // user and return. 
        if (newContent === "" || newTitle === "") {
            alert("invalid input");
            return;
        }
        $.ajax({
            type: "PUT",
            url: backendUrl + "/api/posts/" + mID + "/" + BasicStructure.sessionKey,
            dataType: "json",
            data: JSON.stringify({
                "title": newTitle, "content": newContent
            }),
            success: function (result) {
                if (debug)
                    console.log(result);
                // refresh all posts and comments
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    };
    /**
    * this function will delete a post to backend database
    */
    MyProfileBlock.deletePost = function () {
        var mID = $(this).data("value");
        if (debug)
            console.log("delete post: " + mID);
        $.ajax({
            type: "DELETE",
            url: backendUrl + "/api/posts/" + mID + "/" + BasicStructure.sessionKey,
            dataType: "json",
            success: function (result) {
                if (debug)
                    console.log(result);
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    };
    /**
     * this function will update a comment to backend database
     */
    MyProfileBlock.updateComment = function () {
        var mCID = $(this).data("value");
        var mPID = $(this).data("postid");
        if (debug)
            console.log("update comment: " + mCID + " under post: " + mPID);
        var newContent = $("#comment-content" + mCID).val();
        if (debug)
            console.log("\tnew content: " + newContent);
        if (newContent === "") {
            alert("invlid input");
            return;
        }
        $.ajax({
            type: "PUT",
            url: backendUrl + "/api/posts/" + mPID + "/comments/" + mCID + "/" + BasicStructure.sessionKey,
            dataType: "json",
            data: JSON.stringify({
                "postID": mPID,
                "content": newContent
            }),
            success: function (result) {
                if (debug)
                    console.log(result);
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    };
    /**
     * this function will delete a post to backend database
     */
    MyProfileBlock.deleteComment = function () {
        var mCID = $(this).data("value");
        var mPID = $(this).data("postid");
        if (debug)
            console.log("delete comment: " + mCID + " under post: " + mPID);
        $.ajax({
            type: "DELETE",
            url: backendUrl + "/api/posts/" + mPID + "/comments/" + mCID + "/" + BasicStructure.sessionKey,
            dataType: "json",
            success: function (result) {
                if (debug)
                    console.log(result);
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    };
    return MyProfileBlock;
}());
/// <reference path="ts/BriefPostsList.ts"/>
/// <reference path="ts/PostCommentBlock.ts"/>
/// <reference path="ts/BasicStructure.ts"/>
/// <reference path="ts/NewPostBlock.ts"/>
/// <reference path="ts/MyProfileBlock.ts"/>
// Prevent compiler errors when using Handlebars
// let Handlebars: any;
// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var Handlebars;
var $;
var backendUrl = "https://cse216-macrosoft.herokuapp.com";
var testing = false;
var debug = false;
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
// Run some configuration code when the web page loads
$(document).ready(function () {
    if (debug)
        console.log("ready");
    if (sessionStorage.getItem("sessionKey")) {
        BasicStructure.sessionKey = sessionStorage.getItem("sessionKey");
    }
    else {
        BasicStructure.sessionKey = "";
    }
    if (debug)
        console.log(BasicStructure.sessionKey);
    BasicStructure.init();
});
