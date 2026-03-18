package pagesobject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {

	private Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    // Actions (converted from your script)

    public void navigateToSite() {
        page.navigate("https://www.raynatours.com/");
    }

    public void openLoginMenu() {
        page.getByRole(AriaRole.BUTTON).nth(2).click();
        page.getByRole(AriaRole.BUTTON).nth(2).click();
    }

    public void clickLoginNow() {
        page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Login Now")).click();
    }

    public void clickContinueWithEmail() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue with Email")).click();
    }

    public void enterEmail(String email) {
        page.getByTestId("LoginForm.div[3]")
            .getByTestId("BaseTextInput.input")
            .click();

        page.getByTestId("LoginForm.div[3]")
            .getByTestId("BaseTextInput.input")
            .fill(email);
    }

    public void clickSendLink() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Send Link")).click();
    }

    public void closePopup() {
        page.locator(".stroke-gray-600.h-6 > use").click();
    }

    // 🔥 Business Flow Method
    public void loginWithEmail(String email) {
        navigateToSite();
        openLoginMenu();
        clickLoginNow();
        clickContinueWithEmail();
        enterEmail(email);
        clickSendLink();
    }
}