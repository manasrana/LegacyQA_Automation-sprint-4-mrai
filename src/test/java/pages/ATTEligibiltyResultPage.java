
package pages;

        import framework.ControlLocators;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.support.FindBy;
        import org.openqa.selenium.support.PageFactory;

/**
 * Created by chanumai on 8/5/2015.
 */
public class ATTEligibiltyResultPage {
    public ATTEligibiltyResultPage(WebDriver d) {
        PageFactory.initElements(d, this);
    }

    @FindBy(xpath=ControlLocators.ELIGIBLE_FOR_NEXT)
    public WebElement elegibleForNext;

    @FindBy(xpath = ControlLocators.DOWN_PAYMENT_LABEL)
    public WebElement downPaymentLabel;

    @FindBy(xpath = ControlLocators.INSTALLMENT_CONTRACT_LENGTH_LABEL)
    public WebElement installmentContractLengthLabel;

    @FindBy(id = ControlLocators.ALTERNATE_DOWN_PAYMENT_EASY_PAY_RADIO_BUTTON)
    public WebElement alternateDownPaymentEasyPayRadioButton;
}
