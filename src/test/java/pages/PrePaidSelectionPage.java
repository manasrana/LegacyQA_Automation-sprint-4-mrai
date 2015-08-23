package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PrePaidSelectionPage {


    /* region Page Initialization */
    public PrePaidSelectionPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }
	/* end region Page Initialization */

    @FindBy(xpath = ControlLocators.VIRGIN_MOBILE_USA_LINK)
    public WebElement virginMobileUSALink;

}
