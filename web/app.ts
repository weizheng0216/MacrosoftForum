/// <reference path="ts/BriefPostsList.ts"/>
/// <reference path="ts/PostCommentBlock.ts"/>
/// <reference path="ts/BasicStructure.ts"/>
/// <reference path="ts/NewPostBlock.ts"/>
/// <reference path="ts/MyProfileBlock.ts"/>

// Prevent compiler errors when using Handlebars
let Handlebars: any;

const backendUrl = "https://cse216-macrosoft.herokuapp.com";

const alertOn = true;

const test = true;

const debug = true;

let sessionKey: string;

// Run some configuration code when the web page loads
jQuery(function () {
    debugOutput("Ready");
    sessionKey = sessionStorage.getItem("sessionKey");
    if (!sessionKey && !test) redirect("login.html");
    debugOutput("Session key: " + sessionKey);
    BasicStructure.init();
});



// ===================================================
// Utility Functions

function format(s: string, ...objs: any[]) {
    if (!s) return "";
    return s.replace(/\{(\d+)\}/g, (m, i) => objs[i-1]);
}

function debugOutput(...data: any[]) {
    if (!debug) return;
    for (let i = 0; i < data.length; i++)
        console.log(data[i]);
}

function alertOutput(msg: string) {
    if (alertOn) alert(msg);
}

function myAjax(settings: JQuery.AjaxSettings) {
    if (!settings) return;
    debugOutput("[ajax] Sending request: " + settings);
    if (test) {
        // Simulate backend response during tests
        let callback: any = settings.success;
        callback(backend.getResponse(settings));
    } else {
        $.ajax(settings);
    }
}

function redirect(name: string) {
    window.location.replace(backendUrl + "/" + name);
}

/**
 * Accept raw backend response and scans through the file headers in
 * each object. If an image is identified, download the image and update
 * the json so that it could later be fed into Handlebars and be displayed.
 * 
 * The file data will be appended to the "mData" field of mFileInfo.
 * 
 * @param posts An array of "PostSubType"
 */
function fetchImgs(posts: any) {
    for (let i = 0; i < posts.length; i++) {
        let p = posts[i];
        // the file attached to the post
        if (/image\/.+/.test(p.mFileInfo.mType)) {
            myAjax({
                async: false,
                type: "GET",
                dataType: "json",
                url: format("{1}/api/posts/{2}/file?session={3}",
                            backendUrl, p.mPostID, sessionKey),
                success: function(data: any) {
                    debugOutput("[ajax] File downloaded: " + data);
                    p.mFileInfo.mData = data.mData.mData;
                }
            });
        }
        // the files attached to the comments
        for (let j = 0; j < p.mComments.length; j++) {
            let c = p.mComments[j];
            if (/image\/.+/.test(c.mFileInfo.mType)) {
                myAjax({
                    async: false,
                    type: "GET",
                    dataType: "json",
                    url: format("{1}/api/posts/{2}/comments/{3}/file?session={4}",
                                backendUrl, p.mPostID, c.mCommentID, sessionKey),
                    success: function(res: any) {
                        debugOutput("[ajax] File downloaded: " + res);
                        c.mFileInfo.mData = res.mData.mData;
                    }
                });
            }
        }
    }
}


// ===================================================
// Fake backend for testing

let backend = test && (function () {
    // Only support simplest logic for now
    let json1 = {
        "mStatus": "OK",
        "mMessage": "",
        "mData": [
            {
                "mPostID": 23,
                "mTitle": "newest post",
                "mContent": "Here is my comment",
                "mAuthor": {
                    "mUserID": 66,
                    "mEmail": "test@lehigh.edu",
                    "mFirstName": "test_first",
                    "mLastName": "test_last"
                },
                "mDate": "2021-04-13 09:20:01.62778",
                "mUpVoteCount": 0,
                "mDownVoteCount": 0,
                "mFlagged": true,
                "mFileInfo": {
                    "mType": "image/png",
                    "mTime": "",
                    "mName": ""
                },
                "mLinks": [
                    ""
                ],
                "mComments": [],
                "mUserUpVote": false,
                "mUserDownVote": false
            },
            {
                "mPostID": 22,
                "mTitle": "newest post",
                "mContent": "Here is my comment",
                "mAuthor": {
                    "mUserID": 66,
                    "mEmail": "test@lehigh.edu",
                    "mFirstName": "test_first",
                    "mLastName": "test_last"
                },
                "mDate": "2021-04-13 09:12:08.854174",
                "mUpVoteCount": 0,
                "mDownVoteCount": 0,
                "mFlagged": false,
                "mFileInfo": {
                    "mType": "image/png",
                    "mTime": "",
                    "mName": ""
                },
                "mLinks": [
                    ""
                ],
                "mComments": [],
                "mUserUpVote": false,
                "mUserDownVote": false
            },
        ]
    };  // end of response

    let json2 = {
        "mStatus": "OK",
        "mMessage": "",
        "mData": {
            "mUser": {
                "mUserID": 66,
                "mEmail": "test@lehigh.edu",
                "mFirstName": "test_first",
                "mLastName": "test_last"
            },
            "mPosts": [
                {
                    "mPostID": 20,
                    "mTitle": "my title",
                    "mContent": "Here is my comment",
                    "mAuthor": {
                        "mUserID": 66,
                        "mEmail": "test@lehigh.edu",
                        "mFirstName": "test_first",
                        "mLastName": "test_last"
                    },
                    "mDate": "2021-04-13 09:04:19.940533",
                    "mUpVoteCount": 0,
                    "mDownVoteCount": 0,
                    "mFlagged": false,
                    "mFileInfo": {
                        "mType": "image/png",
                        "mTime": "",
                        "mName": ""
                    },
                    "mLinks": [
                        ""
                    ],
                    "mComments": []
                }
            ]
        }
    };

    let json3 = {
        "mStatus": "OK",
        "mMessage": "",
        "mData": {
            "mData": "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=="
        }
    };

    return {
        getResponse: function (settings: JQuery.AjaxSettings) {
            let regex: RegExp;
            settings.url = settings.url || "";

            // PostCommentBlock Get file under post
            regex = /\/api\/posts\/[0-9]+\/file(\?.*)?/;
            if (settings.type == "GET" && regex.test(settings.url)) {
                debugOutput("Backend response: Get file under post");
                return json3;
            }

            // PostCommentBlock Get file under comment
            regex = /\/api\/posts\/[0-9]+\/comments\/[0-9]+\/file(\?.*)?/;
            if (settings.type == "GET" && regex.test(settings.url)) {
                debugOutput("Backend response: Get file under comment");
                return json3;
            }

            // BriefPostsList Get all
            regex = /\/api\/posts(\?.*)?/;
            if (settings.type == "GET" && regex.test(settings.url)) {
                debugOutput("Backend response: get all posts");
                return json1;
            }

            // MyProfileBlock Get my profile
            regex = /\/api\/users\/my(\?.*)?/;
            if (settings.type == "GET" && regex.test(settings.url)) {
                debugOutput("Backend response: Get my profile");
                return json2;
            }

            // PostCommentBlock Get others' profile
            regex = /\/api\/users\/[0-9]+(\?.*)?/;
            if (settings.type == "GET" && regex.test(settings.url)) {
                debugOutput("Backend response: Get others' profile");
                return json2;
            }

            // Trivial reponses
            debugOutput("Backend response: default");
            return {
                "mStatus": "OK",
                "mMessage": "",
                "mData": null
            };
        }  // end of getResponse()
    }  // end of the exported obj
})();
