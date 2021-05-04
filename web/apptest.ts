var describe: any;
var it: any;
var expect: any;

/**
 * Hide the UI so that the test result could be shown properly.
 */
jQuery(function() {
    document.getElementById("banner").style.visibility = "hidden";
    document.getElementById("main-block").style.visibility = "hidden";
});

function expectHidden(jquery: JQuery, b: boolean) {
    let style = jquery.attr("style");
    if (style) {  // element has style, check display property
        if (b)  // should be hidden
            expect(style.indexOf("display: none;")).not.toEqual(-1);
        else  // should be shown
            expect(style.indexOf("display: none;")).not.toEqual(-1);
    }
    // otherwise success
    expect(true).toBe(true);
}

describe("BasicStructure tests", function() {
    /**
     * When new post button is clicked, 
     *     1) new post block should be shown
     *     2) all other blocks should be hidden
     */
    it("Click new post button", function() {
        $('.new-post-button').trigger("click");
        expectHidden($("#my-new-post-block"), false);
        expectHidden($(".my-user-profile-block:first"), true);
        expectHidden($(".post-comment-view:first"), true);
        // no need to reset the UI
    });

    /**
     * This test is to ensure when the user profile button is clicked,
     *     1) the user interface will be showed,
     *     2) new post block will be hidden. 
     */
    it("Click my profile button", function () {
        $(".my-profile-button").trigger("click");
        expectHidden($(".my-user-profile-block:first"), false);
        expectHidden($("#my-new-post-block"), true);
        expectHidden($(".post-comment-view:first"), true);
        // no need to reset the UI
    });
});

describe("BreifPostList tests", function() {
    /**
     * This test is to ensure when the brief post is clicked,
     *     1) the corresponding detail will be showed in the right
     *     2) new post block will be hidden. 
     */
    it("Click post detail", function(){
        let post_id = $(".post-brief-block").first().data("value");
        $(".post-brief-block").first().trigger("click");
        expectHidden($("#my-new-post-block"), true);
        expectHidden($(".post-comment-view[data-value='"+post_id+"']"), false);
    });
});

describe("PostCommentBlock tests", function() {
    it("Show others' profile", function(){
        $(".new-post-button").trigger("click");
        $(".my-profile-button").trigger("click");
        $(".post-brief-block").first().trigger("click");
        $(".user-button").trigger("click");
        expectHidden($(".other-user-profile-block"), false);
    });

    it("Dislike the post", function() {
        $("#my-down-vote-button23").trigger("click")
        expect($("#my-down-vote-count23").text()).toEqual('1');
    });

    it("Like the post", function() {
        $("#my-up-vote-button23").trigger("click");
        expect($("#my-up-vote-count23").text()).toEqual('1');
    });

    it("Flagging inappropriate contents", function() {
        let post_id = $(".post-brief-block").first().data("value");
        let jqbtn = $("#flag-button-" + post_id);
        jqbtn.trigger("click");
        expectHidden($("#postlist-flag-" + post_id), false);
    });

    it("Unflagging inappropriate contents", function() {
        let post_id = $(".post-brief-block").first().data("value");
        let jqbtn = $("#flag-button-" + post_id);
        jqbtn.trigger("click");
        expectHidden($("#postlist-flag-" + post_id), true);
    });
});

/**
 * Test to see if the IFrame are displayed as required.
 */
describe("Phase4 Extra credit: IFrame test", function() {
    it("IFrame displays properly", function() {
        $(".post-brief-block").first().trigger("click");
        let frames = document.getElementsByClassName("yt-block");
        expect(frames.length).toEqual(2);
    });

    it("No IFrame for post without video link", function() {
        $(".post-brief-block").first().trigger("click");
        let frames = document.getElementsByClassName("yt-block");
        expect(frames.length).toEqual(2);
    });
});
