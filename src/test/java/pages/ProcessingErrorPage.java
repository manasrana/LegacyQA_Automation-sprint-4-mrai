package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProcessingErrorPage {

	// region Page Initialization
	public ProcessingErrorPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// endregion Page Initialization

	@FindBy(xpath = ControlLocators.PROCESSING_ERROR_TEXT)
	public WebElement processingErrorText;

	@FindBy(xpath = ControlLocators.PROCESSING_ERROR_MESSAGE_TEXT)
	public WebElement processingErrorMessageText;

	@FindBy(xpath = ControlLocators.ATT_HELP_NUMBET_TEXT)
	public WebElement aTTHelpNumberText;

	@FindBy(xpath = ControlLocators.VERIZON_HELP_NUMBET_TEXT)
	public WebElement verizonHelpNumberText;

	@FindBy(xpath = ControlLocators.SPRINT_HELP_NUMBET_TEXT)
	public WebElement sprintHelpNumberText;
}