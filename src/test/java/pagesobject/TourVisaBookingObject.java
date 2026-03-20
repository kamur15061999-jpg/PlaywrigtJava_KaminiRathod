package pagesobject;

import java.nio.file.Paths;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

public class TourVisaBookingObject {

    private Page page;
    public TourVisaBookingObject(Page page) {
        this.page = page;
    }
    // ================= COMMON =================
    public void openHome() {
        page.navigate("https://www.raynatours.com/");
    }

    // ================= TOUR FLOW =================
    public void clickActivities() {
        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Activities Activities")).click();}
    public void goToDubaiActivities() {
        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Things to do in Dubai United")).click();}
    public void selectCruiseFlow() {
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Dhow Cruise")).click();
        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Recommended creek_cruise Dhow")).click();
        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Check Availability")).click();}
    public void selectTourDateGuests() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("More dates")).click();
        page.getByTestId("DateInput.button[1]").click();
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("19")).nth(2).click();
        page.getByTestId("TourGuestInput.button[1]").first().click();
        page.getByTestId("TourGuestInput.button[1]").nth(1).click();
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Select")).first().click();}

    // ================= VISA FLOW =================
    public void selectVisa() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Visas Visas")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Bahrain Visa New")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Book Now")).click();}
    public void fillVisaDetails() {
        page.getByTestId("ResidencyInput.div[1]").getByRole(AriaRole.BUTTON,new Locator.GetByRoleOptions().setName("Select")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("India")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Select")).click();
        page.getByTestId("BaseTextInput.input").click();
        page.getByTestId("BaseTextInput.input").fill("indi");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Indian")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("More dates")).click();
        page.getByTestId("DateInputVisa.button[1]").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("19")).nth(1).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Normal")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("14 Days Bahrain Single Entry")).click();}

     // ================= COMMON BOOKING =================
    public void addToCart() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to Cart")).click();}
    public void checkout() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Cart")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();}
    public void fillUserDetails(String fullName, String email, String phone) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter your full name")).fill(fullName);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter your email")).fill(email);
        page.getByTestId("PhoneNumberInput.div[2]").getByTestId("BaseTextInput.input").fill(phone);}
    // ================= PAYMENT =================
    public void payment(String nameOnCard, String cardNumber, String expiry, String cvv) {
        page.getByTestId("CardDetailsControlled.input").fill(nameOnCard);
        page.getByTestId("CardDetailsControlled.input[1]").fill(cardNumber);
        page.getByTestId("CardDetailsControlled.input[2]").fill(expiry);
        page.getByTestId("CardDetailsControlled.input[3]").fill(cvv);
        page.getByTestId("FormComponent.div[11]").getByTestId("PayButton.button").click();}
    public boolean isPaymentFailed() {
        return page.locator("text=Failed to process payment").isVisible();
    }}