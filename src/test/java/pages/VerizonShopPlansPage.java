package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.ControlLocators;
import framework.PageBase;
import framework.Utilities;

public class VerizonShopPlansPage {

	public VerizonShopPlansPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// region- Verizon Shop Plans Elements.
	@FindBy(xpath = ControlLocators.KEEP_MY_EXISTING_VERIZON_WIRELESS_LEGACY_ADD_BUTTON)
	public WebElement KeepMyExistingVerizonWirelessLegacyAddButton;

	@FindBy(xpath = ControlLocators.VERIZON_MORE_EVERYTHING_UNLIMITED_MINUTES_AND_MESSAGING)
	public WebElement Verizon_More_Everything_Unlimited_Minutes_And_Messaging;

	@FindBy(xpath = ControlLocators.VERIZON_MORE_EVERYTHING_UNLIMITED_MINUTES_AND_MESSAGING_500MB_DATA_ADD_BUTTON)
	public WebElement verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton;

	@FindBy(xpath = ControlLocators.VERIZON_MORE_EVERYTHING)
	public WebElement VerizonMoreEverything;

	@FindBy(xpath = ControlLocators.VERIZON_MORE_PLAN_ONLY)
	public WebElement verizonMorePlanOnly;

	@FindBy(xpath = ControlLocators.SPRINT_FAMILY_SHARE_PLAN)
	public WebElement sprintFamilySharePlan;

	@FindBy(xpath=ControlLocators.ATT_UNLIMITED_PLAN)
	public WebElement attUnlimitedPlan;

	@FindBy(xpath = ControlLocators.ADD_PLAN)
	public WebElement addPlan;

	@FindBy(xpath = ControlLocators.KEEP_MY_EXISTING_SPRINT_PLAN)
	public WebElement KeepMyExistingSprintPlan;

	@FindBy(xpath = ControlLocators.SPRINT_MORE_EVERYTHING)
	public WebElement sprintMoreEveryThing;

	@FindBy(xpath = ControlLocators.SPRINT_FIRST_PLAN)
	public WebElement sprintFirstPlan;

	@FindBy(xpath = ControlLocators.FIRST_PLAN_ADD_BUTTON)
	public WebElement firstPlanAddButton;

	@FindBy(xpath = ControlLocators.VERIZON_ANY_PLAN)
	public WebElement verizonAnyPlan;

	@FindBy(xpath = ControlLocators.VERIZON_SECONDPLAN)
	public WebElement verizonSecondPlan;
	// endregion

	// region -Methods
	public String selectPlanWithMore() {
		String orderDescription = PageBase.OrderReviewAndConfirmPage().planDescriptionText
				.getText();
		PageBase.VerizonShopPlansPage().verizonMorePlanOnly.click();
		return orderDescription;
	}

	public void addPlan() {
		Utilities
				.waitForElementVisible(PageBase.VerizonShopPlansPage().addPlan);
		PageBase.VerizonShopPlansPage().addPlan.click();
	}

	public void selectAnyPlan()
	{
		PageBase.VerizonShopPlansPage().verizonAnyPlan.click();
	}
	// endregion
}