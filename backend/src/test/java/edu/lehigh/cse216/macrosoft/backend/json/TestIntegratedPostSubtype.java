package edu.lehigh.cse216.macrosoft.backend.json;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestIntegratedPostSubtype  extends TestCase {
    public TestIntegratedPostSubtype(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestIntegratedPostSubtype.class);
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
