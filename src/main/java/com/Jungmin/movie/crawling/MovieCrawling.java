package com.Jungmin.movie.crawling;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class MovieCrawling {

    private WebDriver driver;
    private WebElement element;
    private JavascriptExecutor js;
    private String url;

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "chromedriver_win32/chromedriver.exe";
    private int interval = 2000;

    public MovieCrawling(boolean mode) {
        if (!mode) return;
        // WebDriver 경로 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        options.setHeadless(true);

        driver = new ChromeDriver(options);

        url = "https://play.google.com/store/movies/top";
        js = (JavascriptExecutor) driver;
        driver.get(url);
    }

    public void scrollDownToBottom() throws InterruptedException, IOException {
        log.info("[MovieCrawling.scrollDownToBottom]");
        Long prev_height = (Long) js.executeScript("return document.body.scrollHeight");
        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(interval);
            Long cur_height = (Long) js.executeScript(
                    "return document.body.scrollHeight"
            );
            if (prev_height.equals(cur_height)) {
                break;
            }
            prev_height = cur_height;
        }
        log.info("Scrolling success!!!");
        String html = driver.getPageSource();
        BufferedWriter bw = new BufferedWriter(new FileWriter("HTML"));
        bw.write(html);
        bw.close();
    }

    public void scrapingSource() throws IOException {
        Document html = Jsoup.parse(driver.getPageSource(), url);
        Iterator<Element> movies = html.getElementsByClass("Vpfmgd").iterator();

        int rank = 1;
        while (movies.hasNext()) {
            Element movie = movies.next();
            String title = movie.getElementsByClass("WsMG1c nnK0zc").text();
            String genre = movie.getElementsByClass("KoLSrc").text();
            if (genre.length() != 0) {
                genre = genre.substring(0, genre.length() / 2);
            }
            String price = movie.getElementsByClass("VfPpfd ZdBevf i5DZme").text();

            System.out.println("rank= " + rank++ + "title= " + title + "  genre= " + genre + " price= " + price);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        MovieCrawling bot1 = new MovieCrawling(true);
        bot1.scrollDownToBottom();
        bot1.scrapingSource();
    }
}
