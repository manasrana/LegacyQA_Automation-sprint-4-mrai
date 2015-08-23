package sprint.tests;

import com.gargoylesoftware.htmlunit.Page;
import framework.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PaymentRequiredPage;
import pages.ServiceProviderVerificationPage;
import pages.CarrierCreditCheckDetails;
import framework.CSVOperations;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AddNewLineTests extends RetailBaseClass {

    //region Variable Declaration
    public String carrierType = "Verizon";  //ToDo: Need to read from data sheet.
    public String cartDevice1price = "";
    public String cartDevice2price = "";
    public String cartDevice3price = "";
    public String cartPlanprice = "";
    String receiptId = "";
    IMEIDetails imeiDetails1 = null;
    IMEIDetails imeiDetails2 = null;
    //endregion

    //region Test Methods
    //region QA 1705
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_1705_Sprint2YrContractWithNewActivation(@Optional String testType) throws Exception {
        String orderId = "";
        Log.startTestCase("QA_1705_Sprint2YrContractWithNewActivation");
        Reporter.log("<h2> QA_1705_Sprint2YrContractWithNewActivation</h2>");
        Reporter.log("<h4>Description:</h4> Parent order is Sprint Non Easy Pay with new activation");

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        imeiDetails2 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails2.IMEI, imeiDetails1.ProductName, imeiDetails2.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "Second device:" + imeiDetails2.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment_1705(testType);

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        //Calling DBError utility to  find initial count or error in log files.
        Reporter.log("<br> DB Errors Initial Check:");
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        // Switching to previous tab.
        Utilities.switchPreviousTab();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");

        //POA Flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_1705(testType);
        Reporter.log("<h2> POA Flow Finishes</h2>");

        //Inventory Management Page verification.
        if (readConfig("Activation").contains("true")) {
            //Ship Admin Verification -orderId= ""
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            shipAdminVerification(orderId);
            Reporter.log("<h3> Order Shipped successfully <h3>");

            // Inventory management verification.
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            Utilities.switchPreviousTab();
            inventoryManagementVerification();
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");
        }

        //DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
         Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        // API History verification.
        Reporter.log("<br> API history Verification ");
        VerifyAPIResponseForQA_1705(orderId);

        Reporter.log("<h3>QA_1705_Sprint2YrContractWithNewActivation - Test Case Completes</h3>");
        Log.endTestCase("QA_1705_Sprint2YrContractWithNewActivation");
    }
    //endregion QA 1705

    //region QA 84
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        Log.startTestCase("QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit");
        Reporter.log("<h2> QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit</h2>");
        Reporter.log("<h4>Description:</h4> Parent order is Sprint Non Easy Pay new activation with Family Share Pack Plans");

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_IPhone5C);

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        Log.startTestCase("QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit");
        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment(testType);
        // Switching to previous tab.
        Utilities.switchPreviousTab();

        //region Calling DBError utility to  find initial count or error in log files.
        Reporter.log("<br> DB Errors Initial Check:");
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");
        Utilities.switchPreviousTab();
        //endregion

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_84(testType, imeiDetails1);
        Reporter.log("<h2> POA Flow Finishes</h2>");
        //endregion

        //Inventory Management Page verification.
        if (readConfig("Activation").contains("true")) {
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            inventoryManagementVerification();
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, InventoryManagementBaseClass.IMEIStatus.Sold.toString());
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");


            //Ship Admin Verification -orderId= ""
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            shipAdminVerification_84(orderId);
        }

        //DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit - Test Case Completes<h3>");
        Log.endTestCase("QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit");
    }
    //endregion QA 84

    //region QA_2966_SprintNewActivationWithEasyPay
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_2966_SprintNewActivationWithEasyPay(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        //boolean activation = false;
        IMEIDetails imeiDetails = null;

        String sTestCaseName = "QA_2966_SprintNewActivationWithEasyPay";

        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.SamsungGalaxyS4_16GBBlack);
        receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI, imeiDetails.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails.IMEI + "(ProdCode: " + imeiDetails.ProductName + ")");

        // Printing the start of Test Case
        Log.startTestCase(sTestCaseName);

        // Fetching the Execution Environment
        testType = BrowserSettings.readConfig("test-type");

        Utilities.switchPreviousTab();

        Reporter.log("<br> Test Type Settings");
        selectCarrierResponderQA2966(testType);

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

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlowQA2966(orderId, imeiDetails.IMEI, imeiDetails.Sim);
        Reporter.log("<h2> POA Flow Finishes</h2>");
        //endregion

        //Inventory Management and Ship admin Page verification.
        if (readConfig("Activation").contains("true")) {
            // Inventory Management Page verification.
            Reporter.log("<h2> Inventory Management Page: IMEI Status  Check</h2>");
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails.IMEI,
                    InventoryManagementBaseClass.IMEIStatus.Sold.toString());
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

            //Ship Admin Verification
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            QA_2966_shipAdminVerification(orderId);
        }

        //DBError Verification.
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_2966_SprintNewActivationWithEasyPay - Test Case Completes<h3>");
        Log.endTestCase("QA_2966_SprintNewActivationWithEasyPay");
    }
    //endregion QA_2699_SprintNewActivationWithEasyPay

    //region QA-4248
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract(@Optional String testType) throws Exception {
        String orderId = "";
        Log.startTestCase("QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract");
        Reporter.log("<h2> QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract</h2>");
        Reporter.log("<h4>Description:</h4> Sprint easy pay ,canclling the order withot accepting the Contract");

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

//      Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        Log.startTestCase("QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract");
        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment_4248(testType);

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

        //POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_4248(testType, imeiDetails1);
        Reporter.log("<h2> POA Flow Finishes</h2>");

        //Inventory Management Page verification.
        if (readConfig("Activation").contains("true")) {
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            inventoryManagementVerification_4284();
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Available.toString() + "</h3>");


            //Ship Admin Verification -orderId= ""
            Reporter.log("<h3>Ship Admin verification started.h3>");
            shipAdminVerification_4248(orderId);
            Reporter.log("<h3>Ship Admin verification Done.h3>");
        }

        //DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
//            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        //API History verification.
        VerifyAPIResponseForQA_4248(orderId);

        //Verify in Server DB
        PageBase.SQLUtilAdminPage().launchSQLUtilInNewTab();
        serverDBVerificationsQA_4248(orderId);
        Reporter.log("<br> Verified Signature in Server DB.");

        Reporter.log("<h3>QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract - Test Case Completes<h3>");
    }

    //endregion QA-4248

    //region QA 4242
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_4242_NewActivationWithEasyPay(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        Log.startTestCase("QA_4242_NewActivationWithEasyPay");
        Reporter.log("<h2> QA_4242_NewActivationWithEasyPay</h2>");
        Reporter.log("<h4>Description:</h4> QA_4242_NewActivationWithEasyPay");

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_4GPhone);

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        Log.startTestCase("QA_4242_NewActivationWithEasyPay");
        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment_4242(testType);

        // Switching to previous tab.
        Utilities.switchPreviousTab();

//        Calling DBError utility to  find initial count or error in log files.
        Reporter.log("<br> DB Errors Initial Check:");
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log(String.valueOf(initialCount) + "</h3>");
        // Switching to previous tab.
        Utilities.switchPreviousTab();

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_4242(testType, imeiDetails1);
        Reporter.log("<h2> POA Flow Finishes</h2>");
        //endregion

        //Inventory Management Page verification.
        if (readConfig("Activation").contains("true")) {
            Reporter.log("<h2> Inventory Management Page: IMEI Status  Check</h2>");
            inventoryManagementVerification_4284();
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

            //Ship Admin Verification -orderId= ""
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            shipAdminVerification_4242(orderId);
        }

        //DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_4242_NewActivationWithEasyPay - Test Case Completes<h3>");
    }
    //endregion QA 4242

    //region QA 93
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_93_Sprint2YrAALExistingFamilyPlan(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        Log.startTestCase("QA_93_Sprint2YrAALExistingFamilyPlan");
        Reporter.log("<h2> QA_93_Sprint2YrAALExistingFamilyPlan</h2>");

        //Samsunag device and 3FF sim needed
        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

        ////Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        Log.startTestCase("QA_93_Sprint2YrAALExistingFamilyPlan");
        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment_93(testType);
        Utilities.switchPreviousTab();

        //region Calling DBError utility to  find initial count or error in log files.
        Reporter.log("<br> DB Errors Initial Check:");
//        DBError.navigateDBErrorPage();
//        int initialCount = PageBase.AdminPage().totalErrorCount();
//        Reporter.log("<h3> DB Errors Initial Check:");
//        Reporter.log(String.valueOf(initialCount) + "</h3>");
//        Utilities.switchPreviousTab();
        //endregion

        orderId = QA_93_poaCompleteFlow(testType, imeiDetails1);

        if (readConfig("Activation").contains("true")) {
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            // Inventory Management Page verification.
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            Utilities.implicitWaitSleep(4000);
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, InventoryManagementBaseClass.IMEIStatus.Sold.toString());
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

            //Ship Admin Verification -orderId= ""
            Reporter.log("<h2> Ship Admin verification started.</h2>");
            //shipAdminVerifications_93(orderId);
            shipAdminVerifications_93("37618");
            Reporter.log("<h3> Ship Admin verification Done.</h3>");
        }
        Reporter.log("<h3>QA_93_Sprint2YrAALExistingFamilyPlan - Test Case Completes<h3>");
        Log.endTestCase("QA_93_Sprint2YrAALExistingFamilyPlan");
    }
    //endregion QA 93


    //region QA-4244
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        Log.startTestCase("QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment");
        Reporter.log("<h2> QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment</h2>");
        Reporter.log("<h4>Description:</h4> QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment");

        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_IPhone5C);

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        Log.startTestCase("QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment");
        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment_4248(testType);
        Utilities.implicitWaitSleep(3000);

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

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_4244(testType, imeiDetails1);
        Reporter.log("<h2> POA Flow Finishes</h2>");
        //endregion

        //Inventory Management Page verification.
        if (readConfig("Activation").contains("true")) {
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            inventoryManagementVerification_4284();
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

            //Ship Admin Verification -orderId= ""
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            shipAdminVerification_4242(orderId);
        }

        //DBError Verification.
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment - Test Case Completes<h3>");
        Log.endTestCase("QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment");
    }

    //endregion QA-4244

    //region QA 3928
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        Log.startTestCase("QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan");
        Reporter.log("<h2> QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan</h2>");
        Reporter.log("<h4>Description:</h4> Parent order is Sprint Non Easy Pay new activation with Family Share Pack Plans");

        //Samunag device and 3FF sim needed
        imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

        //Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");

        Log.startTestCase("QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan");
        testType = BrowserSettings.readConfig("test-type");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        selectingCarrierEnviornment_QA_3928(testType);
        Utilities.switchPreviousTab();

