package net.ed.yahoo_scraper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class LoginControllerTest {

	@Test
	public void loadWebDriverTest() {

		LoginController login = new LoginController();
		String expected = "Yahoo - login";
		assertEquals(expected, login.loadWebDriver());
		login.unloadWebDriver();
	}
}
