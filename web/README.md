# Web

## Front-end Role
- Phase 1: Wei Zheng
- Phase 2: Weihang Guo
- Phase 3: Siyu(Coco) Chen
- Phase 4: Haocheng Gao

## Introduction 
Buzz front-end is an `html` page that runs in client browsers. It's an interactive
user interface that allows users to login, view posts, make posts, make comments,
vote posts, upload files, upload links, upload youtube videos, view profiles, and
flag inappropriate contents.

***

## Dependencies
There are three types of dependencies used in this project: the deployed node
modules, the undeployed node modules, and the URLs embedded in HTML pages.

We have installed Bootstrap, Handlebars, jQuery as modules that will be deployed
onto the Heroku server. They contribute to CSS templating, html templating, and 
serve as function library. Jasmine is used for unit testing and is also deployed
to the server.

TypeScript and simple http server are installed as undeploed modules. They are
the develop tools we used during this project.

We have also linked other external sources, like google's oAuth API, into our html.
Please view them in the `head` secion of `login.html` and `index.html`.

***

## Refactorization
For maintainability and future developments, the Buzz front-end is refactored in
Phase4 to eliminate the technical debts on record. Here is a listing of all debts
accumulated through the course of development, and how they are resolved.


### Adding Handlebars for BasicStructure
`BasicStructure` is the foundamental skeleton of the Buzz front-end. It was
embedded in `index.html` originally. To extend the templating design with
handlebars to its maximum potential, these skeleton elements are moved to
`BasicStructure.hbr` and a function call to its loaded is run in the main
function of `app.ts`.

This part of refactorization improves the readability of the project by letting
each module having its own template file, stylesheet and script file instead of
hard coding anything into the html page.


### Redo the CSS of NewPostBlock
NewPostBlock follows a very simple flow-layout, meaning each component in the
layout would share most of their style attributes. However the original CSS
design heavily utilized the `position` attribute and the offset settings that
comes with it. Although it worked in previous phases, the complexity of the
stylesheets increases very fast as we extend the module.

Therefore the CSS of this module is re-designed with extensive uses of margins,
paddings, flex-boxes and height constraints. To add new components to the module,
we can simply insert new container into its DOM tree then define the height
constrain of the new container.


### Global CSS definitions
There are patterns that show up repeatedly through the application. For instance,
the shaded red border, the CSS effect upon mouse clicks, the file containers,
etc. `BasicStructure.css` was initially desinged not only to define the style
information of the document skeleton, but also to contain some global CSS class
definitions.  However, through the course of developing, this file is rarely
edited and has become out-dated. There are more grouped style attributes that
could be extracted into this global file.

By creating `mybtn` class in the global css file, we don't need to specify the
same border pattern and CSS effect over buttons over and over again when
extending the application.


### Wrapping console.log
In previous implementations, the script functions are flooded with `console.log`
output functions which will print something to the console in debug mode. There
are countless `if` branches inserted and they make the code unreadable.

In the new implementation a function called `deboutOutput` is created. It wraps
the `console.log` and performs the debug mode check so that we can keep the main
functional scripts as succinct as possible.


### Wrapping jQuery.ajax
The implementations in previous phases suffer from ajax requests during unit tests.
The unit tests, by definition, does not integrate true backend functionalities.
However most UI events and interactions are designed around the HTTP communication
with the server.

The solution I came up is to again create a wrapper function around the `$.ajax`, 
`myAjax`. During tests, it passes the ajax request to a in-memory "backend" object,
which then responds to each request with some pre-defined test data.

By using this wrapper, our `onSuccess` functions will be garanteed to execute
during tests, meaning we can further avoid the "test branching" during logical 
scripting.


### Other utility functions
Utility functions allow programmers to encapsulate individual functionalities
into "blackboxes" that could be re-used anywhere in the project without worry
about their internals. 

To "save time", there was no utility functions developed in previous phases.
The current implementation provides a set of utility functions in `app.ts`.
All other modules are free to access these function from anywhere.


### Event based function naming
Naming is also an important part of refactorization. The original implementation
has pretty poor naming in terms of events. The programmer must have concrete
and comprehensive knowledge of how each component of the UI work in order to
understand the event handlers.

The event handlers have been renamed to functions with names of form `onEventAction`.
For example, `sendPost` has been renamed to `onClickSendPost` to signal that 
calling this funcion is always equivalent to hitting the `SendPost` button.


### Replacing deprecated library functions
A lot of jQuery functions we used has been deprecated, including `$(document).ready`,
`$(selector).click()`, `$(selector).click(handler)`. Using deprecated functions
may prevent us from upgrading dependencies in the future. Also, there are
reasons that these functions become obsolete. Therefore it's best to replace
them as early as possible.


### General refinement
A lot of other details throughout the entire project have been refined, which
include but are not limited to:

- Re-naming the handlebars extention to allow text editors to apply coloring on
handlebar files;
- Adding loops and wildcards in `deploy.sh` and `ldeploy.sh` to save us from
updating the shell scripts again and again upon adding new `css`, `hbr`, or
`ts` files;
- Removing the out-dated code that were commented out.
- Indentations have been unified to 4.
- Adding/editing in-text documentations to a lot of script functions.


***

## Developer guide

### Project structure
The project leviates singleton design pattern on the major UI components: 
`BasicStructure`, `BriefPostList`, `MyProfileBlock`, `PostCommentBlock`. Each
of them has a handlebars file under `hbr` directory that defines the `html`
structure, a stylesheet under `css` directory that defines the style, and a
script file under `ts` directory to allow the UI to be interactive. Whenever
you desire to change a component or to add a new component, it's important
to stick with the structure described above.