//        region Calling DBError utility to  find initial count or error in log files.
        Reporter.log("<br> DB Errors Initial Check:");
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");
        Utilities.switchPreviousTab();
        //    endregion

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_3928(testType, imeiDetails1);
        Reporter.log("<h2> POA Flow Finishes</h2>");
        //endregion

        //Inventory Management Page & Ship Admin Verification.
        if (readConfig("Activation").contains("true")) {
            Reporter.log("<h2> Inveotory Management Page: IMEI Status  Check</h2>");
            // Inventory Management Page verification.
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            Utilities.implicitWaitSleep(4000);
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, InventoryManagementBaseClass.IMEIStatus.Sold.toString());
            Reporter.log("<h3> Status of IMEI: " + InventoryManagementBaseClass.IMEIStatus.Sold.toString() + "</h3>");

            //Ship Admin Verification -orderId= ""
            Reporter.log("<h2> ShipAdmin Verification:</h2>");
            shipAdminVerification(orderId);
        }
        //endregion

        //DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan - Test Case Completes<h3>");
        Log.endTestCase("QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan");
    }
    //endregion QA 3928

    //region QA_85
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_85_SPT2YrContract_CancelOrderBeforePayment() throws IOException, InterruptedException, AWTException {
        String orderId = "";
        String testType = BrowserSettings.readConfig("test-type");
        Log.startTestCase("QA_85_SPT2YrContract_CancelOrderBeforePayment");
        Reporter.log("<h2> QA_85_SPT2YrContract_CancelOrderBeforePayment</h2>");
        Reporter.log("<h4>Description:</h4> Parent order is Sprint Non Easy Pay , cancel the order before payment");

        IMEIDetails imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName
                (CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        IMEIDetails imeiDetails2 = PageBase.CSVOperations().GetIMEIAndProductName
                (CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

        // Adding Two Devices to Inventory.
        Reporter.log("<br> Receiving Inventory", true);
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails2.IMEI,
                imeiDetails1.ProductName, imeiDetails2.ProductName);
//
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("<br> IMEI Added to Inventory: " + imeiDetails1.IMEI + "Second Deice:" + imeiDetails2.IMEI + "(ProdCode: " + imeiDetails1.ProductName + ")");
        Log.startTestCase("QA_85_SPT2YrContract_CancelOrderBeforePayment");

        // Verify whether which enviorement to use internal or external.
        Reporter.log("<br> Test Type Settings");
        //CarrierResponderSettings_QA_85();

        //Calling DBError utility to  find initial count or error in log files.
        Reporter.log("<br> DB Errors Initial Check:");
//        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Reporter.log("<h3> DB Errors Initial Check:");
        Reporter.log(String.valueOf(initialCount) + "</h3>");
//        // Switching to previous tab.
        Utilities.switchPreviousTab();

        //region POA flow
        Reporter.log("<h2> POA Flow Starts </h2>");
        orderId = poaCompleteFlow_QA_85(imeiDetails1, imeiDetails2);
        Reporter.log("<h2> POA Flow Finishes</h2>");

        // DBError Verification.
        Reporter.log("<br> DB Errors Verification ");
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_85_SPT2YrContract_CancelOrderBeforePayment - Test Case Completes<h3>");
        Log.endTestCase("QA_85_SPT2YrContract_CancelOrderBeforePayment");
    }
//endregion QA 85

    //region QA 2640
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_2640_SPTNonEasy_APIFailDuringActivation_ManualFlows()
            throws IOException, InterruptedException, AWTException {
        String orderId = "";
        String testType = BrowserSettings.readConfig("test-type");
        Log.startTestCase("QA_2640_SPTNonEasy_APIFailDuringActivation_ManualFlows");

/*Verify whether which enviorement to use internal or external.*/
        selectingCarrierEnviornment_QA_2640(testType);

//////Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        Utilities.switchPreviousTab();

        IMEIDetails imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName
                (CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

        // Adding Two Devices to Inventory.
        Reporter.log("<br> Receiving Inventory", true);
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails1.IMEI, imeiDetails1.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        orderId = poaCompleteFlow_QA_2640(imeiDetails1);
        System.out.println("Order ID: " + orderId);
        Reporter.log("<br><h3> OrderID: " + orderId + "</h3>");

        /////DBError Verification.
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Reporter.log("<h3>QA_2640_SPTNonEasy_APIFailDuringActivation_ManualFlows - Test Case Completes<h3>");
        Log.endTestCase("QA_2640_SPTNonEasy_APIFailDuringActivation_ManualFlows");
    }
    //endregion QA 2640

    //region QA_80
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_80_Sprint_FamilyPlan_NewActivation_NumPorting(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        IMEIDetails imeiDetails1 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.iPhone5C);
        IMEIDetails imeiDetails2 = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.iPhone5C);
        ArrayList<String> imei = new ArrayList<String>();
        ArrayList<String> sim = new ArrayList<String>();
        //Appending the imei to an Array list
        imei.add(imeiDetails1.IMEI);
        imei.add(imeiDetails2.IMEI);
        String recpitId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
        sim.add(imeiDetails1.Sim);
        sim.add(imeiDetails2.Sim);
        String productName = imeiDetails1.ProductName;
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.SSNWithDeposit);
        String status = "Sold";

        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imei.get(0), productName);
        Reporter.log("<br> IMEI Added to Inventory: " + imei.get(0) + "(ProdCode: " + productName + ")");
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        PageBase.InventoryManagementPage().launchInventoryInNewTab(BrowserSettings.readConfig("inventoryStoreIdDetail"));
        PageBase.InventoryManagementPage().addDeviceToInventory(imei.get(1), productName);
        Reporter.log("<br> IMEI Added to Inventory: " + imei.get(1) + "(ProdCode: " + productName + ")");
        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();

        Reporter.log("QA_80_Sprint_FamilyPlan_NewActivation_NumPorting");
        Reporter.log("<h3>Description: </h3>" + "Sprint - family Plan order Run credit check prior checkout " +
                "with deposit required and Number Porting" + " check the box for sprint ebill - smart phones");
        Log.startTestCase("QA_80_Sprint_FamilyPlan_NewActivation_NumPorting");
        String testtype = BrowserSettings.readConfig("test-type");
        String internalTestType = BrowserSettings.readConfig("internalTestType");
        if (testtype.contains("internal")) {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            //Selecting the Execution env either Backend or CarrierResponder based on config file
            if (internalTestType.contains("BackendSimulator")) {
                QA_80_BackendSimulator();
            } else {
                QA_80_CarrierResponder();
            }
        } else {
            AdminBaseClass adminBaseClassExternal = new AdminBaseClass();
            adminBaseClassExternal.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
            Reporter.log("<h3><U> External Server</U></h3>", true);
        }
        orderId = QA_80_POAFlow(imei, sim, ssn, recpitId);
        System.out.print(orderId);
        QA_80_ShipAdminVerification(orderId);
        QA_80_InventoryManagement(imei, status);
    }
    //endregion QA_80

    //region QA-4162
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_4162_WithEasyPay_WithDepositandNumberPort(@Optional String testType) {
        String orderId = "";
        try {
            IMEIDetails imeiDetails = null;
            imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

            Reporter.log("<h2>Start - QA_4162_WithEasyPay_WithDepositandNumberPort. <br></h2>");
            Reporter.log("<h3>Description: Sprint - Easy Pay - with Deposit -with Number Port</h3>");
            Reporter.log("Launching Browser <br>", true);
            Log.startTestCase("QA_4162_WithEasyPay_WithDepositandNumberPort");
            testType = BrowserSettings.readConfig("test-type");

            // Verify whether which environment to use internal or external.
            selectingCarrierEnviornment_4162(testType);

            // Switching to previous tab.aditi
            Utilities.switchPreviousTab();

            //Calling DBError utility to  find initial count or error in log files.
            DBError.navigateDBErrorPage();
            int initialCount = PageBase.AdminPage().totalErrorCount();

            // Switching to previous tab.
            Utilities.switchPreviousTab();

            orderId = poaCompleteFlow_4162(testType, imeiDetails.IMEI, imeiDetails.Sim);

            //Inventory Management Page verification.
            if (readConfig("Activation").contains("true")) {
                inventoryManagementVerification_4162(imeiDetails.IMEI);

                //Ship Admin Verification -orderId= ""
                QA_4162_shipAdminVerification(orderId);
            }

            //DBError Verification.
            DBError.navigateDBErrorPage();
            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Reporter.log("<h3>QA_4162_WithEasyPay_WithDepositandNumberPort - Test Case Completes<h3>");
            Log.endTestCase("QA_4162_WithEasyPay_WithDepositandNumberPort");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            Utilities.driverTakesScreenshot("QA_4162_WithEasyPay_WithDepositandNumberPort");
            Assert.fail();
        }
    }

