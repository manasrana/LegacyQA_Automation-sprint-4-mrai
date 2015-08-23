package verizon.tests;

import com.gargoylesoftware.htmlunit.Page;
import com.sun.xml.internal.ws.policy.AssertionSet;
import org.openqa.selenium.Keys;
import pages.CarrierCreditCheckDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import framework.*;
import framework.NumPortingDetails;
import framework.CSVOperations.FileName;
import framework.InventoryManagementBaseClass.IMEIStatus;
import framework.Utilities.SelectDropMethod;
import pages.PaymentRequiredPage;
import pages.PaymentRequiredPage.CardType;
import pages.ServiceProviderVerificationPage.IdType;
import pages.ReturnProcessPage;
import pages.ServiceProviderVerificationPage.Month;

import static framework.PageBase.OrderActivationCompletePage;

public class AddNewLineTests extends RetailBaseClass {
    //region Variable Declaration
    public String carrierType = "Verizon";  //ToDo: Need to read from data sheet.
    public String cartDevice1price = "";
    public String cartDevice2price = "";
    public String simType1 = "";
    String receiptId = "";
    IMEIDetails imeiDetails1 = null;
    IMEIDetails imeiDetails2 = null;
    //endregion

    //region Test Methods
    //region QA 78
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_78_NonedgeNewActivationMultiplelinesExchangetoEdge(@Optional String testtype) throws InterruptedException, AWTException, IOException {
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        String order = VerizonActivateVerizonNonedgeMultipleLines(customerDetails);
        //PageBase.LoginPageRetail().LaunchNewPOATab();
        PageBase.HomePageRetail().newGuestButton.click();
        System.out.println("Clicked once");

        VerizonExchangeDevice(order, customerDetails);
        //-------------Upgrade Flow-----
        Utilities.waitForElementVisible(PageBase.HomePageRetail().newGuestButton);
        PageBase.HomePageRetail().newGuestButton.click();
        System.out.println("After Exchange");

        String orderId = VerizonExchangetoNonEdgeUpgradeFlow(customerDetails);

        QA_78ShipadminVerification(orderId);
    }
//endregion QA 78

    //region QA-5437
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_5437_VerizonNonEdgeAAL(@Optional String testtype) throws InterruptedException, AWTException, IOException {
        Log.startTestCase("QA_5437_VerizonNonEdgeAAL");
        Reporter.log("<h4>Description:</h4> Verizon Non Edge - Adding A Line .");

        //This TC requires 1 fresh IMEI for every run.
        IMEIDetails imeiDetails = null;

        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);
        CustomerDetails custDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        Log.startTestCase("QA_5437_VerizonNonEdgeAAL");
        String iMEINumber = imeiDetails.IMEI;
        System.out.println(iMEINumber);
        String simNumber = imeiDetails.Sim;
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(FileName.VerizonEdgeUPPaymentNotRequired);
        String phoneNumber = accountDetails.MTN;
        String sSN = accountDetails.SSN;
        String zipCode = custDetails.Zip;
        String accountPassword = accountDetails.Password;
        String orderId = "";

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails.IMEI + "(ProdCode: " + imeiDetails.ProductName + ")");

        //Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        testtype = BrowserSettings.readConfig("test-type");

        if (testtype.equals("internal")) {
            //Customizing xml files in Carrier Responder
            carrierResponderSettingsQA5437();
        } else {
            selectingVerizonExternalEnvironment();
        }

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");

        //Switching Back To Retail
        Utilities.switchPreviousTab();

        //POA FLOW
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaFlowQA5437(testtype, iMEINumber, simNumber, orderId, receiptId, phoneNumber, sSN, zipCode, accountPassword);
        Reporter.log("<h2> POA Flow Finishes</h2>");

        if (readConfig("Activation").toLowerCase().contains("true")) {

            //Ship Admin
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            shipAdminVerificationsQA5437(testtype, orderId);

            //Inventory Management
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);
            Reporter.log("<h3>IMEI Number =" + iMEINumber + "has been sold.<h3>");
        }

        //DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        Log.endTestCase("QA_5437_VerizonNonEdgeAAL");
    }
    //endregion QA-5437

    //region QA-5006
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_5006_Verizon_NewActivationForTablet2Years(@Optional String testtype) throws InterruptedException, AWTException, IOException {
        Log.startTestCase("QA_5006_Verizon_NewActivationForTablet2Years");
        Reporter.log("<h4>Description:</h4> Verizon New Activation For Tablet for 2 Years.");

        //This TC requires 1 fresh IMEI for every run.
        IMEIDetails imeiDetails = null;

        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(FileName.VerizonIPadAir16GB);
        CustomerDetails custDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        Log.startTestCase("QA_5006_Verizon_NewActivationForTablet2Years");
        String iMEINumber = imeiDetails.IMEI;
        System.out.println(iMEINumber);
        String simNumber = imeiDetails.Sim;
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        String orderId = "";

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails.IMEI + "(ProdCode: " + imeiDetails.ProductName + ")");

        //Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        testtype = BrowserSettings.readConfig("test-type");

        if (testtype.equals("internal")) {
            //Customizing xml files in Carrier Responder
            carrierResponderSettingsQA5006();
        } else {
            selectingVerizonExternalEnvironment();
        }

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");

        //Switching Back To Retail
        Utilities.switchPreviousTab();
        orderId = poaFlowQA5006(iMEINumber, simNumber, receiptId);


        if (readConfig("Activation").toLowerCase().contains("true")) {

            //Ship Admin
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            ShipAdminBaseClass.launchShipAdminInNewTab();
            shipAdminVerificationsQA5006(orderId);

            //Inventory Management
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);
            Reporter.log("<h3>IMEI Number =" + iMEINumber + "has been sold.<h3>");
        }

        //DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        Log.endTestCase("QA_5006_Verizon_NewActivationForTablet2Years");
    }
    //endregion QA-5006

    //region QA-71
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_71_VerizonEdgeTryToSwitchIndividualToFamilyAndAAL(@Optional String testtype) {
        try {
            Log.startTestCase("QA_71_VerizonEdgeTryToSwitchIndividualToFamilyAndAAL");
            Reporter.log("<h4>Description:</h4> Verizon Edge Try To Switch To Family Account From Individual Account And Adding A Line.");

            //Verify whether which enviorement to use internal or external.
            Reporter.log("<br> Test Type Settings");
            testtype = BrowserSettings.readConfig("test-type");

            if (testtype.contains("internal")) {
                //Customizing xml files in Carrier Responder
                carrierResponderSettingsQA71();
            } else {
                selectingVerizonExternalEnvironment();
            }

            //Calling DBError utility to  find initial count or error in log files.
            DBError.navigateDBErrorPage();
            int initialCount = PageBase.AdminPage().totalErrorCount();
            Reporter.log("<h3> DB Errors Initial Check:");
            Reporter.log(String.valueOf(initialCount) + "</h3>");

            //Switching Back To Retail
            Utilities.switchPreviousTab();

            //POA FLOW
            Reporter.log("<h2> POA Flow Starts </h2>");
            poaFlowQA71();
            Reporter.log("<h2> POA Flow Finishes</h2>");

            //DBError Verification.
            Reporter.log("<br> DB Errors Verification ");
            DBError.navigateDBErrorPage();
            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Log.endTestCase("QA_71_VerizonEdgeTryToSwitchIndividualToFamilyAndAAL");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA_71_VerizonEdgeTryToSwitchIndividualToFamilyAndAAL");
            Assert.assertTrue(false);
        } finally {

        }
    }
    //endregion QA-71

    //region QA-50
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA50_VerizonNonEdgeWithNumberPortCCInCA(@Optional String testtype)
            throws IOException, InterruptedException, AWTException {
        try {
            IMEIDetails imeiDetails1 = null;
            IMEIDetails imeiDetails2 = null;
            IMEIDetails imeiDetails3 = null;

            CreditCardDetails creditCard = new CreditCardDetails();
            creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
            String orderId = "";

            imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBBlack);
            imeiDetails2 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBBlack);
            imeiDetails3 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBBlack);
            receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);

            Reporter.log("<h2>Start - QA50_VerizonNonEdgeWithNumberPortCCInCA. <br></h2>");
            Reporter.log("<h3>Description: Verizon-NonEdge-with Number Port</h3>");
            Reporter.log("Launching Browser <br>", true);
            Log.startTestCase("QA50_VerizonNonEdgeWithNumberPortCCInCA");

            // Adding Devices to Inventory.
            PageBase.InventoryManagementPage().launchInventoryInNewTab(readConfig("inventoryStoreId2766"));
            PageBase.InventoryManagementPage().add3DevicesToInventory(imeiDetails1.IMEI, imeiDetails2.IMEI, imeiDetails3.IMEI,
                    imeiDetails1.ProductName);
            PageBase.InventoryManagementPage().closeInventoryTab();

            Reporter.log("<br> IMEIs Added to Inventory: " + imeiDetails1.IMEI + imeiDetails2.IMEI + imeiDetails3.IMEI +
                    "(ProdCode: " + imeiDetails1.ProductName + ")");

            Utilities.switchPreviousTab();

            // Verify whether which enviorement to use internal or external.
            Reporter.log("<br> Test Type Settings");
            selectCarrierResponderQA50(testtype);

            //region Calling DBError utility to  find initial count or error in log files.
            Reporter.log("<br> DB Errors Initial Check:");
            DBError.navigateDBErrorPage();
            int initialCount = PageBase.AdminPage().totalErrorCount();
            Reporter.log("<h3> DB Errors Initial Check:");
            Reporter.log(String.valueOf(initialCount) + "</h3>");
            // Switching to previous tab.
            Utilities.switchPreviousTab();
            //endregion

            //region POA flow
            Reporter.log("<h2> POA Flow Starts </h2>");
            orderId = poaCompleteFlowQA50(orderId, imeiDetails1.IMEI, imeiDetails2.IMEI, imeiDetails3.IMEI,
                    imeiDetails1.Sim, imeiDetails2.Sim, imeiDetails3.Sim);
            Reporter.log("<h2> POA Flow Finishes</h2>");
            //endregion

            //Inventory Management and Ship admin Page verification.
            if (readConfig("Activation").toLowerCase().contains("true")) {
                // Inventory Management Page verification.
                Reporter.log("<h2> Inventory Management Page: IMEI Status  Check</h2>");
                PageBase.InventoryManagementPage().launchInventoryInNewTab(readConfig("inventoryStoreId2766"));
                Utilities.implicitWaitSleep(5000);
                PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, imeiDetails2.IMEI, imeiDetails3.IMEI,
                        IMEIStatus.Sold.toString());
                Reporter.log("<h3> Status of IMEI1: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() +
                        "Status of IMEI2: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() +
                        "Status of IMEI3: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() +
                        "</h3>");

                //Ship Admin Verification
                Reporter.log("<h2> ShipAdmin Verification:</h2>");
                shipadminVerificationQA50(orderId);
            }

            //DBError Verification.
            /*Reporter.log("<br> DB Errors Verification ");
            DBError.navigateDBErrorPage();*/
            //Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Reporter.log("<h3>QA50_VerizonNonEdgeWithNumberPortCCInCA - Test Case Completes<h3>");
            Log.endTestCase("QA50_VerizonNonEdgeWithNumberPortCCInCA");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            Utilities.driverTakesScreenshot("QA50_VerizonNonEdgeWithNumberPortCCInCA");
            Assert.fail(ex.getMessage());
        }
    }
//endregion QA 50

    //region QA 5574
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_5574_VerizonNonEdgeWithDeposit(@Optional String testType) throws Exception {
        String orderId = "";
        Log.startTestCase("QA_5574_VerizonNonEdgeWithDeposit");
        Reporter.log("<h2> QA_5574_VerizonNonEdgeWithDeposit</h2>");
        Reporter.log("<h4>Description:</h4> New Activation deposit ,no insurance and no number port with 2 Yr Contract");

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);
        imeiDetails2 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.Verizon_MIFI_devices);

        // Adding  Devices in to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreId2766"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails2.IMEI, imeiDetails1.ProductName, imeiDetails2.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");
        Reporter.log("<br> MIFI IMEI Added to Inventory: " + imeiDetails2.IMEI + "(ProdCode: " + imeiDetails2.ProductName + ")");

        Log.startTestCase("QA_5574_VerizonNonEdgeWithDeposit");
        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        Utilities.implicitWaitSleep(2000);
        selectingCarrierEnviornment(testType);

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        //Calling DBError utility to  find initial count or error in log files.
        Reporter.log("<br> DB Errors Initial Check:");
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        // POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_5574(testType, imeiDetails1, imeiDetails2);
        Reporter.log("<h2> POA Flow Finishes</h2>");

        //Inventory Management Page verification.
        if (readConfig("Activation").contains("true")) {
            //Ship Admin Verification -orderId= ""
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            shipAdminVerification(orderId);
            Reporter.log("<h3> Order Shipped successfully <h3>");

            // Inventory mangement verification.
            inventoryManagementVerification();
            Reporter.log("<h3> Device Sold status in POS <h3>");
        }

        //DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        // API Verification.
        Reporter.log("<br> API verification.");
        VerifyAPIResponseForQA_5574(orderId);

        Reporter.log("<h3>DBError validated");

        Reporter.log("<h3>QA_5574_VerizonNonEdgeWithDeposit - Test Case Completes<h3>");
        Log.endTestCase("QA_5574_VerizonNonEdgeWithDeposit");
    }
    //endregion QA 5574

    //region QA 56
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_56_VerizonESecuritelDisabled(@Optional String testType) throws InterruptedException, AWTException, IOException {
        IMEIDetails imeiDetails1 = null;
        String carrierType = "Verizon";

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);

//        // Adding Devices to Inventory.
//        PageBase.InventoryManagementPage().launchInventoryInNewTab(Utilities.getCredentials("storeId0263"));
//        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);
//
//        PageBase.InventoryManagementPage().closeInventoryTab();
//        Utilities.switchPreviousTab();
//
//        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI);

        Reporter.log("<h2>Start - QA_56_VerizonESecuritelDisabled. <br></h2>");
        Reporter.log("<h3>New Activation without esecuretel</h3>");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("QA_56_VerizonESecuritelDisabled");

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        Log.startTestCase("QA_56_VerizonESecuritelDisabled");
        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        Utilities.implicitWaitSleep(2000);
        //selectingCarrierEnviornment(testType);

        //Utilities.switchPreviousTab();

        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        String orderId = QA_56_POAFlow(imeiDetails1);
        QA_56_VerifyShipadminDetails(orderId);

//        DBError.navigateDBErrorPage();
//        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Log.endTestCase("QA_56_VerizonNonEdgeExchange");
        Reporter.log("<h3>QA_56_VerizonNonEdgeExchange - Test Case Completes<h3>");
    }
    //endregion

    //region QA 57
    @Test(groups = {"verizon"})
    public void QA_57_VerizonNonEdgeSkipCreditCheck() throws IOException, AWTException, InterruptedException {
        String testType = BrowserSettings.readConfig("test-type");

        String orderId = "";

        Reporter.log("<h1>Start - QA_57_VerizonNonEdgeSkipCreditCheck. <br></h1>");
        Reporter.log("<h3>Description: </h3> Verizon non-edge new activation , Run credit check in RTCC, CC result - "
                + "manual review and goes to support center. Roc team updates CC result - "
                + "approved with deposit manually. Order becomes non-xml");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("57_VerizonNonEdgeExchange_LikeForUnlike");


        EnviornmentSettings_57(testType);

        Utilities.switchPreviousTab();

        IMEIDetails imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);
        // Adding Devices to Inventory.
        Reporter.log("<br> Receiving Inventory", true);
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        PoaCompleteFlow_57(imeiDetails1);
    }

    //endregion QA 57

    //region QA 5571
    @Test(groups = {"verizon"})
    public void QA_5571_VerizonNonEdgeWithDeposit() throws InterruptedException, AWTException, IOException {

        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        String orderId = QA_5571_PoaFlow();

        QA_79ShipAdminVerification(orderId);

        //DBError Verification.
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        Reporter.log("<h3>QA_5571_VerizonNonEdgeWithDeposit - Test Case Completes<h3>");
        Log.endTestCase("QA_5571_VerizonNonEdgeWithDeposit");

    }
    //endregion QA 5571

    // region QA_67_Verizon Edge With Number Portability
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_67_VerizonEdgeWithNumberPorting(@Optional String testType)
            throws InterruptedException, AWTException, IOException {
        String orderId = "";
        Log.startTestCase("QA_67_VerizonEdgeWithNumberPorting");
        Reporter.log("<h2> QA_67_VerizonEdgeWithNumberPorting</h2>");
        Reporter.log("<h4>Description:</h4> QA_67_VerizonEdgeWithNumberPorting");

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);

        //Adding Devices in to Inventory.
