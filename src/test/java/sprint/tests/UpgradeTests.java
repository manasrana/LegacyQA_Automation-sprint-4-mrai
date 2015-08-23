package sprint.tests;

import framework.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.PaymentRequiredPage;
import pages.ServiceProviderVerificationPage;
import pages.CarrierCreditCheckDetails;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UpgradeTests extends RetailBaseClass {
    public String carrierType = "Sprint";

    //region Test Methods
//region QA-3065
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan(@Optional String testtype) throws IOException, AWTException, InterruptedException {
        IMEIDetails imeiDetails = null;
        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        //This TC need 1 fresh phone number and 1 fresh IMEI for every run.
        CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
        String iMEINumber = imeiDetails.IMEI;
        String simNumber = imeiDetails.Sim;
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintEasyPayUpgradeMultipleLinesEligible);
        String phoneNumber = accountDetails.MTN;
        String sSN = accountDetails.SSN;
        String zipCode = cusDetails.Zip;
        String accountPassword = accountDetails.Password;
        String orderId = "";

        Log.startTestCase("QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan");
        Reporter.log("<h2>Start - QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan. <br></h2>");
        Reporter.log("Launching Browser <br>", true);

        //Verify whether which enviorement to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        if (testtype.equals("internal")) {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();

            if (readConfig("internalTestType").toLowerCase().contains("carrierresponder")) {
                //Customizing xml files in Carrier Responder
                Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
                carrierResponderSettingsQA3065(phoneNumber);
                Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);

            } else {
                Reporter.log("<h3><U> Backend</U></h3>", true);
                //backendSettingsQA3065(phoneNumber);
                PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");
                Reporter.log("<h3><U> Backend Changes Done.</U></h3>", true);
            }
        } else {
            selectingSprintExternalEnvironment();
        }

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        //Switching Back To Retail
        Utilities.switchPreviousTab();

        //POA FLOW
        orderId = poaFlowQA3065(receiptId, iMEINumber, simNumber, phoneNumber, sSN, accountPassword, orderId);


        if (readConfig("Activation").toLowerCase().contains("true")) {
            //Ship Admin
            ShipAdminBaseClass.launchShipAdminInNewTab();
            shipAdminUpgradeVerifications(orderId);

            //Inventory Management
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);

            //Verify in Server DB
            PageBase.SQLUtilAdminPage().launchSQLUtilInNewTab();
            serverDBVerificationsQA3065(orderId);
        }

        //DBError Verification.
        DBError.navigateDBErrorPage();
        //Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan - Test Case Completes<h3>");
        Log.endTestCase("QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan");
    }
    //endregion QA-3065

    //region QA-3096
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible(@Optional String testtype) throws IOException, AWTException, InterruptedException {
        IMEIDetails imeiDetails = null;
        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        //This TC need 2 fresh phone numbers and 1 IMEI for internal run with CR.
        CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
        String iMEINumber = imeiDetails.IMEI;
        String simNumber = imeiDetails.Sim;
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintBuddyUpgrade);
        String phoneNumber = accountDetails.MTN;
        String donorPhoneNumber = CommonFunction.getUniqueNumber(phoneNumber); //This is for Carrier Responder.
        String sSN = accountDetails.SSN;
        String zipCode = cusDetails.Zip;
        String accountPassword = accountDetails.Password;
        String orderId = "";

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails.IMEI + "(ProdCode: " + imeiDetails.ProductName + ")");

        Log.startTestCase("QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible");
        Reporter.log("<h2>Start - QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible. <br></h2>");
        Reporter.log("Launching Browser <br>", true);

        //Verify whether which enviorement to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        if (testtype.equals("internal")) {
            //Customizing xml files in Carrier Responder
            Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
            carrierResponderSettingsQA3096(phoneNumber, donorPhoneNumber);
            Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
        } else {
            selectingSprintExternalEnvironment();
        }

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        //Switching Back To Retail
        Utilities.switchPreviousTab();

        //POA FLOW
        orderId = poaFlowQA3096(receiptId, iMEINumber, simNumber, phoneNumber, sSN, accountPassword, orderId);

//            if (readConfig("Activation").toLowerCase().contains("true")) {
//                //Ship Admin
//                ShipAdminBaseClass.launchShipAdminInNewTab();
//                shipAdminUpgradeVerifications(orderId);
//
//                //Inventory Management
//                PageBase.InventoryManagementPage().launchInventoryInNewTab();
//                PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);
//            }

        //DBError Verification.
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible - Test Case Completes<h3>");
        Log.endTestCase("QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible");
    }
    //endregion QA-3096

    //region QA-5268
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA5268_BuddyEarlyUpgradeNonEasyPay(@Optional String testtype) throws IOException, AWTException, InterruptedException {
        IMEIDetails imeiDetails = null;
        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        //This TC need 2 fresh phone numbers and 1 IMEI for internal run with CR.
        CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
        String iMEINumber = imeiDetails.IMEI;
        String simNumber = imeiDetails.Sim;
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintBuddyEarlyUpgrade);
        String phoneNumber = accountDetails.MTN;
        String donorPhoneNumber = CommonFunction.getUniqueNumber(phoneNumber); //This is for Carrier Responder.
        String sSN = accountDetails.SSN;
        String zipCode = cusDetails.Zip;
        String accountPassword = accountDetails.Password;
        String orderId = "";

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails.IMEI + "(ProdCode: " + imeiDetails.ProductName + ")");

        Log.startTestCase("QA5268_BuddyEarlyUpgradeNonEasyPay");
        Reporter.log("<h2>Start - QA5268_BuddyEarlyUpgradeNonEasyPay. <br></h2>");
        Reporter.log("Launching Browser <br>", true);

        //Verify whether which enviorement to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        if (testtype.equals("internal")) {
            //Customizing xml files in Carrier Responder
            Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
            carrierResponderSettingsQA3096(phoneNumber, donorPhoneNumber); //Same as QA-3096
            Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
        } else {
            selectingSprintExternalEnvironment();
        }

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        //Switching Back To Retail
        Utilities.switchPreviousTab();

        //POA FLOW
        orderId = poaFlowQA5268(receiptId, iMEINumber, simNumber, phoneNumber, sSN, accountPassword, orderId);


        if (readConfig("Activation").toLowerCase().contains("true")) {
            //Ship Admin
            ShipAdminBaseClass.launchShipAdminInNewTab();
            shipAdminUpgradeVerifications(orderId);

            //Inventory Management
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);
        }

        //DBError Verification.
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA5268_BuddyEarlyUpgradeNonEasyPay - Test Case Completes<h3>");
        Log.endTestCase("QA5268_BuddyEarlyUpgradeNonEasyPay");
    }
    //endregion QA-5268

    //region QA-87
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA87_UpgradeAllLinesWithNewFamilyPlanWithESecuritel(@Optional String testtype) throws IOException, AWTException, InterruptedException {
        //This TC need 2 fresh phone numbers and 2 IMEI for internal run with CR.
        IMEIDetails imeiDetails1 = null;
        IMEIDetails imeiDetails2 = null;
        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        imeiDetails2 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
        String iMEINumber1 = imeiDetails1.IMEI;
        String iMEINumber2 = imeiDetails2.IMEI;
        String simNumber1 = imeiDetails1.Sim;
        String simNumber2 = imeiDetails2.Sim;
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintMultipleLinesEligibleUpgrade);
        String phoneNumber1 = accountDetails.MTN;
        String phoneNumber2 = CommonFunction.getUniqueNumber(phoneNumber1); //This is for Carrier Responder.
        String sSN = accountDetails.SSN;
        String zipCode = cusDetails.Zip;
        String accountPassword = accountDetails.Password;
        String orderId = "";

