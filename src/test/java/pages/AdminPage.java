package pages;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.BrowserSettings;
import framework.ControlLocators;
import framework.Utilities;
import framework.Utilities.SelectDropMethod;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openqa.selenium.By.xpath;

public class AdminPage {
    // region Veariable Declarations
    public static final String IGNORED_DB_ERROR = "notice: *exact same* order class for ordId";
    public static final String PHP_WARNING = "PHP WARNING :";
    final static Charset ENCODING = StandardCharsets.UTF_8;
    public static List<String> erros = new ArrayList<String>();

    // endregion

    public AdminPage(WebDriver d) {
        PageFactory.initElements(d, this);
    }

    // region Web Elements
    @FindBy(id = ControlLocators.CREDIT_READ)
    public WebElement creditRead;

    @FindBy(name = ControlLocators.CREDIT_READ_DD)
    public WebElement creditReadDropDown;

    @FindBy(name = ControlLocators.CREDIT_WRITE_DD)
    public WebElement creditWriteDropDown;

    @FindBy(name = ControlLocators.RETRIEVE_CUSTOMER_DETAILS)
    public WebElement retrieveCustomerDetails;

    @FindBy(name = ControlLocators.ACCOUNT_PLAN_TYPE)
    public WebElement accountPlanType;

    @FindBy(xpath = ControlLocators.ADD_PHONE_NUMBERS)
    public WebElement addPhoneNumbers;

    @FindBy(xpath = ControlLocators.PHONE_LIST)
    public WebElement phoneList;

    @FindBy(xpath = ControlLocators.FIRST_PHONE_TEXT_BOX)
    public WebElement firstPhoneTextBox;

    @FindBy(xpath = ControlLocators.SECOND_PHONE_TEXT_BOX)
    public WebElement secondPhoneTextBox;

    @FindBy(xpath = ControlLocators.SECOND_PHONE_REMOVE)
    public WebElement secondPhoneRemoveTextBox;

    @FindBy(name = ControlLocators.RETRIEVE_EXISTING_CUSTOMER_INSTALLEMENTS_DETAILS)
    public WebElement retrieveExistingCustomerInstallmentsDetails;

    @FindBy(name = ControlLocators.RETRIEVE_PRICE_PLANS)
    public WebElement retrievePricePlan;

    @FindBy(name = ControlLocators.SUBMIT_ACTIVATION)
    public WebElement submitActivation;

    @FindBy(name = ControlLocators.SUBMIT_CREDIT_APPLICATION)
    public WebElement submitCreditApplication;

    @FindBy(name = ControlLocators.SUBMIT_RECIEPT)
    public WebElement submitReciept;

    @FindBy(name = ControlLocators.SUBMIT_SERVICE_DETAILS)
    public WebElement submitServiceDetails;

    @FindBy(name = ControlLocators.SUBMIT_EDGE_UP_PAYMENT)
    public WebElement submitEdgeUpPayment;

    @FindBy(name = ControlLocators.RETURN_OR_EXCHANGE_DEVICE)
    public WebElement returnOrExchangeDevice;

    @FindBy(name = ControlLocators.SAVE)
    public WebElement save;

    @FindBy(name = ControlLocators.VERIZON_BS)
    public WebElement verizonBackendSimulator;

    @FindBy(xpath = ControlLocators.VERIZON_CR)
    public WebElement verizonCarrierResponder;

    @FindBy(xpath = ControlLocators.VERIZON_EXTERNAL)
    public WebElement verizonExternal;

    @FindBy(name = ControlLocators.ATT_BS)
    public WebElement attBackendSimulator;

    @FindBy(xpath = ControlLocators.ATT_CR)
    public WebElement attCarrierResponder;

    @FindBy(xpath = ControlLocators.ATT_EXTERNAL)
    public WebElement attExternal;

    @FindBy(name = ControlLocators.SPRINT_BS)
    public WebElement sprintBackendSimulator;

    @FindBy(xpath = ControlLocators.SPRINT_CR)
    public WebElement sprintCarrierResponder;

    @FindBy(xpath = ControlLocators.SPRINT_EXTERNAL)
    public WebElement sprintExternal;

    @FindBy(xpath = ControlLocators.ATT_APICONFIG_LINK)
    public WebElement attAPIConfigLink;

    @FindBy(xpath = ControlLocators.VERIZON_APICONFIG_LINK)
    public WebElement verizonAPIConfigLink;

    @FindBy(xpath = ControlLocators.SPRINT_APICONFIG_LINK)
    public WebElement sprintAPIConfigLink;

