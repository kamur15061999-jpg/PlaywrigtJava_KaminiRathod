package tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import base.BaseTest;
import pagesobject.LoginPage;

public class LoginTest2  extends BaseTest {

	    	@Test
	    	public void logintest1() {

	    	    LoginPage login = new LoginPage(page);
	    	   
	    	    test.info("Starting login flow");
	    	    login.loginWithEmail("kamini.r@technoheaven.com");
	    	    
	    	    page.waitForTimeout(10000);

	    	    test.info("Login flow completed");
	    	}
@Test
public void logintest2() {

    test.skip("Login flow completed");
	throw new SkipException("Skipped this test");
}
@Test
public void logintest3() {

    test.info("Starting login flow");

    page.waitForTimeout(10000);

    test.info("Login flow completed");

    // ❌ Force this test to fail
    Assert.fail("Intentionally failing test 3");
}}


