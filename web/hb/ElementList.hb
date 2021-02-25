
<div class="card-columns id="ElementList"">
    {{#each mData}}
    <div class="card" style="width: 18rem;">
        <div class="card-body">
            <h5 class="card-title">{{this.mTitle}}</h5>
            <p class="card-text">{{this.mContent}}</p>
            <button class="upbtn" data-value="{{this.mID}}">Upvote</button>
            <button class="downbtn" data-value="{{this.mID}}">Downvote</button>
        </div>
    </div>
    {{/each}}
</div>



