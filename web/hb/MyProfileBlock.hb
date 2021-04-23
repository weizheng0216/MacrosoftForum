{{#with mData}}
<div class="my-user-profile-block">
    <h4 style="margin-top: 30px;">First Name:</h4>
    <p>{{this.mUser.mFirstName}}</p>
    <h4>Last Name:</h4>
    <p>{{this.mUser.mLastName}}</p>
    <h4>E-mail:</h4>
    <p>{{this.mUser.mEmail}}</p>

    {{#each this.mPosts}}
        <h4>Post: </h4>
        <h5>title: </h5>
        <textarea name="" id="post-title{{this.mPostID}}" cols="30" rows="3" class="edit-place">{{this.mTitle}}</textarea>
        <h5>content: </h5>
        <textarea name="" id="post-content{{this.mPostID}}" cols="30" rows="5" class="edit-place">{{this.mContent}}</textarea>
        <button class="post-update-button edit-button" data-value="{{this.mPostID}}">
            <span class="glyphicon glyphicon-upload"></span>
            update
        </button>

        <button class="post-delete-button edit-button"  data-value="{{this.mPostID}}">
            <span class="glyphicon glyphicon-trash"></span>
            delete
        </button>
    {{/each}}
     
    {{#each this.mComments}}
        <h4>Comment: </h4>
        <h5>content: </h5>
        <textarea name="" id="comment-content{{this.mCommentID}}" cols="30" rows="5" class="edit-place">{{this.mContent}}</textarea>
        <button class="comment-update-button edit-button" data-value="{{this.mCommentID}}" data-postid="{{this.mPostID}}">
            <span class="glyphicon glyphicon-upload"></span>
            update
        </button>
        <button class="comment-delete-button edit-button" data-value="{{this.mCommentID}}" data-postid="{{this.mPostID}}">
            <span class="glyphicon glyphicon-trash"></span>
            delete
        </button>
    {{/each}}
</div>
{{/with}}
