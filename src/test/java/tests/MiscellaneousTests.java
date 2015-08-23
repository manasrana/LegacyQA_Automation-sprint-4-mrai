package tests;

import framework.*;
import com.gargoylesoftware.htmlunit.Page;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.openqa.selenium.By;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.ServiceProviderVerificationPage;
import pages.CarrierCreditCheckDetails;
import pages.PaymentRequiredPage;
import pages.ServiceProviderVerificationPage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mritunjaikr on 7/9/2015.
 */
public class MiscellaneousTests extends RetailBaseClass {

    //region QA-136
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA136_InvalidUserCheck(@Optional String testtype) {
        try {
            Log.startTestCase("QA136_InvalidUserCheck");
            Reporter.log("<h2>Start - QA136_InvalidUserCheck. <br></h2>");
            Reporter.log("Launching Browser <br>", true);
            //Login Page
            Utilities.waitForElementVisible(PageBase.LoginPageRetail().usernameTextbox);
            PageBase.LoginPageRetail().usernameTextbox.sendKeys(Constants.INVALID_USERNAME);
            PageBase.LoginPageRetail().passwordTextbox.sendKeys(Constants.INVALID_PASSWORD);
            PageBase.LoginPageRetail().storeIdTextbox.sendKeys(Utilities.getCredentials("storeId0003"));
            Utilities.waitForElementVisible(PageBase.LoginPageRetail().storeIdButton);
            PageBase.LoginPageRetail().storeIdButton.click();
            lStartTime = new Date().getTime();
            pageName = readPageName("MobileActivationCenter");
            PageBase.LoginPageRetail().loginButton.click();
            Utilities.webPageLoadTime(lStartTime, pageName);
            Utilities.waitForElementVisible(PageBase.LoginPageRetail().errorMessageInvalidLoginCredentials);
            Assert.assertTrue(PageBase.LoginPageRetail().errorMessageInvalidLoginCredentials.isDisplayed());

            Reporter.log("<h3>User is not able to login with invalid credentials.<h3>");
            Reporter.log("<h3>QA136_InvalidUserCheck - Test Case Completes<h3>");
            Log.endTestCase("QA136_InvalidUserCheck");

        } catch (Exception ex) {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA136_InvalidUserCheck");
            Assert.assertTrue(false);
        } finally {

        }
    }
    //endregion QA-136

