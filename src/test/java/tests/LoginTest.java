package tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import base.BaseTest;


public class LoginTest extends BaseTest {

	
	@Test
	public void test() {
	    //try (Playwright playwright = Playwright.create()) {
	    // Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
	      //  .setHeadless(false));
	      //BrowserContext context = browser.newContext();
	     // Page page = context.newPage();
		page.navigate("http://webmail.technoheaven.com/");
	      page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email Address")).fill("kamini.r@technoheaven.com");
	      page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).click();
	      page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill("x(^+,afz5!fw");
	      page.locator(".login-btn").click();
	      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Open your inbox")).click();
	    
	    }
	  }
	

