package pages;

import framework.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.Utilities.SelectDropMethod;

import java.io.IOException;
import java.util.List;

public class PortMyNumbersPage {

	// region Page Initialization
	public PortMyNumbersPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// end region Page Initialization

	@FindBy(xpath = ControlLocators.PORT_NUMBER_TEXT)
	public WebElement portNumberText;

	@FindBy(xpath = ControlLocators.CURRENT_PHONE_NUMBER)
	public WebElement currentPhoneNumber;

	@FindBy(xpath = ControlLocators.SELECT_CARRIER)
	public WebElement selectCarrier;

	@FindBy(xpath = ControlLocators.CURRENT_ACCOUNT_NUMBER)
	public WebElement currentAccountNumber;

	@FindBy(xpath = ControlLocators.PORT_SSN)
	public WebElement ssn;

	@FindBy(xpath = ControlLocators.USE_A_NEW_ADDRESS_BUTTON)
	public WebElement useANewAddressButton;

	@FindBy(xpath = ControlLocators.PORT_FIRST_NAME)
	public WebElement firstName;

	@FindBy(xpath = ControlLocators.PORT_LAST_NAME)
	public WebElement lastName;

	@FindBy(xpath = ControlLocators.ADDRESS_1)
	public WebElement address1;

	@FindBy(xpath = ControlLocators.ADDRESS_2)
	public WebElement address2;

	@FindBy(xpath = ControlLocators.PORT_CITY)
	public WebElement city;

	@FindBy(xpath = ControlLocators.PORT_STATE)
	public WebElement state;

	@FindBy(xpath = ControlLocators.PORT_ZIP)
	public WebElement zip;

	public void enterPortData(String phoneNo, String carrier, String accountNo,
			String sSN, String fName, String lName, String addr1, String addr2,
			String cITY, String sTATE, String zIP) {
		Utilities.waitForElementVisible(portNumberText);
		portNumberText.click();
		currentPhoneNumber.sendKeys(phoneNo);
		Utilities.dropdownSelect(selectCarrier, SelectDropMethod.SELECTBYTEXT,
				carrier);
		currentAccountNumber.clear();
		currentAccountNumber.sendKeys(accountNo);
		ssn.clear();
		ssn.sendKeys(sSN);
		firstName.clear();
		firstName.sendKeys(fName);
		lastName.clear();
		lastName.sendKeys(lName);
		address1.clear();
		address1.sendKeys(addr1);
		address2.clear();
		address2.sendKeys(addr2);
		city.clear();
		city.sendKeys(cITY);
		Utilities.dropdownSelect(state, SelectDropMethod.SELECTBYTEXT, sTATE);
		zip.clear();
		zip.sendKeys(zIP);
		PageBase.CommonControls().continueButton.click();
	}

	public void populatePortData() throws IOException {
		PageBase.CSVOperations();
		NumPortingDetails portdetails = CSVOperations.ReadPortingDetails();
		PageBase.CSVOperations();
		CustomerDetails custDetails = CSVOperations
				.ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
		Utilities.waitForElementVisible(portNumberText);
		portNumberText.click();
		currentPhoneNumber.sendKeys(portdetails.CurrentPhoneNumber);
		Utilities.dropdownSelect(selectCarrier, SelectDropMethod.SELECTBYTEXT,
				portdetails.Carrier);
		currentAccountNumber.clear();
		currentAccountNumber.sendKeys(portdetails.CurrentAccNumber);
		ssn.clear();
		ssn.sendKeys(portdetails.SSN);
		useANewAddressButton.click();
		firstName.clear();
		firstName.sendKeys(custDetails.FirstName);
		lastName.clear();
		lastName.sendKeys(custDetails.LastName);
		address1.clear();
		address1.sendKeys(custDetails.Address1);
		address2.clear();
		address2.sendKeys(custDetails.Address1);
		city.clear();
		city.sendKeys(custDetails.City);
		Utilities.dropdownSelect(state, SelectDropMethod.SELECTBYTEXT,
				custDetails.State);
		zip.clear();
		zip.sendKeys(custDetails.Zip);
		PageBase.CommonControls().continueButton.click();
	}

