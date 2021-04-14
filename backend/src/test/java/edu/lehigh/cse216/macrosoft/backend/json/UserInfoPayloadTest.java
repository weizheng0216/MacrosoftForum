package edu.lehigh.cse216.macrosoft.backend.json;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UserInfoPayloadTest extends TestCase {
    public UserInfoPayloadTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(UserInfoPayloadTest.class);
    }

    public void testConstructor() {
        UserInfoPayload payload = new UserInfoPayload(null, null, null);
        assertNull(payload.mUser);
        assertNull(payload.mPosts);
        assertNull(payload.mComments);
    }
}
