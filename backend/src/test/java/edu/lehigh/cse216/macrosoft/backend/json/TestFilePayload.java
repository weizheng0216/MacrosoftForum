package edu.lehigh.cse216.macrosoft.backend.json;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestFilePayload  extends TestCase {
    public TestFilePayload(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestFilePayload.class);
    }

    public void testConstructor() {
        FilePayload file = new FilePayload("12345");
        assertEquals(file.mData, "12345");
    }
}
