package edu.lehigh.cse216.macrosoft.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SimpleRequestTest extends TestCase{

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SimpleRequestTest(String testName){
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(SimpleRequestTest.class);
    }
 
    public void testMyself() {
        assertTrue(true);
    }
}