/// <reference path="ts/BriefPostsList.ts"/>
/// <reference path="ts/PostCommentBlock.ts"/>
/// <reference path="ts/BasicStructure.ts"/>
/// <reference path="ts/NewPostBlock.ts"/>
/// <reference path="ts/MyProfileBlock.ts"/>
// Prevent compiler errors when using Handlebars
// let Handlebars: any;

// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
let Handlebars: any;
let $: any;
const backendUrl = "http://localhost:8000";
const testing = true;
const debug = false;
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */

// Run some configuration code when the web page loads
$(document).ready(function () {
    console.log("ready");
 
    BasicStructure.init();
    
});

