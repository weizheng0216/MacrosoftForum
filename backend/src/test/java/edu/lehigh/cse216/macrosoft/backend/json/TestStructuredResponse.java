package edu.lehigh.cse216.macrosoft.backend.json;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

public class TestStructuredResponse extends TestCase {
    public TestStructuredResponse(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestStructuredResponse.class);
    }

    public void testConstructor() {
        StructuredResponse res = new StructuredResponse("OK", null, null);
        assertEquals(res.mStatus, "OK");
        assertNull(res.mMessage);
        assertNull(res.mData);
    }

    public void testShortHand() {
        StructuredResponse res = StructuredResponse.OK("12345");
        assertEquals(res.mData, "12345");
    }
}
