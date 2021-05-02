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

    public static refresh() {
        debugOutput("NewPostBlock.refresh()");
        this.init();
    }

    // ===================================================================
    // Events

    private static onChangeAddFile() {
        debugOutput("NewPostBlock.onClickAddFile()");

        let htmFileInput: any = document.getElementById("post-fileupload-input");
        let htmImg: any = document.getElementById("post-fileupload-imgpreview");
        let htmName: any = document.getElementById("post-fileupload-name");
        let file = htmFileInput.files[0];
        // Setup reader
        let reader = new FileReader();
        reader.onloadend = function(e) {
            htmImg.style.display = "block";
            htmImg.src = e.target.result.toString();
        };
        // Load file
        if (/image\/\w/.test(file.type)) {
            // only load file if it's an image
            reader.readAsDataURL(file);
        } else {
            // otherwise make sure the preview is hidden
            htmImg.style.display = "none";
        }
        htmName.innerHTML = file.name;  // Update the filename entry
    }

    private static onClickRemoveFile() {
        debugOutput("NewPostBlock.onClickRemoveFile()");

        let htmInput: any = document.getElementById("post-fileupload-input");
        let htmImg: any = document.getElementById("post-fileupload-imgpreview");
        let htmName: any = document.getElementById("post-fileupload-name");
        htmInput.value = null;  // clear selection
        htmImg.style.display = "none";  // hide preview
        htmName.innerHTML = "No file selected";  // reset filename entry
    }

    private static onClickAddVideo() {
        debugOutput("NewPostBlock.onClickAddVideo()");

    }

    private static onClickSendPost() {
        debugOutput("NewPostBlock.onClickSendPost()");
        let htmFileInput: any = document.getElementById("post-fileupload-input");
        let file = htmFileInput.files ? htmFileInput.files[0] : null;

        // Data to be included in request Json
        let title: any = $("#input-title").val();  // any: avoid compiler error
        let content: any = $("#input-content").val();
        let rawLinks: any = $("#input-links").val();
        let fileName = file ? file.name : "";
        let fileType = file ? file.type : "";
        let fileData = "";  // base64 string for file content

        // Length check
        if (!title || !content) return alertOutput("Missing title or body.");
        if (title.length > 100) return alertOutput("Title too long.");
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
                url: backendUrl + "/api/posts?session=" + sessionKey,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "title": title,
                    "content": content,
                    "links": rawLinks.split(/\s+/),
                    "fileName": fileName,
                    "fileType": fileType,
                    "fileData": fileData
                }),
                success: function (res: any) {
                    debugOutput("[ajax] New Post Response: " + JSON.stringify(res));
                    // Refresh DOM upon success
                    $("#my-new-post-block").hide();
                    $("#input-title").val("");
                    $("#input-content").val("");
                    $("#input-links").val("");
                    NewPostBlock.onClickRemoveFile();
                    BriefPostsList.refresh();
                }
            });
        })();
    }
}