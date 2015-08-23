package framework;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

public class BrowserSettings extends TestListenerAdapter {
	public static StringBuilder log = new StringBuilder();
	public static WebDriver driver;
	public static ResStatus tcResult;
	public static long lStartTime;
	public static String pageName;

	public enum ResStatus {
		PASS, FAIL, WARNING
	}

	public enum CarrierType {
		ATT, SPRINT, VERIZON, NONE
	}

	public static void launchBrowser(String url) throws InterruptedException,
			IOException {
		String s = readConfig("BrowserType");
		if (s.equals("internetExplorer")) {
			System.setProperty("webdriver.ie.driver",
					"Drivers\\IEDriverServer.exe");

			DesiredCapabilities capability = DesiredCapabilities
					.internetExplorer();
			capability
					.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
			capability.setCapability(CapabilityType.SUPPORTS_ALERTS, false);
			capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
					UnexpectedAlertBehaviour.DISMISS);
			capability.setCapability(
					InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			capability
					.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);
			capability.setCapability(
					InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
			capability.setCapability(
					InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
			capability.setCapability("javascriptEnabled", true);
			capability.setCapability("applicationCacheEnabled", true);
			capability.setCapability("EnableNativeEvents", true);
			driver = new InternetExplorerDriver(capability);

		} else if (s.equals("firefox")) {
			ProfilesIni profile = new ProfilesIni();
			FirefoxProfile ffprofile = profile.getProfile("SeleniumProfile");
			ffprofile.setEnableNativeEvents(true);
			driver = new FirefoxDriver(ffprofile);
			driver.manage().window().maximize();

		} else if (s.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"Drivers\\Chrome\\32bit\\chromedriver.exe");

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability("chrome.verbose", false);
			driver = new ChromeDriver(capabilities);

		} else if (s.equals("safari")) {
			driver = new SafariDriver();

		} else if (s.equals("phantomjs")) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setJavascriptEnabled(true);
			caps.setCapability(
					PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					"Drivers\\phantomjs\\phantomjs.exe");
			driver = new PhantomJSDriver(caps);
		}
		driver.manage().timeouts()
				.pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
		driver.get(url);
		Reporter.log("Successfully launched the browser <br><p>");
	}

	public static String readConfig(String keyName) throws IOException {
		Properties configFile = new Properties();
		FileReader reader = new FileReader(
				"src/test/resources/TestSettings.properties");
		configFile.load(reader);
		return configFile.getProperty(keyName);
	}

	public static String readPageName(String keyName) throws IOException {
		Properties configFile = new Properties();
		FileReader reader = new FileReader(
				"src/test/resources/PageName.properties");
		configFile.load(reader);
		return configFile.getProperty(keyName);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getTestClass().getRealClass().getSimpleName();
		Utilities.driverTakesScreenshot(testName);
		// driver.quit();
	}
}