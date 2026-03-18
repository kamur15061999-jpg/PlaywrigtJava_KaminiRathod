package pagesobject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

public class HomePage {
	
	
	private Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    // 🔹 Locators
    private String activitiesMenu = "text=Activities";
    
    
    
    public void clickActivities() {
        page.click(activitiesMenu);
    }
    
    public void searchTour(String tourName) {
        page.fill(tourName, tourName);
        page.keyboard().press("Enter");
    }

    // 🔹 Validation (important after login)
    public void verifyHomePageLoaded() {
        PlaywrightAssertions.assertThat(page.locator(activitiesMenu)).isVisible();
    }

    public void verifyUserLoggedIn() {
        PlaywrightAssertions.assertThat(page.locator(activitiesMenu)).isVisible();
    }

    {
    	
    
    
    }
    }