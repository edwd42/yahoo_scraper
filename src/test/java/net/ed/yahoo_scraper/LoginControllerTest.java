package net.ed.yahoo_scraper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.concurrent.TimeoutException;

import org.junit.Test;

public class LoginControllerTest {

	@Test
	public void loadWebDriverTest() {

		LoginController login = new LoginController();
		String expected = "Yahoo - login";
		assertEquals(expected, login.loadWebDriver());
		login.unloadWebDriver();
	}
	
	@Test
	public void getDataTest() {

		LoginController login = new LoginController();
    	login.loadWebDriver();
    	try {
			login.navigateToLogin();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	login.getWatchlistPage();
    	login.unloadWebDriver();
	}
}
