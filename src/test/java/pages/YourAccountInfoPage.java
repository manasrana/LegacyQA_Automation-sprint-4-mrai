package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import framework.ControlLocators;
import framework.PageBase;
import framework.Utilities;
import framework.Utilities.SelectDropMethod;



public class YourAccountInfoPage {

    /* region Page Initialization */
    public YourAccountInfoPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }
	/* end region Page Initialization */

    @FindBy(xpath = ControlLocators.PRINT_ALL_BUTTON)
    public WebElement printAllButton;

}
