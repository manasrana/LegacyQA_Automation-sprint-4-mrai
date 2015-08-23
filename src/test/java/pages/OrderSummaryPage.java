package pages;

import framework.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class OrderSummaryPage extends ShipAdminBaseClass {

	public OrderSummaryPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.STATUS_VALUE_LINK)
	public WebElement statusValueLink;

	@FindBy(xpath = ControlLocators.ISSUED_RMA)
	public WebElement issuedRMA;

	@FindBy(xpath = ControlLocators.CHILD_ORDER)
	public WebElement childOrder;

	@FindBy(xpath = ControlLocators.ORDER_NUMBER_VALUE_SA_LINK)
	public WebElement orderNumberValueSALink;

	@FindBy(xpath = ControlLocators.ORDER_BUY_TYPE_VALUE_TEXT)
	public WebElement orderBuyTypeValueText;

	@FindBy(xpath = ControlLocators.ADDITIONAL_INFO_VALUE_TEXT)
	public WebElement additionalInfoValueText;

	@FindBy(xpath = ControlLocators.PARTNER_VALUE_TEXT)
	public WebElement partnerValueText;

	@FindBy(xpath = ControlLocators.FINANCING_PROGRAM_VALUE_TEXT)
	public WebElement financingProgramValueText;

	@FindBy(xpath = ControlLocators.TRADE_IN_REQUIRED_VALUE_TEXT)
	public WebElement tradeInRequiredValueText;

	@FindBy(xpath = ControlLocators.PAYMENT_REQUIRED_VALUE_TEXT)
	public WebElement paymentRequiredValueText;

	@FindBy(xpath = ControlLocators.AMOUNT_PAID_VALUE_TEXT)
	public WebElement amountPaidVauleText;

	@FindBy(xpath = ControlLocators.FIRST_SERVICE_ACTIVATION_RESPONSE_RECEIVED_COMMENT_VALUE)
	public WebElement firstServiceActivationResponseReceivedCommentValue;

	@FindBy(xpath = ControlLocators.SECOND_SERVICE_ACTIVATION_RESPONSE_RECEIVED_COMMENT_VALUE)
	public WebElement secondServiceActivationResponseReceivedCommentValue;

	@FindBy(xpath = ControlLocators.THIRD_SERVICE_ACTIVATION_RESPONSE_RECEIVED_COMMENT_VALUE)
	public WebElement thirdServiceActivationResponseReceivedCommentValue;

	@FindBy(xpath = ControlLocators.FOURTH_SERVICE_ACTIVATION_RESPONSE_RECEIVED_COMMENT_VALUE)
	public WebElement fourthServiceActivationResponseReceivedCommentValue;

	@FindBy(xpath = ControlLocators.FIFTH_SERVICE_ACTIVATION_RESPONSE_RECEIVED_COMMENT_VALUE)
	public WebElement fifthServiceActivationResponseReceivedCommentValue;

	@FindBy(xpath = ControlLocators.TRANSACTION_POST_COMMENT_VALUE)
	public WebElement transactionPostCommentValue;

	@FindBy(xpath = ControlLocators.SHIPPING_COMMENT_VALUE)
	public WebElement shippingCommentValue;

	@FindBy(xpath = ControlLocators.EVENT_LOG_TABLE)
	public WebElement eventLogTable;

	@FindBy(xpath = ControlLocators.BAN_VALUE_TEXT)
	public WebElement bANValueText;

	@FindBy(name = ControlLocators.VIEW_DETAILS_DROPDOWN)
	public WebElement viewDetailsDropdown;

	@FindBy(xpath = ControlLocators.ROC_HOME_LINK)
	public WebElement rOCHomeLink;

	@FindBy(xpath = ControlLocators.IN_STORE_BILLING_STATUS_LINK)
	public WebElement inStoreBillingStatusLink;

	@FindBy(xpath = ControlLocators.ACTIVATION_SCAN_REQUIRED_STATUS_LINK)
	public WebElement activationScanRequiredStatusLink;

	@FindBy(xpath = ControlLocators.WCA_SIGNATURE_REQUIRED_STATUS_LINK)
	public WebElement wCASignatureRequiredStatusLink;

	@FindBy(xpath = ControlLocators.SHIPPED_STATUS_LINK)
	public WebElement shippedStatusLink;

	@FindBy(xpath = ControlLocators.APPLECARE_INSURANCESTATUS)
	public WebElement appleCareInsuranceStatus;
	@FindBy(xpath = ControlLocators.DEACTIVATE_LINE_1)
	public WebElement deactivateLine1;

	@FindBy(name = ControlLocators.PRINTABLE_FORMS_DROPDOWN)
	public WebElement printableFormsDropdown;

	@FindBy(name = ControlLocators.BAN_TEXTBOX)
	public WebElement banTextbox;

	@FindBy(name = ControlLocators.INSURANCE_PAYMENT_SECURITY_CODE_TEXTBOX)
	public WebElement insurancePaymentSecurityCodeTextbox;

	@FindBy(xpath = ControlLocators.CELL_PHONE_NUMBER_TEXTBOX)
	public WebElement cellPhoneNumberTextbox;

	@FindBy(xpath = ControlLocators.MANUAL_CARRIER_WEB_SYSTEM_RADIO_BUTTON)
	public WebElement manualCarrierWebSystemRadioButton;

	@FindBy(xpath = ControlLocators.ACTIVATION_COMPLETE_BUTTON)
	public WebElement activationCompleteButton;

	@FindBy(xpath = ControlLocators.UPDATE_BUTTON)
	public WebElement updateButton;

	@FindBy(xpath = ControlLocators.MOVE_SUBQUEUES_BUTTON)
	public WebElement moveSubqueuesButton;


	// / <summary>
	// / This function will be used to get value of status as string w.r.t.
	// order id.
	// / Pass the order id as string.
	// / It will return value of status as string.
	// / </summary>
	// / Author:Tarun
	public String getOrderStatus() {
		Utilities
				.waitForElementVisible(PageBase.OrderSummaryPage().statusValueLink);
		String status = PageBase.OrderSummaryPage().statusValueLink.getText();
		return status;
	}

	public String getIssuedRMA() {
		Utilities.waitForElementVisible(PageBase.OrderSummaryPage().issuedRMA);
		String issuedRMA = PageBase.OrderSummaryPage().issuedRMA.getText();
		return issuedRMA;
	}

	public String getChildOrderId() {
		Utilities.waitForElementVisible(PageBase.OrderSummaryPage().childOrder);
		String childOrderId = PageBase.OrderSummaryPage().childOrder.getText();
		return childOrderId;
	}

	public void goToOrderSummaryPage(String orderId) {
		Utilities.waitForElementVisible(PageBase.ShipAdminPage().orderTextbox);
		PageBase.ShipAdminPage().orderTextbox.sendKeys(orderId);
		PageBase.ShipAdminPage().orderTextbox.sendKeys(Keys.ENTER);
		Utilities
				.waitForElementVisible(PageBase.OrderSummaryPage().statusValueLink);
	}

	public String checkForErrorAndLog(String orderId) {
		String eventLogTableContent = PageBase.OrderSummaryPage().eventLogTable
				.getText();
		String lines[] = eventLogTableContent.split("\n");
		Log.info("-------------------START ERROR INFO-----------------------------");
		Log.info("Error Info for Order Number:-" + orderId + "\n"); // ordreId
		Log.info("Order Status:-"
				+ PageBase.OrderSummaryPage().statusValueLink.getText() + "\n");
		System.out.println(lines.length);
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("Error")) {
				Log.error(lines[i] + "\n");
			}
		}
		Log.info("---------------------END ERROR INFO---------------------------");
		return eventLogTableContent;
	}

	public void verifyAppleCareInsuranceStatus() {
		Utilities
				.dropdownSelect(viewDetailsDropdown,
						Utilities.SelectDropMethod.SELECTBYTEXT,
						"View POA Extra Infos");
		Assert.assertEquals(appleCareInsuranceStatus.getText(), "1");
	}

	public void SelectActivationInfo(String orderId) {
		// viewDetailsDropdown.click();
		goToOrderSummaryPage(orderId);
		Utilities.dropdownSelect(viewDetailsDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT,
				Constants.VIEW_ACTIVATION_INFO);
		Utilities.waitForElementVisible(deactivateLine1);
		deactivateLine1.click();
		updateButton.click();
	}
}