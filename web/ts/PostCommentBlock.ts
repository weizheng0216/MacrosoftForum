class PostCommentBlock {

    private static currPostId = -1;

    /**
     * Called by BreifPostsList.refresh().
     * Update all background post-comment views with new data.
     * @param data Backend response with img.
     */
    public static update(data: any) {
        debugOutput("PostCommentBlock.update()");

        // Remove old container from DOM
        $(".post-comment-block").remove();

        // Append the updated template
        $("#right-part").append(templatedHTML("PostCommentBlock", data));
        if (PostCommentBlock.currPostId == -1)
            $(".post-comment-block").hide();

        // Register events
        $(".my-down-vote-button").on("click", PostCommentBlock.onClickDownVote);
        $(".my-up-vote-button").on("click", PostCommentBlock.onClickUpVote);
        $(".user-button").on("click", PostCommentBlock.onClickOthersProfile);

        $("#send-comment").on("click", PostCommentBlock.onClickSendComment);
        $("#comment-fileupload-input").on("change", PostCommentBlock.onChangeCommentAddFile);
        $("#comment-fileupload-remove").on("click", PostCommentBlock.onClickCommentRemoveFile);
    }

    /**
     * Profile click event handler.
     * @param data The ajax response with img.
     */
    private static showOthersProfile(data: any) {
        debugOutput("PostCommentBlock.showOtherProfile()");
        $(".other-user-profile-block").remove();
        $("#right-part").append(templatedHTML("OthersProfileBlock", data));
        $(".close-button").on("click", function () {
            $(".other-user-profile-block").remove();
        })
    }

    /**
     * Show the post with specific ID. However this function does not set
     * the visibility of "post-comment-block", i.e. caller should set the
     * "post-comment-block" to be the only visible block before calling this
     * function.
     * @param postId ID of the post to show.
     */
    public static showPost(postId: number) {
        PostCommentBlock.currPostId = postId;
        $(".post-comment-view").hide();
        let targetView = $(format(".post-comment-view[data-value='{1}']", postId));
        if (targetView) {  // only show when the view exists
            targetView.show();
        } else {  // otherwise hide the entire block again.
            $(".post-comment-block").hide();
        }
    }

    // ===================================================================
    // Events

    private static onClickOthersProfile() {
        debugOutput("PostCommentBlock.onClickOthersProfile()");

        var userID = $(this).data("value");
        debugOutput("user click user: " + userID + ", request for detail");
        myAjax({
            type: "GET",
            url: backendUrl + "/api/users/" + userID + "?session=" + sessionKey,
            dataType: "json",
            success: function (res: any) {
                debugOutput("[ajax] Others profile response: " + JSON.stringify(res));
                fetchImgs(res.mData.mPosts);
                PostCommentBlock.showOthersProfile(res);
            }
        });
    }

    private static onClickUpVote() {
        debugOutput("PostCommentBlock.onClickUpVote()");

        let postID = $(this).data("postid");
        let oldState = $(this).attr("data-upvotestate") == "true";

        // update DOM
        if (oldState) {
            //user wants to cancel the like
            $(this).attr("data-upvotestate", "false");
            let currentUpVotes = Number($("#my-up-vote-count" + postID).text());
            $("#my-up-vote-count" + postID).text(--currentUpVotes);
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
        } else {
            //user likes this post
            $(this).attr("data-upvotestate", "true");
            $(this).removeClass("my-vote-false").addClass("my-vote-true");
            let currentUpVotes = Number($("#my-up-vote-count" + postID).text());
            $("#my-up-vote-count" + postID).text(++currentUpVotes);
            if ($("#my-down-vote-button" + postID).attr("data-downvotestate") == "true") {
                let currentDownVote = Number($("#my-down-vote-count" + postID).text());
                $("#my-down-vote-count" + postID).text(--currentDownVote);
                $("#my-down-vote-button" + postID).attr("data-downvotestate", "false");
                $("#my-down-vote-button" + postID).removeClass("my-vote-true").addClass("my-vote-false");
            }
        }
        // inform backend
        myAjax({
            type: "PUT",
            url: backendUrl + "/api/posts/" + postID + "/vote?session=" + sessionKey,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "upVote": !oldState,
                "downVote": false
            }),
            success: function (res: any) {
                debugOutput("[ajax] Upvote Click response: " + JSON.stringify(res));
            }
        });
    }

    private static onClickDownVote() {
        debugOutput("PostCommentBlock.onClickDownVote()");

        let postID = $(this).data("postid");
        let oldState = $(this).attr("data-downvotestate") == "true";

        if (oldState) {
            // user want to cancel the dislike 
            $(this).attr("data-downvotestate", "false");
            let currentDownVote = Number($("#my-down-vote-count" + postID).text());
            $("#my-down-vote-count" + postID).text(--currentDownVote);
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
        } else {
            // user dislike this post
            $(this).attr("data-downvotestate", "true");
            $(this).removeClass("my-vote-false").addClass("my-vote-true");
            let currentDownVote = Number($("#my-down-vote-count" + postID).text());
            $("#my-down-vote-count" + postID).text(++currentDownVote);
            if ($("#my-up-vote-button" + postID).attr("data-upvotestate") == "true") {
                let currentUpVote = Number($("#my-up-vote-count" + postID).text());
                $("#my-up-vote-count" + postID).text(--currentUpVote);
                $("#my-up-vote-button" + postID).attr("data-upvotestate", "false");
                $("#my-up-vote-button" + postID).removeClass("my-vote-true").addClass("my-vote-false");
            }
        }
        // inform backend
        myAjax({
            type: "PUT",
            url: backendUrl + "/api/posts/" + postID + "/vote?session=" + sessionKey,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "upVote": false,
                "downVote": !oldState
            }),
            success: function (res: any) {
                debugOutput("[ajax] Downvote Click response: " + JSON.stringify(res));
            }
        });
    }

    private static onClickDownloadFile() {
        debugOutput("PostCommentBlock.onClickDownloadFile()");

    }

    private static onChangeCommentAddFile() {
        debugOutput("PostCommentBlock.onChangeCommentAddFile()");

        let htmFileInput: any = document.getElementById("comment-fileupload-input");
        let htmName: any = document.getElementById("comment-fileupload-name");
        htmName.innerHTML = "<b>" + htmFileInput.files[0].name + "</b>";
    }

    private static onClickCommentRemoveFile() {
        debugOutput("PostCommentBlock.onClickCommentRemoveFile()");

        let htmInput: any = document.getElementById("comment-fileupload-input");
        let htmName: any = document.getElementById("comment-fileupload-name");
        htmInput.value = null;  // clear selection
        htmName.innerHTML = "No file selected";  // reset filename entry
    }

    private static onClickSendComment() {
        debugOutput("PostCommentBlock.onClickSendComment()");
        let htmFileInput: any = document.getElementById("comment-fileupload-input");
        let file = htmFileInput.files ? htmFileInput.files[0] : null;

        // Data to be included in request Json
        let content = $("#comment-content-input").val();
        let rawLinks: any = $("#comment-links").val();  // any: avoid compiler error
        let fileName = file ? file.name : "";
        let fileType = file ? file.type : "";
        let fileData = "";  // base64 string for file content

        // Comment must have content.
        if (!content) {
            alertOutput("Missing comment body.");
            return;
        }

        (async function () {
            // Load fileData
            fileData = await new Promise(function (resolve) {
                let reader = new FileReader();
                reader.onloadend = e => resolve(btoa(e.target.result.toString()));
                if (!file) resolve("");
                reader.readAsBinaryString(file);
            });
            // Send request
            myAjax({
                type: "POST",
                url: format("{1}/api/posts/{2}/comments?session={3}",
                    backendUrl, PostCommentBlock.currPostId, sessionKey),
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "content": content,
                    "links": rawLinks.split(/\s+/),
                    "fileName": fileName,
                    "fileType": fileType,
                    "fileData": fileData
                }),
                success: function (res: any) {
                    debugOutput("[ajax] New Comment Response: " + JSON.stringify(res));

                    // Refresh DOM upon success
                    $("#comment-content-input").val("");
                    $("#comment-links").val("");
                    BriefPostsList.refresh();  // the entire PostCommentBlock will be reset here
                    PostCommentBlock.showPost(PostCommentBlock.currPostId);
                }
            });
        })();
    }
}
