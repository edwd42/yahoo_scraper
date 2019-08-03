package net.ed.yahoo_scraper;

import org.openqa.selenium.WebDriver;

public class App 
{
	public static void main( String[] args )
    {
    	try {
    		final long startTime = System.currentTimeMillis();
    		LoginController login = new LoginController();
    		login.loadWebDriver();
    		WebDriver driver = login.navigateToLogin();
	    	Watchlist watchlist = new Watchlist();
    		watchlist.getWatchlistPage(driver);
    		watchlist.getData(driver);
    		login.unloadWebDriver();
    		final long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) / 1000.0 + " seconds");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
}
