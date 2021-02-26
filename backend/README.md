# This file clarifies the details of the design
```java
public class DataRow{
    public final int mID;
    public String mTitle;
    public String mContent;
    public String mUserName;
    //Note: using String instead of Data to store date
    public String mDate;
    public int mUpVote;
    public int mDownVote;

    ...(Constructors)
}
```

```java
//display all messages base on votes. All pinned messages are in front of the array
    Arraylist<DataRow> selectAllVotes(); 

//display all message base on date. All pinned messages are in front of the array
	Arraylist<DataRow> selectAllDate(); 

//For front end, incease a message's v_up by 1. Return v_up if succeed, -1 otherwise.
    void voteUpOne(int id);

//For front end, incease a message's v_down by 1. Return v_down if succeed, -1 otherwise.
    int voteDownOne(int id);

// For administrator, edit a message. return m_id if succeed, -1 to indicate error
	int updateOne(int id, String title, String content, String username);

// For administrator, delete a message. return m_id if succeed, return -1 to indicate error
	int deleteOne(int id) 

// For front end and administrator, create a new message. return m_id of new message, -1 to indicate error
	int insertOne(String title, String content, String username) 

// For administrator, stick a message on the top
	int topOne(int id) 

```

```java 
//format of getting all data base on votes

/*
* return type: structResponse 
* access data through "mData"
* mData is a arrayList of DataRow
* example: mData[i].mContent
*/
{
    type: "GET",
    url: "/messages/sortbyvotes",
    dataType: "json",
    ...
}



//format of getting all data base on data
{
    type: "GET",
    url: "/messages/sortbydate",
    dataType: "json",
    ...
}


//format of getting one data（maybe use after update votes）
/*
* return type: structResponse 
* access data through "mData"
* mData is a class of DataRow
* example: mData.mContent
*/
{
    type: "GET",
    // id should be mID of this message
    url: "/messages/"+id,
    dataType: "json",
    ...
}

//format of upvote a message. 
// mdata is new up vote count
{
    type: "PUT",
    // id should be mID of this message
    url: "/messages/upvote/" + id,
    dataType: "json",
    ...
}

//format of downvote a message. 
//mdata is new down vote count
{
    type: "PUT",
    // id should be mID of this message
    url: "/messages/downvote/" + id,
    dataType: "json",
    ...
}


//format of insert(create) a message
{
    type: "POST",
    url: "/messages",
    dataType: "json",
    // upvote and downvote are 0 when message is created
    data: JSON.stringify({ mTitle: title, mContent: content, mUserName: username}),
    ...
}



```