    //region QA-144
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA144_AccountLookUpSprint(@Optional String testtype) {
        try {

            Log.startTestCase("QA144_AccountLookUpSprint");
            Reporter.log("<h2>Start - QA144_AccountLookUpSprint. <br></h2>");
            Reporter.log("Launching Browser <br>", true);

            //Verify whether which enviorement to use internal or external.
            testtype = BrowserSettings.readConfig("test-type");
            if (testtype.equals("internal")) {
                //Customizing xml files in Carrier Responder
                Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
                AdminBaseClass adminBaseClass = new AdminBaseClass();
                adminBaseClass.launchAdminInNewTab();
                PageBase.AdminPage().navigateToSimulator();
                PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");
                PageBase.AdminPage().selectAPIConfig("Sprint");
                PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "family_2_lines_eligible.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(5000);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
                Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
            } else {
                selectingSprintExternalEnvironment();
            }

            //Switch Back To Retail
            Utilities.switchPreviousTab();

            //Login Page
            PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

            //Home Page
            PageBase.HomePageRetail().accountLookUpLink.click();

            //Customer Account Look Up Page
            Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
            PageBase.UECVerificationPage().sprintTab.click();
            if (testtype.equals("internal")) {
                PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(Constants.DEFAULT_XML_NUMBER_8155491829);
                PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(Constants.DEFAULT_XML_SSN_7777);
            } else {
                AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintValidDataReUseable);
                String phoneNumber = accountDetails.MTN;
                String sSN = accountDetails.SSN;
                String accountPassword = accountDetails.Password;
                PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
                PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(sSN);
                PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(accountPassword);
            }
            PageBase.UECVerificationPage().continueSprintButton.click();

            //Your Account Info Page
            Utilities.waitForElementVisible(PageBase.YourAccountInfoPage().printAllButton);
            Assert.assertTrue(PageBase.YourAccountInfoPage().printAllButton.isEnabled());

            Reporter.log("<h3>User is able to do Sprint Account Look Up.<h3>");
            Reporter.log("<h3>Print All Button is enabled in Sprint Account Look Up Page.<h3>");
            Reporter.log("<h3>QA144_AccountLookUpSprint - Test Case Completes<h3>");
            Log.endTestCase("QA144_AccountLookUpSprint");

        } catch (Exception ex) {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA144_AccountLookUpSprint");
            //Assert.assertTrue(false);
        } finally {

        }
    }
    //endregion QA-144

    //region QA-143
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA143_AccountLookUpVerizon(@Optional String testtype) {
        try {

            Log.startTestCase("QA143_AccountLookUpVerizon");
            Reporter.log("<h2>Start - QA143_AccountLookUpVerizon. <br></h2>");
            Reporter.log("Launching Browser <br>", true);

            //Verify whether which enviorement to use internal or external.
            testtype = BrowserSettings.readConfig("test-type");
            if (testtype.equals("internal")) {
                //Customizing xml files in Carrier Responder
                Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
                AdminBaseClass adminBaseClass = new AdminBaseClass();
                adminBaseClass.launchAdminInNewTab();
                PageBase.AdminPage().navigateToSimulator();
                PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
                //Selecting Use Case from dropdown list.
                PageBase.AdminPage().selectAPIConfig("Verizon");
                PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCustomerDetails", "vwz_accountLookup_LLP_3lines_eligible.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(5000);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
                Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
            } else {
                selectingVerizonExternalEnvironment();
            }

            //Switch Back To Retail
            Utilities.switchPreviousTab();

            //Login Page
            PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

            //Home Page
            PageBase.HomePageRetail().accountLookUpLink.click();

            //Customer Account Look Up Page
            Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
            PageBase.UECVerificationPage().verizonTab.click();
            if (testtype.equals("internal")) {
                PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(Constants.DEFAULT_XML_NUMBER_8155491829);
                PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(Constants.DEFAULT_XML_SSN_7777);
                PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys(Constants.DEFAULT_XML_ZipCode_60007);
            } else {
                CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
                AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.VerizonValidDataReUseable);
                String phoneNumber = accountDetails.MTN;
                String sSN = accountDetails.SSN;
                String accountPassword = accountDetails.Password;
                String zipCode = cusDetails.Zip;
                PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(phoneNumber);
                PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(sSN);
                PageBase.UECVerificationPage().accountPasswordVerizonTextbox.sendKeys(accountPassword);
                PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys(zipCode);
            }

            PageBase.UECVerificationPage().continueVerizonButton.click();

            //Your Account Info Page
            Utilities.waitForElementVisible(PageBase.YourAccountInfoPage().printAllButton);
            Assert.assertTrue(PageBase.YourAccountInfoPage().printAllButton.isDisplayed());

            Reporter.log("<h3>User is able to do Verizon Account Look Up.<h3>");
            Reporter.log("<h3>QA143_AccountLookUpVerizon - Test Case Completes<h3>");
            Log.endTestCase("QA143_AccountLookUpVerizon");

        } catch (Exception ex) {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA143_AccountLookUpVerizon");
            Assert.assertTrue(false);
        } finally {

        }
    }
    //endregion QA-143

    //region QA-142
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA142_AccountLookUpATT(@Optional String testtype) {
        try {

            Log.startTestCase("QA142_AccountLookUpATT");
            Reporter.log("<h2>Start - QA142_AccountLookUpATT. <br></h2>");
            Reporter.log("Launching Browser <br>", true);

            //Verify whether which enviorement to use internal or external.
            testtype = BrowserSettings.readConfig("test-type");
            if (testtype.equals("internal")) {
                //Customizing xml files in Carrier Responder
                Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
                AdminBaseClass adminBaseClass = new AdminBaseClass();
                adminBaseClass.launchAdminInNewTab();
                PageBase.AdminPage().navigateToSimulator();
                PageBase.AdminPage().selectWebAPIResponse("ATT", "CarrierResponder");
                PageBase.AdminPage().selectAPIConfig("ATT");
                PageBase.CarrierResponseXMLPage().attCarrierTab.click();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "inquireaccountprofile", "succes_device_finance_only_individual_plan.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(5000);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
                Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
            } else {
                selectingATTExternalEnvironment();
            }

            //Switch Back To Retail
            Utilities.switchPreviousTab();

            //Login Page
            PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2228"));

            //Home Page
            PageBase.HomePageRetail().accountLookUpLink.click();

            //Customer Account Look Up Page
            Utilities.waitForElementVisible(PageBase.UECVerificationPage().attTab);
            PageBase.UECVerificationPage().attTab.click();
            if (testtype.equals("internal")) {
                PageBase.UECVerificationPage().phoneNumberATTTextbox.sendKeys(Constants.DEFAULT_XML_NUMBER_8155491829);
                PageBase.UECVerificationPage().last4OfSSNATTTextbox.sendKeys(Constants.DEFAULT_XML_SSN_7777);
                PageBase.UECVerificationPage().accountZipcodeATTTextbox.sendKeys(Constants.DEFAULT_XML_ZipCode_60007);
            } else {
                CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
                AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.ATTValidDataReUseable);
                String phoneNumber = accountDetails.MTN;
                String sSN = accountDetails.SSN;
                String accountPassword = accountDetails.Password;
                String zipCode = cusDetails.Zip;
                PageBase.UECVerificationPage().phoneNumberATTTextbox.sendKeys(phoneNumber);
                PageBase.UECVerificationPage().last4OfSSNATTTextbox.sendKeys(sSN);
                PageBase.UECVerificationPage().accountPasswordATTTextbox.sendKeys(accountPassword);
                PageBase.UECVerificationPage().accountZipcodeATTTextbox.sendKeys(zipCode);
            }

            PageBase.UECVerificationPage().continueATTButton.click();

            //Your Account Info Page
            Utilities.waitForElementVisible(PageBase.YourAccountInfoPage().printAllButton);
            Assert.assertTrue(PageBase.YourAccountInfoPage().printAllButton.isDisplayed());

            Reporter.log("<h3>User is able to do ATT Account Look Up<h3>");
            Reporter.log("<h3>Print All Button is enabled.<h3>");
            Reporter.log("<h3>QA142_AccountLookUpATT - Test Case Completes<h3>");
            Log.endTestCase("QA142_AccountLookUpATT");

        } catch (Exception ex) {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA142_AccountLookUpATT");
            Assert.assertTrue(false);
        } finally {

        }
    }
    //endregion QA-142

    //region QA-146
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA146_BrowsePhoneAndPhoneDetailPage(@Optional String testtype) {
        try {

            Log.startTestCase("QA146_BrowsePhoneAndPhoneDetailPage");
            Reporter.log("<h2>Start - QA146_BrowsePhoneAndPhoneDetailPage. <br></h2>");
            Reporter.log("Launching Browser <br>", true);

            //Login Page
            PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

            //Home Page
            PageBase.HomePageRetail().salesAndActivationsLink.click();

            //Choose A Path Page
            Utilities.waitForElementVisible(PageBase.ChoosePathPage().browsePhones);
            PageBase.ChoosePathPage().browsePhones.click();

            //Shop Devices Page
            Utilities.waitForElementVisible(PageBase.ShopDevicesPage().firstPhoneImage);
            PageBase.ShopDevicesPage().firstPhoneImage.click();

            //Product Details Page
            Utilities.waitForElementVisible(PageBase.ProductDetailsPage().returnToCartButton);
            PageBase.ProductDetailsPage().returnToCartButton.click();

            //Cart Page
            Utilities.waitForElementVisible(PageBase.CartPage().clearCartButton);
            Assert.assertTrue(PageBase.CartPage().clearCartButton.isDisplayed());

            Reporter.log("<h3>Return to Cart Button-Product Details Page is enabled.<h3>");
            Reporter.log("<h3>Return to Cart Button-Product Details Page is redirecting to Cart Page.<h3>");
            Reporter.log("<h3>QA146_BrowsePhoneAndPhoneDetailPage - Test Case Completes<h3>");
            Log.endTestCase("QA146_BrowsePhoneAndPhoneDetailPage");

        } catch (Exception ex) {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA146_BrowsePhoneAndPhoneDetailPage");
            Assert.assertTrue(false);
        } finally {

        }
    }
    //endregion QA-146

    //region QA-137
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA137_VerifyVirginMobilePage(@Optional String testtype) {
        try {

            Log.startTestCase("QA137_VerifyVirginMobilePage");
            Reporter.log("<h2>Start - QA137_VerifyVirginMobilePage. <br></h2>");
            Reporter.log("Launching Browser <br>", true);

            //Login Page
            PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

            //Home Page
            PageBase.HomePageRetail().prePaidActivationLink.click();

            //Select An Option Page - Pre Paid Selection Page
            Utilities.waitForElementVisible(PageBase.PrePaidSelectionPage().virginMobileUSALink);
            PageBase.PrePaidSelectionPage().virginMobileUSALink.click();

            //Virgin Mobile USA Page
            Utilities.waitForElementVisible(PageBase.VirginMobileUSAPage().continueButton);
            Assert.assertTrue(PageBase.VirginMobileUSAPage().continueButton.isDisplayed());

            Reporter.log("<h3>Virgin Mobile USA Link is enabled on Select An Option Page-Pre Paid Selection Page<h3>");
            Reporter.log("<h3>Virgin Mobile USA Link on Select An Option Page-Pre Paid Selection Page is redirecting to Virgin Mobile USA Page<h3>");
            Reporter.log("<h3>QA137_VerifyVirginMobilePage - Test Case Completes<h3>");
            Log.endTestCase("QA137_VerifyVirginMobilePage");

        } catch (Exception ex) {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA146_BrowsePhoneAndPhoneDetailPage");
            Assert.assertTrue(false);
        } finally {

        }
    }
    //endregion QA-137

    //region QA-5047
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA5047_NewActivationDeviceFinancingForTablet(@Optional String testtype) throws IOException, InterruptedException, AWTException {
        IMEIDetails imeiDetails1 = null;

        Log.startTestCase("QA5047_NewActivationDeviceFinancingForTablet");
        Reporter.log("<h2>Start - QA5047_NewActivationDeviceFinancingForTablet. <br></h2>");
        Reporter.log("Launching Browser <br>", true);

        //This TC need 1 fresh IMEI for every run.

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.VerizonIPadAir16GB);
        CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
        String iMEINumber = imeiDetails1.IMEI;
        String simNumber = imeiDetails1.Sim;
        String zipCode = cusDetails.Zip;
        String orderId = "";

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        //Verify whether which enviorement to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        if (testtype.equals("internal")) {
            //Customizing xml files in Carrier Responder
            Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
            carrierResponderSettingsQA5047();
            Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
        } else {
            selectingVerizonExternalEnvironment();
        }

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        //Switching Back To Retail
        Utilities.switchPreviousTab();

        //POA Flow
        orderId = poaFlowQA5047(receiptId, iMEINumber, simNumber, testtype);


        if (readConfig("Activation").toLowerCase().contains("true")) {

            //Ship Admin
            ShipAdminBaseClass.launchShipAdminInNewTab();
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
            String status = PageBase.OrderSummaryPage().getOrderStatus();
            Assert.assertEquals(status, Constants.SHIPPED);

            //Inventory Management
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);
            Reporter.log("<h3>IMEI Number =" + iMEINumber + "has been sold.<h3>");
        }

        //DBError Verification.
