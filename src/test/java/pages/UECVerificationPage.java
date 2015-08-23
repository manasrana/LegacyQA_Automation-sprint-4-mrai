package pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.AccountDetails;
import framework.CSVOperations;
import framework.ControlLocators;
import framework.CustomerDetails;
import framework.PageBase;

public class UECVerificationPage {

	// region Page Initialization
	public UECVerificationPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// endregion Page Initialization

	// region Home Page Retail Elements
	@FindBy(xpath = ControlLocators.VERIZON_TAB)
	public WebElement verizonTab;

	@FindBy(xpath = ControlLocators.PHONE_NUMBER_VERIZON_TEXTBOX)
	public WebElement phoneNumberVerizonTextbox;

	@FindBy(xpath = ControlLocators.LAST_4_OF_SSN_VERIZON_TEXTBOX)
	public WebElement last4OfSSNVerizonTextbox;

	@FindBy(xpath = ControlLocators.ACCOUNT_PASSWORD_VERIZON_TEXTBOX)
	public WebElement accountPasswordVerizonTextbox;

	@FindBy(xpath = ControlLocators.ACCOUNT_ZIPCODE_VERIZON_TEXTBOX)
	public WebElement accountZipcodeVerizonTextbox;

	@FindBy(xpath = ControlLocators.CONTINUE_VERIZON_BUTTON)
	public WebElement continueVerizonButton;

	@FindBy(xpath = ControlLocators.SPRINT_TAB)
	public WebElement sprintTab;

	@FindBy(xpath = ControlLocators.PHONE_NUMBER_SPRINT_TEXTBOX)
	public WebElement phoneNumberSprintTextbox;

	@FindBy(xpath = ControlLocators.LAST_4_OF_SSN_SPRINT_TEXTBOX)
	public WebElement last4OfSSNSprintTextbox;

	@FindBy(xpath = ControlLocators.PIN_SPRINT_TEXTBOX)
	public WebElement pinSprintTextbox;

	@FindBy(xpath = ControlLocators.CONTINUE_SPRINT_BUTTON)
	public WebElement continueSprintButton;

	@FindBy(xpath = ControlLocators.SUBMIT_AND_CONTINUE)
	public WebElement submitAndContinueButton;

	@FindBy(xpath = ControlLocators.ATT_TAB)
	public WebElement attTab;

	@FindBy(xpath = ControlLocators.PHONE_NUMBER_ATT_TEXTBOX)
	public WebElement phoneNumberATTTextbox;

	@FindBy(xpath = ControlLocators.LAST_4_OF_SSN_ATT_TEXTBOX)
	public WebElement last4OfSSNATTTextbox;

	@FindBy(xpath = ControlLocators.ACCOUNT_PASSWORD_ATT_TEXTBOX)
	public WebElement accountPasswordATTTextbox;

	@FindBy(xpath = ControlLocators.ACCOUNT_ZIPCODE_ATT_TEXTBOX)
	public WebElement accountZipcodeATTTextbox;

	@FindBy(xpath = ControlLocators.CONTINUE_ATT_BUTTON)
	public WebElement continueATTButton;

	// endregion Login Page Elements

	public void fillVerizonDetails(String phoneNumber, String SSN, String Pwd,
			String zipCode) {
		verizonTab.click();
		phoneNumberVerizonTextbox.sendKeys(phoneNumber);
		last4OfSSNVerizonTextbox.sendKeys(SSN);
		accountPasswordVerizonTextbox.sendKeys(Pwd);
		accountZipcodeVerizonTextbox.sendKeys(zipCode);
	}

	public void fillSprintDetails(String phoneNumber, String SSN, String pin) {
		sprintTab.click();
		phoneNumberSprintTextbox.sendKeys(phoneNumber);
		last4OfSSNSprintTextbox.sendKeys(SSN);
		pinSprintTextbox.sendKeys(pin);
	}
}