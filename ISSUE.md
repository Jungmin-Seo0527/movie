# ISSUE 정리

## 동적 페이지 크롤링을 위한 Selenium 의존성 추가

[Gradle에 추가하기](https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java)

블로그를 검색해보면 대부분 Gradle 보다는 Maven 방식으로 의존성을 추가하는 방법이 존재한다. 아직까지 Gradle과 Maven 을 잘 다루지 못하기 때문에 Maven 코드를 보고 Gradle 로 바꾸는
작업을 실패했다.

따라서 Gradle 방법을 찾아 보았다. 위 사이트는 Selenium 말고도 다른 프레임워크, 라이브러리 의존성 추가 코드를 제공하고 잇어서 유용하게 사용할 수 있을 듯 하다.

버전은 3.141.59 를 받았다. 4버전이 출시했지만 alpha, beta 만 존재해서 3 버전으로 받았다. 이전에 Selenium3 을 사용한 경험이 있어서 금방 배울 수 있을것이다.