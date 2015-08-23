package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by Rashmimk on 7/8/2015.
 */
public class EditSessionVarsPage {

    public EditSessionVarsPage(WebDriver d) {
        PageFactory.initElements(d, this);
    }

	/* end region Page Initialization */

    @FindBy(xpath = ControlLocators.BRAND_ID_731)
    public WebElement brandId;
}
