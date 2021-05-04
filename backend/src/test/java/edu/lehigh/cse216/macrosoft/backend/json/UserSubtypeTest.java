package edu.lehigh.cse216.macrosoft.backend.json;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UserSubtypeTest extends TestCase {
    public UserSubtypeTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(UserSubtypeTest.class);
    }

    public void testConstructor() {
        UserSubtype user = new UserSubtype(123, "email",
                "first", "last", false);
        assertEquals(user.mUserID, 123);
        assertEquals(user.mEmail, "email");
        assertEquals(user.mFirstName, "first");
        assertEquals(user.mLastName, "last");
        assertEquals(user.mBlocked, false);
    }
}
