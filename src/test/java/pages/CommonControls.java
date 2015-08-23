package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.ControlLocators;

public class CommonControls {

	/* region Page Initialization */
	public CommonControls(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	/* end region Page Initialization */

	@FindBy(id = ControlLocators.CONTINUE_BUTTON)
	public WebElement continueButton;

	@FindBy(id = ControlLocators.CANCEL_BUTTON)
	public WebElement cancelButton;

	@FindBy(id = ControlLocators.CONTINUE_COMMON_BUTTON)
	public WebElement continueCommonButton;

	@FindBy(xpath = ControlLocators.SUBMIT_BUTTON)
	public WebElement submitButton;

	@FindBy(xpath = ControlLocators.CONTINUE_BUTTON_DVA)
	public WebElement continueButtonDVA;

	@FindBy(xpath = ControlLocators.CONTINUE_BUTTON_DEPOSIT)
	public WebElement continueButtonDeposit;

	@FindBy(xpath = ControlLocators.SUPPORT_CENTER_MESSAGE_POA)
	public WebElement supportCenterPageMessage;

	@FindBy(xpath = ControlLocators.ORDERID_SUPPORT_CENTER_PAGE)
	public WebElement orderIdSupportCenterPage;

	@FindBy(xpath = ControlLocators.CONTINUE_SUPPORT_CENTER_PAGE)
	public WebElement continueSupportCenter;

	@FindBy(id = ControlLocators.CONTINUE_ACTIVATION)
	public WebElement continueActivation;

	@FindBy(id = ControlLocators.CONTINUE_AFTER_DEVICE_SHIPPED)
	public WebElement continueAfterDeviceShipped;

	@FindBy(xpath = ControlLocators.SUPPORT_CENTER_MESSAGE_POA)
	public WebElement supportCenterText;
}