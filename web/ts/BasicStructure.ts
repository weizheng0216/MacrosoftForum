class BasicStructure {

    private static isInit = false;

    /**
     * This function will be called when the html is initialized. 
     * 
     * After the first time it is called, isInit will be set true so that nothing will happened
     *  if this function is called again.
     * 
     * This function initialize all basic structure and block of the index.html page. 
     *      1) it will initialize the banner, add functions to button
     *      2) it will refresh the BriefPostList. Note that BriefPostList have not been initalized, 
     *         this funciton will send "GET api/posts" request to the backend and get all lists
     *         and load the response to the screen etc. 
     *      3) it will initialize the NewPostBlock, which enable user to create a new post. 
     */
    private static init() {
        debugOutput("BasicStructure.init()");
        if (!BasicStructure.isInit) {
            // add the basix structure to the html
            $("body").append(templatedHTML("BasicStructure"));
            $(".new-post-button").on("click", BasicStructure.onClickNewPost);
            $(".my-profile-button").on("click", BasicStructure.onClickMyProfile);
            $("#sign-out").on("click", BasicStructure.onClickSignOut)
            BriefPostsList.refresh();   
            NewPostBlock.refresh();
            BasicStructure.isInit = true;
        }
    }

    public static refresh() {
        debugOutput("BasicStructure.refresh()");
        BasicStructure.init();
    }

    // ===================================================================
    // Events

    private static onClickSignOut() {
        debugOutput("BasicStructure.signOut()");
        alertOutput("sign out successfully");
        redirect("login.html");
    }

    /**
     * this function is called when "new post" block is clicked
     * during this function:
     *      1) hide all post-comment-view 
     *      2) show new post block
     * 
     * Since NewPostBlock has aleady initialize in init(), we do not need to initialize it again
     */
    private static onClickNewPost() {
        debugOutput("BasicStructure.showNewPostBlock()");
        
        $(".post-comment-block").hide();     // hide post-comment-block
        $(".my-user-profile-block").hide();  // hide my-profile-block
        $("#my-new-post-block").show();      // show new-post-block

        // remember to hide other user's profile, if there is one
        $(".other-user-profile-block").remove();
    }

    /**
     * This function is called when "my profile" button is clicked
     *  this fucntion will do:
     *      1) hide the post-comment-view
     *      2) hide new post interface
     *      3) remove other user info interface
     * 
     * IMPORTANT: since "my profile" may different from previous call (because the user may have new posts
     *  and comments). We will call refresh() each time to ensure "my profile" is up to date.
     */
    private static onClickMyProfile() {
        debugOutput("BasicStructure.showMyProfileBlock()");

        $("#my-new-post-block").hide();     // hide new-post-block
        $(".post-comment-block").hide();    // hide post-comment-block
        
        $(".other-user-profile-block").remove();
        MyProfileBlock.refresh();
    }

}