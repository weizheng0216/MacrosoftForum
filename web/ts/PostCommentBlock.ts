class PostCommentBlock {

    /**
     * 
     * @param data 
     * NOTE: during testing, we do not need Handlebars to append div to html
     */

    private static filedata = null;
    private static filetype = null;

    // Handler installed on HTML
    public static readFile(input: any) {
        debugOutput("PostCommentBlock.readFile()");

        if (input.files && input.files[0]) {
            $('#file-title').val(input.files[0].name.split('.')[0]);
            var reader = new FileReader();
            PostCommentBlock.filetype = input.files[0].type.split('.')[0];
            reader.readAsBinaryString(input.files[0]);
            reader.onloadend = function () {
                debugOutput(reader.result);
                var encodedStr = btoa(reader.result.toString());
                $('#img-id').attr("src", "data:" + PostCommentBlock.filetype + ";base64," + encodedStr);
                PostCommentBlock.filedata = encodedStr;
                debugOutput(encodedStr);
            }
        }
    }

    public static showImage(postID) {
        debugOutput("PostCommentBlock.showImage()");
        let imageType = $('#imgType').val();
        debugOutput("imageType: " + imageType);
        if (imageType == "image/jpg" || imageType == "image/png") {
            myAjax({
                type: "GET",
                url: backendUrl + "/api/posts/" + postID + "/file?session=" + sessionKey,
                dataType: "json",
                success: function (result: any) {
                    debugOutput(result);
                    if (result.mData.mData) {
                        $('#pcbimg-post'+postID).attr("src", "data:" + imageType + ";base64," + result.mData.mData);
                    }
                    if ($('#pcbimg').attr("src") == "#") {
                        $('#pcbimg').hide();
                    }
                }
            });
        }

    }

    public static AddLink() {
        debugOutput("PostCommentBlock.AddLink()");
        var strLink = $('#insert-link').val();
        $('#id-link').attr('href', strLink);
        $('#id-link').text(strLink);
        $('#id-link').attr('target', '_blank');
    }

    /**
     * Called by BreifPostsList.refresh().
     * Update all background post-comment views with new data.
     * @param data Backend response with img.
     */
    public static update(data: any) {
        debugOutput("PostCommentBlock.update()");

        // Remove old containers from DOM
        $(".post-comment-view").remove();

        // Add new ones & hide
        $("#right-part").append(templatedHTML("PostCommentBlock", data));
        $(".post-comment-view").hide()

        // Register events
        $(".send-comment").on("click", PostCommentBlock.addnewComment);
        $(".my-down-vote-button").on("click", PostCommentBlock.onClickDownVote);
        $(".my-up-vote-button").on("click", PostCommentBlock.onClickUpVote);
        $(".user-button").on("click", PostCommentBlock.onClickOthersProfile);
    }

    private static addnewComment() {
        debugOutput("PostCommentBlock.addnewComment()");

        let postID = $(this).data("value");
        let newContent = $("#my-new-comment-content" + postID).val();
        var newFileName = $("#file-title").val();
        if (newFileName.length <= 0) {
            newFileName = "";
        }
        var newFileType = PostCommentBlock.filetype;
        if (newFileType == null) {
            //if(newFileType.length<=0){
            newFileType = "";
        }
        var newFileData = PostCommentBlock.filedata;
        if (newFileData == null) {
            newFileData = "";
        }
        var newLink = $('#id-link').text();
        if (newLink.length <= 0) {
            newLink = "";
        }
        debugOutput(newFileName);
        // link and file
        if (!newContent) {
            alertOutput("invalid input")
            return;
        } else {
            sessionStorage.setItem("currentPost", postID);
            myAjax({
                type: "POST",
                url: backendUrl + "/api/posts/" + postID + "/comments?session=" + sessionKey,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "postID": postID,
                    "content": newContent,
                    "links": "[" + newLink + "]",
                    "fileName": newFileName,
                    "fileType": newFileType,
                    "fileData": newFileData
                }),
                success: function (result: any) {
                    debugOutput(result);
                    BriefPostsList.refresh()
                    let postID = sessionStorage.getItem("currentPost");
                    sessionStorage.removeItem("currentPost");
                    debugOutput("this postID: " + postID, "this postID2: " + postID);
                    $("#my-new-comment-content" + postID).val("");
                    debugOutput("length: " + $(".post-comment-view[data-value='" + postID + "']").length);
                    $(".post-comment-view[data-value='" + postID + "']").show();

                }
            });
        }
        debugOutput("user add new comment under post: " + postID,
            "user send new comment: " + newContent);
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







    // ===================================================================
    // Events

    private static onClickOthersProfile() {
        debugOutput("PostCommentBlock.getOtherProfile()");

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
        debugOutput("PostCommentBlock.upVotePost()");

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
        debugOutput("PostCommentBlock.downVotePost()");

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

    private static onClickCommentAddFile() {

    }

    private static onClickCommentAddLink() {

    }

    private static onClickSendComment() {

    }
}
