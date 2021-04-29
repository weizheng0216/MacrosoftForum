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

    // called by AJAX: GET ALL POSTS
    public static update(data: any) {
        debugOutput("PostCommentBlock.update()");

        $(".post-comment-view").remove();
        $("#right-part").append(Handlebars.templates['PostCommentBlock.hb'](data));

        $(".send-comment").on("click", PostCommentBlock.addnewComment);
        $(".my-down-vote-button").on("click", PostCommentBlock.onClickDownVote);
        $(".my-up-vote-button").on("click", PostCommentBlock.onClickUpVote);
        $(".user-button").on("click", PostCommentBlock.onClickOthersProfile);
        $(".post-comment-view").hide()
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
     * 
     * @param data 
     * NOTE: during testing, we do not need to create other profile block. 
     */
    private static showOtherProfile(data: any) {
        debugOutput("PostCommentBlock.showOtherProfile()");
        $(".other-user-profile-block").remove();
        $("#right-part").append(Handlebars.templates['OtherProfileBlock.hb'](data));
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
            data: JSON.stringify({ "sessionKey": sessionKey }),
            success: function (result: any) {
                debugOutput(result);
                PostCommentBlock.showOtherProfile(result);
            }
        });
    }

    private static onClickUpVote() {
        debugOutput("PostCommentBlock.upVotePost()");
        // we need to make a state check 
        let postID = $(this).data("postid");
        let downVoteState = $("#my-down-vote-button" + postID).attr("data-downvotestate");
        let upVoteState = $(this).attr("data-upvotestate");
        debugOutput("up vote button is clicked for post: " + postID,
            "up state: " + upVoteState,
            "down state: " + downVoteState);

        if (upVoteState == "true") {
            //user want to cancel the like
            $(this).attr("data-upvotestate", "false");
            let currentUpVote = Number($("#my-up-vote-count" + postID).text());
            $("#my-up-vote-count" + postID).text(--currentUpVote);
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
            // both up vote and down vote are 0

            myAjax({
                type: "PUT",
                url: backendUrl + "/api/posts/" + postID + "/vote?session=" + sessionKey,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "upVote": false,
                    "downVote": false
                }),
                success: function (result: any) {
                    debugOutput(result);
                }
            });

        } else {
            //user like this post
            $(this).attr("data-upvotestate", "true");
            $(this).removeClass("my-vote-false").addClass("my-vote-true");
            let currentUpVote = Number($("#my-up-vote-count" + postID).text());
            $("#my-up-vote-count" + postID).text(++currentUpVote);
            debugOutput("after click: " + $("#my-up-vote-count" + postID).text());
            if ($("#my-down-vote-button" + postID).attr("data-downvotestate") == "true") {
                let currentDownVote = Number($("#my-down-vote-count" + postID).text());
                $("#my-down-vote-count" + postID).text(--currentDownVote);
                $("#my-down-vote-button" + postID).attr("data-downvotestate", "false");
                $("#my-down-vote-button" + postID).removeClass("my-vote-true").addClass("my-vote-false");
            }

            myAjax({
                type: "PUT",
                url: backendUrl + "/api/posts/" + postID + "/vote?session=" + sessionKey,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "upVote": true,
                    "downVote": false
                }),
                success: function (result: any) {
                    debugOutput(result);
                }
            });

        }
    }

    private static onClickDownVote() {
        debugOutput("PostCommentBlock.downVotePost()");
        // we need to make a state check 
        let postID = $(this).data("postid");
        let upVoteState = $("#my-up-vote-button" + postID).attr("data-upvotestate");
        let downVoteState = $(this).attr("data-downvotestate");
        debugOutput("down vote button is clicked for post: " + postID,
            "up state: " + upVoteState,
            "down state: " + downVoteState);

        if (downVoteState == "true") {
            // user want to cancel the dislike 
            $(this).attr("data-downvotestate", "false");
            let currentDownVote = Number($("#my-down-vote-count" + postID).text());
            $("#my-down-vote-count" + postID).text(--currentDownVote);
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
            // both up vote and down vote are 0

            myAjax({
                type: "PUT",
                url: backendUrl + "/api/posts/" + postID + "/vote?session=" + sessionKey,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "upVote": false,
                    "downVote": false
                }),
                success: function (result: any) {
                    debugOutput(result);
                }
            });
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

            myAjax({
                type: "PUT",
                url: backendUrl + "/api/posts/" + postID + "/vote?session=" + sessionKey,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "upVote": false,
                    "downVote": true
                }),
                success: function (result: any) {
                    debugOutput(result);
                }
            });
        }
    }

    private static onClickCommentAddFile() {

    }

    private static onClickCommentAddLink() {

    }

    private static onClickSendComment() {

    }
}
