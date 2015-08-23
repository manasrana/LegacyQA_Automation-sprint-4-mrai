package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by Rashmimk on 7/8/2015.
 */
public class BrowsePlans {

    public BrowsePlans(WebDriver d) {
        PageFactory.initElements(d, this);
    }

	/* end region Page Initialization */

    @FindBy(xpath = ControlLocators.SPRINT_FAMILY_SHARE_PACK_8GB_TEXT)
    public WebElement sprintFamilySharePackText;

    @FindBy(xpath = ControlLocators.VERIZON_MORE_EVERYTHING_TEXT)
    public WebElement verizonMoreEverythingText;
}