//        // Adding Devices to Inventory.
//        PageBase.InventoryManagementPage().launchInventoryInNewTab();
//        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails2.IMEI, imeiDetails1.ProductName, imeiDetails2.ProductName);
//        PageBase.InventoryManagementPage().closeInventoryTab();
//        Utilities.switchPreviousTab();
//        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");
//        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails2.IMEI + "(ProdCode: " + imeiDetails2.ProductName + ")");

        Log.startTestCase("QA87_UpgradeAllLinesWithNewFamilyPlanWithESecuritel");
        Reporter.log("<h2>Start - QA87_UpgradeAllLinesWithNewFamilyPlanWithESecuritel. <br></h2>");
        Reporter.log("Launching Browser <br>", true);

        //Verify whether which enviorement to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        if (testtype.equals("internal")) {
            //Customizing xml files in Carrier Responder
            carrierResponderSettingsQA87(phoneNumber1, phoneNumber2);
        } else {
            selectingSprintExternalEnvironment();
        }

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        //Switching Back To Retail
        Utilities.switchPreviousTab();

        //POA FLOW
        orderId = poaFlowQA87(receiptId, iMEINumber1, iMEINumber2, simNumber1, simNumber2, phoneNumber1, sSN, accountPassword, orderId);


        if (readConfig("Activation").toLowerCase().contains("true")) {
            //Ship Admin
            ShipAdminBaseClass.launchShipAdminInNewTab();
            shipAdminUpgradeVerifications(orderId);

            //Inventory Management
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber1, iMEINumber2, Constants.SOLD);
        }

        //DBError Verification.
        DBError.navigateDBErrorPage();
        //Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA87_UpgradeAllLinesWithNewFamilyPlanWithESecuritel - Test Case Completes<h3>");
        Log.endTestCase("QA87_UpgradeAllLinesWithNewFamilyPlanWithESecuritel");
    }
    //endregion QA-87

    //region QA-92
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA92_AddALineNumberPortWithDepositActivationThroughShipadmin(@Optional String testtype) throws InterruptedException, AWTException, IOException {
        //This TC needs 1 fresh IMEI for CR run.
        IMEIDetails imeiDetails = null;
        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
        String iMEINumber = imeiDetails.IMEI;
        String simNumber = imeiDetails.Sim;
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SamsungGaSprintAALFamilyWithDepositlaxyS4_16GBWhite);
        String phoneNumber = accountDetails.MTN;
        String sSN = accountDetails.SSN;
        String zipCode = cusDetails.Zip;
        String accountPassword = accountDetails.Password;
        String orderId = "";

//        // Adding Devices to Inventory.
//        PageBase.InventoryManagementPage().launchInventoryInNewTab();
//        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
//        PageBase.InventoryManagementPage().closeInventoryTab();
//        Utilities.switchPreviousTab();
//        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails.IMEI + "(ProdCode: " + imeiDetails.ProductName + ")");

        Log.startTestCase("QA92_AddALineNumberPortWithDepositActivationThroughShipadmin");
        Reporter.log("<h2>Start - QA92_AddALineNumberPortWithDepositActivationThroughShipadmin. <br></h2>");
        Reporter.log("Launching Browser <br>", true);

        //Verify whether which enviorement to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        if (testtype.equals("internal")) {
            //Customizing xml files in Carrier Responder
            carrierResponderSettingsQA92();
        } else {
            selectingSprintExternalEnvironment();
        }

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        //Switching Back To Retail
        Utilities.switchPreviousTab();

        //POA FLOW
        orderId = poaFlowQA92(receiptId, iMEINumber, simNumber, phoneNumber, sSN, accountPassword, orderId);


        if (readConfig("Activation").toLowerCase().contains("true")) {
            //Ship Admin
            shipAdminVerificationsQA92(orderId);

            //Inventory Management
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);
        }

        //DBError Verification.
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA92_AddALineNumberPortWithDepositActivationThroughShipadmin - Test Case Completes<h3>");
        Log.endTestCase("QA92_AddALineNumberPortWithDepositActivationThroughShipadmin");
    }
    //endregion QA-92

    //region QA 495
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_495_Sprint2YrAALExistingFamilyPlan(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        IMEIDetails imeiDetails = null;
        Log.startTestCase("QA_495_Sprint2YrAALExistingKeepExistingPlan");
        Reporter.log("<h2> QA_495_Sprint2YrAALExistingKeepExistingPlan</h2>");
        Reporter.log("<h4>Description:</h4> QA_495_Sprint2YrAALExistingKeep Existing plan");

        //Samsung device and 3FF sim needed
        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.Sprint2yrUpgrade);

        //Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails.IMEI + "(ProdCode: " + imeiDetails.ProductName + ")");

        Log.startTestCase("QA_495_Sprint2YrAALExistingFamilyPlan");
        testType = BrowserSettings.readConfig("test-type");

        //Verify whether which enviornment to use internal or external.
        Reporter.log("<br> Test Type Settings");
        if (testType.equals("internal")) {

            //Customizing xml files in Carrier Responder
            carrierResponderSettingsQA495(accountDetails.MTN);
        } else {
            selectingSprintExternalEnvironment();
        }
        Utilities.switchPreviousTab();

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = QA_495_poaCompleteFlow(testType, imeiDetails, accountDetails);
        Reporter.log("<h2> POA Flow Finishes</h2>");
        //endregion

        if (readConfig("Activation").contains("true")) {
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails.IMEI, Constants.SOLD);

            //Ship Admin Verification -orderId= ""
            shipAdminVerifications_495(orderId);
        }
        Reporter.log("<h3>QA_495_Sprint2YrAALExistingFamilyPlan - Test Case Completes<h3>");
        Log.endTestCase("QA_495_Sprint2YrAALExistingFamilyPlan");
    }

//endregion QA 495

    //region QA 4258
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_4258_SprintEasyAddaLine(@Optional String testtype) throws InterruptedException, AWTException, IOException {

        String sTestCaseName = "QA_4258_SprintEasyAddaLine";

        // Printing the start of Test Case
        Log.startTestCase(sTestCaseName);
        IMEIDetails imeiDetails = null;
        String testType = BrowserSettings.readConfig("test-type");
        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintEasyPayUpgrade);
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        String phoneNumber = accountDetails.MTN;
        String sSN = accountDetails.SSN;
        String accountPassword = accountDetails.Password;
        String orderId = "";
        String receiptId = "";

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));
            carrierResponderSettingsQA4258();
        } else  //External
        {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
        }

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        //Calling DBError utility to find initial count or error in log files.
        Reporter.log("<br> DB Errors Initial Check:");
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaFlowQA4258(phoneNumber, imeiDetails.IMEI, imeiDetails.Sim, orderId, sSN, accountPassword);
        Reporter.log("<h2> POA Flow Finishes</h2>");
        //endregion

        //Ship Admin Verification
        Reporter.log("<h2> ShipAdmin Verification:</h2>");
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);

        //Inventory Management
        Reporter.log("<h2> Inventory Management Page: IMEI Status Check</h2>");
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails.IMEI, Constants.SOLD);
        Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

        //DB Errors
    /*DBError.navigateDBErrorPage();
    Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));*/

        PageBase.CSVOperations();
        CSVOperations.WriteToCSV("QA_4258", orderId, imeiDetails.IMEI, "", "", customerDetails.FirstName, customerDetails.LastName,
                customerDetails.EMail, receiptId, customerDetails.IDType, customerDetails.State,
                customerDetails.IDNumber, customerDetails.PhNum, customerDetails.Zip, sSN,
                customerDetails.IDExpirationMonth, customerDetails.IDExpirationYear);

        Reporter.log("<h3>QA_4258_SprintEasyAddaLine - Test Case Completes<h3>");
        Log.endTestCase("QA_4258_SprintEasyAddaLine");
    }
//endregion QA 4258

    //region QA 3181
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_3181_SprintAddaLineCancelOnInstallmentDetails(@Optional String testtype) throws InterruptedException, AWTException, IOException {

        String sTestCaseName = "QA_3181_SprintAddaLineCancelOnInstallmentDetails";

        // Printing the start of Test Case
        Log.startTestCase(sTestCaseName);
        IMEIDetails imeiDetails = null;
        String testType = BrowserSettings.readConfig("test-type");
        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_QA3181);
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintEasyPayUpgrade);
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        String phoneNumber = accountDetails.MTN;
        String sSN = accountDetails.SSN;
        String accountPassword = accountDetails.Password;
        String orderId = "";
        String receiptId = "";

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> Test Type Settings");
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));

            selectCarrierResponderDetailsQA3181();
        } else  //External
        {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
        }

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        //Calling DBError utility to  find initial count or error in log files.
        Reporter.log("<br> DB Errors Initial Check:");
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaFlowQA3181(phoneNumber, imeiDetails.IMEI, imeiDetails.Sim, orderId, sSN, accountPassword);
        //endregion

        //Ship Admin Verification
        Reporter.log("<h2> ShipAdmin Verification:</h2>");
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);

        //Inventory Management
        Reporter.log("<h2> Inventory Management Page: IMEI Status  Check</h2>");
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails.IMEI, Constants.SOLD);
        Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

        //DB Errors
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        PageBase.CSVOperations();
        CSVOperations.WriteToCSV("QA_3181", orderId, imeiDetails.IMEI, "", "", customerDetails.FirstName, customerDetails.LastName,
                customerDetails.EMail, receiptId, customerDetails.IDType, customerDetails.State,
                customerDetails.IDNumber, customerDetails.PhNum, customerDetails.Zip, sSN,
                customerDetails.IDExpirationMonth, customerDetails.IDExpirationYear);

        Reporter.log("<h3>QA_3181_SprintAddaLineCancelOnInstallmentDetails - Test Case Completes<h3>");
        Log.endTestCase("QA_3181_SprintAddaLineCancelOnInstallmentDetails");
    }
