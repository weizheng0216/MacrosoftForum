// Prevent compiler errors when using Handlebars
let Handlebars: any;

// a global for the main ElementList of the program.  See newEntryForm for 
// explanation
var mainList: ElementList;

/**
 * ElementList provides a way of seeing all of the data stored on the server.
 */
class ElementList {
    /**
     * refresh is the public method for updating messageList
     */
    refresh(option:string) {
        
        // Issue a GET, and then pass the result to update()
        // TODO: refresh the list once the option was selected
        // let option = (<HTMLSelectElement>document.getElementById('sortby')).value;
        // window.alert(option);
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
        // $("#ElementList").remove();
        $(".card-columns").remove();
        $("body").append(Handlebars.templates["ElementList.hb"](data));
        // $("#messageList").html(Handlebars.templates["ElementList.hb"](data));
        // Find all of the delete buttons, and set their behavior
        $(".upbtn").click(mainList.upvote);
        // Find all of the Edit buttons, and set their behavior
        $(".downbtn").click(mainList.downvote);
    }

 
    /**
     * clickDelete is the code we run in response to a click of a delete button
     */
    private upvote() {
        // for now, just print the ID that goes along with the data in the row
        // whose "delete" button was clicked

        let id = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: backendUrl + "/messages/upvote/" + id,
            dataType: "json",
            // TODO: we should really have a function that looks at the return
            //       value and possibly prints an error message.
            success: mainList.refresh,
            error : function(){
                window.alert("/messages/upvote/" + id);
              }
        })
        
        
    }

    private downvote() {
        // for now, just print the ID that goes along with the data in the row
        // whose "delete" button was clicked

        let id = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: backendUrl + "/messages/downvote/" + id,
            dataType: "json",
            // TODO: we should really have a function that looks at the return
            //       value and possibly prints an error message.
            success: mainList.refresh,
            error : function(){
                window.alert("/messages/downvote/" + id);
              }
        })
    }
    
    // private upButtons(id: string): string {
    //     return "<td><button class='upbtn' data-value='" + id
    //         + "'>Upvote</button></td>";
    // }

    // private downButtons(id: string): string {
    //     return "<td><button class='downbtn' data-value='" + id
    //         + "'>Downvote</button></td>";
    // }

} // end class ElementList
