package pages;

import java.util.List;

import framework.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.thoughtworks.selenium.webdriven.commands.Click;
import org.testng.Assert;

public class UECAddLinesPage extends RetailBaseClass {

    // region Page Initialization
    public UECAddLinesPage(WebDriver d) {
        PageFactory.initElements(d, this);
    }

    // endregion Page Initialization

    // region UEC Add Lines Page Elements
    @FindBy(xpath = ControlLocators.FIRST_AAL_CHECKBOX)
    public WebElement firstAALCheckbox;

    @FindBy(xpath = ControlLocators.SECOND_AAL_CHECKBOX)
    public WebElement secondAALCheckbox;

    @FindBy(xpath = ControlLocators.THIRD_AAL_CHECKBOX)
    public WebElement thirdAALCheckbox;

    @FindBy(xpath = ControlLocators.FOURTH_AAL_CHECKBOX)
    public WebElement fourthAALCheckbox;

    @FindBy(xpath = ControlLocators.FIFTH_AAL_CHECKBOX)
    public WebElement fifthAALCheckbox;

    @FindBy(xpath = ControlLocators.CONTINUE_UEC_ADD_LINES_BUTTON)
    public WebElement continueUECAddLinesButton;

    @FindBy(xpath = ControlLocators.ELIGIBLE_FOR_DF)
    public WebElement eligibleForDF;

    @FindBy(xpath = ControlLocators.ELIGIBLE_FOR_2YEAR)
    public WebElement eligibleFor2Year;

    @FindBy(xpath = ControlLocators.TRANSFER_ELIGIBLE)
    public WebElement transferEligible;

    @FindBy(id = ControlLocators.PLEASE_SELECT_AN_ELIGIBLE_DONOR_1_DROPDOWN)
    public WebElement pleaseSelectAnEligibleDonor1Dropdown;

    @FindBy(id = ControlLocators.PLEASE_SELECT_AN_ELIGIBLE_DONOR_2_DROPDOWN)
    public WebElement pleaseSelectAnEligibleDonor2Dropdown;

    @FindBy(id = ControlLocators.PLEASE_SELECT_AN_ELIGIBLE_DONOR_3_DROPDOWN)
    public WebElement pleaseSelectAnEligibleDonor3Dropdown;

    @FindBy(id = ControlLocators.PLEASE_SELECT_AN_ELIGIBLE_DONOR_4_DROPDOWN)
    public WebElement pleaseSelectAnEligibleDonor4Dropdown;

    @FindBy(id = ControlLocators.PLEASE_SELECT_AN_ELIGIBLE_DONOR_5_DROPDOWN)
    public WebElement pleaseSelectAnEligibleDonor5Dropdown;

    @FindBy(id = ControlLocators.PLEASE_SELECT_AN_ELIGIBLE_DONOR_6_DROPDOWN)
    public WebElement pleaseSelectAnEligibleDonor6Dropdown;

    @FindBy(id = ControlLocators.PLEASE_SELECT_AN_ELIGIBLE_DONOR_7_DROPDOWN)
    public WebElement pleaseSelectAnEligibleDonor7Dropdown;

    @FindBy(id = ControlLocators.PLEASE_SELECT_AN_ELIGIBLE_DONOR_8_DROPDOWN)
    public WebElement pleaseSelectAnEligibleDonor8Dropdown;

    @FindBy(id = ControlLocators.PLEASE_SELECT_AN_ELIGIBLE_DONOR_9_DROPDOWN)
    public WebElement pleaseSelectAnEligibleDonor9Dropdown;

    @FindBy(xpath = ControlLocators.ALL_NUMBERS_ELEMENTS)
    public List<WebElement> numbersUnderAccount;

    @FindBy(xpath = ControlLocators.FIRST_EARLY_UPGRADE_CHECKBOX)
    public WebElement firstEarlyUpgradeCheckbox;

    @FindBy(xpath = ControlLocators.SECOND_EARLY_UPGRADE_CHECKBOX)
    public WebElement secondEarlyUpgradeCheckbox;

    @FindBy(xpath = ControlLocators.THIRD_EARLY_UPGRADE_CHECKBOX)
    public WebElement thirdEarlyUpgradeCheckbox;

    @FindBy(xpath = ControlLocators.FOURTH_EARLY_UPGRADE_CHECKBOX)
    public WebElement fourthEarlyUpgradeCheckbox;

    @FindBy(xpath = ControlLocators.FIFTH_EARLY_UPGRADE_CHECKBOX)
    public WebElement fifthEarlyUpgradeCheckbox;

    @FindBy(xpath = ControlLocators.SIXTH_EARLY_UPGRADE_CHECKBOX)
    public WebElement sixthEarlyUpgradeCheckbox;

