package net.ed.yahoo_scraper;

import net.ed.utils.ILoggable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.gargoylesoftware.htmlunit.BrowserVersion; 

public class LoginController implements ILoggable {
	
    private static String[] loginCredentials = new String[4];

	private WebDriver driver;
//    private HtmlUnitDriver driver;
	
	public WebDriver loadWebDriver() {
		
		// add support for chrome driver
		String keyC = "webdriver.chrome.driver";
		String valueC = "/Users/melocal/Applications/lib/chromedriver";
		System.setProperty(keyC, valueC);
		
		driver = new ChromeDriver(); // launch chrome
//		driver = new HtmlUnitDriver();
		return driver;
	}
	
	public void navigateToLogin() {
		
		driver.get("https://login.yahoo.com");
		
		String pageTitle = driver.getTitle();
		logger.debug(pageTitle);
		
		getLoggedIn();
	}
	
	public void unloadWebDriver() {
	    if (driver != null) {
	    	logger.debug("unloading driver");
	    	driver.quit();
	    }
	}

    public static String[] readloginCredentials() {
    	
    	String filename = "src/main/resources/yahoo.txt";
        
		try {
			File file = new File(filename);
			Scanner scnr = new Scanner(file);
	        int lineNumber = 0;
	        while(scnr.hasNextLine()){
	            String line = scnr.nextLine();
	            loginCredentials[lineNumber] = line;
	            lineNumber++;
	        }
	        scnr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return loginCredentials; 
    }

	public WebDriver getLoggedIn() {
		
		readloginCredentials();
		
		String username = loginCredentials[1];
		String password = loginCredentials[2];
		
		
		try {
			
			driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);			 
			driver.findElement(By.name("username")).sendKeys(username + Keys.ENTER); // fill in the blanks
			driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
//			driver.findElement(By.name("signin")).click();
			driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
			driver.findElement(By.name("password")).sendKeys(password + Keys.ENTER);
			driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
//			driver.findElement(By.name("verifyPassword")).click();
			logger.info(driver.getCurrentUrl());

		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		}
		return driver;
	}
	
	public void getWatchlistPage() {
		
		String watchlistURL = loginCredentials[3];
		logger.info(watchlistURL);
//		driver.get(watchlistURL);
		driver.navigate().to(watchlistURL);
		logger.info(driver.getTitle());
		getData();
	}
	
	
	public void getData() {
		
		WebElement dataTable = driver.findElement(By.xpath("//table/tbody"));
		System.out.println(dataTable.getText());
	}
	
}
