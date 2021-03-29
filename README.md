# Buzz
---

## Project Manager
- Phase 1: Siyu(Coco) Chen
- Phase 2: Haocheng Gao

## Introduction
Buzz provide a platform for the company staffs to illustrate, view, comment,
and vote their ideas through posts.  The administrator can also pin important
posts on the very top of the post list.

## User Guide
Please follow this [link](https://cse216-macrosoft.herokuapp.com/) to access
our application main page.  It's available at `https://cse216-macrosoft.herokuapp.com/`.
Please note you must login with your Google account to user this app. Everyone
else on this platform will be able to see your name, your email, as well as
all the posts and comments you make.  However they cannot view your votes.

Once you logged into the Buzz system, the mage page with a message list on
the left and a viewing area on the right is displayed.  You can click on the
message list to see the details of different posts.  Other operations are listed
below.

### Making Post
To add a new post, click the `New Post` button on the left top of the message
list.  Then follow the UI displayed in the viewing area.  Other users will be
able to view, comment, and vote on your post.  You can edit or delete the post
you made anytime you want.

### Voting
First select the post by clicking on it within the message list.  Then simply
click on the little square button in the bottom of the content of a post to
vote.  You can either `upVote`, `downVote`, or remain `neutral`.  Other users
can only see the total vote counts on posts.

### Commenting
First select the post by clicking on it within the message list.  Then input
your comment text in the textarea on the bottom of the viewing region.  When
you finish, hit `send`.  Other users can see your comments.  If the author of
the post decides to delete the post, your comment under it will be deleted,
too. You may delete or edit your comment later.

### Viewing Others' Profile
To see the profile of a post author or a comment author, click on the button
with a small human icon on the bottom of the post/comment.

### Viewing Your Own Profile
Your profile page is what other people will see about you.  To view it, click
on the `My Profile` button on the top right of the message list.

### Editing Your Posts/Comments
You can edit the posts and comments you made through your profile page.  The
post text and comment text shown in the profile page are enclosed by textarea.
Simply edit those text areas and hit `update` to edit existing text.  To delete
anything, click the `delete` button.

## Developer Guide
Please refer to the following sections to view the details of the different
component of this project.

- [Front-end](web/README.md) is the web-based user interface that runs in browsers.
- [Backend](backend/README.md) is the HTTP server running on Heroku.
- [Admin-cli](admin-cli/README.md) is the app used by the administrator to
  manage the state of Buzz.
