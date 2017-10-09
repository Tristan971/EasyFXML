package moe.tristan.easyfxml;

import org.junit.Before;

import static org.junit.Assume.assumeFalse;

/**
 * This class skips all extending test classes tests if we are in a CI
 * environment. Depending on the reason it generally means (unless commented
 * appropriately) that it is unreliable/failing in CI despite being
 * actually testing working code when used in a non-CI environment.
 */
public class CIIncompatibleTest {
    @Before
    public void setUp() {
        final String envProperty = System.getProperty("env");
        if (envProperty != null) {
            assumeFalse(System.getProperty("env").equals("ci"));
        }
        System.out.println("WAS NOT CI ENVIRONMENT. EXECUTING TEST.");
    }
}
