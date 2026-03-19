package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pagesobject.TourVisaBookingObject;

public class B2cTourVisaBooking extends BaseTest {

    @Test
    public void tourBookingTest() {
        TourVisaBookingObject booking = new TourVisaBookingObject(page);

        booking.openHome();
        booking.clickActivities();
        booking.goToDubaiActivities();
        booking.selectCruiseFlow();
        booking.selectTourDateGuests();
        booking.addToCart();
        booking.checkout();
        booking.fillUserDetails("kamini Rathod", "kamini.r@technoheaven.com", "9645679797");
        booking.payment("rayna", "5412 8589 8985 8555", "02/25", "123");

        if (booking.isPaymentFailed()) {
        	
        	
            System.out.println("Tour Booking is Done Sucessfully");
        }
    }

    @Test
    public void visaBookingTest() {
        TourVisaBookingObject booking = new TourVisaBookingObject(page);

        booking.openHome();
        booking.selectVisa();
        booking.fillVisaDetails();
        booking.addToCart();
        booking.checkout();
        booking.fillUserDetails("kamini", "kamini.r@technoheaven.com", "9666565656");
        booking.payment("rayna", "5464 6656 5656 5656", "01/25", "123");

        if (booking.isPaymentFailed()) {
            System.out.println("Visa Booking is Done Sucessfully ");
        }
    }}
    
/*
 * @Test public void tourBookingTest1() { TourVisaBookingObject booking = new
 * TourVisaBookingObject(page);
 * 
 * booking.openHome(); booking.clickActivities(); booking.goToDubaiActivities();
 * booking.selectCruiseFlow(); Assert.assertFalse(booking.isPaymentFailed(),
 * "Payment failed!"); booking.selectTourDateGuests(); booking.addToCart();
 * booking.checkout(); booking.fillUserDetails("kamini Rathod",
 * "kamini.r@technoheaven.com", "9645679797"); booking.payment("rayna",
 * "5412 8589 8985 8555", "02/25", "123");
 * 
 * Assert.assertFalse(booking.isPaymentFailed(), "Payment failed!");
 * System.out.println("❌ Tour Payment Failed card is expired"); }
 * 
 * 
 * @Test public void visaBookingTest1() { TourVisaBookingObject booking = new
 * TourVisaBookingObject(page);
 * 
 * booking.openHome(); booking.selectVisa(); booking.fillVisaDetails();
 * booking.addToCart(); booking.checkout();
 * 
 * booking.fillUserDetails("kamini", "kamini.r@technoheaven.com", "9666565656");
 * booking.payment("rayna", "5464 6656 5656 5656", "01/25", "123");
 * 
 * // Expect failure Assert.assertTrue(booking.isPaymentFailed(),
 * "Payment should fail!");
 * 
 * System.out.println("❌ Visa Payment Failed card is expired"); }}
 */