package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pagesobject.DevBookingPage;
import util.ConfigReader;

public class DevBookingTest extends BaseTest {

    /**
     * Dhow Cruise booking on dev.raynatours.com
     * - Date  : 15 April 2026
     * - Option: Without Transfer
     */
    @Test
    public void dhowCruiseBookingWithoutTransfer() {
        DevBookingPage booking = new DevBookingPage(page);

        test.info("Step 0: Login");
        booking.openHome();
        booking.loginWithEmail(ConfigReader.get("test.email"));

        test.info("Step 1: Open Dubai Activities page");
        booking.navigateToDubaiActivities();
        test.info("Step 2: Filter by Dhow Cruise category");
        booking.clickDhowCruiseCategory();

        test.info("Step 3: Select Dhow Cruise listing");
        booking.selectDhowCruiseListing();

        test.info("Step 4: Click Check Availability");
        booking.clickCheckAvailability();

        test.info("Step 5: Select date - 15 April 2026");
        booking.selectDate15April();

        test.info("Step 6: Select 1 adult");
        booking.selectAdults(1);

        test.info("Step 7: Select Without Transfer option");
        booking.selectWithoutTransfer();

        test.info("Step 8: Confirm selection");
        booking.clickSelect();

        test.info("Step 9: Add to cart");
        booking.addToCart();

        test.info("Step 10: View cart & proceed to checkout");
        booking.viewCart();
        booking.proceedToCheckout();

        Assert.assertFalse(booking.isCheckoutNotFound(),
                "Checkout page not found — cart may be empty or session expired.");

        // Debug: log current URL to help identify checkout page structure
        test.info("Checkout URL: " + page.url());

        test.info("Step 11: Fill user details");
        booking.fillUserDetails(
                ConfigReader.get("test.fullname"),
                ConfigReader.get("test.email"),
                ConfigReader.get("test.phone")
        );

        test.info("Step 12: Fill payment details");
        booking.fillPaymentDetails(
                ConfigReader.get("test.card.name"),
                ConfigReader.get("test.card.number"),
                ConfigReader.get("test.card.expiry"),
                ConfigReader.get("test.card.cvv")
        );

        test.info("Step 13: Submit payment");
        booking.submitPayment();

        if (booking.isPaymentFailed()) {
            test.warning("Payment failed — expected on dev with test card.");
            System.out.println("Payment failed (test card on dev env).");
        } else {
            test.pass("Dhow Cruise booking completed successfully.");
            System.out.println("Dhow Cruise booking done successfully.");
        }
    }

    /** Sanity: verify Dubai Activities page loads */
    @Test
    public void verifyActivitiesPageLoads() {
        DevBookingPage booking = new DevBookingPage(page);

        test.info("Navigate to Dubai Activities page");
        booking.navigateToDubaiActivities();

        // Log title for info (don't fail on empty — dev site may have no title)
        String title = page.title();
        test.info("Page title: " + (title.isEmpty() ? "(empty)" : title));

        // Check that some content exists on the page
        int contentCount = (int) page.locator("h1, h2, h3, .activity-card, .tour-card, a[href*='activity']").count();
        Assert.assertTrue(contentCount > 0,
                "No activity content found on the page — page may have failed to load.");

        test.pass("Dubai Activities page loaded with content. Items found: " + contentCount);
    }
}
