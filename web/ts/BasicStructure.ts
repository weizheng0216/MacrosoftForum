class BasicStructure{

    public static sessionKey:string;

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
     *              this funciton will send "GET api/posts" request to the backend and get all lists
     *              and load the response to the screen etc. 
     *      3) it will initialize the NewPostBlock, which enable user to create a new post. 
     */
    static init() {
        if (!BasicStructure.isInit) {
            // add the basix structure to the html
            $(".container").append(Handlebars.templates["BasicStructure.hb"]());
            $(".new-post-button").click(BasicStructure.showNewPostBlock);
            $(".my-profile-button").click(BasicStructure.showMyProfileBlock);
            $("#sign-out").click(BasicStructure.signOut)
            BriefPostsList.refresh();

            // initial new post block
            NewPostBlock.refresh();

            BasicStructure.isInit = true;
        }
    }

    private static signOut(){
        alert("sign out successfully");
        window.location.replace(backendUrl+"/login.html");
    }

    /**
     * this function is called when "new post" block is clicked
     * during this function:
     *      1) hide all post-comment-view 
     *      2) show new post block
     * 
     * Since NewPostBlock has aleady initialize in init(), we do not need to initialize it again
     */
    private static showNewPostBlock(){
        
        $(".post-comment-view").hide();
        $("#my-new-post-block").show();
        $(".post-comment-view").hide();
        $(".my-user-profile-block").hide();

        // remember to hide other user's profile, if has one
        if(!testing){
            $(".other-user-profile-block").remove();
        }else{
            $(".other-user-profile-block").hide();
        }
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
    private static showMyProfileBlock(){
        $(".post-comment-view").hide();
        $("#my-new-post-block").hide();
        if(!testing){
            $(".other-user-profile-block").remove();
        }else{
            $(".other-user-profile-block").hide();
        }
        
        MyProfileBlock.refresh();
    }

}