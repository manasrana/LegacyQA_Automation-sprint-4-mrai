package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.ControlLocators;
import framework.Utilities;

public class WirelessCustomerAgreementPage {

	// region Page Initialization
	public WirelessCustomerAgreementPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// endregion Page Initialization

	@FindBy(id = ControlLocators.ACCEPTS_WCA_CHECKBOX)
	public WebElement acceptsWCACheckbox;

	@FindBy(name = ControlLocators.EMAIL_CHECKBOX)
	public WebElement emailCheckbox;

	@FindBy(id = ControlLocators.CARRIER_TERMS_CHECKBOX)
	public WebElement acceptsWCACheckboxForSprint;

	@FindBy(id = ControlLocators.SAVE_SIGNATURE_WCA_BUTTON)
	public WebElement saveSignatuteButton;

	@FindBy(id = ControlLocators.CONTINUE_WCA_BUTTON)
	public WebElement continueWCAButton;

	@FindBy(id = ControlLocators.SIGNATURE_WCA_TEXTBOX)
	public WebElement signatureTextbox;

	@FindBy(xpath = ControlLocators.TERMS_AND_CONDITIONS_DIV)
	public WebElement termsAndConditionsDiv;

	@FindBy(xpath = ControlLocators.TERM_1_LABEL)
	public WebElement term1Label;

	@FindBy(xpath = ControlLocators.TERM_2_LABEL)
	public WebElement term2Label;

	@FindBy(xpath = ControlLocators.ETF_1_LABEL)
	public WebElement eTF1Label;

	@FindBy(xpath = ControlLocators.ETF_1_LABEL)
	public WebElement eTF2Label;

	@FindBy(xpath = ControlLocators.ACTIVATION_DATE_1_LABEL)
	public WebElement activationDate1Label;

	@FindBy(xpath = ControlLocators.ACTIVATION_DATE_2_LABEL)
	public WebElement activationDate2Label;

	@FindBy(xpath = ControlLocators.ACTIVATION_FEE_1_LABEL)
	public WebElement activationFee1Label;

	@FindBy(xpath = ControlLocators.ACTIVATION_FEE_2_LABEL)
	public WebElement activationFee2Label;

	@FindBy(xpath = ControlLocators.UPGRADE_FEE_1_LABEL)
	public WebElement upgradeFee1Label;

	@FindBy(xpath = ControlLocators.UPGRADE_FEE_2_LABEL)
	public WebElement upgradeFee2Label;

	@FindBy(xpath = ControlLocators.FEATURES_1_LABEL)
	public WebElement features1Label;

	@FindBy(xpath = ControlLocators.FEATURES_2_LABEL)
	public WebElement features2Label;

	@FindBy(xpath = ControlLocators.WCA_CANCEL_ORDER_BUTTON)
	public WebElement wcaCancelOrder;

	public void WCASignature(WebDriver driver) {
		Actions builder = new Actions(driver);
		Action drawAction = builder.moveToElement(signatureTextbox, 100, 50)
				// signatureWebElement is the element that holds the signature
				// element you have in the DOM
				.clickAndHold().moveByOffset(6, 7).moveByOffset(-15, 15)
				.release().build();
		drawAction.perform();
	}

	// Region Methods
	public void signingWCA(WebDriver driver) {
		Actions builder = new Actions(driver);
		Action drawAction = builder.moveToElement(signatureTextbox, 100, 50)
				// signatureWebElement is the element that holds the signature
				// element you have in the DOM
				.clickAndHold().moveByOffset(6, 7).moveByOffset(-15, 15)
				.release().build();
		drawAction.perform();
		saveSignatuteButton.click();
	}

	public void cancelOrderAlert(WebDriver driver) {
		Utilities.implicitWaitSleep(5000);
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}
	// endregion Methods
}