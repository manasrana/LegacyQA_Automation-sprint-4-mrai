package pages;

import framework.BrowserSettings;
import framework.ControlLocators;
import framework.PageBase;
import framework.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by divyan on 4/24/2015.
 */
public class MailVerificationPage {
	public MailVerificationPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(id = ControlLocators.SIGN_IN_LINK)
	public WebElement signIN;

	@FindBy(id = ControlLocators.G_EMAIL)
	public WebElement email;

	@FindBy(id = ControlLocators.GMAIL_PASSWORD)
	public WebElement password;

	@FindBy(id = ControlLocators.SIGIN_IN_BUTTON)
	public WebElement signInButton;

	@FindBy(xpath = ControlLocators.INBOX)
	public WebElement inBox;

	@FindBy(xpath = ControlLocators.DELETE)
	public WebElement delete;

	@FindBy(xpath = ControlLocators.SELECT_ALL)
	public WebElement selectALL;

	@FindBy(xpath = ControlLocators.DELETE)
	public WebElement deleteButton;

	@FindBy(id = ControlLocators.NEXT_BUTTON)
	public WebElement nextButton;

	@FindBy(id = ControlLocators.SEARCH_TEXTBOX)
	public WebElement searchTextbox;

	@FindBy(id = ControlLocators.SEARCH_BUTTON)
	public WebElement searchButton;

	@FindBy(xpath = ControlLocators.WCA_EMAIL)
	public WebElement wcaEmail;

	@FindBy(xpath = ControlLocators.VERIZON_WCA)
	public WebElement verizonWCA;

	// Methods
	public void deleteAllInBoxMail() {
		selectALL.click();
		deleteButton.click();
	}

	public boolean isNewMail() {
		String mail = inBox.getText();
		String mailCount = mail.substring(5, 1);
		System.out.println(mailCount);
		int count = Integer.parseInt(mailCount);

		return true;
	}

	public void launchGmailInNewTab() throws InterruptedException,
			AWTException, java.io.IOException {
		try {
			BrowserSettings.driver.findElement(By.cssSelector("body"))
					.sendKeys(Keys.CONTROL + "t");
			BrowserSettings.driver.manage().timeouts()
					.pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
			BrowserSettings.driver.navigate().to(
					BrowserSettings.readConfig("urlGmail"));
			email.sendKeys(BrowserSettings.readConfig("gmailUserID"));
			nextButton.click();
			Utilities.implicitWaitSleep(2000);
			password.sendKeys(BrowserSettings.readConfig("gmailPwd"));
			signInButton.click();
			BrowserSettings.log
					.append("Successfully launched the Gmail page in new tab <br>\n");
		} catch (Exception e) {
		}
		BrowserSettings.driver.manage().timeouts()
				.pageLoadTimeout(300000, TimeUnit.MILLISECONDS);// Setting back
																// to default
																// Selenium
																// timeout
	}

	public void wcaEmailVerification() {
		searchTextbox.sendKeys("Terms and Conditions");
		searchButton.click();
		Utilities.implicitWaitSleep(3000);
		Assert.assertTrue(wcaEmail.isEnabled());
		/*
		 * Utilities.implicitWaitSleep(3000);
		 * Assert.assertTrue(verizonWCA.isDisplayed());
		 */
	}
}