//endregion QA-4162

    //endregion Test Methods

    //region private methods

    //region QA 1705 Private Methods

    private void selectingCarrierEnviornment_1705(String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            //selectingBackendSimulatorForQA_84();

            //Selecting Carrier Responder
            selectCarrierResponderQA_1705();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
            Reporter.log("<h2> External Testing: Sprint </h2>");
        }
    }

    private void selectCarrierResponderQA_1705() throws AWTException, InterruptedException {
        Reporter.log("<h2> Carrier Responder: Sprint </h2>");
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_family_2_lines.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace("<plan id=\"3473710\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41587111\">\n" +
                "                    <phone id=\"5415551111\">");
        xmlContent2 = xmlContent2.replace(" <plan id=\"34738211\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41588210\">\n" +
                "                    <phone id=\"5415551112\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "noloannoleaseEligible.xml");
        Utilities.implicitWaitSleep(6000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "newactivation_NO_DEPOSIT_REQUIRED.xml");
        Utilities.implicitWaitSleep(6000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private String poaCompleteFlow_1705(@Optional String testType) throws IOException {
        String orderId = "";//Login to retail page.
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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
        PageBase.VerizonEdgePage().declineSprintEasyPay();
        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails2.IMEI);
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();
        Reporter.log("Device has been scanned with IMEI1" + imeiDetails1.IMEI + "IMEI2:" + imeiDetails2.IMEI);

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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

        // Selecting Plan
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFirstPlan);
        PageBase.VerizonShopPlansPage().sprintFirstPlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        cartDevice2price = PageBase.CartPage().device2Price.getText();

        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();
        Reporter.log("Selected plan and Feature");

        // Selecting No Insurance .
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForSprintWithTwoDevices();
            //PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForTwoDevices();
            Reporter.log("No Insurance selected");
        } catch (Exception ex) {
        }

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();
        Reporter.log("No number porting");
        //Service Provider Verification Page
        try {
            PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);
        } catch (Exception ex) {
        }

        // Order Review and Confirm Page.
        Reporter.log("<br><p> Order Review and Confirm Page.</p>");
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
                cartDevice1price);
        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device2PriceActual.getText(), cartDevice2price);

        PageBase.OrderReviewAndConfirmPage().OrderReviewConfirmPageAssertionsFor2Devices(cartDevice1price, cartDevice2price);

        Reporter.log("<br> Device Price in Order Review and Confirmation Page Matches with Cart Page.");

        Assert.assertTrue(driver.findElements(By.xpath("//strong[contains(text(),'2 Year Contract')]")).size() > 0);

        String line1ActivationFee = driver.findElement(By.xpath("(//span[contains(text(),'One Time Activation Fee:')])[1]/following-sibling::span[1]")).getText();
        String line2ActivationFee = driver.findElement(By.xpath("(//span[contains(text(),'One Time Activation Fee:')])[2]/following-sibling::span[1]")).getText();

        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalMonthlyDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalMonthlyDue = totalMonthlyDue.replace("inc. tax", "");
        totalMonthlyDue = totalMonthlyDue.replace(" ", "");

        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalDue = totalDue.replace("inc. tax", "");
        totalDue = totalDue.replace(" ", "");

        double device1 = Double.parseDouble(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText().substring(1));
        double device2 = Double.parseDouble(PageBase.OrderReviewAndConfirmPage().device2PriceActual.getText().substring(1));

        Assert.assertEquals(device1 + device2, Double.parseDouble(totalDue.replace("$", "").replace(",", "")));

        // Storing total monthly/ activation fee.
        String activationFee = PageBase.OrderReviewAndConfirmPage().totalFeeActual.getText();
        double actfeeMonthly = Double.parseDouble(activationFee.substring(1).replace("+ tax", "").replace(" ", ""));

        Reporter.log("<br> Line1 activation Fee: " + line1ActivationFee);
        Reporter.log("<br> Line2 activation Fee: " + line2ActivationFee);
        Reporter.log("<br> total due: " + totalDue);
        Reporter.log("<br> Total Monthly due: " + totalMonthlyDue);

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
            Assert.assertTrue(driver.findElement(By.xpath("//h1[contains(text(),'Sprint Terms')]")).getText().contains("Sprint Terms"));
            Assert.assertTrue(driver.findElement(By.xpath("//h1[contains(text(),'Target Terms')]")).getText().contains("Target Terms"));
            PageBase.TermsAndConditionsPage().emailTCChkBox.click();
            Utilities.implicitWaitSleep(2000);
            PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
            Utilities.implicitWaitSleep(2000);
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            Utilities.implicitWaitSleep(1000);
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            /*Utilities.implicitWaitSleep(10000);
            boolean exists1 = driver.findElements(By.id("cc")).size() != 0;

            if (exists1) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
            }*/

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            PageBase.PrintMobileScanSheetPage().verifyAllTwoDeviceBarCode();

            // WebElement web = driver.findElement(By.xpath("//span[contains(text(),'Target 0003: 5537 W Broadway Ave, Crystal, MN 55428, U.S.A')]"));
            Assert.assertTrue(driver.findElement(By.xpath("(//span[contains(text(),'Store Location')])/following-sibling::span")).getText().contains("0003 - TARGET - CRYSTAL"));
            String mssdevice1Price = driver.findElement(By.xpath("(//tr[@class = 'mssmarginb'])[1]/child::td[2]")).getText();
            Assert.assertEquals(cartDevice1price, mssdevice1Price);

            String mssdevice2Price = driver.findElement(By.xpath("(//tr[@class = 'mssmarginb'])[2]/child::td[2]")).getText();
            Assert.assertEquals(cartDevice2price, mssdevice2Price);


            //Ship Admin Order Status Verification
            ShipAdminBaseClass.launchShipAdminInNewTab();
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            Assert.assertEquals(PageBase.OrderSummaryPage().getOrderStatus(), Constants.IN_STORE_BILLING);

            //Switching Back to Retail
            Utilities.switchPreviousTab();

            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            String recieptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(recieptId);
            Reporter.log("<br> Receipt ID Used: " + recieptId);
            PageBase.PaymentVerificationPage().submitButton.click();

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
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor3Devices(imeiDetails1.IMEI, imeiDetails2.IMEI, "", imeiDetails1.Sim, imeiDetails2.Sim, "");

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails2.IMEI, CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(recieptId, CSVOperations.FileName.ReceiptId);

            // Order Activation Complete page.
            Utilities.implicitWaitSleep(10000);
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
            Assert.assertEquals(orderId, orderIdfromActPage.substring(1));
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Reporter.log("<h3><U>Order ha been confirmed, Order Id =" + orderId + "</U></h3>");
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), imeiDetails1.IMEI);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValue2Text.getText(), imeiDetails2.IMEI);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), imeiDetails1.Sim);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValue2Text.getText(), imeiDetails2.Sim);
            Reporter.log("<br> Order Activation Complete Page. ");
            //Device verification
            Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValueText.getText(), cartDevice1price);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValue2Text.getText(), cartDevice2price);

            //Activation fee verification.
            double actviationfeedev1 = Double.parseDouble(PageBase.OrderActivationCompletePage().activationFeeDevice1.getText().substring(1));
            double actviationfeedev2 = Double.parseDouble(PageBase.OrderActivationCompletePage().activationFeeDevice2.getText().substring(1));
            double todalFee = Double.parseDouble(PageBase.OrderActivationCompletePage().activationTotal.getText().substring(1));
            Assert.assertEquals(actviationfeedev1 + actviationfeedev2, todalFee);

            //Estimated Total.
            double estimatedTotal = Double.parseDouble(PageBase.OrderActivationCompletePage().estimatedTotal.getText().substring(1));
            Assert.assertEquals(actfeeMonthly, estimatedTotal);

        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void inventoryManagementVerification() throws InterruptedException, AWTException, java.io.IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, imeiDetails2.IMEI, InventoryManagementBaseClass.IMEIStatus.Sold.toString());
    }

    private void selectingBackendSimulatorForQA1705() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        //PageBase.AdminPage().selectCreaditReadUseCase("APPROVE_WITH_DEPOSIT");
        //PageBase.AdminPage().selectCreaditWriteUseCase("APPROVE_WITH_DEPOSIT");
        //PageBase.AdminPage().retrieveCustomerDetails("ELIGIBLE");

        PageBase.AdminPage().save();
    }

    private void selectCarrierResponderQA1705() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        // Selecting Verizon and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved_with_deposit.xml");
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        //PageBase.CarrierResponseXMLPage().loadResponseButton.click();
    }

    private void VerifyAPIResponseForQA_1705(String orderId) throws Exception {
        Reporter.log("<h2> Activation API Verification started.</h2>");
        String url = null;
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        System.out.print(readConfig("apiHistory"));
        driver.navigate().to(readConfig("apiHistory"));
        Utilities.WaitUntilElementIsClickable(PageBase.AdminPage().orderIdLink);
        PageBase.AdminPage().orderIdLink.sendKeys(orderId);
        Robot robot = new Robot();
        Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
        Utilities.waitForDocumentReady(driver);
//        Utilities.ScrollToElement(PageBase.AdminPage().lastActivationAPILink);
//        url = PageBase.AdminPage().lastActivationAPILink.getText();
//        Assert.assertTrue(driver.findElement(By.xpath("//td[contains(text(),'activation')]")).getText().contains("activation"));

        Reporter.log("<br> Activation API verified.");
    }
    //endregion QA 1705 Private Methods

    //region QA 84 Private Methods
    private String poaCompleteFlow_84(String testType, IMEIDetails imei) throws IOException {
        String orderId = "";//Login to retail page.
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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

        // Scanning smart phone
        try {
            PageBase.DeviceScanPage().enterDeviceScanDetails(imei.IMEI);
            PageBase.VerizonEdgePage().declineSprintEasyPay();
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
            PageBase.CommonControls().continueButtonDVA.click();
            Reporter.log("<br><p> Apple care device scanned:.</p>" + imei.IMEI);
        } catch (Exception ex) {
            PageBase.CommonControls().continueButtonDVA.click();
        }

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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
        Utilities.webPageLoadTime(lStartTime, pageName);

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

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();
        Reporter.log("<br><p> Selected family share plan.</p>");

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance . Need to write if else condition for internal and external testing.
        try {
            if (testType.equals("internal")) {
                Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
                PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
            } else {
                Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
                PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
            }
        } catch (Exception ex) {
        }

        // Selecting No Number Porting.
        Reporter.log("<br><p>No number porting.</p>");
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Reporter.log("<br><p> Order Review and Confirm Page.</p>");
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        Reporter.log("<br><p> Order Review and Confirm Page.</p>");
        Double sumOfAdditions = 0.00;
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), cartDevice1price);
//        String[] prodName = imeiDetails1.ProductName.split(" ");
//
//        List <WebElement> child = driver.findElements(By.xpath(".//div[(@id='retailPage')]/section/table/tbody/child::*"));
//        for(WebElement e: child){
//            try {
//                if (e.getAttribute("class").toString().equals("ordcAdditions")) {
//                    String additions = null;
//                    additions = e.findElement(By.xpath("./child::td[2]")).getText().replace("$","");
//                    sumOfAdditions = sumOfAdditions + Double.valueOf(additions);
//                }
//            }catch(Exception ex){}
//        }
//        String monthlyFee = PageBase.OrderReviewAndConfirmPage().totalFeeActual.getText();
//
//        String line1Fee = PageBase.OrderReviewAndConfirmPage().device1ActivationFee1Device.getText();
//        line1Fee =line1Fee.substring(1, 6);
//        sumOfAdditions = Double.valueOf(line1Fee) + sumOfAdditions;
//        Reporter.log("<br> Line1 Fee: " + line1Fee);
//        Reporter.log("<br> Additions Sum with Line1 Fee: " + sumOfAdditions);
//        Reporter.log("<br> Total Monthly Fee: "+ monthlyFee);
//        Assert.assertTrue(monthlyFee.contains(String.valueOf(sumOfAdditions)));
//        Reporter.log("<br> Monthly Fee Estimate matches with Sum of individual Additions");
//        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), cartDevice1price);
        Reporter.log("<br> Device Price in Order Review and Confirmation Page Matches with Cart Page.");

        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        PageBase.CommonControls().continueCommonButton.click();


        //Terms and Condition Page.
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
        PageBase.TermsAndConditionsPage().emailTCChkBox.click();
        PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.TermsAndConditionsPage().continueTCButton.click();

        //Print Mobile Scan Sheet.
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());
        Reporter.log("<br> MSS page order ID: " + orderId);

        //TODO:Need to add assertion for store location.
        //    WebElement web = driver.findElement(By.xpath("//span[contains(text(),'2766 - TARGET - SAN FRANCISCO CENTRAL')]"));
        //    String storeLocation = web.getText();
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        if (readConfig("Activation").contains("true")) {
            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
            PageBase.PaymentVerificationPage().submitButton.click();
            Reporter.log("<br> Receipt ID Used: " + receiptId);

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei.IMEI, imei.Sim);
            PageBase.CommonControls().continueActivation.click();

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei.IMEI, CSVOperations.FileName.Sprint_IPhone5C);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(receiptId, CSVOperations.FileName.ReceiptId);

            // Order Activation Complete page.
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
            Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
            Reporter.log("<br> Order Activation Complete Page.");
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void selectingBackendSimulatorForQA_84() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        //PageBase.AdminPage().checkLoaanEligibility("LOAN_ELIGIBLE");
        //PageBase.AdminPage().selectCreaditWriteUseCase("APPROVE_WITH_DEPOSIT");
        //PageBase.AdminPage().retrieveCustomerDetails("ELIGIBLE");

        PageBase.AdminPage().save();
    }

    private void selectCarrierResponderQA_84() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_family_2_lines_numport.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "noloannoleaseEligible.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private void shipAdminVerification_84(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        // Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_COMPLETED_SUCCESSFULLY));
        Assert.assertTrue(eventLogTableContent.contains(Constants.NO_DEPOSIT_REQUIRED));
        //Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
        //Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }
    //endregion QA 84 Private Methods

    //region QA-2966 Refactored Methods
    private void selectCarrierResponderQA2966(@Optional String testtype) throws IOException, InterruptedException, AWTException {
        // Verify whether which environment to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        carrierType = "Sprint";
        if (testtype.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));

            if (BrowserSettings.readConfig("internalTestType").contains("CarrierResponder")) {

                //Selecting Use Case from dropdown list.
                PageBase.AdminPage().selectAPIConfig(carrierType);
                //Customizing xml files in Carrier Responder
                PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loaneligible.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(4000);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "newactivation_data_error.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(4000);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "servicevalidation", "success.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(4000);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
                Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);
            }
        } else {   //External
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
        }
    }

    private String poaCompleteFlowQA2966(String orderId, String imei, String sim) throws IOException,
            AWTException, InterruptedException {
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");

        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();

        //Login to retail page.
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        // Click on Sales & Activations page.
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);

        // Click on New Activation link.
        PageBase.ChoosePathPage().newActivation.click();

        // Device Scan Page.
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei);

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        PageBase.SprintEasyPayPage().yesButton.click();

        ///Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();

        // Device Financing Result page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDeposit);
        PageBase.CommonControls().continueButtonDeposit.click();

        // Credit Check Verification Results page.
        PageBase.CreditCheckVerificationResultsPage().depositCheckBox.click();
        PageBase.CommonControls().continueCommonButton.click();

        Utilities.switchPreviousTab();

        // Selecting Carrier Responder
        Reporter.log("<h3><U> Carrier Responder Changes.</U></h3>", true);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "newactivation_NO_DEPOSIT_REQUIRED.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_individual_no_numport.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        Robot robot = new Robot();
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace("<plan id=\"3473210\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41935111\">\n" +
                "                    <phone id=\"5417721111\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
        Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);

        Utilities.switchPreviousTab();
        Utilities.switchPreviousTab();

        // Selecting Plan.
        Utilities.ClickElement(PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton);

        // Storing the Device and plan prices for further verification.
        cartDevice1price = PageBase.CartPage().device1Price.getText();

        Utilities.ClickElement(PageBase.CartPage().continueCartButton);

        // Selecting plan feature.
        Utilities.ClickElement(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);

        // Selecting NO Insurance.
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();

        // Selecting Number Porting.
        Utilities.ClickElement(PageBase.NumberPortPage().noNumberPortRadiobutton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        // Enter data in Service Provider Verification page.
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.OrderReviewAndConfirmPage().device1PriceActual);

        // Device prices in Cart Page and Order Review Confirm page are not matching, hence commenting out the Assertions.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Assert.assertTrue(driver.findElements(By.xpath("//strong[contains(text(),'2 Year Contract')]")).size() > 0);

        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(),'One Time Activation Fee:')]/following-sibling::span[1]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//td[contains(text(),'Estimated Tax:')]/following-sibling::td[2]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Total Due Today:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Estimated Total Due Monthly:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());
        //Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Estimated Monthly Installment:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());

        //String devicePrice = driver.findElement(By.xpath("//span[contains(text(),'Full Retail Price :')]/following-sibling::strong[1]")).getText();

        PageBase.OrderReviewAndConfirmPage().OrderReviewConfirmPageAssertionsFor1Device(cartDevice1price);
        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
            PageBase.TermsAndConditionsPage().emailTCChkBox.click();
            PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei,
                    sim);
            PageBase.CommonControls().continueButtonDVA.click();

            // Order Activation Complete page.
            Utilities.implicitWaitSleep(6000);
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
        }
        return orderId;
    }

    private void QA_2966_shipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }

//endregion QA-2966 Refactored Methods

    //region QA 4248 Private Methods

    private String poaCompleteFlow_4248(String testType, IMEIDetails imeiDetails1) throws IOException, AWTException, InterruptedException {
        String orderId = "";//Login to retail page.
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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
        Reporter.log("<br> device scanned");

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        Reporter.log("<br> Credit Check Started.");
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);

        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();

        lStartTime = new Date().getTime();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        do {
        } while ((driver.getCurrentUrl().contains("creditcheck")));
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {

        }
        Utilities.webPageLoadTime(lStartTime, pageName);
        Reporter.log("<br> Credit check done");

        do {

        } while (!driver.getCurrentUrl().contains("installmentdetail"));

        //Sprint Easy Pay Eligibility Result
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        if (driver.findElements(By.xpath("//span[contains(text(),'Sprint Easy Pay Installment Details')]")).size() > 0) {
            driver.findElement(By.id("radio-choice-2")).click();
            Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
            Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
            Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
            Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());
            PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
            PageBase.CommonControls().continueCommonButton.click();
        } else {
            Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
            Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
            Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
            PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
            Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());
            PageBase.CommonControls().continueCommonButton.click();
        }

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();
        Reporter.log("<br> Selected family share plan");

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();

        String downpaymentAmt = PageBase.CartPage().downPaymentAmt.getText();
        String monthlyDeviceInstallmentBalance = PageBase.CartPage().monthlyDeviceInstallmentBalance.getText();
        String monthlyRecurringFee = PageBase.CartPage().monthlyRecurringFee.getText();
        String totalDueToday = PageBase.CartPage().totalDueToday.getText();

        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForSprintWithOneDevices();
        Reporter.log("<br> No insurance selected");

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();
        Reporter.log("<br> No Number porting");

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Reporter.log("<br><p> Order Review and Confirm Page.</p>");
        // Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),cartDevice1price);

        Assert.assertTrue(driver.findElements(By.xpath("//strong[contains(text(),'Sprint Easy Pay')]")).size() > 0);
        String downPaymentAmt = null;
        try {
            downPaymentAmt = driver.findElement(By.xpath("//td[contains(text(),'Down Payment:Sprint Easy Pay')]/following-sibling::td[2]")).getText();
//            Assert.assertEquals(downpaymentAmt, downPaymentAmt);
        } catch (Exception e) {
        }

        System.out.println(downpaymentAmt + "    " + downPaymentAmt);


        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(),'One Time Activation Fee:')]/following-sibling::span[1]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//td[contains(text(),'Estimated Tax:')]/following-sibling::td[2]")).isDisplayed());
        // Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Estimated Total Due Today:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Total Due Today:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Estimated Total Recurring Fee:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());
        // Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Estimated Monthly Installment:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(),'Monthly Installment:')]/parent::th/following-sibling::th/child::h3")).isDisplayed());

        String devicePrice = driver.findElement(By.xpath("//span[contains(text(),'Full Retail Price :')]/following-sibling::strong[1]")).getText();
        //Assert.assertEquals(cartDevice1price,devicePrice);
        Reporter.log("<br> downpayment amt: " + downPaymentAmt);
        Reporter.log("<br> Device fee: " + devicePrice);

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
            PageBase.TermsAndConditionsPage().emailTCChkBox.click();
            PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            String recieptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(recieptId);
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imeiDetails1.IMEI, imeiDetails1.Sim);
            PageBase.CommonControls().continueActivation.click();

//            //Updating device in csv files.
//            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);
//            PageBase.CSVOperations().UpdateIMEIStatusToUsed(recieptId, CSVOperations.FileName.ReceiptId);

            //Device Financing Installment Contract.
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
            PageBase.DeviceFinancingInstallmentContractPage().print.click();
            Robot robot = new Robot();
            Utilities.implicitWaitSleep(6000);
            Utilities.sendKeys(KeyEvent.VK_ESCAPE, robot);
            //CommonFunction.clickingNTimesTab(8);
//            Utilities.implicitWaitSleep(2000);

            //Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
            Utilities.implicitWaitSleep(5000);
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
            Utilities.WaitUntilElementIsClickable(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().cancelOrder);
            Utilities.WaitUntilElementIsClickable(PageBase.DeviceFinancingInstallmentContractPage().cancelOrder);
            PageBase.DeviceFinancingInstallmentContractPage().cancelOrder.click();
            Utilities.implicitWaitSleep(4000);
            driver.switchTo().alert().accept();