    @FindBy(name = ControlLocators.CHECK_LOAN_ELIGIBILITY)
    public WebElement checkLoanEligibility;
    // EndRegion
    // Region - DBError Web Element
    @FindBy(id = ControlLocators.DBERROR_FILTER)
    public WebElement filter;

    @FindBy(xpath = ControlLocators.DBERROR_SECTION)
    public WebElement dbErrorSection;

    @FindBy(xpath = ControlLocators.DBERROR_SECTION_BR)
    public WebElement dbErrorBody1;

    @FindBy(name = ControlLocators.SEARCH_WIDGET_TEXTBOX)
    public WebElement widgetNameSearchTextbox;

    @FindBy(name = ControlLocators.SEARCH_WIDGET_BUTTON)
    public WebElement widgetSearchButton;

    @FindBy(xpath = ControlLocators.SERVICE_CHARGE_BREAKDOWN_LINK)
    public WebElement serviceChargeBreakdownLink;

    @FindBy(xpath = ControlLocators.WIDGET_CONTENT)
    public WebElement widgetContent;

    @FindBy(xpath = ControlLocators.UP_LEVEL)
    public WebElement upLevel;

    @FindBy(name = ControlLocators.SPRINT_ACCOUNT_VALIDATION)
    public WebElement sptAccountValidationDropDown;

    @FindBy(name = ControlLocators.SPRINT_ACTIVATION)
    public WebElement sptActivationDropDown;

    @FindBy(name = ControlLocators.SPRINT_LOAN_ELIGIBILITY)
    public WebElement sptLoanEligibilityDropDown;

    @FindBy(name = ControlLocators.SPRINT_LOAN_CONTRACT)
    public WebElement sptLoanContractDropDown;

    @FindBy(name = ControlLocators.SPRINT_CREDIT_CHECK)
    public WebElement sptCreditCheckDropDown;

    @FindBy(name = ControlLocators.SPRINT_PORT_ELIGIBILITY)
    public WebElement sptPortEligibilityDropDown;

    @FindBy(name = ControlLocators.SPRINT_PRE_AUTHORIZATION)
    public WebElement sptPreAuthorizationDropDown;

    @FindBy(name = ControlLocators.SPRINT_PROCESS_DEVICE_RETURN)
    public WebElement sptProcessDeviceReturnDropDown;

    @FindBy(name = ControlLocators.SPRINT_SERVICE_VALIDATION)
    public WebElement sptServiceValidationDropDown;

    @FindBy(name = ControlLocators.SPRINT_PLAN_TYPE)
    public WebElement sptPlanTypeDropDown;

    @FindBy(id = ControlLocators.ORDERID_LINK_APIHISTORY)
    public WebElement orderIdLink;

    @FindBy(xpath = ControlLocators.ACTIVATION_API)
    public WebElement lastActivationAPI;

    @FindBy(xpath = ControlLocators.ACTIVATION_API_LINK)
    public WebElement lastActivationAPILink;

    @FindBy(xpath = ControlLocators.RETRIEVE_PRICE_PLAN)
    public WebElement lastRetrievePricePlanAPI;

    @FindBy(xpath = ControlLocators.CREATE_LOAN_CONTRACT)
    public WebElement createLoanContract;

    @FindBy(xpath = ControlLocators.SUBMIT_CREDIT_APPLICATION_API)
    public WebElement submitCreditApplicationAPI;

    @FindBy(xpath = ControlLocators.RETRIEVE_CREDIT_APPLICATION_API)
    public WebElement retieveCreditApplicationAPI;

    @FindBy(xpath = ControlLocators.SUBMIT_PAYMENT_API)
    public WebElement submitPaymentAPICreditApplicationAPI;

    @FindBy(xpath = ControlLocators.RETIEVE_NPANXX_API)
    public WebElement retrieveNPANXXAPI;

    @FindBy(xpath = ControlLocators.SUBMIT_SERVICE_DETAILS_API)
    public WebElement submitServiceDetailsAPI;

    @FindBy(xpath = ControlLocators.SUBMIT_ACTIVATION_API)
    public WebElement submitActivationAPI;

    @FindBy(xpath = ControlLocators.SUBMIT_RECIEPT_API)
    public WebElement submitRecieptAPI;

    @FindBy(xpath = ControlLocators.RETRIEVE_PORTRAIT_RECEIPT_API)
    public WebElement retrievePortraitRecieptAPI;

    @FindBy(name = ControlLocators.SP_CREDIT_CHECK)
    public WebElement spCreditCheck;

