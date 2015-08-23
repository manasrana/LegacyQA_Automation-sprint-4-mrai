package pages;

import java.awt.AWTException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.ControlLocators;
import framework.PageBase;
import framework.Utilities;
import framework.Utilities.SelectDropMethod;

public class ServiceProviderVerificationPage {
	public ServiceProviderVerificationPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// region Enums
	public enum IdType {
		DRIVERLICENCE, USPASSPORT, STATEID
	}

	public enum Month {
		JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER

	}

	// EndRegion Enums

	/* region Select Insurance Page Elements */
	@FindBy(id = ControlLocators.POPULATE_FORM)
	public WebElement populateForm;

	@FindBy(className = ControlLocators.POPULATE_FORM_BYCLASS_BUTTON)
	public WebElement populateFormByClassButton;

	@FindBy(xpath = ControlLocators.GUEST_CREDIT_CHECK_CHECKBOX)
	public WebElement guestCreditCheckCheckbox;

	@FindBy(id = ControlLocators.FIRST_NAME_TEXTBOX)
	public WebElement firstNameTextbox;

	@FindBy(id = ControlLocators.MIDDLE_INITIAL_TEXTBOX)
	public WebElement middleInitialTextbox;

	@FindBy(id = ControlLocators.LAST_NAME_TEXTBOX)
	public WebElement lastNameTextbox;

	@FindBy(id = ControlLocators.EMAIL_TEXTBOX)
	public WebElement emailTextbox;

	@FindBy(id = ControlLocators.ID_TYPE_DROPDOWN)
	public WebElement idTypeDropdown;

	@FindBy(id = ControlLocators.STATE_DROPDOWN)
	public WebElement stateDropdown;

	@FindBy(id = ControlLocators.ID_NUMBER_TEXTBOX)
	public WebElement idNumberTextbox;

	@FindBy(id = ControlLocators.MONTH_ID_EXPIRATION_DROPDOWN)
	public WebElement monthIdExpirationDropdown;

	@FindBy(id = ControlLocators.YEAR_ID_EXPIRATION_DROPDOWN)
	public WebElement yearIdExpiratinDropdown;

	@FindBy(id = ControlLocators.CONTINUE_SPV_BUTTON)
	public WebElement continueSPVButton;

	@FindBy(id = ControlLocators.SPV_PHONE_NUMBER)
	public WebElement spvPhoneNumber;

	@FindBy(id = ControlLocators.GUEST_AGREES_CREDIT_CHECK)
	public WebElement guestAgreesCreditCheck;

	@FindBy(id = ControlLocators.SERVICE_BILLING_ADDRESS1)
	public WebElement serviceBillingAddress1;

	@FindBy(id = ControlLocators.SERVICE_BILLING_CITY)
	public WebElement serviceBillingCity;

	@FindBy(id = ControlLocators.SERVICE_BILLING_STATE)
	public WebElement serviceBillingState;

	@FindBy(id = ControlLocators.SERVICE_BILLING_ZIPCODE)
	public WebElement serviceBillingZipCode;

	@FindBy(id = ControlLocators.ADDRESS_1_TEXTBOX)
	public WebElement address1Textbox;

	@FindBy(id = ControlLocators.ADDRESS_2_TEXTBOX)
	public WebElement address2Textbox;

	@FindBy(id = ControlLocators.CITY_TEXTBOX)
	public WebElement cityTextbox;

	@FindBy(id = ControlLocators.ZIP_TEXTBOX)
	public WebElement zipTextbox;

	@FindBy(id = ControlLocators.BIRTH_MONTH_DROPDOWN)
	public WebElement birthMonthDropdown;

	@FindBy(id = ControlLocators.BIRTH_DAY_DROPDOWN)
	public WebElement birthDayDropdown;

	@FindBy(id = ControlLocators.BIRTH_YEAR_DROPDOWN)
	public WebElement birthYearDropdown;

	@FindBy(id = ControlLocators.SSN_TEXTBOX)
	public WebElement sSNTextbox;

	@FindBy(id = ControlLocators.STATE_PI_DROPDOWN)
	public WebElement statePIDropdown;

	@FindBy(id = ControlLocators.PIN_NUMBER)
	public WebElement pinNumber;

	@FindBy(id = ControlLocators.SPRINT_SECURITY_QUESTIONS)
	public WebElement sprintSecurityQuestionsDD;

	@FindBy(id = ControlLocators.SPRINT_SECURITY_ANSWERS)
	public WebElement sprintSecurityAnswers;

	@FindBy(xpath = ControlLocators.YES_RECIEVE_SPRINT_EBILL)
	public WebElement yesRecieveSprintEBill;

	/* region end Select Insurance Page Elements */

