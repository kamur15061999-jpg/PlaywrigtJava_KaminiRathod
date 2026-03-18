package tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import base.BaseTest;

public class B2CTest extends BaseTest {
	@Test
	public void test() {
		 page.navigate("https://www.raynatours.com/");
	      page.getByRole(AriaRole.BUTTON).nth(2).click();
	      page.getByRole(AriaRole.BUTTON).nth(2).click();
	      page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Login Now")).click();
	      page.navigate("https://www.raynatours.com/login?redirectTo=%2F%3F");
	      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue with Email")).click();
	      page.getByTestId("LoginForm.div[3]").getByTestId("BaseTextInput.input").click();
	      page.getByTestId("LoginForm.div[3]").getByTestId("BaseTextInput.input").fill("kamini.r@technoheaven.com");
	      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send Link")).click();
	      page.locator(".stroke-gray-600.h-6 > use").click();
	    }
}