You can find various files under project root. Each of them is important.

| File Name | Functionality |
|:----|:----|
| .gitignore | The gitignore file local to the web branch. You are free to edit it. |
| app.ts | The entry point of application JavaScript. |
| apptest.ts | The entry point of unit tests. |
| deploy.sh | Deploy sources to the resource folder under backend. |
| ldeploy.sh | Deploy sources to the `local` directory for faster tests. |
| index.html | Home page of Buzz front-end. |
| login.html | Login page of Buzz front-end. |
| spec_runner.html | Jasmine test page of Buzz front-end. |
| package-lock.json | Version locks. Commit but don't edit manually. |
| package.json | Node dependencies. Edit with causion. |
| README.md | Front-end documentation. |

### Deployment
When you deploy, `html` pages under root directory will be copied to the
resource folder. `app.ts` will be linked with the scripts under `ts` directory
and be compiled into `app.js` in resource folder. `apptest.ts` will be compiled
into `apptest.js`. All `hbr` files will be compiled into `templates.js`. All
`css` files will be compiled into `app.css`. All of them will be placed in
backend's resource folder to be packaged into its `jar` file, which will then
be deployed onto `heroku` server and run. Please refer to the content of`deploy.sh`
for more details about deployment.

### Functions

#### Sending ajax request to backend
The core functionality of front-end is to communicate with backend by HTTPs
protocol. To allow local tests, developers should use the `myAjax` utility
function instead of the `$.ajax` function. Here is an example.

```javascript
myAjax({
    type: "GET",
    url: backendUrl + "/api/users/my?session=" + sessionKey,
    dataType: "json",
    success: function (res: any) {
        debugOutput("[ajax] MyProfile Response: " + JSON.stringify(res));
        fetchImgs(res.mData.mPosts);
        fetchImgs(res.mData.mComments);
        MyProfileBlock.update(res);
    },
    error: function() {
        alertOutput("Login timeout, please login again");
        redirect("login.html");
    }
});
```

#### Downloading file
The Buzz system support file downloading with base64 strings. The function is
implemented at `app.ts/downloadFile`. The developer should get the base64 data
string and file type through `html` element properties or through ajax requests.

```javascript
downloadFile(base64, "image/png", "reddot.png");
```

#### Using promises
During the development, there are a lot of senarios where we need to use the
FileReader. FileReader reads blobs asynchronously, meaning the jobs that depdend
on the result of FileReader must be performed after the asynchronous call. Our
solution to this problem is to use promises. Please see an example from the project:

```javascript
let id = PostCommentBlock.currPostId;
let type = $("#file-panel-" + id).data("value");
let name = $("#post-filename-" + id).html();

(async function () {
    let base64: string = await new Promise(resolve => {
        if (/image\/\w/.test(type)) {
            let src = $("#post-img-preview-"+id).attr("src");
            resolve(src.replace(/^data:.+;base64,/, ""));
        } else {
            // need to download from backend
            myAjax({
                type: "GET",
                dataType: "json",
                url: format("{1}/api/posts/{2}/file?session={3}",
                            backendUrl, id, sessionKey),
                success: function(res: any) {
                    let msg = JSON.stringify(res);
                    debugOutput("[ajax] File downloaded: " + msg);
                    resolve(res.mData.mData);
                }
            });
        }
    });
    downloadFile(base64, type, name);
})();
```

In order to download a file, we may need to wait for the result from an ajax
download request. Therefore the entire download process should be viewed as
asynchronous(`async`), and we need to `await` the promise that retireve the
file data before initiating the download process.

#### CSS Conventions
Under the `/css` folder contains all stylesheets that we need. Each file contains
the stylesheet specific to the module, with the `BasicStructure.css` being an
exception. `BasicStructure.css` also contains the global formatting information.

#### Outputs
There are two types of output functions that we define: `debugOutput` and `alertOutput`.
The `debugOutput` will output to console in debug mode, and `alertOutput` will
prompt the alert frame to user if the alert flag is on. It's important to
produce output messages along the development because they help with debugging.
The least requirement is to have one line of output to console that indicates
which function is being called. Here is an example:
```javascript
function foo() {
    debutOutput("Calling foo()");

    // body of foo()...
}

function boo() {
    debutOutput("Calling boo()");

    // body of boo()...
}
```

#### OAuth2.0
We handle use session and logins with OAuth protocal and the use of session key.
The work flow is simple:
1. Get session key from browser storage in `app.ts`
2. If there is session key, we are done.
3. If there is no session key, redirect the user to login page
4. User will go through Google's OAuth login process, producing an access token
5. Send the access token to backend to get session key. Store the session key
browser's storage and redirect the user to home page. This will bring us again to
step 1.
6. When the server indicate an error in the session key, log the user out and
redirect them to the login page.

#### Modules
- BasicStructure: The overall skeleton of the web page.
- BriefPostList: The left part of the page, which is a list of post breifings.
- PostCommentBlock: The main viewing window that shows post contents and comments.
- NewPostBlock: The block that prompt user to create new posts.
- MyProfileBlock: The block that shows user profile.

***

### Phase4 Extra Credit

**API: IFrame with Youtube**

Buzz has prooved its usefulness by allowing posts, comments, and files. What
else employees may need on a social network platform? Our group figures it
to be the ability to watch videos. The most recent version of Buzz support
youtube link uploading. Users can upload a youtube link when they make a post.
The video will be displayed when other users view the post. In user's profile
page, a "video icon" will be shown on the left of post titles to indicate
there is a video attached to the post.

The major task of web front-end is to display the video block properly. This is
achieved by applying handlebars templating with data and CSS box model.