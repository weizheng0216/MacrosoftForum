class PostCommentBlock {

    /**
     * 
     * @param data 
     * NOTE: during testing, we do not need Handlebars to append div to html
     */
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
        if (newContent === "") {
            alert("invalid input")
        } else {
            sessionStorage.setItem("currentPost", postID);
            $.ajax({
                type: "POST",
                url: backendUrl + "/api/posts/" + postID + "/comments/"+BasicStructure.sessionKey,
                dataType: "json",
                data: JSON.stringify({
                    "postID": postID, "content": newContent
                }),
                success: function (result: any) {
                    console.log(result);
                    BriefPostsList.refresh()
                    let postID = sessionStorage.getItem("currentPost");
                    sessionStorage.removeItem("currentPost");
                    console.log("this postID: " + postID);
                    console.log("this postID2: " + postID);
                    $("#my-new-comment-content" + postID).val("");
                    console.log("length: " + $(".post-comment-view[data-value='" + postID + "']").length);
                    $(".post-comment-view[data-value='" + postID + "']").show();

                }
            });
        }
        console.log("user add new comment under post: " + postID);
        console.log("user send new comment: " + newContent);
    }

    private static downVotePost() {
        // we need to make a state check 
        let postID = $(this).data("postid");
        let upVoteState = $("#my-up-vote-button" + postID).attr("data-upvotestate");
        let downVoteState = $(this).attr("data-downvotestate");
        console.log("down vote button is clicked for post: " + postID);
        console.log("up state: " + upVoteState);
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
                    url: backendUrl + "/api/posts/" + postID + "/vote/"+BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 0, "downovte": 0
                    }),
                    success: function (result: any) {
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
                    url: backendUrl + "/api/posts/" + postID + "/vote/"+BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 0, "downovte": 1
                    }),
                    success: function (result: any) {
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
        console.log("up vote button is clicked for post: " + postID);
        console.log("up state: " + upVoteState);
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
                    url: backendUrl + "/api/posts/" + postID + "/vote/"+BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 0, "downovte": 0
                    }),
                    success: function (result: any) {
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
                    url: backendUrl + "/api/posts/" + postID + "/vote/"+BasicStructure.sessionKey,
                    dataType: "json",
                    data: JSON.stringify({
                        "upvote": 1, "downovte": 0
                    }),
                    success: function (result: any) {
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
            console.log("user click user: "+userID+", request for detail");
            $.ajax({
                type: "GET",
                url: backendUrl + "/api/users/" + userID+"/"+ BasicStructure.sessionKey,
                dataType: "json",
                // data: JSON.stringify({ "sessionKey": BasicStructure.sessionKey }),
                success: function (result: any) {
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