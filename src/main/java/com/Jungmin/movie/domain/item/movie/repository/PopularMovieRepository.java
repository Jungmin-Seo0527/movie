package com.Jungmin.movie.domain.item.movie.repository;

import com.Jungmin.movie.domain.item.movie.PopularMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularMovieRepository extends JpaRepository<PopularMovie, Long> {
}
