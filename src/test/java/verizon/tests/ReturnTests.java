package verizon.tests;

import framework.*;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import framework.Utilities.SelectDropMethod;
import pages.ServiceProviderVerificationPage.IdType;

import java.awt.*;
import java.io.IOException;

public class ReturnTests extends RetailBaseClass {
    String orderId = "";
    public String carrierType = "Verizon";

    //region QA 53
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_53_VerizonNonEdgeReturn(@Optional String testtype) throws IOException, InterruptedException {
        Reporter.log("<h2>Start - QA_53_VerizonNonEdgeReturn. <br></h2>");
        Reporter.log("<h3>Description: Verizon-NonEdge-Return Only</h3>");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("QA_53_VerizonNonEdgeReturn");

        DependantTestCaseInputs dependantValues = Utilities.ReadFromCSV("QA_50");
        try {
            Log.startTestCase("QA_53_VerizonNonEdgeReturn");

            // Selecting Carrier Responder
            Reporter.log("<br> Test Type Settings");
            selectCarrierResponderQA53(testtype);

            // Switching to previous tab.
            Utilities.switchPreviousTab();

            //Calling DBError utility to  find initial count or error in log files.
            Reporter.log("<br> DB Errors Initial Check:");
            DBError.navigateDBErrorPage();
            int initialCount = PageBase.AdminPage().totalErrorCount();
            Reporter.log("<h3> DB Errors Initial Check:");
            Reporter.log(String.valueOf(initialCount) + "</h3>");

            // Switching to previous tab.A5661
            Utilities.switchPreviousTab();

            // POA Complete flow
            Reporter.log("<h2> POA Flow Starts </h2>");
            returnFlowQA53(testtype, dependantValues.ORDER_ID, dependantValues.STATE, IdType.valueOf(dependantValues.ID_TYPE),
                    dependantValues.ID_NUMBER, dependantValues.FIRST_NAME, dependantValues.LAST_NAME, dependantValues.ESN_IMEI1);
            Reporter.log("<h2> POA Flow Finishes</h2>");

            // Inventory Management Page verification.
            Reporter.log("<h2> Inventory Management Page: IMEI Status  Check</h2>");
            inventoryManagementVerificationQA53(dependantValues.ESN_IMEI1);
            Reporter.log("<h3> Status of IMEI1: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

            // Shipadmin Verification
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            shipAdminVerification(orderId);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//endregion QA 53

    //region QA-73
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA73_VerizonEdgeUpgradeAndReturn(@Optional String testtype) {
        boolean stopActivation = false;
        try {
            //This TC requires

            Log.startTestCase("QA73_VerizonEdgeUpgradeAndReturn");

            String phoneNumber = "8553835666";
            String iMEINumber = "9886886321";
            System.out.println(iMEINumber);
            String simNumber = "12345678901234567890";
            String receiptID = "132710003003680723";


            //Login
            PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));
            //Verify whether which enviorement to use internal or external.
            testtype = "internal";
            if (testtype.equals("internal")) {


            } else {
                //Script for external testing.
            }
            PageBase.HomePageRetail().newGuestButton.click();

            //Home Page
            Utilities.waitForElementVisible(PageBase.HomePageRetail().upgradeEligibilityCheckerLink);
            PageBase.HomePageRetail().guestLookupTab.click();

            //Customer Lookup Page
            Utilities.waitForElementVisible(PageBase.CustomerLookupPage().receiptIdTextbox);
            PageBase.CustomerLookupPage().receiptIdTextbox.sendKeys(receiptID);
            PageBase.CustomerLookupPage().submitButton.click();
            //Order History Page
            try {
                Utilities.waitForElementVisible(PageBase.OrderHistory().firstCompletedLink, 12);
                PageBase.OrderHistory().firstCompletedLink.click();
            } catch (Exception e) {
            }

            //Guest Verification Page
            Utilities.waitForElementVisible(PageBase.GuestVerificationPage().idTypeDropdown);
            PageBase.GuestVerificationPage().populateGuestVerificationDetails(IdType.DRIVERLICENCE, "CA",
                    "123456789", "TEST", "TESTER");
            PageBase.CommonControls().continueCommonButton.click();

            //Return or Exchange Scan Device Page
            Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().esnIemeidTextbox);
            PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(iMEINumber);
            PageBase.CustomerLookupPage().continueButton.click();

            //Return or Exchange Verification Page
            Utilities.waitForElementVisible(PageBase.ReturnOrExhangePreConditions().continueREVButton);
            PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
            PageBase.ReturnOrExhangePreConditions().continueREVButton.click();

            //Return or Exchange Verification
            PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
            PageBase.ReturnOrExchangeVerificationPage().returnDEVICE.click();
            Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().returnReasons,
                    SelectDropMethod.SELECTBYINDEX, "1");
            PageBase.CustomerLookupPage().continueButton.click();

            //Verizon Account Password
            Utilities.waitForElementVisible(PageBase.VerizonAccountPassword().continueButton);
            PageBase.VerizonAccountPassword().continueButton.click();

            //Support Center Page, currently this is the expected behaviour
            Utilities.waitForElementVisible(PageBase.CommonControls().supportCenterText);
            PageBase.CommonControls().supportCenterText.isDisplayed();

            Log.endTestCase("QA73_VerizonEdgeUpgradeAndReturn");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA73_VerizonEdgeUpgradeAndReturn");
            Assert.assertTrue(false);
        } finally {

        }
    }
    //endregion QA-73

    //region Private Methods and Refacotored Codes

    //region QA 53 private Methods
    private void selectCarrierResponderQA53(@Optional String testtype) throws IOException, InterruptedException, AWTException {
        // Verify whether which environment to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        if (testtype.equals("internal")) {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));

            //Selecting Use Case from dropdown list.
            PageBase.AdminPage().selectAPIConfig(carrierType);
        } else {
            //External// Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
        }
    }

    private String returnFlowQA53(@Optional String testType, String parentOrderId, String state,
                                  IdType idType, String idNumber, String firstName, String lastName, String iMEI) throws IOException {
        Reporter.log("Login to POA");
        //Login to retail page.
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

        Reporter.log("Clicked Guest Look Up Tab");
        PageBase.HomePageRetail().guestLookupTab.click();

        PageBase.CustomerLookupPage().viewGuestOrders.click();
        PageBase.CustomerLookupPage().continueButton.click();

        Reporter.log("Select a Particular Order");
        //Select a Particular Order
        driver.navigate().to(readConfig("CustomerVerification") + parentOrderId);

        Reporter.log("Enter Guest Verification Details");
        Utilities.waitForElementVisible(PageBase.GuestVerificationPage().idTypeDropdown);
        PageBase.GuestVerificationPage().populateGuestVerificationDetails(idType, state,
                idNumber, firstName, lastName);

        Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().orderID);
        orderId = PageBase.ReturnScanDevicePage().orderID.getText();
        PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(iMEI);
        PageBase.CustomerLookupPage().continueButton.click();

        Reporter.log("Giving Return or Exchange Preconditions");
        PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
        PageBase.ReturnOrExhangePreConditions().continueREVButton.click();
        PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
        PageBase.ReturnOrExchangeVerificationPage().returnDEVICE.click();
        Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().returnReasons,
                SelectDropMethod.SELECTBYINDEX, "1");
        PageBase.CustomerLookupPage().continueButton.click();

        if (driver.getCurrentUrl().contains("passwordcapture")) {
            PageBase.VerizonAccountPassword().password.sendKeys("Hello");
            PageBase.VerizonAccountPassword().continueButton.click();
        } else if (driver.getCurrentUrl().contains("printticket")) {
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();
        }
        Utilities.implicitWaitSleep(15000);
        Reporter.log("Goes to Support Center");
        Utilities.waitForElementVisible(driver.findElement(By.xpath("//h2[contains(text(), 'Support Center')]")));
        Assert.assertTrue(driver.findElement(By.xpath("//h2[contains(text(), 'Support Center')]")).isDisplayed());

        //*PageBase.CommonControls().continueButton.click();

        // Not verifiable for Carrier Responder, since it goes to Support Center
        // RMSS Assertions - Todo from Parent Order
/*Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(),
      "");
Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phoneTaxValuePMSSText.getText(),
      "");
PageBase.PrintMobileScanSheetPage().barcodePMSSImage.isDisplayed();

PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();


Assert.assertTrue(PageBase.ReturnConfirmation().returnConfirmation.isDisplayed());
Assert.assertTrue(PageBase.ReturnConfirmation().successfullyReturnedString.isDisplayed());
Assert.assertTrue(PageBase.ReturnConfirmation().linePhonenoString.isDisplayed());
Assert.assertTrue(PageBase.ReturnConfirmation().returnStep1Text.isDisplayed());
Assert.assertTrue(PageBase.ReturnConfirmation().returnStep2Text.isDisplayed());
Assert.assertTrue(PageBase.ReturnConfirmation().printInstruction.isDisplayed());
Assert.assertTrue(PageBase.ReturnConfirmation().returnHome.isDisplayed());*/
        return orderId;
    }

    private void inventoryManagementVerificationQA53(String imei) throws InterruptedException, AWTException, java.io.IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imei, InventoryManagementBaseClass.IMEIStatus.Sold.toString());
    }

    private void shipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
    }
    //endregion QA 53 private Methods
//endregion Private Methods and Refacotored Codes
}
