package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.ControlLocators;

public class NumberPortPage {

	// region Page Initialization
	public NumberPortPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// end region Page Initialization

	@FindBy(id = ControlLocators.NUMBER_PORT_RADIOBUTTON)
	public WebElement numberPortRadiobutton;

	@FindBy(id = ControlLocators.NO_NUMBER_PORT_RADIOBUTTON)
	public WebElement noNumberPortRadiobutton;

	@FindBy(id = ControlLocators.CONTINUE_SPV_BUTTON)
	public WebElement continueSPVButton;
}