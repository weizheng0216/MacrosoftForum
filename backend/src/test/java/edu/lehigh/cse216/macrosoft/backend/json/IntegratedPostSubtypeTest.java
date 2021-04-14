package edu.lehigh.cse216.macrosoft.backend.json;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class IntegratedPostSubtypeTest extends TestCase {
    public IntegratedPostSubtypeTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(IntegratedPostSubtypeTest.class);
    }

    public void testConstructor() {
        PostSubtype post = new PostSubtype(
                12345,
                "title",
                "content",
                null,
                "today",
                5,
                10,
                false,
                null,
                null,
                null
        );
        IntegratedPostSubtype intPost = IntegratedPostSubtype.fromBasicPost(
                post, true, false);
        assertNull(intPost.mAuthor);
        assertNull(intPost.mFileInfo);
        assertEquals(intPost.mDownVoteCount, 10);
    }
}
