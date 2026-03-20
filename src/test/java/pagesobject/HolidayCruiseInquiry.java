package pagesobject;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

public class HolidayCruiseInquiry {

    private Page page;

    public HolidayCruiseInquiry(Page page) {
        this.page = page;
    }

    // ================= HOME =================
    public void navigateToHome() {
        page.navigate("https://www.raynatours.com/");
    }

    public void clickHolidays() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Holidays Holidays")).click();
    }

    public void clickDubaiPackages() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Holidays Packages in Dubai")).click();
    }

    public void clickCruises() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cruises Cruises")).click();
    }

    // ================= Holiday Flow =================
    public void selectDubaiPackage() {
        page.navigate("https://www.raynatours.com/dubai-packages");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Dubai Budget Delight New")).click();
    }

    public void sendDubaiEnquiry() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Email")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("More dates")).click();
        page.getByTestId("DateInputHoliday.button[1]").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("18")).nth(1).click();
        page.locator("button:has-text('No, I haven’t')").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send Request")).click();
        
        page.waitForTimeout(5000);
    }

    // ================= CRUISE FLOW =================
    public void selectCruise() {
        page.navigate("https://www.raynatours.com/cruises");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cruises from Dubai United")).click();
        page.getByTestId("CruiseBookingCard.div").click();
    }

    public void sendCruiseEnquiry() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Email")).click();
        page.getByTestId("DateInputForCruise.div[6]").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Interior Cabin Total Price INR")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send Request")).click();
    }

    // ================= FORM =================
    public void fillForm(String name, String phone, String email, String msg) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter your full name")).fill(name);
        page.getByTestId("PhoneNumberInput.div[2]").getByTestId("BaseTextInput.input").fill(phone);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter your email")).fill(email);
        page.getByTestId("EnquiryFormComponent.div[5]").getByTestId("BaseTextInput.input").fill(msg);
    }

    public void submitForm() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Request")).click();
        
        page.waitForTimeout(5000);
    }
}