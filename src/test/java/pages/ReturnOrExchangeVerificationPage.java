package pages;

import framework.ControlLocators;
import framework.Utilities;
import pages.ServiceProviderVerificationPage.IdType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ReturnOrExchangeVerificationPage {
	public ReturnOrExchangeVerificationPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(id = ControlLocators.FIRST_NAME_TEXTBOX)
	public WebElement firstNameTextbox;

	@FindBy(id = ControlLocators.MIDDLE_INITIAL_TEXTBOX)
	public WebElement middleInitialTextbox;

	@FindBy(id = ControlLocators.LAST_NAME_TEXTBOX)
	public WebElement lastNameTextbox;

	@FindBy(id = ControlLocators.ID_TYPE_DROPDOWN)
	public WebElement idTypeDropdown;

	@FindBy(id = ControlLocators.STATE_DROPDOWN)
	public WebElement stateDropdown;

	@FindBy(id = ControlLocators.ID_NUMBER_TEXTBOX)
	public WebElement idNumberTextbox;

	@FindBy(id = ControlLocators.CV_FIRST_NAME_TEXTBOX)
	public WebElement cvFirstNameTextbox;

	@FindBy(id = ControlLocators.CV_MIDDLE_NAME_TEXTBOX)
	public WebElement cvMiddleInitialTextbox;

	@FindBy(id = ControlLocators.CV_LAST_NAME_TEXTBOX)
	public WebElement cvLastNameTextbox;

	@FindBy(xpath = ControlLocators.CONTINUE_EXCHANGE)
	public WebElement continueEXCHANGE;

	@FindBy(xpath = ControlLocators.CONTINUE_PRECONDITION)
	public WebElement continuePRECONDITION;

	@FindBy(xpath = ControlLocators.PROCEED_EXCHANGE)
	public WebElement proceedEXCHANGE;

	@FindBy(xpath = ControlLocators.EXCHANGE_DEVICE)
	public WebElement exchangeDEVICE;

	@FindBy(xpath = ControlLocators.RETURN_DEVICE)
	public WebElement returnDEVICE;

	@FindBy(id = ControlLocators.FINANCING_DROPDOWN)
	public WebElement financingOptIn;

	@FindBy(id = ControlLocators.EXCHANGEREASONS_DROPDOWN)
	public WebElement exchangeReasons;

	@FindBy(id = ControlLocators.RETURNREASONS_DROPDOWN)
	public WebElement returnReasons;

	@FindBy(xpath = ControlLocators.RETURNDEVICEESN_TEXTBOX)
	public WebElement returnDeviceTextBox;

	@FindBy(id = ControlLocators.SUBMITFORM_BUTTON)
	public WebElement submitFormButton;

	@FindBy(id = ControlLocators.RETURN_ANOTHER)
	public WebElement returnAnotherDevice;

	@FindBy(xpath = ControlLocators.FIRSTDEVICE_NOINSURANCE)
	public WebElement firstDeviceNoInsurance;

	@FindBy(xpath = ControlLocators.SECONDDEVICE_NOINSURANCE)
	public WebElement secondDeviceNoInsurance;

	@FindBy(xpath = ControlLocators.CONTINUE_EXCHANGE_MSS)
	public WebElement continueExchangeMSSPage;

	@FindBy(xpath = ControlLocators.DEVICE_POWERON)
	public WebElement devicePowerOn;

	@FindBy(xpath = ControlLocators.DEVICECONDITION_YES)
	public WebElement deviceConditionYes;

	@FindBy(xpath = ControlLocators.DEVICEACCESSORY_NO)
	public WebElement deviceAccessoryNo;

	@FindBy(xpath = ControlLocators.DEVICEPACKAGING_RADIOBUTTON)
	public WebElement devicePackingNo;

	@FindBy(xpath = ControlLocators.PARENT_ORDER_NUMBER)
	public WebElement parentOrderNumber;

	// commonControls - continueButton

	public void populatingPage(IdType idType, String state, String idNumber,
			String firstName, String middleInitial, String lastName) {
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

		cvFirstNameTextbox.clear();
		cvFirstNameTextbox.sendKeys(firstName);
		cvMiddleInitialTextbox.clear();
		cvMiddleInitialTextbox.sendKeys(middleInitial);
		cvLastNameTextbox.clear();
		cvLastNameTextbox.sendKeys(lastName);
	}
}