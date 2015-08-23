package sprint.tests;

import framework.*;
import framework.CSVOperations.FileName;
import pages.PaymentRequiredPage;
import pages.ServiceProviderVerificationPage;
import pages.ServiceProviderVerificationPage.IdType;
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
import java.io.IOException;
import java.util.List;

import static pages.ServiceProviderVerificationPage.IdType.DRIVERLICENCE;

public class ExchangeTests extends RetailBaseClass {

    //region public variable
    public String carrierType = "Sprint";
    public String orderId = "";
    String ChildOrderId = "";
    String iMEINumber1 = "";
    //endregion

    //region QA 4263
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_4263_VerizonSprintEasyUpgradeExchangeToEasyPay(@Optional String testtype) throws InterruptedException, AWTException, IOException {
        String phno = PageBase.CSVOperations().GenerateRandomNoForNumberPorting();
        String testType = BrowserSettings.readConfig("test-type");
        CreditCardDetails creditCard = new CreditCardDetails();
        creditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);

        String phoneNumberLine1 = "";
        String receiptId = "";

        String iMEINumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);
        String simType1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sim_3FF);
        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintEasyPayUpgrade);
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);

        String phoneNumber = accountDetails.MTN;
        String sSN = accountDetails.SSN;
        String zipCode = customerDetails.Zip;
        String accountPassword = accountDetails.Password;

        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(iMEINumber1, "SPT IPHONE 4S WHITE 8GB ");
        PageBase.driver.close();

        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));

            if (BrowserSettings.readConfig("internalTestType") == "CarrierResponder") {
                //Selecting Use Case from dropdown list.
                PageBase.AdminPage().selectAPIConfig(carrierType);
                //Customizing xml files in Carrier Responder
                PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "accountvalidation",
                        "individual_2_lines_eligible.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(5000);
                String xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
                xmlContent1 = xmlContent1.replace(Constants.DEFAULT_XML_NUMBER_6232157629, phoneNumber);
                PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
                Robot robot = new Robot();
                Utilities.copyPaste(xmlContent1, robot);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
                Utilities.implicitWaitSleep(5000);
                Reporter.log("<br>Carrier Responder Changes Done!!!", true);
            }
        } else //External
        {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
        }
        // Switching to Retail tab.
        Utilities.switchPreviousTab();
        String orderId = QA_4263_UpgradeFlowWithEasyPay(phoneNumber, iMEINumber1, sSN, zipCode, simType1);
        //PageBase.LoginPageRetail().LaunchNewPOATab();
        PageBase.HomePageRetail().newGuestButton.click();
        returnFlowQA4263(testType, orderId, customerDetails.State, iMEINumber1, IdType.valueOf(customerDetails.IDType),
                customerDetails.IDNumber, customerDetails.FirstName, customerDetails.LastName);
        //-------------Upgrade Flow-----
        // To do
        // cannot do upgrade with phone number of existing order
    }
    //endregion QA 4263


    @Parameters({"test-type"})
    @Test
    public void QA_88_SptUpgradeOrderESecutitel_LikeForLikeExchange() throws IOException,
            InterruptedException, AWTException {

        String sTestCaseName = "QA_88_SptUpgradeOrderESecutitel_LikeForLikeExchange";

        String ChildOrderId = null;

        String testType = BrowserSettings.readConfig("test-type");
        Reporter.log(sTestCaseName + "Starts.");
        Reporter.log("<br>Launching Browser");
        Log.startTestCase(sTestCaseName);

        //carrierType = "Sprint";
        try {
            Log.startTestCase(sTestCaseName);
            //
            //			selectCarrierResponderQA88(testType);
            //
            //			Utilities.switchPreviousTab();

            ChildOrderId = poaCompleteFlowQA88();

            //Inventory Management and Ship admin Page verification.
            if (readConfig("Activation") == "true") {
                // Inventory Management Page verification.
                PageBase.InventoryManagementPage().launchInventoryInNewTab();
                PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber1, InventoryManagementBaseClass.IMEIStatus.Sold.toString());

                //Ship Admin Verification -orderId= "";
                QA_88_shipAdminVerification(orderId);
            }
            //DBError Verification.
            DBError.navigateDBErrorPage();
            //Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Reporter.log("<h3>QA_85_SptUpgradeOrderESecutitel_LikeForLikeExchange - Test Case Completes<h3>");
            Log.endTestCase("QA_85_SptUpgradeOrderESecutitel_LikeForLikeExchange");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    // Region Helper Methods
    //region QA-4263 Refactored Methods
    private String QA_4263_UpgradeFlowWithEasyPay(String phoneNumber, String imei, String ssn, String zipCode,
                                                  String simType1) throws InterruptedException, AWTException, IOException {
        CreditCardDetails creditCard = new CreditCardDetails();
        creditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
        //Login
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"),
                Utilities.getCredentials("storeId0003"));
        PageBase.HomePageRetail().upgradeEligibilityCheckerLink.click();
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

        //UEC Add Lines Page
        Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
        Assert.assertTrue(PageBase.UECAddLinesPage().eligibleForDF.getText().contains("Eligible for Device Finance\nYes"));
        PageBase.UECAddLinesPage().clickCheckboxForParticularPhoneNumber(phoneNumber);
        PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(imei);
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
        PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton.click();
        PageBase.CommonControls().continueCommonButton.click();

        //Sprint Shop Plans Page
        Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton);
        PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
        Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "1");
        String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
        String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
        PageBase.CartPage().continueCartButton.click();

        //Verizon Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);

        //PageBase.VerizonSelectPlanFeaturesPage().basicPhoneDiscount0Checkbox.click();
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().noInsuranceSprintFirstMob.click();
        PageBase.SelectProtectionPlanInsurancePage().noInsuranceSprintFirstMob.click();
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

		/*Order Review and Confirm Page */
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().phonePriceLine1.isDisplayed());
        //Assert.assertEquals(OrderReviewAndConfirmPage().phoneMonthlyFee.getText(), phonePrice);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().existingPlanDiv.isDisplayed());
        String existingPlan = PageBase.OrderReviewAndConfirmPage().existingPlanDiv.getText();
        //Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().twoYearsUpgradeLabel.isDisplayed());
        //Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().upgradeFeeValueText.isDisplayed());
        String upgradeFee = PageBase.OrderReviewAndConfirmPage().upgradeFeeValueText.getText();
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
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
            //TODO:Need to add assertion for store location.
            // WebElement web = driver.findElement(By.xpath("//span[contains(text(),'2766 - TARGET - SAN FRANCISCO CENTRAL')]"));
            // String storeLocation = web.getText();
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();
            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();
            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei, simType1);
            //Device Financing Installment Contract.
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
            PageBase.DeviceFinancingInstallmentContractPage().print.click();
            Utilities.implicitWaitSleep(3000);
            Robot robot = new Robot();
            Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
            Utilities.implicitWaitSleep(6000);
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
            PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox.click();
            // Order Activation Complete page.
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

    private String returnFlowQA4263(@Optional String testType, String parentOrderId, String state, String iMEINumber1,
                                    IdType idType, String idNumber, String firstName, String lastName)
            throws IOException {

        //Login to retail page.
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        PageBase.HomePageRetail().guestLookupTab.click();
        PageBase.CustomerLookupPage().viewGuestOrders.click();
        PageBase.CustomerLookupPage().continueButton.click();

        //Select a Particular Order
        driver.navigate().to(readConfig("CustomerVerification") + parentOrderId);
        Utilities.waitForElementVisible(PageBase.GuestVerificationPage().idTypeDropdown);
        PageBase.GuestVerificationPage().populateGuestVerificationDetails(idType, state,
                idNumber, firstName, lastName);
        Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().orderID);
        orderId = PageBase.ReturnScanDevicePage().orderID.getText();
        PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(iMEINumber1);
        PageBase.CustomerLookupPage().continueButton.click();
        PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
        PageBase.ReturnOrExhangePreConditions().continueREVButton.click();
        PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
        PageBase.ReturnOrExchangeVerificationPage().returnDEVICE.click();
        Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().returnReasons,
                Utilities.SelectDropMethod.SELECTBYINDEX, "1");
        PageBase.CustomerLookupPage().continueButton.click();

        if (driver.getCurrentUrl().contains("passwordcapture")) {
            PageBase.VerizonAccountPassword().password.sendKeys("Hello");
            PageBase.VerizonAccountPassword().continueButton.click();
        } else if (driver.getCurrentUrl().contains("printticket")) {
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();
        }

        Assert.assertTrue(driver.findElement(By.xpath("//h2[contains(text(), 'Support Center')]")).isDisplayed());

        // Commenting below Assertions as this doesn't show up in Carrier Responder
        if (driver.getCurrentUrl().contains("passwordcapture")) {
            PageBase.VerizonAccountPassword().password.sendKeys("Hello");
            PageBase.VerizonAccountPassword().continueButton.click();
        }
        //*PageBase.CommonControls().continueButton.click();
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

    private CarrierCreditCheckDetails getCarrierCreditCheckDetails() throws IOException {
        CarrierCreditCheckDetails cccDetails = new CarrierCreditCheckDetails();
        PageBase.CSVOperations();
        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(DRIVERLICENCE);
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
        cccDetails.setIDType(DRIVERLICENCE);
        cccDetails.setIdTypeState(customerDetails.IDState);
        cccDetails.setIdNumber(customerDetails.IDNumber);
        cccDetails.setMonth(customerDetails.IDExpirationMonth);
        cccDetails.setYear(customerDetails.IDExpirationYear);
        return cccDetails;
    }

    public void selectCarrierResponderQA88(String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            if (readConfig("internalTestType").toLowerCase().contains("simulator")) {
                PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");
                PageBase.AdminPage().selectAPIConfig("Sprint");
                PageBase.AdminPage().sptCreditCheck("APPROVED");
                PageBase.AdminPage().save();
            } else {
                PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");
            }
            //Selecting Carrier Responder
        } else {   //External
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
        }
    }

    public String poaCompleteFlowQA88() throws IOException {

        DependantTestCaseInputs dependantValues = Utilities.ReadFromCSV("QA_87");
        //		if(dependantValues.TC_ID == "" ||dependantValues.ORDER_ID==""){
        //			AddNewLineTests addNewLine = new AddNewLineTests();
        //			addNewLine.QA50_VerizonNonEdgeWithNumberPortCCInCA(BrowserSettings.readConfig("test-type"));
        //			dependantValues = Utilities.ReadFromCSV("QA_87");
        //			if(dependantValues.TC_ID == "" ||dependantValues.ORDER_ID==""){
        //				Reporter.log("<br> Data is not available from QA_87");
        //				Assert.fail("Data is not available from QA_87");
        //			}
        //		}


        String[] imeiAndSim1 = PageBase.CSVOperations().GetIMEIAndSimNumber(FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        String[] imeiAndSim2 = PageBase.CSVOperations().GetIMEIAndSimNumber(FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        String imei1 = imeiAndSim1[0];
        String simType1 = imeiAndSim1[1];
        String imei2 = imeiAndSim2[0];
        String simType2 = imeiAndSim2[1];

        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        PageBase.HomePageRetail().guestLookupTab.click();
        PageBase.CustomerLookupPage().receiptIdTextbox.sendKeys(dependantValues.RECEIPT_ID);
        Reporter.log(dependantValues.RECEIPT_ID);

        List<WebElement> SubmitButtons = driver.findElements(By.xpath(ControlLocators.SUBMIT_RECEIPTID));
        for (WebElement visibleSubmitButton : SubmitButtons) {
            if (visibleSubmitButton.isDisplayed()) {
                visibleSubmitButton.click();
                break;
            }
        }


        //Select a Particular Order
        driver.navigate().to(readConfig("CustomerVerification") + dependantValues.ORDER_ID);
        Utilities.waitForElementVisible(PageBase.GuestVerificationPage().idTypeDropdown);
        PageBase.GuestVerificationPage().populateGuestVerificationDetails(IdType.valueOf(dependantValues.ID_TYPE),
                dependantValues.STATE, dependantValues.ID_NUMBER, dependantValues.FIRST_NAME,
                dependantValues.LAST_NAME);
        Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().orderID);

        PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(dependantValues.ESN_IMEI1);
        PageBase.CustomerLookupPage().continueButton.click();
        PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
        PageBase.ReturnOrExhangePreConditions().continueREVButton.click();
        PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
        PageBase.ReturnOrExchangeVerificationPage().returnDEVICE.click();
        Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().returnReasons,
                Utilities.SelectDropMethod.SELECTBYINDEX, "1");
        PageBase.CustomerLookupPage().continueButton.click();

        if (driver.getCurrentUrl().contains("passwordcapture")) {
            PageBase.VerizonAccountPassword().password.sendKeys("Hello");
            PageBase.VerizonAccountPassword().continueButton.click();
        } else if (driver.getCurrentUrl().contains("returnticket.htm")) {
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();
        }

        driver.findElement(By.xpath(".//*[contains(text(),'return home')]")).click();

        return ChildOrderId;
    }

    public void QA_88_shipAdminVerification(String orderId) {

    }


    //Endregion
}