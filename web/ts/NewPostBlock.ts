class NewPostBlock {

    private static isInit = false;

    /**
     * This function will initailize the new post block, which includes adding the
     * DOM nodes and registering click events.
     */
    private static init() {
        debugOutput("NewPostBlock.init()");
        if (!NewPostBlock.isInit) {
            $("#right-part").append(templatedHTML("NewPostBlock"));
            //$("#file-upload-btn").on("click", NewPostBlock.readURL);
            $("#post-fileupload-input").on("change", NewPostBlock.onChangeAddFile);
            $("#post-fileupload-remove").on("click", NewPostBlock.onClickRemoveFile);
            $("#my-send-new-post-button").on("click", NewPostBlock.onClickSendPost);
            $("#my-new-post-block").hide();
            NewPostBlock.isInit = true;
        }
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

    private static onChangeAddFile() {
        debugOutput("NewPostBlock.onClickAddFile()");

    }

    private static onClickRemoveFile() {
        debugOutput("NewPostBlock.onClickRemoveFile()");
    }

    private static onClickAddVideo() {
        debugOutput("NewPostBlock.onClickAddVideo()");

    }

    private static onClickSendPost() {
        debugOutput("NewPostBlock.onClickSendPost()");
    }
}