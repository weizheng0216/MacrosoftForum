<div class="container " id= "list">
    <div class="card-columns justify-content-sm-center" id="ElementList">
        {{#each mData}}
        {{#if this.mPinned}}
            <div class="card text-white bg-warning mb-3" style="max-width: 18rem;">   
                <div class="card-header">
                    <i class="fas fa-thumbtack"></i> {{this.mDate}}
                </div>         
                <div class="card-body">
                    <h5 class="card-title">
                        {{this.mTitle}}
                    </h5>
                    <p class="card-text">{{this.mContent}}</p>
                    <p class="card-text"><small class="text-white">By {{this.mUserName}}</small></p>
                    <div class="container">
                        <div class = "row justify-content-sm-center">
                            <div class=".col-sm-3">
                                <button class="upbtn" data-value="{{this.mID}}">
                                    <i class="far fa-heart"></i>  {{this.mUpVote}} 
                                </button>
                            </div>
                            <div class = "offset-md-2 .col-sm-3">
                                <button class="downbtn" data-value="{{this.mID}}">
                                    <i class="far fa-thumbs-down"></i> {{this.mDownVote}} 
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        {{else}}
            <div class="card bg-light mb-3" style="max-width: 18rem;">   
                <div class="card-header">
                    {{this.mDate}}
                </div>         
                <div class="card-body">
                    <h5 class="card-title">
                        {{this.mTitle}}
                    </h5>
                    <p class="card-text">{{this.mContent}}</p>
                    <p class="card-text"><small class="text-muted">By {{this.mUserName}}</small></p>
                    <div class="container">
                        <div class = "row justify-content-sm-center">
                            <div class=".col-sm-3">
                                <button class="upbtn" data-value="{{this.mID}}">
                                    <i class="far fa-heart"></i>  {{this.mUpVote}} 
                                </button>
                            </div>
                            <div class = "offset-md-2 .col-sm-3">
                                <button class="downbtn" data-value="{{this.mID}}">
                                    <i class="far fa-thumbs-down"></i> {{this.mDownVote}} 
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        {{/if}}
        {{/each}}
    </div>
</div>