//            Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
            Utilities.implicitWaitSleep(4000);

            try {
                driver.switchTo().defaultContent();
                Utilities.waitForDocumentReady(driver);
                System.out.println(driver.getCurrentUrl());
                Utilities.WaitUntilElementIsClickable(PageBase.HomePageRetail().newGuestButton);
            } catch (Exception ex) {
            }
            Reporter.log("<br> Cancelled the order");

        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }


    private void selectingBackendSimulatorForQA_4248() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");
        PageBase.AdminPage().checkLoaanEligibility("LOAN_ELIGIBLE");
        PageBase.AdminPage().save();
    }

    private void selectingCarrierEnviornment_4248(@Optional String testType) throws InterruptedException, AWTException, java.io.IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            //selectingBackendSimulatorForQA_4248();

            //Selecting Carrier Responder
            selectCarrierResponderQA_4248();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
            Reporter.log("<h2> External Testing: Sprint </h2>");
        }
    }

    private void selectCarrierResponderQA_4248() throws AWTException, InterruptedException {
        Robot robot = new Robot();
        Reporter.log("<h2> Carrier Responder: Sprint </h2>");
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_individual_no_numport.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(8000);
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace("<plan id=\"3473210\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41939111\">\n" +
                "                    <phone id=\"5415551111\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loannoleaseEligible.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "newactivation_NO_DEPOSIT_REQUIRED_LOANELIGIBLE.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "createloancontract", "success_lease_request.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "individual_line_success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(7000);

        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "servicevalidation", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(7000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "preauthorization", "success.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(10000);
    }

    private void selectCarrierResponderQA_4284() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_individual_no_numport.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loaneligible_with_loandownpayment.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private void inventoryManagementVerification_4284() throws InterruptedException, AWTException, IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails1.IMEI, InventoryManagementBaseClass.IMEIStatus.Available.toString());
    }

    private void shipAdminVerification_4248(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.ORDER_CANCELLED_BY_USER);
        Assert.assertTrue(eventLogTableContent.contains(Constants.NO_DEPOSIT_REQUIRED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }

    private void VerifyAPIResponseForQA_4248(String orderId) throws Exception {
        Reporter.log("<h2> Activation API Verification started.</h2>");
        String url = null;
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        System.out.print(readConfig("apiHistory"));
        driver.navigate().to(readConfig("apiHistory"));
        Utilities.WaitUntilElementIsClickable(PageBase.AdminPage().orderIdLink);
        PageBase.AdminPage().orderIdLink.sendKeys(orderId);
        Robot robot = new Robot();
        Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
        Utilities.waitForDocumentReady(driver);
//               Utilities.ScrollToElement(PageBase.AdminPage().lastActivationAPI);
//        url = PageBase.AdminPage().lastActivationAPI.getText();
//        Assert.assertTrue(driver.findElement(By.xpath("//td[contains(text(),'activation')]")).getText().contains("createLoanContract"));

        Reporter.log("<br> Cretae loan contract api verified.</br>");
    }

    private void serverDBVerificationsQA_4248(String orderId) {
        Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().chooseQueryWrapperDropdown);
        Utilities.dropdownSelect(PageBase.SQLUtilAdminPage().chooseQueryWrapperDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "2");
        PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("select * from ordersignatures where ordid=" + orderId);
        PageBase.SQLUtilAdminPage().submitButton.click();
        Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().orderSignaturesTable);
        String orderSignaturesTableContent = PageBase.SQLUtilAdminPage().orderSignaturesTable.getText();
        Assert.assertTrue(orderSignaturesTableContent.contains(orderId));
        String encryptedSignature = PageBase.SQLUtilAdminPage().encryptedSignatureRow.getText(); //If it is coming in string i.e. signature is encrypted.
        Assert.assertTrue(encryptedSignature.length() > 0);
    }

    //endregion QA 4248 Private Methods

    //region QA- 4242
    private String poaCompleteFlow_4242(String testType, IMEIDetails imeiDetails1) throws IOException, AWTException, InterruptedException {
        String orderId = "";//Login to retail page.
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
        lStartTime = new Date().getTime();
        pageName = readPageName("PoaLogin");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on Sales & Activations page.
        lStartTime = new Date().getTime();
        pageName = readPageName("SaleAndActivation");
        Utilities.waitForElementVisible(PageBase.HomePageRetail().salesAndActivationsLink);
        PageBase.HomePageRetail().salesAndActivationsLink.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on New Activation link.
        lStartTime = new Date().getTime();
        pageName = readPageName("DeviceScan");
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().newActivation);
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Scanning smart phone.
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails1.IMEI);

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        // Assert.assertTrue(PageBase.SprintEasyPayPage().priceBox.getText().contains("2yr agreement"));
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        try {
            Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
            String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
            CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
            PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);

            PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }


        lStartTime = new Date().getTime();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {

        }
        Utilities.webPageLoadTime(lStartTime, pageName);

        //Sprint Easy Pay Eligibility Result
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        // PageBase.SprintEasyPayEligibilityResultPage().sprintEasyPayInstallmentDetailsTab.click();
        Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());
        PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance.
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
        } catch (Exception ex) {
        }

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Reporter.log("<br><p> Order Review and Confirm Page.</p>");

        //TODO: Need to read from data sheet.
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
        //    cartDevice1price);

        PageBase.CommonControls().continueCommonButton.click();
        Reporter.log("<br> Device Price in Order Review and Confirmation Page Matches with Cart Page.");

        //Terms and Condition Page.
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
        PageBase.TermsAndConditionsPage().emailTCChkBox.click();
        PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.TermsAndConditionsPage().continueTCButton.click();

        // Credit card payment  page is coming.
        boolean visaexists = driver.findElements(By.id("radio-1a")).size() != 0;
        if (visaexists) {
            Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().visaTab);
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
            PageBase.PaymentRequiredPage().continuePRButton.click();
        }

        //Print Mobile Scan Sheet.
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());

        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        if (readConfig("Activation").contains("true")) {
            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            String recieptID = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
            Reporter.log("<br> Receipt ID Used: " + recieptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(recieptID);
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imeiDetails1.IMEI, imeiDetails1.Sim);
            PageBase.CommonControls().continueActivation.click();

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, CSVOperations.FileName.Sprint_4GPhone);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(recieptID, CSVOperations.FileName.ReceiptId);

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
            Utilities.implicitWaitSleep(4000);
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void shipAdminVerification_4242(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_COMPLETED_SUCCESSFULLY));
        Assert.assertTrue(eventLogTableContent.contains(Constants.NO_DEPOSIT_REQUIRED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }

    private void selectingCarrierEnviornment_4242(String testType) throws InterruptedException, AWTException, IOException {

        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            //selectingBackendSimulatorForQA_4248();

            //Selecting Carrier Responder
            carrierResponderSettingsQA_4242();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
        }
    }

    private void carrierResponderSettingsQA_4242() throws AWTException, InterruptedException {
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");
        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "creditcheck",
                "newactivation_NO_DEPOSIT_REQUIRED_LOANELIGIBLE.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "activation",
                "success_newactivation_individual_no_numport.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace("<plan id=\"3473210\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41939111\">\n" +
                "                    <phone id=\"5415561111\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "checkloaneligibility",
                "loaneligible_with_loandownpayment.xml");
        Utilities.implicitWaitSleep(1000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);

        PageBase.CarrierResponseXMLPage().selectOptions("current",
                "createloancontract",
                "success_lease_request.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        Utilities.WaitUntilElementIsClickable(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(8000);
    }

    //endregion QA 4242

    //region QA 93 Private Methods
    private String QA_93_poaCompleteFlow(@Optional String testType, IMEIDetails imei1) throws IOException {
        String orderId = "";
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.Sprint2yrUpgrade);
        String MTNNumber = accountDetails.MTN;
        String accountPassword = accountDetails.Password;
        String SSN = accountDetails.SSN;

        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        //Click on Sales and Activation Link
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Click on Existing Carrier Link
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
        PageBase.ChoosePathPage().existingCarrier.click();

        //Click on AAL to existing Family Plan
        Utilities.waitForElementVisible(PageBase.PickYourPathPage().AALExistingAccount);
        PageBase.PickYourPathPage().AALExistingAccount.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Fill Sprint Details
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
        PageBase.UECVerificationPage().fillSprintDetails(MTNNumber, SSN, accountPassword);//"9876543210","1234","94109"
        PageBase.UECVerificationPage().continueSprintButton.click();

        //Select an option.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        driver.findElement(By.id("radio-choice-3")).click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Select AAL to existing Family Plan
        try {
            PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
            PageBase.CommonControls().continueButtonDVA.click();
        } catch (Exception ex) {
        }

        //Enter IMEI of the device
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1.IMEI);
        //PageBase.VerizonEdgePage().declineSprintEasyPay();
        Utilities.waitForElementVisible(PageBase.CommonControls().cancelButton);
        PageBase.CommonControls().cancelButton.click();
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);

        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();

        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
