package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.ControlLocators;

public class VirginMobileUSAPage {

    /*region Page Initialization*/
    public VirginMobileUSAPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }
	 /* end region Page Initialization */

    @FindBy(xpath = ControlLocators.CONTINUE_FIRST_MSS_BUTTON)
    public WebElement continueButton;
}
