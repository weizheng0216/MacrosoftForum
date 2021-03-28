{{#each mData}}
<div class="post-brief-block" data-value="{{this.mPostID}}">
    <div class="upper-information">

        <div class="title">
            <!-- leave post title here -->
            {{this.mTitle}}
        </div>

        <div class="date">
            <!-- leave post date here -->
            {{this.mDate}}
        </div>
        {{#if this.mPinned}}
            <span class="glyphicon glyphicon-pushpin pin-icon"></span>
        {{/if}}
    </div>

    <div class="content">
        <!-- leave post content here -->
        {{this.mContent}}
    </div>

</div>
{{/each}}