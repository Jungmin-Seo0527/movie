package com.Jungmin.movie.domain.item.movie.crawling;

import com.Jungmin.movie.domain.item.movie.Movie;
import com.Jungmin.movie.domain.item.movie.Platform;
import com.Jungmin.movie.domain.item.movie.PopularMovie;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
public class MovieCrawling {

    private final WebDriver driver;

    private JavascriptExecutor js;
    private String url;
    private Iterator<Element> movies;
    private List<Document> htmlList = new ArrayList<>();

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "chromedriver_win32/chromedriver.exe";
    private final int interval = 2000;

    public MovieCrawling() {
        // WebDriver 경로 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        options.setHeadless(true);

        driver = new ChromeDriver(options);
    }

    public MovieCrawling connectUrl(Platform platform) {
        if (platform.equals(Platform.GOOGLE)) url = Platform.GOOGLE_POPULAR_URL;
        else if (platform.equals(Platform.NAVER)) url = Platform.NAVER_POPULAR_URL;
        js = (JavascriptExecutor) driver;
        driver.get(url);
        return this;
    }

    /**
     * 동적 페이지 스크롤 끝까지 내리기
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public MovieCrawling scrollDownToBottom() throws InterruptedException {
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
        return this;
    }

    /**
     * Movie 정보 매칭
     *
     * @return List<Movie>
     */
    public MovieCrawling scrapingSource() {
        Document html = Jsoup.parse(driver.getPageSource(), url);
        movies = html.getElementsByClass("Vpfmgd").iterator();
        return this;
    }

    public MovieCrawling scrapingNaverHtml() {
        for (int i = 1; i <= 6; i++) {
            String curUrl = Platform.NAVER_POPULAR_URL.replace("page=0", "page=" + i);
            driver.get(curUrl);
            htmlList.add(Jsoup.parse(driver.getPageSource(), curUrl));
        }
        return this;
    }

    public List<PopularMovie> getResultNaverMoviesByList() {
        List<PopularMovie> movieList = new ArrayList<>();

        int rank = 1;
        for (int i = 0; i < htmlList.size(); i++) {

            movies = htmlList.get(i).getElementsByTag("li").iterator();

            while (movies.hasNext()) {
                Element movie = this.movies.next();
                // System.out.println("movie = " + movie);
                String title = movie.getElementsByClass("NPI=a:dcontent").attr("title");
                if(title.isBlank()) continue;

                System.out.println("title = " + title);
            }
        }

        return movieList;
    }

    public static void main(String[] args) {
        MovieCrawling m = new MovieCrawling();
        List<PopularMovie> resultNaverMoviesByList = m.connectUrl(Platform.NAVER)
                .scrapingNaverHtml()
                .getResultNaverMoviesByList();
    }

    public List<PopularMovie> getResultByList() {
        List<PopularMovie> movieList = new ArrayList<>();
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
            movieList.add(PopularMovie.builder()
                    .rank(rank++)
                    .title(title)
                    .price(Integer.parseInt(price))
                    .genre(genre)
                    .url(Platform.GOOGLE_URL + link)
                    .platform(Platform.GOOGLE)
                    .build());
        }
        return movieList;
    }
}