	// Region Methods
	// / <summary>
	// / This function will be used to populate Service Provider Verification
	// Page
	// / </summary>
	// / Author:Tarun
	public void populatingSPVPage(String firstName, String lastName,
			String email, IdType idType, String state, String idNumber,
			String month, int year)
	// / This function will be used to populate Service Provider Verification
	// Page
	// / </summary>
	// / Author:Tarun
	{
		firstNameTextbox.clear();
		firstNameTextbox.sendKeys(firstName);
		middleInitialTextbox.clear();
		lastNameTextbox.clear();
		lastNameTextbox.sendKeys(lastName);
		emailTextbox.clear();
		emailTextbox.sendKeys(email);
		switch (idType) {
		case DRIVERLICENCE:
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "1");
			break;
		case USPASSPORT:
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "2");
			break;
		case STATEID:
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "3");
			break;
		}
		state = state.toUpperCase();
		Utilities.dropdownSelect(stateDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, state);
		idNumberTextbox.clear();
		idNumberTextbox.sendKeys(idNumber);

		SetMonth(monthIdExpirationDropdown, month);
		// region Month Selection
		// endregion Month Selection
		String yearI = "" + year;
		Utilities.dropdownSelect(yearIdExpiratinDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, yearI);
	}

	// / <summary>
	// / This function will be used to populate Service Provider Verification
	// Page
	// / </summary>
	// / Author:Rashmi
	public void populatingSPVPage(String firstName, String middleInitial,
			String lastName, String email, IdType idType, String state,
			String idNumber, Month month, int year, String phoneNo,
			String birthMonth, int birthdate, int birthYear) {
		firstNameTextbox.clear();
		firstNameTextbox.click();
		firstNameTextbox.sendKeys(firstName);
		middleInitialTextbox.clear();
		middleInitialTextbox.sendKeys(middleInitial);
		lastNameTextbox.clear();
		lastNameTextbox.sendKeys(lastName);
		spvPhoneNumber.clear();
		spvPhoneNumber.sendKeys(phoneNo);
		emailTextbox.clear();
		emailTextbox.sendKeys(email);

		String birthMonthI = "" + birthMonth;
		String birthdateI = "" + birthdate;
		String yearBirthI = "" + birthYear;
		Utilities.dropdownSelect(PageBase.CarrierCreditCheckPage().monthDD,
				Utilities.SelectDropMethod.SELECTBYTEXT, birthMonthI);
		Utilities.dropdownSelect(PageBase.CarrierCreditCheckPage().dayDD,
				Utilities.SelectDropMethod.SELECTBYTEXT, birthdateI);
		Utilities.dropdownSelect(PageBase.CarrierCreditCheckPage().yearDD,
				Utilities.SelectDropMethod.SELECTBYTEXT, yearBirthI);

		switch (idType) {
		case DRIVERLICENCE:
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "1");
			break;
		case USPASSPORT:
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "2");
			break;
		case STATEID:
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "3");
			break;
		}
		Utilities.dropdownSelect(stateDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, state);
		idNumberTextbox.clear();
		idNumberTextbox.sendKeys(idNumber);
		// Region Month Selection

		SetMonth(monthIdExpirationDropdown, month.toString());

		String yearI = "" + year;
		Utilities.dropdownSelect(yearIdExpiratinDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, yearI);
	}

	// / <summary>
	// / This function will be used to populate Service Provider Verification
	// Page with credit card details.
	// / </summary>
	// / Author:Tarun
	public void populatingSPVPageWithCreditCardDetails(String firstName,
			String middleInitial, String lastName, String address1,
			String address2, String city, String statePI, String zip,
			String phoneNo, String birthMonth, String birthDay,
			String birthYear, String email, String sSNNumber, IdType idType,
			String stateV, String idNumber, String monthIDExpiration,
			String yearIDExpiration) {
		firstNameTextbox.clear();
		firstNameTextbox.sendKeys(firstName);
		middleInitialTextbox.clear();
		middleInitialTextbox.sendKeys(middleInitial);
		lastNameTextbox.clear();
		lastNameTextbox.sendKeys(lastName);
		address1Textbox.clear();
		address1Textbox.sendKeys(address1);
		address2Textbox.clear();
		address2Textbox.sendKeys(address2);
		cityTextbox.clear();
		cityTextbox.sendKeys(city);
		statePI = statePI.toUpperCase();
		Utilities.dropdownSelect(statePIDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, statePI);
		zipTextbox.clear();
		zipTextbox.sendKeys(zip);
		spvPhoneNumber.clear();
		spvPhoneNumber.sendKeys(phoneNo);
		birthMonth = birthMonth.substring(0, 1).toUpperCase()
				+ birthMonth.substring(1).toLowerCase();
		Utilities.dropdownSelect(birthMonthDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, birthMonth);
		Utilities.dropdownSelect(birthDayDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, birthDay);
		Utilities.dropdownSelect(birthYearDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, birthYear);
		emailTextbox.clear();
		emailTextbox.sendKeys(email);

		sSNTextbox.clear();
		sSNTextbox.sendKeys(sSNNumber);
		switch (idType) {
		case DRIVERLICENCE:
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "1");
			break;
		case USPASSPORT:
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "2");
			break;
		case STATEID:
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "3");
			break;
		}
		stateV = stateV.toUpperCase();
		Utilities.dropdownSelect(stateDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, stateV);
		idNumberTextbox.clear();
		idNumberTextbox.sendKeys(idNumber);
		monthIDExpiration = monthIDExpiration.substring(0, 1).toUpperCase()
				+ monthIDExpiration.substring(1).toLowerCase();
		Utilities.dropdownSelect(monthIdExpirationDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, monthIDExpiration);
		Utilities.dropdownSelect(yearIdExpiratinDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, yearIDExpiration);
	}

	public void populatingSPVPageAllMandateFields(String firstName,
			String lastName, String address1, String city, String state,
			String zipCode, String homePhone, String email, String birthMonth,
			String birthDay, String birthYear, String idType,
			String IdIssueState, String idNumber, String idExpiryMonth,
			String IdExpiryYear) throws AWTException, InterruptedException {
		Utilities.setValue(firstNameTextbox, firstName);
		Utilities.setValue(lastNameTextbox, lastName);
		Utilities.setValue(serviceBillingAddress1, address1);
		Utilities.setValue(serviceBillingCity, city);
		Utilities.setValue(spvPhoneNumber, homePhone);
		Utilities.setValue(serviceBillingZipCode, zipCode);
		state = state.toUpperCase();
		Utilities.dropdownSelect(stateDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, state);

		Utilities.dropdownSelect(PageBase.CarrierCreditCheckPage().monthDD,
				SelectDropMethod.SELECTBYTEXT, birthMonth);
		Utilities.dropdownSelect(PageBase.CarrierCreditCheckPage().dayDD,
				SelectDropMethod.SELECTBYTEXT, birthDay);
		Utilities.dropdownSelect(PageBase.CarrierCreditCheckPage().yearDD,
				SelectDropMethod.SELECTBYTEXT, birthYear);
		Utilities.setValue(emailTextbox, email);

		if (idType.toLowerCase().contains("driver"))
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "1");
		else if (idType.toLowerCase().contains("passport"))
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "2");
		else
			Utilities.dropdownSelect(idTypeDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "3");

		state = state.toUpperCase();
		Utilities.dropdownSelect(serviceBillingState,
				Utilities.SelectDropMethod.SELECTBYTEXT, state);
		Utilities.ScrollToElement(serviceBillingState);
		Utilities.setValue(idNumberTextbox, idNumber);
		SetMonth(monthIdExpirationDropdown, idExpiryMonth);

		// region Month Selection

		// endregion Month Selection
		Utilities.dropdownSelect(yearIdExpiratinDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, IdExpiryYear);
	}

	public void SetMonth(WebElement monthIdExpirationDropdown, String month) {
		switch (month.toUpperCase()) {
		case "JANUARY":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "1");
			break;
		case "FEBRUARY":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "2");
			break;
		case "MARCH":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "3");
			break;
		case "APRIL":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "4");
			break;
		case "MAY":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "5");
			break;
		case "JUNE":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "6");
			break;
		case "JULY":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "7");
			break;
		case "AUGUST":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "8");
			break;
		case "SEPTEMBER":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "9");
			break;
		case "OCTOBER":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "10");
			break;
		case "NOVEMBER":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "11");
			break;
		case "DECEMBER":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "12");
			break;
		}
	}

	public void SetMonthWithIndex(WebElement monthIdExpirationDropdown,
			String month) {
		switch (month.toUpperCase()) {
		case "JANUARY":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "01");
			break;
		case "FEBRUARY":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "02");
			break;
		case "MARCH":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "3");
			break;
		case "APRIL":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "04");
			break;
		case "MAY":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "05");
			break;
		case "JUNE":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "06");
			break;
		case "JULY":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "07");
			break;
		case "AUGUST":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "08");
			break;
		case "SEPTEMBER":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "09");
			break;
		case "OCTOBER":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "10");
			break;
		case "NOVEMBER":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "11");
			break;
		case "DECEMBER":
			Utilities.dropdownSelect(monthIdExpirationDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "12");
			break;
		}
	}

	public void populatingSprintSPV(String pinNumber, String questions,
			String answers) {
		Utilities.waitForElementVisible(PageBase
				.ServiceProviderVerificationPage().pinNumber);
		PageBase.ServiceProviderVerificationPage().pinNumber
				.sendKeys(pinNumber);
		Utilities.dropdownSelect(sprintSecurityQuestionsDD,
				Utilities.SelectDropMethod.SELECTBYINDEX, questions);
		PageBase.ServiceProviderVerificationPage().sprintSecurityAnswers
				.sendKeys(answers);
		PageBase.ServiceProviderVerificationPage().yesRecieveSprintEBill
				.click();
		PageBase.CommonControls().continueButton.click();
	}
	// endregion Methods
}