package pages;

import framework.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.BrowserSettings;
import framework.ControlLocators;

import java.util.List;

public class OrderHistory {

	public OrderHistory(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.COMPLETED_LINK)
	public WebElement completedLink;

	@FindBy(xpath = ControlLocators.FIRST_COMPLETED_LINK)
	public WebElement firstCompletedLink;

	@FindBy(xpath = ControlLocators.DEACTIVATE_BUTTON)
	public WebElement deactivateButton;

	public void clickCompleted(String orderId) {
		String link = String.format(ControlLocators.COMPLETED_LINK1, orderId);
		BrowserSettings.driver.findElement(By.xpath(link)).click();
	}
}