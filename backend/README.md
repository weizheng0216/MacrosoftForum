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

- [GET /api/posts](#api-01)
- [GET /api/users/my](#api-02)
- [GET /api/users/:user_id](#api-03)
- [POST /api/posts](#api-04)
- [POST /api/posts/:post_id/comments](#api-05)
- [POST /api/auth](#api-06)
- [PUT /api/posts/:post_id](#api-07)
- [PUT /api/posts/:post_id/comments/:comment_id](#api-08)
- [PUT /api/posts/:post_id/vote](#api-09)
- [DELETE /api/posts/:post_id/comments/:comment_id](#api-10)
- [DELETE /api/posts/:post_id](#api-11)

The backend provides a set of APIs to collaborate with the front-end application.
All APIs listed above, except `POST /api/auth`, which is explicitly used for
authentication, require a **session key** in the URL query: `?session=[session_key]`.
If the session key is missing, or if the session key is not valid or has expired,
the server will respond with an authentication error with code `401`.

All APIs will reply with a comment format called *StructuredResponse*. It contains
the status, the message, and a nullable payload. The optional payload could be in
various `json` format that holds the data which the front-end is requesting.

Here is an example. Note that *mData* will be `null` or various types of objects
depending on the type of the response.

```json
{
  "mStatus": "OK",
  "mMessage": "This is an example response",
  "mData": {}  // payload
}
```

### <span id="api-01">GET `/api/posts`</span>
Get all post information, including all the posts, their comments, the author of
the comments, and the vote status of the requesting user.

The response payload has the following format.

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
    "mComments": [
      {
        "mCommentID": 333,
        "mAuthor": {
          "mUserID": 202,
          "mEmail": "example2@lehigh.edu",
          "mFirstName": "first",
          "mLastName": "last"
        },
        "mContent": "This is a comment",
        "mDate": "2019-02-01 12:46:39"
      }
      // ... other comments ...
    ],
    "mUserUpVote": true,
    "mUserDownVote": false
  }
  // ... other posts ...
]
```

### <span id="api-02">GET `/api/users/my`</span>
Get the profile of the front-end user, which will be displayed at the profile
page. An example of the response `json` is shown below.

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
      "mPinned": false
    }
    // ... other posts ...
  ],
  "mComments": [
    {
      "mCommentID": 555,
      "mPostID": 123,
      "mContent": "My comment 1",
      "mDate": "2018-02-18 14:00:06"
    }
    // ... other comments ...
  ]
}
```

### <span id="api-03">GET `/api/users/:user_id`</span>
Get the profile of the specified user, which will be displayed at the post
or profile window. This is very similar to `GET /api/users/my`, therefore I
will not provide another example response.

### <span id="api-04">POST `/api/posts`</span>
Request to add a new post. The front-end needs to provide the title and the
content of the post. The server will record the author information based on
the *session key*.

There is no response payload for this request.

Example request:
```json
{
  "title": "My Title",
  "content": "And my post"
}
```

### <span id="api-05">POST `/api/posts/:post_id/comments`</span>
Request to add a new comment to an existing post. The target *post_id* is to
be provided in the URL, and the front-end only needs to provide the content
of the comment in its request body, which is something like:

There is no response payload of this request.

If the *post_id* cannot be found, a `404` error is returned.

Example request:
```json
{
  "content": "Here is my comment"
}
```

### <span id="api-06">POST `/api/auth`</span>
This is the login API for **BUZZ**. The front-end needs to pass the *idToken*
obtained through Google's authentication process. This *idToken* will be used
by the backend to authenticate/verify with Google. A *session key* will be
returned if everything went well.

If the login fails, an error with code `401` will be returned.

Example request:
```json
{
  "idToken": ".............................."
}
```

The response payload will be a `String` instead of a nested `json` object.

### <span id="api-07">PUT `/api/posts/:post_id`</span>
Request to update an existing post with new title and new content. It is very
similar to creating a new post, except that a valid *post_id* is required in
the URL.

An error of `404` will be returned if the *post_id* provided is invalid.

There is no response payload of this request.

Example request:
```json
{
  "title": "My Updated Title",
  "content": "And my updated post"
}
```

### <span id="api-08">PUT `/api/posts/:post_id/comments/:comment_id`</span>
Request to update an existing comment under a particular post. It is very
similar to creating a new comment, except that a valid *comment_id* is also
required in the URL.

If either *post_id* or *comment_id* is not valid, an error with code `404`
will be returned.

There is no response payload of this request.

Example request:
```json
{
  "content": "Here is my comment"
}
```

### <span id="api-09">PUT `/api/posts/:post_id/vote`</span>
Submit a vote request through this API when the front-end user changes her
vote status on a particular post. The front-end needs to provide the post on
which the user wishes to make the update in URL and the vote status in the
request body.

A `404` error will be returned if the *post_id* is invalid. A `401` error will
be returned if both of the request fields are `true`.

There is no response payload for this request.

Example request:
```json
{
  "upVote": false,
  "downVote": false
}
```

### <span id="api-10">DELETE `/api/posts/:post_id/comments/:comment_id`</span>
Delete the comment identified by *comment_id* under the post identified by
*post_id*. All parameters should be passed through URI.

If either *post_id* or *comment_id* is not valid, an error with code `404`
will be returned.

There is no request body nor response payload for this request.

### <span id="api-11">DELETE `/api/posts/:post_id`</span>
Delete the post identified by *post_id* along with all comments attached to it.
The *post_id* should be passed through URI.

If the *post_id* provided is invalid, a `404` error will be thrown.

There is no request body nor resposne payload for this request.

## Database

## Unit Tests

## Integration Test