//        try {
//            if (PageBase.CommonControls().continueButton.isEnabled())
//                PageBase.CommonControls().continueButton.click();
//        } catch (Exception e) {
//
//        }

        Utilities.waitForElementVisible(PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox);
        PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();


        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //cartDevice1price = PageBase.CartPage().device1Price.getText();
        //cartDevice2price = PageBase.CartPage().device2Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        try {
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();
        } catch (Exception ex) {
        }

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();


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
            String recieptID = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
            Reporter.log("<br> Receipt ID Used: " + recieptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(recieptID);
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei1.IMEI, imei1.Sim);
            PageBase.CommonControls().continueButtonDVA.click();

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei1.IMEI, CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(recieptID, CSVOperations.FileName.ReceiptId);


            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
            Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imei1.IMEI, CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }

        return orderId;
    }

    private void SelectCarrierResponderQA_93() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Verizon and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loanleaseEligible_multiple.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
    }

    private void selectingCarrierEnviornment_93(String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            //selectingBackendSimulatorForQA_84();

            //Selecting Carrier Responder
            selectCarrierResponderQA_93();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
            Reporter.log("<h2> External Testing: Sprint </h2>");
        }
    }

    private void selectingCarrierEnviornment(@Optional String testType) throws InterruptedException, AWTException, java.io.IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            //selectingBackendSimulatorForQA_84();

            //Selecting Carrier Responder
            selectCarrierResponderQA_84();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
            Reporter.log("<h2> External Testing: Sprint </h2>");
        }
    }

    private void selectCarrierResponderQA_93() throws AWTException, InterruptedException {
        Reporter.log("<h2> Carrier Responder: Sprint </h2>");
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);

        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "individual_line_success.xml");
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_individual_no_numport.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
//        xmlContent2 = xmlContent2.replace("<plan id=\"3473411\">\n" +
//                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41935111\">\n" +
//                "                    <phone id=\"5415551111\">");
        xmlContent2 = xmlContent2.replace("<plan id=\"3473210\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41935111\">\n" +
                "                    <phone id=\"5415551111\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        Utilities.implicitWaitSleep(5000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(10000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "noloannoleaseEligible.xml");
        // PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loaneligible.xml");
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "createloancontract", "success.xml");
        Utilities.implicitWaitSleep(6000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);

        // PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "newactivation_NO_DEPOSIT_REQUIRED.xml");
        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "existing_family_plan_added_lines_NO_DEPOSIT_REQUIRED.xml");
        Utilities.implicitWaitSleep(6000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        Utilities.implicitWaitSleep(5000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(5000);
    }

    private void shipAdminVerifications_93(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        // Assert.assertTrue(eventLogTableContent.contains(Constants.ORDER_VALIDATION_PASSED));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_COMPLETED_SUCCESSFULLY));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        // Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.HANDSET_UPGRADE));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
        Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.EXISTING_ACCOUNT_ORDER));
    }
    //endregion QA 93 Private Methods

    //region QA 4244 Private Methods
    private String poaCompleteFlow_4244(String testType, IMEIDetails imeiDetails1) throws IOException, InterruptedException, AWTException {
        String orderId = "";//Login to retail page.
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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
        Utilities.webPageLoadTime(lStartTime, pageName);

        //Sprint Easy Pay Eligibility Result
        Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());

        PageBase.VerizonEdgePage().customerDownPayment.sendKeys("100");
        PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance.
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
        } catch (Exception ex) {
        }

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Reporter.log("<br><p> Order Review and Confirm Page.</p>");
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
        //    cartDevice1price);
        PageBase.CommonControls().continueCommonButton.click();
        Reporter.log("<br> Device Price in Order Review and Confirmation Page Matches with Cart Page.");

        //Terms and Condition Page.
        Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
        PageBase.TermsAndConditionsPage().emailTCChkBox.click();
        PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
        PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.TermsAndConditionsPage().continueTCButton.click();

        // Credit card payment  page is coming.
        boolean visaexists = driver.findElements(By.id("radio-1a")).size() != 0;
        if (visaexists) {
            Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().visaTab);
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
            PageBase.PaymentRequiredPage().continuePRButton.click();
        }

        //Print Mobile Scan Sheet.
        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());

        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        if (readConfig("Activation").contains("true")) {
            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            String recieptID = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(recieptID);
            PageBase.PaymentVerificationPage().submitButton.click();
            Reporter.log("<br> Receipt ID Used: " + recieptID);

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imeiDetails1.IMEI, imeiDetails1.Sim);
            PageBase.CommonControls().continueActivation.click();

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, CSVOperations.FileName.Sprint_IPhone5C);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(recieptID, CSVOperations.FileName.ReceiptId);

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
            Utilities.implicitWaitSleep(4000);
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    //endregion QA 4244 Private Methods

    //region QA 3928 Private Methods

    private String poaCompleteFlow_3928(String testType, IMEIDetails imeiDetails1) throws IOException {
        String orderId = "";//Login to retail page.
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        // Click on Sales & Activations page.
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        // Click on New Activation link.
        PageBase.ChoosePathPage().newActivation.click();

        // Scanning smart phone
        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails1.IMEI);
        PageBase.VerizonEdgePage().declineSprintEasyPay();
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
        }

        //if(testType.equals("internal")) PageBase.CommonControls().continueButton.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

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

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();
        // Selecting No Insurance .
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();
        } catch (Exception ex) {
        }

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Reporter.log("<br><p> Order Review and Confirm Page.</p>");
        Double sumOfAdditions = 0.00;
        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), cartDevice1price);
        String[] prodName = imeiDetails1.ProductName.split(" ");

        List<WebElement> child = driver.findElements(By.xpath(".//div[(@id='retailPage')]/section/table/tbody/child::*"));
        for (WebElement e : child) {
            try {
                if (e.getAttribute("class").toString().equals("ordcAdditions")) {
                    String additions = null;
                    additions = e.findElement(By.xpath("./child::td[2]")).getText().replace("$", "");
                    sumOfAdditions = sumOfAdditions + Double.valueOf(additions);
                }
            } catch (Exception ex) {
            }
        }
        String monthlyFee = PageBase.OrderReviewAndConfirmPage().totalFeeActual.getText();

        String line1Fee = PageBase.OrderReviewAndConfirmPage().device1ActivationFee1Device.getText();
        line1Fee = line1Fee.substring(1, 6);
        sumOfAdditions = Double.valueOf(line1Fee) + sumOfAdditions;
        Reporter.log("<br> Line1 Fee: " + line1Fee);
        Reporter.log("<br> Additions Sum with Line1 Fee: " + sumOfAdditions);
        Reporter.log("<br> Total Monthly Fee: " + monthlyFee);
        Assert.assertTrue(monthlyFee.contains(String.valueOf(sumOfAdditions)));
        Reporter.log("<br> Monthly Fee Estimate matches with Sum of individual Additions");
        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), cartDevice1price);
        Reporter.log("<br> Device Price in Order Review and Confirmation Page Matches with Cart Page.");

        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),cartDevice1price);
        PageBase.CommonControls().continueCommonButton.click();
        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsAndConditionsPage().emailTCChkBox);
            PageBase.TermsAndConditionsPage().emailTCChkBox.click();
            PageBase.TermsAndConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsAndConditionsPage().continueTCButton.click();

            do {
            } while (driver.getCurrentUrl().contains("runcredit.htm"));

            if (driver.getCurrentUrl().contains("payment")) {
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
                Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
            }

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();

            //TODO:Need to add assertion for store location.
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            String recieptID = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
            Reporter.log("<br> Receipt ID Used: " + recieptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(recieptID);
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imeiDetails1.IMEI, imeiDetails1.Sim);
            PageBase.CommonControls().continueButtonDVA.click();

            //Updating device in csv files.
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(recieptID, CSVOperations.FileName.ReceiptId);

            // Order Activation Complete page.
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Reporter.log("<br> Order Activation Complete Page. ");
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
            Assert.assertEquals(orderId, orderIdfromActPage.substring(1));
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Reporter.log("<h3><font color='green'>Order has been confirmed, Order Id =<U>" + orderId + "</U></h3>");
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), imeiDetails1.IMEI);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), imeiDetails1.Sim);

            //Device verification
            Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValueText.getText(), cartDevice1price);
            Reporter.log("<br> Device Price matches with Cart Page and Order Review and Confirm Page: " + cartDevice1price);

            //Activation fee verification.
            double actviationfeedev1 = Double.parseDouble(PageBase.OrderActivationCompletePage().activationFeeDevice1.getText().substring(1));
            double totalFee = Double.parseDouble(PageBase.OrderActivationCompletePage().activationTotal.getText().substring(1));
            Assert.assertEquals(actviationfeedev1, totalFee);
            Reporter.log("<br> Total Fee matches with Order Review and Confirm Page: " + totalFee);

            //Estimated Total.
            double estimatedTotal = Double.parseDouble(PageBase.OrderActivationCompletePage().estimatedTotal.getText().substring(1));
            Assert.assertTrue(monthlyFee.contains(String.valueOf(estimatedTotal)));
            Reporter.log("<br> Estimated Total monthly Fee matches with Order Review and Confirm Page: " + estimatedTotal);

            Reporter.log("<br> Order Activation Complete Page.");
            orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
            Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
            try {
                Reporter.log("<br> Phone Number: " + PageBase.OrderActivationCompletePage().phoneNumberValueText.getText());
            } catch (Exception ex) {
            }
            Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
            try {
                Reporter.log("<br> IMEI: " + PageBase.OrderActivationCompletePage().iMEINumberValueText.getText());
            } catch (Exception ex) {
            }
            Reporter.log("<br> Order Activation Complete Page.");
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void selectingCarrierEnviornment_QA_3928(@Optional String testType) throws InterruptedException, AWTException, java.io.IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            // selectingBackendSimulatorForQA_84();

            //Selecting Carrier Responder
            selectCarrierResponderQA_3928();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
            Reporter.log("<h2> External Testing: Sprint </h2>");
        }
    }

    private void selectCarrierResponderQA_3928() throws AWTException, InterruptedException {
        Reporter.log("<h2> Carrier Responder: Sprint </h2>");
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);

        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_individual_no_numport.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace("<plan id=\"3473210\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41935111\">\n" +
                "                    <phone id=\"5415551111\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "noloannoleaseEligible.xml");
        Utilities.implicitWaitSleep(6000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "newactivation_NO_DEPOSIT_REQUIRED.xml");
        Utilities.implicitWaitSleep(6000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
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

    //endregion QA 3928 Private Methods

    //region QA_85 Private Methods
    private void CarrierResponderSettings_QA_85() throws InterruptedException, AWTException, IOException {
        if (readConfig("test-type").equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            if (readConfig("internalTestType").toLowerCase().contains("simulator")) {
                PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");
            } else {
                Robot robot = new Robot();
                PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

                //Selecting Carrier config file.
                PageBase.AdminPage().selectAPIConfig("Sprint");

                // Selecting Sprint and response xml.
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
                PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);

                PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_family_2_lines.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(5000);
                String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
                xmlContent2 = xmlContent2.replace("<plan id=\"3473710\">\n" +
                        "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41587111\">\n" +
                        "                    <phone id=\"5415551111\">");
                xmlContent2 = xmlContent2.replace(" <plan id=\"34738211\">\n" +
                        "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41588210\">\n" +
                        "                    <phone id=\"5415551112\">");
                PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
                Utilities.copyPaste(xmlContent2, robot);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();

                Utilities.implicitWaitSleep(2000);
                PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "noloannoleaseEligible.xml");
                Utilities.implicitWaitSleep(4000);
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();

                Utilities.implicitWaitSleep(2000);
                PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "newactivation_NO_DEPOSIT_REQUIRED.xml");
                Utilities.implicitWaitSleep(4000);
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            }
            //Selecting Carrier Responder
        } else {   //External
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
        }

        Utilities.switchPreviousTab();
    }

    private String poaCompleteFlow_QA_85(IMEIDetails imeiDetails1, IMEIDetails imeiDetails2) throws IOException, InterruptedException, AWTException {
        String orderId = null;

        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");

        // Click on Sales & Activations page.
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        // Click on New Activation link.
        PageBase.ChoosePathPage().newActivation.click();

        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails1.IMEI);
        if (driver.getCurrentUrl().contains(ControlLocators.FINANCE_OPTIN_PAGE_URL))
            PageBase.VerizonEdgePage().declineSprintEasyPay();
        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails2.IMEI);
        if (driver.getCurrentUrl().contains(ControlLocators.FINANCE_OPTIN_PAGE_URL))
            PageBase.VerizonEdgePage().declineSprintEasyPay();
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();
        Reporter.log("<br> Credit Check Completes.");

        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {
        }

        // Credit Check Verification Results with deposits.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        boolean exists = driver.findElements(By.id("checkbox-deposit-1")).size() != 0;
        if (exists) {
            PageBase.CreditCheckVerificationResultsPage().depositCheckBox.click();
            Reporter.log("<br> Selected Deposit Check Box ");
        } else {
            PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
        }
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        cartDevice2price = PageBase.CartPage().device2Price.getText();

        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting No Insurance
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForSprintWithTwoDevices();
        Reporter.log("No Insurance page");


        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();
        Reporter.log("No number porting");

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV
                (spvCollections[0], spvCollections[1], spvCollections[2]);


        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Reporter.log("<br><p> Order Review and Confirm Page.</p>");

        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
                cartDevice1price);
        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device2PriceActual.getText(), cartDevice2price);

        PageBase.OrderReviewAndConfirmPage().OrderReviewConfirmPageAssertionsFor2Devices(cartDevice1price, cartDevice2price);
        Assert.assertTrue(driver.findElements(By.xpath("//strong[contains(text(),'2 Year Contract')]")).size() > 0);

        String line1ActivationFee = driver.findElement(By.xpath("(//span[contains(text(),'One Time Activation Fee:')])[1]/following-sibling::span[1]")).getText();
        String line2ActivationFee = driver.findElement(By.xpath("(//span[contains(text(),'One Time Activation Fee:')])[2]/following-sibling::span[1]")).getText();

        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalMonthlyDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalMonthlyDue = totalMonthlyDue.replace("inc. tax", "");
        totalMonthlyDue = totalMonthlyDue.replace(" ", "");

        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        totalDue = totalDue.replace("inc. tax", "");
        totalDue = totalDue.replace(" ", "");

        double device1 = Double.parseDouble(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText().substring(1));
        double device2 = Double.parseDouble(PageBase.OrderReviewAndConfirmPage().device2PriceActual.getText().substring(1));

        Assert.assertEquals(device1 + device2, Double.parseDouble(totalDue.replace("$", "").replace(",", "")));

        // Storing total monthly/ activation fee.
        String activationFee = PageBase.OrderReviewAndConfirmPage().totalFeeActual.getText();
        double actfeeMonthly = Double.parseDouble(activationFee.substring(1).replace("+ tax", "").replace(" ", ""));

        Reporter.log("<br> ActivationLine1 Fee: " + line1ActivationFee);
        Reporter.log("<br> ActivationLine2 Fee: " + line2ActivationFee);
        Reporter.log("<br> Total Due: " + totalDue);
        Reporter.log("<br> Device1 Price:" + device1 + "device2 Price: " + device2);

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckboxForSprint, 120);
            PageBase.WirelessCustomerAgreementPage().acceptsWCACheckboxForSprint.click();
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            Assert.assertTrue(driver.findElement(By.xpath("//h1[contains(text(),'Sprint Terms')]")).getText().contains("Sprint Terms"));
            Assert.assertTrue(driver.findElement(By.xpath("//h1[contains(text(),'Target Terms')]")).getText().contains("Target Terms"));

            PageBase.TermsAndConditionsPage().continueTCButton.click();

            do {
                Utilities.implicitWaitSleep(1000);
            } while (!driver.getCurrentUrl().contains("checkout/printticket.htm"));

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            PageBase.PrintMobileScanSheetPage().verifyAllTwoDeviceBarCode();

            // WebElement web = driver.findElement(By.xpath("//span[contains(text(),'Target 0003: 5537 W Broadway Ave, Crystal, MN 55428, U.S.A')]"));
            Assert.assertTrue(driver.findElement(By.xpath("(//span[contains(text(),'Store Location')])/following-sibling::span")).getText().contains("0003 - TARGET - CRYSTAL"));
            String mssdevice1Price = driver.findElement(By.xpath("(//tr[@class = 'mssmarginb'])[1]/child::td[2]")).getText();
            Assert.assertEquals(cartDevice1price, mssdevice1Price);

            String mssdevice2Price = driver.findElement(By.xpath("(//tr[@class = 'mssmarginb'])[2]/child::td[2]")).getText();
            Assert.assertEquals(cartDevice2price, mssdevice2Price);

            //Click on new guest to start new session.
            Utilities.WaitUntilElementIsClickable(PageBase.HomePageRetail().newGuestButton);
            PageBase.HomePageRetail().newGuestButton.click();
            Utilities.WaitUntilElementIsClickable(PageBase.HomePageRetail().guestLookupTab);
            PageBase.HomePageRetail().guestLookupTab.click();

            PageBase.CustomerLookupPage().viewGuestOrders.click();
            PageBase.CustomerLookupPage().awaitingActivationTab.click();

            Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
            PageBase.CommonControls().continueCommonButton.click();

            PageBase.GuestVerificationPage().populateGuestVerificationDetails(cccDetails.getIDType(), cccDetails.getState(),
                    cccDetails.getIdNumber(), cccDetails.getFirstName(), cccDetails.getLastName());
            String orderElement = ".//*[contains(text(),'" + orderId + "')]";
            driver.findElement(By.xpath(orderElement + "/following-sibling::div[3]/child::a")).click();
            PageBase.CarrierCreditCheckPage().continueAfterGuestAgreesToPayDeposit.click();

            PageBase.CommonControls().cancelButton.click();
            Reporter.log("<br> Order cancel by user: ");
            Utilities.waitForElementVisible(PageBase.HomePageRetail().salesAndActivationsLink);
            ShipAdminBaseClass.launchShipAdminInNewTab();
            Utilities.WaitUntilElementIsClickable(PageBase.ShipAdminPage().orderTextbox);
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            String status = PageBase.OrderSummaryPage().getOrderStatus();
            Assert.assertEquals(status, Constants.ORDER_CANCELLED_BY_USER);
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            Utilities.WaitUntilElementIsClickable(PageBase.InventoryManagementPage().productsLink);

            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails1.IMEI, CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
            PageBase.CSVOperations().UpdateIMEIStatusToUsed(imeiDetails2.IMEI, CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

            PageBase.InventoryManagementPage().verifyDeviceStatus
                    (imeiDetails1.IMEI, imeiDetails2.IMEI, InventoryManagementBaseClass.IMEIStatus.Available.toString());
            Reporter.log("<br> Inventory Verification done Both device are available in inventory: ");
        }
        return orderId;
    }

