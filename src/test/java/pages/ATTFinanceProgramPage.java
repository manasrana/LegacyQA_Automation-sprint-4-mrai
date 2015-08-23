package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by chanumai on 8/4/2015.
 */
public class ATTFinanceProgramPage {
    public ATTFinanceProgramPage(WebDriver d) {
        PageFactory.initElements(d, this);
    }

    @FindBy(id = ControlLocators.ATT_NEXT12)
    public WebElement aatNext12;

    @FindBy(id = ControlLocators.ATT_NEXT18)
    public WebElement attNext18;

    @FindBy(id = ControlLocators.ATT_NEXT24)
    public WebElement attNext24;

    @FindBy(id = ControlLocators.ATT_NEXT_WITH_DOWNPAYMENT)
    public WebElement aatNextWithDownPayment;

}