//            DBError.navigateDBErrorPage();
//            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA5047_NewActivationDeviceFinancingForTablet - Test Case Completes<h3>");
        Log.endTestCase("QA5047_NewActivationDeviceFinancingForTablet");
    }
    //endregion QA-5047

    //region QA 4651
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA_4651_MiscContentVerification(@Optional String testtype) throws InterruptedException, AWTException, IOException {

        Reporter.log("<h2>Start - QA_4651_MiscContentVerification. <br></h2>");
        Reporter.log("<h3>Description: Misc : Content verification: phone browse page, plan browse page,images, phone price and monthly fee</h3>");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("QA_4651_MiscContentVerification");

        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        // Click on Sales & Activations page.
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);

        // Click on Browse Phones link.
        PageBase.ChoosePathPage().browsePhones.click();

        // Verify Phones.
        Assert.assertTrue(PageBase.BrowsePhones().verizonHotspotText.isDisplayed(), "Verizon Hotspot device is not displayed");
        Assert.assertTrue(PageBase.BrowsePhones().verizonHotspotImage.isDisplayed(), "Verizon Hotspot image is not displayed");
        Assert.assertTrue(PageBase.BrowsePhones().samsungGalaxyS516GBText.isDisplayed(), "Samsung Galaxy S5 16GB device is not displayed");
        Assert.assertTrue(PageBase.BrowsePhones().samsungGalaxyS516GBImage.isDisplayed(), "Samsung Galaxy S5 16GB image is not displayed");

        PageBase.HomePageRetail().newGuestButton.click();

        // Click on Sales & Activations page.
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);

        Utilities.waitForElementVisible(PageBase.ChoosePathPage().browsePlans);

        // Click on Browse Plans link.
        PageBase.ChoosePathPage().browsePlans.click();

        // Click on All Plans link.
        Utilities.waitForElementVisible(PageBase.PickYourPathPage().allPlans);
        PageBase.PickYourPathPage().allPlans.click();

        PageBase.CommonControls().continueButtonDVA.click();

        Utilities.waitForElementVisible(PageBase.BrowsePlans().sprintFamilySharePackText);
        Assert.assertTrue(PageBase.BrowsePlans().sprintFamilySharePackText.isDisplayed());
        // Assert.assertTrue(PageBase.BrowsePlans().verizonMoreEverythingText.isDisplayed());
        ArrayList<WebElement> list = (ArrayList<WebElement>) driver.findElements(By.xpath("//h1[contains(text(),'Verizon Plan')]"));
        Assert.assertTrue(list.size()>0);

        Reporter.log("<h2>End - QA_4651_MiscContentVerification test case completes. <br></h2>");
    }
    //endregion QA 4651

    //region QA 135
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA_135_ValidUserAuthentication(@Optional String testtype) throws InterruptedException, AWTException, IOException {

        Reporter.log("<h2>Start - QA_135_ValidUserAuthentication. <br></h2>");
        Reporter.log("<h3>Description: Misc -Valid user authentication</h3>");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("QA_135_ValidUserAuthentication");

        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        // Wait for Sales & Activations link.
        Utilities.waitForElementVisible(PageBase.HomePageRetail().salesAndActivationsLink);

        Assert.assertTrue(PageBase.HomePageRetail().salesAndActivationsLink.isDisplayed());

        Reporter.log("<h2>End - QA_135_ValidUserAuthentication test case completes. <br></h2>");
    }
    //endregion QA 135

    //region QA 2028
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA_2028_ShipadminDefaultBrand(@Optional String testtype) throws InterruptedException, AWTException, IOException {

        Reporter.log("<h2>Start - QA_2028_ShipadminDefaultBrand. <br></h2>");
        Reporter.log("<h3>Description: Misc - shipadmin page should default to brand 731</h3>");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("QA_2028_ShipadminDefaultBrand");

        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.waitForElementVisible(PageBase.ShipAdminPage().brandName);
        Assert.assertEquals(PageBase.ShipAdminPage().brandName.getText(), Constants.retailOperationCenter);

        driver.navigate().to(readConfig("urlEditSessionVars"));

        Utilities.waitForElementVisible(PageBase.EditSessionVars().brandId);
        Assert.assertTrue(PageBase.EditSessionVars().brandId.isDisplayed());

        Reporter.log("<h2>End - QA_2028_ShipadminDefaultBrand test case completes. <br></h2>");
    }
    //endregion QA 2028

    //region QA 5257
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA_5257_VerifyShipadminOrderSearch(@Optional String testtype) throws InterruptedException, AWTException, IOException {

        Reporter.log("<h2>Start - QA_5257_VerifyShipadminOrderSearch. <br></h2>");
        Reporter.log("<h3>Description: Misc || verify shipadmin order search page</h3>");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("QA_5257_VerifyShipadminOrderSearch");

        DependantTestCaseInputs dependantValues = Utilities.ReadFromCSV("QA_50");
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.waitForElementVisible(PageBase.ShipAdminPage().orders);
        PageBase.ShipAdminPage().orders.click();

        Utilities.waitForElementVisible(PageBase.ShipAdminPage().searchOrderButton);
        Assert.assertTrue(PageBase.ShipAdminPage().searchOrderButton.isDisplayed());

        PageBase.ShipAdminPage().lastWeekRadiobutton.click();
        PageBase.ShipAdminPage().searchOrderButton.click();

        PageBase.driver.navigate().to(readConfig("shipAdminOrderDetails") +
                PageBase.ShipAdminPage().orderIdLink.getText());

        Utilities.waitForElementVisible(PageBase.OrderSummaryPage().statusValueLink);
        Assert.assertTrue(PageBase.OrderSummaryPage().statusValueLink.isDisplayed());

        Reporter.log("<h2>End - QA_5257_VerifyShipadminOrderSearch test case completes. <br></h2>");
    }
