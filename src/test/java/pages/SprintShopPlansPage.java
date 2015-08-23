package pages;

import framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by tarunk on 6/4/2015.
 */
public class SprintShopPlansPage {

    public SprintShopPlansPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }

    @FindBy(xpath = ControlLocators.KEEP_MY_EXISTING_SPRINT_PLAN_ADD_BUTTON)
    public WebElement keepmyExistingSprintPlanAddButton;

    @FindBy(xpath = ControlLocators.SPRINT_FAMILY_SHARE_1_GB_ADD_BUTTON)
    public WebElement sprintFamilySharePack1GBAddButton;

    @FindBy(xpath = ControlLocators.KEEP_MY_EXISTING_SPRINT_PLAN)
    public WebElement keepMyExistingSprintPlan;
}