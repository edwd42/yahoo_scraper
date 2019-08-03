package net.ed.yahoo_scraper;

import java.util.concurrent.TimeoutException;

public class App 
{

	public static void main( String[] args )
    {
    	LoginController login = new LoginController();
    	login.loadWebDriver();
    	try {
			login.navigateToLogin();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    	login.getWatchlistPage();
    	login.unloadWebDriver();
    }
    

}
