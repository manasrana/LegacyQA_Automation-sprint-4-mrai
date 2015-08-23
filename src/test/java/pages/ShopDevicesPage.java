package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ShopDevicesPage {

      /*region Page Initialization*/
    public ShopDevicesPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }
	 /* end region Page Initialization */

    @FindBy(xpath = ControlLocators.FIRST_PHONE_IMAGE)
    public WebElement firstPhoneImage;
}
