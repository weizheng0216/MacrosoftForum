package edu.lehigh.cse216.macrosoft.backend.json;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FilePayloadTest extends TestCase {
    public FilePayloadTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(FilePayloadTest.class);
    }

    public void testConstructor() {
        FilePayload file = new FilePayload("12345");
        assertEquals(file.mData, "12345");
    }
}