    @FindBy(name = ControlLocators.SP_PORT_ELIGIBILITY)
    public WebElement spPortEligibility;

    public void spCreditCheck(String useCase) {
        Utilities.dropdownSelect(spCreditCheck, SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void spPortEligibility(String useCase) {
        Utilities.dropdownSelect(spPortEligibility, SelectDropMethod.SELECTBYTEXT, useCase);
    }
    // endregion

    // region Methods
    public void navigateToSimulator() throws IOException {
        BrowserSettings.driver.navigate().to(
                BrowserSettings.readConfig("baseUrl")
                        + "/admin/qa/webapisimulator/");
    }

    public void selectWebAPIResponse(String carrierType, String responseType) {
        if (carrierType.equals("ATT")) {
            if (responseType.equals("BackendSimulator")) {
                Utilities.waitForElementVisible(attBackendSimulator);
                attBackendSimulator.click();
            } else if (responseType.equals("CarrierResponder")) {
                Utilities.waitForElementVisible(attCarrierResponder);
                attCarrierResponder.click();
            } else {
                Utilities.waitForElementVisible(attExternal);
                attExternal.click();
            }
        } else if (carrierType.equals("Verizon")) {
            if (responseType.equals("BackendSimulator")) {
                Utilities.waitForElementVisible(verizonBackendSimulator);
                verizonBackendSimulator.click();
            } else if (responseType.equals("CarrierResponder")) {
                Utilities.waitForElementVisible(verizonCarrierResponder, 10);
                verizonCarrierResponder.click();
            } else {
                Utilities.waitForElementVisible(verizonExternal);
                verizonExternal.click();
            }
        } else {
            if (responseType.equals("BackendSimulator")) {
                Utilities.waitForElementVisible(sprintBackendSimulator);
                sprintBackendSimulator.click();
            } else if (responseType.equals("CarrierResponder")) {
                Utilities.waitForElementVisible(sprintCarrierResponder);
                sprintCarrierResponder.click();
            } else {
                Utilities.waitForElementVisible(sprintExternal);
                sprintExternal.click();
            }
        }
    }

    public void selectAPIConfig(String carrierType) {
        if (carrierType.equals("ATT")) {
            Utilities.waitForElementVisible(attAPIConfigLink);
            attAPIConfigLink.click();
        } else if (carrierType.equals("Verizon")) {
            try {
                BrowserSettings.driver.navigate().to(BrowserSettings.readConfig("baseUrl")
                        + "/admin/qa/carrierresponder/index.php");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Utilities.implicitWaitSleep(10000);
//            Actions action = new Actions(BrowserSettings.driver);
//            action.moveToElement(verizonAPIConfigLink).doubleClick().build().perform();
            //verizonAPIConfigLink.click();
        } else {
            Utilities.waitForElementVisible(sprintAPIConfigLink);
            sprintAPIConfigLink.click();
        }
    }

    // endregion

    // region Backend Simulator API Selection
    public void selectCreaditReadUseCase(String useCase) {
        Utilities.dropdownSelect(creditReadDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void selectCreaditWriteUseCase(String useCase) {
        Utilities.dropdownSelect(creditWriteDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void retrieveCustomerDetails(String useCase) {
        Utilities.dropdownSelect(retrieveCustomerDetails,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void accountPlanType(String useCase) {
        Utilities.dropdownSelect(accountPlanType,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void retrieveExistingCustomerInstallmentsDetails(String useCase) {
        Utilities.dropdownSelect(retrieveExistingCustomerInstallmentsDetails,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void retrievePricePlan(String useCase) {
        Utilities.dropdownSelect(retrievePricePlan,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void submitActivation(String useCase) {
        Utilities.dropdownSelect(submitActivation,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void submitCreditApplication(String useCase) {
        Utilities.dropdownSelect(submitCreditApplication,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void submitReciept(String useCase) {
        Utilities.dropdownSelect(submitReciept, SelectDropMethod.SELECTBYTEXT,
                useCase);
    }

    public void submitServiceDetails(String useCase) {
        Utilities.dropdownSelect(submitServiceDetails,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void submitEdgeUpPayment(String useCase) {
        Utilities.dropdownSelect(submitEdgeUpPayment,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void returnOrExchangeDevice(String useCase) {
        Utilities.dropdownSelect(returnOrExchangeDevice,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void checkLoaanEligibility(String useCase) {
        Utilities.dropdownSelect(checkLoanEligibility,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void save() {
        save.click();
    }

    // endregion Backend Simulator API Selection

    // region DBError Methods

    public void navigateToDBError() throws IOException {
        BrowserSettings.driver.navigate().to(
                BrowserSettings.readConfig("baseUrl")
                        + "/admin/dberrsdump.htm/");
    }

    public void grantAllPermission() throws IOException {
        Utilities utilities = new Utilities();
        BrowserSettings.driver.navigate().to(
                BrowserSettings.readConfig("baseUrl")
                        + ControlLocators.GRANT_PERMISSIONS_URL);
        Utilities.implicitWaitSleep(10000);
        WebElement element = utilities.findByXp(BrowserSettings.driver,
                ControlLocators.GRANT_ALL_PERMISSIONS_EXPR);

        Actions action = new Actions(BrowserSettings.driver);
        action.moveToElement(element).doubleClick().build().perform();
        Utilities.implicitWaitSleep(10000);
        //element.click();
    }

    public int totalErrorCount() {
        WebElement sectionList = BrowserSettings.driver
                .findElement(xpath("//pre"));
        String errorFile = sectionList.getText();

        String[] errorLine = errorFile.split("\n");
        System.out.println(errorLine.length);

        for (String element : errorLine) {
            // Pattern pattern2 =
            // Pattern.compile("Dev Error AT \\d{4}-\\d{2}-\\d{2}\\p{ASCII}\\d[0-2]:\\d{2}:\\d{2}");
            Pattern pattern2 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            Matcher m = pattern2.matcher(element);
            if (m.find()) {
                erros.add(element);
            }
        }
        return erros.size();
    }

    public boolean isDBErrorFound(int beforeRunningTestCount) {
        boolean isPass = false;
        List<String> errorList = new ArrayList<>();
        WebElement sectionList = BrowserSettings.driver
                .findElement(xpath("//pre"));
        String errorFile = sectionList.getText();
        String[] errorLine = errorFile.split("\n");
        System.out.println(errorLine.length);

        for (String element : errorLine) {
            Pattern pattern2 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            Matcher m = pattern2.matcher(element);
            if (m.find()) {
                errorList.add(element);
            }
        }
        if (beforeRunningTestCount < errorList.size()) {
            for (int start = beforeRunningTestCount; start <= errorList.size() - 1; start++) {
                if (errorList.get(start).toLowerCase()
                        .contains(new String(IGNORED_DB_ERROR).toLowerCase()) ||
                        errorList.get(start).toLowerCase()
                                .contains(new String(PHP_WARNING).toLowerCase())) {
                    isPass = true;
                } else {
                    isPass = false;
                    Reporter.log("<br> DBError found :" + errorList.get(start));
                    break;
                }
            }
        } else {
            isPass = true;
        }
        return isPass;
    }

    public void widgetManagementOperations() throws IOException {
        BrowserSettings.driver.navigate().to(
                BrowserSettings.readConfig("baseUrl")
                        + ControlLocators.WIDGET_MANAGEMENT);
        Utilities.implicitWaitSleep(3000);
        widgetNameSearchTextbox.sendKeys("Hide Service Charge Breakdown");
        widgetSearchButton.click();
        Utilities.waitForElementVisible(serviceChargeBreakdownLink);
        serviceChargeBreakdownLink.click();
        Utilities.waitForElementVisible(widgetContent);
        Assert.assertTrue(widgetContent.getText().contains("660"));
        // upLevel.click();
        // upLevel.click();
    }

    public void sptAccountvalidation(String useCase) {
        Utilities.dropdownSelect(sptAccountValidationDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void sptActivation(String useCase) {
        Utilities.dropdownSelect(sptActivationDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void sptLoanEligibility(String useCase) {
        Utilities.dropdownSelect(sptLoanEligibilityDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void sptLoanContract(String useCase) {
        Utilities.dropdownSelect(sptLoanContractDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void sptCreditCheck(String useCase) {
        Utilities.dropdownSelect(sptCreditCheckDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void sptPortEligibility(String useCase) {
        Utilities.dropdownSelect(sptPortEligibilityDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void sptPreAuthorization(String useCase) {
        Utilities.dropdownSelect(sptPreAuthorizationDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void sptProcessDeviceReturn(String useCase) {
        Utilities.dropdownSelect(sptProcessDeviceReturnDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void sptServiceValidation(String useCase) {
        Utilities.dropdownSelect(sptServiceValidationDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }

    public void sptPlanType(String useCase) {
        Utilities.dropdownSelect(sptPlanTypeDropDown,
                SelectDropMethod.SELECTBYTEXT, useCase);
    }
    // endregion DBError Methods
}