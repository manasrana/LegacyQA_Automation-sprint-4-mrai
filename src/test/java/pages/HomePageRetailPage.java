package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.ControlLocators;
import framework.RetailBaseClass;

public class HomePageRetailPage extends RetailBaseClass {

	// region Page Initialization
	public HomePageRetailPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// endregion Page Initialization

	// region Home Page Retail Elements
	@FindBy(xpath = ControlLocators.NEW_GUEST_BUTTON)
	public WebElement newGuestButton;

	@FindBy(id = ControlLocators.CART_ICON)
	public WebElement cartIcon;

	@FindBy(id = ControlLocators.SETTINGS_ICON)
	public WebElement settingsIcon;

	@FindBy(xpath = ControlLocators.SALES_AND_ACTIVATIONS_LINK)
	public WebElement salesAndActivationsLink;

	@FindBy(xpath = ControlLocators.UPGRADE_ELIGIBILITY_CHECKER_LINK)
	public WebElement upgradeEligibilityCheckerLink;

	@FindBy(id = ControlLocators.CARRIER_CREDIT_CHECK_LINK)
	public WebElement carrierCreditCheckLink;

	@FindBy(id = ControlLocators.ELECTRONIC_TRADEIN_LINK)
	public WebElement electronicTradeInLink;

	@FindBy(id = ControlLocators.APPLE_CARE_LINK)
	public WebElement appleCareLink;

	@FindBy(xpath = ControlLocators.ACCOUNT_LOOK_UP_LINK)
	public WebElement accountLookUpLink;

	@FindBy(xpath = ControlLocators.PREPAID_ACTIVATION_LINK)
	public WebElement prePaidActivationLink;

	@FindBy(id = ControlLocators.HOME_TAB)
	public WebElement homeTab;

	@FindBy(id = ControlLocators.INVENTORY_MANAGEMENT_TAB)
	public WebElement inventoryManagementTab;

	@FindBy(id = ControlLocators.GUEST_LOOKUP_TAB)
	public WebElement guestLookupTab;

	@FindBy(xpath = ControlLocators.SAVE_TAB)
	public WebElement saveTab;

	@FindBy(xpath = ControlLocators.LOCK_TAB)
	public WebElement lockTab;
	// endregion
}