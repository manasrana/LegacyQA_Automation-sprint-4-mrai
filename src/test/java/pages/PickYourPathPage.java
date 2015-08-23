package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PickYourPathPage {

	// region Page Initialization
	public PickYourPathPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}
	// endregion Page Initialization

	@FindBy(id = ControlLocators.AAL_EXISTING_ACCOUNT)
	public WebElement AALExistingAccount;

	@FindBy(id = ControlLocators.UPGRADE_DEVICE_FOR_ONE_OR_MORE_LINES_ON_EXISTING_ACCOUNT_RADIO_BUTTON)
	public WebElement upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton;

	@FindBy(xpath = ControlLocators.ALL_PLANS)
	public WebElement allPlans;
}