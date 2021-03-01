package edu.lehigh.cse216.macrosoft.backend;

import java.sql.DataTruncation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DataRowTest extends TestCase{

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DataRowTest(String testName){
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DataRowTest.class);
    }

    public void testMyself() {
        assertTrue(true);
    }

    /**
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public void testConstructor() {
        int id = 17;
        String title = "Test Title";
        String content = "Test Content";
        String userName = "Test Username";
        String date = "Test date";
        int upVote = 1;
        int downVote = 2;
        boolean pinned = true;
        DataRow d = new DataRow(id,title,content,userName,date,upVote,downVote,pinned);
        assertTrue(d.mTitle.equals(title));
        assertTrue(d.mContent.equals(content));
        assertTrue(d.mDate.equals(date));
        assertTrue(d.mID == id);
        assertTrue(d.mUpVote == upVote);
        assertTrue(d.mDownVote == downVote);
        assertTrue(d.mPinned == pinned);
    }

    /**
     * Ensure that the copy constructor works correctly
     */
    public void testCopyconstructor() {
        int id = 100;
        String title = "Test Copy Title";
        String content = "Test Copy Content";
        String userName = "Test Copy Username";
        String date = "Test Copy date";
        int upVote = 3;
        int downVote = 5;
        boolean pinned = true;
        DataRow d = new DataRow(id,title,content,userName,date,upVote,downVote,pinned);
        DataRow d2 = new DataRow(d);
        assertTrue(d2.mTitle.equals(title));
        assertTrue(d2.mContent.equals(content));
        assertTrue(d2.mDate.equals(date));
        assertTrue(d2.mID == id);
        assertTrue(d2.mUpVote == upVote);
        assertTrue(d2.mDownVote == downVote);
        assertTrue(d2.mPinned == pinned);
    }
}