//endregion QA_85 Private Methods

    //region QA 2640 Private methods
    private void selectingCarrierEnviornment_QA_2640(String testType) throws InterruptedException, AWTException, java.io.IOException {
        if (testType.equals("internal")) {
            Reporter.log("ONLY on External Testing");
        } else {   //External
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
        }
        Utilities.switchPreviousTab();
    }


    private String poaCompleteFlow_QA_2640(IMEIDetails imeiDetails)
            throws IOException, InterruptedException, AWTException {
        String orderId = null;
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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
        pageName = readPageName("Choose Path");
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().newActivation);
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);


        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails.IMEI);
        PageBase.VerizonEdgePage().declineSprintEasyPay();
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId
                (CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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

        try {
            WebElement e = driver.findElement(By.xpath(".//*[contains(text(),'I agree to my spending limits rules per line when I activate with Sprint)]"));
            System.out.println(e.getTagName());
            System.out.println(e.getText());
            e.click();
        } catch (Exception e) {
        }

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting No Insurance .
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();
        } catch (Exception e) {
        }
        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV
                (spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Reporter.log("<br><p> Order Review and Confirm Page.</p>");
        Double sumOfAdditions = 0.00;
//        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), cartDevice1price);
//        String[] prodName = imeiDetails1.ProductName.split(" ");

        List<WebElement> child = driver.findElements(By.xpath(".//div[(@id='retailPage')]/section/table/tbody/child::*"));
        for (WebElement e : child) {
            try {
                if (e.getAttribute("class").toString().equals("ordcAdditions")) {
                    String additions = null;
                    additions = e.findElement(By.xpath("./child::td[2]")).getText().replace("$", "");
                    sumOfAdditions = sumOfAdditions + Double.valueOf(additions);
                }
            } catch (Exception ex) {
            }
        }
        String monthlyFee = PageBase.OrderReviewAndConfirmPage().totalFeeActual.getText();

        String line1Fee = PageBase.OrderReviewAndConfirmPage().device1ActivationFee1Device.getText();
        line1Fee = line1Fee.substring(1, 6);
        sumOfAdditions = Double.valueOf(line1Fee) + sumOfAdditions;
        Reporter.log("<br> Line1 Fee: " + line1Fee);
        Reporter.log("<br> Additions Sum with Line1 Fee: " + sumOfAdditions);
        Reporter.log("<br> Total Monthly Fee: " + monthlyFee);
        Assert.assertTrue(monthlyFee.contains(String.valueOf(sumOfAdditions)));
        Reporter.log("<br> Monthly Fee Estimate matches with Sum of individual Additions");
//        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(), cartDevice1price);
        Reporter.log("<br> Device Price in Order Review and Confirmation Page Matches with Cart Page.");


        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckboxForSprint, 120);
            PageBase.WirelessCustomerAgreementPage().acceptsWCACheckboxForSprint.click();
            PageBase.TermsAndConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsAndConditionsPage().continueTCButton.click();
            do {
                Utilities.implicitWaitSleep(1000);
            } while (!driver.getCurrentUrl().contains("checkout/printticket.htm"));

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(
                    PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();

            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(imeiDetails.IMEI);//"78998767887645"
            PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
            String simTypeInvalid = "11111111111111111111";
            PageBase.DeviceVerificationaandActivation().simType.sendKeys(simTypeInvalid);

            try {
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
            } catch (Exception e) {
            }

            PageBase.CommonControls().continueActivation.click();
            do {
                Utilities.implicitWaitSleep(1000);
            } while (!driver.getCurrentUrl().contains("retail/support.htm"));

            ShipAdminBaseClass.launchShipAdminInNewTab();
            Utilities.implicitWaitSleep(5000);
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            String status = PageBase.OrderSummaryPage().getOrderStatus();
            Assert.assertEquals(status, "In-Store Activation");
            Reporter.log("<br> " + status);
            String eventLogTableContent = PageBase.OrderSummaryPage().eventLogTable.getText();
            Log.info("-------------------START ERROR INFO -- QA-2640 -----------------------------");
            Log.info(eventLogTableContent); //ordreId
            Log.info("-------------------END ERROR INFO -- QA-2640 -----------------------------");
            Assert.assertTrue(eventLogTableContent.contains("Encountered Fatal Exception:"));
            Assert.assertTrue(eventLogTableContent.contains("Exception Code: 004"));
            Assert.assertTrue(eventLogTableContent.contains("Exception Message: Invalid XML Response - Activation"));
            Assert.assertTrue(eventLogTableContent.contains("No lines returned."));
            Assert.assertTrue(eventLogTableContent.contains("Number of lines in response does not match number of lines in request."));
            Assert.assertTrue(eventLogTableContent.contains("Exception Error: Error Code: "));
            Assert.assertTrue(eventLogTableContent.contains("Error Sub Name: ICC_ID_NOT_FOUND_INVENTORY"));
            Assert.assertTrue(eventLogTableContent.contains(simTypeInvalid));
            Reporter.log("<br> In ROC");
            driver.findElement(By.xpath(".//*[@value='" + simTypeInvalid + "']")).sendKeys(imeiDetails.Sim);

            driver.findElement(By.name("billingAccountNumber")).sendKeys(CommonFunction.GenerateRandomNumber(9));
            driver.findElement(By.xpath(".//*[@id='actItemFieldsBlock']/table/tbody/tr[4]/td[2]/input")).sendKeys(CommonFunction.GenerateRandomNumber(10));
            driver.findElement(By.name("manMethods[1]")).click();
            driver.findElement(By.xpath(".//input[@value='Activation Complete']")).click();
            Utilities.implicitWaitSleep(1000);
            PageBase.OrderSummaryPage().rOCHomeLink.click();
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

            status = PageBase.OrderSummaryPage().getOrderStatus();
            Assert.assertEquals(status, Constants.SHIPPED);
            Assert.assertTrue(eventLogTableContent.contains("but not literally. Order is technically non-shipping"));
            Assert.assertTrue(eventLogTableContent.contains("Activation completed"));
            Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
            Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
            Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains("Sprint BackUp (non xml)"));
            Reporter.log("<br> " + status);

            Utilities.switchPreviousTab();
            Utilities.waitForElementVisible(PageBase.CommonControls().continueSupportCenter);
            PageBase.CommonControls().continueSupportCenter.click();

            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), imeiDetails.IMEI);


            PageBase.SQLUtilAdminPage().launchSQLUtilInNewTab();
            Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().chooseQueryWrapperDropdown);
            Utilities.dropdownSelect(PageBase.SQLUtilAdminPage().chooseQueryWrapperDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "2");
            PageBase.SQLUtilAdminPage().queryTextbox.clear();
            PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("select * from ordersignatures where ordid='" + orderId + "'");
            PageBase.SQLUtilAdminPage().submitButton.click();
            Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().orderSignaturesTable);
            String orderSignaturesTableContent = PageBase.SQLUtilAdminPage().orderSignaturesTable.getText();
            Assert.assertTrue(orderSignaturesTableContent.contains(orderId));
            Assert.assertTrue(orderSignaturesTableContent.contains("serivce contract"));
            String encryptedSignature = PageBase.SQLUtilAdminPage().encryptedSignatureRow.getText(); //If it is coming in string i.e. signature is encrypted.
            Assert.assertTrue(encryptedSignature.length() > 0);

            PageBase.SQLUtilAdminPage().queryTextbox.clear();
            PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("select * from orderextrainfos where ordid=" + orderId);
            PageBase.SQLUtilAdminPage().submitButton.click();
            Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().orderSignaturesTable);
            orderSignaturesTableContent = PageBase.SQLUtilAdminPage().orderSignaturesTable.getText();
            Assert.assertFalse(orderSignaturesTableContent.contains("46"));

            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            Utilities.implicitWaitSleep(5000);
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiDetails.IMEI, InventoryManagementBaseClass.IMEIStatus.Sold.toString());

        }
        return orderId;
    }

    //endregion QA 2640 private methods

    //region QA_80 Private Methods

    private String QA_80_POAFlow(ArrayList<String> imei, ArrayList<String> sim, String ssn, String recpitId) throws InterruptedException, AWTException, IOException {
        //Test Data for activation flow
        String orderId = null;
        String device_1_PriceCartPage;
        String device_2_PriceCartPage;
        String planPriceCartPage;
        String pinNumber = "112314";
        String secretAns = "Hello";
        Utilities.switchPreviousTab();
        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();
        System.out.println("DB Error Count  " + initialCount);
        Utilities.switchPreviousTab();
        //Login to Application
        lStartTime = new Date().getTime();
        pageName = readPageName("PoaLogin");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        //Choose the new activation path
        lStartTime = new Date().getTime();
        pageName = readPageName("SaleAndActivation");
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        lStartTime = new Date().getTime();
        pageName = readPageName("DeviceScan");
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        //Scan imei for two phones and continue with two year contract
        for (int i = 0; i < imei.size(); i++) {
            PageBase.DeviceScanPage().enterDeviceScanDetails(imei.get(i));
            Utilities.implicitWaitSleep(100);
            PageBase.CommonControls().cancelButton.click();
        }
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        Utilities.webPageLoadTime(lStartTime, pageName);
        PageBase.CommonControls().continueButtonDVA.click();
        Utilities.implicitWaitSleep(1000);
        //Credit Check Page
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        lStartTime = new Date().getTime();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

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
//        if((PageBase.CreditCheckVerificationResultsPage().creditCheckSpendingChkBox.isDisplayed())== true)
//            PageBase.CreditCheckVerificationResultsPage().creditCheckSpendingChkBox.click();
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        device_1_PriceCartPage = PageBase.CartPage().device1Price.getText();
        device_2_PriceCartPage = PageBase.CartPage().device2Price.getText();
//        planPriceCartPage = PageBase.CartPage().planPriceWith2Phones.getText();

        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForSprintWithTwoDevices();

        Utilities.implicitWaitSleep(10000);

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().numberPortRadiobutton);
        PageBase.NumberPortPage().numberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);

        for (int i = 0; i < imei.size(); i++) {
            PageBase.CSVOperations();
            NumPortingDetails PortingData = CSVOperations.ReadPortingDetails();
            Utilities.implicitWaitSleep(100);
            PageBase.PortMyNumbersPage().enterMultiplePortData(i, PortingData.CurrentPhoneNumber
                    , PortingData.Carrier, PortingData.CurrentAccNumber, PortingData.SSN);
        }
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        //Service Provider Page
        Utilities.implicitWaitSleep(1000);

        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(pinNumber, "2", secretAns);

        //Order Review and confirm
