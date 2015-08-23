package framework;

import java.awt.AWTException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;

public class AdminBaseClass extends BaseClass {
	@BeforeSuite
	public void LaunchApplication() throws InterruptedException, AWTException,
			java.io.IOException {
		try {
			Reporter.log("Launch Admin <br><p>");
			launchBrowser(readConfig("urlAdmin"));
		} catch (Exception e) { // Setting UserName and Password by Sending Keys
								// using Robot class
//			PageBase.EnvironmentSetup().loginAdmin(readConfig("adminUserId"),
//					readConfig("adminPwd"));
		}
		driver.manage().timeouts()
				.pageLoadTimeout(300000, TimeUnit.MILLISECONDS);// Setting back
																// to default
																// Selenium
																// timeout
	}

	public void launchAdminInNewTab() throws InterruptedException,
			AWTException, java.io.IOException {
		try {
			driver.findElement(By.cssSelector("body")).sendKeys(
					Keys.CONTROL + "t");
			driver.manage().timeouts()
					.pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
			driver.get(readConfig("urlAdmin"));
			Reporter.log("Successfully launched the admin page in new tab <br><p>");
		} catch (Exception e) { // Setting UserName and Password by Sending Keys
								// using Robot class
//			PageBase.EnvironmentSetup().loginAdmin(readConfig("adminUserId"),
//					readConfig("adminPwd"));
		}
		driver.manage().timeouts()
				.pageLoadTimeout(300000, TimeUnit.MILLISECONDS);// Setting back
																// to default
																// Selenium
																// timeout
	}
}