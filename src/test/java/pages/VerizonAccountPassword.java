package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.ControlLocators;

public class VerizonAccountPassword {

	public VerizonAccountPassword(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(id = ControlLocators.PASSWORD_TEXTBOX)
	public WebElement password;

	@FindBy(name = ControlLocators.VZNPASSWORD_CONTINUE_BUTTON)
	public WebElement continueButton;
}
