package pages;

import framework.Log;
import framework.PageBase;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.ControlLocators;

public class OrderReviewAndConfirmPage {

    // region Page Initialization
    public OrderReviewAndConfirmPage(WebDriver d) {
        PageFactory.initElements(d, this);
    }

    // endregion Page Initialization

    // region Select Insurance Page Elements
    @FindBy(xpath = ControlLocators.CANCEL_ORDER_BUTTON)
    public WebElement cancelOrderButton;

    @FindBy(xpath = ControlLocators.PLAN_DESCRIPTION_TEXT)
    public WebElement planDescriptionText;

    @FindBy(xpath = ControlLocators.VERIFY_2YR_UPGRADE_TEXT)
    public WebElement verify2yrUpgradeText;

    @FindBy(xpath = ControlLocators.VERIFY_PLAN_DESCRIPTION_TEXT)
    public WebElement verifyPlanDescriptionText;

    @FindBy(xpath = ControlLocators.phonePrice$649_99)
    public WebElement phonePrice$649_99;

    @FindBy(xpath = ControlLocators.PHONE_PRICE_LINE1)
    public WebElement phonePriceLine1;

    @FindBy(xpath = ControlLocators.PHONE_PRICE_LINE2)
    public WebElement phonePriceLine2;

    @FindBy(xpath = ControlLocators.UPGRADE_FEE_VALUE_TEXT)
    public WebElement upgradeFeeValueText;

    @FindBy(xpath = ControlLocators.UPGRADE_FEE_VALUE_2_TEXT)
    public WebElement upgradeFeeValue2Text;

    @FindBy(xpath = ControlLocators.TOTAL_DUE_TODAY_VALUE)
    public WebElement totalDueTodayValue;

    @FindBy(xpath = ControlLocators.EXISTING_PLAN_DIV)
    public WebElement existingPlanDiv;

    @FindBy(xpath = ControlLocators.EXISTING_PLAN_2_DIV)
    public WebElement existingPlan2Div;

    @FindBy(xpath = ControlLocators.TWO_YEARS_UPGRADE_LABEL)
    public WebElement twoYearsUpgradeLabel;

    @FindBy(xpath = ControlLocators.TWO_YEARS_UPGRADE_2_LABEL)
    public WebElement twoYearsUpgrade2Label;

    @FindBy(xpath = ControlLocators.TWO_YEAR_CONTRACT_LABEL_1)
    public WebElement twoYearContractLabel1;

	/*@FindBy(xpath = ControlLocators.DEVICE1_PRICE_ORDERREVIEW)
    public WebElement device1PriceActual;

	@FindBy(xpath = ControlLocators.DEVICE2_PRICE_ORDERREVIEW)
	public WebElement device2PriceActual;

	@FindBy(xpath = ControlLocators.DEVICE3_PRICE_ORDERREVIEW)
	public WebElement device3PriceActual;

	@FindBy(xpath = ControlLocators.TOTAL_FEE)
	public WebElement totalFeeActual;*/

    @FindBy(xpath = ControlLocators.DEVICE1_ACTIVATIONFEE_ORDERREVIEW)
    public WebElement device1ActivationFeeActual;

    @FindBy(xpath = ControlLocators.DEVICE2_ACTIVATIONFEE_ORDERREVIEW)
    public WebElement device2ActivationFeeActual;

    @FindBy(xpath = ControlLocators.DEVICE3_ACTIVATIONFEE_ORDERREVIEW)
    public WebElement device3ActivationFeeActual;

    @FindBy(xpath = ControlLocators.PLAN_PRICE_ORDERREVIEW)
    public WebElement planPrice;

    @FindBy(className = ControlLocators.ORDER_DETAILS)
    public WebElement orderDetails;

    @FindBy(xpath = ControlLocators.PHONE_MONTHLY_FEE)
    public WebElement phoneMonthlyFee;;

    @FindBy(id = ControlLocators.PAGE_TEXT)
    public WebElement pageText;

    @FindBy(xpath = ControlLocators.FIRST_PHONE_PRICE_Value_TEXT)
    public WebElement firstPhonePrice;

    @FindBy(xpath = ControlLocators.LAST_MONTH_INSTALLMENT_LABEL)
    public WebElement lastMonthInstallmentLabel;

    @FindBy(xpath = ControlLocators.MONTHLY_INSTALLMENT_LABEL)
    public WebElement monthlyInstallmentLabel;

    @FindBy(xpath = ControlLocators.DOWN_PAYMENT_ORDER_REVIEW_LABEL)
    public WebElement downPaymentLabel;

    @FindBy(xpath = ControlLocators.MONTHLY_RECURRING_FEE_LABEL)
    public WebElement monthlyRecurringFeeLabel;

    @FindBy(xpath = ControlLocators.DEVICE1_PRICE_ORDERCONFIRM)
    public WebElement device1PriceActual;

