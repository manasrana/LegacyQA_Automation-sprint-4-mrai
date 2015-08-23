package framework;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;

public class InventoryManagementBaseClass extends BaseClass {

	public enum IMEIStatus {
		Sold, Available
	}

	private static Logger logFile = Logger.getLogger(Utilities.class.getName());

	@BeforeSuite
	public void LaunchApplication() throws InterruptedException, AWTException,
			java.io.IOException {
		launchBrowser(readConfig("urlInventory"));
		String window = driver.getWindowHandle();
		driver.switchTo().window(window);
		inventoryLogin();
	}

	public void launchInventoryInNewTab() throws InterruptedException,
			AWTException, java.io.IOException {
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
		driver.manage().timeouts()
				.pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
		driver.get(readConfig("urlInventory"));
		Reporter.log("Successfully launched the Inventory Management page in new tab <br><p>");
		inventoryLogin();
	}

	// region Inventory Login Page Methods
	public void inventoryLogin() throws IOException {
		Utilities.implicitWaitSleep(2000);
		boolean exists = driver.findElements(By.id("logout")).size() != 0;

		if (!exists) {
			Utilities
					.waitForElementVisible(PageBase.InventoryManagementPage().iframeInventory);
			driver.switchTo().frame(
					PageBase.InventoryManagementPage().iframeInventory);
			Utilities
					.waitForElementVisible(PageBase.InventoryManagementPage().userNameTextbox);
			PageBase.InventoryManagementPage().userNameTextbox.clear();
			PageBase.InventoryManagementPage().passwordTextbox.clear();
			PageBase.InventoryManagementPage().storeIDTextbox.clear();
			PageBase.InventoryManagementPage().userNameTextbox
					.sendKeys(readConfig("inventoryUserId"));
			PageBase.InventoryManagementPage().passwordTextbox
					.sendKeys(readConfig("inventoryPwd"));
			PageBase.InventoryManagementPage().storeIDTextbox
					.sendKeys(readConfig("inventoryStoreIdDetail"));
			PageBase.InventoryManagementPage().submitButton.click();
			driver.switchTo().defaultContent();
		}
		Utilities
				.waitForElementVisible(PageBase.InventoryManagementPage().productsLink);
	}

	// endregion Inventory Login Page Methods

	public void launchInventoryInNewTab(String storeID)
			throws InterruptedException, AWTException, java.io.IOException {
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");

		driver.get(readConfig("urlInventory"));

		Reporter.log("Successfully launched the Inventory Management page in new tab <br><p>");
		inventoryLogin(storeID);
	}

	public void inventoryLogin(String storeID) throws IOException {
		Utilities.implicitWaitSleep(2000);
		boolean exists = driver.findElements(By.id("logout")).size() != 0;

		if (!exists) {
			driver.switchTo().frame(
					PageBase.InventoryManagementPage().iframeInventory);
			Utilities
					.waitForElementVisible(PageBase.InventoryManagementPage().userNameTextbox);
			PageBase.InventoryManagementPage().userNameTextbox.clear();
			PageBase.InventoryManagementPage().passwordTextbox.clear();
			PageBase.InventoryManagementPage().storeIDTextbox.clear();
			PageBase.InventoryManagementPage().userNameTextbox
					.sendKeys(readConfig("inventoryUserId"));
			PageBase.InventoryManagementPage().passwordTextbox
					.sendKeys(readConfig("inventoryPwd"));
			PageBase.InventoryManagementPage().storeIDTextbox.sendKeys(storeID);
			PageBase.InventoryManagementPage().submitButton.click();
			driver.switchTo().defaultContent();
		}
		Utilities.waitForElementVisible(PageBase.InventoryManagementPage().productsLink);
	}
}

	// endregion}