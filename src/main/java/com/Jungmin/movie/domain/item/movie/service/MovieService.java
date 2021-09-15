package com.Jungmin.movie.domain.item.movie.service;

import com.Jungmin.movie.domain.item.movie.Platform;
import com.Jungmin.movie.domain.item.movie.PopularMovie;
import com.Jungmin.movie.domain.item.movie.crawling.MovieCrawling;
import com.Jungmin.movie.domain.item.movie.repository.MovieRepository;
import com.Jungmin.movie.domain.item.movie.repository.PopularMovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final PopularMovieRepository popularMovieRepository;
    private final MovieCrawling movieCrawling;

    @Transactional
    public List<PopularMovie> refreshGooglePopularMovies() throws InterruptedException {
        List<PopularMovie> movies = movieCrawling.connectUrl(Platform.GOOGLE)
                .scrollDownToBottom()
                .scrapingSource()
                .getResultByList();

        return popularMovieRepository.saveAll(movies);
    }

    @Transactional
    public List<PopularMovie> refreshNaverPopularMovies() {
        List<PopularMovie> movies = movieCrawling.connectUrl(Platform.NAVER)
                .scrapingNaverHtml()
                .getResultNaverMoviesByList();
        return popularMovieRepository.saveAll(movies);
    }
}