    @FindBy(xpath = ControlLocators.DEVICE2_PRICE_ORDERCONFIRM)
    public WebElement device2PriceActual;

    @FindBy(xpath = ControlLocators.DEVICE3_PRICE_ORDERCONFIRM)
    public WebElement device3PriceActual;

    @FindBy(xpath = ControlLocators.LINE1ACTIVATIONFEE_3DEVICES)
    public WebElement device1ActivationFee3Devices;

    @FindBy(xpath = ControlLocators.LINE2ACTIVATIONFEE_3DEVICES)
    public WebElement device2ActivationFee3Devices;

    @FindBy(xpath = ControlLocators.LINE3ACTIVATIONFEE_3DEVICES)
    public WebElement device3ActivationFee3Devices;

    @FindBy(xpath = ControlLocators.LINE1ACTIVATIONFEE_2DEVICES)
    public WebElement device1ActivationFee2Devices;

    @FindBy(xpath = ControlLocators.LINE2ACTIVATIONFEE_2DEVICES)
    public WebElement device2ActivationFee2Devices;

    @FindBy(xpath = ControlLocators.LINE1ACTIVATIONFEE_1DEVICE)
    public WebElement device1ActivationFee1Device;

    @FindBy(xpath = ControlLocators.TOTAL_MONTHLYFEES)
    public WebElement totalFeeActual;

    @FindBy(xpath = ControlLocators.ESECURITELINSURANCE_ORDERCONFIRM)
    public WebElement esecuritelInsuranceText;

    @FindBy(xpath = ControlLocators.EXISTING_PLAN_1_DIV)
    public WebElement existingPlan1Div;

    @FindBy(xpath = ControlLocators.DOWN_PAYMENT_FOR_SPRINT_EASY_PAY_LABEL)
    public WebElement downPaymentForSprintEasyPayLabel;


    // endregion

    //region Methods
    public String GetOrderDetails() {
        String orderTableContent = orderDetails.getText();
        // String lines[] = orderTableContent.split("\n");
        // Log.info("-------------------START ERROR INFO-----------------------------");
        // Log.info("Error Info for Order Number:-"+orderId+"\n"); //ordreId
        // Log.info("Order Status:-"+PageBase.OrderSummaryPage().statusValueLink.getText()+"\n");
        // System.out.println(lines.toString());
        return orderTableContent;
    }

    public boolean OrderReviewConfirmPageAssertionsFor3Devices(String device1Price, String device2Price, String device3Price) {
        try {
            Assert.assertEquals(device1PriceActual.getText(), device1Price);
            Assert.assertEquals(device2PriceActual.getText(), device2Price);
            Assert.assertEquals(device3PriceActual.getText(), device3Price);
            String monthlyFee = totalFeeActual.getText();

            String line1Fee = device1ActivationFee3Devices.getText();
            String line2Fee = device2ActivationFee3Devices.getText();
            String line3Fee = device3ActivationFee3Devices.getText();

            double line1Fees = Double.parseDouble(line1Fee.substring(1, 3));
            double line2Fees = Double.parseDouble(line2Fee.substring(1, 3));
            double line3Fees = Double.parseDouble(line3Fee.substring(1, 3));
            double monthlyFees = Double.parseDouble(monthlyFee.substring(1).replace("+ tax", "").replace(" ", ""));

            Assert.assertTrue(line1Fees + line2Fees + line3Fees <= monthlyFees);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean OrderReviewConfirmPageAssertionsFor1Device(String device1Price) {
        try {
            //Assert.assertEquals(device1PriceActual.getText(), device1Price);
            String monthlyFee = totalFeeActual.getText();

            String line1Fee = device1ActivationFee1Device.getText();

            double line1Fees = Double.parseDouble(line1Fee.substring(1, 3));
            double monthlyFees = Double.parseDouble(monthlyFee.substring(1).replace("+ tax","").replace(" ", ""));

            Assert.assertTrue(line1Fees < monthlyFees);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean OrderReviewConfirmPageAssertionsFor2Devices(String device1Price, String device2Price) {
        try {
            Assert.assertEquals(device1PriceActual.getText(), device1Price);
            Assert.assertEquals(device2PriceActual.getText(), device2Price);
            String monthlyFee = totalFeeActual.getText();

            String line1Fee = device1ActivationFee2Devices.getText();
            String line2Fee = device2ActivationFee2Devices.getText();
            Double.parseDouble(line2Fee.substring(1, 3));
            double line1Fees = Double.parseDouble(line1Fee.substring(1, 3));
            double line2Fees = Double.parseDouble(line2Fee.substring(1, 3));
            double monthlyFees = Double.parseDouble(monthlyFee.substring(1).replace("+ tax","").replace(" ", ""));

            Assert.assertTrue(line1Fees + line2Fees<monthlyFees);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
//endregion Methods
}