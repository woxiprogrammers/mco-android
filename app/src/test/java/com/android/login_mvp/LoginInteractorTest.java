package com.android.login_mvp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class LoginInteractorTest {
    private LoginInteractor loginInteractor;

    @Before
    public void setup() {
        loginInteractor = new LoginInteractor();
        System.out.println("Ready for testing!");
    }

    @After
    public void cleanup() {
        System.out.println("Done with unit test!");
    }

    @BeforeClass
    public static void testClassSetup() {
        System.out.println("Getting test class ready");
    }

    @AfterClass
    public static void testClassCleanup() {
        System.out.println("Done with tests");
    }

    @Test
    public void testAdd() {
        loginInteractor = new LoginInteractor();
//        int total = calculator.add(4, 5);
//        assertEquals("Calculator is not adding correctly", 9, total);
//        loginInteractor.login("asdfg", "sdfsd", );
    }
}