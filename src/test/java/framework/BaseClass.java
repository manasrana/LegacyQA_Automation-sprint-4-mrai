package framework;

import java.awt.AWTException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

public abstract class BaseClass extends BrowserSettings {
	@BeforeTest
	abstract void LaunchApplication() throws InterruptedException,
			AWTException, java.io.IOException;

	@BeforeTest(alwaysRun = true)
	public void SetUp() throws InterruptedException {
		Reporter.log("Start of Executing Test Case <br> <p>");
		Reporter.log(
				"-----------------------------------------------------------------------<br><p>",
				true);
		DOMConfigurator.configure("src/test/resources/log4j.xml");
		Log.info("browser is being launched");
		Log.info("browser has been launched");
	}

	@AfterMethod(alwaysRun = true, enabled = true)
	public void AfterMethod(ITestResult result) {
		// Close the driver
		Reporter.log("Finished Executing Test Case <br> <p>", true);
		// Reporter.log(context.getMethodName());
		Reporter.log(
				"-----------------------------------------------------------------------<br><p>",
				true);

		if (result.getStatus() == 2) {
			Utilities.driverTakesScreenshot(result.getMethod().getMethodName());
		}
		driver.quit();
		Reporter.log("Selenium driver object has been disposed");
	}

	@BeforeSuite(alwaysRun = true, enabled = true)
	public void CreateCSV() throws IOException {
		File objFile = null;
		String testType = readConfig("test-type");
		if (testType.equals("internal")) {
			objFile = new File(
					"src/test/resources/TestData-Internal/DependentTestCaseDetails.csv");
		} else {
			objFile = new File(
					"src/test/resources/TestData-External/DependentTestCaseDetails.csv");
		}
		if (!objFile.exists()) {
			objFile.createNewFile();
		}
		// true = append file
		FileWriter fileWriter = new FileWriter(objFile, true);
		BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
		if (objFile.length() < 1) {
			bufferWriter
					.write("TC_ID,ORDER_ID,ESN/IMEI,ESN/IMEI,FNAME,LNAME,EMAIL,RECEIPT_ID,ID_TYPE,STATE,ID_NUMBER,PhNum,zip,SSN,Month,Year");
		}
		bufferWriter.close();
	}

	@AfterSuite(alwaysRun = true, enabled = true)
	public void RemoveCSV() throws IOException {
		File objFile = null;
		File newFileName = null;
		String testType = readConfig("test-type");

		if (testType.equals("internal")) {
			/*
			 * objFile = new
			 * File("TestData-Internal/DependentTestCaseDetails.csv");
			 * newFileName = new
			 * File("TestData-Internal/DependentTestCaseDetails"+
			 * LocalDateTime.now() + ".csv");
			 */
		} else {/*
				 * objFile = new
				 * File("TestData-External/DependentTestCaseDetails.csv");
				 * newFileName = new
				 * File("TestData-External/DependentTestCaseDetails"+
				 * LocalDateTime.now() + ".csv");
				 */
		}

		// System.out.println(newFileName.getName());
		// System.out.println(newFileName.getAbsolutePath());
		// System.out.println(objFile.getName());
		// System.out.println(objFile.getAbsolutePath());
		// FileUtils.copyFile(objFile, newFileName);
		// objFile.delete();
	}
}