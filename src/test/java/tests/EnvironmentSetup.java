package tests;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import framework.AdminBaseClass;
import framework.ControlLocators;
import framework.Utilities;

@Test
public class EnvironmentSetup extends AdminBaseClass {

	public static final double SCORE_FACTOR_DEFAULT = 19.00;
	public static final int SCORE_FACTOR_BRAND_DEFAULT = -100;
	public static int[] SCORE_FACTORS_TO_SET = { 34, 35 };
	Utilities utilities = new Utilities();

	@Test
	public void AdminBasicSettings() throws IOException {
		double scoreFactor34 = 0;
		double scoreFactor35 = 0;
		int scoreFactorNumber = 0;
		int brandScoreFactorNumber = 0;

		String window = driver.getWindowHandle();
		driver.switchTo().window(window);
		driver.manage().window().maximize();

		driver.navigate().to(
				readConfig("baseUrl") + ControlLocators.GRANT_PERMISSIONS_URL);
		WebElement element = utilities.findByXp(driver,
				ControlLocators.GRANT_ALL_PERMISSIONS_EXPR);
		if (!utilities.contains(element, "selected")) {
			element.click();
		}

		driver.navigate().to(
				readConfig("baseUrl") + ControlLocators.LIST_SCORE_FACTORS_URL);
		scoreFactor34 = Double.parseDouble(utilities.findByXp(driver,
				generateXPath_ScoreFactor(34)).getText());
		scoreFactor35 = Double.parseDouble(utilities.findByXp(driver,
				generateXPath_ScoreFactor(35)).getText());

		if (!(scoreFactor34 == SCORE_FACTOR_DEFAULT)) {
			scoreFactorNumber = 34;
			setScoreFactor(scoreFactorNumber, SCORE_FACTOR_DEFAULT);
		}
		if (!(scoreFactor35 == SCORE_FACTOR_DEFAULT)) {
			scoreFactorNumber = 35;
			setScoreFactor(scoreFactorNumber, SCORE_FACTOR_DEFAULT);
		}

		scoreFactor34 = Double.parseDouble(utilities.findByXp(driver,
				generateXPath_ScoreFactor(34)).getText());
		scoreFactor35 = Double.parseDouble(utilities.findByXp(driver,
				generateXPath_ScoreFactor(35)).getText());

		brandScoreFactorNumber = 731;
		setBrandScoreFactor(brandScoreFactorNumber);

		Assert.assertEquals(scoreFactor34, SCORE_FACTOR_DEFAULT,
				"Failed setting 34 Numbered Score Factor");
		Assert.assertEquals(scoreFactor35, SCORE_FACTOR_DEFAULT,
				"Failed setting 35 Numbered Score Factor");
	}

	public void setScoreFactor(int scoreFactorNumber, double scoreToBeSet)
			throws IOException {
		driver.navigate().to(
				readConfig("baseUrl")
						+ "/admin/scorefactor/editScoreFactor.php?sfId="
						+ scoreFactorNumber + "&storeId=");
		WebElement EditScoreFactorPoint = utilities.findByName(driver,
				ControlLocators.EDIT_SCORE_FACTOR_POINTS);
		setText(driver, EditScoreFactorPoint, scoreToBeSet);
		utilities
				.findListOfElements(driver, "name",
						ControlLocators.MODIFY_RESULT).get(0).click();
	}

	public void setBrandScoreFactor(int brandScoreFactorNumber)
			throws IOException {
		driver.navigate().to(
				readConfig("baseUrl") + ControlLocators.BRAND_SCORE_FACTOR_EXPR
						+ brandScoreFactorNumber);
		WebElement brandScoreFactor_EditPoint = utilities.findByName(driver,
				ControlLocators.BRAND_SCORE_FACTOR_POINTS);
		setText(driver, brandScoreFactor_EditPoint, SCORE_FACTOR_BRAND_DEFAULT);
		utilities.findByName(driver, ControlLocators.MODIFY_RESULT).click();
	}

	public void setText(WebDriver driver, WebElement element, double score) {
		element.clear();
		element.sendKeys(String.valueOf(score));
	}

	public String generateXPath_ScoreFactor(int scoreFactor) {
		String xPath = null;
		xPath = ControlLocators.SCORE_FACTOR_XPATH_EXPR.replaceFirst("sfId=",
				"sfId=" + scoreFactor);
		return xPath;
	}

	public void loginAdmin(String username, String password) {
		try {
			Thread.sleep(10000);
			Robot robot = new Robot();
			Utilities.copyPaste(username, robot);
			Utilities.sendKeys(KeyEvent.VK_TAB, robot);
			Utilities.copyPaste(password, robot);
			Utilities.sendKeys(KeyEvent.VK_TAB, robot);
			Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}