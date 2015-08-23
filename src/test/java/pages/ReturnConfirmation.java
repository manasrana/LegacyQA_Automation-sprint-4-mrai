package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.ControlLocators;

public class ReturnConfirmation {
	public ReturnConfirmation(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.RETURNS_CONFIRMATION)
	public WebElement returnConfirmation;

	@FindBy(xpath = ControlLocators.SUCCESSFULLY_RETURNED_STRING)
	public WebElement successfullyReturnedString;

	@FindBy(xpath = ControlLocators.LINE_AND_PHONE_NUMBER_STRING)
	public WebElement linePhonenoString;

	@FindBy(xpath = ControlLocators.RETURN_STEP1_TEXT)
	public WebElement returnStep1Text;

	@FindBy(xpath = ControlLocators.RETURN_STEP2_TEXT)
	public WebElement returnStep2Text;

	@FindBy(xpath = ControlLocators.PRINT_INSTRUCTIONS)
	public WebElement printInstruction;

	@FindBy(xpath = ControlLocators.RETURN_HOME)
	public WebElement returnHome;
}