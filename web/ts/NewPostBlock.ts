class NewPostBlock {

    private static isInit = false;

    private static strbase64 = null;
    private static imagetype = null;

    /**
     * this function will initailize the new post block. 
     *  including add the html structure and click function
     * 
     * NOTE: NewPostBlock will not be initialized during the test since spec_running.html has
     *  already had this structure. 
     */
    private static init() {
        debugOutput("NewPostBlock.init()");
        if (!NewPostBlock.isInit) {
            $("#right-part").append(Handlebars.templates["NewPostBlock.hb"]());
            //$("#file-upload-btn").on("click", NewPostBlock.readURL);
            $("#my-send-new-post-button").on("click", NewPostBlock.sendPost);
            $("#my-new-post-block").hide();
            NewPostBlock.isInit = true;
        }
    }

    /**
     * This function will send the new post to the server. 
     * 
     * NOTE: this function will not be called during test, so no if(testing) statement here
     */

    public static AddLink() {
        debugOutput("NewPostBlock.AddLink()");
        var strLink = $('#upload-link').val();
        $('#link-id').attr('href', strLink);
        $('#link-id').text(strLink);
        $('#link-id').attr('target', '_blank');
    }

    public static readURL(input) {
        debugOutput("NewPostBlock.readURL()");
        if (input.files && input.files[0]) {

            var reader = new FileReader();

            //$('#title').val(this.value ? this.value.match(/([\w-_]+)(?=\.)/)[0] : '');
            $('#image-title').val(input.files && input.files.length ? input.files[0].name.split('.')[0] : '');
            NewPostBlock.imagetype = input.files && input.files.length ? input.files[0].type.split('.')[0] : '';



            reader.readAsBinaryString(input.files[0]);
            reader.onloadend = function () {
                debugOutput(reader.result);
                var encodedStr = btoa(reader.result);
                //var img = $('#img');
                //img.src = this.result;
                $('#img').attr("src", "data:" + NewPostBlock.imagetype + ";base64," + encodedStr);
                NewPostBlock.strbase64 = encodedStr;
                debugOutput(encodedStr);
            }
            //reader.readAsDataURL(document.getElementById('file').files[0]);
        }
        // else {
        //   NewPostBlock.removeUpload();
        // }
    }




    private static sendPost() {
        debugOutput("NewPostBlock.sendPost()");
        var newTitle = $("#input-title").val();
        var newContent = $("#input-content").val();
        //var newFile = $("#file-upload-image").val();
        var newFileName = $("#image-title").val();
        if (newFileName.length <= 0) {
            newFileName = null;
        }
        var newFileType = NewPostBlock.imagetype;
        if (newFileType == null) {
            newFileType = null;
        }
        var newFileData = NewPostBlock.strbase64;
        if (newFileData == null) {
            newFileData = null;
        }

        //var links[];

        var newLink = $('#upload-link').val();
        if (newLink.length <= 0) {
            newLink = null;
        }

        console.log(newFileName);
        if (!newTitle || !newContent) {
            alertOutput("invalid input");
            return;
        } else {
            myAjax({
                type: "POST",
                url: backendUrl + "/api/posts?session=" + sessionKey,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "title": newTitle, "content": newContent, "links": [newLink], "fileName": newFileName, "fileType": newFileType, "fileData": newFileData
                }),
                success: function (result: any) {
                    debugOutput(result);
                    BriefPostsList.refresh();
                    $("#my-new-post-block").hide();
                    $("#input-title").val("");
                    $("#input-content").val("");
                    $('#img').attr("src", "");

                }
            });
        }
    }

    public static refresh() {
        debugOutput("NewPostBlock.refresh()");
        this.init();
    }








    // ===================================================================
    // Events

    private static onClickAddFile() {

    }

    private static onClickAddLink() {

    }

    private static onClickAddVideo() {
        
    }

    private static onClickSendPost() {

    }
}