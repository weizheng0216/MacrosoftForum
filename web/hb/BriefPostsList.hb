{{#each mData}}
<div class="post-brief-block" data-value="{{this.mPostID}}">
    <div class="upper-information">
        <div class="title">
            <!-- leave post title here -->
            {{this.mTitle}}
        </div>

        {{#if this.mFlagged}}
            <div class="glyphicon glyphicon-exclamation-sign flag-icon">
                <span class="bad">BAD</span>
            </div>
        {{/if}}
    </div>

    <div class="content">
        <!-- leave post content here -->
        {{this.mContent}}
    </div>

</div>
{{/each}}