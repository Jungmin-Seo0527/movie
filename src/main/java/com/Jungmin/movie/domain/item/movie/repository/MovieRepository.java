package com.Jungmin.movie.domain.item.movie.repository;

import com.Jungmin.movie.domain.item.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);

    @Query("select m from Movie m where m.title = :title and m.genre = :genre and m.url = :url")
    Optional<Movie> findMovie(@Param("title") String title,
                              @Param("genre") String genre,
                              @Param("url") String url);
}
