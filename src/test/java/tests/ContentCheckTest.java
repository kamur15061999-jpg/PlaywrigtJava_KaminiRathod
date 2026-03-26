package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pagesobject.ContentCheckPage;

import java.util.List;

public class ContentCheckTest extends BaseTest {

    @Test
    public void checkTourImagesOnDubaiActivities() {
        ContentCheckPage checker = new ContentCheckPage(page);

        // Step 1: Open Dubai Activities page
        test.info("Step 1: Opening Dubai Activities page");
        checker.navigate("https://dev.raynatours.com/dubai-activities");

        // Step 2: Click Load More until all tours are loaded (max 10 times)
        test.info("Step 2: Clicking Load More to load all tours");
        checker.clickLoadMoreUntilEnd(10);

        // Step 3: Check all images
        test.info("Step 3: Checking all tour images");
        List<String> broken = checker.getBrokenImages();

        // Step 4: If broken images found → take screenshot and fail
        if (!broken.isEmpty()) {
            String screenshotPath = checker.takeScreenshot("broken_tour_images");
            String absolutePath   = System.getProperty("user.dir") + "/" + screenshotPath;

            String msg = "Broken images found (" + broken.size() + "):\n"
                    + String.join("\n", broken);

            System.out.println(msg);
            System.out.println("Screenshot: " + screenshotPath);

            test.fail(msg);
            test.addScreenCaptureFromPath(absolutePath, "Broken Tour Images");

            Assert.fail(msg);

        } else {
            // Step 5: All images OK
            test.pass("All tour images loaded successfully on Dubai Activities page.");
            System.out.println("[PASS] All tour images OK.");
        }
    }
}
