package edu.lehigh.cse216.macrosoft.backend.json;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestFileInfoSubtype  extends TestCase {
    public TestFileInfoSubtype(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestFileInfoSubtype.class);
    }

    public void testConstructor() {
        FileInfoSubtype file = new FileInfoSubtype(
                "png", "0_0", "test.png");
        assertEquals(file.mType, "png");
        assertEquals(file.mTime, "0_0");
        assertEquals(file.mName, "test.png");
    }
}
