package com.Jungmin.movie.domain.item.movie.service;

import com.Jungmin.movie.domain.item.movie.PopularMovie;
import com.Jungmin.movie.domain.item.movie.crawling.GoogleMovieCrawling;
import com.Jungmin.movie.domain.item.movie.Movie;
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
    private final GoogleMovieCrawling movieCrawling;

    @Transactional
    public List<PopularMovie> refreshPopularList() throws InterruptedException {
        List<PopularMovie> movies = movieCrawling.scrapingSource();
        return popularMovieRepository.saveAll(movies);
    }
}
