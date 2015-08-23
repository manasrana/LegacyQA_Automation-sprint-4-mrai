package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by tarunk on 6/4/2015.
 */
public class SprintEasyPayEligibilityResultPage {

	public SprintEasyPayEligibilityResultPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.ELIGIBLE_FOR_EASY_PAY_LABEL)
	public WebElement eligibleForEasyPayLabel;

	@FindBy(xpath = ControlLocators.DOWN_PAYMENT_LABEL)
	public WebElement downPaymentLabel;

	@FindBy(xpath = ControlLocators.INSTALLMENT_CONTRACT_LENGTH_LABEL)
	public WebElement installmentContractLengthLabel;

	@FindBy(id = ControlLocators.MINIMUM_DOWN_PAYMENT_EASY_PAY_RADIO_BUTTON)
	public WebElement minimumDownPaymentEasyPayRadioButton;

	@FindBy(id = ControlLocators.ALTERNATE_DOWN_PAYMENT_EASY_PAY_RADIO_BUTTON)
	public WebElement alternateDownPaymentEasyPayRadioButton;

	@FindBy(id = ControlLocators.SPRINT_EASY_PAY_INSTALLMENT_DETAILS)
	public WebElement sprintEasyPayInstallmentDetails;
}