//endregion QA 5257

    //region QA 5256
    @Test(groups = {"Misc"})
    @Parameters("test-type")
    public void QA_5256_VerifyEventTrackerPages(@Optional String testtype) throws InterruptedException, AWTException, IOException {

        Reporter.log("<h2>Start - QA_5256_VerifyEventTrackerPages. <br></h2>");
        Reporter.log("<h3>Description: Verify event tracker pages</h3>");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("QA_5256_VerifyEventTrackerPages");

        DependantTestCaseInputs dependantValues = Utilities.ReadFromCSV("QA_50");
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.waitForElementVisible(PageBase.ShipAdminPage().eventTrackerLink);
        PageBase.ShipAdminPage().eventTrackerLink.click();

        Utilities.waitForElementVisible(PageBase.ShipAdminPage().eventTrackerOrderTextbox);
        PageBase.ShipAdminPage().lastWeekRadiobutton.click();
        PageBase.ShipAdminPage().searchOrderButton.click();

        PageBase.driver.navigate().to(readConfig("shipAdminOrderDetails") +
                PageBase.ShipAdminPage().eventTrackerOrderIdLink.getText());

        Utilities.waitForElementVisible(PageBase.OrderSummaryPage().statusValueLink);
        Assert.assertTrue(PageBase.OrderSummaryPage().statusValueLink.isDisplayed());

        Reporter.log("<h2>End - QA_5256_VerifyEventTrackerPages test case completes. <br></h2>");
    }
