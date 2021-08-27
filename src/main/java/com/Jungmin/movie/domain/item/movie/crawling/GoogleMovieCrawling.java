package com.Jungmin.movie.domain.item.movie.crawling;

import com.Jungmin.movie.domain.item.movie.Movie;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class GoogleMovieCrawling {

    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final String url;

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "chromedriver_win32/chromedriver.exe";
    private final int interval = 2000;

    public GoogleMovieCrawling() {
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

    /**
     * 동적 페이지 스크롤 끝까지 내리기
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public void scrollDownToBottom() throws InterruptedException {
        log.info("scroll down to bottom");
        Long prev_height = (Long) js.executeScript("return document.body.scrollHeight");
        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(interval);
            Long cur_height = (Long) js.executeScript(
                    "return document.body.scrollHeight"
            );
            if (prev_height.equals(cur_height)) break;
            prev_height = cur_height;
        }
        log.info("Scrolling success!!!");
    }

    /**
     * Movie 정보 매칭
     *
     * @return List<Movie>
     */
    public List<Movie> scrapingSource() throws InterruptedException {
        scrollDownToBottom();
        List<Movie> movieList = new ArrayList<>();
        Document html = Jsoup.parse(driver.getPageSource(), url);
        Iterator<Element> movies = html.getElementsByClass("Vpfmgd").iterator();
        String platformUrl = "https://play.google.com";

        int rank = 1;
        while (movies.hasNext()) {
            Element movie = movies.next();
            String title = movie.getElementsByClass("WsMG1c nnK0zc").text();
            String genre = movie.getElementsByClass("KoLSrc").text();
            if (genre.length() != 0) {
                genre = genre.substring(0, genre.length() / 2);
            }
            String price = movie.getElementsByClass("VfPpfd ZdBevf i5DZme").text().replaceAll("[^0-9]", "");
            String link = movie.getElementsByClass("JC71ub").attr("href");
            movieList.add(Movie.builder()
                    .rank(rank++)
                    .title(title)
                    .price(Integer.parseInt(price))
                    .genre(genre)
                    .url(platformUrl + link)
                    .build());
        }
        log.info("done");
        return movieList;
    }
}
