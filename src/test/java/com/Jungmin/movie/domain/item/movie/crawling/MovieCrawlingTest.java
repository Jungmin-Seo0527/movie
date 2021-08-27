package com.Jungmin.movie.domain.item.movie.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MovieCrawlingTest {

    @Test
    public void test_title() { //타이틀 확인하는 테스트 코드
        WebDriver driver = null;
        try {
            // drvier 설정 - 저는 d드라이브 work 폴더에 있습니다.
            System.setProperty("webdriver.chrome.driver", "chromedriver_win32/chromedriver.exe");
            // Chrome 드라이버 인스턴스 설정
            driver = new ChromeDriver();
            // 스크립트를 사용하기 위한 캐스팅
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // 블로그 URL로 접속
            driver.get("https://nowonbun.tistory.com");
            // 대기 설정
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            // xpath로 element를 찾는다. 이 xpath는 명월 일지 블로그의 왼쪽 메뉴의 Dev note의 Javascript, Jquery, Css 카테고리다.
            WebElement element = driver.findElement(By.xpath("//*[@id='leftside']/div[2]/ul/li/ul/li[1]/ul/li[6]/a"));
            // 클릭한다. 사실 element.click()로도 클릭이 가능한데 가끔 호환성 에러가 발생하는 경우가 있다.
            js.executeScript("arguments[0].click();", element);
            while (true) {
                try {
                    // css selector로 element를 찾는다.
                    element = driver.findElement(By.cssSelector("[href^='/626']"));
                    // 클릭
                    js.executeScript("arguments[0].click();", element);
                    // 루프 정지
                    break;
                } catch (Throwable e) {
                    // 해당 element가 없으면 아래의 다음 페이지 element를 찾는다.
                    element = driver.findElement(By.cssSelector(".paging li.active+li > a"));
                    // 클릭
                    js.executeScript("arguments[0].click();", element);
                }
            }
            // id가 promptEx인 데이터를 찾는다.
            element = driver.findElement(By.xpath("//*[@id='promptEx']"));
            // 버튼은 클릭이 되는데 link 계열은 script로 클릭해야 한다.
            element.click();
            // xpath로 팝업의 dom를 찾는다.
            element = driver.findElement(By.xpath("/html/body/div[6]/div/div/div[2]/div/form/input"));
            // input text에 test의 값을 넣는다.
            element.sendKeys("test");
            // 5초 기다린다.
            Thread.sleep(5000);
            // xpath로 팝업의 dom를 찾는다.
            element = driver.findElement(By.xpath("/html/body/div[6]/div/div/div[2]/div/form/input"));
            // 속성 value를 출력한다.
            System.out.println(element.getAttribute("value"));
            // .article의 글에 p 태그의 속성을 전부 가져온다.
            List<WebElement> elements = driver.findElements(By.cssSelector(".article p"));
            for (WebElement ele : elements) {
                // 속성의 NodeText를 전부 출력한다.
                System.out.println(ele.getText());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }

    @Test
    @DisplayName("크롤링 테스트")
    public void test2() {
        // Jsoup를 이용해서 http://www.cgv.co.kr/movies/ 크롤링
        String url = "http://www.cgv.co.kr/movies/"; //크롤링할 url지정
        Document doc = null;        //Document에는 페이지의 전체 소스가 저장된다

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //select를 이용하여 원하는 태그를 선택한다. select는 원하는 값을 가져오기 위한 중요한 기능이다.
        Elements element = doc.select("div.sect-movie-chart");
        System.out.println(element.text());

        System.out.println("============================================================");

        //Iterator을 사용하여 하나씩 값 가져오기
        Iterator<Element> ie1 = element.select("strong.rank").iterator();
        Iterator<Element> ie2 = element.select("strong.title").iterator();

        while (ie1.hasNext()) {
            System.out.println(ie1.next().text() + "\t" + ie2.next().text());
        }

        System.out.println("============================================================");
    }
}

