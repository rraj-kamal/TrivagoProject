package com.Trivago.Room5;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestDriver extends ReadPropertyFile {

	ExtentReports extent;
	ExtentTest logger;
	public WebDriver driver;

	static Properties properties;

	@BeforeSuite
	public void startBrowser() throws IOException {

		/* Report configuration */
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/STMExtentReport.html", true);
		extent.addSystemInfo("Host name", getValuefromProperty("appName"))
				.addSystemInfo("Environment", getValuefromProperty("browser"))
				.addSystemInfo("User name", getValuefromProperty("user"));
		extent.loadConfig(new File(System.getProperty("user.dir") + "\\extent-config.xml"));
	}

	@BeforeTest
	public void startTest() throws InterruptedException, IOException {

		/* Launch & navigate to URL */
		driver = launchBrowser(driver, getValuefromProperty("browser"));
		driver.navigate().to(getValuefromProperty("url"));
		driver.manage().window().maximize();

		/* Check and write to report if cookie is present */
		if (driver.findElement(By.className(getValuefromProperty("cookie"))).isDisplayed()) {
			driver.findElement(By.className(getValuefromProperty("cookie"))).click();
			// reportEvent("Pass","CCCCookie Popup","User accepted the cookie with OK");

			logger = extent.startTest("Cookie Popup");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User accepted the cookie with OK");
		} else {
			logger = extent.startTest("Cookie Popup");
			throw new SkipException("Skip test : Cookie message is not displayed");
		}
		/* Check page title */
		if (driver.getTitle().contains("Room5")) {
			logger = extent.startTest("Navigate to the URL");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User successfully navigated to the URL");
			// reportEvent("Pass","Navigate to the URL","User successfully navigated to the
			// URL");
		} else if (driver.getTitle().contains("404")) {
			{
				logger = extent.startTest("Navigate to the URL");
				AssertJUnit.assertTrue(false);
				logger.log(LogStatus.FAIL, "Error 404 : Not found");
			}
		} else {
			logger = extent.startTest("Navigate to the URL");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "User failed to navigated to the URL");
		}
	}

	@Test(priority = 0)
	public void searchLocation() throws InterruptedException, IOException {

		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions
				.or(ExpectedConditions.visibilityOfElementLocated(By.className(getValuefromProperty("searchIcon")))));

		/* Click on search icon and enter a destination */
		if (driver.findElement(By.className(getValuefromProperty("searchIcon"))).isEnabled()) {
			driver.findElement(By.className(getValuefromProperty("searchIcon"))).click();
			Thread.sleep(30);

			// reportEvent("Pass","Search : Click on search icon","User is able to
			// successfully click the search icon");
			logger = extent.startTest("Search : Click on search icon");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is able to successfully click the search icon");
		} else {
			logger = extent.startTest("Search : Click on search icon");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "User is unable click the search icon");
		}

		// JavascriptExecutor js = (JavascriptExecutor) driver;
		// js.executeScript("arguments[0].scrollIntoView();",By.cssSelector(searchBar));

		if (driver.findElement(By.cssSelector(getValuefromProperty("searchBar"))).isEnabled()) {
			driver.findElement(By.cssSelector(getValuefromProperty("searchBar")))
					.sendKeys(getValuefromProperty("searchKeyword"));
			driver.findElement(By.cssSelector(getValuefromProperty("searchBar"))).sendKeys(Keys.RETURN);
			Thread.sleep(2000);
			logger = extent.startTest("Search : Enter a destination");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is able to successfully enter a destination for search");
		} else {
			logger = extent.startTest("Search : Enter a destination");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "User is unable to enter a destination for search");
		}

		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.className("section-title"))));

		if (driver.findElement(By.tagName("h3")).getText().contains("SEARCH RESULTS")) {
			System.out.println(driver.findElement(By.tagName("h3")).getText());
			logger = extent.startTest("Search : Search results based on input destination");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is able to receive results based on input keyword");
		} else if (driver.findElement(By.tagName("h3")).getText().contains("NO RESULTS")) {
			System.out.println(driver.findElement(By.tagName("h3")).getText());
			logger = extent.startTest("Search : Search results based on input destination");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "No results found for the keyword");
		}

		wait.until(ExpectedConditions
				.or(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class='search-icon open']"))));
		driver.findElement(By.cssSelector("div[class='search-icon open']")).click();
	}

	@Test(priority = 1)
	public void contactDetails() throws InterruptedException, IOException {

		// Sync
		WebDriverWait wait = new WebDriverWait(driver, 30);

		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

		/* Click on contact link and send a message */
		if (driver.findElement(By.linkText(getValuefromProperty("contactLink"))).isEnabled()) {
			driver.findElement(By.linkText(getValuefromProperty("contactLink"))).click();
			Thread.sleep(2500);
			logger = extent.startTest("Click the Contact link");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is able to successfully click the contact link");
		} else {
			logger = extent.startTest("Click the Contact link");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "User is unable click the contact link");
		}

		switchTab(driver);

		if (driver.findElement(By.className(getValuefromProperty("message"))).isEnabled()) {
			driver.findElement(By.className(getValuefromProperty("message"))).click();
			driver.findElement(By.className(getValuefromProperty("message")))
					.sendKeys(getValuefromProperty("contactMessage"));
			logger = extent.startTest("Contact : Enter the message");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is able to successfully enter the message in textbox");
		} else {
			logger = extent.startTest("Contact : Enter the message");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "User is unable enter the message in textbox");
		}

		wait.until(ExpectedConditions
				.or(ExpectedConditions.visibilityOfElementLocated(By.className(getValuefromProperty("name")))));

		if (driver.findElement(By.className(getValuefromProperty("name"))).isEnabled()) {
			driver.findElement(By.className(getValuefromProperty("name"))).click();
			driver.findElement(By.className(getValuefromProperty("name")))
					.sendKeys(getValuefromProperty("contactName"));
			logger = extent.startTest("Contact : Enter Name");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is able to successfully enter the name in the textbox");
		} else {
			logger = extent.startTest("Contact : Enter Name");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "User is unable enter the name in name textbox");
		}

		wait.until(ExpectedConditions
				.or(ExpectedConditions.visibilityOfElementLocated(By.id(getValuefromProperty("email")))));

		if (driver.findElement(By.id(getValuefromProperty("email"))).isEnabled()) {
			driver.findElement(By.id(getValuefromProperty("email"))).click();
			driver.findElement(By.id(getValuefromProperty("email"))).sendKeys(getValuefromProperty("contactEmail"));
			logger = extent.startTest("Contact : Enter Email ID");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is able to successfully enter the email id in the textbox");
		} else {
			logger = extent.startTest("Contact : Enter Email ID");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "User is unable enter the name in name textbox");
		}

		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.id("confirm"))));
		if (driver.findElement(By.id("confirm")).isEnabled()) {
			driver.findElement(By.id("confirm")).click();
			logger = extent.startTest("Contact : Confirmation Tick-box");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is able to click and confirm the details");
		} else {
			logger = extent.startTest("Contact : Enter Email ID");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "User is unable to click and confirm the details");
		}

		wait.until(
				ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.className("contact-submit"))));
		if (driver.findElement(By.className("contact-submit")).isEnabled()) {
			driver.findElement(By.className("contact-submit")).click();
			logger = extent.startTest("Contact : Submit Message");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is able to successfully subit the message");
		} else {
			logger = extent.startTest("Contact : Submit Message");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "User is unable to submit the message");
		}

		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.className("feedback"))));

		if (driver.findElement(By.className("feedback")).isEnabled()) {
			driver.findElement(By.className("feedback")).click();
			logger = extent.startTest("Contact : Message acknoledgement");
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, "User is prompted with the mesaage sent successfully");
		} else {
			logger = extent.startTest("Contact : Message acknoledgement");
			AssertJUnit.assertTrue(false);
			logger.log(LogStatus.FAIL, "Prompt message regarding submitted message is not displayed");
		}

	}

	@Test(priority = 2)
	public void demoFailTest() {
		logger = extent.startTest("Fail Test : Demo");
		AssertJUnit.assertTrue(false);
		logger.log(LogStatus.FAIL, "This test is for report demonstration purpose only");
	}

	@Test(priority = 3)
	public void demoSkipTest() {
		logger = extent.startTest("Skip test : Demo purpose");
		throw new SkipException("Skipping");
	}

	@AfterMethod
	public void getResult(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			// log
			logger.log(LogStatus.FAIL, "Test failed is : " + result.getName());
			logger.log(LogStatus.FAIL, "Test failed is : " + result.getThrowable());
		}

		else if (result.getStatus() == ITestResult.SKIP) {
			// log
			logger.log(LogStatus.SKIP, "Test failed is : " + result.getName());
			// logger.log(LogStatus.FAIL, "Test failed is : "+result.getThrowable());
		}
		extent.endTest(logger);

	}

	@AfterTest
	public void endReport() {
		extent.flush();
		// extent.close();
	}

	@AfterSuite
	public void killBrowser() {
		WindowsUtils.killByName("firefox.exe");
	}
}
