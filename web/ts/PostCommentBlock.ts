class PostCommentBlock {

    /**
     * 
     * @param data 
     * NOTE: during testing, we do not need Handlebars to append div to html
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
      
    public static showImage() {
        let postID = $(this).data("value");
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
                $('#img').attr("src","data:"+imageType+";base64,"+result);
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

        if (!testing) {
            $(".post-comment-view").remove();
            $("#right-part").append(Handlebars.templates['PostCommentBlock.hb'](data));
        }

        $(".send-comment").click(PostCommentBlock.addnewComment);
        $(".my-down-vote-button").click(PostCommentBlock.downVotePost);
        $(".my-up-vote-button").click(PostCommentBlock.upVotePost);
        $(".user-button").click(PostCommentBlock.getOtherProfile);
        $(".post-comment-view").hide()
    }

    private static addnewComment() {
        let postID = $(this).data("value");
        let newContent = $("#my-new-comment-content" + postID).val();
        var newFileName = $("#file-title").val();
        if(newFileName.length<=0){
            newFileName=null;
        }
        var newFileType = PostCommentBlock.filetype;
        if(newFileType.length<=0){
            newFileType=null;
        }
        var newFileData = PostCommentBlock.filedata;
        if(newFileData.length<=0){
            newFileData=null;
        }
        var newLink = $('#id-link').text();
        if(newLink.length<=0){
            newLink=null;
        }
        console.log(newFileName);
        // link and file
        if (newContent === "") {
            alert("invalid input")
        } else {
            sessionStorage.setItem("currentPost", postID);
            $.ajax({
                type: "POST",
                url: backendUrl + "/api/posts/" + postID + "/comments?session=" + BasicStructure.sessionKey,
                dataType: "json",
                data: JSON.stringify({
                    "postID": postID, "content": newContent, "links" : "["+newLink+"]", "fileName":newFileName, "fileType":newFileType, "fileData":newFileData
                }),
                success: function (result: any) {
                    console.log(result);
                    BriefPostsList.refresh()
                    let postID = sessionStorage.getItem("currentPost");
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
    }

    private static downVotePost() {
        // we need to make a state check 
        let postID = $(this).data("postid");
        let upVoteState = $("#my-up-vote-button" + postID).attr("data-upvotestate");
        let downVoteState = $(this).attr("data-downvotestate");
        if (debug)
            console.log("down vote button is clicked for post: " + postID);
        if (debug)
            console.log("up state: " + upVoteState);
        if (debug)
            console.log("down state: " + downVoteState);

        if (downVoteState == "true") {
            // user want to cancel the dislike 
            $(this).attr("data-downvotestate", "false");
            let currentDownVote = $("#my-down-vote-count" + postID).text();
            $("#my-down-vote-count" + postID).text(--currentDownVote);
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
            // both up vote and down vote are 0

            // ONLY connect backend when not testing
            if (!testing) {
                $.ajax({
                    type: "PUT",
                    url: backendUrl + "/api/posts/" + postID + "/vote?session=" + BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 0, "downvote": 0
                    }),
                    success: function (result: any) {
                        if (debug)
                            console.log(result);
                    }
                });
            }
        } else {
            // user dislike this post
            $(this).attr("data-downvotestate", "true");
            $(this).removeClass("my-vote-false").addClass("my-vote-true");
            let currentDownVote = $("#my-down-vote-count" + postID).text();
            $("#my-down-vote-count" + postID).text(++currentDownVote);
            if ($("#my-up-vote-button" + postID).attr("data-upvotestate") == "true") {
                let currentUpVote = $("#my-up-vote-count" + postID).text();
                $("#my-up-vote-count" + postID).text(--currentUpVote);
                $("#my-up-vote-button" + postID).attr("data-upvotestate", "false");
                $("#my-up-vote-button" + postID).removeClass("my-vote-true").addClass("my-vote-false");
            }

            // ONLY connect backend when not testing
            if (!testing) {
                $.ajax({
                    type: "PUT",
                    url: backendUrl + "/api/posts/" + postID + "/vote?session=" + BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 0, "downvote": 1
                    }),
                    success: function (result: any) {
                        if (debug)
                            console.log(result);
                    }
                });
            }
        }
    }

    private static upVotePost() {
        // we need to make a state check 
        let postID = $(this).data("postid");
        let downVoteState = $("#my-down-vote-button" + postID).attr("data-downvotestate");
        let upVoteState = $(this).attr("data-upvotestate");
        if (debug)
            console.log("up vote button is clicked for post: " + postID);
        if (debug)
            console.log("up state: " + upVoteState);
        if (debug)
            console.log("down state: " + downVoteState);

        if (upVoteState == "true") {
            //user want to cancel the like
            $(this).attr("data-upvotestate", "false");
            let currentUpVote = $("#my-up-vote-count" + postID).text();
            $("#my-up-vote-count" + postID).text(--currentUpVote);
            $(this).removeClass("my-vote-true").addClass("my-vote-false");
            // both up vote and down vote are 0

            // ONLY connect backend when not testing
            if (!testing) {
                $.ajax({
                    type: "PUT",
                    url: backendUrl + "/api/posts/" + postID + "/vote?session=" + BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 0, "downvote": 0
                    }),
                    success: function (result: any) {
                        if (debug)
                            console.log(result);

                    }
                });
            }

        } else {
            //user like this post
            $(this).attr("data-upvotestate", "true");
            $(this).removeClass("my-vote-false").addClass("my-vote-true");
            let currentUpVote = $("#my-up-vote-count" + postID).text();
            $("#my-up-vote-count" + postID).text(++currentUpVote);
            if (debug)
                console.log("after click: " + $("#my-up-vote-count" + postID).text());
            if ($("#my-down-vote-button" + postID).attr("data-downvotestate") == "true") {
                let currentDownVote = $("#my-down-vote-count" + postID).text();
                $("#my-down-vote-count" + postID).text(--currentDownVote);
                $("#my-down-vote-button" + postID).attr("data-downvotestate", "false");
                $("#my-down-vote-button" + postID).removeClass("my-vote-true").addClass("my-vote-false");
            }

            // ONLY connect backend when not testing
            if (!testing) {
                $.ajax({
                    type: "PUT",
                    url: backendUrl + "/api/posts/" + postID + "/vote?session=" + BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 1, "downvote": 0
                    }),
                    success: function (result: any) {
                        if (debug)
                            console.log(result);
                    }
                });
            }

        }
    }

    /**
     * 
     * NOTE: during testing, we will not connect to the backend. We do not need any information about "this user"
     */
    private static getOtherProfile() {
        if (!testing) {

            var userID = $(this).data("value");
            if (debug)
                console.log("user click user: " + userID + ", request for detail");
            $.ajax({
                type: "GET",
                url: backendUrl + "/api/users/" + userID + "?session=" + BasicStructure.sessionKey,
                dataType: "json",
                data: JSON.stringify({ "sessionKey": BasicStructure.sessionKey }),
                success: function (result: any) {
                    if (debug)
                        console.log(result);
                    PostCommentBlock.showOtherProfile(result);
                }
            });
        } else {
            PostCommentBlock.showOtherProfile(" ");
        }

    }


    /**
     * 
     * @param data 
     * NOTE: during testing, we do not need to create other profile block. 
     */
    private static showOtherProfile(data: any) {
        if (!testing) {
            $(".other-user-profile-block").remove();
            $("#right-part").append(Handlebars.templates['OtherProfileBlock.hb'](data));
            $(".close-button").click(function () {
                $(".other-user-profile-block").remove();
            })
        } else {
            $(".other-user-profile-block").show();
            $(".close-button").click(function () {
                $(".other-user-profile-block").hide();
            })
        }

    }

   
}