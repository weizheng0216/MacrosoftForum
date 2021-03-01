var $:any;
var describe: any;
var it: any;
var expect: any;

describe("UI Test", function() {
    it("UI Test: Open Form", function(){
        // click the button to pop-up the form
        $('.open-button').click();
        // expect that the form is showing up
        expect($("#myForm").attr("style").indexOf("display: none;")).toEqual(-1);
        // $('.cancel').click();
    });


    it("UI Test: Hide Form", function(){
        // click the button to pop-up the form
        $('.hide').click();
        // expect that the form is showing up
        expect($("#myForm").attr("style").indexOf("display: none;")).toEqual(0);
    });
    
    
    it("UI Test: Clear Form", function(){
        // click the button for clear the form
        $('.clear').click();
        // all the field to be empty
        expect($("#newTitle").val()).toEqual("");
        expect($("#newMessage").val()).toEqual("");
        expect($("#newUser").val()).toEqual("");
        // $('.cancel').click();
    });
    it("UI Test: Add Post", function(){
        // click the button for adding a post
        $('#addButton').click();
        // expect that the form hidden
        expect($("#myForm").attr("style").indexOf("display: none;")).toEqual(0);
        $('.cancel').click();
    });

    // it("UI Test: Hide Post", function(){
    //     // click the button to hide the form
    //     $('.cancel').click();
    //     // expect that the form hidden
    //     expect($("#myForm").attr("style").indexOf("display: none;")).toEqual(0);
    // });

});


// describe("Tests Open/Clear Form Btn UI function", function() {
    // it("UI Test: Open Form", function(){
    //     // click the button for showing the add button
    //     $('.open-button').click();
    //     // expect that the form hidden
    //     expect($("#myForm").attr("style").indexOf("display: none;")).toEqual(-1);
    // });
//     it("UI Test: Clear Form", function(){
//         $('#addCancel').click();
//         expect($("#newTitle").val()).toEqual("");
//         expect($("#newMessage").val()).toEqual("");
//         expect($("#newUser").val()).toEqual("");
    
//     });
// });




// describe("Tests Open/Clear Form Btn UI function", function() {
//     it("UI Test: Open Form", function(){
//         // click the button for showing the add button
//         $('.open-button').click();
//         // expect that the form hidden
//         expect($("#myForm").attr("style").indexOf("display: none;")).toEqual(-1);
            
//     });
//     it("UI Test: Clear Form", function(){
//         // click the button for showing the add button
//         $('#addCancel').click();
//         // expect that the form hidden
//         it("UI Test: Cancel Button Clears the Form", function(){
        
//         expect($("#newTitle").val()).toEqual("");
//         expect($("#newMessage").val()).toEqual("");
//         expect($("#newUser").val()).toEqual("");
 
//     });
    
// });
