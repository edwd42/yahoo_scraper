package net.ed.yahoo_scraper;

public class App 
{

	public static void main( String[] args )
    {
    	LoginController login = new LoginController();
    	login.loadWebDriver();
    	login.navigate();
//    	login.unloadWebDriver();
    }
    

}
