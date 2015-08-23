package framework;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

public class CommonFunction {
    public static String filePath, tcName, verificationErrors;
    public static StringBuilder log = new StringBuilder();
    public static WebDriver driver;
    public static String BrowserName = "Firefox";
    public static ResStatus tcResult;

    public enum ResStatus {
        PASS, FAIL, WARNING
    }

    public enum CarrierType {
        ATT, SPRINT, VERIZON, NONE
    }

    public void log() {
        try {
            String path = "logs.txt";
            // creating file object from given path
            File file = new File(path);
            // FileWriter second argument is for append if its true than
            // FileWritter will
            // write bytes at the end of File (append) rather than beginning of
            // file
            FileWriter fileWriter = new FileWriter(file, true);
            // Use BufferedWriter instead of FileWriter for better performance
            BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
            fileWriter.append(System.getProperty("line.separator"));
            // fileWriter.append("This text should be appended in; File form Java Program--new content\n");
            fileWriter.append(log.toString());
            // Don't forget to close Streams or Reader to free FileDescriptor
            // associated with it
            bufferFileWriter.close();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void LaunchBrowser(String url) throws InterruptedException {

        switch (readConfig("BrowserType")) {
            case "internetExplorer":
                System.setProperty("webdriver.ie.driver",
                        "Drivers\\IE\\32bit\\IEDriverServer.exe");
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
                break;
            case "firefox":
                ProfilesIni profile = new ProfilesIni();
                FirefoxProfile ffprofile = profile.getProfile("SeleniumProfile");
                ffprofile.setEnableNativeEvents(true);
                driver = new FirefoxDriver(ffprofile);
                driver.manage().window().maximize();
                break;

            case "chrome":
                System.setProperty("webdriver.chrome.driver",
                        "Drivers\\Chrome\\32bit\\chromedriver.exe");
                DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                capabilities.setCapability("chrome.verbose", false);
                driver = new ChromeDriver(capabilities);
                break;

            case "safari":
                driver = new SafariDriver();
                break;

            case "phantomjs":
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setJavascriptEnabled(true);
                caps.setCapability(
                        PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                        "Drivers\\phantomjs\\phantomjs.exe");
                driver = new PhantomJSDriver(caps);
        }
        driver.manage().timeouts().pageLoadTimeout(5000, TimeUnit.MILLISECONDS);
        driver.get(url);
        log.append("Successfully launched the browser <br>\n");
    }

    public static String readConfig(String keyName) {
        try {
            Properties configFile = new Properties();
            FileReader reader = new FileReader("TestSettings.properties");
            configFile.load(reader);
            return configFile.getProperty(keyName);
        } catch (Exception e) {
            System.out.print(e.toString());
            return "";
        }
    }

    // This function will be used to get formatted phone number as string.
    // Author: Tarun
    public static String getFormattedPhoneNumber(String phoneNumber) {
        // (857) 474-8521
        String s1 = phoneNumber.substring(0, 3);
        String s2 = phoneNumber.substring(3, 6);
        String s3 = phoneNumber.substring(6, 10);
        String formattedPhoneNumber = "(" + s1 + ")" + " " + s2 + "-" + s3;
        return formattedPhoneNumber;
    }

    // This function will be used to get unformatted phone number as string from
    // formatted phone number.
    // Author: Tarun
    public static String getUnFormattedPhoneNumber(String formattedPhoneNumber) {
        // (857) 474-8521
        String s1 = formattedPhoneNumber.substring(1, 4);
        String s2 = formattedPhoneNumber.substring(6, 9);
        String s3 = formattedPhoneNumber.substring(10, 14);
        String unFormattedPhoneNumber = s1 + s2 + s3;
        return unFormattedPhoneNumber;
    }

    // This function will be used to get current date in MM/DD/YYYY format
    // Author: Tarun
    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    // This function will to get current date in MM/DD/YYYY format
    // Pass cutting length 16 if plan cost is 2 digit ex. $80.00 /mo
    // Pass cutting length 17 if plan cost is 3 digit ex. $800.00 /mo
    // Pass cutting length 15 if plan cost is 1 digit ex. $8.00 /mo
    // Author: Tarun
    public static String getPlan(WebElement webElement, int cuttingLength) {
        String plan = webElement.getText();
        int length = plan.length();
        plan = plan.substring(9, length - cuttingLength);
        return plan;
    }

    public static String getUniqueNumber(String number) {
        long num = Long.parseLong(number);
        num = num - 1000000000;
        String numStr = String.valueOf(num);
        return numStr;
    }

    public static void clickingNTimesTab(int n) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        for (int i = 1; i <= n ; i++) {
            Utilities.sendKeys(KeyEvent.VK_TAB, robot);
        }
    }
    public static String GenerateRandomNumber(int number){
        String generatedRandomNumber = String.valueOf((long) (Math.floor(Math.random() * 9000000000L) + 6000000000L));
        generatedRandomNumber = generatedRandomNumber.substring(0, number);
        return generatedRandomNumber;
    }
}
