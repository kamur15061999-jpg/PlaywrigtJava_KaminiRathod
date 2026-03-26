package pagesobject;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Page Object for dev.raynatours.com booking flow.
 * Covers: Dhow Cruise booking → date selection (15 Apr 2026) → Without Transfer → cart → checkout → payment
 */
public class DevBookingPage {

    private final Page page;
    private static final String BASE_URL = "https://dev.raynatours.com/";

    public DevBookingPage(Page page) {
        this.page = page;
    }

    // ─────────────────────────────────────────
    // NAVIGATION
    // ─────────────────────────────────────────

    
    // LOGIN
    public void loginWithEmail(String email) {
        page.getByRole(AriaRole.BUTTON).nth(2).click();
        page.waitForTimeout(500);
        try {
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Login Now")).click();
            page.waitForLoadState();
        } catch (Exception ignored) {}
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue with Email")).click();
        page.waitForTimeout(500);
        page.getByTestId("LoginForm.div[3]").getByTestId("BaseTextInput.input").fill(email);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send Link")).click();
        page.waitForTimeout(2000);
        try { page.locator(".stroke-gray-600.h-6 > use").click(); } catch (Exception ignored) {}
    }
    public void openHome() {
        page.navigate(BASE_URL);
        page.waitForLoadState();
    }

    public void navigateToDubaiActivities() {
        page.navigate(BASE_URL + "dubai-activities");
        page.waitForLoadState();
        waitForVisible("h1, .activity-card, .tour-card, .listing-item");
    }

    // ─────────────────────────────────────────
    // DHOW CRUISE FLOW
    // ─────────────────────────────────────────

