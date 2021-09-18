package com.Jungmin.movie.domain.item.movie.service;

import com.Jungmin.movie.domain.item.movie.PopularMovie;
import com.Jungmin.movie.domain.item.movie.repository.MovieRepository;
import com.Jungmin.movie.domain.item.movie.repository.PopularMovieRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MovieServiceTest {

    @Autowired
    MovieService movieService;

    @Autowired MovieRepository movieRepository;
    @Autowired PopularMovieRepository popularMovieRepository;


    @Test
    @DisplayName("테스트 01. Movie 테이블의 데이터와 PopularMovie 테이블 연관관계 테스트 - 일대일 단방향 연관관계: PupularMovie테이블의 movie객체의 ID가 Movie테이블의 같은 제목의 객체의 ID와 같다.")
    public void movieAndPopularMovieRelationTest() throws InterruptedException {
        // given
        movieService.refreshGooglePopularMovies();

        // when
        PopularMovie popularMovie = popularMovieRepository.findByRank(1);
        Long movieIdByPopularRepository = popularMovie.getMovie().getId();
        Long movieIdByMovieRepository = movieRepository.findByTitle(popularMovie.getMovie().getTitle()).getId();

        // then
        Assertions.assertThat(movieIdByMovieRepository).isEqualTo(movieIdByPopularRepository);
    }
}