(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['NewPostBlock.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div class=\"new-post-block\" id=\"my-new-post-block\">\n    <div class=\"title\">\n        Title\n        <textarea class=\"input-title\" id=\"input-title\" placeholder=\"write your title here\"></textarea>\n    </div>\n\n    <div class=\"content\">\n        Content\n        <textarea class=\"input-content\" id=\"input-content\" placeholder=\"write your content here\"></textarea>\n    </div>\n\n    <div class = \"file-upload\">\n        <button class = \"file-upload-button\" id = \"file-upload-button\"\n        onclick=\"$('.file-upload-input').trigger( 'click')\">\n	        <span class = \"glyphicon glyphicon-upload\"> </span>\n            Add File\n        </button>\n\n        <input class=\"file-upload-input\" id = \"file-upload-input\" type='file' onchange=\"NewPostBlock.readURL(this);\" accept=\"image/*, application/pdf\" />\n        <input id = \"image-title\" />\n        <img src =\"\" id = \"img\" />\n     \n    \n\n        <div class = \"link\"> \n            <textarea class=\"upload-link\" id=\"upload-link\" placeholder=\"write your link here\"></textarea>\n\n            <button class = \"link-upload\" id = \"link-upload\" onclick=\"NewPostBlock.AddLink();\"\n                <span class = \"glyphicon glyphicon-link\"> </span>\n                Add Link\n            </button>\n            <a href = \"\" id = \"link-id\"> </a>\n        </div>\n\n        <button class=\"send-post\" id=\"my-send-new-post-button\">\n            <span class=\"glyphicon glyphicon-send\"></span>\n            send\n        </button>\n    </div>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['BriefPostsList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, alias1=container.lambda, alias2=container.escapeExpression, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "<div class=\"post-brief-block\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\">\n    <div class=\"upper-information\">\n\n        <div class=\"title\">\n            <!-- leave post title here -->\n            "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mTitle") : depth0), depth0))
    + "\n        </div>\n\n        <div class=\"date\">\n            <!-- leave post date here -->\n            "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mDate") : depth0), depth0))
    + "\n        </div>\n"
    + ((stack1 = lookupProperty(helpers,"if").call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? lookupProperty(depth0,"mPinned") : depth0),{"name":"if","hash":{},"fn":container.program(2, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":14,"column":8},"end":{"line":16,"column":15}}})) != null ? stack1 : "")
    + "    </div>\n\n    <div class=\"content\">\n        <!-- leave post content here -->\n        "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mContent") : depth0), depth0))
    + "\n    </div>\n\n</div>\n";
},"2":function(container,depth0,helpers,partials,data) {
    return "            <span class=\"glyphicon glyphicon-pushpin pin-icon\"></span>\n";
},"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return ((stack1 = lookupProperty(helpers,"each").call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? lookupProperty(depth0,"mData") : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":1,"column":0},"end":{"line":25,"column":9}}})) != null ? stack1 : "");
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['PostCommentBlock.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=container.lambda, alias2=container.escapeExpression, alias3=depth0 != null ? depth0 : (container.nullContext || {}), alias4=container.hooks.helperMissing, alias5="function", lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "    <div class=\"post-comment-view\"  data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\">\n        <div class=\"post-comment-block\">\n            <div class=\"post-detail-block\" value-postid=\"\">\n                <div class=\"title\">\n                    <!-- leave comment's title here -->\n                    "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mTitle") : depth0), depth0))
    + "\n                </div>\n                <div class=\"content\">\n                    <!-- leave comment's content here -->\n                    "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mContent") : depth0), depth0))
    + "\n                </div>\n                <div>\n"
    + ((stack1 = lookupProperty(helpers,"each").call(alias3,(depth0 != null ? lookupProperty(depth0,"mLinks") : depth0),{"name":"each","hash":{},"fn":container.program(2, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":15,"column":20},"end":{"line":17,"column":30}}})) != null ? stack1 : "")
    + "                </div>\n                <div class = \"show-image\" >\n                    <input type=\"hidden\" id = \"imgType\" value=\""
    + alias2(alias1(((stack1 = (depth0 != null ? lookupProperty(depth0,"mFileInfo") : depth0)) != null ? lookupProperty(stack1,"mType") : stack1), depth0))
    + "\"> \n                    <img src =\"\" id = \"img\" />\n                </div>\n                <div class=\"lower-information\">\n\n                    <button id=\"my-down-vote-button"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\" class=\"dislike-button little-button my-down-vote-button my-vote-"
    + alias2(((helper = (helper = lookupProperty(helpers,"mUserDownVote") || (depth0 != null ? lookupProperty(depth0,"mUserDownVote") : depth0)) != null ? helper : alias4),(typeof helper === alias5 ? helper.call(alias3,{"name":"mUserDownVote","hash":{},"data":data,"loc":{"start":{"line":25,"column":133},"end":{"line":25,"column":150}}}) : helper)))
    + "\"\n                        data-postid=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\" data-downvotestate=\""
    + alias2(((helper = (helper = lookupProperty(helpers,"mUserDownVote") || (depth0 != null ? lookupProperty(depth0,"mUserDownVote") : depth0)) != null ? helper : alias4),(typeof helper === alias5 ? helper.call(alias3,{"name":"mUserDownVote","hash":{},"data":data,"loc":{"start":{"line":26,"column":75},"end":{"line":26,"column":92}}}) : helper)))
    + "\">\n                        <!-- leave down vote count of this post here -->\n                        <span id=\"my-down-vote-count"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\" class=\"glyphicon glyphicon-thumbs-down\">"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mDownVote") : depth0), depth0))
    + "\n\n                        </span>    \n                    </button>\n\n                    <button id=\"upload-button\" class = \"upload-button little-button\"\n                        onclick=\"$('.file-upload-input').trigger( 'click')\">\n                        <span     class = \"glyphicon glyphicon-link\">\n                        </span>\n                    </button>\n\n                    <button id=\"my-up-vote-button"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\" class=\"like-button little-button my-up-vote-button my-vote-"
    + alias2(((helper = (helper = lookupProperty(helpers,"mUserUpVote") || (depth0 != null ? lookupProperty(depth0,"mUserUpVote") : depth0)) != null ? helper : alias4),(typeof helper === alias5 ? helper.call(alias3,{"name":"mUserUpVote","hash":{},"data":data,"loc":{"start":{"line":39,"column":126},"end":{"line":39,"column":141}}}) : helper)))
    + "\" \n                        data-postid=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\" data-upvotestate=\""
    + alias2(((helper = (helper = lookupProperty(helpers,"mUserUpVote") || (depth0 != null ? lookupProperty(depth0,"mUserUpVote") : depth0)) != null ? helper : alias4),(typeof helper === alias5 ? helper.call(alias3,{"name":"mUserUpVote","hash":{},"data":data,"loc":{"start":{"line":40,"column":73},"end":{"line":40,"column":88}}}) : helper)))
    + "\">\n                        <!-- leave up vote count of this post here -->\n                        <span id=\"my-up-vote-count"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\" class=\"glyphicon glyphicon-thumbs-up\">"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mUpVote") : depth0), depth0))
    + "\n                            \n                        </span>\n                        \n                    </button>\n\n                    <button class=\"user-button little-button\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mUserID") : depth0), depth0))
    + "\">\n                        <span class=\"glyphicon glyphicon-user\"></span>\n                        <!-- leave post's user here -->\n                        "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mUserFirstName") : depth0), depth0))
    + " "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mUserLastName") : depth0), depth0))
    + "\n                    </button>\n\n                    <button class=\"date-button little-button\">\n                        <span class=\"glyphicon glyphicon-calendar\"></span>\n                        <!-- leave post's date here -->\n                        "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mDate") : depth0), depth0))
    + "\n                    </button>\n\n\n\n                </div>\n            </div>\n\n"
    + ((stack1 = lookupProperty(helpers,"each").call(alias3,(depth0 != null ? lookupProperty(depth0,"mComments") : depth0),{"name":"each","hash":{},"fn":container.program(4, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":65,"column":12},"end":{"line":88,"column":21}}})) != null ? stack1 : "")
    + "        </div>\n        <div class=\"add-comment-block\">\n            <textarea id=\"my-new-comment-content"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\" class=\"comment-input\" placeholder=\"add a comment\"></textarea>\n            <button class=\"send-comment\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\">\n                <span class=\"glyphicon glyphicon-send\"></span>\n                Send!\n            </button>\n\n            <button class= \"insert-file\"\n                onclick=\"$('.insert-file-input').trigger( 'click')\">\n                <span class = \"glyphicon glyphicon-file\"></span>\n                File\n            </button>\n            <input class=\"insert-file-input\" id = \"insert-file-input\" type='file' onchange=\"PostCommentBlock.readFile(this);\" accept=\"image/*, application/pdf\" />\n            <input class = \"file-title\" id = \"file-title\" />\n            <img src =\"\" id = \"img-id\" />\n     \n\n            <div class = \"link\"> \n                <textarea class=\"insert-link\" id=\"insert-link\" placeholder=\"Link\"></textarea>\n\n                <button class = \"link-insert\" id = \"link-insert\" onclick=\"PostCommentBlock.AddLink();\"\n                    <span class = \"glyphicon glyphicon-link\"> </span>\n                    Link\n                </button>\n            <a href = \"\" id = \"id-link\"> </a>\n            </div>\n\n        </div>\n    </div>\n";
},"2":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "                    <a id=\"postLink\" href= \""
    + alias2(alias1(depth0, depth0))
    + "\" target=\"_blank\" value=\""
    + alias2(alias1(depth0, depth0))
    + "\" />\n";
},"4":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "                <div class=\"comment-block\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mCommentID") : depth0), depth0))
    + "\">\n                    <div class=\"content\">\n                        <!-- leave comment's content here -->\n                        "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mContent") : depth0), depth0))
    + "\n                    </div>\n\n                    <div class=\"lower-information\">\n\n                        <button class=\"user-button little-button\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mUserID") : depth0), depth0))
    + "\">\n                            <span class=\"glyphicon glyphicon-user\"></span>\n                            <!-- leave comment's user here -->\n                            "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mFirstName") : depth0), depth0))
    + " "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mLastName") : depth0), depth0))
    + "\n                        </button>\n\n                        <button class=\"date-button little-button\">\n                            <span class=\"glyphicon glyphicon-calendar\"></span>\n                            <!-- leave commnet's post date here -->\n                            "
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mDate") : depth0), depth0))
    + "\n                        </button>\n\n                    </div>\n                </div>\n";
},"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "\n"
    + ((stack1 = lookupProperty(helpers,"each").call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? lookupProperty(depth0,"mData") : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":2,"column":0},"end":{"line":119,"column":9}}})) != null ? stack1 : "");
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['BasicStructure.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['MyProfileBlock.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, alias1=container.lambda, alias2=container.escapeExpression, alias3=depth0 != null ? depth0 : (container.nullContext || {}), lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "<div class=\"my-user-profile-block\">\n    <h4 style=\"margin-top: 30px;\">First Name:</h4>\n    <p>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mFirstName") : depth0), depth0))
    + "</p>\n    <h4>Last Name:</h4>\n    <p>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mLastName") : depth0), depth0))
    + "</p>\n    <h4>E-mail:</h4>\n    <p>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mEmail") : depth0), depth0))
    + "</p>\n\n"
    + ((stack1 = lookupProperty(helpers,"each").call(alias3,(depth0 != null ? lookupProperty(depth0,"mPosts") : depth0),{"name":"each","hash":{},"fn":container.program(2, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":10,"column":4},"end":{"line":25,"column":13}}})) != null ? stack1 : "")
    + "     \n"
    + ((stack1 = lookupProperty(helpers,"each").call(alias3,(depth0 != null ? lookupProperty(depth0,"mComments") : depth0),{"name":"each","hash":{},"fn":container.program(4, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":27,"column":4},"end":{"line":39,"column":13}}})) != null ? stack1 : "")
    + "</div>\n";
},"2":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "        <h4>Post: </h4>\n        <h5>title: </h5>\n        <textarea name=\"\" id=\"post-title"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\" cols=\"30\" rows=\"3\" class=\"edit-place\">"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mTitle") : depth0), depth0))
    + "</textarea>\n        <h5>content: </h5>\n        <textarea name=\"\" id=\"post-content"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\" cols=\"30\" rows=\"5\" class=\"edit-place\">"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mContent") : depth0), depth0))
    + "</textarea>\n        <button class=\"post-update-button edit-button\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\">\n            <span class=\"glyphicon glyphicon-upload\"></span>\n            update\n        </button>\n\n        <button class=\"post-delete-button edit-button\"  data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\">\n            <span class=\"glyphicon glyphicon-trash\"></span>\n            delete\n        </button>\n";
},"4":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "        <h4>Comment: </h4>\n        <h5>content: </h5>\n        <textarea name=\"\" id=\"comment-content"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mCommentID") : depth0), depth0))
    + "\" cols=\"30\" rows=\"5\" class=\"edit-place\">"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mContent") : depth0), depth0))
    + "</textarea>\n        <button class=\"comment-update-button edit-button\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mCommentID") : depth0), depth0))
    + "\" data-postid=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\">\n            <span class=\"glyphicon glyphicon-upload\"></span>\n            update\n        </button>\n        <button class=\"comment-delete-button edit-button\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mCommentID") : depth0), depth0))
    + "\" data-postid=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mPostID") : depth0), depth0))
    + "\">\n            <span class=\"glyphicon glyphicon-trash\"></span>\n            delete\n        </button>\n";
},"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return ((stack1 = lookupProperty(helpers,"with").call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? lookupProperty(depth0,"mData") : depth0),{"name":"with","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":1,"column":0},"end":{"line":41,"column":9}}})) != null ? stack1 : "");
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['OtherProfileBlock.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, alias1=container.lambda, alias2=container.escapeExpression, alias3=depth0 != null ? depth0 : (container.nullContext || {}), lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "<div class=\"other-user-profile-block\">\n    <span class=\"glyphicon glyphicon-remove close-button\"></span>\n\n    <div class=\"user-information\">\n        <h4>First Name:</h3>\n        <p>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mFirstName") : depth0), depth0))
    + "</p>\n        <h4>Last Name:</h3>\n        <p>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mLastName") : depth0), depth0))
    + "</p>\n        <h4>E-mail:</h3>\n        <p>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mEmail") : depth0), depth0))
    + "</p>\n        \n"
    + ((stack1 = lookupProperty(helpers,"each").call(alias3,(depth0 != null ? lookupProperty(depth0,"mPosts") : depth0),{"name":"each","hash":{},"fn":container.program(2, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":13,"column":8},"end":{"line":21,"column":17}}})) != null ? stack1 : "")
    + "\n"
    + ((stack1 = lookupProperty(helpers,"each").call(alias3,(depth0 != null ? lookupProperty(depth0,"mContent") : depth0),{"name":"each","hash":{},"fn":container.program(4, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":23,"column":8},"end":{"line":26,"column":17}}})) != null ? stack1 : "")
    + "\n    </div>\n</div>\n";
},"2":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "            <h4>Post: </h4>\n\n            <h5>Title: </h5>\n            <p>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mTitle") : depth0), depth0))
    + "</p>\n\n            <h5>Content: </h5>\n            <p>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mContent") : depth0), depth0))
    + "</p>\n";
},"4":function(container,depth0,helpers,partials,data) {
    var lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "            <h5>Comment: </h5>\n            <p>"
    + container.escapeExpression(container.lambda((depth0 != null ? lookupProperty(depth0,"mContent") : depth0), depth0))
    + "</p>\n";
},"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return ((stack1 = lookupProperty(helpers,"with").call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? lookupProperty(depth0,"mData") : depth0),{"name":"with","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":1,"column":0},"end":{"line":30,"column":9}}})) != null ? stack1 : "");
},"useData":true});
})();
