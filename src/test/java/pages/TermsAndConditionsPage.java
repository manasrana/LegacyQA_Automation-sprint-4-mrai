package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.ControlLocators;
import framework.Utilities;

public class TermsAndConditionsPage {

	/* region Page Initialization */
	public TermsAndConditionsPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	/* end region Page Initialization */

	/* region Select Insurance Page Elements */
	@FindBy(name = ControlLocators.TERMS_CONDITIONS_CHECKBOX)
	public WebElement checkboxTermsConditions;

	@FindBy(id = ControlLocators.CONTINUE_TERMS_AND_CONDITION_BUTTON)
	public WebElement continueTCButton;

	@FindBy(xpath = ControlLocators.TARGET_TERMS)
	public WebElement targteTerms;

	@FindBy(xpath = ControlLocators.TC_CHK_BOX)
	public WebElement tcChkBox;

	@FindBy(id = ControlLocators.EMAIL_TC_CHK_BOX)
	public WebElement emailTCChkBox;

	@FindBy(id = ControlLocators.ACCEPTS_TARGET_TC_CHECKBOX)
	public WebElement acceptsTargetTCCheckbox;

	@FindBy(id = ControlLocators.SAVE_SIGNATURE_BUTTON)
	public WebElement saveSignatureButton;

	@FindBy(id = ControlLocators.CARRIER_TERMS_CHECKBOX)
	public WebElement carrierTermsCheckBox;

	@FindBy(id = ControlLocators.SAVE_SIGNATURE_WCA_BUTTON)
	public WebElement saveSignatuteButton;

	@FindBy(id = ControlLocators.SIGNATURE_WCA_TEXTBOX)
	public WebElement signatureTextbox;

	@FindBy(id = ControlLocators.CARRIER_TERMS_CHECKBOX)
	public WebElement carrierTermsAcceptCheckbox;

	@FindBy(id = ControlLocators.SPRINT_TERMS_CONDITION_CHECKBOX)
	public WebElement sprintTermsConsditionCheckbox;

	// EndRegion

	public void acceptTermsAndConditions() {
		Utilities.waitForElementVisible(checkboxTermsConditions);
		checkboxTermsConditions.click();
		acceptsTargetTCCheckbox.click();
		continueTCButton.click();
	}

	public void sprintAcceptTermsAndConditions() {
		Utilities.waitForElementVisible(carrierTermsCheckBox);
		carrierTermsCheckBox.click();
		acceptsTargetTCCheckbox.click();
	}

	public void signingTermsAndConditions(WebDriver driver) {
		Actions builder = new Actions(driver);
		Action drawAction = builder
				.moveToElement(signatureTextbox, 100, 50)
				// signatureWebElement is the element that holds the signature
				// element you have in the DOM
				.clickAndHold().moveByOffset(6, 7).moveByOffset(-15, 15)
				.release().build();
		drawAction.perform();
		saveSignatuteButton.click();
	}

	public void acceptSprintTermsAndCondition() {
		Utilities.waitForElementVisible(checkboxTermsConditions);
		checkboxTermsConditions.click();
		acceptsTargetTCCheckbox.click();
		sprintTermsConsditionCheckbox.click();
		continueTCButton.click();
	}
}