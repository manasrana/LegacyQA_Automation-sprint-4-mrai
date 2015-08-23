package pages;

import framework.PageBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.ServiceProviderVerificationPage.IdType;
import framework.ControlLocators;
import framework.Utilities;

public class GuestVerificationPage {
	public GuestVerificationPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(id = ControlLocators.ID_TYPE_DROPDOWN)
	public WebElement idTypeDropdown;

	@FindBy(id = ControlLocators.STATE_DROPDOWN)
	public WebElement stateDropdown;

	@FindBy(id = ControlLocators.ID_NUMBER_TEXTBOX)
	public WebElement idNumberTextbox;

	@FindBy(id = ControlLocators.FIRST_NAME)
	public WebElement firstName;

	@FindBy(id = ControlLocators.LAST_NAME)
	public WebElement lastName;

	public void populateGuestVerificationDetails(IdType idType, String state,
			String idNumber, String fName, String lName) {
		Utilities.dropdownSelect(idTypeDropdown,
				Utilities.SelectDropMethod.SELECTBYINDEX, "1");
		Utilities.dropdownSelect(stateDropdown,
				Utilities.SelectDropMethod.SELECTBYTEXT, state);
		idNumberTextbox.sendKeys(idNumber);
		firstName.sendKeys(fName);
		lastName.sendKeys(lName);
		PageBase.CustomerLookupPage().continueButton.click();
	}
}