    @FindBy(xpath = ControlLocators.SEVENTH_EARLY_UPGRADE_CHECKBOX)
    public WebElement seventhEarlyUpgradeCheckbox;

    @FindBy(xpath = ControlLocators.EIGHTH_EARLY_UPGRADE_CHECKBOX)
    public WebElement eighthEarlyUpgradeCheckbox;

    @FindBy(xpath = ControlLocators.NINTH_EARLY_UPGRADE_CHECKBOX)
    public WebElement ninthEarlyUpgradeCheckbox;

    @FindBy(xpath = ControlLocators.TENTH_EARLY_UPGRADE_CHECKBOX)
    public WebElement tenthEarlyUpgradeCheckbox;

    // end region UEC Add Line Page Elements

    // region Methods
    public void addALine() {
        PageBase.UECAddLinesPage().clickEnabledCheckBoxOtherThanFirst();
        PageBase.UECAddLinesPage().continueUECAddLinesButton.click();
    }

    // This method will be used for checking checkbox for a particular number.
    // Author : Tarun
    public void clickCheckboxForParticularPhoneNumber(String phoneNumber) {
        String formattedPhoneNumber = CommonFunction
                .getFormattedPhoneNumber(phoneNumber);
        driver.findElement(
                By.xpath("//span[contains(text(),'Mobile Number:  "
                        + formattedPhoneNumber
                        + "')]/parent::span/parent::label/parent::div/child::input"))
                .click();
    }

    // This method will be used for checking first enabled checkbox.
    // Author : Tarun
    public void clickFirstEnabledCheckbox() {
        try {
            Utilities.waitForElementVisible(firstAALCheckbox);
            if (firstAALCheckbox.isEnabled()) {
                firstAALCheckbox.click();
            } else if (secondAALCheckbox.isEnabled()) {
                secondAALCheckbox.click();
            } else if (thirdAALCheckbox.isEnabled()) {
                thirdAALCheckbox.click();
            } else if (fourthAALCheckbox.isEnabled()) {
                fourthAALCheckbox.click();
            } else if (fifthAALCheckbox.isEnabled()) {
                fifthAALCheckbox.click();
            } else {
                Log.error("Not even single checkbox is enabled");
                System.out.println("Not even single checkbox is enabled");
                Assert.assertTrue(false);
            }
        } catch (Exception e) {
            Log.error("Not even single checkbox is enabled");
            System.out.println("Not even single checkbox is enabled");
            Assert.assertTrue(false);
        }
    }

    // This method will be used for checking first two enabled checkbox.
    // Author : Tarun
    public void clickFirstTwoEnabledCheckbox() {
        try {
            clickFirstEnabledCheckbox();
            if (secondAALCheckbox.isEnabled()
                    && !secondAALCheckbox.isSelected()) {
                secondAALCheckbox.click();
            } else if (thirdAALCheckbox.isEnabled()
                    && !thirdAALCheckbox.isSelected()) {
                thirdAALCheckbox.click();
            } else if (fourthAALCheckbox.isEnabled()
                    && !fourthAALCheckbox.isSelected()) {
                fourthAALCheckbox.click();
            } else if (fifthAALCheckbox.isEnabled()
                    && !fifthAALCheckbox.isSelected()) {
                fifthAALCheckbox.click();
            } else {
                Log.error("No two checkboxes are enabled");
                System.out.println("No two checkboxes are enabled");
                Assert.assertTrue(false);
            }
        } catch (Exception e) {
            Log.error("No two checkboxes are enabled");
            System.out.println("No two checkboxes are enabled");
            Assert.assertTrue(false);
        }
    }

    public void clickEnabledCheckBoxOtherThanFirst() {
        try {
            Utilities.waitForElementVisible(firstAALCheckbox);
            if (secondAALCheckbox.isEnabled()) {
                secondAALCheckbox.click();
            } else if (thirdAALCheckbox.isEnabled()) {
                thirdAALCheckbox.click();
            } else if (fourthAALCheckbox.isEnabled()) {
                fourthAALCheckbox.click();
            } else if (fifthAALCheckbox.isEnabled()) {
                fifthAALCheckbox.click();
            } else {
                Log.error("Not even single checkbox is enabled");
                System.out.println("Not even single checkbox is enabled");
                Assert.assertTrue(false);
            }
        } catch (Exception e) {
            Log.error("Not even single checkbox is enabled");
            System.out.println("Not even single checkbox is enabled");
            Assert.assertTrue(false);
        }
    }

