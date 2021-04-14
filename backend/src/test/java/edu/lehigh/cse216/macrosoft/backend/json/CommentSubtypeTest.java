package edu.lehigh.cse216.macrosoft.backend.json;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

public class CommentSubtypeTest extends TestCase {
    public CommentSubtypeTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(CommentSubtypeTest.class);
    }

    public void testConstructor() {
        CommentSubtype comment = new CommentSubtype(
                10, 20, "Test", null,
                "Date", null, new ArrayList<>()
        );
        assertEquals(comment.mCommentID, 10);
        assertEquals(comment.mPostID, 20);
        assertEquals(comment.mContent, "Test");
        assertNull(comment.mFileInfo);
        assertEquals(comment.mLinks.size(), 0);
    }
}