//        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
//        PageBase.InventoryManagementPage().addSingleDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);
//
//        PageBase.InventoryManagementPage().closeInventoryTab();
//        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        // Printing the begin of testcase
        // Log.startTestCase("QA_67_VerizonEdgeWithNumberPorting");
        // Verify whether which environment to use internal or external.
        testType = BrowserSettings.readConfig("test-type");
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder .
            Reporter.log("<br> Test Type Settings");
            setUpCarrierResponderForQA_67();
        } else {
            //Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType,
                    "External");
        }
        // Switching to Retail tab.
        //Utilities.switchPreviousTab();

        //Calling DBError utility to find initial count or error in log files.
//            Reporter.log("<br> DB Errors Initial Check:");
//            DBError.navigateDBErrorPage();
//            int initialCount = PageBase.AdminPage().totalErrorCount();
//            Reporter.log("<h3> DB Errors Initial Check:");

        // Switching to Retail tab.
        // Utilities.switchPreviousTab();

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_QA_67(orderId, imeiDetails1);
        Reporter.log("<h2> POA Flow Finishes</h2>");

        //Inventory Management and Ship admin Page verification.
        if (readConfig("Activation").contains("true")) {
            // Inventory Management Page verification.
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, IMEIStatus.Sold.toString());
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

            //Ship Admin Verification -orderId= "";
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            shipadminVerificationQA50(orderId);
        }

        //DBError Verification.
        DBError.navigateDBErrorPage();
        // Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_67_VerizonEdgeWithNumberPorting - Test Case Completes<h3>");
        Log.endTestCase("QA_67_VerizonEdgeWithNumberPorting");
    }
//endregion QA_67_Verizon Edge With Number Portability

    //region QA 75
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_75_VerizonEdgeAttemptToEdgeWithDeposit(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        Log.startTestCase("QA_75_VerizonEdgeAttemptToEdgeWithDeposit");
        Reporter.log("<h2> QA_75_VerizonEdgeAttemptToEdgeWithDeposit</h2>");
        Reporter.log("<h4>Description:</h4> QA_75_VerizonEdgeAttemptToEdgeWithDeposit");

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        Log.startTestCase("QA_75_VerizonEdgeAttemptToEdgeWithDeposit");
        testType = BrowserSettings.readConfig("test-type");


        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment_75(testType);

        //Calling DBError utility to  find initial count or error in log files.
//            Reporter.log("<br> DB Errors Initial Check:");
//            DBError.navigateDBErrorPage();
//            int initialCount = PageBase.AdminPage().totalErrorCount();
//            Reporter.log("<h3> DB Errors Initial Check:");
//            Reporter.log(String.valueOf(initialCount) + "</h3>");
//

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_75(testType);
        Reporter.log("<h2> POA Flow Finishes</h2>");

        //Inventory Management Page verification.
        if (readConfig("Activation").contains("true")) {
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            inventoryManagementVerification_75();
            InventoryManagementBaseClass.IMEIStatus.Sold.toString();
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

            //Ship Admin Verification -orderId= ""
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            shipAdminVerification(orderId);

            shipAdminVerification_75(orderId);
        }

        //DBError Verification.
//            DBError.navigateDBErrorPage();
//            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_75_VerizonEdgeAttemptToEdgeWithDeposit - Test Case Completes<h3>");
        Log.endTestCase("QA_75_VerizonEdgeAttemptToEdgeWithDeposit");
    }

    //endregion QA 75

    //region QA-2639
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_2639_VZWEdgeParkingFailDuringWCAGeneration(@Optional String testType) throws IOException, InterruptedException, AWTException {
        IMEIDetails imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);
        String imei_QA2639 = imeiDetails.IMEI;
        String invalid_sim = "1111111111111111111a";
        String productName = imeiDetails.ProductName;
        String orderId = null;
        String sTestCaseName = "QA_2639_VZWEdgeParkingFailDuringWCAGeneration";
        testType = BrowserSettings.readConfig("test-type");
        String internalTestType = BrowserSettings.readConfig("internalTestType");
        Reporter.log("<h2>" + sTestCaseName + "</h2>");
        Reporter.log("<h3>Description: </h3>" + " WCA VZW edge-Cancel order on WCA page");
        Log.startTestCase(sTestCaseName);

        //Adding Device to Inventory
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addSingleDeviceToInventory(imei_QA2639, productName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imei_QA2639 + "(ProdCode: " + productName + ")");
        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment_QA_2639(testType, internalTestType);
        // Switching to Retail tab.
        Utilities.switchPreviousTab();
        // Calling DBError utility to find initial error count in log
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");
        // Switching to previous tab.
        Utilities.switchPreviousTab();

        // QA_2639 complete POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = QA_2639_VZWEdgeParkingFailDuringWCAGeneration_PoaFlow(imei_QA2639, invalid_sim);
        System.out.print(orderId);
        Reporter.log("<h2> POA Flow Finishes</h2>");
        //ShipAdmin Verification
        QA_2639_shipAdminVerification(orderId);

        // DBError Verification for any new db errors
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        Reporter.log("<h3>" + sTestCaseName + " - Test Case Completes<h3>");
        // logging the end of test case
        Log.endTestCase(sTestCaseName);
    }

    //endregion QA-2639

    // region QA-5252
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_5252_VZWSwitchFromIndividualToFamilyAccountError(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String sTestCaseName = "QA_5252_VZWSwitchFromIndividualToFamilyAccountError";
        testType = BrowserSettings.readConfig("test-type");
        String internalTestType = BrowserSettings.readConfig("internalTestType");
        Reporter.log(sTestCaseName);
        Reporter.log("<h3>Description: </h3>" + " WCA VZW edge-Cancel order on WCA page");
        Log.startTestCase(sTestCaseName);
        // Configuring the API responses for current test case
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment_QA_5252(testType, internalTestType);
        // Switching to Retail tab.
        Utilities.switchPreviousTab();
        // Calling DBError utility to find initial error count in log
        Reporter.log("<br> DB Errors Initial Check:");
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");
        // Switching to previous tab.
        Utilities.switchPreviousTab();
        Reporter.log("<h2> POA Flow Starts </h2>");
        QA_5252_PoaFlow();
        Reporter.log("<h2> POA Flow Finishes</h2>");
        // DBError Verification for any new db errors
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        Reporter.log("<h3>" + sTestCaseName + " - Test Case Completes<h3>");
        // logging the end of test case
        Log.endTestCase(sTestCaseName);
    }
    // endregion QA-5252

    //region QA-1734
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_1734_CancelOrderOnWCAPage(@Optional String testType)
            throws IOException, AWTException, InterruptedException {
        String orderId = "";
        String sTestCaseName = "QA_1734_CancelOrderOnWCAPage";
        testType = BrowserSettings.readConfig("test-type");
        String internalTestType = BrowserSettings
                .readConfig("internalTestType");
        IMEIDetails imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);
        String imei_QA1734 = imeiDetails.IMEI;
        String sim = imeiDetails.Sim;
        String productName = imeiDetails.ProductName;
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imei_QA1734, productName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imei_QA1734 + "(ProdCode: " + productName + ")");
        Reporter.log(sTestCaseName);
        Reporter.log("<h3>Description: </h3>" + " WCA VZW edge-Cancel order on WCA page");
        Log.startTestCase(sTestCaseName);
        // Configuring the API responses for current test case
        selectingCarrierEnviornment_QA_1734(testType, internalTestType);
        // Switching to Retail tab.
        Utilities.switchPreviousTab();
        // Calling DBError utility to find initial error count in log
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        // Switching to previous tab.
        Utilities.switchPreviousTab();
        // QA_1734_CancelOrderOnWCAPage_PoaFlow Method has all the code for
        // complete POA flow
        orderId = QA_1734_CancelOrderOnWCAPage_PoaFlow(orderId, imei_QA1734, sim);
        // QA_1734_shipAdminVerification contains all shipadmin
        // verifications
        QA_1734_shipAdminVerification(orderId);
        // DBError Verification for any new db errors
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        Reporter.log("<h3>" + sTestCaseName + " - Test Case Completes<h3>");
        // logging the end of test case
        Log.endTestCase(sTestCaseName);

    }
    //endregion QA-1734

    //region QA 55
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_55_VerizonNonEdgeWithOutDeposit2LineActivationWithEsecuretel(@Optional String testType) throws Exception {
        String orderId = "";
        Log.startTestCase("QA_55_VerizonNonEdgeWithOutDeposit2LineActivationWithEsecuretel");
        Reporter.log("<h2> QA_55_VerizonNonEdgeWithOutDeposit2LineActivationWithEsecuretel</h2>");
        Reporter.log("<h4>Description:</h4> QA_55_VerizonNonEdgeWithOutDeposit2LineActivationWithEsecuretel");

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);
        imeiDetails2 = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);

        // Adding  Devices in to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails2.IMEI, imeiDetails1.ProductName, imeiDetails2.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");
        Reporter.log("<br> MIFI IMEI Added to Inventory: " + imeiDetails2.IMEI + "(ProdCode: " + imeiDetails2.ProductName + ")");

        Log.startTestCase("QA_55_VerizonNonEdgeWithOutDeposit2LineActivationWithEsecuretel");
        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        Utilities.implicitWaitSleep(2000);
        selectingCarrierEnviornment_55(testType);

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        // POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_55(testType, imeiDetails1, imeiDetails2);
        Reporter.log("<h2> POA Flow Finishes</h2>");

        //Inventory Management Page verification.
        if (readConfig("Activation").contains("true")) {
            //Ship Admin Verification -orderId= ""
            shipAdminVerification(orderId);
            Reporter.log("<h3> Order Shipped successfully <h3>");

            // Inventory mangement verification.
            inventoryManagementVerification();
            Reporter.log("<h3> Device Sold status in POS <h3>");
        }

        Reporter.log("<h3>QA_55_VerizonNonEdgeWithOutDeposit2LineActivationWithEsecuretel - Test Case Completes<h3>");
        Log.endTestCase("QA_55_VerizonNonEdgeWithOutDeposit2LineActivationWithEsecuretel");
    }
    //endregion QA 55

    //region QA_69_VerizonAddLineToFamilyPlan

    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_69_VerizonAddLineToFamilyPlan(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        CustomerDetails custDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        boolean activation = false;
        String sTestCaseName = "QA_69_VerizonAddLineToFamilyPlan";
        IMEIDetails imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBWhite);
        String imei = imeiDetails.IMEI;
        String sim = imeiDetails.Sim;
        String product = imeiDetails.ProductName;
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(FileName.VerizonEdgeUPPaymentNotRequired);
        String phoneNumber = accountDetails.MTN;
        String sSN = accountDetails.SSN;
        String zipCode = custDetails.Zip;
        String accountPassword = accountDetails.Password;
        String internalTestType = BrowserSettings.readConfig("internalTestType");
        testType = BrowserSettings.readConfig("test-type");
        Log.startTestCase(sTestCaseName);
        Reporter.log("<h2>Start - " + sTestCaseName + "<br></h2>");
        Reporter.log("Launching Browser <br>", true);
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imei, product);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imei + "(ProdCode: " + product + ")");
        selectingCarrierEnviornment_QA_69(testType, internalTestType);
        // Configuring the API responses for current test case
        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        // Switching to previous tab.
        Utilities.switchPreviousTab();
        // QA_69_PoaFlow Method has all the code for complete POA flow
        orderId = QA_69_PoaFlow(testType, phoneNumber, sSN, accountPassword, zipCode, imei, sim);
        //QA_69_shipAdminVerification contains all shipadmin verifications
        QA_69_shipAdminVerification(orderId);
        //logging the end of test case
        Log.endTestCase(sTestCaseName);
    }

    //endregion QA_69_VerizonAddLineToFamilyPlan

    //endregion Test Methods

    //region Private Methods and Refacotored Codes

    //region QA 5574  Refactored Methods
    private String poaCompleteFlow_5574(@Optional String testType, IMEIDetails imei1, IMEIDetails imei2) throws IOException {
        String orderId = "";//Login to retail page.
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        CreditCardDetails creditCard = new CreditCardDetails();
        creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
        lStartTime = new Date().getTime();
        pageName = readPageName("PoaLogin");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on Sales & Activations page.
        lStartTime = new Date().getTime();
        pageName = readPageName("SaleAndActivation");
        PageBase.HomePageRetail().salesAndActivationsLink.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on New Activation link.
        lStartTime = new Date().getTime();
        pageName = readPageName("DeviceScan");
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Scanning smart phone and MIFI phone.
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1.IMEI);
        PageBase.VerizonEdgePage().declineVerizonEdge();
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei2.IMEI);
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        Reporter.log("Device has been scanned with IMEI1" + imei1.IMEI + "IMEI2:" + imei2.IMEI);
        PageBase.CommonControls().continueButtonDVA.click();

        //Filling information in Carrier Credit Check Page.
        crditCheck(PageBase.CarrierCreditCheckPage().skip);

        Utilities.webPageLoadTime(lStartTime, pageName);

        // Credit Check Verification Results with deposits.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        Reporter.log("<br> Credit Check Completes.");
        PageBase.CreditCheckVerificationResultsPage().depositCheckBox.click();
        Reporter.log("<br> Selected Deposit Check Box ");

        WebElement depositTable = driver.findElement(By.id("table-cc"));
        String firstdeviceDeposit = driver.findElement(By.xpath("id('table-cc')/li[2]/span[2]")).getText();
        String seconddeviceDeposit = driver.findElement(By.xpath("id('table-cc')/li[3]/span[2]")).getText();

        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMorePlanOnly);
        PageBase.VerizonShopPlansPage().selectPlanWithMore();
        PageBase.VerizonShopPlansPage().addPlan();
        Reporter.log("<br> Added Plan to devices.");

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Select Plan - Storing the Device and plan prices for further verification.
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        cartDevice2price = PageBase.CartPage().device2Price.getText();
        String monthlyRecurringFee = driver.findElement(By.xpath("//td[contains(text(),'Monthly Recurring Fee:')]/parent::tr/td[2]")).getText();
        String totalDueToday = PageBase.CartPage().totalDueToday.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().selectFamilyBasePlan(0);
        PageBase.SelectPlanFeaturesPage().selectNetworkAccessPlan(1);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();
        Reporter.log("<br> Selected Plan and feature");

        // Selecting No Insurance .
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForTwoDevices();
            Reporter.log("<br> Selected No Insurance for all devices");
        } catch (Exception ex) {
        }

        // Redirecting to Credit check page.
        try {
            //crditCheck(PageBase.CarrierCreditCheckPage().skip);
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.CommonControls().continueButton.click();
        } catch (Exception ex) {
        }

        // Selecting No Number Porting.
        try {
            Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
            PageBase.NumberPortPage().noNumberPortRadiobutton.click();
            PageBase.CommonControls().continueButton.click();
            Reporter.log("<br> Selected No number porting");
        } catch (Exception ex) {
            Reporter.log("<br> No porting page is missing");
        }

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

