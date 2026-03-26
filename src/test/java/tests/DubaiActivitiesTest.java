package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.SkipException;
import pagesobject.DubaiActivitiesPage;

import java.util.Arrays;
import java.util.List;

public class DubaiActivitiesTest extends BaseTest {

    @Test
    public void dubaiActivitiesLoadMoreAndVerify() {
        String testName = "dubaiActivitiesLoadMoreAndVerify";
        DubaiActivitiesPage pageObj = new DubaiActivitiesPage(page);

        // 1. Open page
        pageObj.navigateToPage();

        // Quick guard: detect an obvious maintenance message and skip test to avoid false failures
        List<String> initialTitles = pageObj.collectVisibleTitles();
        if (initialTitles.stream().anyMatch(t -> t != null && t.toLowerCase().contains("we'll be right back"))) {
            System.out.println("Detected maintenance/placeholder page. Skipping test.");
            throw new SkipException("Site appears to be under maintenance or placeholder page present: " + initialTitles);
        }

        // 2. Click load more a few times
        pageObj.clickLoadMoreMultipleTimes(3);

        // wait for lazy images to settle after load more
        page.waitForTimeout(3000);

        // 3. Verify images loaded
        pageObj.verifyVisibleImagesLoaded(testName);

        // 4. Collect titles and run a relaxed check: ensure there is at least one meaningful title
        List<String> titles = pageObj.collectVisibleTitles();
        // Log titles so test output shows what's present (useful for debugging)
        System.out.println("Collected titles: " + titles);

        Assert.assertFalse(titles.isEmpty(), "No visible titles were collected from the page. This may indicate the selector needs adjustment or the page failed to load correctly.");

        // Relaxed presence check: ensure at least one title mentions 'Dubai' or common activity words
        boolean found = titles.stream().anyMatch(t -> t.toLowerCase().contains("dubai")
                || t.toLowerCase().contains("desert")
                || t.toLowerCase().contains("safari")
                || t.toLowerCase().contains("cruise")
                || t.toLowerCase().contains("dhow")
        );

        Assert.assertTrue(found, "None of the collected titles mention expected keywords (dubai/desert/safari/cruise/dhow). Actual titles: " + titles);
    }
}