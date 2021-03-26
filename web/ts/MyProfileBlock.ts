class MyProfileBlock {

    /**
     * This function will send a request to backend to get user's information.
     *  then is function will call update() to load the result. 
     * 
     * NOTE: we should not connect to the backend when testing. 
     */
    static refresh() {
        if (!testing) {
            console.log("MyProfileBlock.refresh() called");
            console.log("\tsession key: " + BasicStructure.sessionKey);
            $.ajax({
                type: "GET",
                url: backendUrl + "/api/users/my",
                dataType: "json",
                data: JSON.stringify({ "sessionKey": BasicStructure.sessionKey }),
                success: function (result: any) {
                    console.log(result);
                    MyProfileBlock.update(result);
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
        console.log("update post: " + mID);
        var newTitle = $("#post-title" + mID).val();
        var newContent = $("#post-content" + mID).val();
        console.log("\tnew title: " + newTitle);
        console.log("\tnew content: " + newContent);

        // if either content or title is "", we cannot update. Just alert to 
        // user and return. 
        if (newContent === "" || newTitle === "") {
            alert("invalid input");
            return;
        }
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

    /**
    * this function will delete a post to backend database
    */
    private static deletePost() {
        var mID = $(this).data("value");
        console.log("delete post: " + mID);
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

    /**
     * this function will update a comment to backend database
     */
    private static updateComment() {
        var mCID = $(this).data("value");
        var mPID = $(this).data("postid");
        console.log("update comment: " + mCID + " under post: " + mPID);
        var newContent = $("#comment-content" + mCID).val();
        console.log("\tnew content: " + newContent);
        if(newContent ===""){
            alert("invlid input");
            return;
        }
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

    /**
     * this function will delete a post to backend database
     */
    private static deleteComment() {
        var mCID = $(this).data("value")
        var mPID = $(this).data("postid");
        console.log("delete comment: " + mCID + " under post: " + mPID);
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
}