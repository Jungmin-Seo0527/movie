package com.Jungmin.movie.domain.item.movie.repository;

import com.Jungmin.movie.domain.item.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
