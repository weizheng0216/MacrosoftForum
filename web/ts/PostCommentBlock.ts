class PostCommentBlock {

    private static currPostId = -1;

    /**
     * Called by BreifPostsList.refresh().
     * Update all background post-comment views with new data.
     * @param data Backend response with img.
     */

    private static filedata = null;
    private static filetype = null;

  


    public static readFile(input) {
        
          if (input.files && input.files[0]) {
            var reader = new FileReader();
           

            $('#file-title').val(input.files && input.files.length ? input.files[0].name.split('.')[0] : '');
            PostCommentBlock.filetype = input.files && input.files.length ? input.files[0].type.split('.')[0] : '';

         reader.readAsBinaryString(input.files[0]);
        reader.onloadend = function(){
            console.log(reader.result);
            var encodedStr = btoa(reader.result);
            //var img = $('#img');
            //img.src = this.result;
            $('#img-id').attr("src","data:"+PostCommentBlock.filetype+";base64,"+encodedStr);
            PostCommentBlock.filedata = encodedStr;
            console.log(encodedStr);
            }
            //reader.readAsDataURL(document.getElementById('file').files[0]);
        } 
      }  
      
    public static showImage(postID) {
        //let postID = $(this).data("value");
        let imageType=$('#imgType').val();
        console.log("imageType", imageType);
        if(imageType=="image/jpg" || imageType=="image/png")
        {
        $.ajax({
            type: "GET",
            url: backendUrl + "/api/posts/" + postID + "/file?session=" + BasicStructure.sessionKey,
            dataType: "json",
            success: function (result: any) {
                console.log(result);
                if(result.mData.mData != undefined){
                $('#pcbimg').attr("src","data:"+imageType+";base64,"+result.mData.mData);
            }
            // else{
            if($('#pcbimg').src == "#"){
                $('#pcbimg').style.visibility = 'hidden';
            } 
            // 
            // }
            } 
        });
        }
        
    }

    public static AddLink()
    {
        var strLink=$('#insert-link').val();
        $('#id-link').attr('href',strLink);
        $('#id-link').text(strLink);
        $('#id-link').attr('target','_blank');
    }
    
    public static update(data: any) {
        debugOutput("PostCommentBlock.update()");

        // Remove old container from DOM
        $(".post-comment-block").remove();

        // Append the updated template
        $("#right-part").append(templatedHTML("PostCommentBlock", data));

        // Register events
        $(".post-flag-button").on("click", PostCommentBlock.onClickFlagPost);
        $(".comment-flag-button").on("click", PostCommentBlock.onClickFlagComment);
        $(".my-down-vote-button").on("click", PostCommentBlock.onClickDownVote);
        $(".my-up-vote-button").on("click", PostCommentBlock.onClickUpVote);
        $(".user-button").on("click", PostCommentBlock.onClickOthersProfile);
        $(".post-file-down-btn").on("click", PostCommentBlock.onClickPostDownloadFile);
        $(".comment-file-down-btn").on("click", PostCommentBlock.onClickCommentDownloadFile);

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
     * Enforece the view window on the right to show the PostCommentBlock and
     * display the post with the ID specified.  If ID is not provided, the
     * ID that's displayed last time will be used.
     * 
     * If target post does not exist, the PostCommentBlock will be hidden.
     * 
     * @param postId ID of the post to show.
     */
    public static showPost(postId?: number) {
        if (postId) {
            PostCommentBlock.currPostId = postId;
        } else {
            postId = PostCommentBlock.currPostId;
        }
        $("#my-new-post-block").hide();      // hide new post block
        $(".my-user-profile-block").hide();  // hide my profile block
        $(".post-comment-block").show();     // show post-comment-block
        $(".post-comment-view").hide();      // hide all views
        let targetView = $(format(".post-comment-view[data-value='{1}']", postId));
        if (targetView.html()) {  // only show when the view exists
            targetView.show();
        } else {  // otherwise hide the entire block again.
            $(".post-comment-block").hide();
        }
    }

    // ===================================================================
    // Events

    private static onClickFlagComment() {
        debugOutput("PostCommentBlock.onClickFlagComment()");

        let postID: any = PostCommentBlock.currPostId;
        let commentID = $(this).data("commentid");
        let oldState = $(this).attr("data-flagstate") == "true";

        // update DOM
        if (oldState) {
            // user wants to cancel the flag
            $(this).attr("data-flagstate", "false");
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
            let hasFlag = (function() {
                let commentFlags = document.getElementsByClassName("comment-flag-button");
                for (let i = 0; i < commentFlags.length; i++) {
                    let elem = commentFlags[i];
                    if (elem.getAttribute("data-flagstate") == "true" &&
                        elem.getAttribute("data-postid") == postID)
                        return true;
                }
                return $("#flag-button-" + postID).attr("data-flagstate") == "true";
            })();
            if (!hasFlag) $("#postlist-flag-" + postID).hide();
        } else {
            // user wants to flag the post
            $(this).attr("data-flagstate", "true");
            $(this).removeClass("my-vote-false").addClass("my-vote-true");
            $("#postlist-flag-" + postID).show();
        }
        // inform backend
        myAjax({
            type: "PUT",
            url: format("{1}/api/posts/{2}/comments/{3}/flag?session={4}",
                backendUrl, postID, commentID, sessionKey),
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "flagged": !oldState
            }),
            success: function (res: any) {
                debugOutput("[ajax] Comment flag click response: " + JSON.stringify(res));
            }
        });
    }

    private static onClickFlagPost() {
        debugOutput("PostCommentBlock.onClickFlagPost()");

        let postID: any = PostCommentBlock.currPostId;
        let oldState = $(this).attr("data-flagstate") == "true";

        // update DOM
        if (oldState) {
            // user wants to cancel the flag
            $(this).attr("data-flagstate", "false");
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
            let hasFlag = (function() {
                let commentFlags = document.getElementsByClassName("comment-flag-button");
                for (let i = 0; i < commentFlags.length; i++) {
                    let elem = commentFlags[i];
                    if (elem.getAttribute("data-flagstate") == "true" &&
                        elem.getAttribute("data-postid") == postID)
                        return true;
                }
                return false;
            })();
            if (!hasFlag) $("#postlist-flag-" + postID).hide();
        } else {
            // user wants to flag the comment
            $(this).attr("data-flagstate", "true");
            $(this).removeClass("my-vote-false").addClass("my-vote-true");
            $("#postlist-flag-" + postID).show();
        }
        // inform backend
        myAjax({
            type: "PUT",
            url: backendUrl + "/api/posts/" + postID + "/flag?session=" + sessionKey,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "flagged": !oldState
            }),
            success: function (res: any) {
                debugOutput("[ajax] Post flag click response: " + JSON.stringify(res));
            }
        });
    }

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
                fetchImgsComments(res.mData.mComments);
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

    private static onClickPostDownloadFile() {
        debugOutput("PostCommentBlock.onClickPostDownloadFile()");

        let id = PostCommentBlock.currPostId;
        let type = $("#file-panel-" + id).data("value");
        let name = $("#post-filename-" + id).html();

        (async function () {
            let base64: string = await new Promise(resolve => {
                if (/image\/\w/.test(type)) {
                    let src = $("#post-img-preview-"+id).attr("src");
                    resolve(src.replace(/^data:.+;base64,/, ""));
                } else {
                    // need to download from backend
                    myAjax({
                        type: "GET",
                        dataType: "json",
                        url: format("{1}/api/posts/{2}/file?session={3}",
                                    backendUrl, id, sessionKey),
                        success: function(res: any) {
                            let msg = JSON.stringify(res);
                            debugOutput("[ajax] File downloaded: " + msg);
                            resolve(res.mData.mData);
                        }
                    });
                }
            });
            downloadFile(base64, type, name);
        })();
    }

    private static onClickCommentDownloadFile() {
        debugOutput("PostCommentBlock.onClickCommentDownloadFile()");

        let postid = PostCommentBlock.currPostId;
        let commentid = $(this).data("commentid");
        let type = $(format("#file-panel-{1}-{2}", postid, commentid)).data("value");
        let name = $(format("#comment-filename-{1}-{2}", postid, commentid)).text();

        (async function () {
            let base64: string = await new Promise(resolve => {
                if (/image\/\w/.test(type)) {
                    let src = $(format("#comment-img-preview-{1}-{2}",
                        postid, commentid)).attr("src");
                    resolve(src.replace(/^data:.+;base64,/, ""));
                } else {
                    // need to download from backend
                    myAjax({
                        type: "GET",
                        dataType: "json",
                        url: format("{1}/api/posts/{2}/comments/{3}/file?session={4}",
                                    backendUrl, postid, commentid, sessionKey),
                        success: function(res: any) {
                            let msg = JSON.stringify(res);
                            debugOutput("[ajax] File downloaded: " + msg);
                            resolve(res.mData.mData);
                        }
                    });
                }
            });
            downloadFile(base64, type, name);
        })();
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
        let content: any = $("#comment-content-input").val();
        let rawLinks: any = $("#comment-links").val();  // any: avoid compiler error
        let fileName = file ? file.name : "";
        let fileType = file ? file.type : "";
        let fileData = "";  // base64 string for file content

        // Length check
        if (!content) return alertOutput("Missing comment body.");
        if (content.length > 500) return alertOutput("Content too long.");
        if (rawLinks.length > 500) return alertOutput("Links too long.");
        if (fileName.length > 100) return alertOutput("File name too long.");

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
