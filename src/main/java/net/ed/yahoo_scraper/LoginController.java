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
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginController implements ILoggable {
	
    private static String[] loginCredentials = new String[4];

	private WebDriver driver;
	
	public WebDriver loadWebDriver() {
		
		String keyC = "webdriver.chrome.driver";
		String valueC = "/Users/melocal/Applications/lib/chromedriver";
		System.setProperty(keyC, valueC);
		
		driver = new ChromeDriver();
		return driver;
	}
	
	public void unloadWebDriver() {
	    if (driver != null) {
	    	logger.debug("unloading driver");
	    	driver.quit();
	    }
	}
	
    public static String[] readLoginCredentials() {
    	
    	String filename = "src/main/resources/yahoo.properties";
        
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

	public WebDriver navigateToLogin() throws TimeoutException {
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		driver.get("https://login.yahoo.com");
		String pageTitle = driver.getTitle();
		logger.debug(pageTitle);
		
		getLoggedIn();
		return driver;
	}

	public WebDriver getLoggedIn() {
		
		readLoginCredentials();
		
		String username = loginCredentials[1];
		String password = loginCredentials[2];
		
		try {
			driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);			 
			driver.findElement(By.name("username")).sendKeys(username); 
			driver.findElement(By.name("username")).sendKeys(Keys.ENTER);
			driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
			driver.findElement(By.name("password")).sendKeys(password);
			driver.findElement(By.name("password")).sendKeys(Keys.ENTER);
			driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
			logger.info(driver.getCurrentUrl());

		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		}
		return driver;
	}
}
