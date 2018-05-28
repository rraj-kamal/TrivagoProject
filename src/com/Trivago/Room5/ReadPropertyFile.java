package com.Trivago.Room5;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.AssertJUnit;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;




public class ReadPropertyFile {

	static Properties properties;
	static ExtentReports extent;
	static ExtentTest logger;
	
	public static void loadData() throws IOException {
		
		properties = new Properties();
		File f = new File(System.getProperty("user.dir")+"\\Config\\InputData.properties");
		FileReader fReader = new FileReader(f);
		properties.load(fReader);
	}
	
	public static String getValuefromProperty(String data) throws IOException{
		loadData();
		data =properties.getProperty(data);
		return data;
	}
	
	public static void switchTab(WebDriver driver) throws InterruptedException
	{
		Set <String> st= driver.getWindowHandles();
		Iterator<String> it = st.iterator();
		String parent =  it.next();
		String child =it.next();
		//swtich to parent
		driver.switchTo().window(parent);
		Thread.sleep(100);
		//System.out.println("Returned to parent");
		// switch to child 
		driver.switchTo().window(child);
		Thread.sleep(100);
	}

	public static WebDriver launchBrowser(WebDriver driver, String strbrowser) throws InterruptedException
	{
		String driverPath = System.getProperty("user.dir")+"\\Libs\\Selenium Libs\\Libs Repository\\";
		WindowsUtils.killByName("firefox.exe");
		System.out.println("Launching firefox browser..."); 
		System.setProperty("webdriver.gecko.driver", driverPath+"geckodriver.exe");
		return driver = new FirefoxDriver();

	}
	
	public static void reportEvent(String strStatus, String strTestName, String strTestDesc) throws InterruptedException, IOException
	{
		
		extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/STMExtentReport.html", true);
		extent.addSystemInfo("Host name", getValuefromProperty("appName"))
		.addSystemInfo("Environment", getValuefromProperty("browser"))
		.addSystemInfo("User name",getValuefromProperty("user"));
		extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
	
		if(strStatus.contains("P"))
		{	
			logger = extent.startTest(strTestName);
			AssertJUnit.assertTrue(true);
			logger.log(LogStatus.PASS, strTestDesc);
		}
	}
	



	}