//endregion

    //region QA 3137
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_3137_DoActivationAPI_ReturnErrorCode(@Optional String testType) throws Exception {
        String orderId = "";
        testType = BrowserSettings.readConfig("test-type");
        Log.startTestCase("QA_3137_DoActivationAPI_ReturnErrorCode");
        Reporter.log("<h2>QA_3137_DoActivationAPI_ReturnErrorCode</h2>");
        Reporter.log("<h4> Test Case Description: </h4>on EasyPay -> doActivation API returns an error code");
        testType = BrowserSettings.readConfig("test-type");

        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.Sprint_FamilyPlan);
        IMEIDetails imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName
                (CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

        ////region Adding Inventory
        Reporter.log("<h2> Adding Item to Inventory: </h2>");
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
        Utilities.WaitUntilElementIsClickable(PageBase.InventoryManagementPage().productsLink);
        PageBase.InventoryManagementPage().productsLink.click();
        Utilities.WaitUntilElementIsClickable(PageBase.InventoryManagementPage().serializedProducts);
        PageBase.InventoryManagementPage().serializedProducts.click();
        driver.switchTo().frame(PageBase.InventoryManagementPage().iframeInventory);
        Utilities.WaitUntilElementIsClickable(PageBase.InventoryManagementPage().imeiTextbox);

        if (!PageBase.InventoryManagementPage().deviceStatus(imeiDetails.IMEI))
            Assert.fail("FAILED TO ADD ITEM TO INVENTOTY");
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails.IMEI + "(ProdCode: " + imeiDetails.ProductName + ")");
        Reporter.log("<br> Account Subscription Num: " + accountDetails.MTN + " SSN: " + accountDetails.SSN + "<br>");

        //region Test Type Settings
        Reporter.log("<br> Test Type Settings to External");
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
        Utilities.switchPreviousTab();
        //endregion

        // //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Count: " + initialCount);
        Utilities.switchPreviousTab();

        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_QA_4255(testType, accountDetails, imeiDetails, "3137");
        System.out.println(orderId);
        Reporter.log("<h2> orderId: " + orderId);
        Reporter.log("<h2> POA Flow Finishes </h2>");

        Reporter.log("<h2> Activation API Response Check: </br>");
        VerifyAPIResponseForQA_3137(orderId);
        Reporter.log("<h2> API Check Finishes </h2>");
//
        //Inventory Management Page verification.
        if (readConfig("Activation").contains("true")) {
            //Ship Admin Verification -orderId= ""
            Reporter.log("<h2> Shipadmin Verification </h2>");
            shipAdminVerificationQA_3137(orderId);
            Reporter.log("<h2> Shipadmin Check Pass </h2>");
        }

        //DBError Verification.
        Reporter.log("<h2> DB Error Check </h2>");
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        Reporter.log("<h2> DB Error Check Finishes with No Errors </h2>");

        Reporter.log("<h3>QA_3137_DoActivationAPI_ReturnErrorCode - Test Case Completes<h3>");
        Log.endTestCase("QA_3137_DoActivationAPI_ReturnErrorCode");
    }

    //endregion

    //region QA 4255
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_4255_SprintAALToExistingOptEasyPayAlterDownPay(@Optional String testType) throws IOException, InterruptedException, AWTException {
        //Needed: 1 Family accountl
        //Needed: 1 IMEI @ Sprint_SamsungGalaxyS4_16GBWhite
        IMEIDetails imeiDetails1 = null;
        String orderId = "";
        Log.startTestCase("QA_4255_SprintAALToExistingOptEasyPayAlterDownPay");
        Reporter.log("<h2> QA_4255_SprintAALToExistingOptEasyPayAlterDownPay</h2>");
        Reporter.log("<h4>Description:</h4> SPT: AAL: Add A Line to existing Family: " +
                "Opt for EasyPay, Alternate Down Pay option & reach Receipt page.");
        testType = BrowserSettings.readConfig("test-type");
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.Sprint_FamilyPlan);
        IMEIDetails imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName
                (CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

        //////region Adding Inventory
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        //TestTypeSettings_QA_4255(accountDetails.MTN);
        Utilities.switchPreviousTab();

        // //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");
        Utilities.switchPreviousTab();

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_QA_4255(testType, accountDetails, imeiDetails, "4255");
        Reporter.log("<h2> POA Flow Finishes</h2>");
        //endregion

        //Inventory Management Page verification.
        if (testType.contains("true")) {
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            Utilities.implicitWaitSleep(5000);
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails.IMEI, InventoryManagementBaseClass.IMEIStatus.Sold.toString());
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

//Ship Admin Verification -orderId= ""
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            shipAdminVerification(orderId);
        }

        //DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_4255_SprintAALToExistingOptEasyPayAlterDownPay - Test Case Completes<h3>");
        Log.endTestCase("QA_4255_SprintAALToExistingOptEasyPayAlterDownPay");
    }

    //endregion QA 4255
    //endregion Test Methods

    //region Helper and Refactored Methods
    private void selectingSprintExternalEnvironment() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
        Reporter.log("<h3><U> External Server</U></h3>", true);
    }

    //region QA 495 private methods
    private String QA_495_poaCompleteFlow(@Optional String testType, IMEIDetails imeiDetails, AccountDetails accountDetails) throws IOException {
        String orderId = "";//Login to retail page.
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");

        String MTNNumber = accountDetails.MTN;
        String accountPassword = accountDetails.Password;
        String SSN = accountDetails.SSN;
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        //Click on Sales and Activation Link
        PageBase.HomePageRetail().upgradeEligibilityCheckerLink.click();

        //Fill Sprint Details
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
        //Utilities.waitForElementVisible(PageBase.UECVerificationPage().phoneNumberSprintTextbox);
        PageBase.UECVerificationPage().fillSprintDetails(MTNNumber, SSN, accountPassword);
        PageBase.UECVerificationPage().continueSprintButton.click();

        //UEC Add Lines Page
        Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
        PageBase.UECAddLinesPage().clickFirstEnabledCheckbox();
        PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

        //Enter IMEI of the device
        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails.IMEI);
        //PageBase.VerizonEdgePage().declineSprintEasyPay();
        PageBase.CommonControls().cancelButton.click();
        //Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        //PageBase.CommonControls().continueButtonDVA.click();

        Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().keepMyExistingSprintPlan);
        PageBase.SprintShopPlansPage().keepMyExistingSprintPlan.click();

        //Select Any one Number from the Dropdown to Assign plan
        Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
        PageBase.CartPage().selectPhoneNumber();
        PageBase.CommonControls().continueCommonButton.click();

        //Select a Feature
        do {
            Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
            PageBase.VerizonSelectPlanFeaturesPage().clickContinue();
        } while (driver.getCurrentUrl().contains("features.htm"));

        // Selecting No Insurance .
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();
        } catch (Exception ex) {
        }

        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().populateFormByClassButton);
        PageBase.ServiceProviderVerificationPage().populateFormByClassButton.click();
        //PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();

        PageBase.CommonControls().continueButton.click();

        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {

        }

        //Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        PageBase.CommonControls().continueCommonButton.click();

        //Terms and Condition Page.
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
        PageBase.TermsAndConditionsPage().emailTCChkBox.click();
        PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.TermsAndConditionsPage().continueTCButton.click();

        if (readConfig("Activation").contains("true")) {
            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();

            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);

            PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(imeiDetails.IMEI);
            PageBase.DeviceVerificationaandActivation().submitDVAButton.click();

            PageBase.DeviceVerificationaandActivation().simType.sendKeys(imeiDetails.Sim);
            // PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
            PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
            Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }

        return orderId;
    }

    private void shipAdminVerifications_495(String orderId) {
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
//    Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
//    Assert.assertTrue(eventLogTableContent.contains(Constants.ORDER_VALIDATION_PASSED));
//    Assert.assertTrue(eventLogTableContent.contains(Constants.LINE_ACTIVATION_SUCCEEDED));
//    Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
//    Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.HANDSET_UPGRADE));
//    //Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
//    Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.EXISTING_ACCOUNT_ORDER));
    }

    private void carrierResponderSettingsQA495(String phoneNumber) throws InterruptedException, AWTException, IOException {
        Robot robot = new Robot();
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();

        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Verizon and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        //PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loanleaseEligible_multiple.xml");
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "noloannoleaseEligible.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "upgrade_success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(6000);
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace(Constants.DEFAULT_XML_NUMBER_4193881179, phoneNumber);
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(6000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(3000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_upgrade_individual_single_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent3 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent3 = xmlContent3.replace("<plan id=\"3473411\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41570111\">\n" +
                "                    <phone id=\"5417721111\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent3, robot);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "servicevalidation", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(6000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "individual_line_success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(6000);
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace(Constants.DEFAULT_XML_NUMBER_6157157755, phoneNumber);
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent1, robot);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
    }

    //endregion  QA 495 private methods

    //region QA 3181 Private Methods

    private String poaFlowQA3181(String phoneNumber, String iMEINumber, String simNumber, String orderId,
                                 String ssn, String zipCode) throws IOException, AWTException, InterruptedException {

        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();
        String phNo = PageBase.CSVOperations().GenerateRandomNoForNumberPorting();

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
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
        PageBase.UECVerificationPage().sprintTab.click();
        PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
        if (ssn.length() > 4) {
            PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(ssn.substring(5, ssn.length()));
        } else {
            PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(ssn);
        }
        PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(zipCode);
        PageBase.UECVerificationPage().continueSprintButton.click();

        //Select an Option Page
        Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().AALExistingFamilyPlan);
        PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        //PageBase.CommonControls().continueButton.click();

        // Installment Page
        Utilities.waitForElementVisible(PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton);
        PageBase.InstallmentPage().decline.click();

        // Device Scan Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
        cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();

        // Credit Check Verification Results
        Utilities.waitForElementVisible(PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox);
        PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
        PageBase.CommonControls().continueCommonButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        //String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
        //String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
        PageBase.CartPage().continueCartButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.click();
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        // Selecting Number Porting.
        Utilities.ClickElement(PageBase.NumberPortPage().noNumberPortRadiobutton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

    /*Order Review and Confirm Page */
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().firstPhonePrice.isDisplayed());
        //Assert.assertEquals(OrderReviewAndConfirmPage().phoneMonthlyFee.getText(), phonePrice);
        // Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        //String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
            PageBase.TermsAndConditionsPage().emailTCChkBox.click();
            PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            // Credit Card Payment Page
            Utilities.implicitWaitSleep(10000);
            if (driver.getCurrentUrl().contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
            }

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            //Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());

            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(iMEINumber, simNumber);
            PageBase.CommonControls().continueButtonDVA.click();

            // Order Activation Complete page.
            Utilities.implicitWaitSleep(5000);
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
            Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void selectCarrierResponderDetailsQA3181() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();

        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loaneligible.xml");
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "newactivation_NO_DEPOSIT_REQUIRED_LOANELIGIBLE.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current", "servicevalidation", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "addline_family_3_lines_eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
        Robot robot = new Robot();
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace(Constants.DEFAULT_ACCOUNT_VALIDATION_PTN_NUMBER, "<ptn>9476199547</ptn>");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent1, robot);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "createloancontract", "success_lease_request.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_individual_no_numport.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace("<plan id=\"3473210\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41588210\">\n" +
                "                    <phone id=\"5415551112\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
        Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
    }

    //endregion QA 3181 Private Methods

    //region QA 3171
    private void VerifyAPIResponseForQA_3137(String orderId) throws Exception {
        String url = null;
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        driver.navigate().to(readConfig("apiHistory"));
        Utilities.WaitUntilElementIsClickable(PageBase.AdminPage().orderIdLink);
        PageBase.AdminPage().orderIdLink.sendKeys(orderId);
        Robot robot = new Robot();
        Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
        Utilities.waitForDocumentReady(driver);
        Utilities.ScrollToElement(PageBase.AdminPage().lastActivationAPI);
        url = PageBase.AdminPage().lastActivationAPI.getText();
        driver.findElement(By.xpath(ControlLocators.ACTIVATION_API + "/a")).click();
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_ALT);
        Utilities.implicitWaitSleep(1000);
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_ALT);
        Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
        Utilities.waitForDocumentReady(driver);
        Utilities.implicitWaitSleep(1000);
        url = url.replace(":", "_");
        url = url.replace("/", "_");