//        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), cartDevice1price);
//        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device2PriceActual.getText(), cartDevice2price);
//        Assert.assertTrue(driver.findElements(By.xpath("//strong[contains(text(),'2 Year Contract')]")).size() > 0);
//
//        double device1Price = Double.parseDouble(cartDevice1price.substring(1));
//        double device2Price = Double.parseDouble(cartDevice2price.substring(1));
//
//        String totaldueToday = driver.findElement(By.xpath("(//h3[contains(text(),'Total Due Today:')]/parent::th/following-sibling::th)[2]/child::h3")).getText();
//        totaldueToday = totalDueToday.replace("inc. tax", "");
//
//        String device1Activation = driver.findElement(By.xpath("((//span[contains(text(),'One Time Activation Fee:')])[1]/following-sibling::span)[1]")).getText();
//        Assert.assertEquals("$40.00", device1Activation);
//
//        String device2Activation = driver.findElement(By.xpath("((//span[contains(text(),'One Time Activation Fee:')])[2]/following-sibling::span)[1]")).getText();
//        Assert.assertEquals("$40.00", device2Activation);
//
//        String mifiDeviceDeposit = driver.findElement(By.xpath("//td[contains(text(),'$20')]")).getText();
//        Assert.assertEquals("$20.00", mifiDeviceDeposit);
//        Reporter.log("<br> MIFI Deposit Fee: " + mifiDeviceDeposit);
//
//        String estimatedTotalDueMonthly = driver.findElement(By.xpath("(//h3[contains(text(),'Estimated Total Due Monthly:')]/parent::th/following-sibling::th)[2]/child::h3")).getText();
//        estimatedTotalDueMonthly = estimatedTotalDueMonthly.replace("+ tax", "");
//
//        String line1Fee = driver.findElement(By.xpath("(//span[contains(text(),'2 Year Contract')])[1]/parent::li/parent::div/parent::div/parent::td//following-sibling::td[1]")).getText();
//        String line2Fee = driver.findElement(By.xpath("(//span[contains(text(),'One Time Activation Fee')])[2]/parent::li/parent::div/parent::div/parent::td//following-sibling::td[1]")).getText();
//        String familyBase = driver.findElement(By.xpath("((//span[contains(text(),'FamilyBase')])[1]/following-sibling::span)[1]")).getText();
//
//        double line1Fees = Double.parseDouble(line1Fee.substring(1));
//        double line2Fees = Double.parseDouble(line2Fee.substring(1));
//        double familyBases = Double.parseDouble(familyBase.substring(1, 3));
//
//
//        Assert.assertEquals(line1Fees + line2Fees + familyBases, Double.parseDouble(estimatedTotalDueMonthly.substring(1).replace(" ", "")));
//        Assert.assertEquals(device1Price + device2Price, Double.parseDouble(totaldueToday.replace("$", "").replace(",", "")));
//
//        Reporter.log("<br> Line1 Fee: " + line1Fee);
//        Reporter.log("<br> Estimated Total due" +
//                " Monthly Fee: " + estimatedTotalDueMonthly);
//        Reporter.log("<br> Total due" +
//                " Total Device Fee: " + totaldueToday);
//        Reporter.log("<br> Device Price in Order Review and Confirmation Page Matches with Cart Page.");

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().continueTCButton);
            PageBase.TermsAndConditionsPage().emailTCChkBox.click();
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            Assert.assertTrue(driver.findElements(By.xpath("//h1[contains(text(),'Target Terms')]")).size() > 0);
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            // Credit Check Verification Results with deposits.
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
            boolean exist = driver.findElements(By.id("checkbox-mini-1")).size() != 0;
            if (exist) {
                PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
                PageBase.CommonControls().continueButtonDeposit.click();
            }

            // Credit card payment  page is coming.
            Utilities.implicitWaitSleep(5000);
            if (driver.getCurrentUrl().contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
            }

            // Print Mobile Scan Sheet.
            try {
                Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
                orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
                PageBase.PrintMobileScanSheetPage().verifyAllTwoDeviceBarCode();

                WebElement web = driver.findElement(By.xpath("//span[contains(text(),'2766 - TARGET - SAN FRANCISCO CENTRAL')]"));

                String mssdevice1Price = driver.findElement(By.xpath("(//tr[@class = 'mssmarginb'])[1]/child::td[2]")).getText();
                Assert.assertEquals(cartDevice1price, mssdevice1Price);

                String mssdevice2Price = driver.findElement(By.xpath("(//tr[@class = 'mssmarginb'])[2]/child::td[2]")).getText();
                Assert.assertEquals(cartDevice2price, mssdevice2Price);
            } catch (Exception ex) {
            }

            //Ship Admin Order Status Verification
            ShipAdminBaseClass.launchShipAdminInNewTab();
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            Assert.assertEquals(PageBase.OrderSummaryPage().getOrderStatus(), Constants.IN_STORE_BILLING);

            //Switching Back to Retail
            Utilities.switchPreviousTab();
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            String recieptID = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(recieptID);
            PageBase.PaymentVerificationPage().submitButton.click();
            Reporter.log("<br> Receipt ID Used: " + recieptID);

            //Ship Admin Order Status Verification
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            PageBase.OrderSummaryPage().statusValueLink.sendKeys(Keys.F5);
            Utilities.waitForElementVisible(PageBase.OrderSummaryPage().activationScanRequiredStatusLink);
            Assert.assertEquals(PageBase.OrderSummaryPage().getOrderStatus(), Constants.ACTIVATION_SCAN_REQUIRED);

            //Switching Back to Retail
            Utilities.switchPreviousTab();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor3Devices(imei1.IMEI, imei2.IMEI, "", imei1.Sim, imei2.Sim, "");

            // WCA Signature page activity and verifications.
            Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);

            //Ship Admin Order Status Verification
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            PageBase.OrderSummaryPage().statusValueLink.sendKeys(Keys.F5);
            Utilities.waitForElementVisible(PageBase.OrderSummaryPage().wCASignatureRequiredStatusLink);
            Assert.assertEquals(PageBase.OrderSummaryPage().getOrderStatus(), Constants.WCA_SIGNATURE_REQUIRED);
            Utilities.implicitWaitSleep(2000);

            //Switching Back to Retail
            Utilities.switchPreviousTab();

            Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);

            String termasAndConditionsContent = PageBase.WirelessCustomerAgreementPage().termsAndConditionsDiv.getText();
            Assert.assertTrue(termasAndConditionsContent.contains("Application ID No.:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Order Date:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Bill Acct. No.:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Target 2766: 789 Mission St, San Francisco, CA 94103, U.S.A"));
            Assert.assertTrue(termasAndConditionsContent.contains("Activation Type:"));
            Assert.assertTrue(termasAndConditionsContent.contains("New"));
            Assert.assertTrue(termasAndConditionsContent.contains("Customer Information:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Home Phone:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Mobile Number:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Price Plan:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Plan Access Fee:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Line Access Fee:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Term:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Activation Date:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Activation Fee:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Upgrade Fee:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Security Deposit (per line):"));
            Assert.assertTrue(termasAndConditionsContent.contains("Customer Accepted:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Customer Agreement version:"));
            Assert.assertTrue(termasAndConditionsContent.contains(Constants.DISCLAIMER_TEXT));
            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei1.IMEI, FileName.SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei2.IMEI, FileName.Verizon_MIFI_devices);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(recieptID, FileName.ReceiptId);

            // Order Activation Complete page.
            Utilities.waitForElementVisible(OrderActivationCompletePage().ActivationComplete);
            Reporter.log("<br> Order Activation Complete Page. ");