//endregion QA 5256

    //region QA-52
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_52_VerizonNonEdgeAddMultipleLinesToExistingAccount(@Optional String testtype) throws IOException, AWTException, InterruptedException {
        IMEIDetails imeiDetails1 = null;
        IMEIDetails imeiDetails2 = null;
        String carrierType = "Verizon";

        //This TC requires 2 fresh IMEI's for every run.
        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);
        imeiDetails2 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);

        Reporter.log("<h2>Start - QA_52_VerizonNonEdgeAddMultipleLinesToExistingAccount. <br></h2>");
        Reporter.log("<h3>Description: Verify event tracker pages</h3>");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("QA_52_VerizonNonEdgeAddMultipleLinesToExistingAccount");
        CustomerDetails custDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.VerizonNonEdgeUpgradeMultipleLinesEligible);
        String phoneNumber = accountDetails.MTN;
        String sSN = accountDetails.SSN;
        String zipCode = custDetails.Zip;
        String accountPassword = accountDetails.Password;
        String orderId = "";

        // Adding Devices to Inventory.
//        PageBase.InventoryManagementPage().launchInventoryInNewTab();
//        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails2.IMEI,
//                imeiDetails1.ProductName, imeiDetails2.ProductName);
//        PageBase.InventoryManagementPage().closeInventoryTab();
//        Utilities.switchPreviousTab();

        //Verify whether which environment to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");

        if (testtype.equals("internal")) {
            //Customizing xml files in Carrier Responder
            // carrierResponderSettingsQA52();
        } else {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
        }

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        //Switching Back To Retail
        Utilities.switchPreviousTab();

        //POA FLOW
        orderId = poaFlowQA52(testtype, imeiDetails1.IMEI, imeiDetails2.IMEI, imeiDetails1.Sim, imeiDetails2.Sim, orderId, receiptId, phoneNumber,
                sSN, zipCode, accountPassword);

        if (readConfig("Activation").toLowerCase().contains("true")) {
            //Ship Admin
            shipAdminVerificationsQA52(testtype, orderId);

            //Inventory Management
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, imeiDetails2.IMEI, Constants.SOLD);
        }

        //DBError Verification.