    /** Click "Dhow Cruise" category filter on Dubai Activities page */
    public void clickDhowCruiseCategory() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Dhow Cruise")).click();
        page.waitForTimeout(1000);
    }

    /** Click the recommended Dhow Cruise listing */
    public void selectDhowCruiseListing() {
        // Try recommended listing first, fallback to first Dhow Cruise link
        try {
            page.getByRole(AriaRole.LINK,
                    new Page.GetByRoleOptions().setName("Recommended creek_cruise Dhow"))
                    .click();
        } catch (Exception e) {
            page.getByRole(AriaRole.LINK,
                    new Page.GetByRoleOptions().setName("Dhow")).first().click();
        }
        page.waitForLoadState();
    }

    /** Click Check Availability button */
    public void clickCheckAvailability() {
        page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Check Availability")).click();
        page.waitForLoadState();
    }

    /**
     * Select date: opens date picker and clicks on day 15 (April 2026).
     * Navigates to next month if needed.
     */
    public void selectDate15April() {
        // Open date picker
        try {
            Locator moreDates = page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("More dates"));
            if (moreDates.count() > 0 && moreDates.first().isVisible()) {
                moreDates.first().click();
                page.waitForTimeout(500);
            }
        } catch (Exception ignored) {}

        // Click the first DateInput slot to open calendar
        try {
            page.getByTestId("DateInput.button[1]").click();
            page.waitForTimeout(500);
        } catch (Exception ignored) {}

        // Navigate to April 2026 if not already there
        navigateCalendarToApril2026();

        // Click day 15
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("15")).first().click();
        page.waitForTimeout(500);
    }

    /** Navigate calendar forward until April 2026 is shown */
    private void navigateCalendarToApril2026() {
        for (int i = 0; i < 12; i++) {
            String headerText = "";
            try {
                headerText = page.locator(".calendar-header, .month-year, [class*='month'], [class*='calendar'] h2, [class*='calendar'] h3")
                        .first().textContent();
            } catch (Exception ignored) {}

            if (headerText.contains("April") && headerText.contains("2026")) break;
            if (headerText.contains("Apr") && headerText.contains("2026")) break;

            // Click next month arrow
            try {
                Locator nextBtn = page.locator("button[aria-label='Next month'], button[aria-label='next'], .next-month, [class*='next']").first();
                if (nextBtn.isVisible()) {
                    nextBtn.click();
                    page.waitForTimeout(300);
                } else break;
            } catch (Exception ignored) { break; }
        }
    }

    /** Select adult guests — clicks + button given number of times */
    public void selectAdults(int count) {
        for (int i = 0; i < count; i++) {
            try {
                page.getByTestId("TourGuestInput.button[1]").first().click();
                page.waitForTimeout(200);
            } catch (Exception ignored) { break; }
        }
    }

    /** Select "Without Transfer" option */
    public void selectWithoutTransfer() {
        try {
            // Try exact button text first
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Without Transfer")).click();
            page.waitForTimeout(500);
        } catch (Exception e) {
            // Fallback: look for any element containing "Without Transfer"
            page.locator("text=Without Transfer").first().click();
            page.waitForTimeout(500);
        }
    }

    /** Click Select to confirm option */
    public void clickSelect() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Select")).first().click();
        page.waitForTimeout(500);
    }

    // ─────────────────────────────────────────
    // CART & CHECKOUT
    // ─────────────────────────────────────────

    public void addToCart() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Add to Cart")).click();
        page.waitForTimeout(1500);
    }

    public void viewCart() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("View Cart")).click();
        page.waitForLoadState();
    }

    public void proceedToCheckout() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Next")).click();
        page.waitForLoadState();
    }

    // ─────────────────────────────────────────
    // USER DETAILS
    // ─────────────────────────────────────────

    public void fillUserDetails(String fullName, String email, String phone) {
        page.waitForLoadState();
        page.waitForTimeout(2000);

        // Wait until any input field is visible on checkout form
        waitForVisible("input[type='text'], input[type='email'], input[type='tel']");

        // Full name
        findTextbox("Enter your full name", "Full Name", "Name", "full name").fill(fullName);

        // Email
        findTextbox("Enter your email", "Email", "Email Address", "email").fill(email);

        // Phone
        try {
            page.getByTestId("PhoneNumberInput.div[2]")
                    .getByTestId("BaseTextInput.input").fill(phone);
        } catch (Exception e) {
            findTextbox("Phone", "Mobile", "Phone Number", "mobile number").fill(phone);
        }
    }

    /** Try multiple label names for a textbox, return first match */
    private Locator findTextbox(String... labels) {
        for (String label : labels) {
            try {
                Locator l = page.getByRole(AriaRole.TEXTBOX,
                        new Page.GetByRoleOptions().setName(label));
                if (l.count() > 0) return l.first();
            } catch (Exception ignored) {}
        }
        // fallback: return first visible input
        return page.locator("input[type='text'], input[type='email'], input:not([type])").first();
    }

    // ─────────────────────────────────────────
    // PAYMENT
    // ─────────────────────────────────────────

    public void fillPaymentDetails(String nameOnCard, String cardNumber,
                                   String expiry, String cvv) {
        page.getByTestId("CardDetailsControlled.input").fill(nameOnCard);
        page.getByTestId("CardDetailsControlled.input[1]").fill(cardNumber);
        page.getByTestId("CardDetailsControlled.input[2]").fill(expiry);
        page.getByTestId("CardDetailsControlled.input[3]").fill(cvv);
    }

    public void submitPayment() {
        page.getByTestId("FormComponent.div[11]")
                .getByTestId("PayButton.button").click();
        page.waitForTimeout(3000);
    }

    public boolean isPaymentFailed() {
        return page.locator("text=Failed to process payment").isVisible();
    }

    public boolean isCheckoutNotFound() {
        return page.locator("text=not found, text=404, text=No items").first().isVisible();
    }

    // ─────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────

    private void waitForVisible(String selector) {
        try {
            page.waitForSelector(selector,
                    new Page.WaitForSelectorOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(10000));
        } catch (Exception ignored) {}
    }
}
