{{#with mData}}
<div class="other-user-profile-block">
    <span class="glyphicon glyphicon-remove close-button"></span>

    <div class="user-information">
        <h4>First Name:</h3>
        <p>{{this.mUser.mFirstName}}</p>
        <h4>Last Name:</h3>
        <p>{{this.mUser.mLastName}}</p>
        <h4>E-mail:</h3>
        <p>{{this.mUser.mEmail}}</p>
        
        {{#each this.mPosts}}
            <h4>Post: </h4>

            <h5>Title: </h5>
            <p>{{this.mTitle}}</p>

            <h5>Content: </h5>
            <p>{{this.mContent}}</p>
        {{/each}}

        {{#each this.mContent}}
            <h5>Comment: </h5>
            <p>{{this.mContent}}</p>
        {{/each}}

    </div>
</div>
{{/with}}