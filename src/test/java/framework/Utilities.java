package framework;

import org.apache.log4j.Logger;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.awt.datatransfer.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Utilities extends BrowserSettings {

    public enum SelectDropMethod {
        SELECTBYTEXT, SELECTBYVALUE, SELECTBYINDEX
    }

    static String fileName;
    private static Logger l = Logger.getLogger(Utilities.class.getName());

    public void PoaClose(WebDriver Utilities) throws InterruptedException {
        try {
            switch (readConfig("BrowserType")) {
                case "1":
                    Runtime.getRuntime().exec("TASKKILL /F /IM firefox.exe");
                    break;
                case "0":
                    Runtime.getRuntime().exec("TASKKILL /F /IM iexplore.exe");
                    break;
                case "2":
                    Runtime.getRuntime().exec("TASKKILL /F /IM chrome.exe");
                    break;
                case "3":
                    Runtime.getRuntime().exec("TASKKILL /F /IM safari.exe");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToWindowUsingTitle(String Title) {
        try {
            org.openqa.selenium.Dimension maximizeDim;
            Set<String> windowTitle = driver.getWindowHandles();
            Reporter.log("Number of browsers found  <br> <p>"
                    + windowTitle.size());
            Reporter.log("Primary browser name: " + driver.getTitle()
                    + "<br><p>");
            Iterator<String> it = windowTitle.iterator();
            String test[] = new String[3];
            int i = 0;
            while (it.hasNext()) {
                test[i] = it.next();
                i++;
            }

            driver.switchTo().window(test[i - 1]);
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            Reporter.log("Switched to browser name: " + driver.getTitle()
                    + "<br><p>");
            driver.manage().window().maximize();

            for (int iCount = 0; iCount < 15; iCount++) {
                Thread.sleep(2000);
                maximizeDim = driver.manage().window().getSize();
                if (maximizeDim.height < 1000) {
                    driver.manage().window().maximize();
                }
                driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            }
            Reporter.log("Maximized the game window <br>\n");
        } catch (Exception e) {
            Reporter.log("Exception occured while Maximizing the browser: "
                    + e.toString() + " <br><p>");
            tcResult = ResStatus.FAIL;
            Assert.fail();
        }
    }

    public static void driverTakesScreenshot(String testName) {
        Date cur_dt = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String strTimeStamp = dateFormat.format(cur_dt);
        try {
            createFolder("src/test/resources/Screenshots");
            File scrFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("Screenshots/" + testName
                    + "_" + strTimeStamp + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createFolder(String S) {
        File file = new File("C:/" + S);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean contains(WebElement element, String tag) {
        return element.getClass().toString().contains(tag);
    }

    public boolean equals(WebElement element, String tag) {
        return element.getText().toString().equals(tag);
    }

    public WebElement findByXp(WebDriver driver, String xp) {
        return driver.findElement(By.xpath(xp));
    }

    public WebElement findByName(WebDriver driver, String name) {
        return driver.findElement(By.name(name));
    }

    public WebElement findByLink(WebDriver driver, String link) {
        return driver.findElement(By.linkText(link));
    }

    public WebElement findByClassName(WebDriver driver, String className) {
        return driver.findElement(By.className(className));
    }

    public List<WebElement> findListOfElements(WebDriver driver, String by,
                                               String value) {
        List<WebElement> Elements = null;
        if (by.equals("xpath")) {
            Elements = driver.findElements(By.xpath(value));
        }
        if (by.equals("id")) {
            Elements = driver.findElements(By.id(value));
        } else if (by.equals("name")) {
            Elements = driver.findElements(By.name(value));
        } else if (by.equals("linkText")) {
            Elements = driver.findElements(By.linkText(value));
        } else if (by.equals("className")) {
            Elements = driver.findElements(By.className(value));
        }

        if (Elements == null) {
            System.out.println("Failed To Find Element" + by + ":" + value);
        }
        return Elements;
    }

    /* Copy and Paste a string using Robot object */
    public static void copyPaste(String str, Robot robot)
            throws InterruptedException {

        Thread.sleep(1000);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        StringSelection stringSelection = new StringSelection(str);
        clipboard.setContents(stringSelection, null);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    /* Key Press and Release action */
    public static void sendKeys(int key, Robot robot) throws AWTException,
            InterruptedException {
        Thread.sleep(1000);
        robot.keyPress(key);
        robot.keyRelease(key);
    }

    public static void setValue(WebElement element, String valueToSet)
            throws AWTException, InterruptedException {
        element.click();
        element.clear();
        element.sendKeys(valueToSet);
    }

    // / <summary>
    // / This function will used for Implicit Wait by Sleep for given
    // milliseconds.
    // / </summary>
    // / <param name="intervalInMilliseconds">Pass the number of milliseconds to
    // sleep</param>
    public static void implicitWaitSleep(int intervalInMilliseconds) {
        try {
            Thread.sleep(intervalInMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void waitForElementVisible(WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public static void waitForElementVisible(WebElement webElement, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public static void WaitForNextPage() {
        boolean isCurrent = true;
        String currentUrl = driver.getCurrentUrl();
        while (true) {
            Utilities.implicitWaitSleep(5000);
            if (!(currentUrl.equalsIgnoreCase(driver.getCurrentUrl()))) {
                break;
            }
        }
        Utilities.implicitWaitSleep(5000);
    }

    public static String getCredentials(String keyName) {
        try {
            File objFile = null;
            FileReader reader = null;
            String testType = readConfig("test-type");
            Properties configFile = new Properties();

            if (testType.equals("internal")) {
                reader = new FileReader(
                        "src/test/resources/TestData-Internal/CredentialsRetail.properties");
            } else {
                reader = new FileReader(
                        "src/test/resources/TestData-External/CredentialsRetail.properties");
            }
            configFile.load(reader);
            return configFile.getProperty(keyName);
        } catch (Exception e) {
            System.out.print(e.toString());
            return "";
        }
    }

    public static void waitForDocumentReady(WebDriver driver) {
        StopWatch sw = new StopWatch();
        sw.start();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            do {
                System.out.println(js.executeScript(
                        "return document.readyState").toString());
                if (sw.getTime() > 180000)
                    break;
            } while (js.executeScript("return document.readyState").toString() == "complete");

        } catch (Exception e) {
            System.out.println("Failed to check document ready" + e.toString());
        }
    }

    public static void webPageLoadTime(long lStartTime, String pageName) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("$(document).ready(function(){return true;});");
        long lEndTime = new Date().getTime();
        long totalTime = (lEndTime - lStartTime) / 1000;
        String str = "Total Time for " + pageName
                + " page to load in seconds - " + totalTime;
        Reporter.log("<br>" + str);
    }

    // / <summary>
    // / This function will be used to select value in dropdown.
    // / </summary>
    // / Author:Tarun
    // / <param name="element">Pass the Dropdown element.</param>
    // / <param name="selectedMethod">Select the dropdown selection
    // method</param>
    // / <param name="valueToSelect">Enter value or text or index to be selected
    // in dopdown.</param>
    public static void dropdownSelect(WebElement element,
                                      SelectDropMethod selectedMethod, String valueToSelect) {
        Select select = new Select(element);
        switch (selectedMethod) {
            case SELECTBYTEXT:
                select.selectByVisibleText(valueToSelect);
                break;
            case SELECTBYVALUE:
                select.selectByValue(valueToSelect);
                break;
            case SELECTBYINDEX:
                int index = Integer.parseInt(valueToSelect);
                select.selectByIndex(index);
                break;
        }
    }

    public static void switchPreviousTab() {
        driver.findElement(By.cssSelector("body"))
                .sendKeys(Keys.CONTROL + "\t");
        driver.switchTo().defaultContent();
    }

    public static void switchNextTab() {
        driver.findElement(By.cssSelector("body"))
                .sendKeys(Keys.CONTROL, Keys.TAB);
    }

    public static void ClickElementByJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public static void RefreshPage() {
        driver.navigate().refresh();
    }

    public static void ClickElement(WebElement element) {
        Utilities.waitForElementVisible(element);
        element.click();
    }

    // / <summary>
    // / This method will used to check particular element is present or not.
    // / </summary>
    // / <param name="iwebelement">Pass the element to which we need to
    // check</param>
    // / <returns>Returns true if element is not present.</returns>
    public static boolean ElementNotPresent(WebElement webelement) {
        try {
            return !webelement.isDisplayed();
        } catch (NoSuchElementException e) {
            return true;
        }

    }

    public static void WCASignature() {
        Actions builder = new Actions(driver);
        Action drawAction = builder
                .moveToElement(
                        PageBase.WirelessCustomerAgreementPage().signatureTextbox,
                        100, 50)
                        // signatureWebElement is the element that holds the signature
                        // element you have in the DOM
                .clickAndHold().moveByOffset(6, 7).moveByOffset(-15, 15)
                .release().build();
        drawAction.perform();
    }

    // Checking whether check box is checked or not.
    public static void checkingChkbox(WebElement chkbx1) {
        boolean checkstatus;
        checkstatus = chkbx1.isSelected();
        if (checkstatus == false) {
            chkbx1.click();
        }
    }

    // Unchecking check boxes.
    public static void unCheckingChkbox(WebElement chkbx1) {
        boolean checkstatus;
        checkstatus = chkbx1.isSelected();
        if (checkstatus == true) {
            chkbx1.click();
            System.out.println("Checkbox is unchecked");
        } else {
            System.out.println("Checkbox is already unchecked");
        }
    }

    public static void ScrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", element);
    }

    public static DependantTestCaseInputs ReadFromCSV(String TCName)
            throws IOException {
        DependantTestCaseInputs dependantValues = new DependantTestCaseInputs();
        String fileName = null;
        String line = null;
        int flg = 0;
        String[] values = null;
        String testType = readConfig("test-type");

        if (testType.equals("internal")) {
            fileName = "src//test//resources//TestData-Internal//DependentTestCaseDetails.csv";
        } else {
            fileName = "src//test//resources//TestData-External//DependentTestCaseDetails.csv";
        }
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        while ((line = br.readLine()) != null) {
            values = line.split(",");
            if (values[0].equals(TCName)) {
                dependantValues.TC_ID = values[0];
                dependantValues.ORDER_ID = values[1];
                dependantValues.ESN_IMEI1 = values[2];
                dependantValues.ESN_IMEI2 = values[3];
                dependantValues.ESN_IMEI3 = values[4];
                dependantValues.FIRST_NAME = values[5];
                dependantValues.LAST_NAME = values[6];
                dependantValues.EMAIL = values[7];
                dependantValues.RECEIPT_ID = values[8];
                dependantValues.ID_TYPE = values[9];
                dependantValues.STATE = values[10];
                dependantValues.ID_NUMBER = values[11];
                dependantValues.PhNumber = values[12];
                dependantValues.Zip = values[13];
                dependantValues.SSN = values[14];
                dependantValues.Month = values[15];
                dependantValues.Year = values[16];
                break;
            }
            if (flg > 0) {
                for (String str : values) {
                    System.out.println(str);
                }
            }
            flg++;
        }
        br.close();
        return dependantValues;
    }

    public static void xmlParsingChangeValueBasedOnKey(String fileName,
                                                       String key, String value1, @Optional String value2,
                                                       @Optional String value3, @Optional String value4,
                                                       @Optional String value5, @Optional int loop)
            throws ParserConfigurationException, SAXException, IOException,
            TransformerException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        String filePath = "TestData-Internal//" + fileName + ".xml";
        Document doc = docBuilder.parse(new File(filePath));

        try {

            NodeList nodes1 = doc.getElementsByTagName(key);
            for (int c = 0; c < loop; c++) {
                if (c == 0)
                    nodes1.item(c).setTextContent(value1);
                if (c == 1)
                    nodes1.item(c).setTextContent(value2);
                if (c == 2)
                    nodes1.item(c).setTextContent(value3);
                if (c == 3)
                    nodes1.item(c).setTextContent(value4);
                if (c == 4)
                    nodes1.item(c).setTextContent(value5);
            }
        } catch (Exception e) {
        } finally {
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(filePath);
            transformer.transform(source, result);
        }
    }

    public static void WaitUntilElementIsClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void isCheck(WebElement element) {
        Utilities.implicitWaitSleep(5000);
        if (!element.isSelected()) {
            element.click();
        }
    }
}