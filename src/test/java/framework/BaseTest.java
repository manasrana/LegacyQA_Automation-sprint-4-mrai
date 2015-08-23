package framework;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.BeforeSuite;

public class BaseTest extends CommonFunction {

	@BeforeSuite
	public void AdminPageLogin() throws InterruptedException, AWTException,
			java.io.IOException {
		try {
			LaunchBrowser(readConfig("urlAdmin"));
			if (readConfig("BrowserType").equals("internetExplorer")) {
				Thread.sleep(30000);
				MimicKeyBoard();
			}
		} catch (Exception e) { // Setting UserName and Password by Sending Keys
								// using Robot class
			System.out.println(e.toString());
			MimicKeyBoard();
		}
		driver.manage().timeouts()
				.pageLoadTimeout(300000, TimeUnit.MILLISECONDS);// Increasing
																// the timeout
																// to Selenium
																// Default Value
	}

	public void MimicKeyBoard() throws InterruptedException, AWTException {
		Robot robot = new Robot();
		Utilities.copyPaste(readConfig("adminUserId"), robot);
		Utilities.sendKeys(KeyEvent.VK_TAB, robot);
		Utilities.copyPaste(readConfig("adminPwd"), robot);
		Utilities.sendKeys(KeyEvent.VK_TAB, robot);
		Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
	}
}