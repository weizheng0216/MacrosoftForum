// Prevent compiler errors when using Handlebars
let Handlebars: any;

// a global for the main ElementList of the program.  See newEntryForm for 
// explanation
var mainList: ElementList;

/**
 * ElementList provides a way of seeing all of the data stored on the server.
 * @param option: string telling how to sort (either by latest or votes)
 */
class ElementList {
    /**
     * refresh is the public method for updating messageList
     */
    refresh(option:string) {
        // Issue a GET, and then pass the result to update()
        // If option is 'Date Posted', ask the list in sortbydate, else in sortbyvotes
        if (option === "Date Posted") {
            $.ajax({
                type: "GET",
                url: backendUrl + "/messages/sortbydate",
                dataType: "json",
                success: mainList.update
            });
        } else if (option === "Vote Counts"){
            $.ajax({
                type: "GET",
                url: backendUrl + "/messages/sortbyvotes",
                dataType: "json",
                success: mainList.update
            });
        }
    }

    /**
     * update() is the private method used by refresh() to update the 
     * ElementList
     */
    private update(data: any) {
        // Use a template to generate a table from the provided data, and put 
        // the table into our messageList element.

        // remove the previous card-col if exist
        $(".card-columns").remove();
        // populate the body with data and format from hb
        $("body").append(Handlebars.templates["ElementList.hb"](data));

        // Find all of the buttons, and set their behavior
        $(".upbtn").click(mainList.upvote);
        $(".downbtn").click(mainList.downvote);
    }

    upvote() {

        // gets the ID that goes along with the data in the row
        let id = $(this).data("value");
        
        // replace the data by using doUp function
        $.ajax({
            type: "PUT",
            url: backendUrl + "/messages/upvote/" + id,
            dataType: "json",
            // TODO: we should really have a function that looks at the return
            //       value and possibly prints an error message.
            success: mainList.doUp,
            error : function(){
                window.alert("/messages/upvote/" + id);
              }
        })
    }

    private doUp(data: any) {
        // If we get an "ok" message, refresh the form so the count goes up by one
        if (data.mStatus === "ok") {
            mainList.refresh(option);
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }

    private downvote() {
        // similar to upvote function
        let id = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: backendUrl + "/messages/downvote/" + id,
            dataType: "json",
            success: mainList.doDown,
            error : function(){
                window.alert("/messages/downvote/" + id);
              }
        })
    }

    private doDown(data: any) {
        // similar to doUp
        if (data.mStatus === "ok") {
            mainList.refresh(option);
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
} // end class ElementList