    public void ClickSecondEnableCheckBox() {
        try {
            Utilities.implicitWaitSleep(10000);
            secondAALCheckbox.sendKeys(Keys.PAGE_DOWN);
            Utilities.implicitWaitSleep(10000);
            if (secondAALCheckbox.isEnabled()) {
                secondAALCheckbox.click();
            } else {
                Log.error("Not even single checkbox is enabled");
                System.out.println("Not even single checkbox is enabled");
                Assert.assertTrue(false);
            }
        } catch (Exception e) {
            Log.error("Not even single checkbox is enabled");
            System.out.println("Not even single checkbox is enabled");
            Assert.assertTrue(false);
        }
    }

    public void selectingFirstBuddyUpgradeLine() {
        Utilities.waitForElementVisible(firstAALCheckbox);
        try {
            Utilities
                    .dropdownSelect(
                            PageBase.UECAddLinesPage().pleaseSelectAnEligibleDonor1Dropdown,
                            Utilities.SelectDropMethod.SELECTBYINDEX, "1");
        } catch (Exception e) {
            try {
                Utilities
                        .dropdownSelect(
                                PageBase.UECAddLinesPage().pleaseSelectAnEligibleDonor2Dropdown,
                                Utilities.SelectDropMethod.SELECTBYINDEX, "1");
            } catch (Exception ee) {
                try {
                    Utilities
                            .dropdownSelect(
                                    PageBase.UECAddLinesPage().pleaseSelectAnEligibleDonor3Dropdown,
                                    Utilities.SelectDropMethod.SELECTBYINDEX,
                                    "1");
                } catch (Exception eee) {
                    try {
                        Utilities
                                .dropdownSelect(
                                        PageBase.UECAddLinesPage().pleaseSelectAnEligibleDonor4Dropdown,
                                        Utilities.SelectDropMethod.SELECTBYINDEX,
                                        "1");
                    } catch (Exception eeee) {
                        try {
                            Utilities
                                    .dropdownSelect(
                                            PageBase.UECAddLinesPage().pleaseSelectAnEligibleDonor5Dropdown,
                                            Utilities.SelectDropMethod.SELECTBYINDEX,
                                            "1");
                        } catch (Exception eeeee) {
                            try {
                                Utilities
                                        .dropdownSelect(
                                                PageBase.UECAddLinesPage().pleaseSelectAnEligibleDonor6Dropdown,
                                                Utilities.SelectDropMethod.SELECTBYINDEX,
                                                "1");
                            } catch (Exception eeeeee) {
                                try {
                                    Utilities
                                            .dropdownSelect(
                                                    PageBase.UECAddLinesPage().pleaseSelectAnEligibleDonor7Dropdown,
                                                    Utilities.SelectDropMethod.SELECTBYINDEX,
                                                    "1");
                                } catch (Exception eeeeeee) {
                                    try {
                                        Utilities
                                                .dropdownSelect(
                                                        PageBase.UECAddLinesPage().pleaseSelectAnEligibleDonor8Dropdown,
                                                        Utilities.SelectDropMethod.SELECTBYINDEX,
                                                        "1");
                                    } catch (Exception eeeeeeee) {
                                        try {
                                            Utilities
                                                    .dropdownSelect(
                                                            PageBase.UECAddLinesPage().pleaseSelectAnEligibleDonor9Dropdown,
                                                            Utilities.SelectDropMethod.SELECTBYINDEX,
                                                            "1");
                                        } catch (Exception eeeeeeeee) {
                                            Log.error("No buddy upgrade option is available");
                                            System.out
                                                    .println("No buddy upgrade option is available");
                                            Assert.assertTrue(false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void selectingFirstEarlyUpgradeLine() {
        try {
            firstEarlyUpgradeCheckbox.click();
        } catch (Exception e) {
            try {
                secondEarlyUpgradeCheckbox.click();
            } catch (Exception ee) {
                try {
                    thirdEarlyUpgradeCheckbox.click();
                } catch (Exception eee) {
                    try {
                        fourthEarlyUpgradeCheckbox.click();
                    } catch (Exception eeee) {
                        try {
                            fifthEarlyUpgradeCheckbox.click();
                        } catch (Exception eeeee) {
                            try {
                                sixthEarlyUpgradeCheckbox.click();
                            } catch (Exception eeeeee) {
                                try {
                                    seventhEarlyUpgradeCheckbox.click();
                                } catch (Exception eeeeeee) {
                                    try {
                                        eighthEarlyUpgradeCheckbox.click();
                                    } catch (Exception eeeeeeee) {
                                        try {
                                            ninthEarlyUpgradeCheckbox.click();
                                        } catch (Exception eeeeeeeee) {
                                            Log.error("No early upgrade option is available");
                                            System.out
                                                    .println("No early upgrade option is available");
                                            Assert.assertTrue(false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // endregion Methods
}