//        DBError.navigateDBErrorPage();
//        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h2>End - QA_52_VerizonNonEdgeAddMultipleLinesToExistingAccount test case completes. <br></h2>");
        Log.endTestCase("QA_52_VerizonNonEdgeAddMultipleLinesToExistingAccount");
    }
//endregion QA-52

    //region Helper and Refactored Methods
    private void selectingVerizonExternalEnvironment() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
        Reporter.log("<h3><U> External Server</U></h3>", true);
    }

    private void selectingSprintExternalEnvironment() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
        Reporter.log("<h3><U> External Server</U></h3>", true);
    }

    private void selectingATTExternalEnvironment() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("ATT", "External");
        Reporter.log("<h3><U> External Server</U></h3>", true);
    }

    //region QA-52 Refactored Methods
    private void shipAdminVerificationsQA52(@Optional String testtype, String orderId) throws IOException {

        ShipAdminBaseClass.launchShipAdminInNewTab();
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains("Phone and Plan"));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
    }

    private String poaFlowQA52(@Optional String testtype, String iMEINumber1, String iMEINumber2, String simNumber1, String simNumber2,
                               String orderId, String receiptId, String phoneNumber, String sSN, String zipCode, String accountPassword)
            throws IOException, AWTException, InterruptedException {

        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();

        //Login
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        //Home Page
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Choose a Path Page
        PageBase.ChoosePathPage().existingCarrier.click();

        //Pick your Path Page
        PageBase.PickYourPathPage().AALExistingAccount.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //UEC Verification Page
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
        PageBase.UECVerificationPage().verizonTab.click();
        if (testtype.equals("internal")) {
            PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(Constants.DEFAULT_XML_NUMBER_8155491829);
        } else {
            PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(phoneNumber);
        }
        PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(sSN);
        PageBase.UECVerificationPage().accountPasswordVerizonTextbox.sendKeys(accountPassword);
        PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys(zipCode);
        PageBase.UECVerificationPage().continueVerizonButton.click();

        //Select an Option Page
        Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().AALExistingFamilyPlan);
        PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber1);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Verizon Edge Page
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
        PageBase.VerizonEdgePage().declineVerizonEdge();
        //PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.clear();
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber2);
        PageBase.DeviceScanPage().submitDeviceButton.click();
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().continueDSButton);
        PageBase.DeviceScanPage().continueDSButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        try {
            PageBase.CommonControls().continueButton.click();
        } catch (Exception ex) {
        }

        // Credit Check Verification Results with deposits.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        boolean exists = driver.findElements(By.id("checkbox-deposit-1")).size() != 0;
        if (exists) {
            Reporter.log("<br> Credit Check Completes.");
            PageBase.CreditCheckVerificationResultsPage().depositCheckBox.click();
            Reporter.log("<br> Selected Deposit Check Box ");
        } else {
            PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
        }
        PageBase.CommonControls().continueCommonButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        String device1Price = PageBase.CartPage().device1Price.getText();
        String device2Price = PageBase.CartPage().device2Price.getText();
        PageBase.CartPage().continueCartButton.click();

        //Verizon Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        try {
            Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance, 10);
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForTwoDevices();
        } catch (Exception e) {
        }

        // Selecting Number Porting.
        Utilities.ClickElement(PageBase.NumberPortPage().numberPortRadiobutton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        PageBase.PortMyNumbersPage().enterPortDataForPreCreditCheck(portDetails.CurrentPhoneNumber, portDetails.Carrier,
                portDetails.CurrentAccNumber, portDetails.SSN);

        //Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        // Cart and Order Review Confirm Prices are not matching.
        //PageBase.OrderReviewAndConfirmPage().OrderReviewConfirmPageAssertionsFor2Devices(device1Price, device2Price);
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox);
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        if (readConfig("Activation").toLowerCase().contains("true")) {
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            //Credit Check Verification Results
            Utilities.waitForElementVisible(PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox, 120);
            PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
            PageBase.CreditCheckVerificationResultsPage().continueButton.click();

            //Payment Required Page
            Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().sameAddressTab);
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
            PageBase.PaymentRequiredPage().sameAddressTab.click();
            PageBase.PaymentRequiredPage().continuePRButton.click();
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);

            //Print Mobile Scan Sheet Page
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            //Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice); //Phone Price Not Same w.r.t. Cart Page
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            //Payment Verification Page
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor3Devices(iMEINumber1, iMEINumber2,
                    "", simNumber1, simNumber2, "");
            PageBase.CommonControls().continueButtonDVA.click();

            // WCA Signature page activity and verifications.
            try {
                Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
                PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
                PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();
            } catch (Exception e) {
            }

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(iMEINumber1, CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(iMEINumber2, CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(PageBase.PaymentVerificationPage().textboxTargetReceiptID.toString(),
                    CSVOperations.FileName.ReceiptId);

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
        }
        return orderId;
    }

    private void carrierResponderSettingsQA52() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCustomerDetails", "vwz_accountLookup_LLP_3lines_eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "default_upgrade.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveExistingCustomerInstallmentDetails", "eligible_no_payment_needed.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "alp_2_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitpayment", "default.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_2_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private CarrierCreditCheckDetails getCarrierCreditCheckDetails() throws IOException {
        CarrierCreditCheckDetails cccDetails = new CarrierCreditCheckDetails();
        PageBase.CSVOperations();
        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        cccDetails.setFirstName(customerDetails.FirstName);
        cccDetails.setLastName(customerDetails.LastName);
        cccDetails.setAddress1(customerDetails.Address1);
        cccDetails.setCity(customerDetails.City);
        cccDetails.setState(customerDetails.State);
        cccDetails.setZip(customerDetails.Zip);
        cccDetails.setHomePhone(customerDetails.PhNum);
        cccDetails.setEmail(customerDetails.EMail);
        cccDetails.setBirthMonth(customerDetails.BirthdayMonth);
        cccDetails.setBirthDate(customerDetails.BirthdayDay);
        cccDetails.setBirthYear(customerDetails.BirthdayYear);
        cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.SSNWithDeposit));
        cccDetails.setIDType(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        cccDetails.setIdTypeState(customerDetails.IDState);
        cccDetails.setIdNumber(customerDetails.IDNumber);
        cccDetails.setMonth(customerDetails.IDExpirationMonth);
        cccDetails.setYear(customerDetails.IDExpirationYear);
        return cccDetails;
    }
