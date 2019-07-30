package net.ed.yahoo_scraper;

import net.ed.utils.ILoggable;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginController implements ILoggable {
	
	private WebDriver driver;
	
	public String loadWebDriver() {
		
		// add support for chrome driver
		String keyC = "webdriver.chrome.driver";
		String valueC = "/Users/melocal/Applications/lib/chromedriver";
		System.setProperty(keyC, valueC);
		
		driver = new ChromeDriver(); // launch chrome
		
		String url = "https://login.yahoo.com";
		driver.get(url);
		
		String pageTitle = driver.getTitle();
		logger.debug(pageTitle);
		return pageTitle;
		
	}
	
	public void unloadWebDriver() {
	    if (driver != null) {
	    	driver.quit();
	    }
	}

}
