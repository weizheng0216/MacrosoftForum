var $:any;
var describe: any;
var it: any;
var expect: any;
/**
 * All tests about the front end is performed here.
 */
describe("basic structure", function() {
    
    /**
     * this test is to ensure when the brief post is clicked, 1) the corresponding detail will
     *  be showed in the right and 2) new post block will be hidden. 
     */
    it("show post detail", function(){
        let post_id = $(".post-brief-block").first().data("value");
        $(".post-brief-block").first().click();

        //expect the new post block is hidden
        expect($("#my-new-post-block").attr("style").indexOf("display: none;")).toEqual(0);

        //expect the first post's detail is showed
        expect($(".post-comment-view[data-value='"+post_id+"']").attr("style").indexOf("display: block;")).toEqual(0);
    });

    /**
     * this test is to ensure when the new post button is clicked, 1) the new post block will 
     *      be showed and 2) new post block will be hidden. 
     */
    it("show new post block", function(){
        
        $(".new-post-button").click();

        //expect the new post block is showed
        expect($("#my-new-post-block").attr("style").indexOf("display: block;")).toEqual(0);

        //expect the first post's detail is hidden
        expect($(".post-comment-view").attr("style").indexOf("display: none;")).toEqual(0);
    });

    /**
     * this test is to ensure when the user profile button is clicked, 1) the user interface will 
     *  be showed, 2) new post block will be hidden. 
     */
    it("show user profile interface", function(){
        
        $(".my-profile-button").click();

        //expect the new post block is hidden
        expect($("#my-new-post-block").attr("style").indexOf("display: none;")).toEqual(0);

        //expect the first post's detail is hidden
        expect($(".post-comment-view").attr("style").indexOf("display: none;")).toEqual(0);
    });

});

describe("test like and dislike", function(){
    /**
     * this test is to ensure when user click the "like" button, the count of like will increase by 1.
     *  In spec_runner.html, the current count of like is "0", so we expect "1" here.
     */
    it("test like", function(){
        $("#my-up-vote-button1").click();
        expect($("#my-up-vote-count1").text()).toEqual('1');
    });

    /**
     * this test is to ensure when user click the "dislike" button, the count of like will increase by 1.
     *  In spec_runner.html, the current count of dislike is "0", so we expect "1" here.
     */
    it("test dislike", function(){
        $("#my-down-vote-button1").click();
        expect($("#my-down-vote-count1").text()).toEqual('1');
    });

});

describe("test close/open other user interface", function(){

    /**
     * this test is to ensure when the user click user button, its corresponding information will be showed.
     */
    it("test show other user interface", function(){
        $(".new-post-button").click();
        $(".my-profile-button").click();
        $(".post-brief-block").first().click();
        $(".user-button").click();
        expect($(".other-user-profile-block").attr("style").indexOf("display: none;")).toEqual(-1);
    });

    /**
     * this test is to ensure when the user click the close button, the user interface will be closed. 
     *  Here we have to ensure the other user interface is already opened.
     */
    it("test show other user interface", function(){
        $(".post-brief-block").first().click();
        $(".user-button").click();
        $(".close-button").click();
        expect($(".other-user-profile-block").attr("style").indexOf("display: none;")).toEqual(0);
    });
});


