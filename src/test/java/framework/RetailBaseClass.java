package framework;

import java.awt.AWTException;
import java.util.concurrent.TimeUnit;

import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public class RetailBaseClass extends BaseClass {
	@BeforeMethod
	public void LaunchApplication() throws InterruptedException, AWTException,
			java.io.IOException {
		try {
			Reporter.log("Launching Retail URL  <br> <p>");
			launchBrowser(readConfig("urlRetail"));
		} catch (Exception e) {
			Reporter.log(e.toString());
		}
		Utilities.waitForDocumentReady(driver);
		driver.manage().timeouts()
				.pageLoadTimeout(300000, TimeUnit.MILLISECONDS);// Setting back
																// to default
																// Selenium
																// timeout
	}
}
