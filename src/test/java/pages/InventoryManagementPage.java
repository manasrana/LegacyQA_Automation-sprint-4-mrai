package pages;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import framework.ControlLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import framework.InventoryManagementBaseClass;
import framework.PageBase;
import framework.Utilities;
import framework.Utilities.SelectDropMethod;
import org.testng.Reporter;

public class InventoryManagementPage extends InventoryManagementBaseClass {
    public InventoryManagementPage(WebDriver d) {
        PageFactory.initElements(d, this);
    }

    @FindBy(id = ControlLocators.SERIALIZED_PRODUCTS)
    public WebElement serializedProducts;

    @FindBy(id = ControlLocators.USERNAME_INVENTORY_TEXTBOX)
    public WebElement userNameTextbox;

    @FindBy(id = ControlLocators.PASSWORD__INVENTORY_TEXTBOX)
    public WebElement passwordTextbox;

    @FindBy(id = ControlLocators.STORE_INVENTORY_TEXTBOX)
    public WebElement storeIDTextbox;

    @FindBy(xpath = ControlLocators.STORE_INVENTORY_SELECT)
    public WebElement storeSelect;

    @FindBy(xpath = ControlLocators.PRODUCTS_INVENTORY_LINK)
    public WebElement productsLink;

    @FindBy(id = ControlLocators.IMEI_INVENTORY_TEXTBOX)
    public WebElement imeiTextbox;

    @FindBy(id = ControlLocators.SUBMIT_INVENTORY)
    public WebElement submitButton;

    @FindBy(id = ControlLocators.IFRAME_INVENTORY)
    public WebElement iframeInventory;

    @FindBy(xpath = ControlLocators.SERIALIZEDSTATUS_INVENTORY)
    public WebElement statusInventory;

    @FindBy(xpath = ControlLocators.PURCHASING_INVENTORY_LINK)
    public WebElement purchasingLink;

    @FindBy(xpath = ControlLocators.RECEIVING)
    public WebElement receivingLink;

    @FindBy(id = ControlLocators.RECEIVE_NEW_SHIPMENT)
    public WebElement receiveNewShipmentButton;

    @FindBy(id = ControlLocators.VENDOR_DROPDOWN)
    public WebElement vendorDropdown;

    @FindBy(id = ControlLocators.PRODUCT_TEXTBOX)
    public WebElement productTextbox;

    @FindBy(id = ControlLocators.ADD_PRODUCT_BUTTON)
    public WebElement addProductButton;

    @FindBy(id = ControlLocators.SCAN_CODE_TEXTBOX)
    public WebElement scanCodeTextbox;

    @FindBy(id = ControlLocators.ADD_TO_INVENTORY)
    public WebElement addToInventory;

    @FindBy(id = ControlLocators.PRODUCT_AUTOCOMPLETE_TEXTBOX)
    public WebElement productAutocompleteTextbox;

    // region Inventory Login Page Methods
    public void verifyDeviceStatus(String imei1, String imei2, String imei3,
                                   String status) {
        productsLink.click();
        serializedProducts.click();
        driver.switchTo().frame(iframeInventory);
        Utilities.WaitUntilElementIsClickable(imeiTextbox);
        imeiTextbox.click();
        imeiTextbox.sendKeys(imei1);
        submitButton.click();
        Utilities.WaitUntilElementIsClickable(statusInventory);
        Assert.assertTrue(statusInventory.getText().contains(status));
        Utilities.WaitUntilElementIsClickable(imeiTextbox);
        imeiTextbox.click();
        imeiTextbox.clear();
        imeiTextbox.sendKeys(imei2);
        submitButton.click();
        Utilities.WaitUntilElementIsClickable(statusInventory);
        Assert.assertTrue(statusInventory.getText().contains(status));
        Utilities.WaitUntilElementIsClickable(imeiTextbox);
        imeiTextbox.click();
        imeiTextbox.clear();
        imeiTextbox.sendKeys(imei3);
        submitButton.click();
        Utilities.WaitUntilElementIsClickable(statusInventory);
        Assert.assertTrue(statusInventory.getText().contains(status));
        driver.switchTo().defaultContent();
    }

