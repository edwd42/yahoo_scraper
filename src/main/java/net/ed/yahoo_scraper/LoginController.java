package net.ed.yahoo_scraper;

import net.ed.utils.ILoggable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
//import com.gargoylesoftware.htmlunit.BrowserVersion; 

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
	
	public void navigateToLogin() throws TimeoutException {
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
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

	public WebDriver getLoggedIn() {
		
		readloginCredentials();
		
		String username = loginCredentials[1];
		String password = loginCredentials[2];
		
		
		try {
			// fill in the loginCredentials
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
	
	public void getWatchlistPage() {
		
		String watchlistURL = loginCredentials[3];
		logger.info(watchlistURL);
		driver.get(watchlistURL);
		logger.info(driver.getTitle());
		String title = driver.getTitle();
		try {
			getData();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}
	
	
	public void getData() {
		
		String pattern = "";
		String tdData = "";
		List<String> watchList = new ArrayList<>();
		
		// https://www.guru99.com/handling-dynamic-selenium-webdriver.html
		WebElement dataTable = driver.findElement(By.xpath("//table/tbody"));

		int numRows = dataTable.findElements(By.xpath("//table/tbody/tr")).size();
		logger.debug("numRows: " + String.valueOf(numRows));
		
		int numCols = dataTable.findElements(By.xpath("//table/tbody/tr[1]/td")).size();
		logger.debug("numCols: " + String.valueOf(numCols));
		
		for (int row=1; row < numRows; row++) {
			System.out.print("row " + row + " fetching data...");
//			watchList.setTodaysDate(getTimeStamp()); // col 0
			for (int col = 1; col < numCols; col++){
				//  col 0 is for getTimeStamp() so start scraping at col 1
				switch(col) {
					case 1:	// symbol
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/a")).getText();
						logger.debug(tdData);
						watchList.add(tdData);
						break;
					case 2: // lastPrice
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						pattern = ",";
						tdData = tdData.replaceAll(pattern,"");
						logger.debug(tdData);
						if (tdData != null && tdData.length() > 0) {
							try {
								watchList.add(String.valueOf(tdData));
							} catch (Exception e) {
								logger.debug("e: " + e);
							}
						} else {
							watchList.add(null);
						}
						break;
					case 3: // todaysChange
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						pattern = "[\\,%](?!\\d+.\\d+)"; 
						tdData = tdData.replaceAll(pattern,"");
						logger.debug(tdData);
						if (tdData != "" && tdData.length() > 0) {
							try {
								watchList.add(String.valueOf(tdData));
							} catch (Exception e) {
								logger.debug("e: " + e);
							}
						} else {
							watchList.add(null);
						}
						break;
					case 4: // percentChange
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						pattern = "[\\,%](?!\\d+.\\d+)"; 
						tdData = tdData.replaceAll(pattern,"");
						logger.debug(tdData);
						watchList.add(tdData);
						break;
					case 5:	// currency
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]")).getText();
						logger.debug(tdData);
						watchList.add(tdData);
						break;
					case 6: // marketTime
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						// need to format market_time as yyyy-mm-dd
						tdData = String.valueOf(tdData);
						logger.debug(tdData);
						watchList.add(tdData);
						break;
					case 7: // volume
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						pattern = "[M](?!\\d+.\\d+)"; 
						tdData = tdData.replaceAll(pattern,"");
						logger.debug(tdData);
						watchList.add(tdData);
						break;
					case 8: // shares
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]")).getText();
						logger.debug(tdData);
						if (tdData != "" && tdData.length() > 0) {
							try {
								watchList.add(String.valueOf(tdData));
							} catch (Exception e) {
								logger.debug("e: " + e);
							}
						} else {
							watchList.add(null);
						}
						break;
					case 9: // avgVol
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]")).getText();
						pattern = "[M](?!\\d+.\\d+)"; 
						tdData = tdData.replaceAll(pattern,"");
						logger.debug(tdData);
						watchList.add(tdData);
						break;
					case 10: case 11: case 12:
						tdData = "";
//						watchList.add("");
//						watchList.add("");
//						watchList.add("");
						break;
					case 13: // marketCap
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						logger.debug(tdData);
						pattern = "[MBT](?!\\d+.\\d+)"; 
						tdData = tdData.replaceAll(pattern,"");
						logger.debug(tdData);
						if (tdData != "" && tdData.length() > 0 && Double.parseDouble(tdData) < 2.0) {
							tdData = Double.toString(Double.parseDouble(tdData)*1000.0);
						}
						if (tdData != "" && tdData.length() > 0) {
							try {
								watchList.add(String.valueOf(tdData));
							} catch (Exception e) {
							}
						} else {
							watchList.add(null);
						}
						break;
					case 14: case 15: case 16:
						tdData = "";
						break;
					} // end switch
				} // end col
		} // end row
		for (String temp : watchList) {
			System.out.println(temp);
		}
	}
	
}