//            String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
//            Assert.assertEquals(orderId, orderIdfromActPage.substring(1));
//            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
//            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
//            Reporter.log("<h3><U>Order ha been confirmed, Order Id =" + orderId + "</U></h3>");
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), imei1.IMEI);
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValue2Text.getText(), imei2.IMEI);
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), imei1.Sim);
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValue2Text.getText(), imei2.Sim);
//            // Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValueText.getText(), cartDevice1price);
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValue2Text.getText(), cartDevice2price);
//            Reporter.log("<br> Device Price matches with Cart Page and Order Review and Confirm Page: " + cartDevice1price);

            CSVOperations.WriteToCSV("QA_5574", orderId, imei1.IMEI, "", "", customerDetails.FirstName, customerDetails.LastName,
                    customerDetails.EMail, receiptId, customerDetails.IDType, customerDetails.State,
                    customerDetails.IDNumber, customerDetails.PhNum, customerDetails.Zip, "",
                    customerDetails.IDExpirationMonth, customerDetails.IDExpirationYear);
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void crditCheck(WebElement skip) throws IOException {
        Utilities.waitForElementVisible(skip);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);

        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();

        lStartTime = new Date().getTime();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
        }
    }

    private void shipAdminVerification_Status(String orderId, String stat) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, stat);
        Reporter.log("<h3>Device is in IN Store Billing Staus: <h3>");
    }

    private void inventoryManagementVerification() throws InterruptedException, AWTException, IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, imeiDetails2.IMEI, IMEIStatus.Sold.toString());
    }

    private void shipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
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
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
        Reporter.log("<h3>Device shipped <h3>");
    }

    private void selectingCarrierEnviornment(@Optional String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            //selectingBackendSimulatorForQA5574();

            //Selecting Carrier Responder
            selectCarrierResponderQA5574();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
        }
    }

    private CarrierCreditCheckDetails getCarrierCreditCheckDetails() throws IOException {
        CarrierCreditCheckDetails cccDetails = new CarrierCreditCheckDetails();
        PageBase.CSVOperations();
        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
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
        cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SSNWithDeposit));
        cccDetails.setIDType(IdType.DRIVERLICENCE);
        cccDetails.setIdTypeState(customerDetails.IDState);
        cccDetails.setIdNumber(customerDetails.IDNumber);
        cccDetails.setMonth(customerDetails.IDExpirationMonth);
        cccDetails.setYear(customerDetails.IDExpirationYear);
        return cccDetails;
    }

    private void selectCarrierResponderQA5574() throws AWTException, InterruptedException {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        // Selecting Verizon and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_no_edge.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace(Constants.APPROVE_WITH_DEPOSIT, Constants.APPROVE_WITH_DEPOSIT_VALUE);
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Robot robot = new Robot();
        Utilities.copyPaste(xmlContent1, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_vd_300mb_2_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved_with_deposit.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "default.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_2_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitpayment", "default.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private void selectingBackendSimulatorForQA5574() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        PageBase.AdminPage().selectCreaditReadUseCase("APPROVE_WITH_DEPOSIT");
        PageBase.AdminPage().selectCreaditWriteUseCase("APPROVE_WITH_DEPOSIT");
        PageBase.AdminPage().retrieveCustomerDetails("ELIGIBLE");

        List<WebElement> phoneList = PageBase.AdminPage().phoneList.findElements(By.className("phoneNumberRow"));

        if (phoneList.size() > 0) {
            List<WebElement> removePhone = PageBase.AdminPage().phoneList.findElements(By.id("retrieveCustomerDetails_removePhoneNumberRowButton"));
            for (int i = 0; i < removePhone.size(); i++) {
                removePhone.get(i).click();
            }
        }

        PageBase.AdminPage().accountPlanType("Family Share");
        PageBase.AdminPage().retrieveExistingCustomerInstallmentsDetails("SUCCESS_WITH_PAYMENT");
        PageBase.AdminPage().retrievePricePlan("SUCCESS");
        PageBase.AdminPage().submitActivation("SUCCESS");
        PageBase.AdminPage().submitReciept("SUCCESS");
        PageBase.AdminPage().submitServiceDetails("SUCCESS");
        PageBase.AdminPage().submitEdgeUpPayment("SUCCESS");
        PageBase.AdminPage().returnOrExchangeDevice("SUCCESS");

        PageBase.AdminPage().save();
    }

    private void VerifyAPIResponseForQA_5574(String orderId) throws Exception {
        String url = null;
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        driver.navigate().to(readConfig("apiHistory"));
        Utilities.WaitUntilElementIsClickable(PageBase.AdminPage().orderIdLink);
        PageBase.AdminPage().orderIdLink.sendKeys(orderId);
        Robot robot = new Robot();
        Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
        Utilities.waitForDocumentReady(driver);
        Utilities.ScrollToElement(PageBase.AdminPage().lastRetrievePricePlanAPI);
        url = PageBase.AdminPage().lastRetrievePricePlanAPI.getText();
        driver.findElement(By.xpath(ControlLocators.RETRIEVE_PRICE_PLAN + "/a")).click();
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
        Assert.assertTrue(validateContent.contains("<accessFee>"));
        Assert.assertTrue(validateContent.contains("<assignedMobileNumber>"));
        Assert.assertTrue(PageBase.AdminPage().submitActivationAPI.isDisplayed());
        Assert.assertTrue(PageBase.AdminPage().submitCreditApplicationAPI.isDisplayed());
        Assert.assertTrue(PageBase.AdminPage().submitActivationAPI.isDisplayed());
        Assert.assertTrue(PageBase.AdminPage().submitRecieptAPI.isDisplayed());
        Assert.assertTrue(PageBase.AdminPage().retrievePortraitRecieptAPI.isDisplayed());
        Assert.assertTrue(PageBase.AdminPage().submitActivationAPI.isDisplayed());
        Assert.assertTrue(PageBase.AdminPage().submitCreditApplicationAPI.isDisplayed());
        Assert.assertTrue(PageBase.AdminPage().retrieveNPANXXAPI.isDisplayed());

        Reporter.log("<br> <accessFee>");
        Reporter.log("<br> <assignedMobileNumber");
        Reporter.log("<br> Verified all API's");

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

    //endregion QA 5574 Refactored Methods

    //region QA 50 refactored methods
    private String poaCompleteFlowQA50(String orderId, String imei1, String imei2, String imei3,
                                       String sim1, String sim2, String sim3) throws IOException {
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();

        String cartDevice1price = "";
        String cartDevice2price = "";
        String cartDevice3price = "";

        //Login to retail page.
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

        // Click on Sales & Activations page.
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);

        // Click on New Activation link.
        PageBase.ChoosePathPage().newActivation.click();

        // Scanning 3 Verizon smart phones.
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().declineVerizonEdge);
        PageBase.VerizonEdgePage().declineVerizonEdge();
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei2);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei3);
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

        //Skip the Carrier Credit Check.
        Utilities.ClickElement(PageBase.CarrierCreditCheckPage().skip);

        // Selecting Plan.
        Utilities.ClickElement(PageBase.VerizonShopPlansPage().verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton);

        // Storing the Device and plan prices for further verification.
        Utilities.waitForElementVisible(PageBase.CartPage().device1Price);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        cartDevice2price = PageBase.CartPage().device2Price.getText();
        cartDevice3price = PageBase.CartPage().device3Price.getText();

        Utilities.ClickElement(PageBase.CartPage().continueCartButton);

        // Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        Utilities.ClickElement(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);

        // Selecting NO Insurance.
        try {
            Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().noInsuranceFirstMob);
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForThreeDevices();
        } catch (Exception ex) {
            Reporter.log("Insurance page not coming");
        }

        // Selecting Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().numberPortRadiobutton);
        Utilities.ClickElement(PageBase.NumberPortPage().numberPortRadiobutton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        PageBase.PortMyNumbersPage().enterPortData(portDetails.CurrentPhoneNumber, portDetails.Carrier,
                portDetails.CurrentAccNumber, portDetails.SSN, customerDetails.FirstName, customerDetails.LastName,
                customerDetails.Address1, "", customerDetails.City, customerDetails.State, customerDetails.Zip);

        // Enter data in Service Provider Verification page.
        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);
        PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, "", customerDetails.LastName,
                customerDetails.EMail, IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber,
                Month.valueOf(customerDetails.IDExpirationMonth.toUpperCase()), Integer.parseInt(customerDetails.IDExpirationYear),
                portDetails.CurrentPhoneNumber, customerDetails.BirthdayMonth, Integer.parseInt(customerDetails.BirthdayDay),
                Integer.parseInt(customerDetails.BirthdayYear));
        Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);
        try {
            Utilities.ClickElement(PageBase.CommonControls().continueButton);
        } catch (Exception ex) {
            Reporter.log("Continue button not needed");
        }

        // Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.OrderReviewAndConfirmPage().device1PriceActual);

        // Device prices in Cart Page and Order Review Confirm page are not matching, hence commenting out the Assertions.
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().OrderReviewConfirmPageAssertionsFor3Devices(cartDevice1price,
                cartDevice2price, cartDevice3price));
        PageBase.CommonControls().continueCommonButton.click();

        // Terms and Condition Page.
        PageBase.TermsAndConditionsPage().acceptTermsAndConditions();

        // Credit Card Payment Page
        Utilities.implicitWaitSleep(10000);
        if (driver.getCurrentUrl().contains("payment")) {
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

        // MSS page.
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText);
        orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        PageBase.SQLUtilAdminPage().launchSQLUtilInNewTab();
        serverDBVerificationsQA50();
        Utilities.switchPreviousTab();
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        Utilities.ClickElement(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);

        // Payment Verification page.
        PageBase.PaymentVerificationPage().paymentVerification(receiptId);

        // Device Verification and Activation page.
        PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor3Devices(imei1, imei2, imei3, sim1, sim2, sim3);

        // WCA Signature page.
        Utilities.implicitWaitSleep(6000);
        Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
        Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

        // Order Activation Complete page.
        Utilities.waitForElementVisible(OrderActivationCompletePage().ActivationComplete);
        Assert.assertTrue(OrderActivationCompletePage().ActivationComplete.isDisplayed());

        CSVOperations.WriteToCSV("QA_50", orderId, imei1, "", "", customerDetails.FirstName, customerDetails.LastName,
                customerDetails.EMail, receiptId, customerDetails.IDType, customerDetails.State,
                customerDetails.IDNumber, customerDetails.PhNum, customerDetails.Zip, portDetails.SSN,
                customerDetails.IDExpirationMonth, customerDetails.IDExpirationYear);
        PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei1, FileName.SamsungGalaxyS4_16GBBlack);
        PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei2, FileName.SamsungGalaxyS4_16GBBlack);
        PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei3, FileName.SamsungGalaxyS4_16GBBlack);
        return orderId;
    }

    private void shipadminVerificationQA50(String orderId) throws IOException {
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
        Assert.assertTrue(eventLogTableContent.contains(Constants.INQUIRING_NUMBER_PORT_ELIGIBILITY));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.NUMBER_PORTABILITY));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
    }

    private void selectCarrierResponderQA50(@Optional String testtype) throws IOException, InterruptedException, AWTException {
        // Verify whether which environment to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        if (testtype.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));

            //Selecting Use Case from dropdown list.
            PageBase.AdminPage().selectAPIConfig(carrierType);

            //Customizing xml files in Carrier Responder
            PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
            PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
            Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
            Utilities.implicitWaitSleep(4000);
            PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            Utilities.implicitWaitSleep(4000);

            PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_no_edge.xml");
            Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
            Utilities.implicitWaitSleep(4000);
            PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            Utilities.implicitWaitSleep(4000);

            PageBase.CarrierResponseXMLPage().selectOptions("current", "validatePortInEligibility", "default_1_line.xml");
            Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
            Utilities.implicitWaitSleep(4000);
            PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            Utilities.implicitWaitSleep(4000);

            PageBase.CarrierResponseXMLPage().selectOptions("current", "submitPortInDetails", "default.xml");
            Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
            Utilities.implicitWaitSleep(4000);
            PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            Utilities.implicitWaitSleep(4000);

            PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveNpaNxx", "single_area_code.xml");
            Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
            Utilities.implicitWaitSleep(4000);
            PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            Utilities.implicitWaitSleep(4000);

            PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "default.xml");
            Utilities.implicitWaitSleep(4000);
            Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
            PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            Utilities.implicitWaitSleep(4000);

            PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "alp_3_line.xml");
            Utilities.implicitWaitSleep(4000);
            Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
            PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            Utilities.implicitWaitSleep(4000);

            PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_3_line.xml");
            Utilities.implicitWaitSleep(4000);
            Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
            PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            Utilities.implicitWaitSleep(4000);

            PageBase.CarrierResponseXMLPage().selectOptions("current", "submitpayment", "default.xml");
            Utilities.implicitWaitSleep(4000);
            Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
            PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            Utilities.implicitWaitSleep(4000);

        } else {   //External// Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
        }
    }


    private void serverDBVerificationsQA50() {
        Utilities.implicitWaitSleep(5000);
        Utilities.dropdownSelect(PageBase.SQLUtilAdminPage().chooseQueryWrapperDropdown, SelectDropMethod.SELECTBYINDEX, "0");
        PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("select rsExternalStoreId,rsZip,taxzones.* from retailstores,zipcodes,taxzones where retailstores.rszip=zipcodes.zipcode and zipcodes.taxzoneId=taxzones.taxzoneId and taxzones.brandId=731 and retailstores.rsExternalStoreId =2766");
        PageBase.SQLUtilAdminPage().submitButton.click();
        Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().queryResult);
        Assert.assertTrue(PageBase.SQLUtilAdminPage().queryResult.isDisplayed());
    }

    //endregion QA 50
    public String VerizonActivateVerizonNonedgeMultipleLines(CustomerDetails customerDetails) throws IOException {

        //PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        String esnNo1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
        String esnNo2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
        String simNumber2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);

        //Click on the Sales and Activation Link
        Utilities.waitForElementVisible(PageBase.HomePageRetail().salesAndActivationsLink);
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Click on the new Activation Link
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().newActivation);
        PageBase.ChoosePathPage().newActivation.click();

        //Scan a Device and Click on 'No' Option
        PageBase.DeviceScanPage().enterDeviceScanDetails(esnNo1); //"6007003004006"
        //PageBase.VerizonEdgePage().declineVerizonEdge();
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
        PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

        //Scan Second Device
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().enterDeviceScanDetails(esnNo2);//"6007003004007"
        //PageBase.VerizonEdgePage().declineVerizonEdge();
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
        PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

        //Skip the Credit Check
        Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);
        Utilities.ClickElement(PageBase.CarrierCreditCheckPage().skip);

        //Choose a Plan
        Utilities.ClickElement(PageBase.VerizonShopPlansPage().VerizonMoreEverything);

        //Click on continue from Cart PAge
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        Utilities.ClickElement(PageBase.CartPage().continueCartButton);

        //Select 'Network Access' and 'Family Base' Features for Each Device
        //PageBase.SelectPlanFeaturesPage().selectFamilyBasePlan(0);
        List<WebElement> networkList = BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'Network Access')]/ancestor::a"));
        //Selecting Network Access plan.
        networkList.get(0).click();
        driver.findElement(By.xpath("(//span[contains(text(),'Network Access')])[2]")).click();
        //PageBase.SelectPlanFeaturesPage().selectNetworkAccessPlan(1);
        List<WebElement> netwkList = BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'FamilyBase')]/ancestor::a"));
        //Selecting Family Base plan.
        netwkList.get(1).click();
        driver.findElement(By.xpath("(//span[contains(text(),'FamilyBase')])[4]")).click();
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        //Do not select an insurance for any of the Device
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().guestReview);
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForTwoDevices();

        //Click on 'No number port' Option
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        // Fill SPV details
        //Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().populateForm);
        PageBase.ServiceProviderVerificationPage().populateFormByClassButton.click();
        //PageBase.ServiceProviderVerificationPage().populatingSPVPage
        //		("Fred", "", "Consumer Two", "nobody@letstalk.com", IdType.DRIVERLICENCE,
        //				"CA", "123456789", ServiceProviderVerificationPage.Month.DECEMBER,
        //				2020, "9882303487", "April", 11, 1970);

        PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, "", customerDetails.LastName, customerDetails.EMail, IdType.DRIVERLICENCE, customerDetails.City, customerDetails.IDNumber, Month.valueOf(customerDetails.IDExpirationMonth),
                Integer.parseInt(customerDetails.IDExpirationYear), customerDetails.PhNum, customerDetails.BirthdayMonth,
                Integer.parseInt(customerDetails.BirthdayDay), Integer.parseInt(customerDetails.BirthdayYear));

        Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        PageBase.CommonControls().continueCommonButton.click();

        //Accept the Terms and Conditions
        PageBase.TermsAndConditionsPage().acceptTermsAndConditions();

        Utilities.implicitWaitSleep(10000);
        if (driver.getCurrentUrl().contains("payment")) {

            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA); //ToDo: Need to read from data sheet.
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

        // MSS page.
        //ToDo: BAR Code verification after clarifying on the expected Bar code.
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        //Enter Receipt ID
        PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);//"132710003003669460"
        PageBase.PaymentVerificationPage().submitButton.click();

        //Enter Both Device Details
        Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo1);//"6007003004006"
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
        // ToDo: Read from data sheet.
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo2);//"6007003004007"
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();

        //Enter Sim Details
        PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber1);//"21212121212121212121"
        PageBase.DeviceVerificationaandActivation().simType2Textbox.sendKeys(simNumber2);//"23232323232323232323"

        try {
            PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
        } catch (Exception e) {
        }
        PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

        //Sign and Accept Terms and Conditions on WCA Page
        Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

        // Order Receipt Page
        Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
        PageBase.OrderReceiptPage().verifyOrderCompletionPage();
        Assert.assertTrue(OrderActivationCompletePage().ActivationComplete.isDisplayed());
        PageBase.CSVOperations().UpdateIMEIStatusToUsed(esnNo1, FileName.SamsungGalaxyS4_16GBBlack);
        PageBase.CSVOperations().UpdateIMEIStatusToUsed(esnNo2, FileName.SamsungGalaxyS4_16GBBlack);
        return orderId;
    }

    public void VerizonExchangeDevice(String OrderId, CustomerDetails customerDetails) throws IOException {

        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        String esnNo = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);

        //Click on Guest Lookup Tab
        Utilities.waitForElementVisible(PageBase.HomePageRetail().guestLookupTab);
        PageBase.HomePageRetail().guestLookupTab.click();
        Utilities.waitForElementVisible(PageBase.CustomerLookupPage().receiptIdTextbox);
        PageBase.CustomerLookupPage().receiptIdTextbox.sendKeys(receiptId);//"132710003003669460"

        List<WebElement> SubmitButtons = driver.findElements(By.xpath(ControlLocators.SUBMIT_RECEIPTID));
        for (WebElement visibleSubmitButton : SubmitButtons) {
            if (visibleSubmitButton.isDisplayed()) {
                visibleSubmitButton.click();
                break;
            }
        }

        //Select the Order
        driver.navigate().to(readConfig("CustomerVerification") + OrderId);

        //Enter the Customer Details for Verification
        Utilities.waitForElementVisible(PageBase.ReturnOrExchangeVerificationPage().cvFirstNameTextbox);
        //PageBase.ReturnOrExchangeVerificationPage().populatingPage(IdType.DRIVERLICENCE, "CA", "123456789", "FredFred", "", "Consumer Two");
        PageBase.ReturnOrExchangeVerificationPage().populatingPage(IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber, customerDetails.FirstName, "", customerDetails.LastName);
        PageBase.CommonControls().continueCommonButton.click();

        //Enter the ESN/EMEI number for the device to be exchanged
        Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().esnIemeidTextbox);
        PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(esnNo);//"6007003004006"
        PageBase.CommonControls().continueButtonDVA.click();

        Utilities.waitForElementVisible(PageBase.ReturnOrExhangePreConditions().deviceAccessoryRadioButton);
        PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
        PageBase.ReturnOrExhangePreConditions().continueREVButton.click();

        //Select more Exchange Details
        Utilities.waitForElementVisible(PageBase.ReturningProcessPage().acceptReturnExchangeRadioButton);
        PageBase.ReturningProcessPage().acceptReturnExchangeRadioButton.click();
        PageBase.ReturningProcessPage().exchangeDeviceRadioButton.click();
        PageBase.ReturningProcessPage().SelectFinancingOption(ReturnProcessPage.FinancingOption.CHECKFINANCING);
        Assert.assertEquals(PageBase.ReturningProcessPage().exchangeMessage.getText(), Constants.EXCHANGE_MESSAGE);
        Assert.assertTrue(PageBase.ReturningProcessPage().continueExchangeButton.isDisplayed());
        PageBase.ReturningProcessPage().SelectExchangeReason(ReturnProcessPage.ExchangeReason.INPOLICYGUESTRETURN);
        PageBase.CommonControls().continueButtonDVA.click();

        Utilities.waitForElementVisible(PageBase.AccountPasswordPage().continueButton);
        PageBase.AccountPasswordPage().accountPassword.sendKeys("HELLO");
        PageBase.AccountPasswordPage().continueButton.click();

        //Deactivate via ShipAdmin
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().SelectActivationInfo(OrderId);
        Utilities.switchPreviousTab();
    }

    public String VerizonExchangetoNonEdgeUpgradeFlow(CustomerDetails customerDetails) throws InterruptedException, AWTException, IOException {
        //Click on UEC Link
        Utilities.waitForElementVisible(PageBase.HomePageRetail().upgradeEligibilityCheckerLink);
        PageBase.HomePageRetail().upgradeEligibilityCheckerLink.click();
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
        PageBase.UECVerificationPage().verizonTab.click();

        String esnNo = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
        String simNumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
        //Enter Verizon Details
        // PageBase.UECVerificationPage().fillVerizonDetails("9882303487", "4850", "HELLO", "94109");
        PageBase.UECVerificationPage().fillVerizonDetails(customerDetails.PhNum, customerDetails.SSN, "", customerDetails.Zip);
        PageBase.UECVerificationPage().continueVerizonButton.click();

        //Click on the First Number that appears
        Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
        PageBase.UECAddLinesPage().firstAALCheckbox.click();
        PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

        //Scann a Device
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(esnNo);//"889988990007"
        PageBase.DeviceScanPage().submitDeviceButton.click();

        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
        PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

        //Enter the Customer Information
        PageBase.ServiceProviderVerificationPage().populateFormByClassButton.click();
        /*PageBase.ServiceProviderVerificationPage().populatingSPVPage
                ("Fred", "", "Consumer Two", "nobody@letstalk.com", IdType.DRIVERLICENCE,
						"CA", "123456789", ServiceProviderVerificationPage.Month.DECEMBER,
						2020, "9880203387", "April", 11, 1970);*/
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        PageBase.CommonControls().continueButton.click();

        //Choose the Edge Option
        Utilities.waitForElementVisible(PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton);
        PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton.click();
        PageBase.CommonControls().continueCommonButton.click();

        //Choose an Existing Plan in Shop Plans
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().KeepMyExistingVerizonWirelessLegacyAddButton);
        PageBase.VerizonShopPlansPage().KeepMyExistingVerizonWirelessLegacyAddButton.click();

        Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
        Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, SelectDropMethod.SELECTBYINDEX, "1");
        String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
        String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
        PageBase.CartPage().continueCartButton.click();

        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        //PageBase.VerizonSelectPlanFeaturesPage().basicPhoneDiscount0Checkbox.click();
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
        PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        PageBase.CommonControls().continueCommonButton.click();

        //Accept the Terms and Conditions
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox);
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.TermsAndConditionsPage().continueTCButton.click();
        Thread.sleep(1000);

        try {
            Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().sameAddressTab);
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA); //ToDo: Need to read from data sheet.
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        } catch (Exception e) {
        }

        // MSS page.
        //ToDo: BAR Code verification after clarifying on the expected Bar code.
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);//"132710003003669460"
        PageBase.PaymentVerificationPage().submitButton.click();

        Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo);//"889988990007"
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();

        PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);//21212121212121212121

        try {
            PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
        } catch (Exception e) {
        }
        PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();
        Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

        //Print the Details
        PageBase.DeviceFinancingInstallmentContractPage().PrintDeviceFinancingDetails(driver);

        //Verify that the Order is Successfully Created
        Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
        PageBase.OrderReceiptPage().verifyOrderCompletionPage();
        Assert.assertTrue(OrderActivationCompletePage().ActivationComplete.isDisplayed());
        //String orderId = PageBase.OrderSummaryPage().orderNumberText.getText().replace("#","");

        //Check inventory Details
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().verifyDeviceStatus(esnNo, "Sold");//"889988990007"
        return orderId;
    }

    private void QA_78ShipadminVerification(String orderId) throws IOException {
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
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.HANDSET_UPGRADE));
        //Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.EXISTING_ACCOUNT_HOLDER));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
    }

    private void setUpCarrierResponderForQA_67() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse(carrierType,
                BrowserSettings.readConfig("internalTestType"));
        // Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig(carrierType);
        // Customizing xml files in Carrier Responder
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "submitCreditApplication", "approved.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "validatePortInEligibility", "default_1_line.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "retrieveInstallmentDetailsForDevice", "eligible.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "generateInstallmentContract", "success.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "submitactivation", "success_1_line.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "submitServiceDetails", "success_edge.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "submitReceipt", "default.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();


        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "retrievePricePlans", "plaid_v_1_line.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "retrieveNpaNxx", "single_area_code.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "retrieveCreditApplication",
                "new_activation_approved_edge.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "submitSigpadCaptureForInstallmentAgreement",
                "success.xml");
        Utilities.waitForElementVisible(PageBase
                .CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

    }

    private String poaCompleteFlow_QA_67(String orderId, IMEIDetails imei) throws IOException, AWTException, InterruptedException {
        PageBase.LoginPageRetail().poaLogin(
                Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"),
                Utilities.getCredentials("storeId0003"));
        // Click on Sales & Activations page.
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);

        // Click on New Activation link.
        PageBase.ChoosePathPage().newActivation.click();
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei.IMEI);

        // Accept Edge
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
        PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

        // Running Credit Check
        Utilities
                .waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(
                cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();

        lStartTime = new Date().getTime();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
        }

        // Verizon Edge
        Utilities
                .waitForElementVisible(PageBase.VerizonEdgePage().customerDownPayment);
        PageBase.VerizonEdgePage().monthlyDownPayment.click();
        PageBase.CartPage().continueCartButton.click();

        // Verizon Shop Plans Page
        Utilities
                .waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton);
        PageBase.VerizonShopPlansPage().verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton
                .click();
        String phonePrice = PageBase.CartPage().device1Price.getText();
        System.out.println("-----------------------------" + phonePrice);
        PageBase.CartPage().continueCartButton.click();

        // Verizon Select Plan Features Page
        Utilities.waitForElementVisible(PageBase
                .VerizonSelectPlanFeaturesPage().continueSPFButton);

        // PageBase.VerizonSelectPlanFeaturesPage().basicPhoneDiscount0Checkbox.click();
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        // Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase
                .SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance
                .click();
        Utilities.implicitWaitSleep(5000);
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        // Selecting Number Porting.
        Utilities.ClickElement(PageBase.NumberPortPage().numberPortRadiobutton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);
        PageBase.PortMyNumbersPage().populatePortData();
        Utilities.ClickElement(PageBase.CommonControls().continueCommonButton);

        // Terms and Condition Page.
        PageBase.TermsAndConditionsPage().acceptTermsAndConditions();

        // Credit Card Payment Page
        Utilities.implicitWaitSleep(10000);
        if (driver.getCurrentUrl().contains("payment")) {
            PageBase.PaymentRequiredPage()
                    .populatingCardDetailsPaymentRequired(CardType.VISA);
            Utilities
                    .ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities
                    .ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }
        if (readConfig("Activation").contains("true")) {

            // MSS page.
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText
                    .getText();
            Utilities
                    .ClickElement(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            // Payment Verification page.
            String recieptID = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
            PageBase.PaymentVerificationPage().paymentVerification(recieptID);

            //Device Activation page
            PageBase.DeviceVerificationaandActivation()
                    .deviceVerificationActiavtionFor1Device(imei.IMEI, imei.Sim);
            PageBase.CommonControls().continueButtonDVA.click();
            // WCA Signature page.
            Utilities
                    .ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei.IMEI, FileName.SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(recieptID, FileName.ReceiptId);
            // PageBase.CSVOperations().UpdateIMEIStatusToUsed(phoneNumber, FileName.NewPhoneNumber);
            //PageBase.CSVOperations().UpdateIMEIStatusToUsed(accountDetails.MTN, FileName.VerizonEdgeUpgrade);

            //Device Finance and Installement Contarct page.
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
        }
        return orderId;
    }

    private void selectingVerizonExternalEnvironment() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
        Reporter.log("<h3><U> External Server</U></h3>", true);
    }

    //region QA-71 Refactored Methods
    private void poaFlowQA71() throws InterruptedException, AWTException,
            IOException {

        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(FileName.VerizonEdgeIndividualAccountReUseable);
        CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        String phoneNumber = accountDetails.MTN;
        String sSN = accountDetails.SSN;
        String zipCode = cusDetails.Zip;
        String accountPassword = accountDetails.Password;

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
        PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(phoneNumber);
        PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(sSN);
        PageBase.UECVerificationPage().accountPasswordVerizonTextbox.sendKeys("");
        PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys(zipCode);
        PageBase.UECVerificationPage().continueVerizonButton.click();

        //Select an Option Page
        Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().switchFromAnIndividualToFamilyPlanAndAddALineRadioButton);
        PageBase.SelectAnOptionPage().switchFromAnIndividualToFamilyPlanAndAddALineRadioButton.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Processing Error Page
        Utilities.waitForElementVisible(PageBase.ProcessingErrorPage().processingErrorText);
        Assert.assertTrue(PageBase.ProcessingErrorPage().processingErrorText.isDisplayed());
        Assert.assertTrue(PageBase.ProcessingErrorPage().processingErrorMessageText.isDisplayed());
        Assert.assertTrue(PageBase.ProcessingErrorPage().aTTHelpNumberText.isDisplayed());
        Assert.assertTrue(PageBase.ProcessingErrorPage().verizonHelpNumberText.isDisplayed());
        Assert.assertTrue(PageBase.ProcessingErrorPage().sprintHelpNumberText.isDisplayed());
    }

    private void carrierResponderSettingsQA71() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCustomerDetails", "IN.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }
