package com.Jungmin.movie.domain.item.movie.service;

import com.Jungmin.movie.domain.comment.Comment;
import com.Jungmin.movie.domain.comment.repository.CommentRepository;
import com.Jungmin.movie.domain.item.movie.Exception.MovieNotFoundException;
import com.Jungmin.movie.domain.item.movie.Movie;
import com.Jungmin.movie.domain.item.movie.Platform;
import com.Jungmin.movie.domain.item.movie.PopularMovie;
import com.Jungmin.movie.domain.item.movie.crawling.MovieCrawling;
import com.Jungmin.movie.domain.item.movie.dto.RequestMovieCommentDto;
import com.Jungmin.movie.domain.item.movie.repository.MovieRepository;
import com.Jungmin.movie.domain.item.movie.repository.PopularMovieRepository;
import com.Jungmin.movie.domain.user.User;
import com.Jungmin.movie.domain.user.exception.NonExistentUserException;
import com.Jungmin.movie.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final PopularMovieRepository popularMovieRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final MovieCrawling movieCrawling;

    // TODO: 2021-09-23 리펙토링 
    @Transactional
    public List<Movie> refreshGooglePopularMovies() throws InterruptedException {
        List<Movie> movies = movieCrawling.connectUrl(Platform.GOOGLE)
                .scrollDownToBottom()
                .scrapingSource()
                .getResultByList();

        List<Movie> saveMovie = new ArrayList<>();
        List<Movie> movies2 = new ArrayList<>();

        checkDuplicatedMovies(movies, saveMovie, movies2);

        log.info("Insert 쿼리문");
        movieRepository.saveAll(saveMovie);

        rankingMovies(movies2);
        return movies;
    }

    private void rankingMovies(List<Movie> movies2) {
        popularMovieRepository.deleteAll();
        final int[] rank = {1};
        movies2.stream().map(movie -> PopularMovie.builder()
                        .rank(rank[0]++)
                        .movie(movie)
                        .build())
                .forEach(popularMovieRepository::save);
    }

    private void checkDuplicatedMovies(List<Movie> movies, List<Movie> saveMovie, List<Movie> movies2) {
        log.info("이미 DB에 저장되어 있는 영화 정보 확인");
        for (Movie movie : movies) {
            movieRepository.findMovie(movie.getTitle(), movie.getGenre(), movie.getUrl())
                    .ifPresentOrElse(
                            (m) -> {
                                m.pricingChange(movie.getPrice());
                                movies2.add(m);
                            },
                            () -> {
                                saveMovie.add(movie);
                                movies2.add(movie);
                            }
                    );
        }
    }

    @Transactional
    public List<Movie> refreshNaverPopularMovies() {
        List<Movie> movies = movieCrawling.connectUrl(Platform.NAVER)
                .scrapingNaverHtml()
                .getResultNaverMoviesByList();
        movieRepository.saveAll(movies);

        rankingMovies(movies);

        return movies;
    }

    @Transactional
    public Long writeComment(RequestMovieCommentDto commentDto) {
        User user = userRepository.findById(commentDto.getUserId()).orElseThrow(NonExistentUserException::new);
        Movie movie = movieRepository.findById(commentDto.getMovieId()).orElseThrow(MovieNotFoundException::new);
        return commentRepository.save(Comment.createComment(user, movie, commentDto.getContents()))
                .getId();
    }
}
