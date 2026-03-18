package tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import base.BaseTest;

@Deprecated
class OldB2CTest extends BaseTest {
	@Test
	public void test() {
		// This is a legacy stub kept for history. Prefer using tests.B2CTest instead.
		page.navigate("https://www.raynatours.com/");

		// Accept cookies if popup appears (pattern used in other tests)
		if (page.isVisible("button:has-text('Accept all')")) {
			page.click("button:has-text('Accept all')");
		}

		// If there is a modal overlay that intercepts clicks, wait for it to be hidden.
		try {
			page.waitForSelector("[data-testid='Dialog.div']", new Page.WaitForSelectorOptions()
				.setState(WaitForSelectorState.HIDDEN)
				.setTimeout(5000));
		} catch (Exception e) {
			// ignore - proceed if the overlay doesn't disappear in time
		}

		// Minimal interactions kept as a stub. Use B2CTest for the full flow.
	}

}
