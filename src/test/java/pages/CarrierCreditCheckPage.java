package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import framework.ControlLocators;
import framework.PageBase;
import framework.Utilities;
import framework.Utilities.SelectDropMethod;

public class CarrierCreditCheckPage {

	public CarrierCreditCheckPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// region-Carrier Credit Check Elements
	@FindBy(xpath = ControlLocators.CCC_POPULATE_FORM)
	public WebElement populateForm;

	@FindBy(xpath = ControlLocators.POPULATE_SUBMIT_FORM)
	public WebElement populateSubmitForm;

	@FindBy(xpath = ControlLocators.SKIP)
	public WebElement skip;

	@FindBy(id = ControlLocators.FIRST_NAME)
	public WebElement firstNameTextBox;

	@FindBy(id = ControlLocators.MIDDLE_NAME)
	public WebElement middleNameTextBox;

	@FindBy(id = ControlLocators.LAST_NAME)
	public WebElement lastNameTextBox;

	@FindBy(id = ControlLocators.ADDRESS1)
	public WebElement address1TextBox;

	@FindBy(id = ControlLocators.ADDRESS2)
	public WebElement address2TextBox;

	@FindBy(id = ControlLocators.CITY)
	public WebElement cityTextBox;

	@FindBy(id = ControlLocators.STATE_DD)
	public WebElement stateDD;

	@FindBy(id = ControlLocators.ZIP)
	public WebElement zipTextBox;

	@FindBy(id = ControlLocators.HOME_PHONE)
	public WebElement homePhoneTextBox;

	@FindBy(id = ControlLocators.EMAIL)
	public WebElement emailIdTextBox;

	@FindBy(id = ControlLocators.MONTH_DD)
	public WebElement monthDD;

	@FindBy(id = ControlLocators.DAY_DD)
	public WebElement dayDD;

	@FindBy(id = ControlLocators.YEAR_DD)
	public WebElement yearDD;

	@FindBy(id = ControlLocators.SSN)
	public WebElement ssnTextBox;

	@FindBy(id = ControlLocators.ID_TYPE_DD)
	public WebElement idTypeDD;

	@FindBy(id = ControlLocators.ID_STATE_DD)
	public WebElement idStateDD;

	@FindBy(id = ControlLocators.ID_NUMBER)
	public WebElement idNumberTextBox;

	@FindBy(id = ControlLocators.ID_EXPIRATION_MONTH_DD)
	public WebElement idExpirationMonthDD;

	@FindBy(id = ControlLocators.ID_EXPIRATION_YEAR_DD)
	public WebElement idExpirationYearDD;

	@FindBy(id = ControlLocators.GUEST_AGREES_CREDIT_CHECK)
	public WebElement guestAgreeToRunCCCheckBox;

	@FindBy(xpath = ControlLocators.CONTINUE_DEPOSIT_PAGE)
	public WebElement continueAfterGuestAgreesToPayDeposit;

	@FindBy(xpath = ControlLocators.AGREE_DESPOIT_$100)
	public WebElement guestAgreeForDeposit;

	// endregion

	// region - Methods
	public void carrierCreditCheckUsingPopulateSubmitForm() {
		PageBase.CarrierCreditCheckPage().populateSubmitForm.click();
		PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
		PageBase.CommonControls().continueButton.click();
	}

	public void populatingCarrierCreditCheckPage(
			CarrierCreditCheckDetails cccDetails) {
		firstNameTextBox.clear();
		firstNameTextBox.sendKeys(cccDetails.getFirstName());
		// middleNameTextBox.clear();
		// middleNameTextBox.sendKeys(middleInitial);
		lastNameTextBox.clear();
		lastNameTextBox.sendKeys(cccDetails.getLastName());
		address1TextBox.clear();
		address1TextBox.sendKeys(cccDetails.getAddress1());
		// address2TextBox.clear();
		// address2TextBox.sendKeys(cccDetails.getAddress2());
		cityTextBox.clear();
		cityTextBox.sendKeys(cccDetails.getCity());

		Utilities.dropdownSelect(stateDD, SelectDropMethod.SELECTBYTEXT,
				cccDetails.getState());

		zipTextBox.clear();
		zipTextBox.sendKeys(cccDetails.getZip());

		homePhoneTextBox.clear();
		homePhoneTextBox.sendKeys(cccDetails.getHomePhone());

		emailIdTextBox.clear();
		emailIdTextBox.sendKeys(cccDetails.getEmail());

		Utilities.dropdownSelect(monthDD, SelectDropMethod.SELECTBYTEXT,
				cccDetails.getBirthMonth());
		Utilities.dropdownSelect(dayDD, SelectDropMethod.SELECTBYTEXT,
				cccDetails.getBirthDate());
		Utilities.dropdownSelect(yearDD, SelectDropMethod.SELECTBYTEXT,
				cccDetails.getBirthYear());

		ssnTextBox.clear();
		ssnTextBox.sendKeys(cccDetails.getSSN());

		switch (cccDetails.getIDType()) {
		case DRIVERLICENCE:
			Utilities.dropdownSelect(idTypeDD,
					Utilities.SelectDropMethod.SELECTBYINDEX, "1");
			break;
		case USPASSPORT:
			Utilities.dropdownSelect(idTypeDD,
					Utilities.SelectDropMethod.SELECTBYINDEX, "2");
			break;
		case STATEID:
			Utilities.dropdownSelect(idTypeDD,
					Utilities.SelectDropMethod.SELECTBYINDEX, "3");
			break;
		}

		Utilities.dropdownSelect(idStateDD,
				Utilities.SelectDropMethod.SELECTBYTEXT,
				cccDetails.getIdTypeState());

		idNumberTextBox.clear();
		idNumberTextBox.sendKeys(cccDetails.getIdNumber());
		PageBase.ServiceProviderVerificationPage().SetMonth(
				idExpirationMonthDD, cccDetails.getMonth().toString());
		Utilities.dropdownSelect(idExpirationYearDD,
				Utilities.SelectDropMethod.SELECTBYTEXT, cccDetails.getYear());
		Reporter.log("<br> User: " + cccDetails.getFirstName() + " "
				+ cccDetails.getLastName());
	}
	// endregion
}