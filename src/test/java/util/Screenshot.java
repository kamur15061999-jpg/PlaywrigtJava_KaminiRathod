package util;

import com.microsoft.playwright.Page;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screenshot {

    public static String takeScreenshot(Page page, String testName) {
        // ensure directory exists
        new File("test-output/screenshots").mkdirs();

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = "test-output/screenshots/" + testName + "_" + timestamp + ".png";

        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(path))
                .setFullPage(false));

        return path;
    }
}
