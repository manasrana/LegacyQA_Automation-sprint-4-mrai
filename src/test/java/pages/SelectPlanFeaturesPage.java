package pages;

import java.util.List;

import framework.PageBase;
import framework.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import framework.BrowserSettings;
import framework.ControlLocators;

public class SelectPlanFeaturesPage {

	public SelectPlanFeaturesPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.CANCEL_SPF_BUTTON)
	public WebElement cancelSPFButton;

	@FindBy(id = ControlLocators.CONTINUE_SPF_BUTTON)
	public WebElement continueSPFButton;

	@FindBy(xpath=ControlLocators.MESSAGING)
	public WebElement Messaging;

	@FindBy(xpath=ControlLocators.FAMILYMAP)
	public WebElement familyMap;

	@FindBy(xpath=ControlLocators.IPHONEDATA)
	public WebElement iphoneData;

	@FindBy(xpath=ControlLocators.OTHER)
	public WebElement otherPlan;

	@FindBy(xpath=ControlLocators.NAGIVATION)
	public WebElement navigation;

	// Selecting Network access plan.
	public void selectNetworkAccessPlan(int planSequence) {
		// Get all the links displayed
		List<WebElement> networkList = BrowserSettings.driver
				.findElements(By
						.xpath("//span[contains(text(),'Network Access')]/ancestor::a"));

		// Selecting Network Access plan.
		networkList.get(planSequence).click();
		Utilities.checkingChkbox(BrowserSettings.driver.findElement(By
				.id("addPoptInput_2_99020")));
	}

	// Selecting Family base plan.
	public void selectFamilyBasePlan(int planSequence) {
		// Get all the links displayed
		List<WebElement> networkList = BrowserSettings.driver.findElements(By
				.xpath("//span[contains(text(),'FamilyBase')]/ancestor::a"));

		// Selecting Family Base plan.
		networkList.get(planSequence).click();
		Utilities.checkingChkbox(BrowserSettings.driver.findElement(By
				.id("addPoptInput_1_99334")));
	}

	// Selecting Message plan
	public void selectMessagePlan(){
		Utilities.checkingChkbox(BrowserSettings.driver.findElement(By
				.id("addPoptInput_1_99085")));
	}

	//Selecting FamilyMap plan
	public void selectFamilyMapPlan(int planSequence)
	{
		Utilities.ClickElement(PageBase.SelectPlanFeaturesPage().familyMap);
		// Get all the links displayed
		List<WebElement> networkList = BrowserSettings.driver.findElements(By
				.id("addPoptInput_1_99087"));

		// Selecting Family Map plan.
		networkList.get(planSequence).click();
		Utilities.checkingChkbox(BrowserSettings.driver.findElement(By
				.id("addPoptInput_1_99087")));
	}

	//Selecting Iphone Data
	public void selectIphoneData(int planSequence){
		Utilities.ClickElement(PageBase.SelectPlanFeaturesPage().iphoneData);
		// Get all the links displayed
		List<WebElement> networkList = BrowserSettings.driver.findElements(By
				.id("addPoptInput_1_99089"));

		// Selecting Family Base plan.
		networkList.get(planSequence).click();
		Utilities.checkingChkbox(BrowserSettings.driver.findElement(By
				.id("addPoptInput_1_99089")));
	}

	//Selecting Other Plan
	public void selectOtherPlan(int planSequence){
		Utilities.ClickElement(PageBase.SelectPlanFeaturesPage().otherPlan);
		// Get all the links displayed
		List<WebElement> networkList = BrowserSettings.driver.findElements(By
				.id("addPoptInput_1_99094"));

		// Selecting Other plan.
		networkList.get(planSequence).click();
		Utilities.checkingChkbox(BrowserSettings.driver.findElement(By
				.id("addPoptInput_1_99094")));
	}

	// Selecting Navigation plan
	public void selectNavigationPlan(){
		Utilities.ClickElement(PageBase.SelectPlanFeaturesPage().navigation);
		Utilities.checkingChkbox(BrowserSettings.driver.findElement(By
				.id("addPoptInput_1_99096")));
	}
}