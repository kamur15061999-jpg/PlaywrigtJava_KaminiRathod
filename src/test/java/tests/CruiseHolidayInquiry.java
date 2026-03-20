package tests;

import org.testng.annotations.Test;
import base.BaseTest;
import pagesobject.HolidayCruiseInquiry;

public class CruiseHolidayInquiry extends BaseTest {

    @Test
    public void testDubaiAndCruiseFlow() {
    	HolidayCruiseInquiry rayna = new HolidayCruiseInquiry(page);
        // ===== DUBAI FLOW =====
        rayna.navigateToHome();
        rayna.clickHolidays();
        rayna.clickDubaiPackages();
        rayna.selectDubaiPackage();
        rayna.sendDubaiEnquiry();
        rayna.fillForm("kamini", "8797897898", "kamini.r@technoheaven.com", "test");
        rayna.submitForm();

        // ===== CRUISE FLOW =====
        rayna.navigateToHome();
        rayna.clickCruises();
        rayna.selectCruise();
        rayna.sendCruiseEnquiry();
        rayna.fillForm("kamini", "8978978978", "kamini.r@technoheaven.com", "test");
        rayna.submitForm();

        System.out.println("Holiday & cruise inquiry Done Sucessfully ");
    }
}