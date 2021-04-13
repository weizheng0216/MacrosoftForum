<div class="new-post-block" id="my-new-post-block">
    <div class="title">
        Title
        <textarea class="input-title" id="input-title" placeholder="write your title here"></textarea>
    </div>

    <div class="content">
        Content
        <textarea class="input-content" id="input-content" placeholder="write your content here"></textarea>
    </div>

    <div class = "file-upload">
        <button class = "file-upload-button" id = "file-upload-button"
        onclick="$('.file-upload-input').trigger( 'click')">
	        <span class = "glyphicon glyphicon-upload"> </span>
            Add File
        </button>

        <input class="file-upload-input" id = "file-upload-input" type='file' onchange="NewPostBlock.readURL(this);" accept="image/*, application/pdf" />
        <input id = "image-title" />
        <img src ="" id = "img" />
     
    

        <div class = "link"> 
            <textarea class="upload-link" id="upload-link" placeholder="write your link here"></textarea>

            <button class = "link-upload" id = "link-upload" onclick="NewPostBlock.AddLink();"
                <span class = "glyphicon glyphicon-link"> </span>
                Add Link
            </button>
            <a href = "" id = "link-id"> </a>
        </div>

        <button class="send-post" id="my-send-new-post-button">
            <span class="glyphicon glyphicon-send"></span>
            send
        </button>
    </div>
</div>