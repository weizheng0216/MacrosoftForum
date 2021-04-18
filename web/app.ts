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

const backendUrl = "https://cse216-macrosoft-phase3.herokuapp.com";

const testing = false;

const debug = false;
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */

// Run some configuration code when the web page loads
$(document).ready(function () {
    if (debug)
        console.log("ready");
    if (sessionStorage.getItem("sessionKey")) {
        BasicStructure.sessionKey = sessionStorage.getItem("sessionKey");
    } else {
        BasicStructure.sessionKey = "";
    }
    if (debug)
        console.log(BasicStructure.sessionKey);
    BasicStructure.init();

});

