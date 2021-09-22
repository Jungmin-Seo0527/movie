package com.Jungmin.movie.domain.item.movie.service;

import com.Jungmin.movie.domain.comment.Comment;
import com.Jungmin.movie.domain.comment.repository.CommentRepository;
import com.Jungmin.movie.domain.item.movie.Exception.MovieNotFoundException;
import com.Jungmin.movie.domain.item.movie.Movie;
import com.Jungmin.movie.domain.item.movie.PopularMovie;
import com.Jungmin.movie.domain.item.movie.dto.RequestMovieCommentDto;
import com.Jungmin.movie.domain.item.movie.repository.MovieRepository;
import com.Jungmin.movie.domain.item.movie.repository.PopularMovieRepository;
import com.Jungmin.movie.domain.user.User;
import com.Jungmin.movie.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MovieServiceTest {

    @Autowired
    MovieService movieService;

    @Autowired MovieRepository movieRepository;
    @Autowired PopularMovieRepository popularMovieRepository;
    @Autowired UserRepository userRepository;
    @Autowired CommentRepository commentRepository;


    @Test
    @DisplayName("테스트 01. 영화 크롤링 테스트: 구글 = 200개, 네이버 = 100개")
    public void movieCrawlingTest() throws InterruptedException {
        // given
        List<Movie> googlePopularMovies = movieService.refreshGooglePopularMovies();
        List<Movie> naverPopularMovies = movieService.refreshNaverPopularMovies();

        // when


        // then
        assertThat(googlePopularMovies.size()).isEqualTo(200);
        assertThat(naverPopularMovies.size()).isEqualTo(100);
    }

    @Test
    @DisplayName("테스트 02. Movie 테이블의 데이터와 PopularMovie 테이블 연관관계 테스트 - 일대일 단방향 연관관계: PupularMovie테이블의 movie객체의 ID가 Movie테이블의 같은 제목의 객체의 ID와 같다.")
    public void movieAndPopularMovieRelationTest() throws InterruptedException {
        // given
        movieService.refreshGooglePopularMovies();

        // when
        PopularMovie popularMovie = popularMovieRepository.findByRank(1);
        Long movieIdByPopularRepository = popularMovie.getMovie().getId();
        Long movieIdByMovieRepository = movieRepository.findByTitle(popularMovie.getMovie().getTitle()).orElseThrow(MovieNotFoundException::new).getId();

        // then
        assertThat(movieIdByMovieRepository).isEqualTo(movieIdByPopularRepository);
    }

    @Test
    @DisplayName("테스트 03. Movie 에 댓글 달기 - 연관관계 테스트 Movie에 댓글을 남기고 저장후에 댓글을 저장소에서 찾고 연관관계에 있는 Movie와 User 확인")
    public void commentTest() {
        // given
        User user = User.builder()
                .username("서정민")
                .build();
        Movie movie = Movie.builder()
                .title("서정민이 온다.")
                .build();

        userRepository.save(user);
        movieRepository.save(movie);

        String content = "재미있다";
        RequestMovieCommentDto commentDto = RequestMovieCommentDto.builder()
                .userId(user.getId())
                .movieId(movie.getId())
                .contents(content)
                .build();

        // when
        Long commentId = movieService.writeComment(commentDto);
        Comment comment = commentRepository.findById(commentId).orElseThrow(NullPointerException::new);


        // then
        assertThat(user.getComments()).contains(comment);
        assertThat(movie.getComments()).contains(comment);
        assertThat(comment.getMovie()).isSameAs(movie);
        assertThat(comment.getAuthor()).isSameAs(user);
        assertThat(comment.getContent()).isSameAs(content);
    }
}