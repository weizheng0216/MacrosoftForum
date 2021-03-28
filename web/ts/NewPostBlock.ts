class NewPostBlock {
    
    private static isInit = false;


    /**
     * this function will initailize the new post block. 
     *  including add the html structure and click function
     * 
     * NOTE: NewPostBlock will not be initialized during the test since spec_running.html has
     *  already had this structure. 
     */
    private static init() {
        if(!testing){
            if (!NewPostBlock.isInit) {
                $("#right-part").append(Handlebars.templates["NewPostBlock.hb"]());
                $("#my-send-new-post-button").click(NewPostBlock.sendPost);
                $("#my-new-post-block").hide();
                NewPostBlock.isInit = true;
            }
        }else{
            if (!NewPostBlock.isInit) {
                $("#my-send-new-post-button").click(NewPostBlock.sendPost);
                $("#my-new-post-block").hide();
                NewPostBlock.isInit = true;
            }
        }

    }

    /**
     * This function will send the new post to the server. 
     * 
     * NOTE: this function will not be called during test, so no if(testing) statement here
     */
    private static sendPost(){
        var newTitle = $("#input-title").val();
        var newContent = $("#input-content").val();
        if(newTitle===""||newContent===""){
            alert("invalid input");
            return;
        }else{
            $.ajax({
                type: "POST",
                url: backendUrl + "/api/posts/"+BasicStructure.sessionKey,
                dataType: "json",
                data: JSON.stringify({"title": newTitle, "content": newContent}),
                success: function(result:any){
                    console.log(result);
                    BriefPostsList.refresh();
                    $("#my-new-post-block").hide();
                    $("#input-title").val("");
                    $("#input-content").val("");
                }
            });
        }
    }

    public static refresh(){
        this.init();
    }
}