//endregion QA-71 Refactored Methods

    //region QA-5437 Refactored Methods
    private void shipAdminVerificationsQA5437(@Optional String testtype, String orderId) throws IOException {
        if (testtype.equals("internal")) {
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            driver.navigate().back();
            Utilities.waitForElementVisible(PageBase.OrderSummaryPage().statusValueLink);
        } else {
            ShipAdminBaseClass.launchShipAdminInNewTab();
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        }
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

    private String poaFlowQA5437(@Optional String testtype, String iMEINumber, String simNumber, String orderId, String receiptId, String phoneNumber, String sSN, String zipCode, String accountPassword) throws IOException, AWTException, InterruptedException {
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
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Verizon Edge Page
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
        PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().continueDSButton);
        PageBase.DeviceScanPage().continueDSButton.click();

        //Carrier Credit Check Page
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        PageBase.CarrierCreditCheckPage().skip.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        String phonePrice = PageBase.CartPage().device1Price.getText();
        String phoneModel = PageBase.CartPage().phoneModelAALLink.getText();
        PageBase.CartPage().continueCartButton.click();

        //Verizon Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
        PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
        Utilities.implicitWaitSleep(6000);
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        //Number Port Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        sSN = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SSNWithoutDeposit);
        PageBase.ServiceProviderVerificationPage().populatingSPVPageWithCreditCardDetails(customerDetails.FirstName,
                "", customerDetails.LastName, customerDetails.Address1, "", customerDetails.City, customerDetails.State,
                zipCode, phoneNumber, customerDetails.BirthdayMonth, customerDetails.BirthdayDay, customerDetails.BirthdayYear, customerDetails.EMail,
                sSN, IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber, customerDetails.IDExpirationMonth, customerDetails.IDExpirationYear);
        PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck.click();
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();
        try {
            PageBase.ServiceProviderVerificationPage().continueSPVButton.click();
        } catch (Exception e) {
        }

        //Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Assert.assertEquals(driver.findElement(By.xpath("(((//div[@class='orConfirmRow']/child::div)[5]/child::li/child::div)[1]/child::div)[2]")).getText(), phonePrice); //Phone Price Not Coming Same
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().existingPlanDiv.isDisplayed());
        String existingPlan1 = CommonFunction.getPlan(PageBase.OrderReviewAndConfirmPage().existingPlanDiv, 17);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalDue = totalDue.replace("inc. tax", "");
        totalDue = totalDue.replace(" ", "");
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
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
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
            Reporter.log("<br> Receipt ID Used: " + receiptId);
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation Page
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(iMEINumber);
            PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
            PageBase.DeviceVerificationaandActivation().simType.clear();
            PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);
            try  //ToDo:Remove this when no insurance bug will fix.
            {
                CreditCardDetails creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(creditCard.CVV);
            } catch (Exception e) {
            }
            PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

            //Wireless Customer Agreement Page
            Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox, 120);

            if (testtype.equals("internal")) {
                //Getting new number assigned from shipadmin
                ShipAdminBaseClass.launchShipAdminInNewTab();
                PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
                Utilities.dropdownSelect(PageBase.OrderSummaryPage().printableFormsDropdown, SelectDropMethod.SELECTBYINDEX, "1");
                Utilities.waitForElementVisible(OrderActivationCompletePage().phoneNumberValueText);
                String newPhoneNumberAssigned = OrderActivationCompletePage().phoneNumberValueText.getText();
                newPhoneNumberAssigned = CommonFunction.getUnFormattedPhoneNumber(newPhoneNumberAssigned);

                //Customizing xml files in Carrier Responder
                Utilities.switchPreviousTab();
                Utilities.switchPreviousTab();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_1_line.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(5000);
                String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
                xmlContent2 = xmlContent2.replace(Constants.DEFAULT_XML_NUMBER_4152647954, newPhoneNumberAssigned);
                PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
                Robot robot = new Robot();
                Utilities.copyPaste(xmlContent2, robot);
                Utilities.implicitWaitSleep(5000);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
                Utilities.implicitWaitSleep(5000);

                //Switching To Retail
                Utilities.switchPreviousTab();
                Utilities.switchPreviousTab();
                Utilities.switchPreviousTab();
            }

            //Wireless Customer Agreement Page
            PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Reporter.log("<h3><U>Order ha been confirmed, Order Id =" + orderId + "</U></h3>");
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(iMEINumber, FileName.SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(receiptId, FileName.ReceiptId);
            Assert.assertEquals(OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
            Assert.assertEquals(OrderActivationCompletePage().simNumberValueText.getText(), simNumber);
            //Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValueText.getText(), phonePrice);
        }
        return orderId;
    }


    private void carrierResponderSettingsQA5437() throws InterruptedException, AWTException, IOException {
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
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "default_upgrade.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveExistingCustomerInstallmentDetails", "eligible_no_payment_needed.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitpayment", "default.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
    }

    //endregion QA-5437 Refactored Methods

    //region QA 75 Refactored Methods
    private String poaCompleteFlow_75(String testType) throws IOException {
        String orderId = "";//Login to retail page.
        CreditCardDetails creditCard = new CreditCardDetails();
        creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
        lStartTime = new Date().getTime();
        pageName = readPageName("PoaLogin");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on Sales & Activations page.
        lStartTime = new Date().getTime();
        pageName = readPageName("SaleAndActivation");
        PageBase.HomePageRetail().salesAndActivationsLink.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on New Activation link.
        lStartTime = new Date().getTime();
        pageName = readPageName("DeviceScan");
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Scanning smart phone.
        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails1.IMEI);
        // Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);

        //Verizon Edge Page
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
        PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);

        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();

        lStartTime = new Date().getTime();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);

        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {

        }
        //if(testType.equals("internal")) PageBase.CommonControls().continueButton.click();
        Utilities.webPageLoadTime(lStartTime, pageName);
        // boolean notfound = true;

        //Continue with 2yrs plan.
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDeposit);
            PageBase.CommonControls().continueButtonDeposit.click();
        } catch (Exception ex) {
        }

        // Credit Check Verification Results with deposits.
        try {
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
        } catch (Exception ex) {
            //notfound = false;
            //PageBase.CommonControls().continueButtonDeposit.click();
        }

        // DownPayment page if comes in external testing.
        String testtype = BrowserSettings.readConfig("test-type");

        if (testtype.equals("internal")) {
            try {
                Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
                PageBase.VerizonEdgePage().monthlyDownPayment.click();
                PageBase.CommonControls().continueCommonButton.click();
            } catch (Exception ex) {
            }
        }

        // Selecting Plan.
        Utilities.implicitWaitSleep(5000);
        ArrayList<WebElement> list = (ArrayList<WebElement>) driver.findElements(By.xpath("//h1[contains(text(),'Verizon Plan')]"));
        if (list.size() > 0) {
            Utilities.waitForElementVisible(list.get(0));
            list.get(0).click();
            PageBase.VerizonShopPlansPage().addPlan();
        } else {
            Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMorePlanOnly);
            PageBase.VerizonShopPlansPage().selectPlanWithMore();
            PageBase.VerizonShopPlansPage().addPlan();
        }


        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //Select Plan - Storing the Device and plan prices for further verification.
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting No Insurance .
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
        } catch (Exception ex) {
        }

        // Selecting No Number Porting.
        try {
            Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
            PageBase.NumberPortPage().noNumberPortRadiobutton.click();
            PageBase.CommonControls().continueButton.click();
        } catch (Exception ex) {
        }

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        // Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),cartDevice1price);

        PageBase.CommonControls().continueCommonButton.click();

        //Terms and Condition Page.
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().continueTCButton);
        PageBase.TermsAndConditionsPage().emailTCChkBox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.TermsAndConditionsPage().continueTCButton.click();

        // Credit Check Verification Results with deposits.
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
            boolean exist = driver.findElements(By.id("checkbox-mini-1")).size() != 0;
            if (exist) {
                PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
                PageBase.CommonControls().continueButtonDeposit.click();
            }
        } catch (Exception ex) {
        }

        // Credit card payment  page is coming.
        do {
            Utilities.implicitWaitSleep(5000);
            if (driver.getCurrentUrl().contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
            }
        } while (driver.getCurrentUrl().contains("payment"));

        //Print Mobile Scan Sheet.
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        // orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        //PageBase.PrintMobileScanSheetPage().verifyAllTwoDeviceBarCode();

        //TODO:Need to add assertion for store location.
        //		WebElement web = driver.findElement(By.xpath("//span[contains(text(),'2766 - TARGET - SAN FRANCISCO CENTRAL')]"));
        //		String storeLocation = web.getText();
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        if (readConfig("Activation").contains("true")) {
            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imeiDetails1);

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, FileName.SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(receiptId, FileName.ReceiptId);

            // WCA Signature page activity and verifications.
            Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);

            List<WebElement> wcaLists = driver.findElements(By.xpath("//div[@class ='termsandcondition'][1]/label"));
            Assert.assertEquals("Application ID No.:", wcaLists.get(0).getText());
            Assert.assertEquals("Order Date:", wcaLists.get(1).getText());
            Assert.assertEquals("Bill Acct. No.:", wcaLists.get(2).getText());
            Assert.assertEquals("Agent Name:", wcaLists.get(3).getText());
            Assert.assertEquals("Activation Type:", wcaLists.get(4).getText());

            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

            // Order Activation Complete page.
            Utilities.waitForElementVisible(OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(OrderActivationCompletePage().ActivationComplete.isDisplayed());
            String orderIdfromActPage = OrderActivationCompletePage().orderNumberValueText.getText();
            Assert.assertTrue(OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
            Assert.assertTrue(OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void shipAdminVerification_75(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
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
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
    }

    private void inventoryManagementVerification_75() throws InterruptedException, AWTException, IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, IMEIStatus.Sold.toString());
    }

    private void selectingCarrierEnviornment_75(String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            //selectingBackendSimulatorForQA75();

            //Selecting Carrier Responder
            selectCarrierResponderQA75();

        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
        }
    }

    private void selectingBackendSimulatorForQA75() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        PageBase.AdminPage().selectCreaditReadUseCase("APPROVE_WITH_DEPOSIT");
        PageBase.AdminPage().selectCreaditWriteUseCase("APPROVE_WITH_DEPOSIT");
        PageBase.AdminPage().retrieveCustomerDetails("ELIGIBLE");

        List<WebElement> phoneList = PageBase.AdminPage().phoneList.findElements(By.className("phoneNumberRow"));

        if (phoneList.size() > 0) {
            List<WebElement> removePhone = PageBase.AdminPage().phoneList.findElements(By.id("retrieveCustomerDetails_removePhoneNumberRowButton"));
            for (int i = 0; i < removePhone.size(); i++) {
                removePhone.get(i).click();
            }
        }

        PageBase.AdminPage().accountPlanType("Family Share");
        PageBase.AdminPage().retrieveExistingCustomerInstallmentsDetails("SUCCESS_WITH_PAYMENT");
        PageBase.AdminPage().retrievePricePlan("SUCCESS");
        PageBase.AdminPage().submitActivation("SUCCESS");
        PageBase.AdminPage().submitReciept("SUCCESS");
        PageBase.AdminPage().submitServiceDetails("SUCCESS");
        PageBase.AdminPage().submitEdgeUpPayment("SUCCESS");
        PageBase.AdminPage().returnOrExchangeDevice("SUCCESS");

        PageBase.AdminPage().save();
    }

    private void selectCarrierResponderQA75() throws AWTException, InterruptedException {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        // Selecting Verizon and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_edge_with_deposit.xml");
        Utilities.implicitWaitSleep(5000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace(Constants.APPROVE_WITH_DEPOSIT, Constants.APPROVE_WITH_DEPOSIT_VALUE);
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Robot robot = new Robot();
        Utilities.copyPaste(xmlContent1, robot);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveInstallmentDetailsForDevice", "eligible.xml");
        Utilities.implicitWaitSleep(5000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_v_1_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved_with_deposit.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "default.xml");
        //PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "success_edge.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_1_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitpayment", "default.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "generateInstallmentContract", "success.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

//        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveNpaNxx", "single_area_code.xml");
//        Utilities.implicitWaitSleep(4000);
//        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
//        Utilities.implicitWaitSleep(4000);
//        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
//        Utilities.implicitWaitSleep(8000);
    }
    //endregion QA 75 Refactored Methods

    //region QA 57 Private Methods
    private void PoaCompleteFlow_57(IMEIDetails imeiDetails1) throws InterruptedException, IOException, AWTException {
        String orderId;
        lStartTime = new Date().getTime();
        pageName = readPageName("PoaLogin");
        Utilities.switchPreviousTab();
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"),
                Utilities.getCredentials("storeId0003"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on Sales & Activations page.
        lStartTime = new Date().getTime();
        pageName = readPageName("SaleAndActivation");
        PageBase.HomePageRetail().salesAndActivationsLink.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on New Activation link.
        lStartTime = new Date().getTime();
        pageName = readPageName("DeviceScan");
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails1.IMEI);
        Reporter.log("<br> Declining Verizon Edge");
        PageBase.VerizonEdgePage().declineVerizonEdge();

        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        PageBase.CarrierCreditCheckPage().skip.click();
        Reporter.log("<br> Skip Credit Check");
        Thread.sleep(1000);
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMorePlanOnly);
        PageBase.VerizonShopPlansPage().selectPlanWithMore();
        PageBase.VerizonShopPlansPage().addPlan();
        Reporter.log("<br> Added a plaid plan");
        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Select Plan - Storing the Device and plan prices for further verification.
        cartDevice1price = PageBase.CartPage().device1Price.getText();

        //cartPlanprice = PageBase.CartPage().planPriceActual.getText();   // Fix it.
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        Reporter.log("<br> Selecting Feature");
        PageBase.SelectPlanFeaturesPage().selectFamilyBasePlan(0);
        Reporter.log("<br> Family Base Plan feature Selected");
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting No Insurance .
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();
            Reporter.log("<br> No Insurance for the device");
        } catch (Exception e) {
            Reporter.log("<br> No Insurance Page");
        }
        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        Reporter.log("<br> No Number Porting Selected ");
        PageBase.CommonControls().continueButton.click();

        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);
        PageBase.ServiceProviderVerificationPage().populatingSPVPageAllMandateFields(customerDetails.FirstName,
                customerDetails.LastName, customerDetails.Address1, customerDetails.City, customerDetails.State,
                customerDetails.Zip, customerDetails.PhNum, customerDetails.EMail, customerDetails.BirthdayMonth,
                customerDetails.BirthdayDay, customerDetails.BirthdayYear, customerDetails.IDType,
                customerDetails.IDState, customerDetails.IDNumber, customerDetails.IDExpirationMonth,
                customerDetails.IDExpirationYear);
        Utilities.ScrollToElement(PageBase.CarrierCreditCheckPage().ssnTextBox);
        PageBase.CarrierCreditCheckPage().ssnTextBox.click();
        Utilities.setValue(PageBase.CarrierCreditCheckPage().ssnTextBox,
                PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SSNWithoutDeposit));

        Reporter.log("<br> Entered Customer Details at the Service Provider Verification Page");
        Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        Utilities.ClickElement(PageBase.CommonControls().continueCommonButton);

        PageBase.TermsAndConditionsPage().acceptTermsAndConditions();
        Reporter.log("<br> Terms and conditions accepted");
        Thread.sleep(20000);

        if (driver.getCurrentUrl().contains("payment")) {

            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

        Utilities.waitForElementVisible(PageBase.CommonControls().supportCenterPageMessage);
        Reporter.log("<br> Support Center Page for Manual Review");
        orderId = PageBase.CommonControls().orderIdSupportCenterPage.getText();
        orderId = orderId.substring(1, orderId.length());
        Reporter.log("<h3><font color=green> Order Id is : " + orderId + "</font></h3>");

        ShipAdminBaseClass.launchShipAdminInNewTab();
        Reporter.log("Navigating to ship admin page for Manual Review");
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        Reporter.log("<br> Navigating to Order Summary Page for " + orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        Reporter.log("<br><U>" + PageBase.ShipAdminPage().manualReviewStatusOnShipAdmin.getText() + "</U>");
        Assert.assertEquals(PageBase.ShipAdminPage().manualReviewStatusOnShipAdmin.getText(),
                "Credit Check Required - Manual Review",
                "Status of the Order is not: Credit Check Required - Manual Review");

        Reporter.log("<br>" + PageBase.ShipAdminPage().creditCheckResultSummaryTable.getText() + " Table exists");
        PageBase.ShipAdminPage().updateCreditCheck.click();
        Reporter.log("<br> Clicked on Update button to edit Credit Check Results");

        Utilities.dropdownSelect(PageBase.ShipAdminPage().updateCreditCheckResult,
                SelectDropMethod.SELECTBYTEXT, "Credit Approved");

        Utilities.dropdownSelect(PageBase.ShipAdminPage().updateCreditCheckResultReason,
                SelectDropMethod.SELECTBYTEXT, "$0 deposit");
        Reporter.log("<br>Credit Approved with $0 deposit");
        Utilities.waitForElementVisible(PageBase.ShipAdminPage().numberOfLinesToUpdate);
        PageBase.ShipAdminPage().numberOfLinesToUpdate.sendKeys("1");

        PageBase.ShipAdminPage().addUpdateCreditCheck.click();
        Reporter.log("<br><U>" + PageBase.OrderSummaryPage().getOrderStatus() + "</U>");
        Utilities.switchPreviousTab();
        Reporter.log("<br> Switched back to POA After Manual Review");

        PageBase.CommonControls().continueSupportCenter.click();
        Reporter.log("<br> Capture Credit Card Details ");
        if (driver.getCurrentUrl().contains("payment")) {
            Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().creditCardNumberTextbox);
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        Reporter.log("<br> Credit Card Details Accepted");
        Assert.assertEquals(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText(), orderId,
                "Order Id missmatch on Support Page and MSS page");
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        //Payment Verification page. Scan Reciept id.
        Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
        receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        Reporter.log("<br> Entered Receipt Id: " + receiptId + "<br>");
        PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
        PageBase.PaymentVerificationPage().submitButton.click();


        Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(imeiDetails1.IMEI);
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
        try {
            if (driver.findElement(By.id("scanerror")).isDisplayed()) {
                Assert.fail(driver.findElement(By.id("scanerror")).getText() + "<h2>Device is not aviable. Re-run the test case</h2.");
            }
        } catch (Exception e) {
            Reporter.log("No Error After Sim is submitted @ Device Verification and Activation Page");
        }
        driver.findElement(By.id(PageBase.DeviceVerificationaandActivation().simTypeM + imeiDetails1.IMEI)).click();
        driver.findElement(By.id(PageBase.DeviceVerificationaandActivation().simTypeM + imeiDetails1.IMEI)).sendKeys(imeiDetails1.Sim);
        Utilities.ScrollToElement(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);


        try {
            if (PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.isEnabled())
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
            PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");
        } catch (Exception e) {
            Reporter.log("CVN Number No Needed");
        }
        PageBase.CommonControls().continueActivation.click();
        do {
            Utilities.implicitWaitSleep(1000);
        } while (!driver.getCurrentUrl().contains("retail/support.htm"));
        Utilities.waitForElementVisible(PageBase.CommonControls().supportCenterPageMessage);

        ShipAdminBaseClass.launchShipAdminInNewTab();
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        try {
            if (!driver.findElement(By.xpath(".//*[@id='actFormDiv']/div[1]/input[1]")).isDisplayed()) {
                Utilities.dropdownSelect(driver.findElement(By.xpath("html/body/table[2]/tbody/tr[2]/th[4]/b/select")),
                        SelectDropMethod.SELECTBYTEXT, "Move queues");

                Utilities.dropdownSelect(driver.findElement(By.xpath(".//select[@name='ordsubstId']")),
                        SelectDropMethod.SELECTBYTEXT, "Awaiting Carrier Resolution");
                driver.findElement(By.xpath(".//select[@value='Move Queues']")).click();
                PageBase.OrderSummaryPage().rOCHomeLink.click();
                PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            }
        } catch (Exception e) {

        }

        driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[5]/td[2]/input")).sendKeys("598764987");
        driver.findElement(By.xpath(".//*[@id='actItemFieldsBlock']/table/tbody/tr[4]/td[2]/input")).sendKeys("6649728193");
        driver.findElement(By.xpath(".//*[@id='actItemFieldsBlock']/table/tbody/tr[8]/td/input[1]")).click();

        driver.findElement(By.xpath(".//*[@id='actFormDiv']/div[1]/input[1]")).click();
        try {
            if (driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[9]/td[2]/input")).isEnabled()) {
                driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[9]/td[2]/input")).sendKeys("123");
            }
        } catch (Exception e) {

        }
        //    //NavigateBackToPage
        //    //Activation Complete
        PageBase.OrderSummaryPage().rOCHomeLink.click();
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        Reporter.log(PageBase.OrderSummaryPage().getOrderStatus());
        if (!PageBase.OrderSummaryPage().getOrderStatus().toLowerCase().contains("shipped")) {
            try {
                Thread.sleep(1000);
                if (driver.findElement(By.xpath(".//*[@id='actFormDiv']/div[1]/input[1]")).isDisplayed()) {
                    driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[5]/td[2]/input")).
                            sendKeys("598764987");
                    driver.findElement(By.xpath(".//*[@id='actItemFieldsBlock']/table/tbody/tr[4]/td[2]/input")).
                            sendKeys("6649728193");
                    driver.findElement(By.xpath(".//*[@id='actItemFieldsBlock']/table/tbody/tr[8]/td/input[1]")).
                            click();
                    driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[9]/td[2]/input")).
                            sendKeys("123");
                    driver.findElement(By.xpath(".//*[@id='actFormDiv']/div[1]/input[1]")).click();
                    PageBase.OrderSummaryPage().rOCHomeLink.click();
                    PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
                }
            } catch (Exception e) {
            }
        }
        Assert.assertTrue(PageBase.OrderSummaryPage().getOrderStatus().toLowerCase().contains("shipped"));
        Reporter.log("<br> Order Moved to Shipped" + PageBase.OrderSummaryPage().getOrderStatus());
        Utilities.switchPreviousTab();
        Utilities.waitForElementVisible(driver.findElement(By.xpath(".//button[contains(text(),'continue')]")));
        driver.findElement(By.xpath(".//button[contains(text(),'continue')]")).click();
        Assert.assertTrue(OrderActivationCompletePage().ActivationComplete.isDisplayed());

        //    Reporter.log("<h4>Errors on Event Log: </h4>" + eventLogTableContent);

        PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, FileName.SamsungGalaxyS4_16GBWhite);
        PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, FileName.ReceiptId);
        Reporter.log("<h2><font color='green'> Test Case Completes </font></h2>");
    }

    private void EnviornmentSettings_57(String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            AdminBaseClass adminBaseClassInternla = new AdminBaseClass();
            adminBaseClassInternla.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();

            if (readConfig("internalTestType").toLowerCase().equals("carrierresponder")) {
                Reporter.log("<h3><U> Carrier Responder</U></h3>", true);

                PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
                PageBase.AdminPage().selectAPIConfig("Verizon");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
                PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();

                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
                PageBase.CarrierResponseXMLPage().selectOptions("current",
                        "submitCreditApplication", "manual_review.xml");

                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();

                Reporter.log("<br>Carrier Responder Changes Done!!!", true);
            } else {
                PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");
                PageBase.AdminPage().selectAPIConfig("Verizon");
                PageBase.AdminPage().selectCreaditWriteUseCase("MANUAL_REVIEW");
                PageBase.AdminPage().save();
            }
        } else   //External
        {
            AdminBaseClass adminBaseClassExternal = new AdminBaseClass();
            adminBaseClassExternal.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
            Reporter.log("<h3><U> External Server</U></h3>", true);
        }
    }
