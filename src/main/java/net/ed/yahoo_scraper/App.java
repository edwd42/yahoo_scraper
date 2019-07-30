package net.ed.yahoo_scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class App 
{
    public static void main( String[] args )
    {

    	LoginController login = new LoginController();
    	login.loadWebDriver();
    	login.unloadWebDriver();
    }
}
