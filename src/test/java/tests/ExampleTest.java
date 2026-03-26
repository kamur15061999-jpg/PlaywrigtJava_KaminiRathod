package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pagesobject.ExamplePage;
import org.testng.SkipException;
import util.Screenshot;

import java.util.List;
import java.util.Objects;

public class ExampleTest extends BaseTest {

    @Test
    public void exampleFlowLoadMoreAndVerify() {
        String testName = "exampleFlowLoadMoreAndVerify";
        ExamplePage ep = new ExamplePage(page);

        // 1. Navigate home
        ep.navigateHome();

        // 2. Open activities and the Dubai listing
        ep.openActivities();
        ep.openDubaiThingsToDo();

        // Quick guard: collect titles and skip if placeholder/maintenance
        List<String> initial = ep.collectVisibleTitles();
        if (initial.stream().filter(Objects::nonNull).map(String::toLowerCase).anyMatch(t -> t.contains("we'll be right back") || t.contains("we will be right back"))) {
            System.out.println("Detected maintenance/placeholder page. Skipping test.");
            throw new SkipException("Site appears to be under maintenance or placeholder page present: " + initial);
        }

        // 3. Set currency and search
        ep.setCurrencyToAED();
        ep.searchFor("dhow");

        // 4. Toggle menu and open filter/sort
        ep.toggleMenuIcon();
        ep.openSortAndFilter();

        // Apply some filters/sorts matching the scripted clicks
        ep.applyFilterByName("Instant Cashback");
        ep.applyFilterByName("Super Saver");
        ep.applySort("Low to high");

        // 5. Click category and load more several times
        ep.clickCategory("City Tours");
        ep.clickLoadMoreMultipleTimes(8);

        // Optionally force images to break for demonstration: set system property -DforceImageBreak=true
        boolean forceBreak = Boolean.parseBoolean(System.getProperty("forceImageBreak", "false"));
        if (forceBreak) {
            try {
                page.evaluate("() => { const imgs = Array.from(document.images || []); imgs.forEach(i => { try { i.src = 'http://invalid.invalid/404.png'; } catch(e){} }); }");
                // give the browser a moment to try loading the invalid srcs
                page.waitForTimeout(1000);
            } catch (Exception ignored) {}
        }

        // 6. Verify images loaded — use non-asserting finder to allow reporting without failing if desired
        List<String> broken = ep.findBrokenVisibleImages(testName);
        if (!broken.isEmpty()) {
            // Take a page-level screenshot (util.Screenshot) and attach to Extent report
            String relPath = "";
            try {
                relPath = Screenshot.takeScreenshot(page, testName + "_broken_images_report");
            } catch (Exception ignored) {}

            String projectPath = System.getProperty("user.dir");
            String absPath = projectPath + "\\" + relPath.replace('/', '\\');

            try {
                // Attach to Extent report (BaseTest.test is protected)
                test.addScreenCaptureFromPath(absPath);
            } catch (Exception e) {
                System.out.println("Could not add screenshot to extent: " + e.getMessage());
            }

            boolean failOnBroken = Boolean.parseBoolean(System.getProperty("failOnBrokenImages", "true"));
            String msg = "Broken or missing images detected: " + broken + (relPath.isEmpty() ? "" : " (screenshot: " + absPath + ")");
            if (failOnBroken) {
                Assert.fail(msg);
            } else {
                // Do not fail the test; record in report and continue
                test.warning(msg);
                System.out.println(msg);
            }
        }

        // 7. Collect titles and assert we have at least one meaningful title
        List<String> titles = ep.collectVisibleTitles();
        System.out.println("Collected titles: " + titles);
        Assert.assertFalse(titles.isEmpty(), "No visible titles were collected from the page.");
        boolean found = titles.stream().filter(Objects::nonNull).map(String::toLowerCase).anyMatch(t -> t.contains("dubai")
                || t.contains("desert")
                || t.contains("safari")
                || t.contains("cruise")
                || t.contains("dhow")
        );
        Assert.assertTrue(found, "None of the collected titles mention expected keywords (dubai/desert/safari/cruise/dhow). Actual titles: " + titles);
    }
}