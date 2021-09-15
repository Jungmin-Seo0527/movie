package com.Jungmin.movie.domain.item.movie;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public enum Platform {
    GOOGLE, NAVER;

    public static String GOOGLE_POPULAR_URL = "https://play.google.com/store/movies/top";
    public static String GOOGLE_URL = "https://play.google.com";
    public static String NAVER_POPULAR_URL = "https://serieson.naver.com/movie/top100List.nhn?page=0&rankingTypeCode=PC_D";
}