//        url = "%temp%\\" + url;
        String validateContent = loadTestDocument(url);
        Assert.assertTrue(validateContent.contains("ICC_ID_NOT_FOUND_INVENTORY"));
        Assert.assertTrue(validateContent.contains("Please resubmit with a different ICC_ID"));
        Assert.assertTrue(validateContent.contains("<error-type>2</error-type>"));
        Reporter.log("<br> Activation XML Content");
        Reporter.log("<br> ICC_ID_NOT_FOUND_INVENTORY");
        Reporter.log("<br> Please resubmit with a different ICC_ID");
        Reporter.log("<br> <error-type>2</error-type>");
        int position = validateContent.indexOf("<details>");
        Reporter.log("<br>" + validateContent.substring(validateContent.indexOf("<details>"),
                validateContent.indexOf("</details>")));

        File file = new File(url);
        System.out.println(file.getAbsolutePath());
        file.delete();
    }

    private static String loadTestDocument(String url) throws Exception {
        java.util.Properties properties = System.getProperties();
        String tempFileName = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
        url = tempFileName + url;
//        File fXmlFile = new File(url);


        BufferedReader reader = new BufferedReader(new FileReader(url));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
//
    }

    private static void shipAdminVerificationQA_3137(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        Reporter.log("<h3> Event Logger Verification: </h3>");
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.IN_STORE_ACTIVATION);
        Reporter.log("<br><h4>Status on Shipadmin: " + status + "</h4>");
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
        Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains("Existing Account Order"));
        Reporter.log("<br> Additional Info: " + PageBase.OrderSummaryPage().additionalInfoValueText.getText());
        System.out.println(PageBase.OrderSummaryPage().additionalInfoValueText.getText());
        Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().
                contains("Add added line to existing family plan"));
        Reporter.log("<br>AdditionalInfo: " + PageBase.OrderSummaryPage().additionalInfoValueText.getText());
    }

    private CarrierCreditCheckDetails getCarrierCreditCheckDetails(String ssn) throws IOException {
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
        cccDetails.setSSN(ssn);
        cccDetails.setIDType(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        cccDetails.setIdTypeState(customerDetails.IDState);
        cccDetails.setIdNumber(customerDetails.IDNumber);
        cccDetails.setMonth(customerDetails.IDExpirationMonth);
        cccDetails.setYear(customerDetails.IDExpirationYear);
        return cccDetails;
    }
    //endregion Qa 3171

    //region QA 4258 Refactored Methods
    private String poaFlowQA4258(String phoneNumber, String iMEINumber, String simNumber, String orderId,
                                 String ssn, String zipCode) throws IOException, AWTException, InterruptedException {

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
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
        PageBase.UECVerificationPage().sprintTab.click();
        PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
        if (ssn.length() > 4) {
            PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(ssn.substring(5, ssn.length()));
        } else {
            PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(ssn);
        }
        PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(zipCode);
        PageBase.UECVerificationPage().continueSprintButton.click();

        //Select an Option Page
        Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().AALExistingFamilyPlan);
        PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        //PageBase.CommonControls().continueButton.click();

        try {
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
        }
        catch(Exception ex) {
        }

        // Installment Page
        Utilities.waitForElementVisible(PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton);
        PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton.click();
        PageBase.CommonControls().continueCommonButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        //String phonePrice = PageBase.CartPage().phonePriceAALText.getText();
        //String phoneModel = PageBase.CartPage().phoneModelAALLink.getText();
        PageBase.CartPage().continueCartButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.click();
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        // Selecting Number Porting.
        Utilities.ClickElement(PageBase.NumberPortPage().numberPortRadiobutton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        PageBase.PortMyNumbersPage().enterPortDataForPreCreditCheck(portDetails.CurrentPhoneNumber, portDetails.Carrier,
                portDetails.CurrentAccNumber, portDetails.SSN);

/*Order Review and Confirm Page */
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().firstPhonePrice.isDisplayed());
        //Assert.assertEquals(OrderReviewAndConfirmPage().phoneMonthlyFee.getText(), phonePrice);
        // Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().existingPlanDiv.isDisplayed());
        // String existingPlan = PageBase.OrderReviewAndConfirmPage().existingPlanDiv.getText();
        // Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        //String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
            PageBase.TermsAndConditionsPage().emailTCChkBox.click();
            PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            // Credit Card Payment Page
            Utilities.implicitWaitSleep(10000);
            if (driver.getCurrentUrl().contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
            }

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(iMEINumber, simNumber);
            PageBase.CommonControls().continueButtonDVA.click();

            //Device Financing Installment Contract.
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
            PageBase.DeviceFinancingInstallmentContractPage().print.click();
            Utilities.implicitWaitSleep(3000);
            Robot robot = new Robot();
            Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
            Utilities.implicitWaitSleep(6000);
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
            PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();
            Utilities.implicitWaitSleep(2000);

            // Order Activation Complete page.
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void carrierResponderSettingsQA4258() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();

        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loaneligible.xml");
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck",
                "newactivation_DEPOSIT_REQUIRED_SL_REQUIRED_LOANELIGIBLE.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        Robot robot = new Robot();
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace(Constants.SL_WITH_DEPOSIT, "<spending-limit-amount>0.0</spending-limit-amount>");
        xmlContent2 = xmlContent2.replace(Constants.SL_WITH_DEPOSIT, "<spending-limit-amount>0.0</spending-limit-amount>");
        xmlContent2 = xmlContent2.replace(Constants.DEPOSIT_AMOUNT, "<deposit-amount>0.0</deposit-amount>");
        xmlContent2 = xmlContent2.replace(Constants.DEPOSIT_AMOUNT, "<deposit-amount>0.0</deposit-amount>");
        Utilities.implicitWaitSleep(3000);
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_individual_with_numport.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        String xmlContent3 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent3 = xmlContent3.replace("<plan id=\"3473210\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41588210\">\n" +
                "                    <phone id=\"5415551112\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent3, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "servicevalidation", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "addline_family_3_lines_eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace(Constants.DEFAULT_ACCOUNT_VALIDATION_PTN_NUMBER, "<ptn>9476199547</ptn>");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent1, robot);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
    }
    //endregion QA 4258 Refactored Methods

    private void serverDBVerificationsQA3065(String orderId) {
        Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().chooseQueryWrapperDropdown);
        Utilities.dropdownSelect(PageBase.SQLUtilAdminPage().chooseQueryWrapperDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "2");
        PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("select * from ordersignatures where ordid=" + orderId);
        PageBase.SQLUtilAdminPage().submitButton.click();
        Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().orderSignaturesTable);
        String orderSignaturesTableContent = PageBase.SQLUtilAdminPage().orderSignaturesTable.getText();
        int rowCount = StringUtils.countMatches(orderSignaturesTableContent, orderId);
        Assert.assertEquals(rowCount, 2);
        PageBase.SQLUtilAdminPage().queryTextbox.clear();
        PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("Select * from orderitemfinanceinfos where ordid=" + orderId);
        Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().generalTable);
        String tableContent = PageBase.SQLUtilAdminPage().generalTable.getText();
        Assert.assertTrue(tableContent.contains(orderId));
        PageBase.SQLUtilAdminPage().queryTextbox.clear();
        PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("select * from  orderiteminfos where ordid=" + orderId);
        Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().generalTable);
        tableContent = PageBase.SQLUtilAdminPage().generalTable.getText();
        Assert.assertTrue(tableContent.contains(orderId));
    }

    private void shipAdminUpgradeVerifications(String orderId) {
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ORDER_VALIDATION_PASSED));
        Assert.assertTrue(eventLogTableContent.contains(Constants.LINE_ACTIVATION_SUCCEEDED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.HANDSET_UPGRADE));
        Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.EXISTING_ACCOUNT_ORDER));
    }

    private String poaFlowQA3065(String receiptId, String iMEINumber, String simNumber, String phoneNumber, String sSN, String accountPassword, String orderId) throws IOException, InterruptedException, AWTException {
        //Login
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        //Home Page
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Choose A Path Page
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
        PageBase.ChoosePathPage().existingCarrier.click();

        //Pick Your Path Page
        Utilities.waitForElementVisible(PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton);
        PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //UEC Verification Page
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
        PageBase.UECVerificationPage().sprintTab.click();
        PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
        PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(sSN);
        PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(accountPassword);
        PageBase.UECVerificationPage().continueSprintButton.click();

        //UEC Add Lines Page
        Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
        PageBase.UECAddLinesPage().clickFirstEnabledCheckbox();
        PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        Assert.assertTrue(PageBase.SprintEasyPayPage().priceBox.getText().contains("2yr agreement"));
        PageBase.SprintEasyPayPage().yesButton.click();

        //Carrier Credit Check Page
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        try {
            PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
        }

        //Sprint Easy Pay Eligibility Result
        Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());
        PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
        PageBase.CommonControls().continueCommonButton.click();

        //Sprint Shop Plans Page
        Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton);
        PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
        Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "1");
        String phonePrice = PageBase.CartPage().device1Price.getText();
        String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
        Assert.assertTrue(PageBase.CartPage().downPaymentAmountLabel.isDisplayed());
        Assert.assertTrue(PageBase.CartPage().monthlyDeviceInstallmentBalanceLabel.isDisplayed());
        Assert.assertTrue(PageBase.CartPage().monthlyRecurringFeeLabel.isDisplayed());
        Assert.assertTrue(PageBase.CartPage().totalDueTodayLabel.isDisplayed());
        Assert.assertTrue(PageBase.CartPage().lastMonthlyInstallmentMayBeDifferentText.isDisplayed());
        PageBase.CartPage().continueCartButton.click();

        //Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();

        //Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().device1PriceActual.isDisplayed());
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), phonePrice); //Not matching right now
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().downPaymentForSprintEasyPayLabel.isDisplayed());
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().lastMonthInstallmentLabel.isDisplayed());
        //Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().monthlyInstallmentLabel.isDisplayed()); //Not coming after OOPIS Changes
        //Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().downPaymentLabel.isDisplayed());  //Not coming after OOPIS Changes
        //Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().monthlyRecurringFeeLabel.isDisplayed()); //Not coming after OOPIS Changes
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalDue = totalDue.replace("inc. tax", "");
        totalDue = totalDue.replace(" ", "");
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox);
        PageBase.TermsAndConditionsPage().carrierTermsAcceptCheckbox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.TermsAndConditionsPage().signingTermsAndConditions(driver);

        if (readConfig("Activation").toLowerCase().contains("true")) {
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            //Payment Required Page //ToDo: Remove this when no insurance bug will fix premanently.
            Utilities.implicitWaitSleep(4000);
            String url = driver.getCurrentUrl();
            if (url.contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                PageBase.PaymentRequiredPage().sameAddressTab.click();
                PageBase.PaymentRequiredPage().continuePRButton.click();
            }

            //Print Mobile Scan Sheet Page
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            //Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice);
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

            //Device Financing Installment Contract Page
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
            Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().print.isDisplayed());
            Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().totalAmountFinancedLabel.isDisplayed());
            Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().downPaymentLabel.isDisplayed());
            Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().monthlyInstallmentLabel.isDisplayed());
            Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().installmentContractLengthLabel.isDisplayed());
            Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().phoneImage.isDisplayed());
            Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().phoneModelText.isDisplayed());
            PageBase.DeviceFinancingInstallmentContractPage().PrintDeviceFinancingDetails(driver);

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), simNumber);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().printConfirmationSlipButton.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().deviceModelLabel.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().downPaymentLabel.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().monthlyDeviceInstallmentLabel.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().lastMonthInstallmentLabel.isDisplayed());
        }
        return orderId;
    }

    private String poaFlowQA3096(String receiptId, String iMEINumber, String simNumber, String phoneNumber, String sSN, String accountPassword, String orderId) throws IOException {
        //Login
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        //Home Page
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Choose A Path Page
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
        PageBase.ChoosePathPage().existingCarrier.click();

        //Pick Your Path Page
        Utilities.waitForElementVisible(PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton);
        PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //UEC Verification Page
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
        PageBase.UECVerificationPage().sprintTab.click();
        PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
        PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(sSN);
        PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(accountPassword);
        PageBase.UECVerificationPage().continueSprintButton.click();

        //UEC Add Lines Page
        PageBase.UECAddLinesPage().selectingFirstBuddyUpgradeLine();
        PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Sprint Shop Plans Page
        Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton);
        PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
        Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "1");
        String phonePrice = PageBase.CartPage().device1Price.getText();
        Assert.assertTrue(PageBase.CartPage().device1Price.isDisplayed());
        Assert.assertTrue(PageBase.CartPage().twoYearsActivationText.isDisplayed());
        String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
        PageBase.CartPage().continueCartButton.click();

        //Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();

        //Service Provider Verification Page
        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, customerDetails.LastName,
                customerDetails.EMail, ServiceProviderVerificationPage.IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber,
                customerDetails.IDExpirationMonth, Integer.parseInt(customerDetails.IDExpirationYear));
        PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck.click();
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

        //Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().device1PriceActual.isDisplayed());
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), phonePrice);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalDue = totalDue.replace("inc. tax", "");
        totalDue = totalDue.replace(" ", "");
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox);
        PageBase.TermsAndConditionsPage().carrierTermsAcceptCheckbox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.TermsAndConditionsPage().signingTermsAndConditions(driver);

        if (readConfig("Activation").toLowerCase().contains("true")) {
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            //Payment Required Page //ToDo: Remove this when no insurance bug will fix premanently.
            Utilities.implicitWaitSleep(4000);
            String url = driver.getCurrentUrl();
            if (url.contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                PageBase.PaymentRequiredPage().sameAddressTab.click();
                PageBase.PaymentRequiredPage().continuePRButton.click();
            }

            //Print Mobile Scan Sheet Page
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            //Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().storeLocationValuePMSSText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().barcodePMSSImage.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().phoneModelText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.isDisplayed());
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

            //Support Center Page
            Utilities.waitForElementVisible(PageBase.CommonControls().supportCenterText, 400);
            Reporter.log("<br> This order needs to be manually activated through shipadmin. Order Id =" + orderId);

//            //Order Activation and Complete Page
//            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
//            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
        }
        return orderId;
    }

    private String poaFlowQA87(String receiptId, String iMEINumber1, String iMEINumber2, String simNumber1, String simNumber2, String phoneNumber1, String sSN, String accountPassword, String orderId) throws IOException {
        //Login
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        //Home Page
        PageBase.HomePageRetail().upgradeEligibilityCheckerLink.click();

        //UEC Verification Page
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
        PageBase.UECVerificationPage().sprintTab.click();
        PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber1);
        PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(sSN);
        PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(accountPassword);
        PageBase.UECVerificationPage().continueSprintButton.click();

        //UEC Add Lines Page
        PageBase.UECAddLinesPage().clickFirstTwoEnabledCheckbox();
        PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber1);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().noContinueWith2YearButton, 8);
        PageBase.SprintEasyPayPage().noContinueWith2YearButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber2);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().noContinueWith2YearButton, 8);
        PageBase.SprintEasyPayPage().noContinueWith2YearButton.click();

        //Sprint Shop Plans Page
        Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton);
        PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
        Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "2");
        Utilities.dropdownSelect(PageBase.CartPage().secondAssignNumberDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "1");
        String phonePrice1 = PageBase.CartPage().device1Price.getText();
        String phonePrice2 = PageBase.CartPage().device2Price.getText();
        PageBase.CartPage().continueCartButton.click();

        //Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.click();
        if (!PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.isSelected()) {
            PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.click();
        }
        PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceSecond.click();
        if (!PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceSecond.isSelected()) {
            PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceSecond.click();
        }
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, customerDetails.LastName,
                customerDetails.EMail, ServiceProviderVerificationPage.IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber,
                customerDetails.IDExpirationMonth, Integer.parseInt(customerDetails.IDExpirationYear));
        PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck.click();
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

        //Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().device1PriceActual.isDisplayed());
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), phonePrice);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalDue = totalDue.replace("inc. tax", "");
        totalDue = totalDue.replace(" ", "");
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox);
        PageBase.TermsAndConditionsPage().carrierTermsAcceptCheckbox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.TermsAndConditionsPage().signingTermsAndConditions(driver);

        if (readConfig("Activation").toLowerCase().contains("true")) {
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            //Payment Required Page //ToDo: Remove this when no insurance bug will fix premanently.
            Utilities.implicitWaitSleep(4000);
            String url = driver.getCurrentUrl();
            if (url.contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                PageBase.PaymentRequiredPage().sameAddressTab.click();
                PageBase.PaymentRequiredPage().continuePRButton.click();
            }

            //Print Mobile Scan Sheet Page
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            //Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice1);
            //Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phone2PriceValuePMSSText.getText(), phonePrice2);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().storeLocationValuePMSSText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().storeLocationValuePMSSText.getText().contains("0003"));
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().barcodePMSSImage.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.isDisplayed());
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            //Payment Verification Page
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation Page
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(iMEINumber1);
            PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
            PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(iMEINumber2);
            PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
            PageBase.DeviceVerificationaandActivation().simType.clear();
            PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber1);
            PageBase.DeviceVerificationaandActivation().simType2Textbox.clear();
            PageBase.DeviceVerificationaandActivation().simType2Textbox.sendKeys(simNumber2);
            try  //ToDo:Remove this when no insurance bug will fix.
            {
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
                //PageBase.DeviceVerificationaandActivationPage().cvnNumberDVATextbox.sendKeys("123");
                CreditCardDetails CreditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
                String cVNNumberS = "" + CreditCard.CVV;
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(cVNNumberS);
            } catch (Exception e) {
            }
            PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber2);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValue2Text.getText(), iMEINumber1);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), simNumber1);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValue2Text.getText(), simNumber2);
            //Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValueText.getText(), phonePrice1);
            //Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValue2Text.getText(), phonePrice2);
        }
        return orderId;
    }

    private void carrierResponderSettingsQA87(String phoneNumber1, String phoneNumber2) throws InterruptedException, AWTException, IOException {
        Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "family_2_lines_eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace("5869333084", phoneNumber1);
        xmlContent1 = xmlContent1.replace("5865240296", phoneNumber2);
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Robot robot = new Robot();
        Utilities.copyPaste(xmlContent1, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_upgrade_family_2_lines.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace("<plan id=\"3436611\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"42006111\">\n" +
                "                    <phone id=\"5415551111\">");
        xmlContent2 = xmlContent2.replace(" <plan id=\"3439721\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"42002210\">\n" +
                "                    <phone id=\"5415551112\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
    }

    private void selectCarrierResponderDetails() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();

        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Verizon and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loanleaseEligible_multiple.xml");
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "upgrade_success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_upgrade_individual_single_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current", "servicevalidation", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "individual_line_success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private void shipAdminVerificationsQA92(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains("Activation completed."));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
    }

    private void carrierResponderSettingsQA92() throws InterruptedException, AWTException, IOException {
        Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "family_2_lines_eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "existing_family_plan_added_lines_require__deposit.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "porteligibility", "eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "preauthorization", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "error_newactivation_individual_transaction_not_completed_call_nss.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
    }

    private String poaFlowQA92(String receiptId, String iMEINumber, String simNumber, String phoneNumber, String sSN, String accountPassword, String orderId) throws IOException {
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
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
        PageBase.UECVerificationPage().sprintTab.click();
        PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
        PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(sSN);
        PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(accountPassword);
        PageBase.UECVerificationPage().continueSprintButton.click();

        //Select an Option Page
        Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().AALExistingFamilyPlan);
        PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().noContinueWith2YearButton);
        PageBase.SprintEasyPayPage().noContinueWith2YearButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().continueDSButton);
        PageBase.DeviceScanPage().continueDSButton.click();

        //Credit Check Page
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().firstNameTextBox);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithDeposit));
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        try {
            PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
        }

        //Credit Check and Verifications Results Page
        Utilities.waitForElementVisible(PageBase.CreditCheckVerificationResultsPage().depositCheckBox, 120);
        PageBase.CreditCheckVerificationResultsPage().depositCheckBox.click();
        PageBase.CommonControls().continueCommonButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        PageBase.CartPage().continueCartButton.click();

        //Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst);
        PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.click();
        if (!PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.isSelected()) {
            PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.click();
        }
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        //Number Port Page
        Utilities.waitForElementVisible(PageBase.NumberPortPage().numberPortRadiobutton);
        PageBase.NumberPortPage().numberPortRadiobutton.click();
        PageBase.NumberPortPage().continueSPVButton.click();

        //Port My Number(s) Page
        Utilities.waitForElementVisible(PageBase.PortMyNumbersPage().currentPhoneNumber);
        NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();
        PageBase.PortMyNumbersPage().enterPortDataForPreCreditCheck(portDetails.CurrentPhoneNumber, portDetails.Carrier,
                portDetails.CurrentAccNumber, portDetails.SSN);
