package pages;

import framework.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import framework.ControlLocators;
import framework.PageBase;

public class SelectProtectionPlanInsurancePage {

	// region Page Initialization
	public SelectProtectionPlanInsurancePage(WebDriver d) {
		PageFactory.initElements(d, this);
	}

	// endregion Page Initialization

	// region Select Insurance Page Elements
	@FindBy(xpath = ControlLocators.E_SECURITEL_INSURANCE)
	public WebElement eSecuritelInsurance;

	@FindBy(xpath = ControlLocators.NO_INSURANCE)
	public WebElement NoInsurance;

	@FindBy(xpath = ControlLocators.NOINSURANCE_FIRSTMOBILE)
	public WebElement noInsuranceFirstMob;

	@FindBy(xpath = ControlLocators.NOINSURANCE_SECONDMOBILE)
	public WebElement noInsurancesecondMob;

	@FindBy(xpath = ControlLocators.NOINSURANCE_THIRDMOBILE)
	public WebElement noInsuranceThirdMob;

	@FindBy(name = ControlLocators.GUEST_REVIEW)
	public WebElement guestReview;

	@FindBy(xpath = ControlLocators.E_SECURITEL_INSURANCE_FIRST)
	public WebElement eSecuritelInsuranceFirst;

	@FindBy(xpath = ControlLocators.E_SECURITEL_INSURANCE_SECOND)
	public WebElement eSecuritelInsuranceSecond;

	@FindBy(xpath = ControlLocators.APPLECARE_INSURANCE)
	public WebElement appleCareInsurance;

	@FindBy(xpath = ControlLocators.NOINSURANCE_SPRINT_FIRSTMOBILE)
	public WebElement noInsuranceSprintFirstMob;

	@FindBy(xpath = ControlLocators.NOINSURANCE_SPRINT_SECONDMOBILE)
	public WebElement noInsuranceSprintSecondMob;

	@FindBy(xpath = ControlLocators.E_SECURITEL_INSURANCE_SECOND_DEVICE)
	public WebElement eSecuritelInsuranceSecondDevice;

	// endregion Select Insurance Page Elements

	// region Utility Methods
	public void selectAnInsurance() {
		PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst
				.click();
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		PageBase.CommonControls().continueButton.click();
	}

	public void selectNoInsurance() {
		Utilities.waitForElementVisible(PageBase
				.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		Utilities.implicitWaitSleep(6000);
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		Utilities.implicitWaitSleep(6000);
		PageBase.CommonControls().continueButton.click();
	}

	public void selectNoInsuranceForTwoDevices() {
		noInsuranceFirstMob.click();
		if(!noInsuranceFirstMob.isSelected()) {
			noInsuranceFirstMob.click();
		}
		Utilities.implicitWaitSleep(5000);
		noInsurancesecondMob.click();
		if(!noInsurancesecondMob.isSelected()) {
			noInsurancesecondMob.click();
		}
		Utilities.implicitWaitSleep(5000);
		guestReview.click();
		PageBase.CommonControls().continueButton.click();
	}

    public void selectNoInsuranceForSingleDevicesVerizonCarrier() {
        noInsuranceFirstMob.click();
        if(!noInsuranceFirstMob.isSelected()) {
            noInsuranceFirstMob.click();
        }
        Utilities.implicitWaitSleep(5000);
        guestReview.click();
        PageBase.CommonControls().continueButton.click();
    }

	public void selectNoInsuranceForSprintWithTwoDevices() {
		noInsuranceSprintFirstMob.click();
		noInsuranceSprintFirstMob.click();
		Utilities.implicitWaitSleep(8000);
		noInsuranceSprintSecondMob.click();
		noInsuranceSprintSecondMob.click();
		guestReview.click();
		Utilities.implicitWaitSleep(8000);
		PageBase.CommonControls().continueButton.click();
	}

	public void selectNoInsuranceForSprintWithOneDevices() {
		noInsuranceSprintFirstMob.click();
		noInsuranceSprintFirstMob.click();
		Utilities.implicitWaitSleep(6000);
		guestReview.click();
		Utilities.implicitWaitSleep(6000);
		PageBase.CommonControls().continueButton.click();
	}

	public void selectNoInsuranceForThreeDevices() {
		noInsuranceFirstMob.click();
		if(!noInsuranceFirstMob.isSelected()) {
			noInsuranceFirstMob.click();
		}
		Utilities.implicitWaitSleep(5000);
		noInsurancesecondMob.click();
		if(!noInsurancesecondMob.isSelected()) {
			noInsurancesecondMob.click();
		}
		Utilities.implicitWaitSleep(5000);
		noInsuranceThirdMob.click();
		if(!noInsuranceThirdMob.isSelected()) {
			noInsuranceThirdMob.click();
		}
		Utilities.implicitWaitSleep(5000);
		guestReview.click();
		PageBase.CommonControls().continueButton.click();
	}

	public void selectInsuranceForTwoDevices() {
		eSecuritelInsurance.click();
		if(!eSecuritelInsurance.isSelected()) {
			eSecuritelInsurance.click();
		}
		Utilities.implicitWaitSleep(5000);
		eSecuritelInsuranceSecondDevice.click();
		if(!eSecuritelInsuranceSecondDevice.isSelected()) {
			eSecuritelInsuranceSecondDevice.click();
		}
		Utilities.implicitWaitSleep(5000);
		guestReview.click();
		PageBase.CommonControls().continueButton.click();
	}
	// endregion Utility Methods
}