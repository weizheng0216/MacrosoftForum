class BriefPostsList {

    /**
     * this function is usually called when user make a DELETE/POST/PUT to backend in order to 
     *  ensure all the posts and comments are up to date. 
     * 
     * In and ONLY in this function, the value of sessionKey will be checked. If the session key 
     *  is "", which means the user may visit the index.html directly without login, the application 
     *  will redirect the user to login.html.
     * 
     * After receive the response, the result will be update to 1) brief post list(which is the 
     *  list of all posts on the left) 2) post Comment Block (which is the detail of posts and comments
     *  on the right) NOTE that ALL POST AND COMMMENT in the post comment block will be load at once.
     * 
     * NOTE: only connect to backend when we are not testing. spec_runner already have post examples
     */
    static refresh() { 
        
        if(!testing){

            if(debug){
                console.log("current section key: "+BasicStructure.sessionKey);
            }
                
            if(BasicStructure.sessionKey === ""){
                alert("please login");
                window.location.replace(backendUrl+"/login.html");
            }else{
                if(debug){
                    console.log("BriefPostsList.refresh() called")
                }
                $.ajax({
                    type: "GET",
                    url: backendUrl + "/api/posts?session="+ BasicStructure.sessionKey,
                    dataType: "json",
                    success: function(result:any){
                        if(debug){
                            console.log(result);
                        }
                        BriefPostsList.update(result);
                        PostCommentBlock.update(result);
                    },
                    error: function(){
                        alert("Login timeout, please login again");
                        window.location.replace(backendUrl+"/login.html");
                    }
                });
            }
        }else{
            //testing.
            BriefPostsList.update("");
            PostCommentBlock.update("");
        }

    }

    /**
     * this function will load the brief post block using the data passed by refresh(). This function 
     *  will ONLY be called by refresh(). 
     * 
     * NOTE: during testing, we do not need Handlebars to append div to html
     */
    private static update(data:any){

        if(!testing){
            // it is important to REMOVE all "post-brief-block" when new posts are added, or there 
                // will be more than one same post on the left, which is not what we want
            $(".post-brief-block").remove();

            // using handlebars, data will be processed and added to left part
            $("#left-part").append(Handlebars.templates['BriefPostsList.hb'](data));
        }
        
        // this line is to add click function that when user click a specific post on the left
            // the corresponding post's detail will be showed on the right
        $(".post-brief-block").click(BriefPostsList.showDetail);

    }

    /**
     * this function will only be called when a button from class "post-brief-block" is clicked. 
     *  it will hide ALL div in the right view and then show the corresponding post detail. 
     */
    private static showDetail(){
        // get the post id to be showed. The post id is stored in data-value attribute of the button
        let postID = $(this).data("value");
        if(debug){
            console.log("user clicked post "+postID+", show post detail on the right");
        }  
        // no matter new post interface is showed or not, hide it. 
        $("#my-new-post-block").hide();

        // hide all post comment block. 
        $(".post-comment-view").hide();

        $(".my-user-profile-block").hide();
        // get the specific post comment block by post id because each post comment block 
            // has a data-value={{postid}} attribute, which is unique. 
        $(".post-comment-view[data-value='"+postID+"']").show();
    }

}