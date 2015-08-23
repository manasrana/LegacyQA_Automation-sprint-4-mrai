package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by mritunjaikr on 5/13/2015.
 */
public class AccountPasswordPage {
	public AccountPasswordPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// region Web Elements
	@FindBy(id = ControlLocators.VZN_ACCOUNT_PWD)
	public WebElement accountPassword;

	@FindBy(xpath = ControlLocators.VZN_PASSWORD_BUTTON)
	public WebElement continueButton;
	// endregion Web Elements
}