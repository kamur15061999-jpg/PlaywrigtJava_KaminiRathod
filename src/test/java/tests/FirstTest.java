package tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import base.BaseTest;

public class FirstTest extends BaseTest {
	@Test
	public void verifyTitle() {
		page.navigate("https://raynatours.com/ncr");
		
		if (page.isVisible("button:has-text('Accept all')")) {
			page.click("button:has-text('Accept all')");
			
		}
		
	}
	
	//public static void main(String[] args) {
		
		//try(Playwright playwright = Playwright.create()){
			
		//	Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		
			//Page page = browser.newPage();
			
			//page.navigate("https://www.raynatours.com/");
			
			
		    //System.out.println("The page title is :"+page.title());
			//browser.close();
			
	//	}
	//}

}