	public void enterMultiplePortData(int srNo, String phoneNo, String carrier,
			String accountNo, String sSN, String fName, String lName,
			String addr1, String addr2, String cITY, String sTATE, String zIP) {
		List<WebElement> portNumberList = BrowserSettings.driver
				.findElements(By.xpath(ControlLocators.PORT_NUMBER_TEXT));
		portNumberList.get(srNo).click();
		List<WebElement> currentPhoneNumberList = BrowserSettings.driver
				.findElements(By.xpath(ControlLocators.CURRENT_PHONE_NUMBER));
		currentPhoneNumberList.get(srNo).sendKeys(phoneNo);
		List<WebElement> selectCarrierList = BrowserSettings.driver
				.findElements(By.xpath(ControlLocators.SELECT_CARRIER));
		// selectCarrierList.get(0).click();
		Utilities.dropdownSelect(selectCarrierList.get(srNo),
				SelectDropMethod.SELECTBYTEXT, carrier);
		List<WebElement> currentAccountNumberList = BrowserSettings.driver
				.findElements(By.xpath(ControlLocators.CURRENT_ACCOUNT_NUMBER));
		currentAccountNumberList.get(srNo).clear();
		currentAccountNumberList.get(srNo).sendKeys(accountNo);
		List<WebElement> ssnDataList = BrowserSettings.driver.findElements(By
				.xpath(ControlLocators.PORT_SSN));
		ssnDataList.get(srNo).clear();
		ssnDataList.get(srNo).sendKeys(sSN);

		List<WebElement> firstNameList = BrowserSettings.driver.findElements(By
				.xpath(ControlLocators.PORT_FIRST_NAME));
		firstNameList.get(srNo).clear();
		firstNameList.get(srNo).sendKeys(fName);

		List<WebElement> lastNameList = BrowserSettings.driver.findElements(By
				.xpath(ControlLocators.PORT_LAST_NAME));
		lastNameList.get(srNo).clear();
		lastNameList.get(srNo).sendKeys(lName);

		List<WebElement> address1List = BrowserSettings.driver.findElements(By
				.xpath(ControlLocators.ADDRESS_1));
		address1List.get(srNo).clear();
		address1List.get(srNo).sendKeys(addr1);

		List<WebElement> address2List = BrowserSettings.driver.findElements(By
				.xpath(ControlLocators.ADDRESS_2));
		address2List.get(srNo).clear();
		address2List.get(srNo).sendKeys(addr2);

		List<WebElement> cityList = BrowserSettings.driver.findElements(By
				.xpath(ControlLocators.PORT_CITY));
		cityList.get(srNo).clear();
		cityList.get(srNo).sendKeys(cITY);

		List<WebElement> zipList = BrowserSettings.driver.findElements(By
				.xpath(ControlLocators.PORT_ZIP));
		zipList.get(srNo).clear();
		zipList.get(srNo).sendKeys(zIP);

		List<WebElement> stateList = BrowserSettings.driver.findElements(By
				.xpath(ControlLocators.PORT_STATE));
		Utilities.dropdownSelect(stateList.get(srNo),
				SelectDropMethod.SELECTBYTEXT, sTATE);
	}

	public void enterPortDataForPreCreditCheck(String phoneNo, String carrier,
			String accountNo, String sSN) {
		Utilities.waitForElementVisible(portNumberText);
		portNumberText.click();
		currentPhoneNumber.sendKeys(phoneNo);
		Utilities.dropdownSelect(selectCarrier, SelectDropMethod.SELECTBYTEXT,
				carrier);
		currentAccountNumber.clear();
		currentAccountNumber.sendKeys(accountNo);
		ssn.clear();
		ssn.sendKeys(sSN);
		PageBase.CommonControls().continueButton.click();
	}

	public void enterMultiplePortData(int srNo, String phoneNo, String carrier,
			String accountNo, String sSN) {

		List<WebElement> portNumberList = BrowserSettings.driver
				.findElements(By.xpath(ControlLocators.PORT_NUMBER_TEXT));
		portNumberList.get(srNo).click();
		List<WebElement> currentPhoneNumberList = BrowserSettings.driver
				.findElements(By.xpath(ControlLocators.CURRENT_PHONE_NUMBER));
		currentPhoneNumberList.get(srNo).sendKeys(phoneNo);
		List<WebElement> selectCarrierList = BrowserSettings.driver
				.findElements(By.xpath(ControlLocators.SELECT_CARRIER));
		// selectCarrierList.get(0).click();
		Utilities.dropdownSelect(selectCarrierList.get(srNo),
				SelectDropMethod.SELECTBYTEXT, carrier);
		List<WebElement> currentAccountNumberList = BrowserSettings.driver
				.findElements(By.xpath(ControlLocators.CURRENT_ACCOUNT_NUMBER));
		currentAccountNumberList.get(srNo).clear();
		currentAccountNumberList.get(srNo).sendKeys(accountNo);
		List<WebElement> ssnDataList = BrowserSettings.driver.findElements(By
				.xpath(ControlLocators.PORT_SSN));
		ssnDataList.get(srNo).clear();
		ssnDataList.get(srNo).sendKeys(sSN);
	}
}