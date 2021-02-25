/// <reference path="ts/NewPost.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>

// Prevent compiler errors when using Handlebars
// let Handlebars: any;

// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;



// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newPost: NewPost;
var option = "Date Posted";

const backendUrl = "https://immense-ravine-11051.herokuapp.com";
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */

// Run some configuration code when the web page loads
$(document).ready(function () {
    // Create the object that controls the "New Entry" form
    newPost = new NewPost();
    // Create the object for the main data list, and populate it with data from
    // the server
    mainList = new ElementList();
    mainList.refresh(option);
    Navbar.refresh();
    // window.alert("hello");
    $('#sortBy a').on('click', function(){
        let option = $(this).text();
        mainList.refresh(option);
        
    });
});

