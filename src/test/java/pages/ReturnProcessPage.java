package pages;

import framework.Utilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.BrowserSettings;
import framework.ControlLocators;

public class ReturnProcessPage {
	public ReturnProcessPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	public enum FinancingOption {
		CHECKFINANCING, NOFINANCING
	}

	public enum ExchangeReason {
		INPOLICYGUESTRETURN, WILLNOTPOWERON, OUTSIDEOFPOLICY
	}

	@FindBy(xpath = ControlLocators.ACCEPTRETURNEXCHANGE_RADIOBUTTON)
	public WebElement acceptReturnExchangeRadioButton;

	@FindBy(xpath = ControlLocators.EXCHANGEDEVICE_RADIOBUTTON)
	public WebElement exchangeDeviceRadioButton;

	@FindBy(id = ControlLocators.FINANCING_DROPDOWN)
	public WebElement financingDropDown;

	@FindBy(id = ControlLocators.EXCHANGEREASONS_DROPDOWN)
	public WebElement exchangeReasonDropdown;

	@FindBy(xpath = ControlLocators.RETURNDEVICEESN_TEXTBOX)
	public WebElement returnDeviceESNTextbox;

	@FindBy(id = ControlLocators.SUBMITFORM_BUTTON)
	public WebElement submitFormButton;

	@FindBy(id = ControlLocators.EXCHANGE_MESSAGE)
	public WebElement exchangeMessage;

	@FindBy(xpath = ControlLocators.CONTINUE_EXCHANGE_BUTTON)
	public WebElement continueExchangeButton;

	public void SelectFinancingOption(FinancingOption financeChoice) {
		switch (financeChoice) {
		case CHECKFINANCING:
			Utilities.dropdownSelect(financingDropDown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "1");
			break;
		case NOFINANCING:
			Utilities.dropdownSelect(financingDropDown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "2");
			break;
		}
	}

	public void SelectExchangeReason(ExchangeReason exhangeReason) {
		switch (exhangeReason) {
		case INPOLICYGUESTRETURN:
			Utilities.dropdownSelect(exchangeReasonDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "1");
			break;
		case WILLNOTPOWERON:
			Utilities.dropdownSelect(exchangeReasonDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "2");
			break;
		case OUTSIDEOFPOLICY:
			Utilities.dropdownSelect(exchangeReasonDropdown,
					Utilities.SelectDropMethod.SELECTBYINDEX, "3");
			break;
		}
	}
}