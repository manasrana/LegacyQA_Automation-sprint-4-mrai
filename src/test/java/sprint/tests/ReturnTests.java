package sprint.tests;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import framework.AdminBaseClass;
import framework.BrowserSettings;
import framework.ControlLocators;
import framework.DBError;
import framework.DependantTestCaseInputs;
import framework.Log;
import framework.PageBase;
import framework.RetailBaseClass;
import framework.ShipAdminBaseClass;
import framework.Utilities;
import framework.CSVOperations.FileName;
import framework.Utilities.SelectDropMethod;
import pages.ServiceProviderVerificationPage.IdType;


public class ReturnTests extends RetailBaseClass {
    public String carrierType = "sprint";

    // region Test Methods
    // region Test Methods
    // region QA-91
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_91_Return_All_Family_Lines(@Optional String testtype) throws IOException, AWTException, InterruptedException {
        DependantTestCaseInputs dependantValues = Utilities.ReadFromCSV("QA_50");
        String phoneNum1 = "1";
        String phoneNum2 = "2";
        String orderId = dependantValues.ORDER_ID;
        System.out.print(orderId);
        ArrayList<String> phoneNum = new ArrayList<String>();
        ArrayList<String> imeiNums = new ArrayList<String>();
        imeiNums.add(dependantValues.ESN_IMEI1);
        imeiNums.add(dependantValues.ESN_IMEI2);
        phoneNum.add(phoneNum1);
        phoneNum.add(phoneNum2);
        Reporter.log("QA_91_Return_All_Family_Lines");
        Reporter.log("<h3>Description: </h3>" + "Sprint: Return all lines - Sprint family account keep existing plans and upgrade all lines ");
        Log.startTestCase("QA_91_Return_All_Family_Lines");
        // Verify whether which environment to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        String internalTestType = BrowserSettings.readConfig("internalTestType");
        if (testtype.equals("internal")) {
            //Selecting the Execution env either Backend or CarrierResponder based on config file
            if (internalTestType.contains("BackendSimulator")) {
                QA_91_BackendSimulatorSettings();
            } else {
                QA_91_CarrierResponderSettings();
            }
        } else {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
        }
        // Switching to Retail tab.
        Utilities.switchPreviousTab();
        // Calling DBError utility to find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        // Switching to Retail tab.
        Utilities.switchPreviousTab();
        // This Methods contains QA_91 POA Flow
        QA_91_POAFlow(orderId, imeiNums, dependantValues.FIRST_NAME, dependantValues.LAST_NAME, dependantValues.ID_NUMBER, dependantValues.STATE);
        QA_91_ShipAdminVerification(orderId, phoneNum);
        QA_91_CheckInventoryStatus(imeiNums);
        // DBError Verification for any new db errors
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        Reporter.log("<h3>QA_91_Return_All_Family_Lines - Test Case Completes<h3>");
        Log.endTestCase("QA_91_Return_All_Family_Lines");
    }

    // endregion QA-91

    // endregion Test Methods


    // region Re-Factored Test Case QA-91 Methods
    private void QA_91_POAFlow(String Orderid, ArrayList<String> imeiNums, String FIRST_NAME, String LAST_NAME, String ID_NUMBER, String STATE) throws IOException {
        String strReciptId = PageBase.CSVOperations()
                .GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        PageBase.LoginPageRetail().poaLogin(
                Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"),
                Utilities.getCredentials("storeId0003"));
        // Click on the guest lookup from HomePage
        PageBase.HomePageRetail().guestLookupTab.click();

        // Enter Receipt Id and click on continue button
        PageBase.CustomerLookupPage().receiptIdTextbox.sendKeys(strReciptId);
        PageBase.CustomerLookupPage().submitButton.click();

        // Select the order from Order History Page to process the returns
        Utilities.implicitWaitSleep(10000);
        PageBase.OrderHistory().clickCompleted(Orderid);

        // Populating the guest verification tool
        Utilities.implicitWaitSleep(10000);
        PageBase.GuestVerificationPage().populateGuestVerificationDetails(IdType.DRIVERLICENCE, STATE,
                ID_NUMBER, FIRST_NAME, LAST_NAME);
        // Scan First Device
        Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().esnIemeidTextbox);
        PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(imeiNums.get(0));
        PageBase.CustomerLookupPage().continueButton.click();
        PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
        Utilities.waitForElementVisible(PageBase.ReturnOrExhangePreConditions().continueREVButton);
        PageBase.ReturnOrExhangePreConditions().continueREVButton.click();
        PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
        PageBase.ReturnOrExchangeVerificationPage().returnDEVICE.click();
        Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().returnReasons,
                SelectDropMethod.SELECTBYINDEX, "1");
        Utilities.waitForElementVisible(PageBase.ReturnOrExchangeVerificationPage().returnAnotherDevice);
        PageBase.ReturnOrExchangeVerificationPage().returnAnotherDevice.click();
        Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().esnIemeidTextbox);
        PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(imeiNums.get(1));
        PageBase.CustomerLookupPage().continueButton.click();
        PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
        PageBase.ReturnOrExhangePreConditions().continueREVButton.click();
        PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
        PageBase.ReturnOrExchangeVerificationPage().returnDEVICE.click();
        Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().returnReasons,
                SelectDropMethod.SELECTBYINDEX, "1");
        PageBase.CustomerLookupPage().continueButton.click();
        Utilities.implicitWaitSleep(10000);
        Utilities.waitForElementVisible(BrowserSettings.driver.findElement(By.xpath("//*[@id='retailPage']/section/h2")));
        String Message = BrowserSettings.driver.findElement(By.xpath("//*[@id='retailPage']/section/h2")).getText();
        Assert.assertTrue(Message.contains("Return"));
    }

    private void QA_91_BackendSimulatorSettings() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");
        Utilities.switchPreviousTab();
    }

    private void QA_91_CarrierResponderSettings() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");
        PageBase.AdminPage().selectAPIConfig("Sprint");
        // Customizing xml files in Carrier Responder
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "processdevicereturn", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private void QA_91_ShipAdminVerification(String Orderid, ArrayList<String> phoneNums) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        driver.get(readConfig("urlShipAdmin") + "deactivation/");
        Utilities.implicitWaitSleep(1000);
        PageBase.ShipAdminPage().deactivateLines(Orderid, phoneNums.size());
    }

    private void QA_91_CheckInventoryStatus(ArrayList<String> imeiNums) throws InterruptedException, AWTException, IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        int size = imeiNums.size();
        for (int i = 0; i < size; i++) {
            String imei = imeiNums.get(i);
            PageBase.InventoryManagementPage().verifyDeviceStatus(imei, "Sold");
        }
    }
}