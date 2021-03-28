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

                <div class="lower-information">

                    <button id="my-down-vote-button{{this.mPostID}}" class="dislike-button little-button my-down-vote-button my-vote-{{mUserDownVote}}"
                        data-postid="{{this.mPostID}}" data-downvotestate="{{mUserDownVote}}">
                        <!-- leave down vote count of this post here -->
                        <span id="my-down-vote-count{{this.mPostID}}" class="glyphicon glyphicon-thumbs-down">{{this.mDownVote}}
                            
                            
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
        </div>
    </div>
{{/each}}