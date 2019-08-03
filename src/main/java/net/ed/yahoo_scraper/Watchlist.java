package net.ed.yahoo_scraper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import net.ed.utils.ILoggable;

public class Watchlist implements ILoggable {
	
	Watchlist(){
		logger.debug("inside Watchlist() constructor");
	}
	
    // get date &  time of scrape in mysql format
	private static LocalDateTime now = LocalDateTime.now();
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static String timeStamp = now.format(formatter);
	
    // get date &  time of scrape in mysql format
	public static String getTimeStamp() {
		return timeStamp;
	}
	
	// set date &  time of scrape in mysql format
	public static void setTimeStamp(String timeStamp) {
		Watchlist.timeStamp = timeStamp;
		System.out.println("67. inside setTimeStamp(): " + Watchlist.timeStamp);
	}
	
    // get date &  time of scrape in mysql format
//    public String Watchlist(String timeStamp) {
//		System.out.println("56. inside ScraperUtil(String timeStamp) constructor");
//		return getTimeStamp();
//    } 
	
	public WebDriver getWatchlistPage(WebDriver driver) {
		
		String watchlistURL = "https://finance.yahoo.com/portfolio/p_0/view?bypass=true";
		logger.info(watchlistURL);
		driver.get(watchlistURL);
		logger.debug(driver.getTitle());
		
		return driver;
	}
	
	public void getData(WebDriver driver) {
		
		logger.info("inside Watchlist.getData()");
		
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
//			System.out.println(" fetching data... row " + row);
			watchList.add(getTimeStamp()); // col 0
			for (int col = 1; col < numCols; col++){
				//  col 0 is for getTimeStamp() so start scraping at col 1
				switch(col) {
					case 1:	// symbol
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/a")).getText();
						watchList.add(tdData);
						break;
					case 2: // lastPrice
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						pattern = ",";
						tdData = tdData.replaceAll(pattern,"");
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
						watchList.add(tdData);
						break;
					case 5:	// currency
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]")).getText();
						watchList.add(tdData);
						break;
					case 6: // marketTime
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						// need to format market_time as yyyy-mm-dd
						tdData = String.valueOf(tdData);
						watchList.add(tdData);
						break;
					case 7: // volume
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						pattern = "[M](?!\\d+.\\d+)"; 
						tdData = tdData.replaceAll(pattern,"");
						watchList.add(tdData);
						break;
					case 8: // shares
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]")).getText();
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
						watchList.add(tdData);
						break;
					case 10: case 11: case 12:
						tdData = "";
						break;
					case 13: // marketCap
						tdData = dataTable.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]/span")).getText();
						pattern = "[MBT](?!\\d+.\\d+)"; 
						tdData = tdData.replaceAll(pattern,"");
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

//		try {
//			FileUtils.writeLines(new File("src/main/resources/watchList.txt"), watchList);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
