package edu.lehigh.cse216.macrosoft.backend.json;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPostSubtype  extends TestCase {
    public TestPostSubtype(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestPostSubtype.class);
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
        assertEquals(post.mTitle, "title");
        assertEquals(post.mDate, "today");
    }
}
