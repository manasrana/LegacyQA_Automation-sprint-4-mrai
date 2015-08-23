package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.ControlLocators;
import framework.PageBase;
import framework.Utilities;

public class DeviceScanPage {
	public DeviceScanPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// region - Web Elements
	@FindBy(xpath = ControlLocators.IMEIESN_TEXTBOX)
	public WebElement iMEIESNTextbox;

	@FindBy(id = ControlLocators.SUBMIT_DEVICE_BUTTON)
	public WebElement submitDeviceButton;

	@FindBy(xpath = ControlLocators.ATT_GO_LINK)
	public WebElement attGoLink;

	@FindBy(xpath = ControlLocators.SPRINT_GO_LINK)
	public WebElement sprintGoLink;

	@FindBy(xpath = ControlLocators.TMOBILE_GO_LINK)
	public WebElement tMobileGoLink;

	@FindBy(xpath = ControlLocators.VOICE_ONLY_PHONE_GO)
	public WebElement voiceOnlyPhoneGo;

	@FindBy(xpath = ControlLocators.VERIZON_GO_LINK)
	public WebElement verizonGoLink;

	@FindBy(xpath = ControlLocators.IPHONE_GO_LINK)
	public WebElement iPhoneGoLink;

	@FindBy(xpath = ControlLocators.VERIZON_MIFI_GO_LINK)
	public WebElement verizonMIFIGoLink;

	@FindBy(xpath = ControlLocators.DEVICE_NOT_FOUND)
	public WebElement deviceNotFound;

	@FindBy(xpath = ControlLocators.CONTINUE_DS_BUTTON)
	public WebElement continueDSButton;

	// endregion

	public void enterDeviceScanDetails(String iEMEIESN) {
		Utilities
				.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().iMEIESNTextbox.clear();
		PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iEMEIESN);
		PageBase.DeviceScanPage().submitDeviceButton.click();
	}

	public void clickVerizonGo() {
		if (PageBase.DeviceScanPage().deviceNotFound.isDisplayed()) {
			PageBase.DeviceScanPage().verizonGoLink.click();
		}
	}


}