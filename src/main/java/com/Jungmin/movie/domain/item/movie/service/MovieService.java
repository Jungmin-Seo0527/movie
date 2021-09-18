package com.Jungmin.movie.domain.item.movie.service;

import com.Jungmin.movie.domain.item.movie.Movie;
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
    public List<Movie> refreshGooglePopularMovies() throws InterruptedException {
        List<Movie> movies = movieCrawling.connectUrl(Platform.GOOGLE)
                .scrollDownToBottom()
                .scrapingSource()
                .getResultByList();
        movieRepository.saveAll(movies);

        final int[] rank = {1};
        movies.stream().map(movie -> PopularMovie.builder()
                        .rank(rank[0]++)
                        .movie(movie)
                        .build())
                .forEach(popularMovieRepository::save);
        return movies;
    }

    @Transactional
    public List<Movie> refreshNaverPopularMovies() {
        List<Movie> movies = movieCrawling.connectUrl(Platform.NAVER)
                .scrapingNaverHtml()
                .getResultNaverMoviesByList();
        movieRepository.saveAll(movies);

        final int[] rank = {1};
        movies.stream().map(movie -> PopularMovie.builder()
                        .rank(rank[0]++)
                        .movie(movie)
                        .build())
                .forEach(popularMovieRepository::save);

        return movies;
    }
}
