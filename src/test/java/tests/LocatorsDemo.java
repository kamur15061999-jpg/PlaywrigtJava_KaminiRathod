package tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;

import base.BaseTest;

public class LocatorsDemo extends BaseTest {
	
	@Test
	public void testAllLocators() {
		
		
		page.navigate("https://instance.raynab2b.in/");
	

		// Click "Continue with Email" button
		page.locator("#txtAgentcode").fill("AGT-30697");
		page.locator("input[id=txtUsername]").fill("raynatechno@gmail.com");
		page.locator("input[id=txtPassword]").fill("Rathod@AKR207951506");
		
		
		page.locator("input[id=chkRememberMe]").click();
		
		page.pause();

}}