    public void verifyDeviceStatus(String iMEI1, String iMEI2, String status) {
        try {
            Utilities
                    .waitForElementVisible(productsLink);
            Utilities.WaitUntilElementIsClickable(productsLink);
            productsLink.click();
            Utilities
                    .waitForElementVisible(serializedProducts);
            Utilities.WaitUntilElementIsClickable(serializedProducts);
            serializedProducts.click();
            driver.switchTo().frame(iframeInventory);
            Utilities.WaitUntilElementIsClickable(imeiTextbox);
            imeiTextbox.click();
            PageBase.InventoryManagementPage().imeiTextbox.sendKeys(iMEI1);
            PageBase.InventoryManagementPage().submitButton.click();
            Utilities.implicitWaitSleep(4000);
            Utilities.WaitUntilElementIsClickable(imeiTextbox);
            imeiTextbox.click();
            PageBase.InventoryManagementPage().imeiTextbox.clear();
            PageBase.InventoryManagementPage().imeiTextbox.sendKeys(iMEI2);
            PageBase.InventoryManagementPage().submitButton.click();
            Utilities.implicitWaitSleep(4000);
            // Assert.assertTrue(PageBase.InventoryManagementPage().statusInventory.getText().contains(status),
            // "Status of IMEI2 "+iMEI2+" in the POS is not as expected. Expected is "+status);
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            Reporter.log(e.toString());
        }
    }

    public void verifyDeviceStatus(String iMEI, String status) {
        try {
            Utilities
                    .waitForElementVisible(productsLink);
            Utilities.WaitUntilElementIsClickable(productsLink);
            productsLink.click();
            Utilities
                    .waitForElementVisible(serializedProducts);
            Utilities.WaitUntilElementIsClickable(serializedProducts);
            serializedProducts.click();
            driver.switchTo().frame(iframeInventory);
            Utilities
                    .waitForElementVisible(PageBase.InventoryManagementPage().imeiTextbox);
            Utilities.WaitUntilElementIsClickable(imeiTextbox);
            imeiTextbox.click();
            imeiTextbox.sendKeys(iMEI);
            submitButton.click();
            Utilities.implicitWaitSleep(3000);
            Utilities.waitForElementVisible(statusInventory);
            Utilities.implicitWaitSleep(4000);
            Assert.assertTrue(statusInventory.getText().contains(status));
            driver.switchTo().defaultContent();
        } catch (Exception ex) {
            Reporter.log(ex.toString());
        }
    }

    public void addDeviceToInventory(String iemi1, String iemi2,
                                     String product1, String product2) throws InterruptedException,
            AWTException, java.io.IOException {
        try {
            Utilities.WaitUntilElementIsClickable(purchasingLink);
            purchasingLink.click();
            Utilities.waitForElementVisible(receivingLink);
            Utilities.WaitUntilElementIsClickable(receivingLink);
            receivingLink.click();
            Utilities.implicitWaitSleep(3000);
            driver.switchTo().frame(iframeInventory);

            // Add first device into inventory
            recieveNewShipment(iemi1, product1);

            //Adding second device
            recieveNewShipment(iemi2, product2);

            //Verifying IMEI added or not.
            Utilities.implicitWaitSleep(3000);
            switchingBetweenSerializedAndFrame();

            if (!deviceStatus(iemi1)) {
                recieveNewShipment(iemi1, product1);
            }

            Utilities.implicitWaitSleep(3000);
            if (!deviceStatus(iemi2)) {
                recieveNewShipment(iemi2, product2);
            }

           /* // Adding second device into inventory.
            Utilities.implicitWaitSleep(4000);
            driver.switchTo().defaultContent();
            Utilities.implicitWaitSleep(4000);
            //Switching into Recieve new shipment link.
            Utilities.waitForElementVisible(purchasingLink);
            Utilities.WaitUntilElementIsClickable(purchasingLink);
            purchasingLink.click();
            Utilities.waitForElementVisible(receivingLink);
            Utilities.WaitUntilElementIsClickable(receivingLink);
            receivingLink.click();
            Utilities.implicitWaitSleep(4000);
            driver.switchTo().frame(iframeInventory);*/
            //  Utilities.implicitWaitSleep(4000);

            //recieveNewShipment(iemi2, product2);

            //Verifying IMEI added or not.
            /*if (!deviceStatus(iemi2)) {
                recieveNewShipment(iemi2, product2);
            }*/

            driver.switchTo().defaultContent();
            Utilities.implicitWaitSleep(5000);
            Utilities.WaitUntilElementIsClickable(productsLink);
            Utilities.WaitUntilElementIsClickable(driver.findElement(By
                    .id("logout")));
            driver.findElement(By.id("logout")).click();
        } catch (Exception e) {
            e.printStackTrace();
            Reporter.log(e.toString());
        }
    }