//endregion QA 57


    private void QA_1734_BackendSimulatorSettings() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");
    }

    private void QA_1734_CarrierResponderSettings() throws IOException {
        PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));
        // Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig(carrierType);
        Utilities.implicitWaitSleep(5000);
        // Customizing xml files in Carrier Responder
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_edge.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveNpaNxx", "single_area_code.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_v_1_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitReceipt", "default.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "success_edge.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "generateInstallmentContract", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveInstallmentDetailsForDevice", "eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
    }

    private void selectingCarrierEnviornment_QA_2639(String testType, String internalTestType) throws InterruptedException, AWTException, IOException {
        if (testType.contains("internal")) {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            // Selecting the Execution env either Backend or
            // CarrierResponder based on config file
            if (internalTestType.contains("BackendSimulator")) {
                QA_1734_BackendSimulatorSettings();
            } else {
                QA_2639_CarrierResponderSettings();
            }
        } else {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "external");
        }

    }

    private void QA_2639_shipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertTrue(status.contains(Constants.ORDER_CANCELLED_BY_USER));
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_FAILED_INVALID_SIM));
    }

    private String QA_2639_VZWEdgeParkingFailDuringWCAGeneration_PoaFlow(String imei, String sim)
            throws IOException, InterruptedException, AWTException {
        PageBase.CSVOperations();
        //This Method Contains POA flow for test case QA_69
        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        String orderId = null;
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);

        //Login to retail application
        Reporter.log("<br> Login to Application");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"),
                Utilities.getCredentials("storeId0003"));
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);

        //Choose a Path Page - Selecting New Activation option
        Utilities.ClickElement(PageBase.ChoosePathPage().newActivation);

        //Device-Page - scanning the IMEI number
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei);

        //Accept Edge
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
        PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

        //Running Credit Check
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
        Reporter.log("<br>Credit Check complete");
        //Verizon Edge Page
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().monthlyDownPayment);
        PageBase.VerizonEdgePage().monthlyDownPayment.click();
        PageBase.CartPage().continueCartButton.click();

        //Verizon Shop Plans Page
        Utilities.implicitWaitSleep(6000);
        ArrayList<WebElement> list = (ArrayList<WebElement>) driver.findElements(By.xpath("//h1[contains(text(),'Verizon Plan')]"));
        if (list.size() > 0) {
            Utilities.waitForElementVisible(list.get(0));
            list.get(0).click();
            PageBase.VerizonShopPlansPage().addPlan();
        } else {
            Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton);
            PageBase.VerizonShopPlansPage().verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton.click();
        }

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        PageBase.CartPage().continueCartButton.click();

        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        //No Number Porting
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Utilities.ClickElement(PageBase.CommonControls().continueCommonButton);

        //Terms and Condition Page.
        PageBase.TermsAndConditionsPage().acceptTermsAndConditions();

        //Credit Card Payment Page
        Utilities.implicitWaitSleep(10000);
        if (driver.getCurrentUrl().contains("payment")) {
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText);
        orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        Utilities.ClickElement(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);

        //Payment Verification page.
        PageBase.PaymentVerificationPage().paymentVerification(receiptId);

        //Device Verification and Activation page.
        PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei, sim);
        PageBase.CommonControls().continueButtonDVA.click();
        Utilities.implicitWaitSleep(120000);
        Assert.assertTrue(driver.getCurrentUrl().contains("processingerror.htm"));
        PageBase.CSVOperations();
        CSVOperations.WriteToCSV("QA_2639", orderId, imei, "", "", customerDetails.FirstName,
                customerDetails.LastName, customerDetails.EMail, receiptId, customerDetails.IDType,
                customerDetails.State, customerDetails.IDNumber, customerDetails.PhNum, customerDetails.Zip,
                customerDetails.SSN, customerDetails.IDExpirationMonth, customerDetails.IDExpirationYear);
        return orderId;
    }
    //endregion QA-2639 Refactored Methods

    // region QA-5252 Refactored Methods
    private void selectingCarrierEnviornment_QA_5252(String testType, String internalTestType) throws InterruptedException, AWTException, IOException {
        if (testType.contains("internal")) {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            // Selecting the Execution env either Backend or
            // CarrierResponder based on config file
            if (internalTestType.contains("BackendSimulator")) {
                Log.info("Using Backend Simulator");
                QA_5252_BackendSimulatorSettings();
            } else {
                Log.info("Using Carrier Resonder");
                QA_5252_CarrierResponderSettings();
            }
        } else {
            Log.info("Using External Env");
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "external");
        }

    }

    private void QA_5252_PoaFlow() throws IOException {
        CustomerDetails details = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(FileName.VerizonEdgeIndividualAccountReUseable);

        // Login to retail application
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"),
                Utilities.getCredentials("storeId0003"));
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);

        // Choose a Path Page - Selecting Existing Carrier option
        Utilities.ClickElement(PageBase.ChoosePathPage().existingCarrier);

        // Pick Your path and continue - Selecting Add a line to existing
        // Account
        Utilities.ClickElement(PageBase.PickYourPathPage().AALExistingAccount);
        PageBase.CommonControls().continueButtonDVA.click();

        // Upgrade-Eligibility-Checker Page - Populating the User Details
        PageBase.UECVerificationPage().fillVerizonDetails(accountDetails.MTN, accountDetails.SSN, accountDetails.Password, details.Zip);
        PageBase.UECVerificationPage().continueVerizonButton.click();

        // Add-Line-type-Selection Page - Selecting Add a line to Existing
        // Family Plan
        PageBase.SelectAnOptionPage().switchFromAnIndividualToFamilyPlanAndAddALineRadioButton.click();
        PageBase.CommonControls().continueButtonDVA.click();

        // Asserting to verify that user is navigated to error page
        Assert.assertTrue(driver.getCurrentUrl().contains("orderassembly/processingerror.htm"));
    }

    private void QA_5252_CarrierResponderSettings() throws IOException {
        PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));
        // Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig(carrierType);
        // Customizing xml files in Carrier Responder
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCustomerDetails", "IN.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private void QA_5252_BackendSimulatorSettings() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");

        // Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");
        PageBase.AdminPage().selectCreaditReadUseCase("APPROVED_EDGEUP");
        PageBase.AdminPage().retrieveCustomerDetails("VZW_EDGE_UP_ELIGIBLE");
        List<WebElement> phoneList = PageBase.AdminPage().phoneList.findElements(By.className("phoneNumberRow"));
        if (phoneList.size() > 0) {
            List<WebElement> removePhone = PageBase.AdminPage().phoneList
                    .findElements(By.id("retrieveCustomerDetails_removePhoneNumberRowButton"));
            for (int i = 0; i < removePhone.size(); i++) {
                removePhone.get(i).click();
            }
        }
        PageBase.AdminPage().accountPlanType("Individual");
        PageBase.AdminPage().retrieveExistingCustomerInstallmentsDetails("SUCCESS_WITH_PAYMENT");
        PageBase.AdminPage().submitCreditApplication("APPROVED");
        PageBase.AdminPage().save();
        Utilities.switchPreviousTab();
    }
    // endregion QA-5252 Refactored Methods


    //region QA 5571
    private String QA_5571_PoaFlow() throws IOException, AWTException, InterruptedException {
        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        NumPortingDetails portDetails = CSVOperations.ReadPortingDetails();
        String esnNo1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
        String esnNo2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
        String simNumber2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);

        Utilities.waitForElementVisible(PageBase.HomePageRetail().salesAndActivationsLink);
        PageBase.HomePageRetail().salesAndActivationsLink.click();
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
        PageBase.ChoosePathPage().existingCarrier.click();
        //		Utilities.waitForElementVisible(PageBase.PlanTypeSelectionPage().AALExistingAccount);
        //		PageBase.PlanTypeSelectionPage().AALExistingAccount.click();

        Utilities.waitForElementVisible(PageBase.PickYourPathPage().AALExistingAccount);
        PageBase.PickYourPathPage().AALExistingAccount.click();

        PageBase.CommonControls().continueButtonDVA.click();
        PageBase.UECVerificationPage().fillVerizonDetails("5598466345", "4850", "HELLO", "94109");
        PageBase.UECVerificationPage().continueVerizonButton.click();

        Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().AALExistingFamilyPlan);
        PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
        PageBase.CommonControls().continueButtonDVA.click();

        PageBase.DeviceScanPage().enterDeviceScanDetails(esnNo1);//"78998767887645"
        //PageBase.VerizonEdgePage().declineVerizonEdge();
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
        PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().enterDeviceScanDetails(esnNo2);//"8976785467898633"
        //PageBase.VerizonEdgePage().declineVerizonEdge();
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
        PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

        Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);
        Utilities.ClickElement(PageBase.CarrierCreditCheckPage().skip);

        Utilities.ClickElement(PageBase.CartPage().continueCartButton);

        //PageBase.SelectPlanFeaturesPage().selectFamilyBasePlan(0);
        List<WebElement> networkList = BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'Network Access')]/ancestor::a"));
        //Selecting Network Access plan.
        networkList.get(0).click();
        driver.findElement(By.xpath("(//span[contains(text(),'Network Access')])[2]")).click();
        //PageBase.SelectPlanFeaturesPage().selectNetworkAccessPlan(1);
        List<WebElement> netwkList = BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'FamilyBase')]/ancestor::a"));
        //Selecting Family Base plan.
        netwkList.get(1).click();
        driver.findElement(By.xpath("(//span[contains(text(),'FamilyBase')])[4]")).click();
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().guestReview);
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForTwoDevices();

        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().numberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

		/*PageBase.PortMyNumbersPage().enterMultiplePortData(0, "9850045987", "Other", "572010311", "162364217",
                "Fred", "Consumer Two", "83144 O'Doula St.", "GARBAGY", "San Francisco", "CA",
                "94109");

		PageBase.PortMyNumbersPage().enterMultiplePortData(1, "9840345927", "Other", "572010311", "162364217",
				"Fred", "Consumer Two", "83144 O'Doula St.", "GARBAGY", "San Francisco", "CA",
				"94109");*/


        PageBase.PortMyNumbersPage().enterPortData(portDetails.CurrentPhoneNumber, portDetails.Carrier, portDetails.CurrentAccNumber, portDetails.SSN, customerDetails.FirstName, customerDetails.LastName, customerDetails.Address1, "", customerDetails.City, customerDetails.State, customerDetails.Zip);
        PageBase.PortMyNumbersPage().enterPortData(portDetails.CurrentPhoneNumber, portDetails.Carrier, portDetails.CurrentAccNumber, portDetails.SSN, customerDetails.FirstName, customerDetails.LastName, customerDetails.Address1, "", customerDetails.City, customerDetails.State, customerDetails.Zip);
        PageBase.CommonControls().continueButton.click();

	/*	PageBase.ServiceProviderVerificationPage().populatingSPVPage
                ("Fred", "", "Consumer Two", "nobody@letstalk.com", IdType.DRIVERLICENCE,
                        "CA", "123456789", Month.DECEMBER,
                        2020, "9850045987", "April", 11, 1970);*/
        PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, "", customerDetails.LastName, customerDetails.EMail, IdType.DRIVERLICENCE, customerDetails.City, customerDetails.IDNumber, Month.valueOf(customerDetails.IDExpirationMonth),
                Integer.parseInt(customerDetails.IDExpirationYear), customerDetails.PhNum, customerDetails.BirthdayMonth,
                Integer.parseInt(customerDetails.BirthdayDay), Integer.parseInt(customerDetails.BirthdayYear));
        Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

        // Terms and Condition Page.
        PageBase.TermsAndConditionsPage().acceptTermsAndConditions();

        Utilities.implicitWaitSleep(10000);
        if (driver.getCurrentUrl().contains("payment")) {

            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA); //ToDo: Need to read from data sheet.
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

        // MSS page.
        //ToDo: BAR Code verification after clarifying on the expected Bar code.
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);//"132710003003669460"
        PageBase.PaymentVerificationPage().submitButton.click();

        Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo1);//"78998767887645"
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
        // ToDo: Read from data sheet.
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo2);//"8976785467898633"
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();

        PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber1);//"21212121212121212121"
        PageBase.DeviceVerificationaandActivation().simType2Textbox.sendKeys(simNumber2);//"23232323232323232323"

        try {
            PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
        } catch (Exception e) {
        }
        PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();
        Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

        Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
        PageBase.OrderReceiptPage().verifyOrderCompletionPage();
        Assert.assertTrue(OrderActivationCompletePage().ActivationComplete.isDisplayed());

        //Check inventory Details
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().verifyDeviceStatus(esnNo1, esnNo1, "Sold");

        return orderId;
    }

    //endregion QA 5571

    //region QA 56
    private String QA_56_POAFlow(IMEIDetails imeiDetails1) throws InterruptedException, AWTException, IOException {
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0263"));

        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);

        //Click on the Sales and Activation Link
        Utilities.waitForElementVisible(PageBase.HomePageRetail().salesAndActivationsLink);
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Click on the new Activation Link
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().newActivation);
        PageBase.ChoosePathPage().newActivation.click();

        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails1.IMEI);
        //PageBase.VerizonEdgePage().declineVerizonEdge();
        Utilities.waitForElementVisible(PageBase.CommonControls().cancelButton);
        PageBase.CommonControls().cancelButton.click();

        Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

        //Skip the Carrier Credit Check.
        Utilities.ClickElement(PageBase.CarrierCreditCheckPage().skip);

        // Selecting Plan.
        Utilities.ClickElement(PageBase.VerizonShopPlansPage().VerizonMoreEverything);

        String cartDeviceprice = PageBase.CartPage().device1Price.getText();
        Utilities.ClickElement(PageBase.CartPage().continueCartButton);

        // Selecting plan feature.
        Utilities.ClickElement(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);

        // Selecting No Insurance .
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForSingleDevicesVerizonCarrier();
            Reporter.log("<br> Selected No Insurance for all devices");
        } catch (Exception ex) {
        }

        Utilities.waitForElementVisible(PageBase.NumberPortPage().continueSPVButton);
        PageBase.NumberPortPage().continueSPVButton.click();

        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().populateFormByClassButton);
        PageBase.ServiceProviderVerificationPage().populateFormByClassButton.click();

        PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, customerDetails.LastName,
                customerDetails.EMail, IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber,
                customerDetails.IDExpirationMonth, Integer.parseInt(customerDetails.IDExpirationYear));

        Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().continueSPVButton);
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        PageBase.CommonControls().continueCommonButton.click();

        //Accept the Terms and Conditions
        PageBase.TermsAndConditionsPage().acceptTermsAndConditions();

        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        //Device Verification Details
        Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
        PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);//"132710003003669460"
        PageBase.PaymentVerificationPage().submitButton.click();
        Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(imeiDetails1.IMEI);//"8799098765778"  // ToDo: Read from data sheet.
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
        PageBase.DeviceVerificationaandActivation().simType.sendKeys(imeiDetails1.Sim);//"21212121212121212121"
        // PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
        PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

        // Click on Accept Terms and Conditions
        Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox, 120);
        PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox.click();

        //Save Signature
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.TermsAndConditionsPage().saveSignatureButton.click();
        PageBase.TermsAndConditionsPage().continueTCButton.click();

        //Verify Order generated on Receipt Page
        Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
        PageBase.OrderReceiptPage().verifyOrderCompletionPage();
        PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, FileName.SamsungGalaxyS4_16GBBlack);

        //Check inventory Details
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, "Sold");//"8799098765778"
        Reporter.log("<br>Status on Inventory: " + "Device Sold");
        return orderId;
    }

    private void selectingCarrierEnviornment_56(@Optional String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Carrier Responder
            selectCarrierResponderQA_56();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
        }
    }

    private void selectCarrierResponderQA_56() throws AWTException, InterruptedException {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        // Selecting Verizon and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_no_edge.xml");
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_vd_300mb_2_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "default.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_1_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitpayment", "default.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

    }

    private void QA_56_VerifyShipadminDetails(String orderId) throws IOException {

        Reporter.log("<h3> Event Logger Verification: </h3>");
        ShipAdminBaseClass.launchShipAdminInNewTab();
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Reporter.log("<br>Status on Shipadmin: " + status);
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
    }
    //endregion QA 56

    private void QA_79ShipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog("39659");
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
        Assert.assertTrue(eventLogTableContent.contains(Constants.INQUIRING_NUMBER_PORT_ELIGIBILITY));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.NUMBER_PORTABILITY));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
    }

    //region QA-1734 Refactored Methods
    private void QA_1734_shipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.ORDER_CANCELLED_BY_USER);
    }

    private String QA_1734_CancelOrderOnWCAPage_PoaFlow(String orderId, String imei_QA1734, String sim) throws IOException, AWTException, InterruptedException {
        PageBase.CSVOperations();
        // This Method Contains POA flow for test case QA_69
        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations()
                .GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        // Login to retail application
        PageBase.LoginPageRetail().poaLogin(
                Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"),
                Utilities.getCredentials("storeId0003"));
        Utilities
                .ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);
        String session_id = BrowserSettings.driver.findElement(By.xpath(".//*[@id='retailPage']/small[2]/a")).getText();
        // Choose a Path Page - Selecting New Activation option
        Utilities.ClickElement(PageBase.ChoosePathPage().newActivation);

        // Device-Page - scanning the IMEI number
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei_QA1734);

        // Accept Edge
        Utilities
                .waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
        PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

        // Running Credit Check
        Utilities
                .waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(
                cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
            Log.error(e.getMessage());
        }

        // Verizon Edge Page
        Utilities
                .waitForElementVisible(PageBase.VerizonEdgePage().monthlyDownPayment);
        PageBase.VerizonEdgePage().monthlyDownPayment.click();
        PageBase.CartPage().continueCartButton.click();

        // Verizon Shop Plans Page
        Utilities.implicitWaitSleep(6000);
        ArrayList<WebElement> list = (ArrayList<WebElement>) driver.findElements(By.xpath("//h1[contains(text(),'Verizon Plan')]"));
        if (list.size() > 0) {
            Utilities.waitForElementVisible(list.get(0));
            list.get(0).click();
            PageBase.VerizonShopPlansPage().addPlan();
        } else {
            Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton);
            PageBase.VerizonShopPlansPage().verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton.click();
        }

        // Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        PageBase.CartPage().continueCartButton.click();

        Utilities.waitForElementVisible(PageBase
                .VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        // Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase
                .SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        // No Number Porting
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        Utilities
                .waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Utilities.ClickElement(PageBase.CommonControls().continueCommonButton);

        // Terms and Condition Page.
        PageBase.TermsAndConditionsPage().acceptTermsAndConditions();
        // Credit Card Payment Page
        Utilities.implicitWaitSleep(10000);
        if (driver.getCurrentUrl().contains("payment")) {
            PageBase.PaymentRequiredPage()
                    .populatingCardDetailsPaymentRequired(CardType.VISA);
            Utilities
                    .ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities
                    .ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

        // Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Utilities.implicitWaitSleep(5000);
        orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        Utilities.ClickElement(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);

        // Payment Verification page.
        PageBase.PaymentVerificationPage().paymentVerification(receiptId);

        // Device Verification and Activation page.
        PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei_QA1734, sim);
        PageBase.CommonControls().continueButtonDVA.click();

        // WCA Page User Cancels the order
        Utilities.waitForElementVisible(PageBase
                .WirelessCustomerAgreementPage().wcaCancelOrder);
        PageBase.WirelessCustomerAgreementPage().wcaCancelOrder.click();
        PageBase.WirelessCustomerAgreementPage().cancelOrderAlert(driver);
        Utilities.implicitWaitSleep(5000);

        // Verifying the user navigation to home page
        Assert.assertTrue((PageBase.HomePageRetail().salesAndActivationsLink
                .getText()).contains("Sales & Activations"));
        String session_id1 = BrowserSettings.driver.findElement(By.xpath(".//*[@id='retailPage']/small[2]/a")).getText();
        Assert.assertFalse(session_id.contains(session_id1));
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(
                FileName.SSNWithoutDeposit);
        PageBase.CSVOperations();
        CSVOperations.WriteToCSV("QA_1734", orderId, imei_QA1734, "", "",
                customerDetails.FirstName, customerDetails.LastName,
                customerDetails.EMail, receiptId, customerDetails.IDType,
                customerDetails.State, customerDetails.IDNumber,
                customerDetails.PhNum, customerDetails.Zip, ssn,
                customerDetails.IDExpirationMonth,
                customerDetails.IDExpirationYear);
        return orderId;
    }

    private void selectingCarrierEnviornment_QA_1734(String testType, String internalTestType) throws InterruptedException, AWTException, IOException {
        if (testType.contains("internal")) {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            // Selecting the Execution env either Backend or
            // CarrierResponder based on config file
            if (internalTestType.contains("BackendSimulator")) {
                QA_1734_BackendSimulatorSettings();
            } else {
                QA_1734_CarrierResponderSettings();
            }
        } else {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "external");
        }
    }

    private void QA_2639_CarrierResponderSettings() throws IOException {
        PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));
        // Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig(carrierType);
        Utilities.implicitWaitSleep(5000);
        // Customizing xml files in Carrier Responder
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_edge.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveNpaNxx", "single_area_code.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_v_1_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitReceipt", "default.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "success_edge.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "generateInstallmentContract", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveInstallmentDetailsForDevice", "eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "notsent_1line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);


    }

    //endregion QA-1734 Refactored Methods

    //region QA 55 refactored methods
    private String poaCompleteFlow_55(@Optional String testType, IMEIDetails imei1, IMEIDetails imei2) throws IOException {
        String orderId = "";//Login to retail page.
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        CreditCardDetails creditCard = new CreditCardDetails();
        creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
        lStartTime = new Date().getTime();
        pageName = readPageName("PoaLogin");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on Sales & Activations page.
        lStartTime = new Date().getTime();
        pageName = readPageName("SaleAndActivation");
        PageBase.HomePageRetail().salesAndActivationsLink.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on New Activation link.
        lStartTime = new Date().getTime();
        pageName = readPageName("DeviceScan");
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Scanning smart phone phone.
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1.IMEI);
        PageBase.VerizonEdgePage().declineVerizonEdge();
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei2.IMEI);
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        Reporter.log("Device has been scanned with IMEI1" + imei1.IMEI + "IMEI2:" + imei2.IMEI);
        PageBase.CommonControls().continueButtonDVA.click();

        //Filling information in Carrier Credit Check Page.
        crditCheck(PageBase.CarrierCreditCheckPage().skip);

        Utilities.webPageLoadTime(lStartTime, pageName);

        // Credit Check Verification Results
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDeposit);
        boolean exist = driver.findElements(By.id("checkbox-mini-1")).size() != 0;
        if (exist) {
            PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
            PageBase.CommonControls().continueButtonDeposit.click();
        }

        // Selecting Plan.
        Utilities.implicitWaitSleep(5000);
        ArrayList<WebElement> list = (ArrayList<WebElement>) driver.findElements(By.xpath("//h1[contains(text(),'Verizon Plan')]"));
        if (list.size() > 0) {
            Utilities.waitForElementVisible(list.get(0));
            list.get(0).click();
            PageBase.VerizonShopPlansPage().addPlan();
        } else {
            Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMorePlanOnly);
            PageBase.VerizonShopPlansPage().selectPlanWithMore();
            PageBase.VerizonShopPlansPage().addPlan();
            Reporter.log("<br> Added Plan to devices.");
        }

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Select Plan - Storing the Device and plan prices for further verification.
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        cartDevice2price = PageBase.CartPage().device2Price.getText();
        String monthlyRecurringFee = driver.findElement(By.xpath("//td[contains(text(),'Monthly Recurring Fee:')]/parent::tr/td[2]")).getText();
        String totalDueToday = PageBase.CartPage().totalDueToday.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();
        Reporter.log("<br> Selected Plan and feature");

        // Selecting No Insurance .
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectInsuranceForTwoDevices();
            Reporter.log("<br> Selected ESecurtel Insurance for all devices");
        } catch (Exception ex) {
        }

        // Redirecting to Credit check page.
        try {
            //crditCheck(PageBase.CarrierCreditCheckPage().skip);
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.CommonControls().continueButton.click();
        } catch (Exception ex) {
        }

        // Selecting No Number Porting.
        try {
            Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
            PageBase.NumberPortPage().noNumberPortRadiobutton.click();
            PageBase.CommonControls().continueButton.click();
            Reporter.log("<br> Selected No number porting");
        } catch (Exception ex) {
            Reporter.log("<br> No porting page is missing");
        }

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

