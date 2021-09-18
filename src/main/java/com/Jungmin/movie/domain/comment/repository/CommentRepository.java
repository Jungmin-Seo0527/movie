package com.Jungmin.movie.domain.comment.repository;

import com.Jungmin.movie.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
