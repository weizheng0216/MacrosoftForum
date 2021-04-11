# Backend

## Backend Role
- Phase 1: Weihang Guo
- Phase 2: Wei Zheng
- Phase 3: Haocheng Gao

## Introduction

## APIs

> ### Main differences from phase2
> - Session key is now passed as a query.
> - most response payloads are re-structured.
> - *postID* is no longer required for updating comments.
> - Use camel case in *VoteRequest*.
> - *GET, POST, PUT* APIs are changed to adapt phase3.
> - New endpoints are added for file uploading/downloading.
> - Forbidden operations will cause error now.

Below is a list of APIs inherited from phase2, most of which are changed either
for improving the comprehensiveness of the design or for adapting phase3 requirement.

- [GET /api/posts](#markdown-header-get-apiposts)
- [GET /api/posts/:post_id/file](#markdown-header-get-apipostspost_idfile)
- [GET /api/posts/:post_id/comments/:comment_id/file](#markdown-header-get-apipostspost_idcommentscomment_idfile)
- [GET /api/users/my](#markdown-header-get-apiusersmy)
- [GET /api/users/:user_id](#markdown-header-get-apiusersuser_id)
- [POST /api/posts](#markdown-header-post-apiposts)
- [POST /api/posts/:post_id/comments](#markdown-header-post-apipostspost_idcomments)
- [POST /api/auth](#markdown-header-post-apiauth)
- [PUT /api/posts/:post_id](#markdown-header-put-apipostspost_id)
- [PUT /api/posts/:post_id/comments/:comment_id](#markdown-header-put-apipostspost_idcommentscomment_id)
- [PUT /api/posts/:post_id/vote](#markdown-header-put-apipostspost_idvote)
- [DELETE /api/posts/:post_id/comments/:comment_id](#markdown-header-delete-apipostspost_idcommentscomment_id)
- [DELETE /api/posts/:post_id](#markdown-header-delete-apipostspost_id)

All APIs listed above, except `POST /api/auth`, which is explicitly used for
authentication, require a **session key** in the URL query: `?session=[session_key]`.
If the session key is missing, or if the session key is not valid or has expired,
the server will respond with an authentication error with code `401`.

Users may only edit the information created by themselves. Modifying other people's
resource is considered invalid and will cause `403` error to be returned.

All APIs will reply with a common format called *StructuredResponse*. It contains
the status, the message, and a nullable payload. The optional payload could be in
various `json` format that holds the data which the front-end is requesting.

Here is an example. Note that *mData* will be `null` or various types of objects
depending on the type of the response.

```json
{
  "mStatus": "OK",
  "mMessage": "This is an example response",
  "mData": {}
}
```

### GET /api/posts
Get all post information, including all the posts, their comments, the author of
the comments, and the vote status of the requesting user.

Example response payload:
```json
[
  {
    "mPostID": 101,
    "mTitle": "Example",
    "mContent": "This is an example",
    "mAuthor": {
      "mUserID": 201,
      "mEmail": "example@lehigh.edu",
      "mFirstName": "first",
      "mLastName": "last"
    },
    "mDate": "2018-09-06 10:16:34",
    "mUpVoteCount": 10,
    "mDownVoteCount": 25,
    "mPinned": true,
    "mFileInfo": {
      "mType": "pdf",
      "mTime": "2020-01-15 08:15:23",
      "mName": "MyPDF"
    },
    "mLinks": [
      "https://www.examples.com",
      "https://www.lehigh.edu"
    ],
    "mComments": [
      {
        "mCommentID": 555,
        "mPostID": 123,
        "mContent": "My comment 1",
        "mDate": "2018-02-18 14:00:06",
        "mFileInfo": {
          "mType": "pdf",
          "mTime": "2020-01-15 08:15:23",
          "mName": "MyPDF"
        },
        "mLinks": [
          "https://www.examples.com",
          "https://www.lehigh.edu"
        ]
      },
      {}
    ],
    "mUserUpVote": true,
    "mUserDownVote": false
  },
  {}
]
```
Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 500+ | Unexpected server error |

### GET /api/posts/:post_id/file
Download the file attached to the particular post. The file content, which is
Base64 encoded, along with other meta info will be returned. The file content
is `null` if there is no file attached to the post.

Example response payload:
```json
{
  "mData": "(base64-encoded string, very long)"
}
```

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 500+ | Unexpected server error |

### GET /api/posts/:post_id/comments/:comment_id/file
Download the file attached to the particular comment under a particular post.
This is very similar to `GET /api/posts/:post_id/file`, so I will not provide
further examples.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 500+ | Unexpected server error |

### GET /api/users/my
Get the profile of the front-end user, which will be displayed at the profile
page.

Example response payload:
```json
{
  "mUser": {
    "mUserID": 202,
    "mEmail": "example2@lehigh.edu",
    "mFirstName": "first",
    "mLastName": "last"
  },
  "mPosts": [
    {
      "mPostID": 111,
      "mTitle": "title",
      "mContent": "This is content.",
      "mDate": "2020-12-32 00:00:00",
      "mUpVoteCount": 0,
      "mDownVoteCount": 100,
      "mPinned": false,
      "mFileInfo": {
        "mType": "pdf",
        "mTime": "2020-01-15 08:15:23",
        "mName": "MyPDF"
      },
      "mLinks": [
        "https://www.examples.com",
        "https://www.lehigh.edu"
      ]
    },
    {}
  ],
  "mComments": [
    {
      "mCommentID": 555,
      "mPostID": 123,
      "mContent": "My comment 1",
      "mDate": "2018-02-18 14:00:06",
      "mFileInfo": {
        "mType": "pdf",
        "mTime": "2020-01-15 08:15:23",
        "mName": "MyPDF"
      },
      "mLinks": [
        "https://www.examples.com",
        "https://www.lehigh.edu"
      ]
    },
    {}
  ]
}
```
Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 404 | user info cannot be found |
| 500+ | Unexpected server error |

### GET /api/users/:user_id
Get the profile of the specified user, which will be displayed at the post
or profile window. This is very similar to `GET /api/users/my`, therefore I
will not provide another example response.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 404 | *user_id* cannot be found |
| 500+ | Unexpected server error |

### POST /api/posts
Request to add a new post. The front-end needs to provide the title and the
content of the post. The server will record the author information based on
the *session key*.

In phase3, file uploading is supported. If there is no file attached, please
set the fields related to file uploading to `null` or empty string.

Example request:
```json
{
  "title": "My Title",
  "content": "And my post",
  "links": [
    "https://www.examples.com",
    "https://www.lehigh.edu"
  ],
  "fileName": "abc.png",
  "fileType": "png",
  "fileData": "(This is base64 encoded string)"
}
```
There is no response payload for this request.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 507 | Server runs out of quota. File not uploaded(however post is uploaded) |
| Other 500+ | Unexpected server error |

### POST /api/posts/:post_id/comments
Request to add a new comment to an existing post. The target *post_id* is to
be provided in the URL, and the front-end only needs to provide the content
of the comment in its request body, which is something like:

In phase3, file uploading is supported. If there is no file attached, please
set the fields related to file uploading to `null` or empty string.

Example request:
```json
{
  "content": "Here is my comment",
  "links": [
    "https://www.examples.com",
    "https://www.lehigh.edu"
  ],
  "fileName": "abc.png",
  "fileType": "png",
  "fileData": "(This is base64 encoded string)"
}
```
There is no response payload of this request.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 404 | *post_id* cannot be found |
| 507 | Server runs out of quota. File not uploaded(however comment is uploaded) |
| Other 500+ | Unexpected server error |

### POST /api/auth
This is the login API for **BUZZ**. The front-end needs to pass the *idToken*
obtained through Google's authentication process. This *idToken* will be used
by the backend to authenticate/verify with Google. A *session key* will be
returned if everything went well.

Example request:
```json
{
  "idToken": "this_is_a_fake_id_token"
}
```

The response payload will be a `String` instead of a nested `json` object.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Login fails, probably because *idToken* is invalid |
| 500+ | Unexpected server error |

### PUT /api/posts/:post_id
Request to update an existing post with new title and new content. It is very
similar to creating a new post, except that a valid *post_id* is required in
the URL.

Example request:
```json
{
  "title": "My Updated Title",
  "content": "And my updated post",
  "links": [
    "https://www.examples.com",
    "https://www.lehigh.edu"
  ]
}
```
There is no response payload of this request.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 403 | Editing other user's info |
| 404 | *post_id* cannot be found |
| 500+ | Unexpected server error |

### PUT /api/posts/:post_id/comments/:comment_id
Request to update an existing comment under a particular post. It is very
similar to creating a new comment, except that a valid *comment_id* is also
required in the URL.

Example request:
```json
{
  "content": "Here is my comment",
  "links": [
    "https://www.examples.com",
    "https://www.lehigh.edu"
  ]
}
```
There is no response payload of this request.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 403 | Editing other user's info |
| 404 | *post_id* or *comment_id* cannot be found |
| 500+ | Unexpected server error |

### PUT /api/posts/:post_id/vote
Submit a vote request through this API when the front-end user changes her
vote status on a particular post. The front-end needs to provide the post on
which the user wishes to make the update in URL and the vote status in the
request body.

Example request:
```json
{
  "upVote": false,
  "downVote": false
}
```
There is no response payload for this request.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 400 | Both fields of the quest are `true` |
| 401 | Session key is invalid |
| 404 | *post_id* cannot be found |
| 500+ | Unexpected server error |

### DELETE /api/posts/:post_id/comments/:comment_id
Delete the comment identified by *comment_id* under the post identified by
*post_id*. All parameters should be passed through URI.

There is no request body nor response payload for this request.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 403 | Editing other user's info |
| 404 | *post_id* or *comment_id* cannot be found |
| 500+ | Unexpected server error |

### DELETE /api/posts/:post_id
Delete the post identified by *post_id* along with all comments attached to it.
The *post_id* should be passed through URI.

There is no request body nor response payload for this request.

Return codes:

| Return code | Explanation |
| :---------: | :---------- |
| 200 | OK |
| 401 | Session key is invalid |
| 403 | Editing other user's info |
| 404 | *post_id* cannot be found |
| 500+ | Unexpected server error |

## Database

## Unit Tests

## Integration Test