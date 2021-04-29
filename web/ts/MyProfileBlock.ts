class MyProfileBlock {

    /**
     * This function will send a request to backend to get user's information.
     * It will only be called by events originated in this class.
     */
    static refresh() {
        debugOutput("MyProfileBlock.refresh()");
        myAjax({
            type: "GET",
            url: backendUrl + "/api/users/my?session=" + sessionKey,
            dataType: "json",
            success: function (res: any) {
                debugOutput("[ajax] Profile Response" + res);
                fetchImgs(res.mData.mPosts);
                debugOutput("[ajax] Profile Response(w/img): " + res);
                MyProfileBlock.update(res);
            },
            error: function() {
                alertOutput("Login timeout, please login again");
                redirect("login.html");
            }
        });
    }

    /**
     * This function loads the MyProfileBlock with the data passed by refresh().
     * This function will ONLY be called by refresh(). 
     * 
     * @param data StructuredResponse(with img data) from backend.
     */
    private static update(data: any) {
        debugOutput("MyProfileBlock.update()");

        // remove the exising outdated list
        $(".my-user-profile-block").remove();

        // append new block using handlebar and the data passed
        $("#right-part").append(Handlebars.templates['MyProfileBlock.hb'](data));

        // register click events of the block
        $(".post-update-button").on("click", MyProfileBlock.onClickUpdatePost);
        $(".post-delete-button").on("click", MyProfileBlock.onClickDeletePost);
        $(".comment-update-button").on("click", MyProfileBlock.onClickUpdateComment);
        $(".comment-delete-button").on("click", MyProfileBlock.onClickDeleteComment);
    }



    // ===================================================================
    // Events

    private static onClickUpdatePost() {
        debugOutput("MyProfileBlock.updatePost()");

        // pickup values
        var mID = $(this).data("value");
        var newTitle = $("#post-title" + mID).val();
        var newContent = $("#post-content" + mID).val();

        // No update if either content or title is "".
        // Alert user and return. 
        if (!newContent || !newTitle) {
            alertOutput("Content or Title cannot be empty");
            return;
        }

        myAjax({
            type: "PUT",
            url: backendUrl + "/api/posts/" + mID + "?session=" + sessionKey,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "title": newTitle,
                "content": newContent
            }),
            success: function (res: any) {
                debugOutput("[ajax] Update Response: " + res);
                // refresh all posts and comments
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    }

    private static onClickDeletePost() {
        debugOutput("MyProfileBlock.deletePost()");

        var mID = $(this).data("value");

        myAjax({
            type: "DELETE",
            url: backendUrl + "/api/posts/" + mID + "?session=" + sessionKey,
            dataType: "json",
            success: function (res: any) {
                debugOutput(res);
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    }

    private static onClickUpdateComment() {
        debugOutput("MyProfileBlock.updateComment()");

        var mCID = $(this).data("value");
        var mPID = $(this).data("postid");
        var newContent = $("#comment-content" + mCID).val();

        if (!newContent) {
            alertOutput("Content cannot be empty");
            return;
        }
        myAjax({
            type: "PUT",
            url: format("{1}/api/posts/{2}/comments/{3}?session={4}",
                        backendUrl, mPID, mCID, sessionKey),
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "content": newContent,
            }),
            success: function (res: any) {
                debugOutput(res);
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    }

    private static onClickDeleteComment() {
        debugOutput("MyProfileBlock.deleteComment()");

        var mCID = $(this).data("value")
        var mPID = $(this).data("postid");

        myAjax({
            type: "DELETE",
            url: format("{1}/api/posts/{2}/comments/{3}?session={4}",
                        backendUrl, mPID, mCID, sessionKey),
            dataType: "json",
            success: function (res: any) {
                debugOutput(res);
                BriefPostsList.refresh();
                MyProfileBlock.refresh();
            }
        });
    }

}