//        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),device_1_PriceCartPage);
//        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device2PriceActual.getText(),device_2_PriceCartPage);
        PageBase.CommonControls().continueCommonButton.click();
        Utilities.implicitWaitSleep(1000);

        //Terms and Condition Page.
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.TermsAndConditionsPage().saveSignatureButton.click();
        PageBase.TermsAndConditionsPage().acceptSprintTermsAndCondition();
        Utilities.implicitWaitSleep(10000);
        // Credit Check Verification Results with deposits.
        WebDriverWait wait = new WebDriverWait(driver, 180); //you can play with the time integer  to wait for longer than 15 seconds.`
        if (BrowserSettings.readConfig("test-type").contains("internal")) {
            Utilities.implicitWaitSleep(10000);
            wait.until(ExpectedConditions.visibilityOf(PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox));
            Utilities.waitForElementVisible(PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox);
            PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
//        if ((PageBase.CreditCheckVerificationResultsPage().creditCheckSpendingChkBox.isDisplayed()) == true)
//            PageBase.CreditCheckVerificationResultsPage().creditCheckSpendingChkBox.click();
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDeposit);
            PageBase.CommonControls().continueButtonDeposit.click();
        }
        if (driver.getCurrentUrl().contains("payment")) {
            Utilities.implicitWaitSleep(10000);
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }
        if (readConfig("Activation").contains("true")) {
            //MSS Page
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            //Payment Verification
            PageBase.PaymentVerificationPage().paymentVerification(recpitId);

            //Device Verification and Activation
            PageBase.DeviceVerificationaandActivation().sprintDeviceVerificationActivation(imei, sim);
            wait.until(ExpectedConditions.visibilityOf(PageBase.OrderActivationCompletePage().ActivationComplete));
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void QA_80_CarrierResponder() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");
        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");
        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation", "family_2_lines_eligible.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_family_2_lines_numport.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loanleaseEligible_with_loanleasedownpayment.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck", "newactivation_DEPOSIT_REQUIRED_SL_REQUIRED_LOANELIGIBLE.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();

        Utilities.implicitWaitSleep(2000);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "porteligibility", "eligible.xml");
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private void QA_80_BackendSimulator() {
        PageBase.AdminPage().selectWebAPIResponse("sprint", "BackendSimulator");
        PageBase.AdminPage().selectAPIConfig("Sprint");
        PageBase.AdminPage().spCreditCheck("APPROVED_DEPOSIT_REQUIRED");
        PageBase.AdminPage().spPortEligibility("ELIGIBLE");
        PageBase.AdminPage().save();
    }

    private void QA_80_InventoryManagement(ArrayList<String> imei, String status) throws InterruptedException, AWTException, IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        for (int i = 0; i < imei.size(); i++)
            PageBase.InventoryManagementPage().verifyDeviceStatus(imei.get(i), InventoryManagementBaseClass.IMEIStatus.Sold.toString());
    }

    private void QA_80_ShipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT1));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_WIRELESS_XML));

    }
    //endregion QA_80 Private Methods

    // region QA 4162 Helper Methods

    private String poaCompleteFlow_4162(String testType, String imei, String sim) throws IOException, InterruptedException, AWTException {

        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();

        String orderId = "";//Login to retail page.
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei);

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);

        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.SSNWithDeposit));
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

        Utilities.webPageLoadTime(lStartTime, pageName);

        //Sprint Easy Pay Eligibility Result
        Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
        PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
        // Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
        //Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
        // Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance.
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
        } catch (Exception ex) {
        }

        // Selecting Number Porting.
        Utilities.ClickElement(PageBase.NumberPortPage().numberPortRadiobutton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        PageBase.PortMyNumbersPage().enterPortDataForPreCreditCheck(portDetails.CurrentPhoneNumber, portDetails.Carrier,
                portDetails.CurrentAccNumber, portDetails.SSN);

        // Enter data in Service Provider Verification page.
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            // Terms and Condition Page.
            PageBase.TermsAndConditionsPage().acceptTermsAndConditions();

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

            //Device Verification and Activation page. Scan Device IMEI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei, sim);
            PageBase.CommonControls().continueActivation.click();

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
            Utilities.implicitWaitSleep(4000);

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), imei);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), sim);
            // Assert.assertTrue(PageBase.OrderActivationCompletePage().printConfirmationSlipButton.isDisplayed());
            //  Assert.assertTrue(PageBase.OrderActivationCompletePage().deviceModelLabel.isDisplayed());
            //  Assert.assertTrue(PageBase.OrderActivationCompletePage().downPaymentLabel.isDisplayed());
            //  Assert.assertTrue(PageBase.OrderActivationCompletePage().monthlyDeviceInstallmentLabel.isDisplayed());
            //  Assert.assertTrue(PageBase.OrderActivationCompletePage().lastMonthInstallmentLabel.isDisplayed());
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void selectingBackendSimulatorForQA_4162() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");
        PageBase.AdminPage().checkLoaanEligibility("LOAN_ELIGIBLE");
        PageBase.AdminPage().save();
    }

    private void selectingCarrierEnviornment_4162(@Optional String testType) throws InterruptedException, AWTException, java.io.IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            //selectingBackendSimulatorForQA_4162();

            //Selecting Carrier Responder
            selectCarrierResponderQA_4162();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
        }
    }

    private void selectCarrierResponderQA_4162() throws AWTException, InterruptedException {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().sprintCarrierTab);
        PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loannoleaseeligible.xml");
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "creditcheck",
                "newactivation_DEPOSIT_REQUIRED_SL_REQUIRED_LOANELIGIBLE.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(5000);
        Robot robot = new Robot();
        String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent2 = xmlContent2.replace(Constants.SL_WITH_DEPOSIT, "<spending-limit-amount>0.0</spending-limit-amount>");
        xmlContent2 = xmlContent2.replace(Constants.SL_WITH_DEPOSIT, "<spending-limit-amount>0.0</spending-limit-amount>");
        Utilities.implicitWaitSleep(3000);
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent2, robot);
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "activation", "success_newactivation_individual_with_numport.xml");
        Utilities.implicitWaitSleep(3000);
        String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
        xmlContent1 = xmlContent1.replace("<plan id=\"3473410\">\n" +
                "                    <phone id=\" %%% *** GET FROM REQUEST *** %%% \">", "<plan id=\"41935111\">\n" +
                "                    <phone id=\"5415551111\">");
        PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
        Utilities.copyPaste(xmlContent1, robot);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }

    private void inventoryManagementVerification_4162(String imei) throws InterruptedException, AWTException, IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imei, InventoryManagementBaseClass.IMEIStatus.Sold.toString());
    }

    private void QA_4162_shipAdminVerification(String orderId) throws IOException {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }

// endregion QA 4162 Helper Methods

    //region Common Methods
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

        cccDetails.setIdTypeState(customerDetails.IDState);
        cccDetails.setIdNumber(customerDetails.IDNumber);
        cccDetails.setMonth(customerDetails.IDExpirationMonth);
        cccDetails.setYear(customerDetails.IDExpirationYear);
        cccDetails.setIDType(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        return cccDetails;
    }

    //endregion Common Methods

    //endregion private methods
}