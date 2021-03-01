package edu.lehigh.cse216.macrosoft.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StructureResponseTest extends TestCase{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StructureResponseTest(String testName){
        super(testName);
    }
    
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(StructureResponseTest.class);
    }

    public void testMyself() {
        assertTrue(true);
    }

    public void testConstructorWithStatusNull(){
        String message = "Test Message";
        Object data = new Object();
        StructuredResponse test1  = new StructuredResponse(null, message, data);
        assertTrue(test1.mStatus.equals("invalid"));
        assertTrue(test1.mMessage.equals(message));
        assertTrue(test1.mData.equals(data));
    }

    public void testConstructorWithNormalCase(){
        String status = "Test Status";
        String message = "Test Message";
        Object data = new Object();
        StructuredResponse test1  = new StructuredResponse(status, message, data);
        assertTrue(test1.mStatus.equals(status));
        assertTrue(test1.mMessage.equals(message));
        assertTrue(test1.mData.equals(data));
    }
}