package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.BrowserSettings;
import framework.ControlLocators;
import framework.PageBase;

public class ReturnOrExchangePagePreConditionsPage {
	public ReturnOrExchangePagePreConditionsPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.DEVICEPOWERON_RADIOBUTTON)
	public WebElement devicePowerOnRadioButton;

	@FindBy(xpath = ControlLocators.DEVICECONDITION_RADIOBUTTON)
	public WebElement deviceConditionRadioButton;

	@FindBy(xpath = ControlLocators.DEVICEACCESSORY_RADIOBUTTON)
	public WebElement deviceAccessoryRadioButton;

	@FindBy(xpath = ControlLocators.DEVICEPACKAGING_RADIOBUTTON)
	public WebElement devicePackagingRadioButton;

	@FindBy(name = ControlLocators.CONTINUEREV_BUTTON)
	public WebElement continueREVButton;

	public void SelectPreconditions() {
		devicePowerOnRadioButton.click();
		deviceConditionRadioButton.click();
		deviceAccessoryRadioButton.click();
		devicePackagingRadioButton.click();
	}
}