//        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), cartDevice1price);
//        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device2PriceActual.getText(), cartDevice2price);
//        Assert.assertTrue(driver.findElements(By.xpath("//strong[contains(text(),'2 Year Contract')]")).size() > 0);
//
//        double device1Price = Double.parseDouble(cartDevice1price.substring(1));
//        double device2Price = Double.parseDouble(cartDevice2price.substring(1));
//
//        String totaldueToday = driver.findElement(By.xpath("(//h3[contains(text(),'Total Due Today:')]/parent::th/following-sibling::th)[2]/child::h3")).getText();
//        totaldueToday = totalDueToday.replace("inc. tax", "");
//
//        String device1Activation = driver.findElement(By.xpath("((//span[contains(text(),'One Time Activation Fee:')])[1]/following-sibling::span)[1]")).getText();
//        Assert.assertEquals("$40.00", device1Activation);
//
//        String device2Activation = driver.findElement(By.xpath("((//span[contains(text(),'One Time Activation Fee:')])[2]/following-sibling::span)[1]")).getText();
//        Assert.assertEquals("$40.00", device2Activation);
//
//        String mifiDeviceDeposit = driver.findElement(By.xpath("//td[contains(text(),'$20')]")).getText();
//        Assert.assertEquals("$20.00", mifiDeviceDeposit);
//        Reporter.log("<br> MIFI Deposit Fee: " + mifiDeviceDeposit);
//
//        String estimatedTotalDueMonthly = driver.findElement(By.xpath("(//h3[contains(text(),'Estimated Total Due Monthly:')]/parent::th/following-sibling::th)[2]/child::h3")).getText();
//        estimatedTotalDueMonthly = estimatedTotalDueMonthly.replace("+ tax", "");
//
//        String line1Fee = driver.findElement(By.xpath("(//span[contains(text(),'2 Year Contract')])[1]/parent::li/parent::div/parent::div/parent::td//following-sibling::td[1]")).getText();
//        String line2Fee = driver.findElement(By.xpath("(//span[contains(text(),'One Time Activation Fee')])[2]/parent::li/parent::div/parent::div/parent::td//following-sibling::td[1]")).getText();
//        String familyBase = driver.findElement(By.xpath("((//span[contains(text(),'FamilyBase')])[1]/following-sibling::span)[1]")).getText();
//
//        double line1Fees = Double.parseDouble(line1Fee.substring(1));
//        double line2Fees = Double.parseDouble(line2Fee.substring(1));
//        double familyBases = Double.parseDouble(familyBase.substring(1, 3));
//
//
//        Assert.assertEquals(line1Fees + line2Fees + familyBases, Double.parseDouble(estimatedTotalDueMonthly.substring(1).replace(" ", "")));
//        Assert.assertEquals(device1Price + device2Price, Double.parseDouble(totaldueToday.replace("$", "").replace(",", "")));
//
//        Reporter.log("<br> Line1 Fee: " + line1Fee);
//        Reporter.log("<br> Estimated Total due" +
//                " Monthly Fee: " + estimatedTotalDueMonthly);
//        Reporter.log("<br> Total due" +
//                " Total Device Fee: " + totaldueToday);
//        Reporter.log("<br> Device Price in Order Review and Confirmation Page Matches with Cart Page.");

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().continueTCButton);
            PageBase.TermsAndConditionsPage().emailTCChkBox.click();
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            Assert.assertTrue(driver.findElements(By.xpath("//h1[contains(text(),'Target Terms')]")).size() > 0);
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            // Credit card payment  page is coming.
            Utilities.implicitWaitSleep(5000);
            if (driver.getCurrentUrl().contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
            }

            // Print Mobile Scan Sheet.
            try {
                Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
                orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
                PageBase.PrintMobileScanSheetPage().verifyAllTwoDeviceBarCode();

                WebElement web = driver.findElement(By.xpath("//span[contains(text(),' 0003 - TARGET - CRYSTAL')]"));

                String mssdevice1Price = driver.findElement(By.xpath("(//tr[@class = 'mssmarginb'])[1]/child::td[2]")).getText();
                Assert.assertEquals(cartDevice1price, mssdevice1Price);

                String mssdevice2Price = driver.findElement(By.xpath("(//tr[@class = 'mssmarginb'])[2]/child::td[2]")).getText();
                Assert.assertEquals(cartDevice2price, mssdevice2Price);
            } catch (Exception ex) {
            }

            //Ship Admin Order Status Verification
            ShipAdminBaseClass.launchShipAdminInNewTab();
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            Assert.assertEquals(PageBase.OrderSummaryPage().getOrderStatus(), Constants.IN_STORE_BILLING);

            //Switching Back to Retail
            Utilities.switchPreviousTab();
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            String recieptID = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(recieptID);
            PageBase.PaymentVerificationPage().submitButton.click();
            Reporter.log("<br> Receipt ID Used: " + recieptID);

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor3Devices(imei1.IMEI, imei2.IMEI, "", imei1.Sim, imei2.Sim, "");
            Reporter.log("<br> IMEI and SIM scanned: " + imei1.IMEI + " " + imei2.IMEI);

            // WCA Signature page activity and verifications.
            Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
            Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);

            String termasAndConditionsContent = PageBase.WirelessCustomerAgreementPage().termsAndConditionsDiv.getText();
            Assert.assertTrue(termasAndConditionsContent.contains("Application ID No.:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Order Date:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Bill Acct. No.:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Activation Type:"));
            Assert.assertTrue(termasAndConditionsContent.contains("New"));
            Assert.assertTrue(termasAndConditionsContent.contains("Customer Information:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Home Phone:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Mobile Number:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Price Plan:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Plan Access Fee:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Line Access Fee:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Term:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Activation Date:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Activation Fee:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Customer Accepted:"));
            Assert.assertTrue(termasAndConditionsContent.contains("Customer Agreement version:"));
            Assert.assertTrue(termasAndConditionsContent.contains(Constants.DISCLAIMER_TEXT));
            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei1.IMEI, FileName.SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei2.IMEI, FileName.SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(recieptID, FileName.ReceiptId);

            // Order Activation Complete page.
            Utilities.waitForElementVisible(OrderActivationCompletePage().ActivationComplete);
            Reporter.log("<br> Order Activation Complete Page. ");

