package com.Jungmin.movie.domain.item.movie.service;

import com.Jungmin.movie.domain.comment.Comment;
import com.Jungmin.movie.domain.comment.repository.CommentRepository;
import com.Jungmin.movie.domain.item.movie.Exception.NonExistentMovieException;
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

    // TODO: 2021-09-19 새롭게 갱신된 인기 영화 목록중 이미 Movie 테이블에 존재하는 영화 처리  - 영화 속성중 불변인 것들에 대한 hashcode, equals 메소드 재정의: dirty checking
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

    @Transactional
    public Long writeComment(RequestMovieCommentDto commentDto) {
        User user = userRepository.findById(commentDto.getUserId()).orElseThrow(NonExistentUserException::new);
        Movie movie = movieRepository.findById(commentDto.getMovieId()).orElseThrow(NonExistentMovieException::new);
        return commentRepository.save(Comment.createComment(user, movie, commentDto.getContents()))
                .getId();
    }
}
