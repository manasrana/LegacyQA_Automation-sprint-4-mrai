package pages;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import framework.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import framework.Utilities.SelectDropMethod;

public class APIHistoryViewPage extends RetailBaseClass{

    public APIHistoryViewPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }

    @FindBy(id = ControlLocators.ORDER_ID_TEXTBOX)
    public WebElement orderIdTextbox;

    @FindBy(xpath = ControlLocators.ENTER_BUTTON)
    public WebElement enterButton;

    @FindBy(xpath = ControlLocators.API_HISTORY_TABLE)
    public WebElement apiHistoryTable;

    public void getAPIHistoryTableByOrderId(String orderId)
    {
        Utilities.waitForElementVisible(PageBase.APIHistoryViewPage().orderIdTextbox);
        PageBase.APIHistoryViewPage().orderIdTextbox.sendKeys(orderId);
        PageBase.APIHistoryViewPage().enterButton.click();
        Utilities.waitForElementVisible(PageBase.APIHistoryViewPage().apiHistoryTable);
    }

    public void launchAPIHistoryViewInNewTab() throws IOException {
        try {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
            driver.manage().timeouts().pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
            driver.get(readConfig("urlAPIHistoryView"));
            log.append("Successfully launched API History View page in new tab <br>\n");
        }
        catch(Exception e){	//Setting UserName and Password by Sending Keys using Robot class
            PageBase.EnvironmentSetup().loginAdmin(readConfig("adminUserId"), readConfig("adminPwd"));
        }
        driver.manage().timeouts().pageLoadTimeout(300000, TimeUnit.MILLISECONDS);//Setting back to default Selenium timeout
    }
}