//            String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
//            Assert.assertEquals(orderId, orderIdfromActPage.substring(1));
//            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
//            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
//            Reporter.log("<h3><U>Order ha been confirmed, Order Id =" + orderId + "</U></h3>");
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), imei1.IMEI);
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValue2Text.getText(), imei2.IMEI);
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), imei1.Sim);
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValue2Text.getText(), imei2.Sim);
//            // Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValueText.getText(), cartDevice1price);
//            Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValue2Text.getText(), cartDevice2price);
//            Reporter.log("<br> Device Price matches with Cart Page and Order Review and Confirm Page: " + cartDevice1price);

            CSVOperations.WriteToCSV("QA_55", orderId, imei1.IMEI, "", "", customerDetails.FirstName, customerDetails.LastName,
                    customerDetails.EMail, receiptId, customerDetails.IDType, customerDetails.State,
                    customerDetails.IDNumber, customerDetails.PhNum, customerDetails.Zip, "",
                    customerDetails.IDExpirationMonth, customerDetails.IDExpirationYear);
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void selectingCarrierEnviornment_55(@Optional String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            //selectingBackendSimulatorForQA5574();

            //Selecting Carrier Responder
            selectCarrierResponderQA55();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
        }
    }

    private CarrierCreditCheckDetails getCarrierCreditCheckDetails(boolean deposit) throws IOException {
        CarrierCreditCheckDetails cccDetails = new CarrierCreditCheckDetails();
        PageBase.CSVOperations();
        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
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
        if (deposit) {
            cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SSNWithDeposit));
        } else {
            cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SSNWithoutDeposit));
        }
        cccDetails.setIDType(IdType.DRIVERLICENCE);
        cccDetails.setIdTypeState(customerDetails.IDState);
        cccDetails.setIdNumber(customerDetails.IDNumber);
        cccDetails.setMonth(customerDetails.IDExpirationMonth);
        cccDetails.setYear(customerDetails.IDExpirationYear);
        return cccDetails;
    }

    private void selectCarrierResponderQA55() throws AWTException, InterruptedException {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        // Selecting Verizon and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_no_edge.xml");
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_vd_300mb_2_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "default.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_2_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitpayment", "default.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }
    //endregion QA 55 refactored methods

    //region QA-69
    private void QA_69_BackendSimulatorSettings() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");
    }

    private void selectingCarrierEnviornment_QA_69(String testType, String internalTestType) throws InterruptedException, AWTException, IOException {
        if (testType.contains("internal")) {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            // Selecting the Execution env either Backend or
            // CarrierResponder based on config file
            if (internalTestType.contains("BackendSimulator")) {
                QA_69_BackendSimulatorSettings();
            } else {
                QA_69_carrierResponderSettings();
            }
        } else {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "external");
        }
    }

    private String QA_69_PoaFlow(@Optional String testtype, String phoneNumber, String ssn, String accountPassword, String zipCode, String imei, String sim) throws IOException, AWTException, InterruptedException {
        // This Method Contains POA flow for test case QA_69
        String orderId = null;
        String Reciptid = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        // Login to retail application
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);
        // Choose a Path Page - Selecting Existing Carrier option
        Utilities.ClickElement(PageBase.ChoosePathPage().existingCarrier);
        // Pick Your path and continue - Selecting Add a line to existing Account
        Utilities.ClickElement(PageBase.PickYourPathPage().AALExistingAccount);
        PageBase.CommonControls().continueButtonDVA.click();
        // Upgrade-Eligibility-Checker Page - Populating the User Details
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
        PageBase.UECVerificationPage().verizonTab.click();
        if (testtype.equals("internal")) {
            PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(Constants.DEFAULT_XML_NUMBER_8155491829);
        } else {
            PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(phoneNumber);
        }
        PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(ssn);
        PageBase.UECVerificationPage().accountPasswordVerizonTextbox.sendKeys(accountPassword);
        PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys(zipCode);
        PageBase.UECVerificationPage().continueVerizonButton.click();
        // Add-Line-type-Selection Page - Selecting Add a line to Existing Family Plan
        PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
        PageBase.CommonControls().continueButtonDVA.click();
        // Device-Page - scanning the IMEI number
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei);
        // Accept Edge
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
        PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();
        // Running Credit Check
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        lStartTime = new Date().getTime();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
        // Verizon Edge Page
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().monthlyDownPayment);
        PageBase.VerizonEdgePage().monthlyDownPayment.click();
        String monthlyDwnPymt = PageBase.VerizonEdgePage().downPaymentAmt.getText();
        String monthlyInstallment = PageBase.VerizonEdgePage().monthlyInstallment.getText();
        String devicePrice = PageBase.VerizonEdgePage().devicePriceWithFinance.getText();
        PageBase.CartPage().continueCartButton.click();
        // Cart Page verifying the
        PageBase.CartPage().continueCartButton.click();
        // Verizon Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();
        // Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance.click();
        Utilities.implicitWaitSleep(5000);
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();
        // Selecting Number Porting.
        Utilities.ClickElement(PageBase.NumberPortPage().numberPortRadiobutton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);
        PageBase.PortMyNumbersPage().populatePortData();
        Utilities.ClickElement(PageBase.CommonControls().continueCommonButton);
        // Terms and Condition Page.
        PageBase.TermsAndConditionsPage().acceptTermsAndConditions();
        // Credit Card Payment Page
        Utilities.implicitWaitSleep(10000);
        if (driver.getCurrentUrl().contains("payment")) {
            PageBase.PaymentRequiredPage()
                    .populatingCardDetailsPaymentRequired(CardType.VISA);
            Utilities
                    .ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities
                    .ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }
//	        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        System.out.println(orderId);
        Utilities.ClickElement(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        // Payment Verification page.
        PageBase.PaymentVerificationPage().paymentVerification(Reciptid);
        // Device Verification and Activation page.
        PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei, sim);
        PageBase.CommonControls().continueButtonDVA.click();
        // WCA Signature page.
        Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();
        // Order Activation Complete page.
        Utilities.implicitWaitSleep(10000);
        //Print the Details
        PageBase.DeviceFinancingInstallmentContractPage().PrintDeviceFinancingDetails(driver);

        //Verify that the Order is Successfully Created
        Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
        PageBase.OrderReceiptPage().verifyOrderCompletionPage();
        Assert.assertTrue(OrderActivationCompletePage().ActivationComplete.isDisplayed());
        //String orderId = PageBase.OrderSummaryPage().orderNumberText.getText().replace("#","");

        //Check inventory Details
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().verifyDeviceStatus(imei, "Sold");//"889988990007"
        return orderId;
    }

    private void QA_69_shipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
    }

    private void QA_69_carrierResponderSettings() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_edge.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCustomerDetails", "vwz_accountLookup_LLP_3lines_eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveNpaNxx", "single_area_code.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_v_1_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitReceipt", "default.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "success_edge.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_1_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "generateInstallmentContract", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveInstallmentDetailsForDevice", "eligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "validatePortInEligibility", "default_1_line.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitSigpadCaptureForInstallmentAgreement", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }
    //endregion QA-69 Refactored Methods

    //region QA-5006 Refactored Methods
    private void carrierResponderSettingsQA5006() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_no_edge.xml");
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_v_1_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "default.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_1_line.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitpayment", "default.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
    }

    private String poaFlowQA5006(String iMEINumber, String simNumber, String receiptId) throws IOException {
        String orderId;//POA FLOW
        Reporter.log("<h2> POA Flow Starts </h2>");
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
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
        PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().continueDSButton);
        PageBase.DeviceScanPage().continueDSButton.click();

        //Credit Check Page
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().firstNameTextBox);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SSNWithoutDeposit));
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        try {
            PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
        }

        //Credit Check Verification Results Page
        Utilities.waitForElementVisible( PageBase.CommonControls().continueCommonButton, 120);
        PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
        PageBase.CommonControls().continueCommonButton.click();

        //Verizon Shop Plans Page
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().firstPlanAddButton);
        PageBase.VerizonShopPlansPage().firstPlanAddButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        String phonePrice = PageBase.CartPage().device1Price.getText();
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
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().twoYearContractLabel1.isDisplayed());
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
            CreditCardDetails CreditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
            String cVNNumberS = "" + CreditCard.CVV;
            PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(cVNNumberS);
        } catch (Exception e) {
        }
        PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

        //Wireless Customer Agreement Page
        Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox, 120);
        PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox.click();
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        if (readConfig("Activation").toLowerCase().contains("true")) {
            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Reporter.log("<h3><U>Order ha been confirmed, Order Id =" + orderId + "</U></h3>");
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(iMEINumber, FileName.VerizonIPadAir16GB);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(receiptId, FileName.ReceiptId);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), simNumber);
            Reporter.log("<h2> POA Flow Finishes</h2>");
        }
        return orderId;
    }

    private void shipAdminVerificationsQA5006(String orderId) {
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
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
    }
    //endregion QA-5006 Refactored Methods
    //endregion Private Methods and Refacotored Codes
}
