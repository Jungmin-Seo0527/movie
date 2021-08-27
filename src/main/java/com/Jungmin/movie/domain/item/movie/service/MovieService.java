package com.Jungmin.movie.domain.item.movie.service;

import com.Jungmin.movie.crawling.MovieCrawling;
import com.Jungmin.movie.domain.item.movie.Movie;
import com.Jungmin.movie.domain.item.movie.repository.MovieRepository;
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
    private final MovieCrawling movieCrawling;

    @Transactional
    public List<Movie> refreshPopularList() throws InterruptedException {
        List<Movie> movies = movieCrawling.scrapingSource();
        return movieRepository.saveAll(movies);
    }
}
