package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by Rashmimk on 7/8/2015.
 */
public class BrowsePhones {

    public BrowsePhones(WebDriver d) {
        PageFactory.initElements(d, this);
    }

	/* end region Page Initialization */

    @FindBy(xpath = ControlLocators.VERIZON_HOTSPOT)
    public WebElement verizonHotspotText;

    @FindBy(xpath = ControlLocators.IMAGE_VERIZON_HOTSPOT)
    public WebElement verizonHotspotImage;

    @FindBy(xpath = ControlLocators.SAMSUNG_GALAXY_S5_16GB)
    public WebElement samsungGalaxyS516GBText;

    @FindBy(xpath = ControlLocators.IMAGE_SAMSUNG_GALAXY_S5_16GB)
    public WebElement samsungGalaxyS516GBImage;
}