    private void recieveNewShipment(String iemi1, String Product1) {
        Utilities.waitForElementVisible(receiveNewShipmentButton);
        Utilities.WaitUntilElementIsClickable(receiveNewShipmentButton);
        receiveNewShipmentButton.click();
        Utilities.waitForElementVisible(vendorDropdown);
        Utilities.WaitUntilElementIsClickable(vendorDropdown);
        Utilities.dropdownSelect(vendorDropdown, SelectDropMethod.SELECTBYTEXT,
                "BSupplier");
        productTextbox.sendKeys(Product1);
        Utilities.implicitWaitSleep(3000);
        productTextbox.sendKeys(Keys.ARROW_DOWN);
        productTextbox.sendKeys(Keys.RETURN);
        Utilities.implicitWaitSleep(4000);
        Utilities.waitForElementVisible(addProductButton);
        Utilities.WaitUntilElementIsClickable(addProductButton);
        addProductButton.click();
        Utilities.waitForElementVisible(scanCodeTextbox);
        Utilities.WaitUntilElementIsClickable(scanCodeTextbox);
        scanCodeTextbox.sendKeys(iemi1);
        Utilities.implicitWaitSleep(3000);
        scanCodeTextbox.sendKeys(Keys.RETURN);
        Utilities.implicitWaitSleep(4000);
        Utilities.WaitUntilElementIsClickable(addToInventory);
        addToInventory.click();
        Utilities.implicitWaitSleep(7000);
        driver.switchTo().alert().accept();
        Utilities.implicitWaitSleep(7000);
    }

    public void addDeviceToInventory(String iMEI, String Product)
            throws InterruptedException, AWTException, java.io.IOException {
        try {
            Utilities.WaitUntilElementIsClickable(purchasingLink);
            purchasingLink.click();
            Utilities.WaitUntilElementIsClickable(receivingLink);
            receivingLink.click();
            Utilities.implicitWaitSleep(3000);
            driver.switchTo().frame(iframeInventory);
            Utilities.WaitUntilElementIsClickable(receiveNewShipmentButton);
            receiveNewShipmentButton.click();
            Utilities.waitForElementVisible(vendorDropdown);
            Utilities.WaitUntilElementIsClickable(vendorDropdown);
            Utilities.dropdownSelect(vendorDropdown,
                    SelectDropMethod.SELECTBYTEXT, "BSupplier");
            productTextbox.sendKeys(Product);
            Utilities.implicitWaitSleep(2000);
            productTextbox.sendKeys(Keys.ARROW_DOWN);
            productTextbox.sendKeys(Keys.RETURN);
            Utilities.implicitWaitSleep(2000);
            Utilities.waitForElementVisible(addProductButton);
            Utilities.WaitUntilElementIsClickable(addProductButton);
            addProductButton.click();
            Utilities.waitForElementVisible(scanCodeTextbox);
            Utilities.WaitUntilElementIsClickable(scanCodeTextbox);
            scanCodeTextbox.sendKeys(iMEI);
            Utilities.implicitWaitSleep(2000);
            scanCodeTextbox.sendKeys(Keys.RETURN);
            Utilities.implicitWaitSleep(4000);
            Utilities.WaitUntilElementIsClickable(addToInventory);
            addToInventory.click();
            Utilities.implicitWaitSleep(8000);
            driver.switchTo().alert().accept();
            Utilities.implicitWaitSleep(8000);
            driver.switchTo().defaultContent();
            Utilities.implicitWaitSleep(5000);

            Utilities.WaitUntilElementIsClickable(productsLink);
            Utilities.WaitUntilElementIsClickable(driver.findElement(By
                    .id("logout")));
            driver.findElement(By.id("logout")).click();

        } catch (Exception e) {
            // ?TODO Auto-generated catch block
            e.printStackTrace();
            Reporter.log(e.toString());
        }
    }