//        PageBase.PortMyNumbersPage().currentPhoneNumber.sendKeys(phoneNumber);
//        Utilities.dropdownSelect(PageBase.PortMyNumbersPage().selectCarrier, Utilities.SelectDropMethod.SELECTBYINDEX, "5");
//        PageBase.PortMyNumbersPage().currentAccountNumber.sendKeys(phoneNumber);
//        PageBase.PortMyNumbersPage().currentAccountPasswordTextbox.sendKeys(accountPassword);
//        PageBase.CommonControls().continueButton.click();

        //Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().device1PriceActual.isDisplayed());
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalDue = totalDue.replace("inc. tax", "");
        totalDue = totalDue.replace(" ", "");
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox);
        PageBase.TermsAndConditionsPage().carrierTermsAcceptCheckbox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.TermsAndConditionsPage().signingTermsAndConditions(driver);

        if (readConfig("Activation").toLowerCase().contains("true")) {
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            //Credit Check Verification Results Page
            Utilities.waitForElementVisible(PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox);
            PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
            PageBase.CreditCheckVerificationResultsPage().continueButton.click();

            //Payment Required Page
            Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().creditCardNumberTextbox);
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
            PageBase.PaymentRequiredPage().sameAddressTab.click();
            PageBase.PaymentRequiredPage().continuePRButton.click();

            //Print Mobile Scan Sheet Page
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            //Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().storeLocationValuePMSSText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().storeLocationValuePMSSText.getText().contains("0003"));
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().barcodePMSSImage.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.isDisplayed());
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
            try {
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
                CreditCardDetails CreditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
                String cVNNumberS = "" + CreditCard.CVV;
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(cVNNumberS);
            } catch (Exception e) {
            }
            PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

            //Support Center Page
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDeposit, 600);

            //Ship Admin
            ShipAdminBaseClass.launchShipAdminInNewTab();
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            PageBase.OrderSummaryPage().banTextbox.sendKeys("1234567890");
            try {
                PageBase.OrderSummaryPage().insurancePaymentSecurityCodeTextbox.sendKeys("1214");
            } catch (Exception e) {
            }
            PageBase.OrderSummaryPage().cellPhoneNumberTextbox.sendKeys(phoneNumber);
            PageBase.OrderSummaryPage().manualCarrierWebSystemRadioButton.click();
            PageBase.OrderSummaryPage().activationCompleteButton.click();
            Utilities.waitForElementVisible(PageBase.OrderSummaryPage().moveSubqueuesButton);

            //Switch Back To Retail
            Utilities.switchPreviousTab();

            //Support Center Page
            PageBase.CommonControls().continueButtonDeposit.click();

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), simNumber);
        }
        return orderId;
    }

    private void carrierResponderSettingsQA3096(String phoneNumber, String donorPhoneNumber) throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "family_2_lines_eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace("5869333084", phoneNumber);
        xmlContent1 = xmlContent1.replace("5865240296", donorPhoneNumber);
        xmlContent1 = xmlContent1.replace("<eligibility-code>392575557-23</eligibility-code>\n" +
                "                  <current-tier>2</current-tier>", "<eligibility-code>392575557-23</eligibility-code>\n" +
                "                  <current-tier>1</current-tier>");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Robot robot = new Robot();
        Utilities.copyPaste(xmlContent1, robot);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_upgrade_individual_single_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "upgrade_success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loaneligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "createloancontract", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
    }

    private String poaFlowQA5268(String receiptId, String iMEINumber, String simNumber, String phoneNumber, String sSN, String accountPassword, String orderId) throws IOException {
        //Login
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        //Home Page
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Choose A Path Page
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
        PageBase.ChoosePathPage().existingCarrier.click();

        //Pick Your Path Page
        Utilities.waitForElementVisible(PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton);
        PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //UEC Verification Page
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
        PageBase.UECVerificationPage().sprintTab.click();
        PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
        PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(sSN);
        PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(accountPassword);
        PageBase.UECVerificationPage().continueSprintButton.click();

        //UEC Add Lines Page
        PageBase.UECAddLinesPage().selectingFirstEarlyUpgradeLine();
        PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().earlyCancelButton);
        PageBase.SprintEasyPayPage().earlyCancelButton.click();

        //Sprint Shop Plans Page
        Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton);
        PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
        Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "1");
        String phonePrice = PageBase.CartPage().device1Price.getText();
        Assert.assertTrue(PageBase.CartPage().device1Price.isDisplayed());
        Assert.assertTrue(PageBase.CartPage().twoYearsActivationText.isDisplayed());
        String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
        PageBase.CartPage().continueCartButton.click();

        //Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();

        //Service Provider Verification Page
        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, customerDetails.LastName,
                customerDetails.EMail, ServiceProviderVerificationPage.IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber,
                customerDetails.IDExpirationMonth, Integer.parseInt(customerDetails.IDExpirationYear));
        PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck.click();
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

        //Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().device1PriceActual.isDisplayed());
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), phonePrice);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalDue = totalDue.replace("inc. tax", "");
        totalDue = totalDue.replace(" ", "");
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox);
        PageBase.TermsAndConditionsPage().carrierTermsAcceptCheckbox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.TermsAndConditionsPage().signingTermsAndConditions(driver);

        if (readConfig("Activation").toLowerCase().contains("true")) {
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            //Payment Required Page //ToDo: Remove this when no insurance bug will fix premanently.
            Utilities.implicitWaitSleep(4000);
            String url = driver.getCurrentUrl();
            if (url.contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                PageBase.PaymentRequiredPage().sameAddressTab.click();
                PageBase.PaymentRequiredPage().continuePRButton.click();
            }

            //Print Mobile Scan Sheet Page
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            //Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().storeLocationValuePMSSText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().barcodePMSSImage.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().phoneModelText.isDisplayed());
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.isDisplayed());
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

            //Support Center Page
            Utilities.waitForElementVisible(PageBase.CommonControls().supportCenterText, 300);

            //Ship Admin - Manual Activation From Shipadmin
            ShipAdminBaseClass.launchShipAdminInNewTab();
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            //Assert.assertTrue(PageBase.OrderSummaryPage().);
            PageBase.OrderSummaryPage().banTextbox.sendKeys("1234567890");
            try {
                PageBase.OrderSummaryPage().insurancePaymentSecurityCodeTextbox.sendKeys("1214");
            } catch (Exception e) {
            }
            PageBase.OrderSummaryPage().cellPhoneNumberTextbox.sendKeys(phoneNumber);
            PageBase.OrderSummaryPage().manualCarrierWebSystemRadioButton.click();
            PageBase.OrderSummaryPage().activationCompleteButton.click();
            Utilities.waitForElementVisible(PageBase.OrderSummaryPage().moveSubqueuesButton);

            //Switch Back To Retail
            Utilities.switchPreviousTab();

            //Support Center Page
            PageBase.CommonControls().continueButtonDeposit.click();

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), simNumber);
        }
        return orderId;
    }

    private void carrierResponderSettingsQA3065(String phoneNumber) throws AWTException, InterruptedException {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "family_2_lines_eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace("5869333084", phoneNumber);
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Robot robot = new Robot();
        Utilities.copyPaste(xmlContent1, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_upgrade_individual_single_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "upgrade_success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loanleaseEligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "createloancontract", "success_lease_request.xml");
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
        cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit));
        cccDetails.setIdTypeState(customerDetails.IDState);
        cccDetails.setIdNumber(customerDetails.IDNumber);
        cccDetails.setMonth(customerDetails.IDExpirationMonth);
        cccDetails.setYear(customerDetails.IDExpirationYear);
        cccDetails.setIDType(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        return cccDetails;
    }

    //region QA 4255 private methods
    private String poaCompleteFlow_QA_4255(String testType, AccountDetails accountDetails, IMEIDetails imeiDetails, String TC)
            throws IOException, InterruptedException, AWTException {
        String orderId = "";//Login to retail page

        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on Sales & Activations page.
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        // Click on New Activation link.
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
        PageBase.ChoosePathPage().existingCarrier.click();

        // Pick Your path and continue - Selecting Add a line to existing Account
        Utilities.waitForElementVisible(PageBase.PickYourPathPage().AALExistingAccount);
        Utilities.ClickElement(PageBase.PickYourPathPage().AALExistingAccount);
        Reporter.log("<br> Adding a line to existing account");
        PageBase.CommonControls().continueButtonDVA.click();

        // Upgrade-Eligibility-Checker Page - Populating the User Details

        PageBase.UECVerificationPage().fillSprintDetails(accountDetails.MTN, accountDetails.SSN, accountDetails.Password);
        PageBase.UECVerificationPage().continueSprintButton.click();

        // Add-Line-type-Selection Page - Selecting Add a line to Existing Family Plan
        PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
        PageBase.CommonControls().continueButtonDVA.click();

        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails.IMEI);

        Assert.assertTrue(driver.getCurrentUrl().contains("financingOptIn"));

        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        PageBase.SprintEasyPayPage().yesButton.click();
        Reporter.log("<br> Opting for Easy Pay");

        //Filling information in Carrier Credit Check Page.
        String ccDataSSN = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ccDataSSN);
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        Reporter.log("<br> Entered Details in the pre-credit check page");

        String url = null;
        boolean flag = false;
        do {
            url = driver.getCurrentUrl();
            if (url.contains("creditcheck/result.htm") || url.contains("orderassembly/installmentdetail.htm"))
                flag = true;
            Utilities.implicitWaitSleep(1000);
        } while (!flag);
        try {
            if (driver.findElement(By.id("checkbox-sl-1")).isDisplayed()) {
                driver.findElement(By.id("checkbox-sl-1")).click();
                driver.findElement(By.id("continue")).click();
            }
        } catch (Exception e) {

        }

        try {
            PageBase.SprintEasyPayEligibilityResultPage().sprintEasyPayInstallmentDetails.click();
        } catch (Exception e) {
        }
        Assert.assertTrue(driver.findElement(By.xpath(".//div[contains(text(),'Down payment:')]")).isDisplayed());
        String defaultDownPayment = driver.findElement(By.xpath(".//fieldset[@id='fpRadioButtons']/div/div/label/span/span")).getText().substring(43, 47);
        Double downpaymentToBeUpdated = Double.valueOf(defaultDownPayment);
        downpaymentToBeUpdated = downpaymentToBeUpdated + 1;
        driver.findElement(By.id("custDownpayment")).click();
        driver.findElement(By.id("custDownpayment")).sendKeys(String.valueOf(downpaymentToBeUpdated));
        driver.findElement(By.id("checkInstallment")).click();
        Thread.sleep(10000);

        driver.findElement(By.xpath(".//span[contains(text(), 'Alternate down payment is ')]")).click();
        Utilities.implicitWaitSleep(1000);
        Reporter.log("<br>Opting for alternate downPayment. Custom Downpayment Choosen: " + downpaymentToBeUpdated);
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        PageBase.CommonControls().continueCommonButton.click();

        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        PageBase.CommonControls().continueCommonButton.click();

        Utilities.waitForElementVisible(driver.findElement(By.id("featureSubmit")));
        driver.findElement(By.id("featureSubmit")).click();


        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);

        try {
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();
        } catch (Exception e) {
        }

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Reporter.log("<br><p> Order Review and Confirm Page.</p>");
        Assert.assertTrue(driver.findElements(By.xpath("//strong[contains(text(),'Sprint Easy Pay')]")).size() > 0);

        String downPaymentAmt = driver.findElement(By.xpath("//td[contains(text(),'Down Payment:Sprint Easy Pay')]/following-sibling::td[2]")).getText();
        String downPaymentOnInstallmentPage = String.valueOf(downpaymentToBeUpdated.toString());
        downPaymentOnInstallmentPage = "$" + downPaymentOnInstallmentPage + "0";
        System.out.println(downPaymentOnInstallmentPage);
        Assert.assertEquals(downPaymentOnInstallmentPage, downPaymentAmt);
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(),'One Time Activation Fee:')]/following-sibling::span[1]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//td[contains(text(),'Estimated Tax:')]/following-sibling::td[2]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Total Due Today:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Estimated Total Recurring Fee:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Monthly Installment:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());

        String devicePrice = driver.findElement(By.xpath("//span[contains(text(),'Full Retail Price :')]/following-sibling::strong[1]")).getText();

        Reporter.log("<br> Device Price in Order Review and Confirmation Page Matches with Cart Page.");
        PageBase.CommonControls().continueCommonButton.click();

        Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckboxForSprint, 120);
        PageBase.WirelessCustomerAgreementPage().acceptsWCACheckboxForSprint.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.TermsAndConditionsPage().continueTCButton.click();
        do {
        } while (driver.getCurrentUrl().toLowerCase().contains("runcredit"));


        if (driver.getCurrentUrl().contains("payment")) {
            Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().creditCardNumberTextbox);
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

        if (driver.getCurrentUrl().contains("rtcc/creditresult.htm?ccmsg=2CC")) {
            Utilities.waitForElementVisible(driver.findElement(By.id("checkbox-mini-a")));
            driver.findElement(By.id("checkbox-mini-a")).click();
            PageBase.AccountPasswordPage().continueButton.click();
            ;
        }

        Utilities.implicitWaitSleep(5000);

        if (driver.getCurrentUrl().contains("support.htm")) {
            WebElement e = driver.findElement(By.xpath("//h2[contains(text(),'Support Center')]"));
            Reporter.log("<br> In Support Center as expected:" + e.getText());
            Utilities.WaitUntilElementIsClickable(PageBase.CommonControls().continueButtonDeposit);
            PageBase.CommonControls().continueButtonDeposit.click();
        }

        //Print Mobile Scan Sheet.
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        if (TC.contains("4255")) {
            if (readConfig("Activation").equals("true")) {
                PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().
                        GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
                PageBase.PaymentVerificationPage().submitButton.click();

                Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
                PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imeiDetails.IMEI, imeiDetails.Sim);
                PageBase.CommonControls().continueButtonDVA.click();

                //Device Financing Installment Contract.
                Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
                Utilities.WaitUntilElementIsClickable(PageBase.DeviceFinancingInstallmentContractPage().print);
                PageBase.DeviceFinancingInstallmentContractPage().print.click();
                Utilities.implicitWaitSleep(3000);
                Robot robot = new Robot();
                Utilities.sendKeys(KeyEvent.VK_ESCAPE, robot);
                Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
                Utilities.WaitUntilElementIsClickable(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
                Utilities.WaitUntilElementIsClickable(PageBase.DeviceFinancingInstallmentContractPage().print);
                PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox.click();
                PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
                PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();
                Utilities.implicitWaitSleep(4000);
                do {

                } while (driver.getCurrentUrl().contains("activation"));

                Utilities.implicitWaitSleep(1000);

                if (driver.getCurrentUrl().contains("support.htm")) {
                    WebElement e = driver.findElement(By.xpath("//h2[contains(text(),'Support Center')]"));
                    Reporter.log("<br> In Support Center as expected:" + e.getText());
                    Utilities.WaitUntilElementIsClickable(PageBase.CommonControls().continueButtonDeposit);
                    PageBase.CommonControls().continueButtonDeposit.click();
                }

                Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
                PageBase.OrderReceiptPage().verifyOrderCompletionPage();
                Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            }
        } else {
            if (readConfig("Activation").equals("true")) {
                PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().
                        GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
                PageBase.PaymentVerificationPage().submitButton.click();

                Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
                PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device
                        (imeiDetails.IMEI, "11111111111111111111");
                PageBase.CommonControls().continueButtonDVA.click();
                //Device Financing Installment Contract.
                Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
                Utilities.WaitUntilElementIsClickable(PageBase.DeviceFinancingInstallmentContractPage().print);
                PageBase.DeviceFinancingInstallmentContractPage().print.click();
                Utilities.implicitWaitSleep(3000);
                Robot robot = new Robot();
                Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
                Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
                Utilities.WaitUntilElementIsClickable(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
                Utilities.WaitUntilElementIsClickable(PageBase.DeviceFinancingInstallmentContractPage().print);
                PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox.click();
                PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
                PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();
                Utilities.implicitWaitSleep(4000);
                do {

                } while (driver.getCurrentUrl().contains("activation"));

                if (driver.getCurrentUrl().contains("support.htm")) {
                    WebElement e = driver.findElement(By.xpath("//h2[contains(text(),'Support Center')]"));
                    Reporter.log("<br> In Support Center as expected:" + e.getText());
                }
            }
        }
        if (readConfig("Activation").equals("true")) {
            //Inventory Management
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            if (TC.equals("4255"))
                PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails.IMEI, Constants.SOLD);
        }
        return orderId;
    }

    public static void SetXMLWithChanges(String apiName, String template, String valueToBeChanged, String MTN)
            throws AWTException, InterruptedException {

        //region retrieveCustomerDetails API Settings
        PageBase.CarrierResponseXMLPage().selectOptions("current", apiName, template);
        Utilities.WaitUntilElementIsClickable(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(2000);
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace(valueToBeChanged, MTN);
        Utilities.WaitUntilElementIsClickable(PageBase.CarrierResponseXMLPage().xmlTextArea);
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.implicitWaitSleep(2000);
        Robot robot = new Robot();
        Utilities.copyPaste(xmlContent1, robot);
        Utilities.WaitUntilElementIsClickable(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
    }

    public static void SetXML(String apiName, String template) {
        PageBase.CarrierResponseXMLPage().selectOptions("current", apiName, template);
        Utilities.implicitWaitSleep(2000);
        Utilities.WaitUntilElementIsClickable(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
    }

    private void shipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        Reporter.log("<h3> Event Logger Verification: </h3>");
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Reporter.log("<br>Status on Shipadmin: " + status);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }
    //endregion QA 4255 private methods
    //endregion Helper and Refactored Methods
}