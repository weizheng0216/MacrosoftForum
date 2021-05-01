class BriefPostsList {

    /**
     * This function is constantly called to make sure all the posts and
     * comments are up to date. 
     * 
     * After recieving the response, the result will be update to 1) BriefPostList
     * (which is the list of all posts on the left); 2) PostCommentBlock (which is
     * the main view of posts and comments on the right).
     * 
     * NOTE that ALL POSTs AND COMMMENTs in the PostCommentBlock will be load at
     * after this call.
     */
    static refresh() { 
        debugOutput("BriefPostList.refresh()");

        myAjax({
            type: "GET",
            url: backendUrl + "/api/posts?session=" + sessionKey,
            dataType: "json",
            success: function(res: any) {
                debugOutput("[ajax] All Posts Response: " + JSON.stringify(res));
                fetchImgs(res.mData);
                BriefPostsList.update(res);
                PostCommentBlock.update(res);
                PostCommentBlock.showPost();
            },
            error: function() {
                alertOutput("Login timeout, please login again");
                redirect("login.html");
            }
        });
    }

    /**
     * This function loads the BriefPostBlock with the data passed by refresh().
     * This function will ONLY be called by refresh(). 
     * 
     * @param data StructuredResponse(with img data) from backend.
     */
    private static update(data: any){
        debugOutput("BriefPostList.update()");

        // remove the exising list
        $(".post-brief-block").remove();

        // append new list using handlebar and the data passed
        $("#left-part").append(templatedHTML("BriefPostsList", data));
        
        // register click events on the list
        $(".post-brief-block").on("click", BriefPostsList.onClickBriefPost);
    }

    // ===================================================================
    // Events

    /**
     * A post in the list is clicked. We should display its detail on the right.
     */
    private static onClickBriefPost() {
        debugOutput("BriefPostList.onClickBriefPost()");

        let postID = $(this).data("value");
        PostCommentBlock.showPost(postID);
    }

}