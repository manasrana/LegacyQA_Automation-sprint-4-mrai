package framework;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;

public class ShipAdminBaseClass extends BaseClass {
	@BeforeTest
	public void LaunchApplication() throws InterruptedException, AWTException,
			java.io.IOException {
		try {
			launchBrowser(readConfig("urlShipAdmin"));
		} catch (Exception e) { // Setting UserName and Password by Sending Keys
								// using Robot class
			Thread.sleep(130000);
			PageBase.EnvironmentSetup().loginAdmin(readConfig("adminUserId"),
					readConfig("adminPwd"));
		}
		driver.manage().timeouts()
				.pageLoadTimeout(300000, TimeUnit.MILLISECONDS);// Setting back
																// to default
																// Selenium
																// timeout
	}

	public static void launchShipAdminInNewTab() throws IOException {
		try {
			driver.findElement(By.cssSelector("body")).sendKeys(
					Keys.CONTROL + "t");
			driver.manage().timeouts()
					.pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
			driver.get(readConfig("urlShipAdmin"));
			Reporter.log("Successfully launched Shipadmin page in new tab <br> <p>");
		} catch (Exception e) { // Setting UserName and Password by Sending Keys
								// using Robot class
			PageBase.EnvironmentSetup().loginAdmin(readConfig("adminUserId"),
					readConfig("adminPwd"));
		}
		driver.manage().timeouts()
				.pageLoadTimeout(300000, TimeUnit.MILLISECONDS);// Setting back
																// to default
																// Selenium
																// timeout
	}
}