    public void closeInventoryTab() throws InterruptedException, AWTException,
            java.io.IOException {
        try {
            Utilities.implicitWaitSleep(5000);
            driver.findElement(By.cssSelector("body")).sendKeys(
                    Keys.CONTROL + "w");
            driver.switchTo().defaultContent();
        } catch (Exception e) {

            Reporter.log(e.toString());
        }
    }

    public boolean deviceStatus(String imei) {
        boolean status = false;
        try {
            Utilities.waitForElementVisible(PageBase.InventoryManagementPage().imeiTextbox);
            Utilities.WaitUntilElementIsClickable(imeiTextbox);
            imeiTextbox.click();
            imeiTextbox.sendKeys(imei);
            submitButton.click();
            Utilities.implicitWaitSleep(3000);
            Utilities.waitForElementVisible(statusInventory);
            Utilities.implicitWaitSleep(4000);

            if (statusInventory.getText().contains("Available")) {
                status = true;
            }

        } catch (Exception ex) {
            Reporter.log(ex.toString());
        }
        return status;
    }

    private void switchingBetweenSerializedAndFrame() {
        driver.switchTo().defaultContent();
        Utilities.implicitWaitSleep(3000);
        productsLink.click();
        Utilities.WaitUntilElementIsClickable(serializedProducts);
        serializedProducts.click();
        driver.switchTo().frame(iframeInventory);
    }

    public void addDeviceToInventory(String iemi1, String iemi2, String iemi3, String product1, String product2,
                                       String product3) throws InterruptedException, AWTException, java.io.IOException {
        try {
            Utilities.WaitUntilElementIsClickable(purchasingLink);
            purchasingLink.click();
            Utilities.waitForElementVisible(receivingLink);
            Utilities.WaitUntilElementIsClickable(receivingLink);
            receivingLink.click();
            Utilities.implicitWaitSleep(3000);
            driver.switchTo().frame(iframeInventory);

            // Add first device into inventory
            recieveNewShipment(iemi1, product1);

            //Adding second device
            recieveNewShipment(iemi2, product2);

            //Adding third device
            recieveNewShipment(iemi3, product3);

            //Verifying IMEI added or not.
            Utilities.implicitWaitSleep(3000);
            switchingBetweenSerializedAndFrame();

            if (!deviceStatus(iemi1)) {
                recieveNewShipment(iemi1, product1);
            }

            Utilities.implicitWaitSleep(3000);
            if (!deviceStatus(iemi2)) {
                recieveNewShipment(iemi2, product2);
            }

            Utilities.implicitWaitSleep(3000);
            if (!deviceStatus(iemi3)) {
                recieveNewShipment(iemi3, product3);
            }

           /* // Adding second device into inventory.
            Utilities.implicitWaitSleep(4000);
            driver.switchTo().defaultContent();
            Utilities.implicitWaitSleep(4000);
            //Switching into Recieve new shipment link.
            Utilities.waitForElementVisible(purchasingLink);
            Utilities.WaitUntilElementIsClickable(purchasingLink);
            purchasingLink.click();
            Utilities.waitForElementVisible(receivingLink);
            Utilities.WaitUntilElementIsClickable(receivingLink);
            receivingLink.click();
            Utilities.implicitWaitSleep(4000);
            driver.switchTo().frame(iframeInventory);*/
            //  Utilities.implicitWaitSleep(4000);

            //recieveNewShipment(iemi2, product2);

            //Verifying IMEI added or not.
            /*if (!deviceStatus(iemi2)) {
                recieveNewShipment(iemi2, product2);
            }*/

            driver.switchTo().defaultContent();
            Utilities.implicitWaitSleep(5000);
            Utilities.WaitUntilElementIsClickable(productsLink);
            Utilities.WaitUntilElementIsClickable(driver.findElement(By
                    .id("logout")));
            driver.findElement(By.id("logout")).click();
        } catch (Exception e) {
            e.printStackTrace();
            Reporter.log(e.toString());
        }
    }

