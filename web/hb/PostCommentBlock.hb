
{{#each mData}}
    <div class="post-comment-view"  data-value="{{this.mPostID}}">
        <div class="post-comment-block">
            <div class="post-detail-block" value-postid="">
                <div class="title">
                    <!-- leave comment's title here -->
                    {{this.mTitle}}
                </div>
                <div class="content">
                    <!-- leave comment's content here -->
                    {{this.mContent}}
                </div>
                <div>
                    {{#each this.mLinks}}
                    <a id="postLink" href= "{{this}}" target="_blank" value="{{this}}" />
                     {{/each}}
                </div>
                <div class = "show-image" >
                    <input type="hidden" id = "imgType" value="{{this.mFileInfo.mType}}"> 
                    <img src ="" id = "img" />
                </div>
                <div class="lower-information">

                    <button id="my-down-vote-button{{this.mPostID}}" class="dislike-button little-button my-down-vote-button my-vote-{{mUserDownVote}}"
                        data-postid="{{this.mPostID}}" data-downvotestate="{{mUserDownVote}}">
                        <!-- leave down vote count of this post here -->
                        <span id="my-down-vote-count{{this.mPostID}}" class="glyphicon glyphicon-thumbs-down">{{this.mDownVote}}

                        </span>    
                    </button>

                    <button id="upload-button" class = "upload-button little-button"
                        onclick="$('.file-upload-input').trigger( 'click')">
                        <span     class = "glyphicon glyphicon-link">
                        </span>
                    </button>

                    <button id="my-up-vote-button{{this.mPostID}}" class="like-button little-button my-up-vote-button my-vote-{{mUserUpVote}}" 
                        data-postid="{{this.mPostID}}" data-upvotestate="{{mUserUpVote}}">
                        <!-- leave up vote count of this post here -->
                        <span id="my-up-vote-count{{this.mPostID}}" class="glyphicon glyphicon-thumbs-up">{{this.mUpVote}}
                            
                        </span>
                        
                    </button>

                    <button class="user-button little-button" data-value="{{this.mUserID}}">
                        <span class="glyphicon glyphicon-user"></span>
                        <!-- leave post's user here -->
                        {{this.mUserFirstName}} {{this.mUserLastName}}
                    </button>

                    <button class="date-button little-button">
                        <span class="glyphicon glyphicon-calendar"></span>
                        <!-- leave post's date here -->
                        {{this.mDate}}
                    </button>



                </div>
            </div>

            {{#each this.mComments}}
                <div class="comment-block" data-value="{{this.mCommentID}}">
                    <div class="content">
                        <!-- leave comment's content here -->
                        {{this.mContent}}
                    </div>

                    <div class="lower-information">

                        <button class="user-button little-button" data-value="{{this.mUserID}}">
                            <span class="glyphicon glyphicon-user"></span>
                            <!-- leave comment's user here -->
                            {{this.mFirstName}} {{this.mLastName}}
                        </button>

                        <button class="date-button little-button">
                            <span class="glyphicon glyphicon-calendar"></span>
                            <!-- leave commnet's post date here -->
                            {{this.mDate}}
                        </button>

                    </div>
                </div>
            {{/each}}
        </div>
        <div class="add-comment-block">
            <textarea id="my-new-comment-content{{this.mPostID}}" class="comment-input" placeholder="add a comment"></textarea>
            <button class="send-comment" data-value="{{this.mPostID}}">
                <span class="glyphicon glyphicon-send"></span>
                Send!
            </button>

            <button class= "insert-file"
                onclick="$('.insert-file-input').trigger( 'click')">
                <span class = "glyphicon glyphicon-file"></span>
                File
            </button>
            <input class="insert-file-input" id = "insert-file-input" type='file' onchange="PostCommentBlock.readFile(this);" accept="image/*, application/pdf" />
            <input class = "file-title" id = "file-title" />
            <img src ="" id = "img-id" />
     

            <div class = "link"> 
                <textarea class="insert-link" id="insert-link" placeholder="Link"></textarea>

                <button class = "link-insert" id = "link-insert" onclick="PostCommentBlock.AddLink();"
                    <span class = "glyphicon glyphicon-link"> </span>
                    Link
                </button>
            <a href = "" id = "id-link"> </a>
            </div>

        </div>
    </div>
{{/each}}