package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import framework.ControlLocators;
import framework.PageBase;

public class CartPage {
	/* region Page Initialization */
	public CartPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	/* end region Page Initialization */

	/* region Cart Page Elements */
	@FindBy(id = ControlLocators.CLEAR_CART_BUTTON)
	public WebElement clearCartButton;

	@FindBy(id = ControlLocators.CONTINUE_CART_BUTTON)
	public WebElement continueCartButton;

	@FindBy(id = ControlLocators.FIRST_ASSIGN_NUMBER_DROPDOWN)
	public WebElement firstAssignNumberDropdown;

	@FindBy(id = ControlLocators.SECOND_ASSIGN_NUMBER_DROPDOWN)
	public WebElement secondAssignNumberDropdown;

	@FindBy(xpath = ControlLocators.FIRST_PHONE_PRICE_TEXT)
	public WebElement firstPhonePriceText;

	@FindBy(xpath = ControlLocators.SECOND_PHONE_PRICE_TEXT)
	public WebElement secondPhonePriceText;

	@FindBy(xpath = ControlLocators.FIRST_PHONE_MODEL_LINK)
	public WebElement firstPhoneModelLink;

	@FindBy(xpath = ControlLocators.SECOND_PHONE_MODEL_LINK)
	public WebElement secondPhoneModelLink;

	@FindBy(xpath = ControlLocators.PHONE_PRICE_AAL_TEXT)
	public WebElement phonePriceAALText;

	@FindBy(xpath = ControlLocators.PHONE_MODEL_AAL_LINK)
	public WebElement phoneModelAALLink;

	@FindBy(xpath = ControlLocators.DEVICE1_PRICE)
	public WebElement device1Price;

	@FindBy(xpath = ControlLocators.DEVICE2_PRICE)
	public WebElement device2Price;

	@FindBy(xpath = ControlLocators.DEVICE3_PRICE)
	public WebElement device3Price;

	@FindBy(xpath = ControlLocators.PLAN_PRICE)
	public WebElement planPriceActual;

	@FindBy(xpath = ControlLocators.PHONE_MODEL_DESCRIPTION)
	public WebElement phoneModelDescription;

	@FindBy(xpath = ControlLocators.PLAN_DESCRIPTION)
	public WebElement planDescription;

	@FindBy(xpath = ControlLocators.DOWN_PAYMENT_AMOUNT_LABEL)
	public WebElement downPaymentAmountLabel;

	@FindBy(xpath = ControlLocators.MONTHLY_DEVICE_INSTALLMENT_BALANCE_LABEL)
	public WebElement monthlyDeviceInstallmentBalanceLabel;

	@FindBy(xpath = ControlLocators.MONTHLY_RECURRING_FEE_CART_LABEL)
	public WebElement monthlyRecurringFeeLabel;

	@FindBy(xpath = ControlLocators.TOTAL_DUE_TODAY_label)
	public WebElement totalDueTodayLabel;

	@FindBy(xpath = ControlLocators.LAST_MONTHLY_INSTALLMENT_MAY_BE_DIFFERENT_TEXT)
	public WebElement lastMonthlyInstallmentMayBeDifferentText;

	@FindBy(xpath = ControlLocators.TWO_YEARS_ACTIVATION_TEXT)
	public WebElement twoYearsActivationText;

	@FindBy(xpath = ControlLocators.PLAN_PRICE_WITH_2_PHONES)
	public WebElement planPriceWith2Phones;

	@FindBy(xpath = ControlLocators.DOWN_PAYMENT_AMT)
	public WebElement downPaymentAmt;

	@FindBy(xpath = ControlLocators.MONTHLY_DEVICE_INSTALLMENT_BALANCE)
	public WebElement monthlyDeviceInstallmentBalance;

	@FindBy(xpath = ControlLocators.MONTHLY_RECURRING_FEE_CART)
	public WebElement monthlyRecurringFee;

	@FindBy(xpath = ControlLocators.TOTAL_DUE_TODAY)
	public WebElement totalDueToday;

	/* end region Cart Page Elements */

	// region - Methods
	public void selectPhoneNumber() {
		Select assignNumber = new Select(
				PageBase.CartPage().firstAssignNumberDropdown);
		assignNumber.selectByIndex(1);
	}
	// endregion
}