    public void add3DevicesToInventory(String imei1, String imei2,
                                       String imei3, String product) throws InterruptedException,
            AWTException, java.io.IOException {
        try {
            Utilities.WaitUntilElementIsClickable(purchasingLink);
            purchasingLink.click();
            Utilities.waitForElementVisible(receivingLink);
            Utilities.WaitUntilElementIsClickable(receivingLink);
            receivingLink.click();
            Utilities.implicitWaitSleep(3000);
            driver.switchTo().frame(iframeInventory);

            // Add first device into inventory
            recieveNewShipment(imei1, product);

            //Adding second device
            recieveNewShipment(imei2, product);

            //Adding third device
            recieveNewShipment(imei3, product);

            Utilities.implicitWaitSleep(3000);
            switchingBetweenSerializedAndFrame();

            if (!deviceStatus(imei1)) {
                recieveNewShipment(imei1, product);
            }

            Utilities.implicitWaitSleep(3000);
            if (!deviceStatus(imei2)) {
                recieveNewShipment(imei2, product);
            }

            Utilities.implicitWaitSleep(3000);
            if (!deviceStatus(imei3)) {
                recieveNewShipment(imei3, product);
            }
            driver.switchTo().defaultContent();
            Utilities.implicitWaitSleep(5000);
            Utilities.WaitUntilElementIsClickable(productsLink);
            Utilities.WaitUntilElementIsClickable(driver.findElement(By
                    .id("logout")));
            driver.findElement(By.id("logout")).click();
        } catch (Exception e) {
            e.printStackTrace();
            Reporter.log(e.toString());
        }
    }

    public void addSingleDeviceToInventory(String iemi1,String product1) throws InterruptedException, AWTException, java.io.IOException {
        try {
            Utilities.WaitUntilElementIsClickable(purchasingLink);
            purchasingLink.click();
            Utilities.waitForElementVisible(receivingLink);
            Utilities.WaitUntilElementIsClickable(receivingLink);
            receivingLink.click();
            Utilities.implicitWaitSleep(3000);
            driver.switchTo().frame(iframeInventory);

            // Add first device into inventory
            recieveNewShipment(iemi1, product1);

            //Verifying IMEI added or not.
            Utilities.implicitWaitSleep(3000);
            switchingBetweenSerializedAndFrame();

            if (!deviceStatus(iemi1)) {
                recieveNewShipment(iemi1, product1);
            }

            driver.switchTo().defaultContent();
            Utilities.implicitWaitSleep(5000);
            Utilities.WaitUntilElementIsClickable(productsLink);
            Utilities.WaitUntilElementIsClickable(driver.findElement(By
                    .id("logout")));
            driver.findElement(By.id("logout")).click();
        } catch (Exception e) {
            e.printStackTrace();
            Reporter.log("Device not added");
        }
    }

    // endregion Inventory Login Page Methods
}