package pages;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.ControlLocators;
import framework.CreditCardDetails;
import framework.PageBase;
import framework.Utilities;

public class PaymentRequiredPage {

	// region Page Initialization
	public PaymentRequiredPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// endregion Page Initialization

	// region Enums
	public enum CardType {
		VISA, MASTERCARD, DISCOVER, AMERICANEXPRESS
	}

	public enum MonthIdExp {
		JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER

	}

	// endregion Enums

	// region Payment Required Page Elements
	@FindBy(id = ControlLocators.VISA_TAB)
	public WebElement visaTab;

	@FindBy(id = ControlLocators.MASTERCARD_TAB)
	public WebElement mastercardTab;

	@FindBy(id = ControlLocators.DISCOVER_TAB)
	public WebElement discoverTab;

	@FindBy(id = ControlLocators.AMERICAN_EXPRESS_TAB)
	public WebElement americanExpressTab;

	@FindBy(id = ControlLocators.CREDIT_CARD_NUMBER_TEXTBOX)
	public WebElement creditCardNumberTextbox;

	@FindBy(id = ControlLocators.MONTH_ID_EXP_DROPDOWN)
	public WebElement monthIDExpDropdown;

	@FindBy(id = ControlLocators.YEAR_ID_EXPDROPDOWN)
	public WebElement yearIDExpDropdown;

	@FindBy(id = ControlLocators.CVN_NUMBER_TEXTBOX)
	public WebElement cvnNumberTextbox;

	@FindBy(id = ControlLocators.CONTINUE_PR_BUTTON)
	public WebElement continuePRButton;

	@FindBy(xpath = ControlLocators.SAME_ADDRESS_TAB)
	public WebElement sameAddressTab;

	// endregion Payment Required Page Elements

	// region Methods

	// / <summary>
	// / This function will be used to populate Credit Card Details on Payment
	// Required Page.
	// / </summary>
	// / Author:Tarun
	public void populatingCardDetailsPaymentRequired(CardType cardType)
			throws IOException {
		CreditCardDetails CreditCard = PageBase.CSVOperations()
				.CreditCardDetails(cardType);
		switch (cardType) {
		case VISA:
			visaTab.click();
			break;
		case MASTERCARD:
			mastercardTab.click();
			break;
		case DISCOVER:
			discoverTab.click();
			break;
		case AMERICANEXPRESS:
			americanExpressTab.click();
			break;
		}
		creditCardNumberTextbox.clear();
		creditCardNumberTextbox.sendKeys(CreditCard.Number);
		String month = CreditCard.ExpiryMonth.toUpperCase();
		switch (month) {
		case "JANUARY":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "1");
			break;
		case "FEBRUARY":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "2");
			break;
		case "MARCH":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "3");
			break;
		case "APRIL":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "4");
			break;
		case "MAY":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "5");
			break;
		case "JUNE":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "6");
			break;
		case "JULY":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "7");
			break;
		case "AUGUST":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "8");
			break;
		case "SEPTEMBER":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "9");
			break;
		case "OCTOBER":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "10");
			break;
		case "NOVEMBER":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "11");
			break;
		case "DECEMBER":
			Utilities.dropdownSelect(monthIDExpDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "12");
			break;
		}
		String yearS = "" + CreditCard.ExpiryYear;
		Utilities.dropdownSelect(yearIDExpDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, yearS);
		String cVNNumberS = "" + CreditCard.CVV;
		cvnNumberTextbox.sendKeys(cVNNumberS);
	}
	// endregion Methods
}
