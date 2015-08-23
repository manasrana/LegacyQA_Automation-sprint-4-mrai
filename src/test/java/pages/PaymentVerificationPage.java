package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.ControlLocators;
import framework.Utilities;

public class PaymentVerificationPage {

	// region Page Initialization
	public PaymentVerificationPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// end region Page Initialization

	@FindBy(id = ControlLocators.TARGET_RECEIPT_ID_TEXTBOX)
	public WebElement textboxTargetReceiptID;

	@FindBy(xpath = ControlLocators.SUBMIT_BUTTON)
	public WebElement submitButton;

	public void paymentVerification(String receiptID) {
		Utilities.waitForElementVisible(textboxTargetReceiptID);
		textboxTargetReceiptID.sendKeys(receiptID);
		submitButton.click();
	}
}