//endregion QA-52 Refactored Methods

    //region Refactored Methods QA-5047
    private String poaFlowQA5047(String receiptId, String iMEINumber, String simNumber, String testtype) throws IOException, AWTException, InterruptedException {
        String orderId;
        //Login Page
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        //Home Page
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Choose A Path Page
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().newActivation);
        PageBase.ChoosePathPage().newActivation.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Verizon Edge Page
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
        PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

        //Credit Check Page
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().firstNameTextBox);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.SSNWithoutDeposit));
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        try {
            PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
        }

        //Installment Details Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton, 120);
        PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton.click();
        PageBase.CommonControls().continueCommonButton.click();

        //Verizon Shop Plans Page
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().firstPlanAddButton);
        PageBase.VerizonShopPlansPage().firstPlanAddButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        PageBase.CartPage().continueCartButton.click();

        //Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        //Number Port Page
        try {
            Utilities.waitForElementVisible(PageBase.NumberPortPage().continueSPVButton, 10);
        }
        catch (Exception e)
        {
            //Handling Temp Error i.e. Service Is Temporarily Unavailable
            driver.navigate().back();
            Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
            PageBase.SelectPlanFeaturesPage().continueSPFButton.click();
        }
        Utilities.waitForElementVisible(PageBase.NumberPortPage().continueSPVButton);
        PageBase.NumberPortPage().continueSPVButton.click();

        //Order Review And Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox);
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.TermsAndConditionsPage().continueTCButton.click();

        //Print Mobile Scan Sheet
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton, 120);
        orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        //Payment Verification Page
        Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
        PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
        PageBase.PaymentVerificationPage().submitButton.click();

        //Device Verification and Activation Page
        Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(iMEINumber);
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
        PageBase.DeviceVerificationaandActivation().simType.clear();
        PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);
        try  //ToDo:Remove this when no insurance bug will fix permanently.
        {
            PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
            CreditCardDetails CreditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
            String cVNNumberS = "" + CreditCard.CVV;
            PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(cVNNumberS);
        } catch (Exception e) {
        }
        PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

        //Wireless Customer Agreement Page
        Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox, 120);
        PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox.click();
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

        //Device Financing Installment Contract Page
        Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print, 120);
        PageBase.DeviceFinancingInstallmentContractPage().print.click();
        Utilities.implicitWaitSleep(6000);
        CommonFunction.clickingNTimesTab(8);
        Utilities.implicitWaitSleep(2000);
        Robot robot = new Robot();
        Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
        Utilities.implicitWaitSleep(5000);
        Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
        PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox.click();
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        if (readConfig("Activation").toLowerCase().contains("true")) {
            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Reporter.log("<h3><U>Order ha been confirmed, Order Id =" + orderId + "</U></h3>");
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(iMEINumber, CSVOperations.FileName.VerizonIPadAir16GB);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(receiptId, CSVOperations.FileName.ReceiptId);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), simNumber);
        }
        return orderId;
    }

    private void carrierResponderSettingsQA5047() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
        PageBase.AdminPage().selectAPIConfig("Verizon");
        // Customizing xml files in Carrier Responder
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveInstallmentDetailsForDevice", "eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "generateInstallmentContract", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_1_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "success_edge.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitReceipt", "default.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_v_1_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveNpaNxx", "single_area_code.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_edge.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitSigpadCaptureForInstallmentAgreement", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
    }
    //endregion Refactored Methods QA-5047

    //endregion Helper and Refactored Methods
}
