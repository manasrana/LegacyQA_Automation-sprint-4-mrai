package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import framework.ControlLocators;
import framework.PageBase;

public class PrintMobileScanSheetPage {

	// region Page Initialization
	public PrintMobileScanSheetPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// endregion Page Initialization

	@FindBy(xpath = ControlLocators.PRINT_MOBILE_SCAN_SHEET)
	public WebElement printMobileScanSheet;

	@FindBy(xpath = ControlLocators.CONTINUE_FIRST_MSS_BUTTON)
	public WebElement continueFirstMSSButton;

	@FindBy(className = ControlLocators.MOBILE_DETAILS)
	public WebElement mobilerDetails;

	@FindBy(xpath = ControlLocators.PHONE_MODEL_TEXT)
	public WebElement phoneModelText;

	@FindBy(xpath = ControlLocators.ORDER_NUMBER_VALUE_PMSS_TEXT)
	public WebElement orderNumberValuePMSSText;

	@FindBy(xpath = ControlLocators.STORE_LOCATION_VALUE_PMSS_TEXT)
	public WebElement storeLocationValuePMSSText;

	@FindBy(xpath = ControlLocators.PHONE_PRICE_VALUE_PMSS_TEXT)
	public WebElement phonePriceValuePMSSText;

	@FindBy(xpath = ControlLocators.PHONE_2_PRICE_VALUE_PMSS_TEXT)
	public WebElement phone2PriceValuePMSSText;

	@FindBy(xpath = ControlLocators.BARCODE_PMSS_IMAGE)
	public WebElement barcodePMSSImage;

	@FindBy(xpath = ControlLocators.BARCODE_PMSS_IMAGE2)
	public WebElement barcodePMSSImage2;

	@FindBy(xpath = ControlLocators.BARCODE_PMSS_IMAGE3)
	public WebElement barcodePMSSImage3;

	@FindBy(id = ControlLocators.ORDER_DETAILS)
	public WebElement orderDetails;

	@FindBy(xpath = ControlLocators.FIRST_DEVICE_BARCODE_IMAGE)
	public WebElement firstDeviceBarCode;

	@FindBy(xpath = ControlLocators.SECOND_DEVICE_BARCODE_IMAGE)
	public WebElement secondDeviceBarCode;

	@FindBy(xpath = ControlLocators.THIRD_DEVICE_BARCODE_IMAGE)
	public WebElement thirdDeviceBarCode;

	@FindBy(xpath = ControlLocators.PHONE_TAX_VALUE_PMSS_TEXT)
	public WebElement phoneTaxValuePMSSText;

	public void verifyOrderDetails() {
		List<WebElement> orderTableDetails = orderDetails.findElements(By
				.className("span"));
	}

	public void verifyMobileDetails() {
		List<WebElement> mobileTableDetails = mobilerDetails.findElements(By
				.tagName("td"));
		WebElement location = mobileTableDetails.get(2);
	}

	public void verifyAllTwoDeviceBarCode() {
		Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode
				.isDisplayed());
		Assert.assertTrue(PageBase.PrintMobileScanSheetPage().secondDeviceBarCode
				.isDisplayed());
	}
}