# ISSUE 정리

## 동적 페이지 크롤링을 위한 Selenium 의존성 추가

[Gradle에 추가하기](https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java)

블로그를 검색해보면 대부분 Gradle 보다는 Maven 방식으로 의존성을 추가하는 방법이 존재한다. 아직까지 Gradle과 Maven 을 잘 다루지 못하기 때문에 Maven 코드를 보고 Gradle 로 바꾸는
작업을 실패했다.

따라서 Gradle 방법을 찾아 보았다. 위 사이트는 Selenium 말고도 다른 프레임워크, 라이브러리 의존성 추가 코드를 제공하고 잇어서 유용하게 사용할 수 있을 듯 하다.

버전은 3.141.59 를 받았다. 4버전이 출시했지만 alpha, beta 만 존재해서 3 버전으로 받았다. 이전에 Selenium3 을 사용한 경험이 있어서 금방 배울 수 있을것이다.

## batch insert

[Spring Data에서 Batch Insert 최적화](https://homoefficio.github.io/2020/01/25/Spring-Data%EC%97%90%EC%84%9C-Batch-Insert-%EC%B5%9C%EC%A0%81%ED%99%94/)

## Builder에서 이미 생성된 컬렉션 처리

`@Builder.Default`

## 새로고침하여 크롤링한 Movie가 이미 DB에 존재하는 경우

Movie 테이블에 엔티티가 존재하지 않았을때 처음 크롤링 하여 영화 정보를 DB에 저장하면 200개가 저장이 된다. 그리고 바로 다시 새로고침하면 이전에 가져온 동일한 영화 정보들을 크롤링 할 것이다. 이때 그대로
DB에 저장하면 이전의 동일한 영화에 대한 정보가 존재하지 않으므로 새로운 영화로 인식되어서 영화 갯수가 400개로 늘어난다.

즉 크롤링한 영화 정보들 중에 이미 DB에 존재하는 영화인지 확인하는 작업이 필요하다.