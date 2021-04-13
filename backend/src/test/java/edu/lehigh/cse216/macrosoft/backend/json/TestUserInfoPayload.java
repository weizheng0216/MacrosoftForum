package edu.lehigh.cse216.macrosoft.backend.json;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestUserInfoPayload extends TestCase {
    public TestUserInfoPayload(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestUserInfoPayload.class);
    }

    public void testConstructor() {
        UserInfoPayload payload = new UserInfoPayload(null, null, null);
        assertNull(payload.mUser);
        assertNull(payload.mPosts);
        assertNull(payload.mComments);
    }
}
