package com.Jungmin.movie.domain.user.repository;

import com.Jungmin.movie.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
