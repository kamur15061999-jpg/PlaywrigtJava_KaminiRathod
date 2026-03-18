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

    @BeforeMethod
    public void setup(Method method) {
    	// reporting
    	extent = Extentmanager.getInstance();
    	test = extent.createTest(method.getName());
    	
    	
    	// playwrights setup
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
      //  page.navigate("https://www.raynatours.com/");
       
        
    }

    @AfterMethod 
    
    public void tearDown(ITestResult result){
    	
    	if(result.getStatus() == ITestResult.FAILURE) {
    		test.fail(result.getThrowable());

    		String screenshotPath = Screenshot.takeScreenshot(page, result.getName());
    		System.out.println("*** screenshotPath : " + screenshotPath);

    		String projectPath = System.getProperty("user.dir");
    		String absoluteScreenshotPath = projectPath + "/" + screenshotPath;

    		System.out.println("*** absoluteScreenshotPath : " + absoluteScreenshotPath);
    		
    		test.addScreenCaptureFromPath(absoluteScreenshotPath, "screenshot");
    		//test.addScreenCaptureFromBase64String(absoluteScreenshotPath,"screenshot");
    		
    	}else if(result.getStatus() == ITestResult.SUCCESS) {
    		test.pass("Test Passed");
    	
    		
    	}else {
    		test.skip("Test Skipped");
    		
    		
    	}
    	extent.flush();
    	
    	if(browser != null) browser.close();
    	if(playwright != null) playwright.close();
    }
}