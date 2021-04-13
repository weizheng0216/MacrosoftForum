package edu.lehigh.cse216.macrosoft.backend.json;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestUserSubtype extends TestCase {
    public TestUserSubtype(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestUserSubtype.class);
    }

    public void testConstructor() {
        UserSubtype user = new UserSubtype(123, "email",
                "first", "last");
        assertEquals(user.mUserID, 123);
        assertEquals(user.mEmail, "email");
        assertEquals(user.mFirstName, "first");
        assertEquals(user.mLastName, "last");
    }
}
