package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.*;
import util.Extentmanager;
import util.Screenshot;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;
    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite
    public void initReport() {
        Extentmanager.reset();
    }

    @BeforeMethod
    public void setup(Method method) {
        extent = Extentmanager.getInstance();
        test = extent.createTest(method.getName());

        boolean headless = Boolean.parseBoolean(util.ConfigReader.get("browser.headless"));
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(headless));
        page = browser.newPage();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
            String screenshotPath = Screenshot.takeScreenshot(page, result.getName());
            String absolutePath = System.getProperty("user.dir") + "/" + screenshotPath;
            test.addScreenCaptureFromPath(absolutePath, "Failure Screenshot");

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Passed");

        } else {
            test.skip("Test Skipped");
        }

        extent.flush();

        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
