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
        // Document html = Jsoup.parse(driver.getPageSource(), url);
        log.debug("저장되어 있는 HTML 문서로 파싱");
        BufferedReader br = new BufferedReader(new FileReader("HTML"));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s).append("\n");
        }

        Document html = Jsoup.parse(sb.toString());

        Iterator<Element> title = html.getElementsByClass("WsMG1c nnK0zc").iterator();
        Iterator<Element> genre = html.getElementsByClass("KoLSrc").iterator();
        Iterator<Element> price = html.getElementsByClass("VfPpfd ZdBevf i5DZme").iterator();
        Iterator<Element> movieUrl = html.getElementsByClass("b8cIId ReQCgd Q9MA7b").iterator();

        int rank = 1;
        while (title.hasNext()) {
            System.out.print("rank = " + rank++ + "\t");
            System.out.print("title = " + title.next().text() + "\t\t\t\t");
            System.out.print("genre = " + genre.next().text() + "\t\t\t");
            System.out.print("price = " + price.next().text() + "\t\t\t");
            System.out.println("movieURL = " + movieUrl.next().text() + "\t");
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        MovieCrawling bot1 = new MovieCrawling(false);
        // bot1.scrollDownToBottom();
        bot1.scrapingSource();
    }
}
