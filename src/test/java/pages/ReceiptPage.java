package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.BrowserSettings;
import framework.ControlLocators;

public class ReceiptPage {
	public ReceiptPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.ORDER_COMPLETION_TEXT)
	public WebElement orderCompletionText;

	public void verifyOrderCompletionPage() {
		if (orderCompletionText.isDisplayed() == true) {
			System.out
					.println("Receipt Page verification Completed Successfully");
		}
	}
}