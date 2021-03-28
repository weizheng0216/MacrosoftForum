class MyProfileBlock {

    /**
     * This function will send a request to backend to get user's information.
     *  then is function will call update() to load the result. 
     * 
     * NOTE: we should not connect to the backend when testing. 
     */
    static refresh() {
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
                success: function (result: any) {
                    if (debug)
                        console.log(result);
                    MyProfileBlock.update(result);
                },
                error: function(){
                    alert("Login timeout, please login again");
                    window.location.replace(backendUrl+"/login.html");
                }
            });
        } else {
            MyProfileBlock.update("");
        }
    }

    /**
     * this function is only called by MyProfileBlock.refresh(). 
     *  it will process data and add click function. 
     */
    private static update(data: any) {
        if (!testing) {
            // we have to delete previous my info interface first, or there will be 
            // more than one my info interface, which is not what we want. 
            $(".my-user-profile-block").remove();

            // the following line will process the data and load the data on the right side. 
            $("#right-part").append(Handlebars.templates['MyProfileBlock.hb'](data));
        } else {
            $(".my-user-profile-block").show();
        }

        // the follwing line will add click functions. 
        $(".post-update-button").click(MyProfileBlock.updatePost);
        $(".post-delete-button").click(MyProfileBlock.deletePost);
        $(".comment-update-button").click(MyProfileBlock.updateComment);
        $(".comment-delete-button").click(MyProfileBlock.deleteComment);
    }

    /**
     * this function will update a post to backend database
     */
    private static updatePost() {
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
            success: function (result: any) {
                if (debug)
                    console.log(result);
                // refresh all posts and comments
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    }

    /**
    * this function will delete a post to backend database
    */
    private static deletePost() {
        var mID = $(this).data("value");
        if (debug)
            console.log("delete post: " + mID);
        $.ajax({
            type: "DELETE",
            url: backendUrl + "/api/posts/" + mID + "/" + BasicStructure.sessionKey,
            dataType: "json",
            success: function (result: any) {
                if (debug)
                    console.log(result);
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    }

    /**
     * this function will update a comment to backend database
     */
    private static updateComment() {
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
            success: function (result: any) {
                if (debug)
                    console.log(result);
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    }

    /**
     * this function will delete a post to backend database
     */
    private static deleteComment() {
        var mCID = $(this).data("value")
        var mPID = $(this).data("postid");
        if (debug)
            console.log("delete comment: " + mCID + " under post: " + mPID);
        $.ajax({
            type: "DELETE",
            url: backendUrl + "/api/posts/" + mPID + "/comments/" + mCID + "/" + BasicStructure.sessionKey,
            dataType: "json",
            success: function (result: any) {
                if (debug)
                    console.log(result);
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    }
}