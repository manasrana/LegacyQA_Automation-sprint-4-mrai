package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SelectAnOptionPage {
	public SelectAnOptionPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(id = ControlLocators.AAL_EXISTING_FAMILY_PLAN)
	public WebElement AALExistingFamilyPlan;

	@FindBy(id = ControlLocators.SWITCH_FROM_AN_INDIVIDUAL_TO_FAMILY_PLAN_AND_ADD_A_LINE_RADIO_BUTTON)
	public WebElement switchFromAnIndividualToFamilyPlanAndAddALineRadioButton;
}