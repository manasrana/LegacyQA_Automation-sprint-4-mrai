package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.ControlLocators;
import framework.PageBase;

public class VerizonEdgePage {

	// region Page Initialization
	public VerizonEdgePage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// /endregion Page Initialization

	// region Verizon Edge Page Elements
	@FindBy(xpath = ControlLocators.DECLINE_BUTTON)
	public WebElement checkBoxDeclineVerizonOrder;

	@FindBy(xpath = ControlLocators.DECLINE_BUTTON)
	public WebElement declineButton;

	@FindBy(id = ControlLocators.NO_CONTINUE_WITH_2_YEAR_BUTTON)
	public WebElement noContinueWith2YearButton;

	@FindBy(id = ControlLocators.YES_CHECK_ELIGIBILITY)
	public WebElement YesCheckEligibilityButton;

	@FindBy(xpath = ControlLocators.ESNVALUE_TEXT)
	public WebElement eSNValueText;

	@FindBy(xpath = ControlLocators.DECLINE_VERIZON_EDGE)
	public WebElement declineVerizonEdge;

	@FindBy(id = ControlLocators.DOWNPAYMENT_TEXTBOX)
	public WebElement customerDownPayment;

	@FindBy(id = ControlLocators.INSTALMENT_SUBMIT_BUTTON)
	public WebElement submit;

	@FindBy(xpath = ControlLocators.MIN_DOWNPAYMENT)
	public WebElement monthlyDownPayment;

	@FindBy(xpath = ControlLocators.DOWN_PAYMENT_AMOUNT)
	public WebElement downPaymentAmt;

	@FindBy(xpath = ControlLocators.DISCLAIMER_MESSAGE)
	public WebElement disclaimerMessage;

	@FindBy(xpath = ControlLocators.MONTHLY_INSTALLMENT_AMT)
	public WebElement monthlyInstallment;

	@FindBy(xpath = ControlLocators.DEVICE_PRICE_WITH_FINANCE)
	public WebElement devicePriceWithFinance;

	@FindBy(xpath = ControlLocators.DO_NOT_PROMPT_FOR_ENTIRE_ORDER)
	public WebElement donotpromptForEntireOrder;

	// end region Verizon Edge Page Elements

	public void declineVerizonEdge() {
		PageBase.VerizonEdgePage().declineVerizonEdge.click();
		PageBase.CommonControls().cancelButton.click();
	}

	public void declineSprintEasyPay() {
		PageBase.VerizonEdgePage().donotpromptForEntireOrder.click();
		PageBase.CommonControls().cancelButton.click();
	}
}